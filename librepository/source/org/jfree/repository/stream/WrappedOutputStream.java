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
 * WrappedOutputStream.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.repository.stream;

import java.io.OutputStream;
import java.io.IOException;

/**
 * Creation-Date: 13.11.2006, 17:30:06
 *
 * @author Thomas Morgner
 */
public class WrappedOutputStream extends OutputStream
{
  private OutputStream stream;
  private boolean closed;

  public WrappedOutputStream(final OutputStream stream)
  {
    this.stream = stream;
  }

  public void write(final int b)
  throws IOException
  {
    stream.write(b);
  }

  public void write(final byte[] b)
  throws IOException
  {
    stream.write(b);
  }

  public void write(final byte[] b, final int off, final int len)
  throws IOException
  {
    stream.write(b, off, len);
  }

  public void flush()
  throws IOException
  {
    stream.flush();
  }

  public void close()
  throws IOException
  {
    closed = true;
    stream.close();
  }

  public boolean isClosed()
  {
    return closed;
  }
}
