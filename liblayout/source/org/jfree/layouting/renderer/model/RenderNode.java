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
 * $Id: RenderNode.java,v 1.4 2006/07/14 14:34:41 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.util.geom.StrictInsets;
import org.jfree.util.Log;

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
  /**
   * Never do any breaking. This is the case for any element that does not
   * support any breaks. A table for instance does not support breaks.
   */
  public static final int UNBREAKABLE = 0;

  /**
   * Allow breaks. The rendernode itself is responsible for placing the elements
   * in a suitable way, so that unbreakable elements do not cross any breaks.
   * (This is a strong should - in case of overlong words, there is no choice
   * sometimes. In that case, the word is broken, and both parts do not cross
   * the boundary. One lies left, the other right of it.)
   * <p/>
   * Boxes are generally soft-breakable. They reserve space for each crossed
   * pagebreak and enforce the above rule to all childs.
   */
  public static final int SOFT_BREAKABLE = 1;

  /**
   * Hard-Breakable elements do not maintain softbreaks. They need to be split
   * on softbreaks. Ordinary text is the most famous example on that. By clever
   * breaking we stay clear of the inner pagebreaks and thus make it easier to
   * derive the physical pages later *and* to reflow the layout if ever
   * necessary.
   */
  public static final int HARD_BREAKABLE = 2;

  public static final int VALID_MASK = 0x0F;

  public static final int HORIZONTAL_AXIS = 0;
  public static final int VERTICAL_AXIS = 1;

  private RenderNodeState state;

  private RenderBox parent;
  private RenderNode prev;
  private RenderNode next;

  // The element's positions.
  private long width;
  private long height;
  private long x;
  private long y;

  private Long parentWidth;

  private int majorAxis;
  private int minorAxis;

  private boolean killMePlease;
  private boolean clearRight;
  private boolean clearLeft;
  private Object instanceId;
  private boolean open;

  public RenderNode()
  {
    instanceId = new Object();
    open = true;
    state = RenderNodeState.UNCLEAN;


    this.absoluteMargins = new StrictInsets();
    this.effectiveMargins = new StrictInsets();

  }

  public Object getInstanceId()
  {
    return instanceId;
  }

  public void performSuicide()
  {
    killMePlease = true;
  }

  public boolean isKillMePlease()
  {
    return killMePlease;
  }

  public int getMajorAxis()
  {
    return majorAxis;
  }

  public void setMajorAxis(final int majorAxis)
  {
    this.majorAxis = majorAxis;
  }

  public int getMinorAxis()
  {
    return minorAxis;
  }

  public void setMinorAxis(final int minorAxis)
  {
    this.minorAxis = minorAxis;
  }

  public RenderNodeState getState()
  {
    return state;
  }


  public void setWidth(long width)
  {
    if (width < 0)
    {
      throw new IllegalArgumentException("Width cannot be negative: " + width);
    }

    long oldValue = this.width;
    this.width = width;

    if (oldValue != width)
    {
      // someone changed the position, invalidate all childs ..
      setState(RenderNodeState.PENDING);
    }
  }

  public long getWidth()
  {
    return width;
  }

  public void setHeight(long height)
  {
    if (height < 0)
    {
      throw new IllegalArgumentException("Width cannot be negative");
    }

    long oldValue = this.height;
    this.height = height;

    if (oldValue != height)
    {
      // someone changed the position, invalidate all childs ..
      setState(RenderNodeState.PENDING);
    }
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
    long oldValue = this.x;
    this.x = x;

    if (oldValue != x)
    {
      // someone changed the position, invalidate all childs ..
      setState(RenderNodeState.UNCLEAN);
    }
  }

  public final void setPosition (int axis, long value)
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

  public final long getPosition (int axis)
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

  public final void setDimension (int axis, long value)
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

  public final long getDimension (int axis)
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
    long oldValue = this.y;
    this.y = y;

    if (oldValue != y)
    {
      // someone changed the position, invalidate all childs ..
      setState(RenderNodeState.UNCLEAN);
    }
  }

  protected final void setState(final RenderNodeState state)
  {
    RenderNodeState oldState = this.state;
    this.state = state;
    if (this.state != oldState)
    {
      notifyStateChange(oldState, state);
    }
  }

  protected void notifyStateChange(final RenderNodeState oldState,
                                   final RenderNodeState newState)
  {
//    Log.debug("STATE_CHANGE: " + toString() + ": " + oldState + " -> " + newState);
    if (newState == RenderNodeState.UNCLEAN)
    {
      invalidateMargins();
      final RenderNode next = getNext();
      if (next != null)
      {
        // the y- or x-position may have changed. We have to rebuild
        // everything from scratch.
        next.setState(RenderNodeState.UNCLEAN);
      }
      else
      {
        final RenderNode parent = getParent();
        if (parent != null)
        {
          parent.setState(RenderNodeState.PENDING);
        }
      }
    }
    else if (newState == RenderNodeState.PENDING)
    {
      invalidateMargins();
      // we expect some changes, but for now, these changes have not occured.
      // if they occur, they may alter the height of this element. Telling the
      // others about that change is part of the parent's responsibility.
      final RenderNode next = getNext();
      if (next != null)
      {
        next.setState(RenderNodeState.PENDING);
      }
      else
      {
        final RenderNode parent = getParent();
        if (parent != null)
        {
          parent.setState(RenderNodeState.PENDING);
        }
      }
    }

  }

  public RenderBox getParent()
  {
    return parent;
  }

  public void setParent(final RenderBox parent)
  {
    Object oldParent = this.parent;
    this.parent = parent;
    if (oldParent != parent)
    {
      setState(RenderNodeState.PENDING);
    }
  }

  public RenderNode getPrev()
  {
    return prev;
  }

  protected void setPrev(final RenderNode prev)
  {
    this.prev = prev;
  }

  public RenderNode getNext()
  {
    return next;
  }

  protected void setNext(final RenderNode next)
  {
    this.next = next;
  }

  /**
   * Splits the render node at the given position. This method returns an array
   * with the length of two; if the node is not splittable, the first element
   * should be empty (in the element's behavioural context) and the second
   * element should contain an independent copy of the original node.
   * <p/>
   * If the break position is ambugious, the break should appear *in front of*
   * the position - where in front-of depends on the reading direction.
   *
   * @param axis     the axis on which to break
   * @param position the break position within that axis.
   * @param target   the target array that should receive the broken node. If
   *                 the target array is not null, it must have at least two
   *                 slots.
   * @return the broken nodes contained in the target array.
   */
  public abstract RenderNode[] split(int axis,
                                     long position,
                                     RenderNode[] target);

  /**
   * Returns the nearest break-point that occurrs before that position. If the
   * position already is a break point, return that point. If there is no break
   * opportinity at all, return zero (= BREAK_NONE).
   * <p/>
   * (This causes the split to behave correctly; this moves all non-splittable
   * elements down to the next free area.)
   *
   * @param axis     the axis.
   * @param position the maximum position
   * @return the best break position.
   */
  public long getBestBreak(int axis, long position)
  {
    return 0;
  }

  /**
   * Returns the first break-point in that element. If there is no break
   * opportinity at all, return zero (= BREAK_NONE).
   * <p/>
   *
   * @param axis the axis.
   * @return the first break position.
   */
  public long getFirstBreak(int axis)
  {
    return 0;
  }

  public abstract long getMinimumChunkSize(int axis);

