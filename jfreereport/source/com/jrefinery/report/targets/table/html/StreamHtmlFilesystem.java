/**
 * Date: Jan 26, 2003
 * Time: 6:30:38 PM
 *
 * $Id$
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
