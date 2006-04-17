/**
 * ========================================
 * Pixie : a free Java vector image library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/pixie/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * GDIPalette.java
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

package org.jfree.pixie.wmf.bitmap;

import java.io.IOException;
import java.io.InputStream;

public class GDIPalette
{
  private int noColors = 0;
  private int[] colors = null;

  public void setNoOfColors (final int colors)
  {
    this.noColors = colors;
  }

  public void setNoOfImportantColors (final int colors)
  {
    if (colors > noColors)
    {
      throw new IllegalArgumentException("There may be not more important colors than colors defined in the palette.");
    }
  }

  public void readPalette (final InputStream in)
          throws IOException
  {
    colors = new int[noColors];
    for (int i = 0; i < noColors; i++)
    {
      colors[i] = readNextColor(in);
    }
  }

  private int readNextColor (final InputStream in)
          throws IOException
  {
    final int b = in.read();
    final int g = in.read();
    final int r = in.read();
    //final int filler =  
    in.read();
    return b + (g << 8) + (r << 16);
  }

  public int lookupColor (final int color)
  {
    if (noColors == 0)
    {
      // Convert from BGR (windows) format to RGB (java) format
      final int b = (color & 0x00ff0000) >> 16;
      final int g = (color & 0x0000ff00);
      final int r = (color & 0x000000ff);
      return b + g + (r << 16);
    }

    return colors[color];
  }
}
