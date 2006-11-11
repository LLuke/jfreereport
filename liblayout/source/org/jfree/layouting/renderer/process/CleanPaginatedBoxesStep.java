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
 * CleanPaginatedBoxesStep.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: CleanPaginatedBoxesStep.java,v 1.3 2006/11/09 14:28:49 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.process;

import org.jfree.layouting.renderer.model.FinishedRenderNode;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.table.TableRowRenderBox;
import org.jfree.layouting.renderer.model.table.TableSectionRenderBox;

/**
 * Creation-Date: 27.10.2006, 18:19:24
 *
 * @author Thomas Morgner
 */
public class CleanPaginatedBoxesStep extends IterateVisualProcessStep
{
  private long pageOffset;

  public CleanPaginatedBoxesStep()
  {
  }

  public void compute(LogicalPageBox pageBox)
  {
    pageOffset = pageBox.getPageOffset();
    startProcessing(pageBox);
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    // we do not process the paragraph lines. This should have been done
    // in the startblock thing and they get re-added anyway as long as the
    // paragraph is active.
  }

  protected boolean startBlockLevelBox(final RenderBox box)
  {
    if (box instanceof ParagraphRenderBox)
    {
      return false;
    }

    if (box instanceof TableSectionRenderBox)
    {
      // Table sections dont get removed at all.
      return true;
    }

    if (box instanceof TableRowRenderBox)
    {
      // TableRows dont get removed now. We remove them when the close-event
      // gets fired, and only if all cells have been replaced by removed-
      // placeholders
      return startTableRow((TableRowRenderBox) box);
    }

    final RenderNode node = box.getVisibleFirst();
    if (node == null)
    {
      // The cell is empty ..
      return false;
    }

    final long nodeY = node.getY();
    if ((nodeY + node.getHeight()) > pageOffset)
    {
      // we cant handle that. At least parts of the node will be visible ..

      if (nodeY > pageOffset)
      {
        // all childs will be visible too, so why visiting them ...
        return false;
      }
      return true;
    }

    // Next, search the last node that is fully invisible. We collapse all
    // invisible node into one big box for efficiency reasons. They wont be
    // visible anyway and thus the result will be the same as if they were
    // still alive ..
    RenderNode last = node;
    for(;;)
    {
      final RenderNode next = last.getVisibleNext();
      if (next == null)
      {
        break;
      }

      if ((next.getY() + next.getHeight()) > pageOffset)
      {
        // we cant handle that. This node will be visible. So the current last
        // node is the one we can shrink ..
        break;
      }
      last = next;
    }

    // So lets get started. We remove all nodes between (and inclusive)
    // node and last.
    final long width = box.getContentAreaX2() - box.getContentAreaX1();
    final long height = last.getY() + last.getHeight() - nodeY;
    final FinishedRenderNode replacement = new FinishedRenderNode(width, height);

    RenderNode removeNode = node;
    while (removeNode != last)
    {
      final RenderNode next = removeNode.getNext();
      box.remove(removeNode);
      removeNode = next;
    }
    box.replaceChild(last, replacement);
    if (replacement.getParent() != box)
    {
      throw new IllegalStateException();
    }
    return (box.getLastChild() != replacement);
  }

  private boolean startTableRow(final TableRowRenderBox box)
  {
    // if the row's cells are all invisible, remove the row itself.
    // todo: Shall we do this? For now, it might be sufficient to stick with
    // the cleaned cells ..
    return true;
  }
}
