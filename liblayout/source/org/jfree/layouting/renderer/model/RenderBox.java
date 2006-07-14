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
 * RenderBox.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: RenderBox.java,v 1.3 2006/07/12 17:53:05 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.util.geom.StrictInsets;
import org.jfree.util.Log;

/**
 * A render-box corresponds to elements in a DOM tree.
 * <p/>
 * Each box has a size, paddings, margins and borders. Boxes may have one or
 * more childs.
 * <p/>
 * While all nodes may have a position or dimensions, boxes are special, as they
 * can have borders, margins and paddings. Borders, paddings  and margins can
 * have percentages, the margins can additionally be 'auto'.
 * <p/>
 * The StrictInset variables for these properties contain the resolved values,
 * while the box-definition contain the unresolved values. The resolve values
 * are not valid unless the object has been validated to least least
 * 'LAYOUTING'.
 *
 * @author Thomas Morgner
 */
public abstract class RenderBox extends RenderNode
{
  private BoxDefinition boxDefinition;
  private RenderNode firstChild;
  private RenderNode lastChild;

  private StrictInsets effectiveMargins;
  private StrictInsets absoluteMargins;
  private StrictInsets paddings;
  private StrictInsets borderWidths;

  private boolean paddingsValidated;
  private boolean bordersValidated;

  public RenderBox(final BoxDefinition boxDefinition)
  {
    if (boxDefinition == null)
    {
      throw new NullPointerException();
    }
    this.boxDefinition = boxDefinition;
    this.absoluteMargins = new StrictInsets();
    this.paddings = new StrictInsets();
    this.borderWidths = new StrictInsets();
    this.effectiveMargins = new StrictInsets();
  }


  public BoxDefinition getBoxDefinition()
  {
    return boxDefinition;
  }

  protected void setBoxDefinition(final BoxDefinition boxDefinition)
  {
    if (boxDefinition == null)
    {
      throw new NullPointerException();
    }
    this.boxDefinition = boxDefinition;
    this.paddingsValidated = false;
    this.bordersValidated = false;
  }

  public RenderNode getFirstChild()
  {
    return firstChild;
  }

  protected void setFirstChild(final RenderNode firstChild)
  {
    this.firstChild = firstChild;
  }

  public RenderNode getLastChild()
  {
    return lastChild;
  }

  protected void setLastChild(final RenderNode lastChild)
  {
    this.lastChild = lastChild;
  }

  public void addChild(final RenderNode child)
  {
    if (child == null)
    {
      throw new NullPointerException
              ("Child to be added must not be null.");
    }

    if (lastChild != null)
    {
      lastChild.setNext(child);
    }

    child.setPrev(lastChild);
    child.setNext(null);
    lastChild = child;

    if (firstChild == null)
    {
      firstChild = child;
    }

    child.setParent(this);
    setState(RenderNodeState.PENDING);
  }

  public void replaceChild(final RenderNode old, final RenderNode replacement)
  {
    if (old == replacement)
    {
      // nothing to do ...
      return;
    }

    boolean changed = false;
    if (old == firstChild)
    {
      replacement.setNext(firstChild.getNext());
      replacement.setPrev(null);
      firstChild.setNext(null);
      firstChild.setPrev(null);
      firstChild = replacement;
      changed = true;
    }
    if (old == lastChild)
    {
      replacement.setPrev(lastChild.getPrev());
      replacement.setNext(null);
      lastChild.setNext(null);
      lastChild.setPrev(null);
      lastChild = replacement;
      changed = true;
    }

    if (changed)
    {
      return;
    }

    final RenderNode prev = old.getPrev();
    final RenderNode next = old.getNext();
    replacement.setPrev(prev);
    replacement.setNext(next);

    if (prev != null)
    {
      prev.setNext(replacement);
    }
    if (next != null)
    {
      next.setPrev(replacement);
    }

    replacement.setParent(this);
    setState(RenderNodeState.PENDING);
  }


