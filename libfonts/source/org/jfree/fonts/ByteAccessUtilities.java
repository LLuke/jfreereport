/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/libfonts/
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
 * ByteAccessUtilities.java
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
package org.jfree.fonts;

import java.io.UnsupportedEncodingException;

/**
 * Creation-Date: 06.11.2005, 18:46:43
 *
 * @author Thomas Morgner
 */
public class ByteAccessUtilities
{
  private ByteAccessUtilities()
  {
  }

  public static int readUShort (final byte[] data, final int pos)
  {
    return ((data[pos] & 0xff) << 8) | (data[pos + 1] & 0xff);
  }

  public static long readULong (final byte[] data, final int pos)
  {
    final int c1 = (data[pos] & 0xff);
    final int c2 = (data[pos + 1] & 0xff);
    final int c3 = (data[pos + 2] & 0xff);
    final int c4 = (data[pos + 3] & 0xff);

    long retval = ((long) c1 << 24);
    retval |= (long)c2 << 16;
    retval |= (long)c3 << 8;
    retval |= (long)c4;
    return retval;
  }

  public static long readLongDateTime (final byte[] data, final int pos)
  {
    final int c1 = (data[pos] & 0xff);
    final int c2 = (data[pos + 1] & 0xff);
    final int c3 = (data[pos + 2] & 0xff);
    final int c4 = (data[pos + 3] & 0xff);
    final int c5 = (data[pos + 4] & 0xff);
    final int c6 = (data[pos + 5] & 0xff);
    final int c7 = (data[pos + 6] & 0xff);
    final int c8 = (data[pos + 7] & 0xff);

    long retval = ((long) c1 << 56);
    retval |= (long)c2 << 48;
    retval |= (long)c3 << 40;
    retval |= (long)c4 << 32;
    retval |= (long)c5 << 24;
    retval |= (long)c6 << 16;
    retval |= (long)c7 << 8;
    retval |= (long)c8;
    return retval;
  }

  public static byte[] readBytes (final byte[] data,
                                 final int pos, final int length)
  {
    final byte[] retval = new byte[length];
    System.arraycopy(data, pos, retval, 0, length);
    return retval;
  }

  public static short readShort (final byte[] data, final int pos)
  {
    return (short) ((data[pos] & 0xff) << 8 | (data[pos + 1] & 0xff));
  }

  public static int readLong (final byte[] data, final int pos)
  {
    int retval = 0;
    retval |= (long)(data[pos] & 0xff) << 24;
    retval |= (long)(data[pos + 1] & 0xff) << 16;
    retval |= (long)(data[pos + 2] & 0xff) << 8;
    retval |= (long)(data[pos + 3] & 0xff);
    return retval;
  }

  public static String readUnicode (final byte[] data, final int pos,
                                    final int length)
  {
    final StringBuffer buffer = new StringBuffer(length / 2);
    // align the max bounds ...
    final int maxPos = pos + (length / 2) * 2;
    for (int i = pos; i < maxPos; i += 2)
    {
      final int cdata = readUShort(data, i);
      final char c = (char) cdata;
      buffer.append(c);
    }
    return buffer.toString();
  }

  public static String readString (final byte[] data, final int pos,
                                   final int length, final String encoding)
          throws UnsupportedEncodingException
  {
    if ("UTF-16".equals(encoding))
    {
      return readUnicode(data, pos, length);
    }

    return new String (data, pos, length, encoding);
  }

  public static void main(String[] args)
  {
    byte[] test = new byte[] {
            (byte) 0xf9, (byte) 0x06, (byte) 0x80, (byte) 0x11
    };
    if (readULong(test, 0) < 0)
    {
      throw new NullPointerException();
    }
  }
}
