/**
 * Date: Jan 13, 2003
 * Time: 12:53:49 PM
 *
 * $Id: ReportDefinitionWriter.java,v 1.4 2003/01/23 18:36:01 taqua Exp $
 */
package com.jrefinery.report.io.ext.writer;

import com.jrefinery.report.io.ParserEntityResolver;

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
    w.write ("<?xml version=\"1.0\" encoding=\"" + getReportWriter().getEncoding() + "\"?>\n");
    w.write ("<!DOCTYPE report PUBLIC \"");
    w.write (ParserEntityResolver.PUBLIC_ID_EXTENDED);
    w.write ("\"\n");
    w.write ("                         \"http://jfreereport.sourceforge.net/extreport.dtd\">\n");
    w.write ("<!--\n");
    w.write (" This report definition was created by the ReportDefinitionWriter.\n");
    w.write ("-->\n");
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
