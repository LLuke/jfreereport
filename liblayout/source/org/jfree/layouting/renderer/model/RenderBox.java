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
 * $Id: RenderBox.java,v 1.9 2006/07/22 15:28:50 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.renderer.border.RenderLength;
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

  private StrictInsets paddings;
  private StrictInsets borderWidths;

  private boolean paddingsValidated;
  private boolean bordersValidated;
  private boolean open;

  public RenderBox(final BoxDefinition boxDefinition)
  {
    if (boxDefinition == null)
    {
      throw new NullPointerException();
    }
    this.boxDefinition = boxDefinition;
    this.paddings = new StrictInsets();
    this.borderWidths = new StrictInsets();
    this.open = true;
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
    invalidateMargins();
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

  protected void addGeneratedChild(final RenderNode child)
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

  public void addChild(final RenderNode child)
  {
    if (child == null)
    {
      throw new NullPointerException
              ("Child to be added must not be null.");
    }

    if (isOpen() == false)
    {
      throw new IllegalStateException
              ("Adding content to an already closed element.");
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

  /**
   * Inserts the given target after the specified node. If the node is null,
   * the target is inserted as first node.
   *
   * @param node
   * @param target
   */
  protected void insertAfter(RenderNode node, RenderNode target)
  {
    if (node == null)
    {
      // ok, insert as new first element.
      RenderNode firstChild = getFirstChild();
      if (firstChild == null)
      {
        setLastChild(target);
        setFirstChild(target);
        target.setParent(this);
        target.setPrev(null);
        target.setNext(null);
        return;
      }

      // we have a first-child.
      firstChild.setPrev(target);
      setFirstChild(target);
      target.setParent(this);
      target.setPrev(null);
      target.setNext(firstChild);
      return;
    }

    if (node.getParent() != this)
    {
      throw new IllegalStateException("You made a big boo");
    }

    final RenderNode next = node.getNext();
    node.setNext(target);
    target.setPrev(node);
    target.setParent(this);
    target.setNext(next);
    if (next != null)
    {
      next.setPrev(target);
    }
    else
    {
      setLastChild(target);
    }
  }

  /**
   * Inserts the given target directly before the the specified node.
   * If the node is null, the element is inserted at the last position.
   *
   * @param node
   * @param target
   */
  protected void insertBefore(RenderNode node, RenderNode target)
  {
    if (node == null)
    {
      RenderNode lastChild = getLastChild();
      if (lastChild == null)
      {
        target.setParent(this);
        target.setPrev(null);
        target.setNext(null);
        setFirstChild(target);
        setLastChild(target);
        return;
      }

      setLastChild(target);
      target.setParent(this);
      target.setPrev(lastChild);
      target.setNext(null);
      lastChild.setNext(target);
      return;
    }

    if (node.getParent() != this)
    {
      throw new IllegalStateException("You made a big boo");
    }

    final RenderNode prev = node.getPrev();
    node.setPrev(target);
    target.setNext(node);
    target.setParent(this);
    target.setNext(prev);
    if (prev != null)
    {
      prev.setNext(target);
    }
    else
    {
      setFirstChild(target);
    }
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
    if (newState == RenderNodeState.PENDING)
    {
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

    final long insets = getLeadingInsets(axis) + getTrailingInsets(axis);

    if (axis == HORIZONTAL_AXIS)
    {
      return insets + Math.max(boxDefinition.getMinimumWidth().resolve(0), size);
    }

    return insets + Math.max(boxDefinition.getMinimumHeight().resolve(0), size);
  }

  public BreakAfterEnum getBreakAfterAllowed(final int axis)
  {
    if (axis == getMinorAxis())
    {
      return BreakAfterEnum.BREAK_ALLOW;
    }

    final RenderNode lastChild = getLastChild();
    if (lastChild == null)
    {
      return BreakAfterEnum.BREAK_DONT_KNOW;
    }

    RenderNode node = lastChild;
    while (node != null)
    {
      BreakAfterEnum breakAllow = node.getBreakAfterAllowed(axis);
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
    box.invalidateMargins();
    box.paddingsValidated = false;
    box.bordersValidated = false;
    box.open = true;

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
    if (isOpen() == false)
    {
      throw new IllegalStateException("Already closed");
    }

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

  public StrictInsets getPaddings()
  {
    validatePaddings();
    return paddings;
  }

  public StrictInsets getBorderWidths()
  {
    validateBorders();
    return borderWidths;
  }

  protected boolean isPaddingsValidated()
  {
    return paddingsValidated;
  }

  protected void setPaddingsValidated(final boolean paddingsValidated)
  {
    this.paddingsValidated = paddingsValidated;
  }

  protected StrictInsets getPaddingsInternal()
  {
    return paddings;
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

  protected boolean isBordersValidated()
  {
    return bordersValidated;
  }

  protected void setBordersValidated(final boolean bordersValidated)
  {
    this.bordersValidated = bordersValidated;
  }

  protected StrictInsets getBordersInternal()
  {
    return borderWidths;
  }

  protected void validateBorders()
  {
    if (bordersValidated)
    {
      return;
    }
    final long boxContextWidth = getComputedBlockContextWidth();
    final Border border = getBorder();
    borderWidths.setTop(border.getTop().getWidth().resolve(boxContextWidth));
    borderWidths.setBottom(border.getBottom().getWidth().resolve(boxContextWidth));
    borderWidths.setLeft(border.getLeft().getWidth().resolve(boxContextWidth));
    borderWidths.setRight(border.getRight().getWidth().resolve(boxContextWidth));
    bordersValidated = true;
  }

  protected long getTrailingInsets(int axis)
  {
    if (axis == HORIZONTAL_AXIS)
    {
      return getPaddings().getRight() +
              getBorderWidths().getRight();
    }
    else
    {
      return getPaddings().getBottom() +
              getBorderWidths().getBottom();
    }
  }

  protected long getLeadingInsets(int axis)
  {
    validatePaddings();
    validateBorders();

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
  protected long getTrailingMarginInternal(int axis)
  {
    RenderNode lastChild = getLastChild();
    while (lastChild != null)
    {
      if (lastChild.isIgnorableForMargins() == false)
      {
        break;
      }
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
  protected long getLeadingMarginInternal(int axis)
  {
    RenderNode firstChild = getFirstChild();
    while (firstChild != null)
    {
      if (firstChild.isIgnorableForMargins() == false)
      {
        break;
      }
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

  protected long getDefinedTrailingMargin(int axis)
  {
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
    return myMargin;
  }

  protected long getDefinedLeadingMargin(int axis)
  {
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
    return myMargin;
  }

  public long getPreferredSize(int axis)
  {
    final long retval;
    if (axis == getMinorAxis())
    {
      Log.debug("MINOR BEGIN : PreferredSize " + this);

      // minor axis means: Drive through all childs and query their size
      // then find the maximum
      long sizeAbove = 0;
      long sizeBelow = 0;
      RenderNode child = getFirstChild();
      while (child != null)
      {
        if (child.isIgnorableForRendering())
        {
          // empty childs may affect the margin computation or cause linebreaks,
          // but they must not appear here.
          child = child.getNext();
          continue;
        }

        if (child instanceof MarkerRenderBox)
        {
          MarkerRenderBox mrb = (MarkerRenderBox) child;
          if (mrb.isOutside())
          {
            child = child.getNext();
            continue;
          }
        }

        //long size = child.getPreferredSize(axis);
        long size = child.getEffectiveLayoutSize(axis);
        long refp = child.getReferencePoint(axis);
        long lead = child.getLeadingSpace(axis);
        long trai = child.getTrailingSpace(axis);

        sizeAbove = Math.max(sizeAbove, refp + lead);
        sizeBelow = Math.max(sizeBelow, (size - refp) + trai);
        Log.debug("  P-Size: " + size + " " + refp + " " + lead + " " + trai + " " + child);
        child = child.getNext();
      }
      retval = getLeadingInsets(axis) + sizeAbove +
              sizeBelow + getTrailingInsets(axis);
      Log.debug("MINOR END: PreferredSize: " + retval + " " + axis + " " + this);

    }
    else
    {
      Log.debug("MAJOR BEGIN : PreferredSize " + this);

      long size = getLeadingInsets(axis);
      RenderNode child = getFirstChild();
      long trailingSpace = 0;
      while (child != null)
      {
        if (child.isIgnorableForRendering())
        {
          // empty childs may affect the margin computation or cause linebreaks,
          // but they must not appear here.
          child = child.getNext();
          continue;
        }
        if (child instanceof MarkerRenderBox)
        {
          MarkerRenderBox mrb = (MarkerRenderBox) child;
          if (mrb.isOutside())
          {
            child = child.getNext();
            continue;
          }
        }


        final long layoutSize = child.getEffectiveLayoutSize(axis);
        size += layoutSize;

        final long leadingSpace = child.getLeadingSpace(axis);
        size += Math.max(leadingSpace, trailingSpace);

        trailingSpace = child.getTrailingSpace(axis);
//        size += trailingSpace;

        Log.debug("  P-Size: " + layoutSize + " " + leadingSpace + " " + trailingSpace + " " + child);
        child = child.getNext();
      }
      retval = size + trailingSpace + getTrailingInsets(axis);
      Log.debug("MAJOR END: PreferredSize: " + retval + " " + axis + " " + this);
    }
    return retval;
  }

  /**
   * This needs to be adjusted to support vertical flows as well.
   *
   * @param axis
   * @param node
   * @return
   */
  public long getEffectiveLayoutSize(int axis)
  {
    if (axis == VERTICAL_AXIS)
    {
      return getPreferredSize(axis);
    }

    // 1. If the element is a flow root other than the root element,
    //    width is the intrinsic width.

    // 2. If the element is the root element on paged media, the value
    //    is the computed width value of the 'size' property.
    final RenderBox parent = getParent();
    if (parent == null)
    {
      // this should not happen ...
      return getPreferredSize(axis);
    }

    // 3. Otherwise, if the element is a block-level element and its
    //    containing block also has a horizontal flow, equation (1)
    //    below determines the width.
    //
    // (1) (width of containing block) = margin-left + border-left +
    //                                   padding-left + width + padding-right +
    //                                   border-right + margin-right

    if (parent instanceof BlockRenderBox)
    {
      final RenderLength preferredWidth =
              getBoxDefinition().getPreferredWidth();
      if (RenderLength.AUTO.equals(preferredWidth))
      {
        // margins with 'auto' are not yet supported.
        // This is also the behaviour of Mozilla and Opera: if the width is
        // auto, then all auto-margins get set to zero.
        final long margins = getLeadingSpace(axis) + getTrailingSpace(axis);
        final long nodeWidth = getPreferredSize(axis);
        final long l = (nodeWidth - margins);
        if (l < 0)
        {
          return 0;
        }
        return l;
      }
      else
      {
        // todo: At the moment, this fails, as there is no computed block
        // context width at all.
        Log.debug("Still Empty");
        final long contextWidth = getComputedBlockContextWidth();
        final long width = preferredWidth.resolve(contextWidth);
        if (width < 0)
        {
          return 0;
        }
        else
        {
          return width;
        }
      }
    }

    // 4. Otherwise, if the element is block-level and the containing
    //    block has a different orientation, the computed value is the
    //    intrinsic width.

    return getPreferredSize(axis);

  }

  /**
   * The reference point corresponds to the baseline of an box. For now, we
   * define only one reference point per box. The reference point of boxes
   * corresponds to the reference point of the first linebox.
   *
   * @param axis
   * @return
   */
  public long getReferencePoint(int axis)
  {
    // we have no horizontal reference point ... (yet)
    if (axis == getMajorAxis())
    {
      RenderNode firstChild = getFirstChild();
      while (firstChild != null)
      {
        if (firstChild.isIgnorableForRendering() == false)
        {
          return getLeadingInsets(axis) + firstChild.getReferencePoint(axis);
        }
        firstChild = firstChild.getNext();
      }
      return 0;
    }

    // this gets a bit more complicated. Iterate over all childs and get their
    // reference point.
    RenderNode child = getFirstChild();
    long referencePoint = 0;
    while (child != null)
    {
      final long refPoint = child.getReferencePoint(getMinorAxis());
      referencePoint = Math.max(refPoint, referencePoint);
      child = child.getNext();
    }
    return getLeadingInsets(axis) + referencePoint;
  }

  private RenderNode getFirstNonEmpty()
  {
    RenderNode firstChild = getFirstChild();
    while (firstChild != null)
    {
      if (firstChild.isEmpty() == false)
      {
        return firstChild;
      }
      firstChild = firstChild.getNext();
    }
    return null;
  }

  public boolean isEmpty()
  {
    if (getBoxDefinition().isEmpty() == false)
    {
      return false;
    }

    RenderNode node = getFirstNonEmpty();
    if (node != null)
    {
      return false;
    }
    // Ok, the childs were not able to tell us some truth ..
    // lets try something else.
    return true;
  }

  public boolean isDiscardable()
  {
    RenderNode node = getFirstChild();
    while (node != null)
    {
      if (node.isDiscardable() == false)
      {
        return false;
      }
      node = node.getNext();
    }
    return true;
  }

  public void close()
  {
    if (isOpen() == false)
    {
      Log.debug("no longer open " + this);
      return;
    }

    this.open = false;
    if (isDiscardable())
    {
      if (getParent() != null)
      {
        getParent().remove(this);
      }
    }
    else
    {
      RenderNode lastChild = getLastChild();
      while (lastChild != null)
      {
        if (lastChild.isDiscardable())
        {
          remove(lastChild);
          lastChild = getLastChild();
        }
        else
        {
          break;
        }
      }
    }
  }

  protected void remove(RenderNode child)
  {
    Log.debug("Removing " + child);
    final RenderBox parent = child.getParent();
    if (parent != this)
    {
      throw new IllegalArgumentException("None of my childs");
    }

    child.setParent(null);

    RenderNode prev = child.getPrev();
    RenderNode next = child.getNext();

    if (prev != null)
    {
      prev.setNext(next);
    }

    if (next != null)
    {
      next.setPrev(prev);
    }

    if (firstChild == child)
    {
      firstChild = next;
    }
    if (lastChild == child)
    {
      lastChild = prev;
    }
  }


  /**
   * Returns the nearest break-point that occurrs before that splitPosition. If
   * the splitPosition already is a break point, return that point. If there is
   * no break opportinity at all, return zero (= BREAK_NONE).
   * <p/>
   * (This causes the split to behave correctly; this moves all non-splittable
   * elements down to the next free area.)
   *
   * @param axis          the axis.
   * @param splitPosition the maximum splitPosition
   * @return the best break splitPosition.
   */
  public long getBestBreak(int axis, long splitPosition)
  {
    if (splitPosition <= 0)
    {
      throw new IllegalArgumentException("Split-Position cannot be negative or zero: " + splitPosition);
    }

    //  Log.debug("InlineRenderer: Start BestBreak: " + splitPosition);
    if (axis == getMinorAxis())
    {
      // not splitable.
      Log.debug("The box is not splittable on the minor axis");
      return 0;
    }

    // this is simply the best we can get from our contents.

    long bestBreak = 0;
    long cursor = getLeadingInsets(axis);

    RenderNode child = getFirstChild();
    while (child != null)
    {
      final long currentSize = child.getEffectiveLayoutSize(axis);
      //  Log.debug("InlineRenderer: Best Break: Preferred Size: " + currentSize + " " + child);

      final long posAfterGeneric = currentSize + cursor;
      final long posAfter;
      if (child.getNext() == null)
      {
        posAfter = posAfterGeneric + getTrailingInsets(axis);
      }
      else
      {
        posAfter = posAfterGeneric;
      }

      // shortcut .. test for a direct hit.
      final BreakAfterEnum breakAfterAllowed = child.getBreakAfterAllowed(axis);
      if (posAfter == splitPosition)
      {
        if (breakAfterAllowed == BreakAfterEnum.BREAK_ALLOW)
        {
          return posAfter;
        }
      }

      // this would go over the limit .. try to find a suitable break point
      // inside the element.
      if (posAfter >= splitPosition)
      {
        final long effectiveSplitPos = splitPosition - cursor;
        final long bestBreakLocal = child.getBestBreak(axis, effectiveSplitPos);
        if (bestBreakLocal > 0)
        {
          //        Log.debug("InlineRenderer: Best Break(1): " + (cursor + bestBreakLocal));
          return cursor + bestBreakLocal;
        }
        else if (bestBreak > 0)
        {
          //       Log.debug("InlineRenderer: Best Break(3): " + (bestBreak));
          return bestBreak;
        }
      }

      if (breakAfterAllowed == BreakAfterEnum.BREAK_ALLOW)
      {
        //     Log.debug("InlineRenderer: Setting best Break: " + (posAfter));
        bestBreak = Math.max(bestBreak, posAfter);
      }

      if (posAfter >= splitPosition)
      {
        //      Log.debug("InlineRenderer: Best Break(2): " + (bestBreak));
        return bestBreak;
      }
      child = child.getNext();
      cursor += currentSize;
    }
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
    if (axis == getMinorAxis())
    {
      // not splitable.
      Log.debug("No split on the minor axis");
      return 0;
    }

    // this is simply the best we can get from our contents.

    long cursor = getLeadingInsets(axis);
    RenderNode child = getFirstChild();
    while (child != null)
    {
      long currentSize = child.getPreferredSize(axis);
      final long posAfter = currentSize + cursor;
      final long bestBreakLocal = child.getFirstBreak(axis);
      if (bestBreakLocal > 0)
      {
        return bestBreakLocal + cursor;
      }

      final BreakAfterEnum breakAfterAllowed = child.getBreakAfterAllowed(axis);
      if (breakAfterAllowed == BreakAfterEnum.BREAK_ALLOW)
      {
        if (child.getNext() == null)
        {
          // if this is the last child, include the paddings and borders ..
          return posAfter + getTrailingInsets(axis);
        }
        return posAfter;
      }
      else
      {
        //       Log.debug("BreakAfter: " + breakAfterAllowed);
      }
      cursor = posAfter;
      child = child.getNext();
    }
    return 0;
  }


  /**
   * Splits the render node at the given splitPoint. This method returns an
   * array with the length of two; if the node is not splittable, the first
   * element should be empty (in the element's behavioural context) and the
   * second element should contain an independent copy of the original node.
   * <p/>
   * If the break splitPoint is ambugious, the break should appear *in front of*
   * the splitPoint - where in front-of depends on the reading direction.
   *
   * @param axis       the axis on which to break
   * @param splitPoint the break splitPoint within that axis.
   * @param target     the target array that should receive the broken node. If
   *                   the target array is not null, it must have at least two
   *                   slots.
   * @return the broken nodes contained in the target array.
   */
  public RenderNode[] split(int axis, long splitPoint, RenderNode[] target)
  {
    if (splitPoint <= 0)
    {
      throw new IllegalArgumentException();
    }

    if (splitPoint == 41000)
    {
      Log.debug("BEGIN SPLIT: " + splitPoint);
    }

    if (target == null || target.length < 2)
    {
      target = new RenderNode[2];
    }

    if (axis == getMinorAxis())
    {
      target[0] = new SpacerRenderNode();
      target[1] = derive(true);
      return target;
    }

    // first, find the split point.
    long firstSplitSize = getLeadingInsets(axis);
    RenderNode firstSplitChild = getFirstChild();
    long trailingSpace = 0;
    while (firstSplitChild != null)
    {
      if (firstSplitChild.isDiscardable())
      {
        firstSplitChild = firstSplitChild.getNext();
        continue;
      }

      final long prefSize =
              firstSplitChild.getEffectiveLayoutSize(axis) +
                      Math.max(firstSplitChild.getLeadingSpace(axis), trailingSpace);

      trailingSpace = firstSplitChild.getTrailingSpace(axis);
      if ((prefSize + firstSplitSize + trailingSpace) <= splitPoint)
      {
        firstSplitSize += prefSize;
        firstSplitChild = firstSplitChild.getNext();
      }
      else
      {
        break;
      }
    }

    // we will fit, no split is needed.
    if (firstSplitChild == null)
    {
      Log.warn("PERFORMANCE: Unnecessarily stupid split detected.");
      target[0] = derive(true);
      target[1] = null;
      return target;
    }

    // Prepare the borders ...
    BoxDefinition[] boxes = getBoxDefinition().split(axis);
    InlineRenderBox firstBox = (InlineRenderBox) derive(false);
    firstBox.setBoxDefinition(boxes[0]);

    // Now add everything up to the split point to the first box.
    int firstNodeCounter = 0;
    RenderNode firstBoxChild = getFirstChild();
    while (firstBoxChild != null && firstSplitChild != firstBoxChild)
    {
      if (firstSplitChild.isDiscardable())
      {
        firstBoxChild = firstBoxChild.getNext();
        continue;
      }

      firstBox.addChild(firstBoxChild.derive(true));
      firstNodeCounter += 1;
      firstBoxChild = firstBoxChild.getNext();
    }

    // now we've reached the split point, make a sanity test.
    final long splitPos = splitPoint - firstBox.getEffectiveLayoutSize(axis);
    if (splitPos < 0)
    {
      throw new IllegalStateException("The selected child split is not valid: " + splitPos);
    }

    // prepare the second box.
    InlineRenderBox secondBox = (InlineRenderBox) derive(false);
    secondBox.setBoxDefinition(boxes[1]);
    int secondNodeCounter = 0;

    if (firstSplitChild.isDiscardable() == false)
    {

      // the split pos is directly after the first box's end. So we can add
      // the child to the second box without having to split it.
      if (splitPos == 0)
      {
        //    Log.debug("Not splitting, deriving a second box. ");
        secondBox.addChild(firstSplitChild.derive(true));
        secondNodeCounter += 1;
      }
      else if (splitPos <= firstSplitChild.getPreferredSize(axis))
      {
        // no split needed, the child will fit perfectly.
        firstBox.addChild(firstSplitChild.derive(true));
        firstNodeCounter += 1;
      }
      else
      {
        //      Log.debug("       Extra Info: PS: " + splitPointChild.getPreferredSize(axis));
        //      Log.debug("       Extra Info: FB: " + splitPointChild.getFirstBreak(axis));
        //      Log.debug("       Extra Info: BP: " + splitPointChild.getBestBreak(axis, splitPos));
        //      Log.debug("       Extra Info:*BP: " + getBestBreak(axis, splitPos));
        //      Log.debug("       Extra Info:*FP: " + getFirstBreak(axis));
        long bestBreakPos = firstSplitChild.getBestBreak(axis, splitPos);
        if (bestBreakPos > 0)
        {
          target = firstSplitChild.split(axis, bestBreakPos, target);
          firstBox.addChild(target[0]);
          firstNodeCounter += 1;
          if (target[1] != null)
          {
            secondNodeCounter += 1;
            secondBox.addChild(target[1]);
            firstSplitChild = target[1];
          }
          else
          {
            firstSplitChild = target[0];
          }
        }
        else
        {
          long firstBreakPos = firstSplitChild.getFirstBreak(axis);
          if (firstBreakPos > 0)
          {
            target = firstSplitChild.split(axis, firstBreakPos, target);
            firstBox.addChild(target[0]);
            firstNodeCounter += 1;
            if (target[1] != null)
            {
              secondNodeCounter += 1;
              secondBox.addChild(target[1]);
              firstSplitChild = target[1];
            }
            else
            {
              firstSplitChild = target[0];
            }
          }
          else
          {
            // there is no best break position, so we add the child to the
            // second box.
            secondBox.addChild(firstSplitChild.derive(true));
            secondNodeCounter += 1;
          }
        }
      }
    }

    RenderNode postChild = firstSplitChild.getNext();
    while (postChild != null)
    {
      if (secondNodeCounter == 0)
      {
        if (postChild.isDiscardable())
        {
          postChild = postChild.getNext();
          continue;
        }
      }
      // first, add everything up to the child .
      secondBox.addChild(postChild.derive(true));
      secondNodeCounter += 1;
      postChild = postChild.getNext();
    }

    Log.debug("Result: " + firstNodeCounter + " " + secondNodeCounter);
    if (firstNodeCounter == 0)
    {
      Log.debug("PERFORMANCE: This split is avoidable.");
      // correct the second box. The box is not split.
      secondBox.setBoxDefinition(getBoxDefinition());
      target[0] = new SpacerRenderNode();
      target[1] = secondBox;
    }
    else if (secondNodeCounter == 0)
    {
      Log.warn("PERFORMANCE: Invalid split implementation. Second box should never be empty.");
      firstBox.setBoxDefinition(getBoxDefinition());
      target[0] = firstBox;
      target[1] = null;
    }
    else
    {
      target[0] = firstBox;
      target[1] = secondBox;
    }
    //   Log.debug("LEAVE SPLIT : " + this);

    // make sure that noone works with that anymore ..
    return target;
  }

  public boolean isOpen()
  {
    return open;
  }

  public void setOpen(final boolean open)
  {
    this.open = open;
  }
}
