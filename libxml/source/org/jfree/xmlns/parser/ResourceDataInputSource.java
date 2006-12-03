/**
 * =========================================
 * LibXML : a free Java layouting library
 * =========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libxml/
 *
 * (C) Copyright 2006, by Object Refinery Ltd, Pentaho Corporation and Contributors.
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
 *
 * ------------
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.xmlns.parser;

import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;
import org.xml.sax.InputSource;

/**
 * Creation-Date: 08.04.2006, 14:16:06
 *
 * @author Thomas Morgner
 */
public class ResourceDataInputSource extends InputSource
{
  private ResourceData data;
  private long version;

  /**
   * Zero-argument default constructor.
   *
   * @see #setPublicId
   * @see #setSystemId
   * @see #setByteStream
   * @see #setCharacterStream
   * @see #setEncoding
   */
  public ResourceDataInputSource(final ResourceData data,
                                 final ResourceManager caller)
          throws ResourceLoadingException
  {
    if (data == null) throw new NullPointerException("Data must not be null");
    this.data = data;
    this.version = data.getVersion(caller);
    setByteStream(data.getResourceAsStream(caller));
  }

  public ResourceData getData()
  {
    return data;
  }

  public long getVersion()
  {
    return version;
  }
}
