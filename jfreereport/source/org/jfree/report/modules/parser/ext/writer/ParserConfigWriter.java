/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * $Id: ParserConfigWriter.java,v 1.1 2003/07/07 22:44:08 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 * 12-Jul-2003 : Internal collectors should not be declared as parser factories.
 */
package org.jfree.report.modules.parser.ext.writer;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.ArrayList;

import org.jfree.report.modules.parser.ext.ExtReportHandler;
import org.jfree.report.modules.parser.ext.ParserConfigHandler;
import org.jfree.report.util.Log;

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
   * @param indentLevel the current indention level.
   */
  public ParserConfigWriter(final ReportWriter reportWriter, final int indentLevel)
  {
    super(reportWriter, indentLevel);
  }

  /**
   * Writes the XML.
   *
   * @param writer  the writer.
   *
   * @throws IOException if there is an I/O problem.
   */
  public void write(final Writer writer) throws IOException
  {
    writeTag(writer, ExtReportHandler.PARSER_CONFIG_TAG);
    writer.write(getLineSeparator());

    writeFactory(writer, ParserConfigHandler.OBJECT_FACTORY_TAG,
        filterFactories(getReportWriter().getClassFactoryCollector().getFactories()));
    writeFactory(writer, ParserConfigHandler.ELEMENT_FACTORY_TAG,
        filterFactories(getReportWriter().getElementFactoryCollector().getFactories()));
    writeFactory(writer, ParserConfigHandler.STYLEKEY_FACTORY_TAG,
        filterFactories(getReportWriter().getStyleKeyFactoryCollector().getFactories()));
    writeFactory(writer, ParserConfigHandler.TEMPLATE_FACTORY_TAG,
        filterFactories(getReportWriter().getTemplateCollector().getFactories()));
    writeFactory(writer, ParserConfigHandler.DATASOURCE_FACTORY_TAG,
        filterFactories(getReportWriter().getDataSourceCollector().getFactories()));
    // datadefinition not yet implemented ...

    writeCloseTag(writer, ExtReportHandler.PARSER_CONFIG_TAG);
    writer.write(getLineSeparator());
  }

  private Iterator filterFactories (final Iterator it)
  {
    ReportWriter writer = getReportWriter();
    ArrayList factories = new ArrayList();
    while (it.hasNext())
    {
      Object o = it.next();
      if (o.equals(writer.getClassFactoryCollector()))
      {
        continue;
      }
      if (o.equals(writer.getDataSourceCollector()))
      {
        continue;
      }
      if (o.equals(writer.getElementFactoryCollector()))
      {
        continue;
      }
      if (o.equals(writer.getStyleKeyFactoryCollector()))
      {
        continue;
      }
      if (o.equals(writer.getTemplateCollector()))
      {
        continue;
      }
      factories.add(o);
    }
    return factories.iterator();
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
  public void writeFactory(final Writer w, final String tagName, final Iterator it)
      throws IOException
  {
    while (it.hasNext())
    {
      final Object itObject = it.next();
      try
      {
        final Class itClass = itObject.getClass();
        final Constructor c = itClass.getConstructor(new Class[0]);
        if (c == null)
        {
          continue;
        }
      }
      catch (Exception e)
      {
        Log.warn("FactoryClass " + itObject.getClass()
            + " has no default constructor. This class will be ignored");
        continue;
      }

      final String className = itObject.getClass().getName();
      writeTag(w, tagName, ParserConfigHandler.CLASS_ATTRIBUTE, className, CLOSE);
    }
  }

}
