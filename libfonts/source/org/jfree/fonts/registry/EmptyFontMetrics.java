/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/libfonts/
 * Project Lead:  Thomas Morgner;
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
 * EmptyFontMetrics.java
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
package org.jfree.fonts.registry;

/**
 * An placeholder metrics for buggy fonts.
 *
 * @author Thomas Morgner
 */
public class EmptyFontMetrics implements FontMetrics
{
  public EmptyFontMetrics()
  {
  }

  /**
   * From the baseline to the
   *
   * @return
   */
  public double getAscent()
  {
    return 0;
  }

  public double getDescent()
  {
    return 0;
  }

  public double getLeading()
  {
    return 0;
  }

  /**
   * The height of the lowercase 'x'. This is used as hint, which size the
   * lowercase characters will have.
   *
   * @return
   */
  public double getXHeight()
  {
    return 0;
  }

  public double getOverlinePosition()
  {
    return 0;
  }

  public double getUnderlinePosition()
  {
    return 0;
  }

  public double getStrikeThroughPosition()
  {
    return 0;
  }

  public double getMaxAscent()
  {
    return 0;
  }

  public double getMaxDescent()
  {
    return 0;
  }

  public double getMaxLeading()
  {
    return 0;
  }

  public double getMaxHeight()
  {
    return 0;
  }

  public double getMaxCharAdvance()
  {
    return 0;
  }

  public double getCharWidth(int codePoint)
  {
    return 0;
  }

  public double getKerning(int previous, int codePoint)
  {
    return 0;
  }
}