  public void replaceChilds(final RenderNode old,
                            final RenderNode[] replacement)
  {
    if (old.getParent() != this)
    {
      throw new IllegalArgumentException("None of my childs.");
    }

    if (replacement.length == 0)
    {
      throw new IndexOutOfBoundsException("Array is empty ..");
    }

    if (old == replacement[0])
    {
      if (replacement.length == 1)
      {
        // nothing to do ...
        return;
      }
      throw new IllegalArgumentException
              ("Thou shall not use the replace method to insert new elements!");
    }

    // first, connect all replacements ...
    RenderNode first = null;
    RenderNode last = null;

    for (int i = 0; i < replacement.length; i++)
    {
      if (last == null)
      {
        last = replacement[i];
        if (last != null)
        {
          first = last;
          first.setParent(this);
        }
        continue;
      }


      RenderNode node = replacement[i];

      last.setNext(node);
      node.setPrev(last);
      node.setParent(this);
      last = node;
    }

    if (first == null)
    {
      throw new IndexOutOfBoundsException("Array is empty (NullValues stripped)..");
    }

    // next, check if the first replacement .

    if (old == firstChild)
    {
      // inserting a replacement for the first child.
      final RenderNode second = firstChild.getNext();

      last.setNext(second);
      first.setPrev(null);

      if (second != null)
      {
        second.setPrev(last);
      }
      else
      {
        // No second element? So there was only one child.
        lastChild = last;
      }

      firstChild.setNext(null);
      firstChild.setPrev(null);
      firstChild = first;
      setState(RenderNodeState.PENDING);
      return;
    }

    if (old == lastChild)
    {
      first.setPrev(lastChild.getPrev());
      last.setNext(null);
      lastChild.setNext(null);
      lastChild.setPrev(null);
      lastChild = last;
      setState(RenderNodeState.PENDING);
      return;
    }

    // Something inbetween ...
    final RenderNode prev = old.getPrev();
    final RenderNode next = old.getNext();
    first.setPrev(prev);
    last.setNext(next);

    if (prev != null)
    {
      prev.setNext(first);
    }
    if (next != null)
    {
      next.setPrev(last);
    }

    setState(RenderNodeState.PENDING);
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
  public RenderNode[] split(int axis, long position, RenderNode[] target)
  {
    if (target == null || target.length < 2)
    {
      target = new RenderNode[2];
    }
    Log.debug("This box is not spittable at all");
    target[0] = new InvisibleRenderBox();
    target[1] = derive(true);
    return target;
  }

  /**
   * Propage all changes to all silbling nodes which come after this node and to
   * all childs.
   * <p/>
   * If this node is the last child, make the parent pending again.
   *
   * @param state
   */
  protected void notifyStateChange(final RenderNodeState oldState,
                                   final RenderNodeState newState)
  {
    if (newState == RenderNodeState.UNCLEAN)
    {
      RenderNode node = getFirstChild();
      if (node != null)
      {
        node.setState(RenderNodeState.UNCLEAN);
      }
      paddingsValidated = false;
      bordersValidated = false;

    }
    super.notifyStateChange(oldState, newState);
  }

  public long getMinimumChunkSize(int axis)
  {
    // Drive through all childs and query their size
    // then find the maximum
    long size = 0;
    RenderNode child = getFirstChild();
    while (child != null)
    {
      size = Math.max(size, child.getMinimumChunkSize(axis));
      child = child.getNext();
    }
    return size;
  }

  public BreakAfterEnum getBreakAfterAllowed()
  {
    final RenderNode lastChild = getLastChild();
    if (lastChild == null)
    {
      return BreakAfterEnum.BREAK_DONT_KNOW;
    }

    RenderNode node = lastChild;
    while (node != null)
    {
      BreakAfterEnum breakAllow = node.getBreakAfterAllowed();
      if (breakAllow != BreakAfterEnum.BREAK_DONT_KNOW)
      {
        return breakAllow;
      }
      node = node.getPrev();
    }
    return BreakAfterEnum.BREAK_DONT_KNOW;
  }

  /**
   * Clones this node. Be aware that cloning can get you into deep trouble, as
   * the relations this node has may no longer be valid.
   *
   * @return
   */
  public Object clone()
  {
    final RenderBox o = (RenderBox) super.clone();
    o.borderWidths = (StrictInsets) borderWidths.clone();
    o.absoluteMargins = (StrictInsets) absoluteMargins.clone();
    o.paddings = (StrictInsets) paddings.clone();
    return o;
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
    RenderBox box = (RenderBox) super.derive(deepDerive);
    if (deepDerive)
    {
      RenderNode node = firstChild;
      RenderNode currentNode = null;
      while (node != null)
      {
        RenderNode previous = currentNode;

        currentNode = node.derive(true);
        currentNode.setParent(box);
        if (previous == null)
        {
          box.firstChild = currentNode;
          currentNode.setPrev(null);
        }
        else
        {
          previous.setNext(currentNode);
          currentNode.setPrev(previous);
        }
        node = node.getNext();
      }

      box.lastChild = currentNode;
      if (lastChild != null)
      {
        box.lastChild.setNext(null);
      }
    }
    else
    {
      box.firstChild = null;
      box.lastChild = null;
    }
    return box;
  }

  public void addChilds(final RenderNode[] nodes)
  {
    for (int i = 0; i < nodes.length; i++)
    {
      addChild(nodes[i]);
    }
  }

  public RenderNode findNodeById(Object instanceId)
  {
    if (instanceId == getInstanceId())
    {
      return this;
    }

    RenderNode child = getFirstChild();
    while (child != null)
    {
      final RenderNode nodeById = child.findNodeById(instanceId);
      if (nodeById != null)
      {
        return nodeById;
      }
      child = child.getNext();
    }
    return null;
  }

  public RenderBox getInsertationPoint()
  {
    final RenderNode lastChild = getLastChild();
    if (lastChild instanceof RenderBox)
    {
      RenderBox lcBox = (RenderBox) lastChild;
      if (lcBox.isOpen())
      {
        return lcBox.getInsertationPoint();
      }
    }
    return this;
  }

  public Border getBorder()
  {
    return boxDefinition.getBorder();
  }

  /**
   * Removes all children.
   */
  public void clear()
  {
    RenderNode child = getFirstChild();
    while (child != null)
    {
      RenderNode nextChild = child.getNext();
      if (child != getFirstChild())
      {
        child.getPrev().setNext(null);
      }
      child.setPrev(null);
      child.setParent(null);
      child = nextChild;
    }
    setFirstChild(null);
    setLastChild(null);
    setState(RenderNodeState.UNCLEAN);
  }

  public int getBreakability()
  {
    return SOFT_BREAKABLE;
  }

  public StrictInsets getAbsoluteMargins()
  {
    return absoluteMargins;
  }

  public StrictInsets getPaddings()
  {
    return paddings;
  }

  public StrictInsets getBorderWidths()
  {
    return borderWidths;
  }

  protected void validatePaddings()
  {
    if (paddingsValidated)
    {
      return;
    }
    final long boxContextWidth = getComputedBlockContextWidth();
    paddings.setTop(boxDefinition.getPaddingTop().resolve(boxContextWidth));
    paddings.setBottom(boxDefinition.getPaddingBottom().resolve(boxContextWidth));
    paddings.setLeft(boxDefinition.getPaddingLeft().resolve(boxContextWidth));
    paddings.setRight(boxDefinition.getPaddingRight().resolve(boxContextWidth));
    paddingsValidated = true;
  }

  protected void validateBorders()
  {
    if (bordersValidated)
    {
      return;
    }
    final long boxContextWidth = getComputedBlockContextWidth();
    final Border border = boxDefinition.getBorder();
    borderWidths.setTop(border.getTop().getWidth().resolve(boxContextWidth));
    borderWidths.setBottom(border.getBottom().getWidth().resolve(boxContextWidth));
    borderWidths.setLeft(border.getLeft().getWidth().resolve(boxContextWidth));
    borderWidths.setRight(border.getRight().getWidth().resolve(boxContextWidth));
    bordersValidated = true;
  }

  protected void validateMargins()
  {
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
      return;
    }

    final StrictInsets parentMargins = parent.getAbsoluteMargins();
    if (getMajorAxis() == HORIZONTAL_AXIS)
    {
      effectiveMargins.setTop(parentMargins.getTop() - absoluteMargins.getTop());
      effectiveMargins.setBottom(parentMargins.getBottom() - absoluteMargins.getBottom());
      if (getNonEmptyPrev() == null)
      {
        effectiveMargins.setLeft(parentMargins.getLeft() - absoluteMargins.getLeft());
      }
      else
      {
        effectiveMargins.setLeft(absoluteMargins.getLeft());
      }
      if (getNonEmptyNext() == null)
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
      effectiveMargins.setLeft(parentMargins.getLeft() - absoluteMargins.getLeft());
      effectiveMargins.setRight(parentMargins.getRight() - absoluteMargins.getRight());

      if (getNonEmptyPrev() == null)
      {
        effectiveMargins.setTop(parentMargins.getTop() - absoluteMargins.getTop());
      }
      else
      {
        effectiveMargins.setTop(absoluteMargins.getTop());
      }
      if (getNonEmptyNext() == null)
      {
        effectiveMargins.setBottom(parentMargins.getBottom() - absoluteMargins.getBottom());
      }
      else
      {
        effectiveMargins.setBottom(absoluteMargins.getBottom());
      }
    }
  }

