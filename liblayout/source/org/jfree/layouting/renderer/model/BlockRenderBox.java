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
 * BlockRenderBox.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: BlockRenderBox.java,v 1.8 2006/07/22 15:28:50 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model;

import org.jfree.util.Log;
import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.renderer.text.ExtendedBaselineInfo;
import org.jfree.layouting.input.style.values.CSSValue;

/**
 * A block box behaves according to the 'display:block' layouting rules. In the
 * simplest case, all childs will consume the complete width and get stacked
 * below each other.
 * <p/>
 * Live would be boring if everything was simple: Floats and absolutly
 * positioned elements enrich and complicate the layout schema. Absolute
 * elements do not affect the normal flow, but inject an invisible placeholder
 * to keep track of their assumed normal-flow position.
 * <p/>
 * (Using Mozilla's behaviour as reference, I assume that absolutly positioned
 * elements are controled from outside the normal-flow (ie. parallel to the
 * normal flow, but positioned relative to their computed placeholder position
 * inside the normal flow. Weird? yes!)
 * <p/>
 * Float-elements are positioned inside the block context and reduce the size of
 * the linebox. Float-elements are positioned once when they are defined and
 * will not alter any previously defined content.
 * <p/>
 * As floats from previous boxes may overlap with later boxes, all floats must
 * be placed on a global storage. The only difference between absolutely
 * positioned and floating elements is *where* they can be placed, and whether
 * they influence the content under them. (Floats do, abs-pos dont).
 *
 * @author Thomas Morgner
 */
public class BlockRenderBox extends RenderBox
{
  public BlockRenderBox(final BoxDefinition boxDefinition,
                        final CSSValue valign)
  {
    super(boxDefinition, valign);

    // hardcoded for now, content forms lines, which flow from top to bottom
    // and each line flows horizontally (later with support for LTR and RTL)

    // Major axis vertical means, all childs will be placed below each other
    setMajorAxis(VERTICAL_AXIS);
    // Minor axis horizontal: All childs may be shifted to the left or right
    // to do some text alignment
    setMinorAxis(HORIZONTAL_AXIS);
  }

  public void validate()
  {
    final RenderNodeState state = getState();
    if (state == RenderNodeState.FINISHED)
    {
      return;
    }
    if (state == RenderNodeState.UNCLEAN)
    {
      setState(RenderNodeState.PENDING);
    }
    if (state == RenderNodeState.PENDING)
    {
      validateBorders();
      validatePaddings();
      setState(RenderNodeState.LAYOUTING);
    }

    validateMargins();

    Log.debug("BLOCK: Begin Validate");

    final long leadingPaddings = getLeadingInsets(getMinorAxis());
    final long trailingPaddings = getTrailingInsets(getMinorAxis());

    long nodePos =
            getPosition(getMajorAxis()) + getLeadingInsets(getMajorAxis());
    final long minorAxisNodePos =
            getPosition(getMinorAxis()) + leadingPaddings;


    final long defaultNodeWidth = Math.max(0,
            getDimension(getMinorAxis()) - leadingPaddings - trailingPaddings);

    long trailingMajor = 0;
    long trailingMinor = 0;
    RenderNode node = getFirstChild();
    while (node != null)
    {
      if (node.isIgnorableForRendering())
      {
        // Ignore all empty childs. However, give it an position.
        node.setPosition(getMajorAxis(), nodePos);
        node.setPosition(getMinorAxis(), minorAxisNodePos);
        node.setDimension(getMinorAxis(), 0);
        node.setDimension(getMajorAxis(), 0);
        node = node.getNext();
        continue;
      }

//      final long layoutSize = node.getEffectiveLayoutSize(getMinorAxis());
//
//      final long nodeSizeMinor;
//      if (defaultNodeWidth < layoutSize)
//      {
//        nodeSizeMinor = layoutSize;
//      }
//      else
//      {
//        nodeSizeMinor = defaultNodeWidth;
//      }
      final long nodeSizeMinor = defaultNodeWidth;
      final long leadingMinor = Math.max
              (node.getLeadingSpace(getMinorAxis()), trailingMinor);

      final long leadingMajor = Math.max
              (node.getLeadingSpace(getMajorAxis()), trailingMajor);
      nodePos += leadingMajor;

      node.setPosition(getMajorAxis(), nodePos);
      node.setPosition(getMinorAxis(), minorAxisNodePos + leadingMinor);
      node.setDimension(getMinorAxis(), nodeSizeMinor);
      node.setDimension(getMajorAxis(), node.getEffectiveLayoutSize(getMajorAxis()));
      node.validate();

      trailingMajor = node.getTrailingSpace(getMajorAxis());
      trailingMinor = node.getTrailingSpace(getMinorAxis());

      nodePos += node.getDimension(getMajorAxis());
      node = node.getNext();
    }

    final long trailingInsets = getTrailingInsets(getMajorAxis());
    setDimension(getMajorAxis(), trailingMajor + (nodePos + trailingInsets) - getPosition(getMajorAxis()));
    setDimension(getMinorAxis(), defaultNodeWidth + leadingPaddings + trailingPaddings);

    Log.debug("BLOCK: Leave Validate: " + defaultNodeWidth + " " +
            leadingPaddings + " " + trailingPaddings);
    setState(RenderNodeState.FINISHED);
  }

  protected long getComputedBlockContextWidth()
  {
    final RenderBox parent = getParent();
    final RenderLength preferredWidth = getBoxDefinition().getPreferredWidth();
    if (parent != null)
    {
      if (RenderLength.AUTO.equals(preferredWidth) == false)
      {
        return preferredWidth.resolve
                (parent.getComputedBlockContextWidth());
      }
      else
      {
        return parent.getComputedBlockContextWidth();
      }
    }
    return preferredWidth.resolve(0);
  }

  public int getBreakability(int axis)
  {
    return SOFT_BREAKABLE;
  }

  /**
   * Returns the baseline info for the given node. This can be null, if the node
   * does not have any baseline info.
   *
   * @return
   */
  public ExtendedBaselineInfo getBaselineInfo()
  {
    long shift = getLeadingInsets(getMajorAxis()) + getLeadingSpace(getMajorAxis());
    RenderNode firstChild = getFirstChild();
    while (firstChild != null)
    {
      if (firstChild.isIgnorableForRendering() == false)
      {
        final ExtendedBaselineInfo baselineInfo = firstChild.getBaselineInfo();
        if (baselineInfo == null)
        {
          return null;
        }

        return baselineInfo.shift(shift);
      }
      firstChild = firstChild.getNext();
    }
    return null;
  }

}
