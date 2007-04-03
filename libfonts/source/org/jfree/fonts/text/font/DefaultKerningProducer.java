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
 * Creation-Date: 11.06.2006, 18:30:42
 *
 * @author Thomas Morgner
 */
public class DefaultKerningProducer implements KerningProducer
{
  private int lastCodePoint;
  private FontMetrics fontMetrics;

  public DefaultKerningProducer(final FontMetrics fontMetrics)
  {
    if (fontMetrics == null)
    {
      throw new NullPointerException();
    }
    this.fontMetrics = fontMetrics;
  }

  public long getKerning(final int codePoint)
  {
    if (codePoint == ClassificationProducer.START_OF_TEXT || codePoint == ClassificationProducer.END_OF_TEXT)
    {
      lastCodePoint = 0;
      return 0;
    }

    final double d = fontMetrics.getKerning(lastCodePoint, codePoint);
    lastCodePoint = codePoint;
    return StrictGeomUtility.toInternalValue(d);
  }

  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

}