//  /**
//   * The minimum size returned here is always a box-size - that is the size from
//   * one border-edge to the opposite border edge (excluding the margins).
//   *
//   * @param axis
//   * @return
//   */
//  public abstract long getMinimumSize(int axis);

  /**
   * The preferred size returned here is always a box-size - that is the size
   * from one border-edge to the opposite border edge (excluding the margins).
   *
   * @param axis
   * @return
   */
  public abstract long getPreferredSize(int axis);

//  /**
//   * The maximum size returned here is always a box-size - that is the size from
//   * one border-edge to the opposite border edge (excluding the margins).
//   *
//   * @param axis
//   * @return
//   */
//  public abstract long getMaximumSize(int axis);

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

  public abstract void validate();

  public abstract BreakAfterEnum getBreakAfterAllowed();

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
      final RenderNode o = (RenderNode)super.clone();
      o.absoluteMargins = (StrictInsets) absoluteMargins.clone();
      o.effectiveMargins = (StrictInsets) effectiveMargins.clone();
      return  o;
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
    node.open = true;
    return node;
  }

  public void setClearLeft(final boolean clearLeft)
  {
    this.clearLeft = clearLeft;
  }

  public boolean isClearLeft()
  {
    return clearLeft;
  }

  public void setClearRight(final boolean clearRight)
  {
    this.clearRight = clearRight;
  }

  public boolean isClearRight()
  {
    return clearRight;
  }

  /**
   * A forced split is done whenever an text contains forced linebreaks. We
   * resolve these breaks before the layouting starts; this makes the paragraph
   * stuff a whole lot easier.
   * <p/>
   * If the element is the last in the line, then the last forced linebreak can
   * be ignored. (The line would have ended anyway.)
   * <p/>
   * The TextFactories already perform the linebreaking for us.
   *
   * @param isEndOfLine whether the element is the last in the line.
   * @return
   */
  public boolean isForcedSplitNeeded(boolean isEndOfLine)
  {
    return false;
  }

  /**
   * Checks, whether this node will cause breaks in its parent. While
   * 'isForcedSplitNeeded' checks, whether an element should be splitted, this
   * method checks, whether this element would be a valid reason to split.
   * <p/>
   * Text, that contains linebreaks at the end of the line, not be split by
   * itself, but will cause splits in the parent, if it is followed by some more
   * text.
   *
   * @param isEndOfLine
   * @return
   */
  public boolean isForcedSplitRequested(boolean isEndOfLine)
  {
    return false;
  }

  /**
   * Performs a forced split. That split, unlike the other split-operation does
   * not take sizes into account. It looks at the 'clear' property and splits,
   * if either clear-left or clear-right are enabled.
   *
   * @param isStartOfLine
   * @param isEndOfLine
   * @param target
   * @return null, if no split is needed, else the splitted nodes in an array.
   */
  public RenderNode[] splitForLinebreak(final boolean isEndOfLine,
                                        RenderNode[] target)
  {
    // ok, now it gets dirty. Find the evil one that needs to be split and
    // do that (*censored*) split.
    if (target == null || target.length < 2)
    {
      target = new RenderNode[2];
    }
    target[0] = this;
    target[1] = null;
    return target;
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
    return open;
  }

  public void close()
  {
    this.open = false;
    Log.debug ("Closing " + this);
  }

  public int getBreakability(int axis)
  {
    return UNBREAKABLE;
  }

  public boolean isEmpty()
  {
    return false;
  }

  protected RenderBox getParentBlockContext()
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

  protected long getComputedBlockContextWidth()
  {
    if (parentWidth == null)
    {
      final RenderBox parent = getParentBlockContext();
      if (parent == null)
      {
        parentWidth = new Long(0);
      }
      else
      {
        parentWidth = new Long(parent.getComputedBlockContextWidth());
      }
    }
    return parentWidth.longValue();
  }

  /**
   * The reference point corresponds to the baseline of an box. For now,
   * we define only one reference point per box. The reference point of boxes
   * corresponds to the reference point of the first linebox.
   *
   * @param axis
   * @return
   */
  public abstract long getReferencePoint(int axis);

