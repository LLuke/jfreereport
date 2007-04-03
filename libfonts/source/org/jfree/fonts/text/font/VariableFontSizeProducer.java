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

package org.jfree.fonts.text.font;

import org.jfree.fonts.registry.FontMetrics;
import org.jfree.fonts.text.ClassificationProducer;
import org.jfree.fonts.tools.StrictGeomUtility;

/**
 * Looks-up the character on the given font.
 *
 * @author Thomas Morgner
 */
public class VariableFontSizeProducer implements FontSizeProducer
{
  private FontMetrics fontMetrics;
  private int maxHeight;
  private int baseLine;


  public VariableFontSizeProducer(final FontMetrics fontMetrics)
  {
    if (fontMetrics == null)
    {
      throw new NullPointerException();
    }
    this.fontMetrics = fontMetrics;
    this.maxHeight = (int) (0x7FFFFFFF &
            StrictGeomUtility.toInternalValue(fontMetrics.getMaxHeight()));
    this.baseLine = (int) (0x7FFFFFFF & StrictGeomUtility.toInternalValue
            (fontMetrics.getMaxHeight() - fontMetrics.getMaxDescent()));
  }

  public GlyphMetrics getCharacterSize(int codePoint,
                                       GlyphMetrics dimension)
  {
    final int width;
    if (codePoint == ClassificationProducer.START_OF_TEXT ||
            codePoint == ClassificationProducer.END_OF_TEXT)
    {
      width = 0;
    }
    else
    {
      width = (int) (0x7FFFFFFF & StrictGeomUtility.toInternalValue
              (fontMetrics.getCharWidth(codePoint)));
    }

    if (dimension == null)
    {
      dimension = new GlyphMetrics();
    }

    dimension.setWidth(width);
    dimension.setHeight(maxHeight);
    dimension.setBaselinePosition(baseLine);
    return dimension;
  }

  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }
}
