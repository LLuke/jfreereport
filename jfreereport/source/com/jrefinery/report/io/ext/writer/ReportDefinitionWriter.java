/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ---------------------------
 * ReportDefinitionWriter.java
 * ---------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportDefinitionWriter.java,v 1.7 2003/02/21 11:31:13 mungady Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package com.jrefinery.report.io.ext.writer;

import com.jrefinery.report.io.ParserEntityResolver;

import java.io.IOException;
import java.io.Writer;

/**
 * A report definition writer.
 * 
 * @author Thomas Morgner.
 */
public class ReportDefinitionWriter extends AbstractXMLDefinitionWriter
{
  /**
   * Creates a new writer.
   * 
   * @param writer  the report writer.
   */
  public ReportDefinitionWriter(ReportWriter writer)
  {
    super(writer);
  }

  /**
   * Writes a report definition to a character stream writer.  After the standard XML
   * header and the opening tag is written, this class delegates work to:
   * 
   * <ul>
   * <li>{@link ParserConfigWriter} to write the parser configuration;</li>
   * <li>{@link ReportConfigWriter} to write the report configuration;</li>
   * <li>{@link StylesWriter} to write the styles;</li>
   * <li>{@link TemplatesWriter} to write the templates;</li>
   * <li>{@link ReportDescriptionWriter} to write the report description;</li>
   * <li>{@link FunctionsWriter} to write the function definitions;</li>
   * </ul>
   * 
   * @param w  the character stream writer.
   * 
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  public void write (Writer w) throws IOException, ReportWriterException
  {
    String reportName = getReport().getName();
    w.write ("<?xml version=\"1.0\" encoding=\"" + getReportWriter().getEncoding() + "\"?>\n");
    w.write ("<!DOCTYPE report-definition PUBLIC \"");
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

    ReportDescriptionWriter reportDescriptionWriter 
        = new ReportDescriptionWriter(getReportWriter());
    reportDescriptionWriter.write(w);

    FunctionsWriter functionsWriter = new FunctionsWriter(getReportWriter());
    functionsWriter.write(w);

    w.write ("</report-definition>");
    w.write (getLineSeparator());
  }

}
