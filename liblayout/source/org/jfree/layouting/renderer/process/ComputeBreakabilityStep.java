/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.layouting.renderer.process;

import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.table.TableRenderBox;
import org.jfree.layouting.renderer.model.table.TableRowRenderBox;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;

/**
 * Creation-Date: 06.11.2006, 19:54:20
 *
 * @author Thomas Morgner
 */
public class ComputeBreakabilityStep extends IterateVisualProcessStep
{
  public ComputeBreakabilityStep()
  {
  }

  public void compute(LogicalPageBox logicalPageBox)
  {
    startProcessing(logicalPageBox);
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    // Inline elements count as one line, even if they have more than
    // one line of content. If a user wants to use inline-boxes or tables,
    // then he/she has to live with some limitations.
  }

  protected void finishBlockLevelBox(final RenderBox box)
  {
    if (box instanceof ParagraphRenderBox)
    {
      finishParagraph((ParagraphRenderBox) box);
      return;
    }
    else if (box instanceof TableRenderBox)
    {
      finishTable(box);
      return;
    }
    else if (box instanceof TableRowRenderBox)
    {
      finishTableRow(box);
      return;
    }

    // do something ..
    // for now, we ignore most of the stuff, and assume that orphans and
    // widows count for paragraphs and tables, and not for the ordinary stuff.
    final RenderNode firstNode = getFirstUsableNode(box);
    if (firstNode instanceof RenderBox)
    {
      final RenderBox firstBox = (RenderBox) firstNode;
      box.setOrphansSize(firstBox.getOrphansSize());
    }
    else if (firstNode != null)
    {
      box.setOrphansSize(firstNode.getHeight());
    }

    final RenderNode lastNode = getLastUsableNode(box);
    if (lastNode instanceof RenderBox)
    {
      final RenderBox lastBox = (RenderBox) lastNode;
      box.setWidowsSize(lastBox.getOrphansSize());
    }
    else if (lastNode != null)
    {
      box.setWidowsSize(lastNode.getHeight());
    }
  }

  private void finishTableRow(final RenderBox box)
  {
    // A table row is different. It behaves as if it is a linebox.
    long orphanSize = 0;
    long widowSize = 0;
    int linecount = 0;

    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      if (node.isIgnorableForRendering())
      {
        node = node.getNext();
        continue;
      }

      if (node instanceof RenderBox == false)
      {
        node = node.getNext();
        continue;
      }

      RenderBox cellBox = (RenderBox) node;
      orphanSize = Math.max (cellBox.getOrphansSize(), orphanSize);
      widowSize = Math.max (cellBox.getWidowsSize(), widowSize);
      linecount = Math.max (cellBox.getLineCount(), linecount);
      node = node.getNext();
    }

    box.setOrphansSize(orphanSize);
    box.setWidowsSize(widowSize);
    box.setLineCount(linecount);
  }

  private RenderNode getFirstUsableNode (RenderBox box)
  {
    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      // Each node is a line.
      if (node.isIgnorableForRendering() == false)
      {
        return node;
      }
      node = node.getNext();
    }
    return null;
  }

  private RenderNode getLastUsableNode (RenderBox box)
  {
    RenderNode node = box.getLastChild();
    while (node != null)
    {
      // Each node is a line.
      if (node.isIgnorableForRendering() == false)
      {
        return node;
      }
      node = node.getPrev();
    }
    return null;
  }

  private void finishTable(final RenderBox box)
  {
    // Tables are simple right now. Just grab whatever you get ..
    // ignore non-renderable stuff ..

    // Todo: Use the row's orphan stuff ..

    int lineCount = 0;
    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      // Each node is a line.
      if (node.isIgnorableForRendering())
      {
        node = node.getNext();
        continue;
      }

      lineCount += 1;
      if (lineCount == box.getOrphans())
      {
        break;
      }
      node = node.getNext();
    }

    if (node == null)
    {
      box.setOrphansSize(box.getHeight());
    }
    else
    {
      final long nodeY2 = (node.getY() + node.getHeight());
      box.setOrphansSize(nodeY2 - box.getY());
    }

    lineCount = 0;
    node = box.getLastChild();
    while (node != null)
    {
      if (node.isIgnorableForRendering())
      {
        node = node.getNext();
        continue;
      }

      // Each node is a line.
      lineCount += 1;
      if (lineCount == box.getWidows())
      {
        break;
      }
      node = node.getPrev();
    }

    if (node == null)
    {
      box.setWidowsSize(box.getHeight());
    }
    else
    {
      final long nodeY2 = (node.getY() + node.getHeight());
      final long paragraphY2 = box.getY() + box.getHeight();
      box.setWidowsSize(paragraphY2 - nodeY2);
    }
  }

  private void finishParagraph(final ParagraphRenderBox box)
  {
    int lineCount = 0;
    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      // Each node is a line.
      if (node.isIgnorableForRendering())
      {
        node = node.getNext();
        continue;
      }

      lineCount += 1;
      if (lineCount == box.getOrphans())
      {
        break;
      }
      node = node.getNext();
    }

    if (node == null)
    {
      box.setOrphansSize(box.getHeight());
    }
    else
    {
      final long nodeY2 = (node.getY() + node.getHeight());
      box.setOrphansSize(nodeY2 - box.getY());
    }

    lineCount = 0;
    node = box.getLastChild();
    while (node != null)
    {
      if (node.isIgnorableForRendering())
      {
        node = node.getNext();
        continue;
      }

      // Each node is a line.
      lineCount += 1;
      if (lineCount == box.getWidows())
      {
        break;
      }
      node = node.getPrev();
    }

    if (node == null)
    {
      box.setWidowsSize(box.getHeight());
    }
    else
    {
      final long nodeY2 = (node.getY() + node.getHeight());
      final long paragraphY2 = box.getY() + box.getHeight();
      box.setWidowsSize(paragraphY2 - nodeY2);
    }
  }

}
