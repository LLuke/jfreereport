/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * -------------------------
 * StreamHtmlFilesystem.java
 * -------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StreamHtmlFilesystem.java,v 1.3 2003/08/25 14:29:32 taqua Exp $
 *
 * Changes
 * -------
 * 26-Jan-2003 : Initial version
 */
package org.jfree.report.modules.output.table.html;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import org.jfree.io.IOUtils;
import org.jfree.report.ImageReference;

/**
 * The StreamHtmlFilesystem is an Implementation for streamed HTML output.
 * <p>
 * The generated content is a single Html-Stream, without any external generated
 * data. The generated HTML Stream has an inline style sheet definition and supports
 * external images. The external images must be loaded from HTTP, HTTPS or FTP sources,
 * generated images or images loaded from the local filesystem are not supported.
 *
 * @author Thomas Morgner
 */
public class StreamHtmlFilesystem implements HtmlFilesystem
{
  /** the output stream. */
  private final OutputStream root;

  private final boolean allowFileSources;
  private final URL baseURL;
  /**
   * Creates a new StreamHtmlFilesystem for the given output stream.
   *
   * @param root the output stream for the main file.
   */
  public StreamHtmlFilesystem(final OutputStream root)
  {
    this (root, false, null);
  }

  /**
   * Creates a new StreamHtmlFilesystem for the given output stream,
   * which allows URLs to be located on the local filesystem.
   *
   * @param root the output stream for the main file.
   */
  public StreamHtmlFilesystem(final OutputStream root, boolean allowFiles,
                              final URL baseURL)
  {
    if (root == null)
    {
      throw new NullPointerException("root stream is null");
    }
    this.root = root;
    this.allowFileSources = allowFiles;
    this.baseURL = baseURL;
  }


  /**
   * The root stream is used to write the main HTML-File. Any external content is
   * referenced from this file.
   *
   * @return the output stream of the main HTML file.
   * @throws IOException if an IO error occured, while providing the root stream.
   */
  public OutputStream getRootStream() throws IOException
  {
    return root;
  }

  /**
   * Creates a HtmlReference for ImageData. If the target filesystem does not support
   * this reference type, return an empty content reference, but never null.
   * <p>
   * This implementation returns the external reference for all image references which
   * are loaded from an HTTP, HTTPS or FTP source.
   *
   * @param reference the image reference containing the data.
   *
   * @return the generated HtmlReference, never null.
   *
   * @throws IOException if there is an I/O problem.
   */
  public HtmlReferenceData createImageReference(final ImageReference reference)
      throws IOException
  {
    final URL src = reference.getSourceURL();
    if (src == null)
    {
      return new EmptyContentHtmlReferenceData();
    }
    else
    {
      if (src.getProtocol().equals("http") || src.getProtocol().equals("https")
          || src.getProtocol().equals("ftp") ||
         (isAllowFileSources() && src.getProtocol().equals("file")))
      {
        if (baseURL != null)
        {
          return new ImageReferenceData
              (IOUtils.getInstance().createRelativeURL(src, baseURL));
        }
        else
        {
          return new ImageReferenceData(src.toExternalForm());
        }
      }
    }
    return new EmptyContentHtmlReferenceData();
  }

  /**
   * Returns an inline stylesheet reference.
   *
   * @param styleSheet the stylesheet data, which should be referenced.
   * @return an InternalCSSReferenceData for the given stylesheet.
   *
   * @throws IOException if there is an I/O problem.
   */
  public HtmlReferenceData createCSSReference(final String styleSheet)
      throws IOException
  {
    return new InternalCSSReferenceData(styleSheet);
  }

  /**
   * Close the Filesystem and write any buffered content. The used stream is
   * flushed, but not closed.
   *
   * @throws IOException if the close operation failed.
   */
  public void close() throws IOException
  {
    // nothing to do, closing the stream is up to the caller ...
    root.flush();
  }

  public boolean isAllowFileSources()
  {
    return allowFileSources;
  }
}
