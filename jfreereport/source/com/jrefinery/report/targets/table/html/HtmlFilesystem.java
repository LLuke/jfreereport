/**
 * Date: Jan 26, 2003
 * Time: 5:26:04 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.ImageReference;

import java.io.OutputStream;
import java.io.IOException;

public interface HtmlFilesystem
{
  // contains the HTML file
  public OutputStream getRootStream () throws IOException;

  public HtmlReferenceData createImageReference(ImageReference reference) throws IOException;
  public HtmlReferenceData createCSSReference (String styleSheet) throws IOException;

  public void close() throws IOException;
}
