/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * LogicalPageBox.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: LogicalPageBox.java,v 1.7 2006/07/26 16:59:47 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.page;

import java.util.ArrayList;
import java.util.Arrays;

import org.jfree.layouting.input.style.keys.line.VerticalAlign;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.EmptyBoxDefinition;
import org.jfree.layouting.renderer.model.IndexedRenderBox;
import org.jfree.layouting.renderer.model.NormalFlowRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderNodeState;
import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.util.Log;

/**
 * The logical page box does not have a layout at all. It has collection of
 * flows, one for each <b>logical</b> page area.
 * <p/>
 * Each logical page has a header (for the repeating group headers, not to be
 * mistaken as PageHeader!), content and footer area. The footer area contains
 * the foot-notes at the very bottom, the repeatable footer area and finally the
 * flow:bottom area.
 * <p/>
 * The flow- and repeatable areas behave like stacks, and are filled from the
 * content area (so for headers, new content goes after any previous content,
 * and for footers, new content goes before any previous content.)
 * <p/>
 * The footNotes section is always filled in the normal-order, so new content
 * gets added at the bottom of that area.
 * <p/>
 * The logical page also holds the absolutely and static positioned elements.
 * These elements may overlap the repeating headers, but will never overlap the
 * physical page header or footer.
 * <p/>
 * The logical page is also the container for all physical pages. (The sizes of
 * the physical pages influence the available space on the logical pages.)
 *
 * @author Thomas Morgner
 */
public class LogicalPageBox extends BlockRenderBox
{
  private NormalFlowRenderBox contentArea;
  private IndexedRenderBox footerArea;
  private IndexedRenderBox headerArea;
  private ArrayList subFlows;
  private PageGrid pageGrid;

  private long[] pageWidths;
  private long[] pageHeights;
  private long[] horizontalBreaks;
  private long[] verticalBreaks;
  private long pageWidth;
  private long pageHeight;


  public LogicalPageBox(final PageGrid pageGrid)
  {
    super(new EmptyBoxDefinition(), VerticalAlign.TOP);

    if (pageGrid == null)
    {
      throw new NullPointerException("PageGrid must not be null");
    }

    this.pageGrid = pageGrid;

    this.subFlows = new ArrayList();
    this.contentArea = new NormalFlowRenderBox
            (new EmptyBoxDefinition(), VerticalAlign.TOP);
    this.headerArea = new IndexedRenderBox(new EmptyBoxDefinition());
    this.footerArea = new IndexedRenderBox(new EmptyBoxDefinition());
    this.pageHeights = new long[pageGrid.getColumnCount()];
    this.pageWidths = new long[pageGrid.getRowCount()];
    this.horizontalBreaks = new long[pageGrid.getColumnCount()];
    this.verticalBreaks = new long[pageGrid.getRowCount()];
    updatePageArea();

    addChild(contentArea);

    footerArea.setElement("footnotes",
            new NormalFlowRenderBox(new EmptyBoxDefinition(), VerticalAlign.TOP));

    setMajorAxis(VERTICAL_AXIS);
    setMinorAxis(HORIZONTAL_AXIS);
  }

  private void updatePageArea()
  {
    Arrays.fill(pageHeights, Long.MAX_VALUE);
    Arrays.fill(pageWidths, Long.MAX_VALUE);

    for (int row = 0; row < pageGrid.getRowCount(); row++)
    {
      for (int col = 0; col < pageGrid.getColumnCount(); col++)
      {
        PhysicalPageBox box = pageGrid.getPage(row, col);
        pageHeights[row] = Math.min (pageHeights[row], box.getHeight());
        pageWidths[col] = Math.min (pageWidths[col], box.getWidth());
      }
    }

    pageHeight = 0;
    for (int i = 0; i < pageHeights.length; i++)
    {
      pageHeight += pageHeights[i];
      verticalBreaks[i] = pageHeight;
    }

    pageWidth = 0;
    for (int i = 0; i < pageWidths.length; i++)
    {
      pageWidth += pageWidths[i];
      horizontalBreaks[i] = pageHeight;
    }
  }

  public NormalFlowRenderBox getContentArea()
  {
    return contentArea;
  }

  public IndexedRenderBox getFooterArea()
  {
    return footerArea;
  }

  public IndexedRenderBox getHeaderArea()
  {
    return headerArea;
  }

  public void addAbsoluteFlow(NormalFlowRenderBox flow)
  {
    subFlows.add(flow);
  }

  public NormalFlowRenderBox[] getAbsoluteFlows()
  {
    return (NormalFlowRenderBox[])
            subFlows.toArray(new NormalFlowRenderBox[subFlows.size()]);
  }

  public NormalFlowRenderBox getAbsoluteFlow(int i)
  {
    return (NormalFlowRenderBox) subFlows.get(i);
  }

  public int getAbsoluteFlowCount()
  {
    return subFlows.size();
  }

  public LogicalPageBox getLogicalPage()
  {
    return this;
  }

  public NormalFlowRenderBox getNormalFlow()
  {
    return contentArea;
  }

  public PageGrid getPageGrid()
  {
    return pageGrid;
  }

  public RenderBox getInsertationPoint()
  {
    return contentArea.getInsertationPoint();
  }

  /**
   * This changes over time, whenever the outer page areas of the physical
   * pages get filled.
   *
   * @param axis
   * @return
   */
  public long getEffectiveLayoutSize(int axis)
  {
    if (axis == HORIZONTAL_AXIS)
    {
      return pageWidth;
    }
    return pageHeight;
  }

  protected long getComputedBlockContextWidth()
  {
    return getEffectiveLayoutSize(HORIZONTAL_AXIS);
  }

  public long[] getPhysicalBreaks(int axis)
  {
    if (axis == HORIZONTAL_AXIS)
    {
      return (long[]) horizontalBreaks.clone();
    }
    return (long[]) verticalBreaks.clone();
  }

  public void validate(RenderNodeState state)
  {
    if (getState() == RenderNodeState.UNCLEAN)
    {
      setX(0);
      setY(0);
      setWidth(getEffectiveLayoutSize(RenderNode.HORIZONTAL_AXIS));
      setHeight(getEffectiveLayoutSize(RenderNode.VERTICAL_AXIS));
    }

    if (state == RenderNodeState.FINISHED)
    {
      Log.debug ("");
    }
    super.validate(state);
  }

  /**
   * Checks, whether a validate run would succeed. Under certain conditions, for
   * instance if there is a auto-width component open, it is not possible to
   * perform a layout run, unless that element has been closed.
   * <p/>
   * Generally speaking: An element cannot be layouted, if <ul> <li>the element
   * contains childs, which cannot be layouted,</li> <li>the element has
   * auto-width or depends on an auto-width element,</li> <li>the element is a
   * floating or positioned element, or is a child of an floating or positioned
   * element.</li> </ul>
   *
   * @return
   */
  public boolean isValidatable()
  {
    if (isOpen() == false) return true;

    for (int i = 0; i < subFlows.size(); i++)
    {
      NormalFlowRenderBox box = (NormalFlowRenderBox) subFlows.get(i);
      if (box.isOpen())
      {
        return false;
      }
    }

    RenderNode child = getLastChild();
    while (child != null)
    {
      if (child.isValidatable() == false)
      {
        return false;
      }
      child = child.getPrev();
    }

    return true;
  }
}
