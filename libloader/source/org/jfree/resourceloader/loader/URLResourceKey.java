/**
 * ================================================
 * LibLoader : a free Java resource loading library
 * ================================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/libloader/
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
 * URLResourceKey.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: URLResourceKey.java,v 1.1.1.1 2006/04/17 16:48:30 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.resourceloader.loader;

import java.net.URL;
import java.util.Map;

import org.jfree.resourceloader.AbstractResourceKey;

/**
 * Creation-Date: 05.04.2006, 15:19:24
 *
 * @author Thomas Morgner
 */
public class URLResourceKey extends AbstractResourceKey
{
  private transient URL url;

  public URLResourceKey(final Map parameters)
  {
    super(parameters);
    Object maybeUrl = parameters.get(AbstractResourceKey.CONTENT_KEY);
    if (maybeUrl instanceof URL == false)
    {
      throw new NullPointerException("URL is not valid.");
    }
    this.url = (URL) maybeUrl;
  }

  public URL getUrl()
  {
    if (url == null)
    {
      this.url = (URL) getLoaderParameter(AbstractResourceKey.CONTENT_KEY);
    }
    return url;
  }

  /**
   * Returns the schema of this resource key. The schema can be mapped to a
   * known resource loader. If no resource loader is available for the given
   * schema, the resource will be unavailable.
   *
   * @return
   */
  public String getSchema()
  {
    return url.getProtocol();
  }
}
