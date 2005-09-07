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
 * StackedLayoutXMLDemoHandler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: StackedLayoutXMLDemoHandler.java,v 1.1 2005/08/29 17:39:33 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.report.demo.layouts;

import java.net.URL;
import javax.swing.JComponent;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.AbstractXmlDemoHandler;
import org.jfree.report.demo.helper.PreviewHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.demo.helper.SimpleDemoFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ObjectUtilities;

/**
 * A simple report that shows the user input as report property value.
 *
 * @author Thomas Morgner
 */
public class StackedLayoutXMLDemoHandler extends AbstractXmlDemoHandler
{
  private DemoTextInputPanel panel;
  private PropertyUpdatePreviewHandler previewHandler;

  /**
   * Constructs the demo application.
   *
   * @param title the frame title.
   */
  public StackedLayoutXMLDemoHandler ()
  {
    panel = new DemoTextInputPanel();
    previewHandler = new PropertyUpdatePreviewHandler(this);
  }

  public String getDemoName()
  {
    return "Stacked Layout Manager Demo (XML)";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    JFreeReport report = parseReport();
    report.setProperty("Message1", panel.getMessageOne());
    report.setProperty("Message2", panel.getMessageTwo());
    return report;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative("stacked-layout.html", StackedLayoutXMLDemoHandler.class);
  }

  public JComponent getPresentationComponent()
  {
    return panel;
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative("stacked-layout.xml", StackedLayoutXMLDemoHandler.class);
  }

  public PreviewHandler getPreviewHandler()
  {
    return previewHandler;
  }


  public static void main (final String[] args)
  {
    JFreeReportBoot.getInstance().start();

    final StackedLayoutXMLDemoHandler demoHandler = new StackedLayoutXMLDemoHandler();
    final SimpleDemoFrame frame = new SimpleDemoFrame(demoHandler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }
}