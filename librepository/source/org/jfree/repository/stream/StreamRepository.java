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
 * StreamRepository.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.repository.stream;

import java.io.InputStream;
import java.io.OutputStream;

import org.jfree.repository.ContentLocation;
import org.jfree.repository.DefaultMimeRegistry;
import org.jfree.repository.MimeRegistry;
import org.jfree.repository.Repository;

/**
 * A repository that feeds a single source.
 *
 * @author Thomas Morgner
 */
public class StreamRepository implements Repository
{
  private MimeRegistry mimeRegistry;
  private WrappedOutputStream outputStream;
  private WrappedInputStream inputStream;
  private StreamContentLocation rootLocation;

  public StreamRepository(final InputStream inputStream, final OutputStream outputStream)
  {
    this.inputStream = new WrappedInputStream(inputStream);
    this.outputStream = new WrappedOutputStream(outputStream);
    this.mimeRegistry = new DefaultMimeRegistry();

    this.rootLocation = new StreamContentLocation(this);
  }

  public WrappedOutputStream getOutputStream()
  {
    return outputStream;
  }

  public WrappedInputStream getInputStream()
  {
    return inputStream;
  }

  public ContentLocation getRoot()
  {
    return rootLocation;
  }

  public MimeRegistry getMimeRegistry()
  {
    return mimeRegistry;
  }
}
