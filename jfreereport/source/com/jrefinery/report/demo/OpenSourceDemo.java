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
 * -------------------
 * OpenSourceDemo.java
 * -------------------
 * (C)opyright 2002, 2003, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: OpenSourceDemo.java,v 1.17 2003/06/27 14:25:16 taqua Exp $
 *
 * Changes
 * -------
 * 29-Nov-2002 : Version 1 (DG);
 *
 */

package com.jrefinery.report.demo;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.text.MessageFormat;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableModel;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.demo.helper.AbstractDemoFrame;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.preview.PreviewFrame;
import com.jrefinery.report.util.ActionButton;
import com.jrefinery.report.util.ActionMenuItem;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.WaitingImageObserver;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple JFreeReport demonstration.  The generated report lists some free and open source
 * software projects for the Java programming language.
 *
 * @author David Gilbert
 */
public class OpenSourceDemo extends AbstractDemoFrame
{

  /** The data for the report. */
  private TableModel data;

  /**
   * Constructs the demo application.
   *
   * @param title  the frame title.
   */
  public OpenSourceDemo(final String title)
  {
    setTitle(title);
    this.data = new OpenSourceProjects();
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
    final JMenu fileMenu = createJMenuItem("menu.file");

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

    final String d = "This demo creates a report listing some useful open source projects for Java.";
    final JTextArea textArea = new JTextArea(d);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.setEditable(false);
    final JScrollPane scroll = new JScrollPane(textArea);
    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    final JTable table = new JTable(this.data);
    final JScrollPane scrollPane = new JScrollPane(table);

    final JButton previewButton = new ActionButton(getPreviewAction());

    content.add(scroll, BorderLayout.NORTH);
    content.add(scrollPane);
    content.add(previewButton, BorderLayout.SOUTH);
    return content;

  }

  /**
   * Displays a print preview screen for the sample report.
   */
  protected void attemptPreview()
  {
    final URL in = getClass().getResource("/com/jrefinery/report/demo/OpenSourceDemo.xml");

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

      //ReportConfiguration config = report.getReportConfiguration();
      //config.setEnableExportExcel(false);
      //config.setEnableExportCSV(false);
      //config.setEnableExportHTML(false);

      // add an image as a report property...
      final URL imageURL = getClass().getResource("/com/jrefinery/report/demo/gorilla.jpg");
      final Image image = Toolkit.getDefaultToolkit().createImage(imageURL);
      final WaitingImageObserver obs = new WaitingImageObserver(image);
      obs.waitImageLoaded();
      report.setProperty("logo", image);
      report.setPropertyMarked("logo", true);

    }
    catch (Exception ex)
    {
      showExceptionDialog("report.definitionfailure", ex);
      return;
    }

    try
    {
      final PreviewFrame frame = new PreviewFrame(report);
      frame.getBase().setToolbarFloatable(true);
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
   * Reads the report from the specified template file.
   *
   * @param templateURL  the template location.
   *
   * @return a report.
   */
  private JFreeReport parseReport(final URL templateURL)
  {

    JFreeReport result = null;
    final ReportGenerator generator = ReportGenerator.getInstance();
    try
    {
      result = generator.parseReport(templateURL);
    }
    catch (Exception e)
    {
      Log.error("Failed to parse the report definition", e);
    }
    return result;

  }

  /**
   * Entry point for running the demo application...
   *
   * @param args  ignored.
   */
  public static void main(final String[] args)
  {
    final OpenSourceDemo frame = new OpenSourceDemo("Open Source Demo");
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }

}