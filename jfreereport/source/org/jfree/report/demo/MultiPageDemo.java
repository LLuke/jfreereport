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
 * MultiPageDemo.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.demo;

import java.awt.BorderLayout;
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

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.demo.helper.AbstractDemoFrame;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.action.ActionButton;
import org.jfree.ui.action.ActionMenuItem;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

public class MultiPageDemo extends AbstractDemoFrame
{
  /**
   * The data for the report.
   */
  private final TableModel data;

  /**
   * Constructs the demo application.
   *
   * @param title the frame title.
   */
  public MultiPageDemo (final String title)
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
  public JMenuBar createMenuBar ()
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
  public JPanel createContent ()
  {
    final JPanel content = new JPanel(new BorderLayout());
    content.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

    final String d = "This demo shows how one could create a page-spanning report.";
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

  protected JFreeReport parseReport ()
  {
    final URL in = ObjectUtilities.getResource
            ("org/jfree/report/demo/multipage-report.xml", OpenSourceDemo.class);

    if (in == null)
    {
      JOptionPane.showMessageDialog(this,
              MessageFormat.format(getResources().getString("report.definitionnotfound"),
                      new Object[]{in}),
              getResources().getString("error"), JOptionPane.ERROR_MESSAGE);
      return null;
    }
    try
    {
      final JFreeReport report = parseReport(in);
      report.setData(this.data);
      return report;
    }
    catch (Exception ex)
    {
      showExceptionDialog("report.definitionfailure", ex);
      return null;
    }
  }

  /**
   * Displays a print preview screen for the sample report.
   */
  protected void attemptPreview ()
  {
    final JFreeReport report = parseReport();
    if (report == null)
    {
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
   * @param templateURL the template location.
   * @return a report.
   */
  private JFreeReport parseReport (final URL templateURL)
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
   * @param args ignored.
   */
  public static void main (final String[] args)
  {
    // initialize JFreeReport
    JFreeReportBoot.getInstance().start();

    final MultiPageDemo frame = new MultiPageDemo("Multi-Page Demo");
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }
}
