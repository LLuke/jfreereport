/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libfonts/
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.fonts.encoding;

/**
 * This is a wrapper around a byte buffer to allows streaming operations. This
 * preserves my sanity, as managing arrays with irregular encodings is hell.
 *
 * @author Thomas Morgner
 */
public class ByteStream
{
  private ByteBuffer buffer;
  private byte[] data;
  private int writeCursor;
  private int lastWritePos;
  private int increment;
  private int readCursor;

  public ByteStream(final ByteBuffer buffer, final int increment)
  {
    if (buffer == null)
    {
      throw new NullPointerException();
    }
    if (increment < 1)
    {
      throw new IllegalArgumentException();
    }
    this.buffer = buffer;
    this.data = buffer.getData();
    this.writeCursor = buffer.getCursor();
    this.lastWritePos = data.length - 1;
    this.increment = increment;
    this.readCursor = buffer.getOffset();
  }

  public void put(byte b)
  {
    if (writeCursor >= lastWritePos)
    {
      this.buffer.ensureSize(writeCursor + increment);
      this.data = buffer.getData();
      this.lastWritePos = data.length - 1;
    }

    data[writeCursor] = b;
    writeCursor += 1;
  }

  public void put(byte[] b)
  {
    if (writeCursor >= lastWritePos)
    {
      this.buffer.ensureSize(writeCursor + Math.max (increment, b.length));
      this.data = buffer.getData();
      this.lastWritePos = data.length - 1;
    }

    System.arraycopy(b, 0, data, writeCursor, b.length);
    writeCursor += b.length;
  }

  public byte get()
  {
    if (readCursor < writeCursor)
    {
      byte retval = data[readCursor];
      readCursor += 1;
      return retval;
    }
    else
    {
      return 0;
    }
  }

  public void close()
  {
    buffer.setCursor(writeCursor);
  }

  public int getReadSize()
  {
    return (writeCursor - readCursor);
  }
}
