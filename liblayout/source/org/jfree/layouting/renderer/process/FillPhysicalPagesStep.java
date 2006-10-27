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
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.process;

import org.jfree.layouting.renderer.model.ParagraphPoolBox;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.page.PageGrid;

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
  public FillPhysicalPagesStep()
  {
  }

  public void compute(LogicalPageBox pagebox)
  {
    startProcessing(pagebox);
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      // all childs of the linebox container must be inline boxes. They
      // represent the lines in the paragraph. Any other element here is
      // a error that must be reported
      if (node instanceof ParagraphPoolBox == false)
      {
        throw new IllegalStateException("Encountered " + node.getClass());
      }
      final ParagraphPoolBox inlineRenderBox = (ParagraphPoolBox) node;
      if (startLine(inlineRenderBox))
      {
        processBoxChilds(inlineRenderBox);
      }
      finishLine(inlineRenderBox);

      node = node.getNext();
    }
  }

  private boolean startLine(final ParagraphPoolBox box)
  {
    return false;
  }

  private void finishLine(final ParagraphPoolBox box)
  {

  }

}
