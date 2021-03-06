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
 * BitmapCompressionFactory.java
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

public class BitmapCompressionFactory
{
  public BitmapCompressionFactory()
  {
  }

  public static BitmapCompression getHandler (final int comp)
  {
    switch (comp)
    {
      case BitmapHeader.BI_RGB:
        return new RGBCompression();
      case BitmapHeader.BI_RLE4:
        return new RLE4Compression();
      case BitmapHeader.BI_RLE8:
        return new RLE8Compression();
      case BitmapHeader.BI_BITFIELDS:
        return new BitFieldsCompression();
      default:
        throw new IllegalArgumentException("Unknown compression: " + comp);
    }
  }
}
