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
 * $Id: WordBreakProducer.java,v 1.1 2007/04/03 14:13:56 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.fonts.text.breaks;

/**
 * This is the standard behaviour for HTML.It breaks texts at word boundaries.
 *
 * @author Thomas Morgner
 */
public class WordBreakProducer extends LineBreakProducer
{
  public WordBreakProducer()
  {
  }

  public int createBreakOpportunity(final int codepoint)
  {
    final int breakOpportunity = super.createBreakOpportunity(codepoint);
    if (breakOpportunity != BreakOpportunityProducer.BREAK_NEVER)
    {
      return breakOpportunity;
    }

    // cheating here for now. Needs an implementation.
    if (Character.isWhitespace((char) codepoint))
    {
      return BreakOpportunityProducer.BREAK_WORD;
    }

    return BreakOpportunityProducer.BREAK_CHAR;
  }
}
