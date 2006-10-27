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
 * RenderNode.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: RenderNode.java,v 1.19 2006/10/22 14:58:25 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;

/**
 * A node of the rendering model. The renderer model keeps track of the
 * rendering state - what has already been rendered, what is pending, etc.
 * Things that have been rendered completely get removed from the model.
 * <p/>
 * The default model makes some assumptions about the node processing:
 * <p/>
 * <ul> <li>Once placed somewhere, nodes will never move anywhere else</li>
 * <li>Once size computation is complete, nodes will not be modified
 * anymore.</li> </ul>
 * <p/>
 * Finished nodes can be removed as soon as they have been physically rendered.
 * <p/>
 * In case a node cannot be rendered right now (think of tables, which need all
 * cells before a row or in some cases even the columns can be computed), a cell
 * receives the 'pending' flag. If a cell is added to a parent which is pending,
 * the cell is pending as well. The pending state must be resolved by the
 * outer-most element. An element cannot be finished or removed unless it
 * resolved the pending state.
 *
 * @author Thomas Morgner
 */
public abstract class RenderNode implements Cloneable
{
  public static final int HORIZONTAL_AXIS = 0;
  public static final int VERTICAL_AXIS = 1;

  private RenderBox parent;
  private RenderNode prev;
  private RenderNode next;

  private long changeTracker;

  // The element's positions.
  private long width;
  private long height;
  private long x;
  private long y;

  private int majorAxis;
  private int minorAxis;

  private boolean frozen;
  private Object instanceId;

  private NodeLayoutProperties layoutProperties;
  private CSSValue alignmentBaseline;
  private CSSValue alignmentAdjust;
  private CSSValue baselineShift;
  private CSSValue verticalAlignment;
  private RenderLength baselineShiftResolved;
  private RenderLength alignmentAdjustResolved;

  private long stickyMarker;

  public RenderNode()
  {
    this.instanceId = new Object();
    this.layoutProperties = new NodeLayoutProperties();
  }

  public void appyStyle(LayoutContext context, OutputProcessorMetaData metaData)
  {
    alignmentBaseline = context.getStyle().getValue(LineStyleKeys.ALIGNMENT_BASELINE);
    alignmentAdjust = context.getStyle().getValue(LineStyleKeys.ALIGNMENT_ADJUST);
    if (alignmentAdjust instanceof CSSNumericValue)
    {
      alignmentAdjustResolved = RenderLength.convertToInternal
          (alignmentAdjust, context, metaData);
    }
    baselineShift = context.getStyle().getValue(LineStyleKeys.BASELINE_SHIFT);
    if (baselineShift instanceof CSSNumericValue)
    {
      baselineShiftResolved = RenderLength.convertToInternal
          (baselineShift, context, metaData);
    }

    verticalAlignment = normalizeAlignment
        (context.getStyle().getValue(LineStyleKeys.VERTICAL_ALIGN));

  }

  protected CSSValue normalizeAlignment(CSSValue verticalAlignment)
  {
    return verticalAlignment;
  }

  public CSSValue getVerticalAlignment()
  {
    return verticalAlignment;
  }

  public RenderLength getBaselineShiftResolved()
  {
    return baselineShiftResolved;
  }

  public CSSValue getAlignmentBaseline()
  {
    return alignmentBaseline;
  }

  public CSSValue getBaselineShift()
  {
    return baselineShift;
  }

  public CSSValue getAlignmentAdjust()
  {
    return alignmentAdjust;
  }

  public RenderLength getAlignmentAdjustResolved()
  {
    return alignmentAdjustResolved;
  }

  public PageContext getPageContext()
  {
    final RenderBox parent = getParent();
    if (parent != null)
    {
      parent.getPageContext();
    }
    return null;
  }

  public Object getInstanceId()
  {
    return instanceId;
  }

  public int getMajorAxis()
  {
    return majorAxis;
  }

  protected void setMajorAxis(final int majorAxis)
  {
    this.majorAxis = majorAxis;
  }

  public int getMinorAxis()
  {
    return minorAxis;
  }

  protected void setMinorAxis(final int minorAxis)
  {
    this.minorAxis = minorAxis;
    this.updateChangeTracker();
  }

  public void setWidth(long width)
  {
    if (width < 0)
    {
      throw new IllegalArgumentException("Width cannot be negative: " + width);
    }

    this.width = width;
    this.updateChangeTracker();
  }

  public long getWidth()
  {
    return width;
  }

  public void setHeight(long height)
  {
    if (height < 0)
    {
      throw new IllegalArgumentException("Height cannot be negative: " + height);
    }
    this.height = height;
//    this.updateChangeTracker();
  }

  public long getHeight()
  {
    return this.height;
  }

  public long getX()
  {
    return x;
  }

  public void setX(final long x)
  {
    this.x = x;
    this.updateChangeTracker();
  }

