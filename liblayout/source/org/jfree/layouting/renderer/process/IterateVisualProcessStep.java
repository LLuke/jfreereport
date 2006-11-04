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
 * RenderingProcessStep.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: IterateVisualProcessStep.java,v 1.2 2006/10/27 18:25:50 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.process;

import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.InlineRenderBox;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.table.TableRenderBox;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.util.Log;

/**
 * Iterates over the tree of nodes and classifies nodes by their Display-Model.
 * The Display-Model of nodes is either 'Block' or 'Inline'. All steps dealing
 * with element placement commonly use this strategy.
 *
 *
 * @author Thomas Morgner
 */
public abstract class IterateVisualProcessStep
{
  protected IterateVisualProcessStep()
  {
  }

  protected final void startProcessing (RenderNode node)
  {
    RenderBox parent = node.getParent();
    if (parent instanceof BlockRenderBox || parent == null)
    {
      processBlockLevelChild(node);
    }
    else if (parent instanceof InlineRenderBox)
    {
      processInlineLevelChild(node);
    }
    else
    {
      processOtherLevelChild(node);
    }
  }

  protected void processOtherLevelChild (final RenderNode node)
  {
    // we do not even handle that one. Other level elements are
    // always non-visual!
  }

  protected void processInlineLevelNode (final RenderNode node)
  {
  }

  protected boolean startInlineLevelBox (final RenderBox box)
  {
    return true;
  }

  protected void finishInlineLevelBox (final RenderBox box)
  {
  }

  protected void processInlineLevelChild (final RenderNode node)
  {
    if (node instanceof ParagraphRenderBox)
    {
      ParagraphRenderBox box = (ParagraphRenderBox) node;
      if (startInlineLevelBox(box))
      {
        processParagraphChilds(box);
      }
      finishInlineLevelBox(box);
    }
    else if (node instanceof RenderBox)
    {
      RenderBox box = (RenderBox) node;
      if (startInlineLevelBox(box))
      {
        processBoxChilds(box);
      }
      finishInlineLevelBox(box);
    }
    else
    {
      processInlineLevelNode(node);
    }
  }

  protected void processBlockLevelNode (final RenderNode node)
  {
  }

  protected boolean startBlockLevelBox (final RenderBox box)
  {
    return true;
  }

  protected void finishBlockLevelBox (final RenderBox box)
  {
  }

  protected void processBlockLevelChild (final RenderNode node)
  {
    if (node instanceof LogicalPageBox)
    {
      LogicalPageBox box = (LogicalPageBox) node;
      if (startBlockLevelBox(box))
      {
        startProcessing(box.getHeaderArea());
        processBoxChilds(box);
        startProcessing(box.getFooterArea());
      }
      finishBlockLevelBox(box);
    }
    else if (node instanceof ParagraphRenderBox)
    {
      ParagraphRenderBox box = (ParagraphRenderBox) node;
      if (startBlockLevelBox(box))
      {
        processParagraphChilds(box);
      }
      finishBlockLevelBox(box);
    }
    else if (node instanceof RenderBox)
    {
      RenderBox box = (RenderBox) node;
      if (startBlockLevelBox(box))
      {
        processBoxChilds(box);
      }
      finishBlockLevelBox(box);
    }
    else
    {
      processBlockLevelNode(node);
    }
  }

  protected abstract void processParagraphChilds(final ParagraphRenderBox box);

  protected void processBoxChilds(RenderBox box)
  {
    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      startProcessing(node);
      node = node.getNext();
    }
  }
}