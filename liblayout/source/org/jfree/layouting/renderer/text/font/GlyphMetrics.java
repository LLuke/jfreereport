/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/liblayout/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.text.font;

/**
 * A return value for glyph metrics. It is used as return value by the font
 * size producer.
 *
 * @author Thomas Morgner
 */
public class GlyphMetrics
{
  private int width;
  private int height;
  private int baselinePosition;

  public GlyphMetrics()
  {
  }

  public int getWidth()
  {
    return width;
  }

  public void setWidth(final int width)
  {
    this.width = width;
  }

  public int getHeight()
  {
    return height;
  }

  public void setHeight(final int height)
  {
    this.height = height;
  }

  public int getBaselinePosition()
  {
    return baselinePosition;
  }

  public void setBaselinePosition(final int baselinePosition)
  {
    this.baselinePosition = baselinePosition;
  }
}
