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
 * DIBReader.java
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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import org.jfree.pixie.wmf.MfRecord;

public class DIBReader
{
  private GDIPalette palette; // as GDI Color value
  private BitmapHeader header;

  public DIBReader ()
  {
  }

  public BufferedImage setRecord (final MfRecord record)
          throws IOException
  {
    return setRecord(record, 0);
  }

  public BufferedImage setRecord (final MfRecord record, final int offset)
          throws IOException
  {
    header = new BitmapHeader();
    header.setRecord(record, offset);
    palette = new GDIPalette();
    palette.setNoOfColors(header.getNoOfColors());

    final int width = header.getWidth();
    final int height = header.getHeight();

    final int paletteStart = MfRecord.RECORD_HEADER_SIZE + header.getHeaderSize() + 4 + offset;
    final InputStream dataIn = record.getInputStream(paletteStart);
    palette.readPalette(dataIn);

    final int compression = header.getCompression();
    final BitmapCompression comHandler = BitmapCompressionFactory.getHandler(compression);
    comHandler.setDimension(width, height);
    comHandler.setBpp(header.getBitsPerPixel());
    final int[] data = comHandler.decompress(dataIn, palette);

    final BufferedImage retval = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    retval.setRGB(0, 0, width, height, data, 0, width);
    return retval;
  }
}
