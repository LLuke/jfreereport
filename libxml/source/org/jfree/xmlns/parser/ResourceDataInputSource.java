/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * ResourceDataInputSource.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ResourceDataInputSource.java,v 1.1 2006/04/18 11:45:16 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.xmlns.parser;

import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceLoadingException;
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
  public ResourceDataInputSource(final ResourceData data)
          throws ResourceLoadingException
  {
    if (data == null) throw new NullPointerException("Data must not be null");
    this.data = data;
    this.version = data.getVersion();
    setByteStream(data.getResourceAsStream());
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