  public final void setPosition(int axis, long value)
  {
    if (axis == HORIZONTAL_AXIS)
    {
      setX(value);
    }
    else
    {
      setY(value);
    }
  }

  public final long getPosition(int axis)
  {
    if (axis == HORIZONTAL_AXIS)
    {
      return getX();
    }
    else
    {
      return getY();
    }
  }

  public final void setDimension(int axis, long value)
  {
    if (axis == HORIZONTAL_AXIS)
    {
      setWidth(value);
    }
    else
    {
      setHeight(value);
    }
  }

  public final long getDimension(int axis)
  {
    if (axis == HORIZONTAL_AXIS)
    {
      return getWidth();
    }
    else
    {
      return getHeight();
    }
  }

  public long getY()
  {
    return y;
  }

  public void setY(final long y)
  {
    this.y = y;
  }

  public RenderBox getParent()
  {
    return parent;
  }

  protected void setParent(final RenderBox parent)
  {
    // Object oldParent = this.parent;
    this.parent = parent;
    if (parent != null && prev == parent)
    {
      throw new IllegalStateException();
    }

  }

  public RenderNode getPrev()
  {
    return prev;
  }

  protected void setPrev(final RenderNode prev)
  {
    this.prev = prev;
    if (prev != null && prev == parent)
    {
      throw new IllegalStateException();
    }
  }

  public RenderNode getNext()
  {
    return next;
  }

  protected void setNext(final RenderNode next)
  {
    this.next = next;
  }

  public LogicalPageBox getLogicalPage()
  {
    final RenderNode parent = getParent();
    if (parent != null)
    {
      return parent.getLogicalPage();
    }
    return null;
  }

  public NormalFlowRenderBox getNormalFlow()
  {
    final RenderNode parent = getParent();
    if (parent != null)
    {
      return parent.getNormalFlow();
    }
    return null;
  }

  /**
   * Clones this node. Be aware that cloning can get you into deep trouble, as
   * the relations this node has may no longer be valid.
   *
   * @return
   */
  public Object clone()
  {
    try
    {
      final RenderNode renderNode = (RenderNode) super.clone();
      //renderNode.layoutProperties = (NodeLayoutProperties) layoutProperties.clone();
      return renderNode;
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException("Clone failed for some reason.");
    }
  }

  /**
   * Derive creates a disconnected node that shares all the properties of the
   * original node. The derived node will no longer have any parent, silbling,
   * child or any other relationships with other nodes.
   *
   * @return
   */
  public RenderNode derive(boolean deep)
  {
    RenderNode node = (RenderNode) clone();
    node.parent = null;
    node.next = null;
    node.prev = null;
    return node;
  }

  public RenderNode deriveFrozen(boolean deep)
  {
    RenderNode node = (RenderNode) clone();
    node.parent = null;
    node.next = null;
    node.prev = null;
    node.frozen = true;
    return node;
  }

  public boolean isFrozen()
  {
    return frozen;
  }

  public RenderNode findNodeById(Object instanceId)
  {
    if (instanceId == getInstanceId())
    {
      return this;
    }
    return null;
  }

  public boolean isOpen()
  {
    return false;
  }

  public boolean isEmpty()
  {
    return false;
  }

  public RenderBox getParentBlockContext()
  {
    if (parent == null)
    {
      return null;
    }
    if (parent instanceof BlockRenderBox)
    {
      return parent;
    }
    return parent.getParentBlockContext();
  }


  public boolean isDiscardable()
  {
    return false;
  }

  /**
   * If that method returns true, the element will not be used for rendering.
   * For the purpose of computing sizes or performing the layouting (in the
   * validate() step), this element will treated as if it is not there.
   * <p/>
   * If the element reports itself as non-empty, however, it will affect the
   * margin computation.
   *
   * @return
   */
  public boolean isIgnorableForRendering()
  {
    return isEmpty();
  }

  /**
   * Returns the baseline info for the given node. This can be null, if the node
   * does not have any baseline info at all. If the element has more than one
   * set of baselines, the baseline of the first element is returned.
   *
   * @return
   */
//  public abstract ExtendedBaselineInfo getBaselineInfo();
  public NodeLayoutProperties getNodeLayoutProperties()
  {
    return layoutProperties;
  }

  public void freeze()
  {
    frozen = true;
  }


  public boolean isDirectionLTR()
  {
    return true;
  }

  public synchronized void updateChangeTracker()
  {
    changeTracker += 1;
    if (parent != null)
    {
      parent.updateChangeTracker();
    }
  }

  public synchronized long getChangeTracker()
  {
    return changeTracker;
  }

  public long getStickyMarker()
  {
    return stickyMarker;
  }

  public void setStickyMarker(final long stickyMarker)
  {
    this.stickyMarker = stickyMarker;
  }
}
