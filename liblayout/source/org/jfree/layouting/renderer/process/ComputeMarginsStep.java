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
 * ComputeSimpleMarginsStep.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.process;

import java.util.ArrayList;

import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.BoxLayoutProperties;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.table.TableCellRenderBox;
import org.jfree.layouting.renderer.model.table.TableRowRenderBox;
import org.jfree.layouting.renderer.model.table.TableSectionRenderBox;

/**
 * This semi-dynamic step computes the effective margins. It requires that the
 * model is fully computed and that all structural errors have been resolved.
 * <p/>
 * Computing the effective margins is a recursive computation. It depends on the
 * already evaluated effective margin of the parent (for top margins), the
 * predecessor and parent (for left) and the last child (for bottom and right
 * margins).
 *
 * Todo: Some better change management. Right now everything is recomputed all the time.
 *
 * @author Thomas Morgner
 */
public class ComputeMarginsStep extends IterateVisualProcessStep
{
  /*
   * Quoting the Specs: CSS3-Box: Chapter 15:
   *
   * Collapsing Margins applies ony to Block-Level elements. InlineLeve
   * elements never collapse their margins.
   *
   * Margins of floating boxes never collapse, neither with normal flow boxes
   * nor with other floating boxes.
   *
   * Note that margins of absolutely positioned boxes also do not collapse
   * with any other margins.
   *
   * In a horizontal flow, the following margins collapse:
   *
   * 1. The bottom margin of a box and the bottom margin of its last child,
   *    provided both are block-level and the former has no bottom padding,
   *    no bottom border and horizontal flow.
   * 2. The top margin of a box and the top margin of its first child,
   *    provided both are block-level and the former has no top padding,
   *    no top border and horizontal flow.
   * 3. The bottom margin of a box and the top margin of its next sibling,
   *    provided both are block-level.
   * 4. The top and bottom margin of a block-level box, if the the box has a
   *    height of 'auto' or 0, no top or bottom padding, no top or bottom
   *    border and no content (i.e., no line boxes and no replaced content).
   */
  /*
   * Implementation:
   *
   * Margin computations can be collapsed. The effective margins of all
   * collapsing elements of an edge follow a simple rule: The lowest element
   * in the tree (the one with the minimum distance to the root of the tree)
   * gets it all.
   *
   * For top margins, the first box of a collapsible top-edge collection
   * (all direct first childs, that do not have a border or padding defined)
   * receives the computed margin; all other boxes receive a margin of zero.
   *
   * The resulting margin will be collapsed with the effective bottom margin
   * of the previous silbling.
   *
   * For bottom margins, the same rules apply. Computing the margins happens
   * in the deepest possible position, applies to all collapsible margins of
   * the margin collection, and the margin is applied to the deepest component.
   *
   * This implementation depends on a proper state management. By putting the
   * whole burden of computing the margin to the first node of the edge, all
   * other nodes must not recompute the margins (as they only dive into one
   * direction.
   */

  private long marginChangeKey;

  public ComputeMarginsStep()
  {
  }

