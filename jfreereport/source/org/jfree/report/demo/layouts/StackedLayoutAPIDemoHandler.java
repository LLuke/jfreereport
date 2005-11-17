/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * StackedLayoutAPIDemoHandler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: StackedLayoutAPIDemoHandler.java,v 1.1 2005/08/29 17:39:33 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.report.demo.layouts;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.net.URL;
import javax.swing.JComponent;

import org.jfree.report.ElementAlignment;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportHeader;
import org.jfree.report.demo.helper.AbstractDemoHandler;
import org.jfree.report.demo.helper.PreviewHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.demo.helper.SimpleDemoFrame;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.ui.FloatDimension;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ObjectUtilities;

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
public class StackedLayoutAPIDemoHandler extends AbstractDemoHandler
{
  private DemoTextInputPanel inputPanel;
  private PropertyUpdatePreviewHandler previewHandler;

  /**
   * Creates and displays a simple report.
   */
  public StackedLayoutAPIDemoHandler ()
  {
    inputPanel = new DemoTextInputPanel();
    previewHandler = new PropertyUpdatePreviewHandler(this);
  }

  public JComponent getPresentationComponent()
  {
    return inputPanel;
  }

  /**
   * Creates a report definition.
   *
   * @return a report definition.
   */
  public JFreeReport createReport() throws ReportDefinitionException
  {

    final JFreeReport report = new JFreeReport();
    final ReportHeader reportHeader = report.getReportHeader();
    report.setName(getDemoName());

    TextFieldElementFactory factory = new TextFieldElementFactory();
    factory.setName("T1");
    factory.setAbsolutePosition(new Point2D.Float(0, 0));
    factory.setMinimumSize(new FloatDimension(150, 12));
    factory.setColor(Color.black);
    factory.setHorizontalAlignment(ElementAlignment.RIGHT);
    factory.setVerticalAlignment(ElementAlignment.MIDDLE);
    factory.setNullString("-");
    factory.setFieldname(DemoReportControler.MESSAGE_ONE_FIELDNAME);
    factory.setDynamicHeight(Boolean.TRUE);
    reportHeader.addElement(factory.createElement());

    factory = new TextFieldElementFactory();
    factory.setName("T2");
    factory.setAbsolutePosition(new Point2D.Float(200, 0));
    factory.setHorizontalAlignment(ElementAlignment.LEFT);
    factory.setFieldname(DemoReportControler.MESSAGE_TWO_FIELDNAME);
    reportHeader.addElement(factory.createElement());

    report.setPropertyMarked(DemoReportControler.MESSAGE_ONE_FIELDNAME, true);
    report.setPropertyMarked(DemoReportControler.MESSAGE_TWO_FIELDNAME, true);
    report.setProperty(DemoReportControler.MESSAGE_ONE_FIELDNAME, "Hello");
    report.setProperty(DemoReportControler.MESSAGE_TWO_FIELDNAME, "World");
    return report;

  }

  public String getDemoName()
  {
    return "Stacked Layout Manager Demo (API)";
  }

  public PreviewHandler getPreviewHandler()
  {
    return previewHandler;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative("stacked-layout.html", StackedLayoutXMLDemoHandler.class);
  }


  public static void main (final String[] args)
  {
    JFreeReportBoot.getInstance().start();

    final StackedLayoutAPIDemoHandler demoHandler = new StackedLayoutAPIDemoHandler();
    final SimpleDemoFrame frame = new SimpleDemoFrame(demoHandler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }
}
