/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * SvgDemoHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 16.02.2006 : Initial version
 */
package org.jfree.report.ext.demo.svg;

import java.io.IOException;
import java.net.URL;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.AbstractXmlDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.demo.helper.SimpleDemoFrame;
import org.jfree.report.resourceloader.DrawableFactory;
import org.jfree.ui.Drawable;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 16.02.2006, 21:41:20
 *
 * @author Thomas Morgner
 */
public class SvgDemoHandler  extends AbstractXmlDemoHandler
{
  /**
   * Default constructor.
   */
  public SvgDemoHandler()
  {
  }

  /**
   * Returns the display name of the demo.
   *
   * @return the name.
   */
  public String getDemoName()
  {
    return "SVG Demo";
  }

  /**
   * Creates the report. This calls the standard parse method and then assigns
   * the table model to the report.
   *
   * @return the fully initialized JFreeReport object.
   * @throws ReportDefinitionException if an error occured preventing the
   * report definition.
   */
  public JFreeReport createReport() throws ReportDefinitionException
  {
    URL url = ObjectUtilities.getResourceRelative
            ("acroread.svg", SvgDemoHandler.class);

    final JFreeReport report = parseReport();

    try
    {
      DrawableFactory drawableFactory = DrawableFactory.getInstance();

      // The drawable factory also offers methods to load the drawable from
      // an byte[], just in case there is no URL to load from.
      //
      // Both the file name and the mime-type are hints for the loader.
      // either a file name in the form *.svg or the svg mime-type must be
      // given, or the loader will not be able to recognize the SVG data.
      //
      // byte[] data = null;
      // Drawable d drawableFactory.createDrawable(data, "image.svg", "image/svg+xml");

      // Lazy as I am, I prefer the URL loading method. This simply loads the
      // file into a byte array and then calls the appropriate method.
      Drawable d = drawableFactory.createDrawable(url);
      report.setProperty("drawable", d);
    }
    catch (IOException e)
    {
      Log.error ("Unable to load the SVG file.");
    }

    return report;
  }

  /**
   * Returns the URL of the HTML document describing this demo.
   *
   * @return the demo description.
   */
  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative("svg.html", SvgDemoHandler.class);
  }

  /**
   * Returns the presentation component for this demo. This component is
   * shown before the real report generation is started. Ususally it contains
   * a JTable with the demo data and/or input components, which allow to configure
   * the report.
   *
   * @return the presentation component, never null.
   */
  public JComponent getPresentationComponent()
  {
    return new JPanel();
  }

  /**
   * Returns the URL of the XML definition for this report.
   *
   * @return the URL of the report definition.
   */
  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative("svg.xml", SvgDemoHandler.class);
  }


  /**
   * Entry point for running the demo application...
   *
   * @param args ignored.
   */
  public static void main (final String[] args)
  {
    // initialize JFreeReport
    JFreeReportBoot.getInstance().start();

    final SvgDemoHandler handler = new SvgDemoHandler();
    final SimpleDemoFrame frame = new SimpleDemoFrame(handler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }
}