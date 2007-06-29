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
 * ZipRepository.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.repository.zipwriter;

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.Deflater;

import org.jfree.repository.Repository;
import org.jfree.repository.ContentLocation;
import org.jfree.repository.ContentIOException;
import org.jfree.repository.MimeRegistry;
import org.jfree.repository.DefaultMimeRegistry;
import org.jfree.io.IOUtils;

/**
 * Creation-Date: 01.12.2006, 21:12:39
 *
 * @author Thomas Morgner
 */
public class ZipRepository implements Repository
{
  private ZipOutputStream zipOutputStream;
  private ZipEntryOutputStream currentStream;
  private MimeRegistry mimeRegistry;
  private ZipContentLocation root;

  public ZipRepository(final OutputStream out,
                       final int level,
                       final MimeRegistry mimeRegistry)
  {
    this.mimeRegistry = mimeRegistry;
    this.zipOutputStream = new ZipOutputStream(out);
    this.zipOutputStream.setLevel(level);
    this.root = new ZipContentLocation(this, null, "");
  }

  public ZipRepository(final OutputStream out,
                       final int level)
  {
    this(out,level, new DefaultMimeRegistry());
  }

  public ZipRepository(final OutputStream out)
  {
    this(out, Deflater.DEFAULT_COMPRESSION, new DefaultMimeRegistry());
  }

  public ContentLocation getRoot() throws ContentIOException
  {
    return root;
  }

  public MimeRegistry getMimeRegistry()
  {
    return mimeRegistry;
  }

  public void close() throws IOException
  {
    zipOutputStream.finish();
    zipOutputStream.flush();
  }

  public void writeDirectory (ZipEntry entry) throws IOException
  {
    zipOutputStream.putNextEntry(entry);
  }

  public void writeContent (ZipEntry entry, InputStream in) throws IOException
  {
    zipOutputStream.putNextEntry(entry);
    IOUtils.getInstance().copyStreams(in, zipOutputStream);
    zipOutputStream.closeEntry();
  }
}
