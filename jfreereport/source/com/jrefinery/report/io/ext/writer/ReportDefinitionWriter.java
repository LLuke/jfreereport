/**
 * Date: Jan 13, 2003
 * Time: 12:53:49 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.writer;

import java.io.Writer;
import java.io.IOException;

public class ReportDefinitionWriter extends AbstractXMLDefinitionWriter
{
  public ReportDefinitionWriter(ReportWriter writer)
  {
    super(writer);
  }

  public void write (Writer w) throws IOException, ReportWriterException
  {
    String reportName = getReport().getName();
    writeTag(w, "report-definition", "name", reportName, OPEN);

    ParserConfigWriter parserConfigWriter = new ParserConfigWriter(getReportWriter());
    parserConfigWriter.write(w);

    ReportConfigWriter reportConfigWriter = new ReportConfigWriter(getReportWriter());
    reportConfigWriter.write(w);

    StylesWriter stylesWriter = new StylesWriter(getReportWriter());
    stylesWriter.write(w);

    w.write ("</report-definition>");
    w.write (getLineSeparator());
  }


}
