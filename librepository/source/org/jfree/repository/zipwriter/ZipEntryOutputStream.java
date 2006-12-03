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
 * ZipEntryOutputStream.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.repository.zipwriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;

/**
 * Creation-Date: 01.12.2006, 21:26:23
 *
 * @author Thomas Morgner
 */
public class ZipEntryOutputStream extends OutputStream
{
  private ByteArrayOutputStream outputStream;
  private DeflaterOutputStream deflaterOutputStream;
  private boolean closed;
  private ZipContentItem item;

  public ZipEntryOutputStream(ZipContentItem item)
  {
    this.item = item;
    this.outputStream = new ByteArrayOutputStream();
    this.deflaterOutputStream = new DeflaterOutputStream(outputStream);
  }

  public void write(final int b)
      throws IOException
  {
    if (closed)
    {
      throw new IOException("Already closed");
    }
    deflaterOutputStream.write(b);
  }

  public void write(final byte[] b, final int off, final int len)
      throws IOException
  {
    if (closed)
    {
      throw new IOException("Already closed");
    }
    deflaterOutputStream.write(b, off, len);
  }

  public void close()
      throws IOException
  {
    if (closed)
    {
      throw new IOException("Already closed");
    }

    deflaterOutputStream.close();
    final byte[] data = outputStream.toByteArray();
    final ByteArrayInputStream bin = new ByteArrayInputStream(data);
    final InflaterInputStream infi = new InflaterInputStream(bin);

    final ZipRepository repository = (ZipRepository) item.getRepository();

    final String contentId = (String) item.getContentId();
    repository.writeContent(new ZipEntry(contentId), infi);
    infi.close();

    outputStream = null;
    deflaterOutputStream = null;
  }

  public void write(final byte[] b)
      throws IOException
  {
    if (closed)
    {
      throw new IOException("Already closed");
    }
    deflaterOutputStream.write(b);
  }

  public void flush()
      throws IOException
  {
    if (closed)
    {
      throw new IOException("Already closed");
    }
    deflaterOutputStream.flush();
  }
}
