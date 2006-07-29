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
 * PhysicalPageBox.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PhysicalPageBox.java,v 1.7 2006/07/27 17:56:27 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.page;

import org.jfree.layouting.input.style.keys.line.VerticalAlign;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.renderer.model.EmptyBoxDefinition;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderNodeState;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.IndexedRenderBox;
import org.jfree.layouting.renderer.model.NormalFlowRenderBox;
import org.jfree.layouting.renderer.text.ExtendedBaselineInfo;

/**
 * This is behaves like a table box (but is none, as the layouting rules are
 * different).
 * <p/>
 * This is a empty prototype, the real version should have a couple of page
 * areas which contain the physical headers and footers.
 *
 * @author Thomas Morgner
 */
public class PhysicalPageBox extends RenderBox
{
  private BlockRenderBox contentArea;

  private PageContext pageContext;
  private long width;
  private long height;

  public PhysicalPageBox(final PageContext pageContext,
                         final long width,
                         final long height)
  {
    super(new EmptyBoxDefinition(), VerticalAlign.TOP);
    freeze();
    this.pageContext = pageContext;
    this.width = width;
    this.height = height;

    this.contentArea = new BlockRenderBox(new EmptyBoxDefinition(), VerticalAlign.TOP);
    addChild(contentArea);
  }

  public BlockRenderBox getContentArea()
  {
    return contentArea;
  }

  protected long getPreferredSize(int axis)
  {
    if (axis == VERTICAL_AXIS)
    {
      return height;
    }
    return width;
  }

  protected long getEffectiveLayoutSize(int axis, RenderNode node)
  {
    if (axis == VERTICAL_AXIS)
    {
      return height;
    }
    return width;
  }

  public long getWidth()
  {
    return width;
  }

  public long getHeight()
  {
    return height;
  }

  public void validate(RenderNodeState state)
  {
//    // todo..
//    setWidth(width);
//    setHeight(height);
    setState(RenderNodeState.FINISHED);
  }

  /**
   * Returns the baseline info for the given node. This can be null, if the node
   * does not have any baseline info.
   *
   * @return
   */
  public ExtendedBaselineInfo getBaselineInfo()
  {
    return null;
  }

  /**
   * This is always a split along the document's major axis. Until we have a
   * really 100% parametrized renderer model, we assume VERTICAL here and are
   * happy.
   *
   * @param position
   * @param target
   * @return
   */
  public RenderNode[] splitForPrint(long position, RenderNode[] target)
  {
    throw new UnsupportedOperationException
            ("Split is not supported for physical boxes");
  }

  public void setContentAreaBounds(final long x, final long y,
                                   final long width, final long height)
  {
    // depending on the other area's sizes, we have to adjust our own
    // origin point ..
    setX(x);
    setY(y);
    setWidth(width);
    setHeight(height);
  }

  /**
   * Derive creates a disconnected node that shares all the properties of the
   * original node. The derived node will no longer have any parent, silbling,
   * child or any other relationships with other nodes.
   *
   * @return
   */
  public RenderNode derive(boolean deepDerive)
  {
    final PhysicalPageBox renderNode = (PhysicalPageBox) super.derive(deepDerive);
    if (deepDerive)
    {
      // after the derive, reconnect the header and footer
      // ok, the structure is known, so that's easy
      renderNode.contentArea = (BlockRenderBox)
              renderNode.findNodeById(contentArea.getInstanceId());
    }
    else
    {
      renderNode.clear();
      renderNode.contentArea = (NormalFlowRenderBox) contentArea.derive(false);
      renderNode.addChild(renderNode.contentArea);
    }
    return renderNode;
  }

  
}