//  /**
//   * Defines a spacing, that only applies if the node is not the first node in
//   * the box. This spacing gets later mixed in with the absolute margins and
//   * corresponds to the effective margin of the RenderBox class.
//   *
//   * @param axis
//   * @return
//   */
//  public abstract long getLeadingSpace (int axis);
//
//  /**
//   * Defines a spacing, that only applies, if the node is not the last node in
//   * the box. This spacing gets later mixed in with the absolute margins and
//   * corresponds to the effective margin of the RenderBox class.
//   *
//   * @param axis
//   * @return
//   */
//  public abstract long getTrailingSpace (int axis);

  public boolean isDiscardable()
  {
    return false;
  }

  protected void setOpen(final boolean open)
  {
    this.open = open;
  }

  protected long getEffectiveLayoutSize (int axis)
  {
    return getPreferredSize(axis);
  }










  private boolean marginsValidated;
  private StrictInsets effectiveMargins;
  private StrictInsets absoluteMargins;


  protected void validateMargins()
  {
    if (marginsValidated)
    {
      return;
    }

    validateAbsoluteMargins();

    // now here comes the complex part: Compute effective margins.
    // We have to deal with two cases: Inner element margins and outer margins
    // for the outer margins, we compute the parent's margin and substract
    // our margin from it (in other words: We do the normal collapsing).
    final RenderBox parent = getParent();
    if (parent == null || isEmpty())
    {
      // nice, no parent means no margins.
      effectiveMargins.setTop(0);
      effectiveMargins.setLeft(0);
      effectiveMargins.setBottom(0);
      effectiveMargins.setRight(0);
      marginsValidated = true;
      return;
    }

    final StrictInsets parentMargins = parent.getAbsoluteMargins();
    if (parent.getMajorAxis() == HORIZONTAL_AXIS)
    {
      if (parent.isLeadingMarginIndependent(VERTICAL_AXIS))
      {
        effectiveMargins.setTop(absoluteMargins.getTop());
      }
      else
      {
        effectiveMargins.setTop(parentMargins.getTop() - absoluteMargins.getTop());
      }

      if (parent.isTrailingMarginIndependent(VERTICAL_AXIS))
      {
        effectiveMargins.setBottom(absoluteMargins.getBottom());
      }
      else
      {
        effectiveMargins.setBottom(parentMargins.getBottom() - absoluteMargins.getBottom());
      }

      if (getNonEmptyPrev() == null &&
              (parent.isLeadingMarginIndependent(HORIZONTAL_AXIS) == false))
      {
        effectiveMargins.setLeft(parentMargins.getLeft() - absoluteMargins.getLeft());
      }
      else
      {
        effectiveMargins.setLeft(absoluteMargins.getLeft());
      }
      if (getNonEmptyNext() == null &&
              (parent.isTrailingMarginIndependent(HORIZONTAL_AXIS) == false))
      {
        effectiveMargins.setRight(parentMargins.getRight() - absoluteMargins.getRight());
      }
      else
      {
        effectiveMargins.setRight(absoluteMargins.getRight());
      }
    }
    else
    {
      if (parent.isLeadingMarginIndependent(HORIZONTAL_AXIS))
      {
        effectiveMargins.setLeft(absoluteMargins.getLeft());
      }
      else
      {
        effectiveMargins.setLeft(parentMargins.getLeft() - absoluteMargins.getLeft());
      }

      if (parent.isTrailingMarginIndependent(HORIZONTAL_AXIS))
      {
        effectiveMargins.setRight(absoluteMargins.getRight());
      }
      else
      {
        effectiveMargins.setRight(parentMargins.getRight() - absoluteMargins.getRight());
      }

      if (getNonEmptyPrev() == null &&
              (parent.isLeadingMarginIndependent(VERTICAL_AXIS) == false))
      {
        effectiveMargins.setTop(parentMargins.getTop() - absoluteMargins.getTop());
      }
      else
      {
        effectiveMargins.setTop(absoluteMargins.getTop());
      }
      if (getNonEmptyNext() == null &&
              (parent.isTrailingMarginIndependent(VERTICAL_AXIS) == false))
      {
        effectiveMargins.setBottom(parentMargins.getBottom() - absoluteMargins.getBottom());
      }
      else
      {
        effectiveMargins.setBottom(absoluteMargins.getBottom());
      }
    }
    marginsValidated = true;

  }

  protected void validateAbsoluteMargins()
  {
    final long topMargin = getLeadingMargin(VERTICAL_AXIS);
    final long bottomMargin = getTrailingMargin(VERTICAL_AXIS);
    final long leftMargin = getLeadingMargin(HORIZONTAL_AXIS);
    final long rightMargin = getTrailingMargin(HORIZONTAL_AXIS);
    absoluteMargins.setTop(topMargin);
    absoluteMargins.setLeft(leftMargin);
    absoluteMargins.setBottom(bottomMargin);
    absoluteMargins.setRight(rightMargin);
  }

  protected StrictInsets getAbsoluteMarginsInternal()
  {
    return absoluteMargins;
  }
  protected StrictInsets getEffectiveMarginsInternal()
  {
    return effectiveMargins;
  }

  public StrictInsets getAbsoluteMargins()
  {
    validateMargins();
    return absoluteMargins;
  }

  public StrictInsets getEffectiveMargins()
  {
    validateMargins();
    return effectiveMargins;
  }

  protected void invalidateMargins ()
  {
    this.marginsValidated = false;
  }

  protected boolean isIgnorableForMargins()
  {
    return isEmpty();
  }

  protected RenderNode getNonEmptyPrev()
  {
    RenderNode previous = getPrev();
    while (previous != null)
    {
      if (previous.isIgnorableForMargins() == false)
      {
        return previous;
      }
      previous = previous.getPrev();
    }
    return null;
  }

  protected RenderNode getNonEmptyNext()
  {
    RenderNode next = getNext();
    while (next != null)
    {
      if (next.isIgnorableForMargins() == false)
      {
        return next;
      }
      next = next.getNext();
    }
    return null;
  }


  /**
   * Queries the absolute margins between to top or left edge of this box and
   * the bottom or right edge of this boxes neighbour. If there is no neighbour
   * we assume an infinite margin instead.
   *
   * @param axis
   * @return
   */
  protected long getTrailingMargin(int axis)
  {
    final long parentMargin;
    RenderBox parent = getParent();
    if (parent == null)
    {
      // assume an infinite margin ..
      parentMargin = Long.MAX_VALUE;
    }
    else
    {
      if (axis == parent.getMajorAxis())
      {
        // check, if we are the last item in the parent.
        RenderNode next = getNonEmptyNext();

        if (next instanceof RenderBox)
        {
          RenderBox nextBox = (RenderBox) next;
          parentMargin = nextBox.getLeadingMarginInternal(axis);
        }
        else if (next != null)
        {
          parentMargin = next.getDefinedLeadingMargin(axis);
        }
        else if (parent.isTrailingMarginIndependent(axis))
        {
          parentMargin = 0;
        }
        else
        {
          parentMargin = parent.getTrailingMargin(axis);
        }
      }
      else if (parent.isTrailingMarginIndependent(axis))
      {
        parentMargin = 0;
      }
      else
      {
        parentMargin = parent.getTrailingMargin(axis);
      }
    }

    return collapseMargins(parentMargin, getDefinedTrailingMargin(axis));
  }

  protected long getDefinedTrailingMargin (int axis)
  {
    return 0;
  }

  /**
   * Queries the margin between to top or left edge of this box and the bottom
   * or right edge of this boxes neighbour.
   *
   * @param axis
   * @return
   */
  protected long getLeadingMargin(int axis)
  {
    final long parentMargin;
    RenderBox parent = getParent();
    if (parent == null)
    {
      // assume an infinite margin ..
      parentMargin = Long.MAX_VALUE;
    }
    else
    {
      if (axis == parent.getMajorAxis())
      {
        // check, if we are the first item in the parent.
        RenderNode previous = getNonEmptyPrev();
        if (previous instanceof RenderBox)
        {
          RenderBox prevBox = (RenderBox) previous;
          parentMargin = prevBox.getTrailingMarginInternal(axis);
        }
        else if (previous != null)
        {
          parentMargin = previous.getDefinedTrailingMargin(axis);
        }
        else if (parent.isLeadingMarginIndependent(axis))
        {
          parentMargin = 0;
        }
        else
        {
          parentMargin = parent.getLeadingMargin(axis);
        }
      }
      else if (parent.isLeadingMarginIndependent(axis))
      {
        parentMargin = 0;
      }
      else
      {
        parentMargin = parent.getLeadingMargin(axis);
      }
    }

    return collapseMargins(parentMargin, getDefinedLeadingMargin(axis));
  }

  protected long getDefinedLeadingMargin (int axis)
  {
    return 0;
  }

  protected long collapseMargins(long parentMargin, long myMargin)
  {
    return Math.max(parentMargin, myMargin);
  }


  /**
   * Defines a spacing, that only applies, if the node is not the last node in
   * the box. This spacing gets later mixed in with the absolute margins and
   * corresponds to the effective margin of the RenderBox class.
   *
   * @param axis
   * @return
   */
  public long getTrailingSpace(int axis)
  {
    if (axis == HORIZONTAL_AXIS)
    {
      return getEffectiveMargins().getRight();
    }
    return getEffectiveMargins().getBottom();
  }

  /**
   * Defines a spacing, that only applies if the node is not the first node in
   * the box. This spacing gets later mixed in with the absolute margins and
   * corresponds to the effective margin of the RenderBox class.
   *
   * @param axis
   * @return
   */
  public long getLeadingSpace(int axis)
  {
    if (axis == HORIZONTAL_AXIS)
    {
      return getEffectiveMargins().getLeft();
    }
    return getEffectiveMargins().getTop();
  }

  /**
   * If that method returns true, the element will not be used for rendering.
   * For the purpose of computing sizes or performing the layouting (in the
   * validate() step), this element will treated as if it is not there.
   *
   * If the element reports itself as non-empty, however, it will affect the
   * margin computation.
   *  
   * @return
   */
  public boolean isIgnorableForRendering ()
  {
    return isEmpty();
  }
}
