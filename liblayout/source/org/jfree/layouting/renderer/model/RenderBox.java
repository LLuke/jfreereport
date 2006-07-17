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
 * $Id: RenderBox.java,v 1.4 2006/07/14 14:34:41 taqua Exp $
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

  public RenderBox(final BoxDefinition boxDefinition)
  {
    if (boxDefinition == null)
    {
      throw new NullPointerException();
    }
    this.boxDefinition = boxDefinition;
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

  public void addChild(final RenderNode child)
  {
    if (child == null)
    {
      throw new NullPointerException
              ("Child to be added must not be null.");
    }

    if (isOpen() == false)
    {
      throw new NullPointerException
              ("Closed: " + this);
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
    target[0] = new SpacerRenderNode();
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
  protected long getEffectiveLayoutSize(int axis)
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
        final long contextWidth = getPreferredSize(axis);
        final long l = contextWidth - margins;
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
          return firstChild.getReferencePoint(axis);
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
    return referencePoint;
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

    super.close();
    if (isDiscardable())
    {
      if (getParent() != null)
      {
        getParent().remove(this);
      }
    }
    else
    {
      Log.debug("not discardable " + this);
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


}
