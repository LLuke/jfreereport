/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ---------------
 * HelloWorld.java
 * ---------------
 * (C)opyright 2002, 2003, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: HelloWorld.java,v 1.11 2005/08/29 17:43:59 taqua Exp $
 *
 * Changes
 * -------
 * 10-Dec-2002 : Version 1 (DG);
 *
 */

package org.jfree.report.demo;

import java.awt.Color;
import java.awt.geom.Point2D;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.report.ElementAlignment;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.demo.helper.DefaultCloseHandler;
import org.jfree.report.demo.helper.DemoHandler;
import org.jfree.report.demo.helper.PreviewHandler;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.modules.gui.base.PreviewDialog;
import org.jfree.ui.FloatDimension;
import org.jfree.util.Log;

/**
 * A very simple JFreeReport demo.  The purpose of this demo is to illustrate the basic
 * steps required to connect a report definition with some data and display a report
 * preview on-screen.
 * <p/>
 * In this example, the report definition is created in code.  It is also possible to read
 * a report definition from an XML file...that is demonstrated elsewhere.
 *
 * @author David Gilbert
 */
public class HelloWorld implements DemoHandler
{
  /**
   * A helper class to make this demo accessible from the DemoFrontend.
   */
  private class HelloWorldPreviewHandler implements PreviewHandler
  {
    public HelloWorldPreviewHandler()
    {
    }

    public void attemptPreview()
    {
      executeReport();
    }
  }

  /**
   * Creates and displays a simple report.
   */
  public HelloWorld ()
  {
  }

  protected void executeReport ()
  {
    final TableModel data = createData();
    final JFreeReport report = createReportDefinition();
    report.setData(data);
    try
    {
      final PreviewDialog preview = new PreviewDialog(report);
      preview.addWindowListener(new DefaultCloseHandler());
      preview.pack();
      preview.setVisible(true);
    }
    catch (ReportProcessingException e)
    {
      Log.error("Failed to generate report ", e);
    }
  }

  /**
   * Creates a small dataset to use in a report.  JFreeReport always reads data from a
   * <code>TableModel</code> instance.
   *
   * @return a dataset.
   */
  private TableModel createData ()
  {

    final Object[] columnNames = new String[]{"Column1", "Column2"};
    final DefaultTableModel result = new DefaultTableModel(columnNames, 1);
    result.setValueAt("Hello\n", 0, 0);
    result.setValueAt("World!\n", 0, 1);
    return result;

  }

  /**
   * Creates a report definition.
   *
   * @return a report definition.
   */
  private JFreeReport createReportDefinition ()
  {

    final JFreeReport report = new JFreeReport();
    report.setName(getDescription());

    TextFieldElementFactory factory = new TextFieldElementFactory();
    factory.setName("T1");
    factory.setAbsolutePosition(new Point2D.Float(0, 0));
    factory.setMinimumSize(new FloatDimension(150, 12));
    factory.setColor(Color.black);
    factory.setHorizontalAlignment(ElementAlignment.RIGHT);
    factory.setVerticalAlignment(ElementAlignment.MIDDLE);
    factory.setNullString("-");
    factory.setFieldname("Column1");
    report.getItemBand().addElement(factory.createElement());

    factory = new TextFieldElementFactory();
    factory.setName("T2");
    factory.setAbsolutePosition(new Point2D.Float(200, 0));
    factory.setMinimumSize(new FloatDimension(150, 12));
    factory.setColor(Color.black);
    factory.setHorizontalAlignment(ElementAlignment.LEFT);
    factory.setVerticalAlignment(ElementAlignment.MIDDLE);
    factory.setNullString("-");
    factory.setFieldname("Column2");
    report.getItemBand().addElement(factory.createElement());
    return report;

  }

  /**
   * Returns a short description of the demo.
   *
   * @return
   */
  protected String getDescription ()
  {
    return "A Very Simple Report";
  }


  public String getDemoName()
  {
    return "Hello World Demo (External)";
  }

  public PreviewHandler getPreviewHandler()
  {
    return new HelloWorldPreviewHandler();
  }


  /**
   * The starting point for the "Hello World" demo application.
   *
   * @param args ignored.
   */
  public static void main (final String[] args)
  {
    // this also installs the log.
    // initialize JFreeReport
    JFreeReportBoot.getInstance().start();

    //final HelloWorld app =
    new HelloWorld().executeReport();
  }

}