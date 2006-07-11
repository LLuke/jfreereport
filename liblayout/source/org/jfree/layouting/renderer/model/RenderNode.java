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
 * $Id: RenderNode.java,v 1.1 2006/07/11 13:51:02 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model;

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
    this.width = width;
  }

  public long getWidth()
  {
    return width;
  }

  public void setHeight(long height)
  {
    this.height = height;
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
    this.parent = parent;
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

  /**
   * The minimum size returned here is always a box-size - that is the size
   * from one border-edge to the opposite border edge (excluding the margins).
   *
   * @param axis
   * @return
   */
  public abstract long getMinimumSize(int axis);

  /**
   * The preferred size returned here is always a box-size - that is the size
   * from one border-edge to the opposite border edge (excluding the margins).
   *
   * @param axis
   * @return
   */
  public abstract long getPreferredSize(int axis);

  /**
   * The maximum size returned here is always a box-size - that is the size
   * from one border-edge to the opposite border edge (excluding the margins).
   *
   * @param axis
   * @return
   */
  public abstract long getMaximumSize(int axis);

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
      return (RenderNode) super.clone();
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

  public RenderNode getPredecessor()
  {
    return null;
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

  protected void setOpen(final boolean open)
  {
    this.open = open;
  }

  public void close()
  {
    this.open = false;
  }

  public int getBreakability()
  {
    return UNBREAKABLE;
  }

  public boolean isEmpty()
  {
    return false;
  }

  protected RenderBox getParentBlockContext ()
  {
    if (parent == null) return null;
    if (parent instanceof BlockRenderBox)
    {
      return (RenderBox) parent;
    }
    return parent.getParentBlockContext();
  }

  protected long getComputedBlockContextWidth()
  {
    final RenderBox parent = getParentBlockContext();
    if (parent == null)
    {
      return 0;
    }
    return parent.getComputedBlockContextWidth();
  }
}
