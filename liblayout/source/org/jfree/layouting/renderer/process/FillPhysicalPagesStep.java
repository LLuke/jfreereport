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
 * FillPhysicalPagesStep.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: FillPhysicalPagesStep.java,v 1.1 2006/10/27 18:28:08 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.process;

import org.jfree.layouting.renderer.model.PageAreaRenderBox;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.util.Log;

/**
 * This Step copies all content from the logical page into the page-grid. When
 * done, it clears the content and replaces the elements with dummy-nodes. These
 * nodes have a fixed-size (the last known layouted size), and will not be
 * recomputed later.
 * <p/>
 * Adjoining dummy-nodes get unified into a single node, thus simplifying and
 * pruning the document tree.
 *
 * @author Thomas Morgner
 */
public class FillPhysicalPagesStep extends IterateVisualProcessStep
{
  private long pageSize;

  public FillPhysicalPagesStep()
  {
  }

  public LogicalPageBox compute(final LogicalPageBox pagebox,
                                final long pageStart,
                                final long pageEnd)
  {
    this.pageSize = pageEnd - pageStart;

    // This is a simpel strategy.
    // Copy and relocate, then prune. (I whished we could prune first, but
    // this does not work.)
    //
    // For the sake of efficiency, we do *not* create private copies for each
    // phyiscal page. This would be an total overkill.
    final LogicalPageBox derived = (LogicalPageBox) pagebox.derive(true);

    // reorganize ...
    final long headerSize = derived.getHeaderArea().getHeight();

    // first, shift the normal-flow content downwards.
    // The start of the logical pagebox might be in the negative range now
    BoxShifter boxShifter = new BoxShifter();
    final long normalFlowShift = -(pageStart - headerSize);
    boxShifter.shiftBoxUnchecked(derived, normalFlowShift);

    // Then add the header at the top - it starts at (0,0) and thus it is
    // ok to leave it unshifted.
    derived.insertFirst(derived.getHeaderArea());
    // finally, move the footer at the bottom (to the page's bottom, please!)
    final PageAreaRenderBox footerArea = derived.getFooterArea();
    final long footerPosition = pageSize - (footerArea.getY() + footerArea.getHeight());
    boxShifter.shiftBoxUnchecked(footerArea, footerPosition);
    derived.insertLast(footerArea);

    startProcessing(derived);
    return derived;
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    processBoxChilds(box);
  }

  protected boolean startBlockLevelBox(final RenderBox box)
  {
    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      if ((node.getY() + node.getHeight()) <= 0)
      {
        final RenderNode next = node.getNext();
        box.remove(node);
        node = next;
      }
      else if (node.getY() >= pageSize)
      {
        final RenderNode next = node.getNext();
        box.remove(node);
        node = next;
      }
      else
      {
        node = node.getNext();
      }
    }
    return true;
  }
}
