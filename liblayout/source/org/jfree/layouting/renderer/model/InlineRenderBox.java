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
 * InlineRenderBox.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: InlineRenderBox.java,v 1.5 2006/07/17 13:27:25 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model;

import org.jfree.util.Log;

/**
 * An inline box is some floating text that might be broken down into lines. The
 * actual linebreaking is performed/initiated by the block context.
 * <p/>
 * Breaking behaviour: An InlineBox forwards all breaks to its childs and tries
 * to break them according to their rules. BlockContent and ImageContent is
 * considered not-very-breakable by default, breaks on that should be avoided.
 *
 * @author Thomas Morgner
 */
public class InlineRenderBox extends RenderBox
{

  public InlineRenderBox(final BoxDefinition boxDefinition)
  {
    super(boxDefinition);

    // hardcoded for now, content forms lines, which flow from top to bottom
    // and each line flows horizontally (later with support for LTR and RTL)

    // Major axis: All child boxes are placed from left-to-right
    setMajorAxis(HORIZONTAL_AXIS);
    // Minor: The childs might be aligned on their position (shifted up or down)
    setMinorAxis(VERTICAL_AXIS);
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
    while (firstSplitChild != null && firstSplitSize <= splitPoint)
    {
      final long prefSize =
              firstSplitChild.getEffectiveLayoutSize(axis) +
              firstSplitChild.getLeadingSpace(axis) +
              firstSplitChild.getTrailingSpace(axis);
      if (prefSize + firstSplitSize <= splitPoint)
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
    BoxDefinition[] boxes = getBoxDefinition().splitVertically();
    InlineRenderBox firstBox = (InlineRenderBox) derive(false);
    firstBox.setBoxDefinition(boxes[0]);

    // Now add everything up to the split point to the first box.
    int firstNodeCounter = 0;
    RenderNode firstBoxChild = getFirstChild();
    while (firstBoxChild != null && firstSplitChild != firstBoxChild)
    {
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
    int secondSize = 0;

    // the split pos is directly after the first box's end. So we can add
    // the child to the second box without having to split it.
    if (splitPos == 0)
    {
      //    Log.debug("Not splitting, deriving a second box. ");
      secondBox.addChild(firstSplitChild.derive(true));
      secondSize += 1;
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
          secondSize += 1;
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
            secondSize += 1;
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
          secondSize += 1;
        }
      }
    }

    RenderNode postChild = firstSplitChild.getNext();
    while (postChild != null)
    {
      // first, add everything up to the child .
      secondBox.addChild(postChild.derive(true));
      secondSize += 1;
      postChild = postChild.getNext();
    }

    //   Log.debug("Result: " + firstSize + " " + secondSize);
    if (firstNodeCounter == 0)
    {
      // Log.debug("PERFORMANCE: This split is avoidable.");
      // correct the second box. The box is not split.
      secondBox.setBoxDefinition(getBoxDefinition());
      target[0] = new SpacerRenderNode();
      target[1] = secondBox;
    }
    else if (secondSize == 0)
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
      final BreakAfterEnum breakAfterAllowed = child.getBreakAfterAllowed();
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

      final BreakAfterEnum breakAfterAllowed = child.getBreakAfterAllowed();
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

  public long getMinimumChunkSize(int axis)
  {
    // todo: Adjacent chunks needs to be added or we run into trouble later.
    // but as said .. later :)

    // maybe we need something like getFirstChunk, getlastChunk or so.
    return super.getMinimumChunkSize(axis);
  }

  public void validate()
  {
    final RenderNodeState state = getState();
    if (state == RenderNodeState.FINISHED)
    {
      return; // nothing to do
    }

    if (state == RenderNodeState.UNCLEAN)
    {
      // ok, recompute the margins, paddings and border-sizes and get me a
      // valid imageable-area.

      // For now, I assume a MBP (margin-border-padding) size of 10, just
      // to get some visual appearance..
      setState(RenderNodeState.PENDING);
    }

    if (state == RenderNodeState.PENDING)
    {
      validateBorders();
      validatePaddings();
      setState(RenderNodeState.LAYOUTING);
    }

    validateMargins();

    long nodePos =
            getPosition(getMajorAxis()) + getLeadingInsets(getMajorAxis());

    if (nodePos < 0)
    {
      throw new IllegalStateException("NodePos cannot be negative");
    }

    final long lineHeight = 0; // this needs to be read from the stylesheet ..
    final long minorInsets = getTrailingInsets(getMinorAxis()) +
            getLeadingInsets(getMinorAxis());
    long effectiveHeight = Math.max(lineHeight,
            getPreferredSize(getMinorAxis()) - minorInsets);

    final long minorAxisNodePos =
            getPosition(getMinorAxis()) + getLeadingInsets(getMinorAxis());


    final long heightAbove = getReferencePoint(getMinorAxis());
    long trailingMajor = 0;
    long trailingMinor = 0;
    RenderNode node = getFirstChild();
    while (node != null)
    {
      if (node.isIgnorableForRendering())
      {
        // Ignore all empty childs. However, give it an position.
        node.setPosition(getMajorAxis(), nodePos);
        node.setPosition(getMinorAxis(), minorAxisNodePos + heightAbove);
        node.setDimension(getMinorAxis(), 0);
        node.setDimension(getMajorAxis(), 0);
        node = node.getNext();
        continue;
      }

      if (node instanceof MarkerRenderBox)
      {
        MarkerRenderBox mrb = (MarkerRenderBox) node;
        if (mrb.isOutside())
        {
          // marker processing is different ...
          // The box is positioned outside of the principal box.
          // Margins do not apply (for now).

          final long nodeHeightAbove = node.getReferencePoint(getMinorAxis());
          final long prefSize = node.getEffectiveLayoutSize(getMajorAxis());
          node.setPosition(getMajorAxis(), nodePos - prefSize - node.getTrailingSpace(getMajorAxis()));
          node.setPosition(getMinorAxis(), minorAxisNodePos + (heightAbove - nodeHeightAbove));
          node.setDimension(getMajorAxis(), prefSize);
          node.validate();
          node = node.getNext();
          continue;
        }
      }
      final long nodeHeightAbove = node.getReferencePoint(getMinorAxis());
      final long leadingMinor = Math.max
              (node.getLeadingSpace(getMinorAxis()), trailingMinor);
      final long leadingMajor = Math.max
              (node.getLeadingSpace(getMajorAxis()), trailingMajor);
      nodePos += leadingMajor;

      node.setPosition(getMajorAxis(), nodePos);
      node.setPosition(getMinorAxis(), minorAxisNodePos + (heightAbove - nodeHeightAbove) + leadingMinor);
      node.setDimension(getMajorAxis(), node.getEffectiveLayoutSize(getMajorAxis()));
      node.setDimension(getMinorAxis(), node.getEffectiveLayoutSize(getMinorAxis()));
      node.validate();

      trailingMajor = node.getTrailingSpace(getMajorAxis());
      trailingMinor = node.getTrailingSpace(getMinorAxis());

      effectiveHeight = Math.max (effectiveHeight, node.getDimension(getMinorAxis()));
      nodePos += node.getDimension(getMajorAxis());
      node = node.getNext();
    }


    final long trailingInsets = getTrailingInsets(getMajorAxis());
    setDimension(getMajorAxis(), trailingMajor + (nodePos + trailingInsets) - getPosition(getMajorAxis()));
    setDimension(getMinorAxis(), trailingMinor + effectiveHeight + minorInsets);

    setState(RenderNodeState.FINISHED);
  }

  /**
   * search all childs for splittable content. This is something static, we dive
   * through and perform splits based on the clear-left and clear-right flag.
   * <p/>
   * multiple clear-lefts can be merged into a single one. &lt;span
   * style="clear: left"&gt;&lt;span style="clear: left"&gt;&lt/span>&lt/span&gt;
   * still causes only one linebreak, as the second one has no effect, if the
   * linebox is already empty.
   * <p/>
   * One thing to note: We do not take any sizes into account here. Whether a
   * line eats one or one million points does not matter.
   *
   * @param isStartOfLine whether this box would be the first child in a new
   *                      linebox. All firstChilds of a first element are also
   *                      considered to be at the start of the line.
   * @return true, if we need to force a split, false otherwise.
   */
  public boolean isForcedSplitNeeded(boolean isEndOfLine)
  {
    // never check your own clear properties. We dont know the context of
    // these...
    RenderNode child = getFirstChild();
    while (child != null)
    {
      final boolean endOfLine = (child.getNext() == null) && isEndOfLine;
      if (child.isForcedSplitNeeded(endOfLine))
      {
        return true;
      }
      child = child.getNext();
    }
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
    // never check your own clear properties. We dont know the context of
    // these...
    RenderNode child = getFirstChild();
    while (child != null)
    {
      final boolean endOfLine = (child.getNext() == null) && isEndOfLine;
      if (child.isForcedSplitNeeded(endOfLine))
      {
        target = child.splitForLinebreak(endOfLine, target);
        InlineRenderBox splitted =
                split(null, getFirstChild(), child.getPrev());
        splitted.addChild(target[0]);
        target[0] = splitted;
        target[1] = split(target[1], child.getNext(), getLastChild());
        return target;
      }
      if (child.isForcedSplitRequested(endOfLine))
      {
        if (target == null || target.length < 2)
        {
          target = new RenderNode[2];
        }
        target[0] = split(null, getFirstChild(), child);
        target[1] = split(null, child.getNext(), getLastChild());
        return target;
      }
      child = child.getNext();
    }
    return null;
  }

  /**
   * @param first    is assumed to be already derived ..
   * @param boundary
   * @return
   */
  private InlineRenderBox split(RenderNode prefix,
                                RenderNode first,
                                RenderNode last)
  {
    InlineRenderBox rb = (InlineRenderBox) derive(false);
    if (prefix != null)
    {
      rb.addChild(prefix);
    }
    RenderNode node = first;
    while (node != null)
    {
      rb.addChild(node.derive(true));
      if (node == last)
      {
        break;
      }
      node = node.getNext();
    }
    return rb;
  }

  public int getBreakability(int axis)
  {
    if (axis == getMajorAxis())
    {
      return SOFT_BREAKABLE;
    }
    return UNBREAKABLE;
  }
}
