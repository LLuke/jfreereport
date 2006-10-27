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
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.process;

import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.FinishedRenderNode;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;

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

    final RenderNode node = box.getFirstChild();
    if (node == null)
    {
      return false;
    }


    if ((node.getY() + node.getHeight()) > pageOffset)
    {
      // we cant handle that. This node will be visible ..

      if (node.getY() > pageOffset)
      {
        // all childs will be visible to, so why visiting them ...
        return false;
      }
      return true;
    }

    RenderNode last = node;
    for(;;)
    {
      final RenderNode next = last.getNext();
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
    final long height = last.getY() + last.getHeight() - node.getY();
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
}
