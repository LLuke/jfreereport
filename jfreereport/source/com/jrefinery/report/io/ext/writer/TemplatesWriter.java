/**
 * Date: Jan 13, 2003
 * Time: 6:47:38 PM
 *
 * $Id: TemplatesWriter.java,v 1.1 2003/01/13 21:39:27 taqua Exp $
 */
package com.jrefinery.report.io.ext.writer;

import com.jrefinery.report.io.ext.ExtReportHandler;

import java.io.IOException;
import java.io.Writer;

public class TemplatesWriter extends AbstractXMLDefinitionWriter
{
  public TemplatesWriter(ReportWriter reportWriter)
  {
    super(reportWriter);
  }

  public void write(Writer writer) throws IOException, ReportWriterException
  {
    writer.write("<!-- templates are not supported by the writer. -->\n");
    writeTag(writer, ExtReportHandler.TEMPLATES_TAG);
    writeCloseTag(writer, ExtReportHandler.TEMPLATES_TAG);
  }
}
