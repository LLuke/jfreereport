/**
 * Date: Jan 13, 2003
 * Time: 12:53:49 PM
 *
 * $Id: ReportDefinitionWriter.java,v 1.2 2003/01/22 19:38:28 taqua Exp $
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
    w.write ("<?xml version=\"1.0\" encoding=\"" + getReportWriter().getEncoding() + "\"?>");
    w.write ("<!--");
    w.write ("<!DOCTYPE report SYSTEM \"http://jfreereport.sourceforge.net/extreport.dtd\">");
    w.write ("-->");
    w.write ("<!--");
    w.write (" This report definition was created by the ReportDefinitionWriter.");
    w.write ("-->");
    writeTag(w, "report-definition", "name", reportName, OPEN);

    ParserConfigWriter parserConfigWriter = new ParserConfigWriter(getReportWriter());
    parserConfigWriter.write(w);

    ReportConfigWriter reportConfigWriter = new ReportConfigWriter(getReportWriter());
    reportConfigWriter.write(w);

    StylesWriter stylesWriter = new StylesWriter(getReportWriter());
    stylesWriter.write(w);

    TemplatesWriter templatesWriter = new TemplatesWriter(getReportWriter());
    templatesWriter.write(w);

    ReportDescriptionWriter reportDescriptionWriter = new ReportDescriptionWriter(getReportWriter());
    reportDescriptionWriter.write(w);

    FunctionsWriter functionsWriter = new FunctionsWriter(getReportWriter());
    functionsWriter.write(w);

    w.write ("</report-definition>");
    w.write (getLineSeparator());
  }


}
