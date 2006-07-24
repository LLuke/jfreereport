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
 * $Id: InlineRenderBox.java,v 1.7 2006/07/18 14:40:28 taqua Exp $
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

//
//  public long getMinimumChunkSize(int axis)
//  {
//    // todo: Adjacent chunks needs to be added or we run into trouble later.
//    // but as said .. later :)
//
//    // maybe we need something like getFirstChunk, getlastChunk or so.
//    return super.getMinimumChunkSize(axis);
//  }

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

          final long nodeHeightAbove =
                  node.getReferencePoint(getMinorAxis()) +
                          node.getLeadingSpace(getMinorAxis());
          final long prefSize = node.getEffectiveLayoutSize(getMajorAxis());
          node.setPosition(getMajorAxis(), nodePos - prefSize - node.getTrailingSpace(getMajorAxis()));
          node.setPosition(getMinorAxis(), minorAxisNodePos + (heightAbove - nodeHeightAbove));
          node.setDimension(getMajorAxis(), prefSize);
          node.validate();
          node = node.getNext();
          continue;
        }
      }
      final long nodeHeightAbove =
              node.getReferencePoint(getMinorAxis()) +
                      node.getLeadingSpace(getMinorAxis());
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