  public void compute(LogicalPageBox root)
  {
    marginChangeKey = root.getChangeTracker();
    startProcessing(root);
    marginChangeKey = 0;
  }


  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    startProcessing(box.getPool());
  }

  /**
   * This computes the top and left margin. The effective margin is always zero,
   * if the box has no parent at all.
   *
   * @param box
   */
  protected boolean startBlockLevelBox(final RenderBox box)
  {
    final BoxLayoutProperties blp = box.getBoxLayoutProperties();
//    if (blp.getMarginCloseState() == marginChangeKey)
//    {
//      return false;
//    }

    if (blp.getMarginOpenState() == marginChangeKey)
    {
      // no changes, but still, we have to dive deep ..
      return true;
    }

    // Inner table boxes have no margins at all ...
    if (box instanceof TableRowRenderBox ||
        box instanceof TableSectionRenderBox ||
        box instanceof TableCellRenderBox)
    {
      blp.setMarginOpenState(marginChangeKey);
      return true;
    }

    final boolean infiniteMarginTop;
    final RenderBox boxParent = box.getParent();
    if (boxParent == null)
    {
      infiniteMarginTop = (box.getPrev() == null);
    }
    else
    {
      final BoxLayoutProperties pBlp = boxParent.getBoxLayoutProperties();
      infiniteMarginTop =
          (pBlp.isInfiniteMarginTop() && box.getPrev() == null &&
          pBlp.getBorderTop() == 0 && pBlp.getPaddingTop() != 0);
    }

    final ArrayList marginCollection = new ArrayList();

    // Collect all elements that will contribute to the margins.
    RenderBox marginBox = box;
    for (;;)
    {
      marginCollection.add(marginBox);

      final BoxLayoutProperties cblp = marginBox.getBoxLayoutProperties();
      if (cblp.getBorderBottom() != 0)
      {
        break;
      }
      if (cblp.getPaddingBottom() != 0)
      {
        break;
      }

      final RenderNode node = marginBox.getFirstChild();
      if (node instanceof RenderBox == false)
      {
        break;
      }

      marginBox = (RenderBox) node;
    }

    // If we are the first child on an infinite margin area, copy the
    // infinite area to yourself ..
    if (infiniteMarginTop)
    {
      for (int i = 0; i < marginCollection.size(); i++)
      {
        RenderBox renderBox = (RenderBox) marginCollection.get(i);
        final BoxLayoutProperties cblp = renderBox.getBoxLayoutProperties();
        cblp.setEffectiveMarginTop(0);
        cblp.setInfiniteMarginTop(true);
        cblp.setMarginOpenState(marginChangeKey);
      }
      return true;
    }

    // Compute the top margin.
    long topMarginPositive = 0;
    long topMarginNegative = 0;
    final long marginTop = blp.getMarginTop();
    if (marginTop < 0)
    {
      topMarginNegative = marginTop;
    }
    else
    {
      topMarginPositive = marginTop;
    }

    if (box.getPrev() instanceof BlockRenderBox)
    {
      final RenderBox prevBox = (RenderBox) box.getPrev();
      final long effectiveMarginBottom =
          prevBox.getBoxLayoutProperties().getEffectiveMarginBottom();

      if (effectiveMarginBottom < 0)
      {
        topMarginNegative = Math.min(topMarginNegative, effectiveMarginBottom);
      }
      else
      {
        topMarginPositive = Math.max(topMarginPositive, effectiveMarginBottom);
      }
    }

    // Dive into all other childs, and set their effective margin to zero.
    // Perform the collapsing, but the result only affects the first child.
    // The margin is already handled by the first element.
    for (int i = 1; i < marginCollection.size(); i++)
    {
      RenderBox renderBox = (RenderBox) marginCollection.get(i);
      final BoxLayoutProperties cblp = renderBox.getBoxLayoutProperties();
      final long childMarginTop = cblp.getMarginTop();

      if (childMarginTop < 0)
      {
        topMarginNegative = Math.min(topMarginNegative, childMarginTop);
      }
      else
      {
        topMarginPositive = Math.max(topMarginPositive, childMarginTop);
      }
      cblp.setMarginOpenState(marginChangeKey);
      cblp.setInfiniteMarginTop(false);
      cblp.setEffectiveMarginTop(0);
    }

    final long effectiveMargin = topMarginPositive + topMarginNegative;
    blp.setEffectiveMarginTop(effectiveMargin);
    blp.setInfiniteMarginTop(false);
    blp.setMarginOpenState(marginChangeKey);
    return true;
  }

  /**
   * On our way out, compute the bottom and right margins as well.
   *
   * @param box
   */
  protected void finishBlockLevelBox(final RenderBox box)
  {
    final BoxLayoutProperties blp = box.getBoxLayoutProperties();

    if (blp.getMarginCloseState() == marginChangeKey)
    {
      return;
    }

    if (box instanceof TableCellRenderBox ||
        box instanceof TableRowRenderBox ||
        box instanceof TableSectionRenderBox)
    {
      // They do not have any margins at all  ..
      blp.setMarginCloseState(marginChangeKey);
      return;
    }

    // Collect all elements that will contribute to the margins.
    final ArrayList marginContext = new ArrayList();
    RenderBox marginBox = box;
    for (;;)
    {
      marginContext.add(marginBox);
      if (marginBox.getNext() != null)
      {
        break;
      }

      final RenderBox parent = marginBox.getParent();
      if (parent == null)
      {
        break;
      }
      if (parent.getBoxLayoutProperties().getBorderBottom() != 0)
      {
        break;
      }
      if (parent.getBoxLayoutProperties().getPaddingBottom() != 0)
      {
        break;
      }
      marginBox = parent;
    }

    // Check, whether we have an infinite margin ..
    final RenderBox lastBox = (RenderBox)
        marginContext.get(marginContext.size() - 1);
    if (lastBox.getParent() == null)
    {
      // Looks like that's it.
      // Strive for the simple solution here.
      for (int i = 0; i < marginContext.size(); i++)
      {
        RenderBox renderBox = (RenderBox) marginContext.get(i);
        BoxLayoutProperties cblp = renderBox.getBoxLayoutProperties();
        cblp.setMarginCloseState(marginChangeKey);
        cblp.setInfiniteMarginBottom(true);
        cblp.setEffectiveMarginBottom(0);
      }
      return;
    }

    long marginNegative = 0;
    long marginPositive = 0;

    // Collapsing the margins. Seek the big one ..
    for (int i = 0; i < marginContext.size(); i++)
    {
      RenderBox renderBox = (RenderBox) marginContext.get(i);
      BoxLayoutProperties cblp = renderBox.getBoxLayoutProperties();
      cblp.setMarginCloseState(marginChangeKey);
      cblp.setInfiniteMarginBottom(false);
      cblp.setEffectiveMarginBottom(0);

      marginNegative = Math.min (marginNegative, cblp.getMarginBottom());
      marginPositive = Math.max (marginPositive, cblp.getMarginBottom());
    }

    final BoxLayoutProperties lblp = lastBox.getBoxLayoutProperties();
    lblp.setEffectiveMarginBottom(marginPositive + marginNegative);
    lblp.setInfiniteMarginBottom(false);
    lblp.setMarginCloseState(marginChangeKey);
  }

  private boolean isCollapsibleTop (RenderNode node)
  {
    if (node instanceof RenderBox == false)
    {
      return false;
    }
    final RenderBox marginBox = (RenderBox) node;
    final BoxLayoutProperties blp = marginBox.getBoxLayoutProperties();
    if (blp.getBorderTop() != 0)
    {
      return false;
    }
    if (blp.getPaddingTop() != 0)
    {
      return false;
    }
    // This one is collapsible ..
    return true;
  }
}
