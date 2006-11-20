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
 * RightAlignmentProcessor.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: RightAlignmentProcessor.java,v 1.1 2006/10/17 17:31:57 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.process;

import org.jfree.layouting.renderer.process.layoutrules.InlineSequenceElement;

/**
 * Right alignment strategy. Not working yet, as this is unimplemented right now.
 *
 * @author Thomas Morgner
 */
public class RightAlignmentProcessor extends AbstractAlignmentProcessor
{
  public RightAlignmentProcessor()
  {
  }

  protected int handleLayout(int start,
                             int count,
                             int contentIndex,
                             long usedWidth)
  {
    final InlineSequenceElement[] sequenceElements = getSequenceElements();
    final long[] elementDimensions = getElementDimensions();
    final long[] elementPositions = getElementPositions();

    // if we reached that method, then this means, that the elements may fit
    // into the available space. (Assuming that there is no inner pagebreak;
    // a thing we do not handle yet)

    long position = getEndOfLine();
    for (int i = sequenceElements.length - 1; i >= 0; i--)
    {
      final long elementWidth = sequenceElements[i].getMaximumWidth();
      position -= elementWidth;
      elementDimensions[i] = elementWidth;
      elementPositions[i] = position;
    }
    return start + count;
  }

}