  protected void validateAbsoluteMargins()
  {
    final long topMargin = getLeadingMargin(VERTICAL_AXIS);
    final long bottomMargin = getTrailingMargin(VERTICAL_AXIS);
    final long leftMargin = getLeadingMargin(HORIZONTAL_AXIS);
    if (leftMargin == 10000)
    {
      Log.debug ("ERE");
    }
    final long rightMargin = getTrailingMargin(HORIZONTAL_AXIS);
    absoluteMargins.setTop(topMargin);
    absoluteMargins.setLeft(leftMargin);
    absoluteMargins.setBottom(bottomMargin);
    absoluteMargins.setRight(rightMargin);
  }

  protected long getTrailingInsets(int axis)
  {
    if (axis == HORIZONTAL_AXIS)
    return getPaddings().getRight() +
            getBorderWidths().getRight();
    else
    {
      return getPaddings().getBottom() +
              getBorderWidths().getBottom();
    }
  }

  protected long getLeadingInsets(int axis)
  {
    if (axis == HORIZONTAL_AXIS)
    {
      return getPaddings().getLeft() +
            getBorderWidths().getLeft();
    }
    else
    {
      return getPaddings().getTop() +
              getBorderWidths().getTop();
    }
  }

  protected long getParentWidth()
  {
    final RenderNode parent = getParent();
    if (parent == null)
    {
      return 0;
    }
    return parent.getWidth();
  }

