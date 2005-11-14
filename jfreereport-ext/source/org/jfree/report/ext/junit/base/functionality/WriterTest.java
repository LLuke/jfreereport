/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ------------------------------
 * WriterTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: WriterTest.java,v 1.10 2005/09/20 16:58:23 taqua Exp $
 *
 * Changes
 * -------------------------
 * 01.06.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.functionality;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import junit.framework.TestCase;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportBuilderHints;
import org.jfree.report.demo.helper.XmlDemoHandler;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.modules.parser.extwriter.ReportConverter;
import org.jfree.report.modules.parser.extwriter.ReportWriter;
import org.jfree.report.modules.parser.ext.factory.objects.DefaultClassFactory;
import org.jfree.report.modules.parser.ext.factory.objects.BandLayoutClassFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.DefaultStyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.PageableLayoutStyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.templates.DefaultTemplateCollection;
import org.jfree.report.modules.parser.ext.factory.elements.DefaultElementFactory;
import org.jfree.report.modules.parser.ext.factory.datasource.DefaultDataSourceFactory;
import org.jfree.util.Log;
import org.jfree.xml.factory.objects.URLClassFactory;
import org.jfree.xml.factory.objects.ArrayClassFactory;
import org.xml.sax.InputSource;

public class WriterTest extends TestCase
{

  public WriterTest(final String s)
  {
    super(s);
  }

  public void testConvertReport() throws Exception
  {
    final ReportConverter rc = new ReportConverter();
    XmlDemoHandler[] handlers = FunctionalityTestLib.getAllXmlDemoHandlers();
    for (int i = 0; i < handlers.length; i++)
    {
      XmlDemoHandler handler = handlers[i];
      final URL url = handler.getReportDefinitionSource();
      assertNotNull("Failed to locate " + url, url);

      final ByteArrayOutputStream bo = new ByteArrayOutputStream();
      try
      {
        rc.convertReport(url, url,
            new OutputStreamWriter (bo, "UTF-16"), "UTF-16");
        final ByteArrayInputStream bin = new ByteArrayInputStream(bo.toByteArray());
        ReportGenerator.getInstance().parseReport(new InputSource(bin), url);
      }
      catch (Exception e)
      {
        Log.debug("Failed to write or parse " + url, e);
        Log.debug (bo.toString("UTF-16"));
        fail();
      }
    }
  }

  public void testWriteReport() throws Exception
  {
    XmlDemoHandler[] handlers = FunctionalityTestLib.getAllXmlDemoHandlers();
    for (int i = 0; i < handlers.length; i++)
    {
      XmlDemoHandler handler = handlers[i];
      final URL url = handler.getReportDefinitionSource();
      assertNotNull("Failed to locate " + url, url);

      final ByteArrayOutputStream bo = new ByteArrayOutputStream();
      JFreeReport report = null;
      try
      {
        report = ReportGenerator.getInstance().parseReport(url);
//        ReportBuilderHints ph = report.getReportBuilderHints();
//        if (ph == null)
//        {
//          continue;
//        }
//        String type = (String) ph.getHint(report, "parser.type", String.class);
//        if (type == null)
//        {
//          continue;
//        }
      }
      catch (Exception e)
      {
        Log.debug("Failed to parse " + url, e);
        fail();
      }
      try
      {
        final ReportWriter writer = new ReportWriter
          (report, "UTF-16", ReportWriter.createDefaultConfiguration(report));
        writer.addClassFactoryFactory(new URLClassFactory());
        writer.addClassFactoryFactory(new DefaultClassFactory());
        writer.addClassFactoryFactory(new BandLayoutClassFactory());
        writer.addClassFactoryFactory(new ArrayClassFactory());

        writer.addStyleKeyFactory(new DefaultStyleKeyFactory());
        writer.addStyleKeyFactory(new PageableLayoutStyleKeyFactory());
        writer.addTemplateCollection(new DefaultTemplateCollection());
        writer.addElementFactory(new DefaultElementFactory());
        writer.addDataSourceFactory(new DefaultDataSourceFactory());

        final OutputStreamWriter owriter = new OutputStreamWriter (bo, "UTF-16");
        writer.write(owriter);
        owriter.close();
      }
      catch (Exception e)
      {
        Log.debug("Failed to write " + url, e);
        fail();
      }

      try
      {
        final ByteArrayInputStream bin = new ByteArrayInputStream(bo.toByteArray());
        ReportGenerator.getInstance().parseReport(new InputSource(bin), url);
      }
      catch (Exception e)
      {
        Log.debug("Failed to (re)parse " + url, e);
        Log.debug (bo.toString("UTF-16"));
        fail();
      }
    }
  }

}
