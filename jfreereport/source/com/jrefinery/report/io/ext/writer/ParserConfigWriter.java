/**
 * Date: Jan 13, 2003
 * Time: 1:20:24 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.writer;

import com.jrefinery.report.io.ext.ExtReportHandler;
import com.jrefinery.report.io.ext.ParserConfigHandler;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

public class ParserConfigWriter extends AbstractXMLDefinitionWriter
{
  public ParserConfigWriter(ReportWriter reportWriter)
  {
    super(reportWriter);
  }

  public void write(Writer writer) throws IOException
  {
    writeTag(writer, ExtReportHandler.PARSER_CONFIG_TAG);
    writer.write(getLineSeparator());

    writeFactory (writer, ParserConfigHandler.OBJECT_FACTORY_TAG,
                  getReportWriter().getClassFactoryCollector().getFactories());
    writeFactory (writer, ParserConfigHandler.ELEMENT_FACTORY_TAG,
                  getReportWriter().getElementFactoryCollector().getFactories());
    writeFactory (writer, ParserConfigHandler.STYLEKEY_FACTORY_TAG,
                  getReportWriter().getStyleKeyFactoryCollector().getFactories());
    writeFactory (writer, ParserConfigHandler.TEMPLATE_FACTORY_TAG,
                  getReportWriter().getTemplateCollector().getFactories());
    writeFactory (writer, ParserConfigHandler.DATASOURCE_FACTORY_TAG,
                  getReportWriter().getDataSourceCollector().getFactories());
    // datadefinition not yet implemented ...

    writeCloseTag (writer, ExtReportHandler.PARSER_CONFIG_TAG);
    writer.write(getLineSeparator());
  }

  public void writeFactory (Writer w, String tagName, Iterator it)
    throws IOException
  {
    while (it.hasNext())
    {
      Object itObject = it.next();
      String className = itObject.getClass().getName();
      writeTag(w, tagName, ParserConfigHandler.CLASS_ATTRIBUTE, className, CLOSE);
      w.write(getLineSeparator());
    }
  }
}
