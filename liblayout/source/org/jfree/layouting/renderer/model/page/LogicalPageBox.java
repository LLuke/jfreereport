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
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.page;

import java.util.ArrayList;

import org.jfree.layouting.renderer.page.PageGrid;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.NormalFlowRenderBox;
import org.jfree.layouting.renderer.model.IndexedRenderBox;
import org.jfree.layouting.renderer.model.EmptyBoxDefinition;
import org.jfree.layouting.renderer.model.RenderBox;

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
 *
 * The logical page is also the container for all physical pages. (The sizes
 * of the physical pages influence the available space on the logical pages.) 
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

  public LogicalPageBox(final PageGrid pageGrid)
  {
    super(new EmptyBoxDefinition());

    if (pageGrid == null)
    {
      throw new NullPointerException("PageGrid must not be null");
    }

    this.pageGrid = pageGrid;
    this.subFlows = new ArrayList();
    this.contentArea = new NormalFlowRenderBox(new EmptyBoxDefinition());
    this.headerArea = new IndexedRenderBox(new EmptyBoxDefinition());
    this.footerArea = new IndexedRenderBox(new EmptyBoxDefinition());

    addChild(headerArea);
    addChild(contentArea);
    addChild(footerArea);

    footerArea.setElement("footnotes",
            new NormalFlowRenderBox(new EmptyBoxDefinition()));

    setMajorAxis(VERTICAL_AXIS);
    setMinorAxis(HORIZONTAL_AXIS);
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
}
