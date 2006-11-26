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
 * ParagraphBreakState.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ParagraphBreakState.java,v 1.1 2006/10/17 17:31:57 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.process;

import java.util.ArrayList;

import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.process.layoutrules.InlineNodeSequenceElement;
import org.jfree.layouting.renderer.process.layoutrules.InlineSequenceElement;
import org.jfree.layouting.renderer.process.layoutrules.TextSequenceElement;

/**
 * Used in the Infinite*AxisLayoutSteps.
 *
 * @author Thomas Morgner
 */
public class ParagraphBreakState
{
  private Object suspendItem;
  private ArrayList primarySequence;
  private ParagraphRenderBox paragraph;
  private boolean containsContent;

  public ParagraphBreakState(final ParagraphRenderBox paragraph)
  {
    if (paragraph == null)
    {
      throw new NullPointerException();
    }
    this.paragraph = paragraph;
    this.primarySequence = new ArrayList();
  }

  public ParagraphRenderBox getParagraph()
  {
    return paragraph;
  }

  public Object getSuspendItem()
  {
    return suspendItem;
  }

  public void setSuspendItem(final Object suspendItem)
  {
    this.suspendItem = suspendItem;
  }

  public void add(InlineSequenceElement element)
  {
    primarySequence.add(element);
    if (element instanceof TextSequenceElement ||
        element instanceof InlineNodeSequenceElement)
    {
      containsContent = true;
    }
  }

  public boolean isContainsContent()
  {
    return containsContent;
  }

  public boolean isSuspended()
  {
    return suspendItem != null;
  }

  public InlineSequenceElement[] getSequence()
  {
    return (InlineSequenceElement[]) primarySequence.toArray
            (new InlineSequenceElement[primarySequence.size()]);
  }

  public void clear()
  {
    primarySequence.clear();
    containsContent = false;
  }
}
