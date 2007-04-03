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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.fonts.text.whitespace;

import org.jfree.fonts.text.ClassificationProducer;

/**
 * Creation-Date: 11.06.2006, 20:11:17
 *
 * @author Thomas Morgner
 */
public class DiscardWhiteSpaceFilter implements WhiteSpaceFilter
{
  public static final char ZERO_WIDTH_NON_JOINER = '\u200C';

  private boolean lastWasWhiteSpace;

  public DiscardWhiteSpaceFilter()
  {
  }

  /**
   * Reset the filter to the same state as if the filter had been constructed
   * but not used yet.
   */
  public void reset()
  {
    lastWasWhiteSpace = false;
  }

  /**
   * Filters the whitespaces. This method returns '-1', if the whitespace should
   * be removed from the stream; otherwise it presents a replacement character.
   * If the codepoint is no whitespace at all, the codepoint is returned
   * unchanged.
   *
   * @param codepoint
   * @return
   */
  public int filter(int codepoint)
  {
    if (Character.isWhitespace((char) codepoint))
    {
      if (lastWasWhiteSpace == false)
      {
        lastWasWhiteSpace = true;
        return DiscardWhiteSpaceFilter.ZERO_WIDTH_NON_JOINER;
      }
      return WhiteSpaceFilter.STRIP_WHITESPACE;
    }
    if (codepoint == ClassificationProducer.START_OF_TEXT)
    {
      lastWasWhiteSpace = true;
      return WhiteSpaceFilter.STRIP_WHITESPACE;
    }
    else if (codepoint == ClassificationProducer.END_OF_TEXT)
    {
      // do not modify the whitespace flag ..
      return WhiteSpaceFilter.STRIP_WHITESPACE;
    }

    lastWasWhiteSpace = false;
    return codepoint;
  }


  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

}
