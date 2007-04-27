/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libfonts/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * $Id: LineBreakProducer.java,v 1.1 2007/04/03 14:13:56 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.fonts.text.breaks;

import org.jfree.fonts.text.ClassificationProducer;

/**
 * This produces linebreaks when a CR/LF is encountered. This corresponds to
 * the expected behaviour of HTML-pre elements.
 *
 * @author Thomas Morgner
 */
public class LineBreakProducer implements BreakOpportunityProducer
{
  private int lastCodePoint;

  public LineBreakProducer()
  {
  }

  /** Signals the start of text. Resets the state to the initial values. */
  public void startText()
  {
    lastCodePoint = 0;
  }

  public int createBreakOpportunity(final int codepoint)
  {
    if (codepoint == ClassificationProducer.START_OF_TEXT)
    {
      lastCodePoint = 0;
      return BreakOpportunityProducer.BREAK_NEVER;
    }

    // well, that's not fully correct, we should detect single \r as well.
    if (codepoint == '\n')
    {
      lastCodePoint = codepoint;
      return BreakOpportunityProducer.BREAK_LINE;
    }

    lastCodePoint = codepoint;
    return BreakOpportunityProducer.BREAK_NEVER;
  }

  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

  public void reset()
  {
    lastCodePoint = 0;
  }
}
