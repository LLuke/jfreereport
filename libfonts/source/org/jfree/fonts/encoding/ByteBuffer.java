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
 * ByteBuffer.java
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
package org.jfree.fonts.encoding;

import java.io.Serializable;

/**
 * A simple byte buffer. The length specifies the fill level of the data-array.
 *
 * @author Thomas Morgner
 */
public class ByteBuffer implements Serializable
{
  private byte[] data;
  private int offset;
  private int cursor;

  public ByteBuffer(int length)
  {
    if (length < 0)
    {
      throw new IllegalArgumentException();
    }
    this.data = new byte[length];
    this.offset = 0;
    this.cursor = 0;
  }

  public ByteBuffer(byte[] data)
  {
    if (data == null)
    {
      throw new NullPointerException();
    }
    this.data = data;
    this.offset = 0;
    this.cursor = data.length;
  }

  public ByteBuffer(byte[] data, int offset, int length)
  {
    if (length < 0)
    {
      throw new IndexOutOfBoundsException();
    }
    if (offset < 0)
    {
      throw new IndexOutOfBoundsException();
    }
    if (data == null)
    {
      throw new NullPointerException();
    }
    if ((length + offset) > data.length)
    {
      throw new IndexOutOfBoundsException();
    }
    this.data = data;
    this.offset = offset;
    this.cursor = offset + length;
  }

  public byte[] getData()
  {
    return data;
  }

  public void setData(final byte[] data, final int length, final int offset)
  {
    if (data == null)
    {
      throw new IllegalArgumentException();
    }
    if (length < 0)
    {
      throw new IndexOutOfBoundsException("Length < 0");
    }
    if (offset < 0)
    {
      throw new IllegalArgumentException("Offset < 0");
    }
    if (length + offset >= data.length)
    {
      throw new IllegalArgumentException("Length + Offset");
    }
    this.data = data;
    this.cursor = length + offset;
    this.offset = offset;
  }

  public int getLength()
  {
    return cursor - offset;
  }

  public int getOffset()
  {
    return offset;
  }

  public int getCursor()
  {
    return cursor;
  }

  public void ensureSize(final int length)
  {
    if (data.length < (offset + length))
    {
      byte[] newdata = new byte[offset + length];
      System.arraycopy(data, 0, newdata, 0, offset);
      data = newdata;
    }
  }

  public void setCursor(final int cursor)
  {
    if (cursor < offset)
    {
      throw new IndexOutOfBoundsException();
    }
    if (cursor > data.length)
    {
      throw new IndexOutOfBoundsException();
    }
    this.cursor = cursor;
  }
}
