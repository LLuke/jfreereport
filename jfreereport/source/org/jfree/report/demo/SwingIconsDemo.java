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
 * -------------------
 * SwingIconsDemo.java
 * -------------------
 * (C)opyright 2002, 2003, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: SwingIconsDemo.java,v 1.4.4.1 2004/04/06 13:56:13 taqua Exp $
 *
 * Changes
 * -------
 * 15-Jul-2002 : Version 1 (DG);
 * 20-Nov-2002 : Corrected possible read error if the icon is not read completely from the zip file;
 * 27-Feb-2003 : Renamed First.java --> SwingIconsDemo.java (DG);
 *
 */

package org.jfree.report.demo;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.demo.helper.AbstractDemoFrame;
import org.jfree.report.demo.helper.ImageCellRenderer;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.action.ActionMenuItem;
import org.jfree.xml.ElementDefinitionException;

/**
 * A demonstration application.
 * <P>
 * This demo is written up in the JFreeReport PDF Documentation.  Please notify David Gilbert
 * (david.gilbert@object-refinery.com) if you need to make changes to this file.
 * <P>
 * To run this demo, you need to have the Java Look and Feel Icons jar file on your classpath.
 *
 * @author David Gilbert
 */
public class SwingIconsDemo extends AbstractDemoFrame
{
  /** The data for the report. */
  private TableModel data;

  /**
   * Constructs the demo application.
   *
   * @param title  the frame title.
   */
  public SwingIconsDemo(final String title)
  {
    setTitle(title);
    setJMenuBar(createMenuBar());
    setContentPane(createContent());
  }

  /**
   * Creates a menu bar.
   *
   * @return the menu bar.
   */
  public JMenuBar createMenuBar()
  {
    final JMenuBar mb = new JMenuBar();
    final JMenu fileMenu = createJMenu("menu.file");

    final JMenuItem previewItem = new ActionMenuItem(getPreviewAction());
    final JMenuItem exitItem = new ActionMenuItem(getCloseAction());

    fileMenu.add(previewItem);
    fileMenu.addSeparator();
    fileMenu.add(exitItem);
    mb.add(fileMenu);
    return mb;
  }

  /**
   * Creates the content for the application frame.
   *
   * @return a panel containing the basic user interface.
   */
  public JPanel createContent()
  {
    final JPanel content = new JPanel(new BorderLayout());
    content.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    this.data = readData();
    final JTable table = new JTable(data);
    table.setDefaultRenderer(java.awt.Image.class, new ImageCellRenderer());
    table.setRowHeight(26);
    final JScrollPane scrollPane = new JScrollPane(table);
    content.add(scrollPane);
    return content;
  }

  /**
   * Creates a data set using Java icons.
   *
   * @return the report data.
   */
  private TableModel readData()
  {
    return new SwingIconsDemoTableModel();
  }

  /**
   * Displays a print preview screen for the sample report.
   */
  protected void attemptPreview()
  {
    final URL in = getClass().getResource("/org/jfree/report/demo/swing-icons.xml");
    if (in == null)
    {
      JOptionPane.showMessageDialog(this,
          MessageFormat.format(getResources().getString("report.definitionnotfound"),
              new Object[]{in}),
          getResources().getString("error"), JOptionPane.ERROR_MESSAGE);
    }

    final JFreeReport report;
    try
    {
      report = parseReport(in);
      report.setData(this.data);
    }
    catch (Exception ex)
    {
      showExceptionDialog("report.definitionfailure", ex);
      return;
    }

    try
    {
      final PreviewFrame frame = new PreviewFrame(report);
      frame.getBase().setLargeIconsEnabled(true);
      frame.getBase().setToolbarFloatable(false);
      frame.pack();
      RefineryUtilities.positionFrameRandomly(frame);
      frame.setVisible(true);
      frame.requestFocus();
    }
    catch (ReportProcessingException rpe)
    {
      showExceptionDialog("report.previewfailure", rpe);
    }

  }

  /**
   * Reads the report from the swing-icons.xml report template.
   *
   * @param templateURL The template location.
   *
   * @return A report.
   * @throws ElementDefinitionException if the report generator encountered an error.
   * @throws IOException if there was an IO error while reading from the URL.
   */
  private JFreeReport parseReport(final URL templateURL)
      throws IOException, ElementDefinitionException
  {

    final ReportGenerator generator = ReportGenerator.getInstance();
    return generator.parseReport(templateURL);

  }

  /**
   * Entry point for running the demo application...
   *
   * @param args ignored.
   */
  public static void main(final String[] args)
  {
    // initialize JFreeReport
    JFreeReportBoot.getInstance().start();

    ReportConfiguration.getGlobalConfig().setLogLevel("Error");
    // update the log system to use the new settings ...
    Log.getJFreeReportLog().init();

    final SwingIconsDemo frame = new SwingIconsDemo("Swing Icons Report");
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }

}