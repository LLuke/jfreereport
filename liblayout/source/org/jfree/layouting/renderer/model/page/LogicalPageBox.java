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
 * $Id: LogicalPageBox.java,v 1.8 2006/07/27 17:56:27 taqua Exp $
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
import org.jfree.layouting.util.geom.StrictInsets;
import org.jfree.layouting.output.pageable.graphics.PageDrawable;
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
  private ArrayList subFlows;
  private PageGrid pageGrid;

  private long[] pageWidths;
  private long[] pageHeights;
  private long[] horizontalBreaks;
  private long[] verticalBreaks;
  private long pageWidth;
  private long pageHeight;

  private long offset;
  private Object contentAreaId;

  public LogicalPageBox(final PageGrid pageGrid)
  {
    super(new EmptyBoxDefinition(), VerticalAlign.TOP);

    if (pageGrid == null)
    {
      throw new NullPointerException("PageGrid must not be null");
    }

    this.pageGrid = pageGrid;

    this.subFlows = new ArrayList();
    NormalFlowRenderBox contentArea = new NormalFlowRenderBox
            (new EmptyBoxDefinition(), VerticalAlign.TOP);
    contentAreaId = contentArea.getInstanceId();
//    this.headerArea = new IndexedRenderBox(new EmptyBoxDefinition());
//    this.footerArea = new IndexedRenderBox(new EmptyBoxDefinition());
    this.pageHeights = new long[pageGrid.getColumnCount()];
    this.pageWidths = new long[pageGrid.getRowCount()];
    this.horizontalBreaks = new long[pageGrid.getColumnCount()];
    this.verticalBreaks = new long[pageGrid.getRowCount()];
    updatePageArea();

//    addChild(headerArea);
    super.addChild(contentArea);
//    addChild(footerArea);
//
//    footerArea.setElement("footnotes",
//            new NormalFlowRenderBox(new EmptyBoxDefinition(), VerticalAlign.TOP));

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
        pageHeights[row] = Math.min(pageHeights[row], box.getHeight());
        pageWidths[col] = Math.min(pageWidths[col], box.getWidth());
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

  protected void validatePaddings()
  {
    StrictInsets paddings = getPaddingsInternal();
    paddings.setTop(offset);
    paddings.setBottom(0);
    paddings.setLeft(0);
    paddings.setRight(0);
  }

  public boolean isLeadingMarginIndependent(int axis)
  {
    return true;
  }

  public boolean isTrailingMarginIndependent(int axis)
  {
    return true;
  }

  public NormalFlowRenderBox getContentArea()
  {
    return (NormalFlowRenderBox) findNodeById(contentAreaId);
  }

//  public IndexedRenderBox getFooterArea()
//  {
//    return footerArea;
//  }
//
//  public IndexedRenderBox getHeaderArea()
//  {
//    return headerArea;
//  }
//
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
    return getContentArea();
  }

  public PageGrid getPageGrid()
  {
    return pageGrid;
  }

  public RenderBox getInsertationPoint()
  {
    return getContentArea().getInsertationPoint();
  }

  /**
   * This changes over time, whenever the outer page areas of the physical pages
   * get filled.
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
      Log.debug("");
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
    if (isOpen() == false)
    {
      return true;
    }

    for (int i = 0; i < subFlows.size(); i++)
    {
      NormalFlowRenderBox box = (NormalFlowRenderBox) subFlows.get(i);
      if (box.isOpen())
      {
        Log.debug ("Not validatable: Subflow is open");
        return false;
      }
    }

    // the header and footer are ignored, as they cannot be filled directly.
    return getContentArea().isValidatable();
  }

  public boolean isOverflow()
  {
    if (isValidatable() == false)
    {
      throw new IllegalStateException();
    }

    validate(RenderNodeState.FINISHED);
    if (getHeight() > pageHeight)
    {
      return true;
    }
    Log.debug ("Overflow test: " + getHeight() + " <= " + pageHeight);
    return false;
  }

  /**
   * Performs a split on an overly full logical pagebox. The current set of
   * physical pageboxes will be filled with the content, and that content will
   * then be removed from the flow.
   *
   * It is assumed, that the vertical splits are safe to use - the content
   * should have been layouted in a safe way already. (If not, its a bug!)
   *
   * So the only critical operations will be the horizontal splits.
   *
   * @return a result object, which contains the new logical page box and the
   * generated physical pages.
   */
  public PrintSplitResult splitForPrint()
  {
    ArrayList physicalPages = new ArrayList();
    LogicalPageBox splitBox = this;
    for (int row = 0; row < pageHeights.length; row++)
    {
      // this is the height of the content area ...
      final long height = pageHeights[row];

      final long y;
      if (row == 0)
      {
        y = 0;
      }
      else
      {
        y = verticalBreaks[row - 1];
      }

      RenderNode[] targets = splitBox.splitForPrint(height, null);
      targets[0].freeze();
      LogicalPageBox oldPage;
      if (targets[0] instanceof LogicalPageBox == false)
      {
        oldPage = (LogicalPageBox) derive(false);
      }
      else
      {
        oldPage = (LogicalPageBox) targets[0];
      }

      // The old page contains all content that must be distributed to the
      // physical pages...
      for (int col = 0; col < pageWidths.length; col++)
      {
        final long width = pageWidths[col];

        final long x;
        if (col == 0)
        {
          x = 0;
        }
        else
        {
          x = horizontalBreaks[col - 1];
        }

        final PhysicalPageBox page = (PhysicalPageBox)
                pageGrid.getPage(row, col).derive(true);
        physicalPages.add(page);

        page.setContentAreaBounds(x, y, width, height);
        final RenderBox contentArea = page.getContentArea();
        // now copy the content from the derived logical page to the physical
        // page.
        RenderNode node = oldPage.getFirstChild();
        while (node != null)
        {
          Log.debug ("Adding " + node);
          contentArea.addChild(node.derive(true));
          node = node.getNext();
        }
      }



      Log.debug ("HERE");
      // the second page part contains the content below the split point.
      if (targets[1] == null)
      {
        splitBox = (LogicalPageBox) derive(false);
      }
      else
      {
        splitBox = (LogicalPageBox) targets[1];
      }
    }

    PhysicalPageBox[] pages = (PhysicalPageBox[])
            physicalPages.toArray(new PhysicalPageBox[physicalPages.size()]);

    for (int i = 0; i < pages.length; i++)
    {
      Log.debug ("----------------------------------------------------------");
      PhysicalPageBox page = pages[i];
      PageDrawable pd = new PageDrawable(page);
      pd.print();
    }
    splitBox.restore();
    if (splitBox.getContentArea() == null)
    {
      throw new IllegalStateException();
    }

    splitBox.getInsertationPoint();
    return new PrintSplitResult(splitBox, pages);
  }

  /**
   * Restores a sane state after the split removed the already processed content.
   * This basicly resets the header/footer area and moves the content window.
   */
  private void restore()
  {
    offset += pageHeight;
    if (getContentArea() == null)
    {
      NormalFlowRenderBox contentArea = new NormalFlowRenderBox
              (new EmptyBoxDefinition(), VerticalAlign.TOP);
      contentAreaId = contentArea.getInstanceId();
      addGeneratedChild(contentArea);
    }
  }

  /**
   * Clones this node. Be aware that cloning can get you into deep trouble, as
   * the relations this node has may no longer be valid.
   *
   * @return
   */
  public Object clone()
  {
    final LogicalPageBox o = (LogicalPageBox) super.clone();
    o.pageHeights = (long[]) pageHeights.clone();
    o.pageWidths = (long[]) pageWidths.clone();
    o.pageGrid = pageGrid;
    return o;
  }

  public boolean isNormalFlowActive()
  {
    for (int i = 0; i < subFlows.size(); i++)
    {
      NormalFlowRenderBox box = (NormalFlowRenderBox) subFlows.get(i);
      if (box.isOpen())
      {
        return false;
      }
    }
    return true;
  }
}