  /**
   * Queries the absolute margins between to top or left edge of this box and
   * the bottom or right edge of this boxes neighbour. If there is no neighbour
   * we assume an infinite margin instead.
   *
   * @param axis
   * @return
   */
  public long getTrailingMargin(int axis)
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
          parentMargin = next.getLeadingSpace(axis);
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

    final long parentWidth = getComputedBlockContextWidth();
    final long myMargin;
    if (axis == HORIZONTAL_AXIS)
    {
      myMargin = getBoxDefinition().getMarginRight().resolve(parentWidth);
    }
    else
    {
      myMargin = getBoxDefinition().getMarginBottom().resolve(parentWidth);
    }

    return collapseMargins(parentMargin, myMargin);
  }

  /**
   * Queries the margin between to top or left edge of this box and the bottom
   * or right edge of this boxes neighbour.
   *
   * @param axis
   * @return
   */
  public long getLeadingMargin(int axis)
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
          parentMargin = previous.getTrailingSpace(axis);
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

    final long parentWidth = getComputedBlockContextWidth();
    final long myMargin;
    if (axis == HORIZONTAL_AXIS)
    {
      myMargin = getBoxDefinition().getMarginLeft().resolve(parentWidth);
    }
    else
    {
      myMargin = getBoxDefinition().getMarginTop().resolve(parentWidth);
    }

    return collapseMargins(parentMargin, myMargin);
  }

  private RenderNode getNonEmptyPrev()
  {
    RenderNode previous = getPrev();
    while (previous != null)
    {
      if (previous.isEmpty() == false)
      {
        return previous;
      }
      previous = previous.getPrev();
    }
    return null;
  }

  private RenderNode getNonEmptyNext()
  {
    RenderNode next = getNext();
    while (next != null)
    {
      if (next.isEmpty() == false)
      {
        return next;
      }
      next = next.getNext();
    }
    return null;
  }


  /**
   * Queries the bottom or right margin. This dives into the last child of the
   * queried box, if needed.
   * <p/>
   * Warning: This method is used by the getLeadingMargin method and should not
   * be used by someone else.
   *
   * @param axis
   * @return
   */
  private long getTrailingMarginInternal(int axis)
  {
    RenderNode lastChild = getLastChild();
    while (lastChild != null && lastChild.isEmpty())
    {
      lastChild = lastChild.getPrev();
    }

    final long childMargins;
    if (isTrailingMarginIndependent(axis) == false)
    {
      if (lastChild instanceof RenderBox)
      {
        // I have some childs, lets ask them about their bottom or right margins
        RenderBox lastBox = (RenderBox) lastChild;
        childMargins = lastBox.getTrailingMarginInternal(axis);
      }
      else
      {
        // ok, no valid, margin-carrying childs ...
        childMargins = 0;
      }
    }
    else
    {
      childMargins = 0;
    }

    final BoxDefinition bd = getBoxDefinition();
    final long parentWidth = getComputedBlockContextWidth();
    if (axis == HORIZONTAL_AXIS)
    {
      return collapseMargins
              (bd.getMarginRight().resolve(parentWidth), childMargins);
    }
    else
    {
      return collapseMargins
              (bd.getMarginBottom().resolve(parentWidth), childMargins);
    }
  }


  /**
   * Queries the bottom or right margin. This dives into the last child of the
   * queried box, if needed.
   * <p/>
   * Warning: This method is used by the getLeadingMargin method and should not
   * be used by someone else.
   *
   * @param axis
   * @return
   */
  private long getLeadingMarginInternal(int axis)
  {
    RenderNode firstChild = getFirstChild();
    while (firstChild != null && firstChild.isEmpty())
    {
      firstChild = firstChild.getNext();
    }

    final long childMargins;
    if (isTrailingMarginIndependent(axis) == false)
    {
      if (firstChild instanceof RenderBox)
      {
        // I have some childs, lets ask them about their bottom or right margins
        RenderBox firstBox = (RenderBox) firstChild;
        childMargins = firstBox.getLeadingMarginInternal(axis);
      }
      else
      {
        // ok, no valid, margin-carrying childs ...
        childMargins = 0;
      }
    }
    else
    {
      childMargins = 0;
    }

    final BoxDefinition bd = getBoxDefinition();
    final long parentWidth = getComputedBlockContextWidth();
    if (axis == HORIZONTAL_AXIS)
    {
      return collapseMargins
              (bd.getMarginLeft().resolve(parentWidth), childMargins);
    }
    else
    {
      return collapseMargins
              (bd.getMarginTop().resolve(parentWidth), childMargins);
    }
  }

  public boolean isLeadingMarginIndependent(int axis)
  {
    if (axis == VERTICAL_AXIS)
    {
      return getBorderWidths().getTop() > 0 ||
              getPaddings().getTop() > 0;
    }
    else
    {
      return getBorderWidths().getLeft() > 0 ||
              getPaddings().getLeft() > 0;
    }
  }

  public boolean isTrailingMarginIndependent(int axis)
  {
    if (axis == VERTICAL_AXIS)
    {
      return getBorderWidths().getBottom() > 0 ||
              getPaddings().getBottom() > 0;
    }
    else
    {
      return getBorderWidths().getRight() > 0 ||
              getPaddings().getRight() > 0;
    }
  }

  private long collapseMargins(long parentMargin, long myMargin)
  {
    return Math.max(parentMargin, myMargin);
  }

  public StrictInsets getEffectiveMargins()
  {
    return effectiveMargins;
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
    validateMargins();
    if (axis == HORIZONTAL_AXIS)
    {
      return getEffectiveMargins().getLeft();
    }
    return getEffectiveMargins().getTop();
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
    validateMargins();
    if (axis == HORIZONTAL_AXIS)
    {
      return getEffectiveMargins().getRight();
    }
    return getEffectiveMargins().getBottom();
  }


  public long getMinimumSize(int axis)
  {
    if (axis == getMinorAxis())
    {
      // minor axis means: Drive through all childs and query their size
      // then find the maximum
      long sizeAbove = 0;
      long sizeBelow = 0;
      RenderNode child = getFirstChild();
      while (child != null)
      {
        long size = child.getMinimumSize(axis);
        long refp = child.getReferencePoint(axis);

        sizeAbove = Math.max (sizeAbove, refp);
        sizeBelow = Math.max (sizeBelow, size - refp);
        child = child.getNext();
      }
      return getLeadingInsets(axis) + sizeAbove + sizeBelow + getTrailingInsets(axis);
    }

    long size = getLeadingInsets(axis);
    RenderNode child = getFirstChild();
    while (child != null)
    {
      size += child.getMinimumSize(axis);
      child = child.getNext();
    }
    return size + getTrailingInsets(axis);
  }

  public long getPreferredSize(int axis)
  {
    if (axis == getMinorAxis())
    {
      // minor axis means: Drive through all childs and query their size
      // then find the maximum
      long sizeAbove = 0;
      long sizeBelow = 0;
      RenderNode child = getFirstChild();
      while (child != null)
      {
        long size = child.getPreferredSize(axis);
        long refp = child.getReferencePoint(axis);

        sizeAbove = Math.max (sizeAbove, refp);
        sizeBelow = Math.max (sizeBelow, size - refp);
        child = child.getNext();
      }
      return getLeadingInsets(axis) + sizeAbove + sizeBelow + getTrailingInsets(axis);
    }

    long size = getLeadingInsets(axis);
    RenderNode child = getFirstChild();
    while (child != null)
    {
      size += child.getPreferredSize(axis);
      child = child.getNext();
    }
    return size + getTrailingInsets(axis);
  }

  public long getMaximumSize(int axis)
  {
    if (axis == getMinorAxis())
    {
      // minor axis means: Drive through all childs and query their size
      // then find the maximum
      long sizeAbove = 0;
      long sizeBelow = 0;
      RenderNode child = getFirstChild();
      while (child != null)
      {
        long size = child.getMaximumSize(axis);
        long refp = child.getReferencePoint(axis);

        sizeAbove = Math.max (sizeAbove, refp);
        sizeBelow = Math.max (sizeBelow, size - refp);
        child = child.getNext();
      }
      return getLeadingInsets(axis) + sizeAbove + sizeBelow + getTrailingInsets(axis);
    }

    long size = getLeadingInsets(axis);
    RenderNode child = getFirstChild();
    while (child != null)
    {
      size += child.getMaximumSize(axis);
      child = child.getNext();
    }
    return size + getTrailingInsets(axis);
  }

}
