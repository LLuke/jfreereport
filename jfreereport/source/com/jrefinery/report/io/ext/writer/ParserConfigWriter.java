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
 * -----------------------
 * ParserConfigWriter.java
 * -----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ParserConfigWriter.java,v 1.5 2003/02/22 18:52:27 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package com.jrefinery.report.io.ext.writer;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.util.Iterator;

import com.jrefinery.report.io.ext.ExtReportHandler;
import com.jrefinery.report.io.ext.ParserConfigHandler;
import com.jrefinery.report.util.Log;

/**
 * A parser configuration writer.
 * 
 * @author Thomas Morgner
 */
public class ParserConfigWriter extends AbstractXMLDefinitionWriter
{
  /**
   * Creates a new writer.
   * 
   * @param reportWriter  the report writer.
   */
  public ParserConfigWriter(ReportWriter reportWriter)
  {
    super(reportWriter);
  }

  /**
   * Writes the XML.
   * 
   * @param writer  the writer.
   * 
   * @throws IOException if there is an I/O problem.
   */
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

  /**
   * Writes a factory element.
   * 
   * @param w  the writer.
   * @param tagName  the tag name.
   * @param it  an iterator over a collection of factories, which should be defined
   * for the target report.
   *
   * @throws IOException if there is an I/O problem.
   */
  public void writeFactory (Writer w, String tagName, Iterator it)
    throws IOException
  {
    while (it.hasNext())
    {
      Object itObject = it.next();
      try
      {
        Class itClass = itObject.getClass();
        Constructor c = itClass.getConstructor(new Class[0]);
        if (c == null)
        {
          continue;
        }
      }
      catch (Exception e)
      {
        Log.warn ("FactoryClass " + itObject.getClass() 
                  + " has no default constructor. This class will be ignored"); 
        continue;
      }

      String className = itObject.getClass().getName();
      writeTag(w, tagName, ParserConfigHandler.CLASS_ATTRIBUTE, className, CLOSE);
    }
  }

}
