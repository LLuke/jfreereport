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
 * $Id: RenderBox.java,v 1.1 2006/07/11 13:51:02 taqua Exp $
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
  private StrictInsets margins;
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
    this.margins = new StrictInsets();
    this.paddings = new StrictInsets();
    this.borderWidths = new StrictInsets();
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
    o.margins = (StrictInsets) margins.clone();
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

  public StrictInsets getMargins()
  {
    return margins;
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
    final long boxContextWidth = getComputedBlockContextWidth();
    margins.setTop(boxDefinition.getMarginTop().resolve(boxContextWidth));
    margins.setBottom(boxDefinition.getMarginBottom().resolve(boxContextWidth));
    margins.setLeft(boxDefinition.getMarginLeft().resolve(boxContextWidth));
    margins.setRight(boxDefinition.getMarginRight().resolve(boxContextWidth));
  }

  protected long getRightInsets()
  {
    return getMargins().getRight() + getPaddings().getRight() + getBorderWidths().getRight();
  }

  protected long getLeftInsets()
  {
    return getMargins().getLeft() + getPaddings().getLeft() + getBorderWidths().getLeft();
  }

  protected long getTopInsets()
  {
    return getMargins().getTop() + getPaddings().getTop() +
            getBorderWidths().getTop();
  }

  protected long getBottomInsets()
  {
    return getMargins().getBottom() + getPaddings().getBottom() +
            getBorderWidths().getBottom();
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

  protected long[] getBordersAndPadding(int axis)
  {
    validateBorders();
    validatePaddings();

    long leftOrTop;
    long rightOrBottom;
    if (axis == HORIZONTAL_AXIS)
    {
      leftOrTop = getPaddings().getLeft() + getBorderWidths().getLeft();
      rightOrBottom = getPaddings().getRight() + getBorderWidths().getRight();
    }
    else
    {
      leftOrTop = getPaddings().getTop() + getBorderWidths().getTop();
      rightOrBottom = getPaddings().getBottom() + getBorderWidths().getBottom();
    }
    return new long[]{leftOrTop, rightOrBottom};
  }

  /**
   * Queries the margin of the top or left neighbour (position wise).
   *
   * @param axis
   * @return
   */
  public long getLeadingMargin (int axis)
  {
    RenderBox parent = getParent();
    if (parent == null)
    {
      return Long.MAX_VALUE;
    }
    // A block renderer box is easy. Our previous element will be the
    // element above us.
    if (axis == parent.getMajorAxis())
    {
      RenderNode previous = getPrev();
      if (previous != null)
      {
        if (previous instanceof RenderBox)
        {
          RenderBox prevBox = (RenderBox) previous;
          return prevBox.getTrailingMargin(axis);
        }
        else
        {
          // fall back ...
          return 0;
        }
      }
      else
      {
        return parent.getLeadingMargin(axis);
      }
    }
    else
    {
      return parent.getLeadingMargin(axis);
    }
  }

  /**
   * Queries the bottom or right margin.
   *
   * @param axis
   * @return
   */
  public long getTrailingMargin (int axis)
  {
    if (axis == VERTICAL_AXIS)
    {
      return getMargins().getBottom();
    }
    else
    {
      return getMargins().getRight();
    }
  }


}
