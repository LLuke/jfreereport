/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -------------------
 * StreamHtmlFilesystem.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StreamHtmlFilesystem.java,v 1.1 2003/01/27 03:20:01 taqua Exp $
 *
 * Changes
 * -------
 * 26-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.ImageReference;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class StreamHtmlFilesystem implements HtmlFilesystem
{
  private OutputStream root;

  public StreamHtmlFilesystem(OutputStream root)
  {
    this.root = root;
  }

  // contains the HTML file
  public OutputStream getRootStream() throws IOException
  {
    return root;
  }

  public HtmlReferenceData createImageReference(ImageReference reference)
      throws IOException
  {
    if (reference.getSourceURL() == null)
    {
      return new EmptyContentHtmlReferenceData();
    }
    else
    {
      URL src = reference.getSourceURL();
      if (src.getProtocol().equals("http") ||
          src.getProtocol().equals("https") ||
          src.getProtocol().equals("ftp"))
      {
        return new ImageReferenceData(src.toExternalForm());
      }
    }
    return new EmptyContentHtmlReferenceData();
  }

  public HtmlReferenceData createCSSReference(String styleSheet)
      throws IOException
  {
    return new InternalCSSReferenceData(styleSheet);
  }

  public void close() throws IOException
  {
    // nothing to do, closing the stream is up to the caller ...
  }
}
