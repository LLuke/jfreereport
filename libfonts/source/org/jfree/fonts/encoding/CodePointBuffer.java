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
 * CodePointBuffer.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: CodePointBuffer.java,v 1.2 2006/06/28 16:50:27 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.fonts.encoding;

import java.io.Serializable;

/**
 * Creation-Date: 20.04.2006, 16:46:50
 *
 * @author Thomas Morgner
 */
public class CodePointBuffer implements Serializable
{
  private int[] data;
  private int offset;
  private int cursor;

  public CodePointBuffer(int[] data)
  {
    if (data == null) throw new NullPointerException();
    this.data = data;
    this.offset = 0;
    this.cursor = 0;
  }

  public CodePointBuffer(int length)
  {
    this.data = new int[length];
    this.offset = 0;
    this.cursor = 0;
  }

  public int[] getBuffer()
  {
    final int length = getLength();
    int[] retval = new int[length];
    System.arraycopy(data, offset, retval, 0, length);
    return retval;
  }

  public int[] getData()
  {
    return data;
  }

  public void setData(final int[] data, final int length, final int offset)
  {
    if (data == null)
    {
      throw new NullPointerException();
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
      int[] newdata = new int[offset + length];
      System.arraycopy(data, 0, newdata, 0, data.length);
      data = newdata;
    }
  }

  public void setCursor(final int cursor)
  {
    if (cursor < offset) throw new IndexOutOfBoundsException();
    if (cursor > data.length) throw new IndexOutOfBoundsException();
    this.cursor = cursor;
  }
}
