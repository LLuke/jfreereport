/**
 * Date: Jan 13, 2003
 * Time: 7:06:26 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.writer;

import java.io.Writer;
import java.io.IOException;

public class ReportDescriptionWriter extends AbstractXMLDefinitionWriter
{
  public ReportDescriptionWriter(ReportWriter reportWriter)
  {
    super(reportWriter);
  }

  public void write(Writer writer) throws IOException, ReportWriterException
  {
  }
}
