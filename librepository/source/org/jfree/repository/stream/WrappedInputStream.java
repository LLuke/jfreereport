/**
 * ===========================================================
 * LibRepository : a free Java content repository access layer
 * ===========================================================
 *
 * Project Info:  http://jfreereport.pentaho.org/librepository/
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
 * WrappedInputStream.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.repository.stream;

import java.io.IOException;
import java.io.InputStream;

/**
 * Creation-Date: 13.11.2006, 17:28:24
 *
 * @author Thomas Morgner
 */
public class WrappedInputStream extends InputStream
{
  private boolean closed;
  private InputStream parent;

  public WrappedInputStream(final InputStream parent)
  {
    this.parent = parent;
  }

  public int read()
      throws IOException
  {
    return parent.read();
  }

  public int read(final byte[] b)
      throws IOException
  {
    return parent.read(b);
  }

  public int read(final byte[] b, final int off, final int len)
      throws IOException
  {
    return parent.read(b, off, len);
  }

  public long skip(final long n)
      throws IOException
  {
    return parent.skip(n);
  }

  public int available()
      throws IOException
  {
    return parent.available();
  }

  public void close()
      throws IOException
  {
    closed = true;
    parent.close();
  }

  public boolean isClosed()
  {
    return closed;
  }

  public void mark(final int readlimit)
  {
    parent.mark(readlimit);
  }

  public void reset()
      throws IOException
  {
    parent.reset();
  }

  public boolean markSupported()
  {
    return parent.markSupported();
  }
}
