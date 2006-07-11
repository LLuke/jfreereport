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
 * CodePointStream.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: CodePointStream.java,v 1.2 2006/06/28 16:50:27 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.fonts.encoding;

/**
 * This is a wrapper around a byte buffer to allows streaming operations. This
 * preserves my sanity, as managing arrays with irregular encodings is hell.
 *
 * @author Thomas Morgner
 */
public class CodePointStream
{
  private CodePointBuffer buffer;
  private int[] data;
  private int cursor;
  private int lastWritePos;
  private int increment;

  public CodePointStream(final CodePointBuffer buffer, final int increment)
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
    this.cursor = buffer.getCursor();
    this.lastWritePos = data.length - 1;
    this.increment = increment;
  }

  public void put(int b)
  {
    if (cursor >= lastWritePos)
    {
      this.buffer.ensureSize(cursor + increment);
      this.data = buffer.getData();
      this.lastWritePos = data.length - 1;
    }

    data[cursor] = b;
    cursor += 1;
  }

  public void put(int[] b)
  {
    if (cursor >= lastWritePos)
    {
      this.buffer.ensureSize(cursor + Math.max (increment, b.length));
      this.data = buffer.getData();
      this.lastWritePos = data.length - 1;
    }

    System.arraycopy(b, 0, data, cursor, b.length);
    cursor += b.length;
  }

  public void close()
  {
    buffer.setCursor(cursor);
  }
}
