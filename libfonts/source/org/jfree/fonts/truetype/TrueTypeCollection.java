/**
 * ========================================
 * libLayout : a free Java font reading library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * TrueTypeCollection.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 2005-11-07 : Initial version
 */
package org.jfree.fonts.truetype;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.jfree.fonts.ByteAccessUtilities;

/**
 * Reads a TrueTypeCollection file and instantiates the fonts contained in that
 * file.
 *
 * @author Thomas Morgner
 */
public class TrueTypeCollection
{
  public static final long MAGIC_NUMBER =
          ('t' << 24 | 't' << 16 | 'c' << 8 | 'f');

  private File filename;
  private long numFonts;
  private long[] offsets;
  private TrueTypeFont[] fonts;

  public TrueTypeCollection(final File filename) throws IOException
  {
    this.filename = filename;

    final RandomAccessFile raf = new RandomAccessFile(filename, "r");
    final byte[] headerBuffer = new byte[12];
    raf.readFully(headerBuffer);
    if (ByteAccessUtilities.readULong(headerBuffer, 0) != MAGIC_NUMBER)
    {
      throw new IOException();
    }
    numFonts = ByteAccessUtilities.readLong(headerBuffer, 8);

    final byte[] offsetBuffer = new byte[(int) (4 * numFonts)];
    raf.readFully(offsetBuffer);

    final int size = (int) numFonts;
    offsets = new long[size];
    fonts = new TrueTypeFont[size];
    for (int i = 0; i < size; i++)
    {
      offsets[i] = ByteAccessUtilities.readULong(offsetBuffer, i * 4);
    }
  }

  public File getFilename()
  {
    return filename;
  }

  public long getNumFonts()
  {
    return numFonts;
  }

  public synchronized TrueTypeFont getFont(int index) throws IOException
  {
    final TrueTypeFont cachedFont = fonts[index];
    if (cachedFont != null)
    {
      return cachedFont;
    }
    final TrueTypeFont font = new TrueTypeFont(filename, offsets[index], index);
    fonts[index] = font;
    return font;
  }
}
