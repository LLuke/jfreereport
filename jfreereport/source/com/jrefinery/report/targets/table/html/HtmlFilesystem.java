/**
 * Date: Jan 26, 2003
 * Time: 5:26:04 PM
 *
 * $Id: HtmlFilesystem.java,v 1.1 2003/01/27 03:20:01 taqua Exp $
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.ImageReference;

import java.io.IOException;
import java.io.OutputStream;

public interface HtmlFilesystem
{
  // contains the HTML file
  public OutputStream getRootStream () throws IOException;

  public HtmlReferenceData createImageReference(ImageReference reference) throws IOException;
  public HtmlReferenceData createCSSReference (String styleSheet) throws IOException;

  public void close() throws IOException;
}
