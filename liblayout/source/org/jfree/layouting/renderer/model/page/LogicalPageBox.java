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
 * $Id: LogicalPageBox.java,v 1.10 2006/10/17 16:39:08 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.page;

import java.util.ArrayList;
import java.util.Arrays;

import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.EmptyBoxDefinition;
import org.jfree.layouting.renderer.model.IndexedRenderBox;
import org.jfree.layouting.renderer.model.NormalFlowRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.output.OutputProcessorMetaData;

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

  //private long offset;
  private Object contentAreaId;
  private IndexedRenderBox footerArea;
  private IndexedRenderBox headerArea;

  public LogicalPageBox(final PageGrid pageGrid)
  {
    super(new EmptyBoxDefinition());

    if (pageGrid == null)
    {
      throw new NullPointerException("PageGrid must not be null");
    }

    this.subFlows = new ArrayList();
    NormalFlowRenderBox contentArea =
        new NormalFlowRenderBox (new EmptyBoxDefinition());
    this.contentAreaId = contentArea.getInstanceId();
    this.headerArea = new IndexedRenderBox(new EmptyBoxDefinition());
    this.footerArea = new IndexedRenderBox(new EmptyBoxDefinition());

    updatePageArea(pageGrid);

    addChild(headerArea);
    addChild(contentArea);
    addChild(footerArea);

    final NormalFlowRenderBox footNotes =
            new NormalFlowRenderBox(new EmptyBoxDefinition());
    footNotes.close();
    footerArea.setElement("footnotes", footNotes);

    setMajorAxis(VERTICAL_AXIS);
    setMinorAxis(HORIZONTAL_AXIS);
  }

  public void appyStyle(LayoutContext context, OutputProcessorMetaData metaData)
  {
    headerArea.appyStyle(context, metaData);
    footerArea.appyStyle(context, metaData);
    final RenderBox element = (RenderBox) footerArea.getElement("footnotes");
    element.appyStyle(context, metaData);
    getContentArea().appyStyle(context, metaData);
  }

  public void updatePageArea(PageGrid pageGrid)
  {
    this.pageGrid = pageGrid;
    this.pageHeights = new long[pageGrid.getColumnCount()];
    this.pageWidths = new long[pageGrid.getRowCount()];
    this.horizontalBreaks = new long[pageGrid.getColumnCount()];
    this.verticalBreaks = new long[pageGrid.getRowCount()];

    Arrays.fill(pageHeights, Long.MAX_VALUE);
    Arrays.fill(pageWidths, Long.MAX_VALUE);

    // todo: This is invalid right now. The page grids content area must be used

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

  public NormalFlowRenderBox getContentArea()
  {
    return (NormalFlowRenderBox) findNodeById(contentAreaId);
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

  public long[] getPhysicalBreaks(int axis)
  {
    if (axis == HORIZONTAL_AXIS)
    {
      return (long[]) horizontalBreaks.clone();
    }
    return (long[]) verticalBreaks.clone();
  }

  public boolean isOverflow()
  {
    return false;
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

  public long getPageHeight()
  {
    return pageHeight;
  }

  public long getPageWidth()
  {
    return pageWidth;
  }
}
