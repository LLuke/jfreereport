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
 * ReportWriter.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportWriterTest.java,v 1.4 2003/11/01 19:57:02 taqua Exp $
 *
 * Changes
 * -------------------------
 * 01.06.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.basic.io.ext.writer;

import java.awt.geom.Line2D;
import java.io.OutputStreamWriter;
import java.io.Writer;

import junit.framework.TestCase;
import org.jfree.report.JFreeReport;
import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.StaticDataSource;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.ext.factory.datasource.DefaultDataSourceFactory;
import org.jfree.report.modules.parser.ext.factory.elements.DefaultElementFactory;
import org.jfree.report.modules.parser.ext.factory.objects.BandLayoutClassFactory;
import org.jfree.report.modules.parser.ext.factory.objects.DefaultClassFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.DefaultStyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.PageableLayoutStyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.templates.DefaultTemplateCollection;
import org.jfree.report.modules.parser.extwriter.DataSourceWriter;
import org.jfree.report.modules.parser.extwriter.ReportWriter;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.xml.Parser;
import org.jfree.xml.factory.objects.ArrayClassFactory;
import org.jfree.xml.factory.objects.ClassFactory;
import org.jfree.xml.factory.objects.URLClassFactory;

public class ReportWriterTest extends TestCase
{
  public ReportWriterTest(final String s)
  {
    super(s);
  }

  private ReportWriter createWriter()
  {
    final JFreeReport report = new JFreeReport();
    final ReportConfiguration repConf = new ReportConfiguration(report.getReportConfiguration());
    repConf.setConfigProperty
        (Parser.CONTENTBASE_KEY, "file://tmp/");

    final ReportWriter writer = new ReportWriter(report, "UTF-16", repConf);
    writer.addClassFactoryFactory(new URLClassFactory());
    writer.addClassFactoryFactory(new DefaultClassFactory());
    writer.addClassFactoryFactory(new BandLayoutClassFactory());
    writer.addClassFactoryFactory(new ArrayClassFactory());

    writer.addStyleKeyFactory(new DefaultStyleKeyFactory());
    writer.addStyleKeyFactory(new PageableLayoutStyleKeyFactory());
    writer.addTemplateCollection(new DefaultTemplateCollection());
    writer.addElementFactory(new DefaultElementFactory());
    writer.addDataSourceFactory(new DefaultDataSourceFactory());
    return writer;
  };

  public void testFactories()
  {
    final ReportWriter writer = createWriter();
    final ClassFactory cc = writer.getClassFactoryCollector();
    assertNotNull(cc.getDescriptionForClass(DataRowDataSource.class));
    assertEquals(cc.getDescriptionForClass
      (DataRowDataSource.class).getObjectClass(), DataRowDataSource.class);
    System.out.println(cc.getDescriptionForClass(DataRowDataSource.class));
  }

  public void testDataSourceWriter() throws Exception
  {
    final ReportWriter writer = createWriter();
    final StaticDataSource ds = new StaticDataSource(new Line2D.Float());
    final ClassFactory cc = writer.getClassFactoryCollector();
    final DataSourceWriter dsW = new DataSourceWriter(writer,
        ds, cc.getDescriptionForClass(ds.getClass()), 0, new CommentHintPath());
    final Writer w = new OutputStreamWriter(System.out, "UTF-16");
    dsW.write(w);
    w.flush();
  }
}
