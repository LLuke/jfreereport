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
 * $Id: LinebreakClassificationProducer.java,v 1.2 2007/04/27 11:34:23 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.fonts.text.classifier;

/**
 * Creation-Date: 26.06.2006, 16:36:50
 *
 * @author Thomas Morgner
 */
public class LinebreakClassificationProducer implements GlyphClassificationProducer
{

  public LinebreakClassificationProducer()
  {
  }

  public int getClassification(final int codepoint)
  {
    if (isLinebreak(codepoint))
    {
      return GlyphClassificationProducer.SPACE_CHAR;
    }
    return GlyphClassificationProducer.LETTER;
  }

  protected boolean isLinebreak (final int codepoint)
  {
    if (codepoint == 0xa || codepoint == 0xd)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

  public void reset()
  {

  }
}
