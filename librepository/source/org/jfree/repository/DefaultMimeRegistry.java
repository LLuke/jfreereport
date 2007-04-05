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
 * DefaultMimeRegistry.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.repository;

import org.jfree.util.StringUtils;

/**
 * Creation-Date: 13.11.2006, 12:24:19
 *
 * @author Thomas Morgner
 */
public class DefaultMimeRegistry implements MimeRegistry
{
  public DefaultMimeRegistry()
  {
  }

  public String getMimeType(ContentItem item)
  {
    final String name = item.getName();
    if (name == null)
    {
      return "application/octet-stream";
    }
    if (StringUtils.endsWithIgnoreCase(name, ".png"))
    {
      return "image/png";
    }
    if (StringUtils.endsWithIgnoreCase(name, ".png"))
    {
      return "image/png";
    }
    if (StringUtils.endsWithIgnoreCase(name, ".jpg"))
    {
      return "image/jpeg";
    }
    if (StringUtils.endsWithIgnoreCase(name, ".jpeg"))
    {
      return "image/jpeg";
    }
    if (StringUtils.endsWithIgnoreCase(name, ".gif"))
    {
      return "image/gif";
    }
    if (StringUtils.endsWithIgnoreCase(name, ".pdf"))
    {
      return "application/pdf";
    }
    if (StringUtils.endsWithIgnoreCase(name, ".txt"))
    {
      return "text/plain";
    }
    if (StringUtils.endsWithIgnoreCase(name, ".html"))
    {
      return "text/html";
    }
    if (StringUtils.endsWithIgnoreCase(name, ".htm"))
    {
      return "text/html";
    }
    return "application/octet-stream";
  }

  public String getSuffix(String mimeType)
  {
    // needs 'libMagic'
    if ("image/png".equals(mimeType))
    {
      return "png";
    }
    if ("image/jpeg".equals(mimeType))
    {
      return "jpg";
    }
    if ("image/jpg".equals(mimeType))
    {
      return "jpg";
    }
    if ("image/gif".equals(mimeType))
    {
      return "gif";
    }
    if ("text/html".equals(mimeType))
    {
      return "html";
    }
    if ("text/plain".equals(mimeType))
    {
      return "txt";
    }
    if ("application/pdf".equals(mimeType))
    {
      return "pdf";
    }
    return "dat";
  }
}
