/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ----------
 * First.java
 * ----------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: First.java,v 1.15 2003/01/14 21:03:52 taqua Exp $
 *
 * Changes
 * -------
 * 15-Jul-2002 : Version 1 (DG);
 * 20-Nov-2002 : Corrected possible read error if the icon is not read completly from the zip file;
 *
 */

package com.jrefinery.report.demo;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.preview.PreviewFrame;
import com.jrefinery.report.util.ExceptionDialog;
import com.jrefinery.report.util.ReportConfiguration;
import com.jrefinery.ui.ApplicationFrame;
import com.jrefinery.ui.RefineryUtilities;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

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
public class First extends ApplicationFrame implements ActionListener
{

  /** The data for the report. */
  private TableModel data;

  /** The report (read from first.xml template). */
  private JFreeReport report;

  /**
   * Constructs the demo application.
   *
   * @param title  the frame title.
   */
  public First(String title)
  {
    super(title);
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
    JMenuBar mb = new JMenuBar();
    JMenu fileMenu = new JMenu("File");

    JMenuItem previewItem = new JMenuItem("Preview Report");
    previewItem.setActionCommand("PREVIEW");
    previewItem.addActionListener(this);

    JMenuItem exitItem = new JMenuItem("Exit");
    exitItem.setActionCommand("EXIT");
    exitItem.addActionListener(this);

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
    JPanel content = new JPanel(new BorderLayout());
    content.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    this.data = readData();
    JTable table = new JTable(data);
    table.setDefaultRenderer(java.awt.Image.class, new ImageCellRenderer());
    table.setRowHeight(26);
    JScrollPane scrollPane = new JScrollPane(table);
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
    return new FirstDemoTableModel();
  }

  /**
   * Handles action events.
   *
   * @param e  the event.
   */
  public void actionPerformed(ActionEvent e)
  {
    String command = e.getActionCommand();
    if (command.equals("PREVIEW"))
    {
      previewReport();
    }
    else if (command.equals("EXIT"))
    {
      dispose();
      System.exit(0);
    }
  }

  /**
   * Displays a print preview screen for the sample report.
   */
  protected void previewReport()
  {

    try
    {
      if (this.report == null)
      {
        URL in = getClass().getResource("/com/jrefinery/report/demo/first.xml");
        this.report = parseReport(in);
        this.report.setData(this.data);
      }

      if (this.report != null)
      {
        PreviewFrame frame = new PreviewFrame(this.report);
        frame.getBase().setLargeIconsEnabled(true);
        frame.getBase().setToolbarFloatable(false);
        frame.pack();
        RefineryUtilities.positionFrameRandomly(frame);
        frame.setVisible(true);
        frame.requestFocus();
      }
    }
    catch (ReportProcessingException rpe)
    {
      ExceptionDialog.showExceptionDialog("Error", "Unable to init", rpe);
    }

  }

  /**
   * Reads the report from the first.xml report template.
   *
   * @param templateURL The template location.
   *
   * @return A report.
   */
  private JFreeReport parseReport(URL templateURL)
  {

    JFreeReport result = null;
    ReportGenerator generator = ReportGenerator.getInstance();
    try
    {
      result = generator.parseReport(templateURL);
    }
    catch (Exception e)
    {
      ExceptionDialog.showExceptionDialog("Error on parsing",
                                          "Error while parsing " + templateURL, e);
    }
    return result;

  }

  /**
   * Entry point for running the demo application...
   *
   * @param args ignored.
   */
  public static void main(String[] args)
  {
    ReportConfiguration.getGlobalConfig().setDisableLogging(true);
    //ReportConfiguration.getGlobalConfig().setLogLevel(Log.);
    First frame = new First("First Report");
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }

}