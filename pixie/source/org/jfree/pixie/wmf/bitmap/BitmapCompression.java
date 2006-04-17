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
 * BitmapCompression.java
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

public abstract class BitmapCompression
{
  private int height;
  private int width;
  private int bpp;
  private boolean topDown;

  public void setDimension (final int width, final int height)
  {
    this.width = width;
    this.height = height;
  }

  public int getHeight ()
  {
    return height;
  }

  public int getWidth ()
  {
    return width;
  }

  public int getBpp ()
  {
    return bpp;
  }

  public void setBpp (final int bpp)
  {
    this.bpp = bpp;
  }

  public void setTopDown (final boolean b)
  {
    this.topDown = b;
  }

  public boolean isTopDown ()
  {
    return topDown;
  }

  public abstract int[] decompress (InputStream in, GDIPalette palette)
          throws IOException;

  public static int[] expandMonocrome (final int b, final GDIPalette pal)
  {
    final int tColor = pal.lookupColor(1);
    final int fColor = pal.lookupColor(0);

    final int[] retval = new int[8];
    if ((b & 0x01) == 0x01)
    {
      retval[0] = tColor;
    }
    else
    {
      retval[0] = fColor;
    }
    if ((b & 0x02) == 0x02)
    {
      retval[1] = tColor;
    }
    else
    {
      retval[1] = fColor;
    }
    if ((b & 0x04) == 0x04)
    {
      retval[2] = tColor;
    }
    else
    {
      retval[2] = fColor;
    }
    if ((b & 0x08) == 0x08)
    {
      retval[3] = tColor;
    }
    else
    {
      retval[3] = fColor;
    }
    if ((b & 0x10) == 0x10)
    {
      retval[4] = tColor;
    }
    else
    {
      retval[4] = fColor;
    }
    if ((b & 0x20) == 0x20)
    {
      retval[5] = tColor;
    }
    else
    {
      retval[5] = fColor;
    }
    if ((b & 0x40) == 0x40)
    {
      retval[6] = tColor;
    }
    else
    {
      retval[6] = fColor;
    }
    if ((b & 0x80) == 0x80)
    {
      retval[7] = tColor;
    }
    else
    {
      retval[7] = fColor;
    }
    return retval;
  }

  public static int[] expand4BitTuple (final int b, final GDIPalette pal)
  {
    final int[] retval = new int[2];
    retval[0] = pal.lookupColor((b & 0xF0) >> 4);
    retval[1] = pal.lookupColor(b & 0x0F);
    return retval;
  }
}
