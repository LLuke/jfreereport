/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * -------------------
 * PercentageDemo.java
 * -------------------
 * (C)opyright 2003, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: PercentageDemo.java,v 1.3 2003/04/09 16:21:35 mungady Exp $
 *
 * Changes
 * -------
 * 04-Apr-2003 : Version 1 (DG);
 *
 */

package com.jrefinery.report.demo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.preview.PreviewFrame;
import com.jrefinery.report.util.Log;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple report where column 3 displays (column 1 / column 2) as a percentage.
 *
 * @author David Gilbert
 */
public class PercentageDemo extends ApplicationFrame implements ActionListener
{

  /** The data for the report. */
  private TableModel data;

  /** The report (read from the PercentageDemo.xml template). */
  private JFreeReport report;

  /**
   * Constructs the demo application.
   *
   * @param title  the frame title.
   */
  public PercentageDemo(String title)
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
    
    String d = "In this demo, the TableModel contains two columns A and B. The generated "
             + "report displays the values in these columns, plus a third value (A/B) "
             + "formatted as a percentage.";
    JTextArea textArea = new JTextArea(d);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.setEditable(false);
    JScrollPane scroll = new JScrollPane(textArea);
    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    this.data = createData();
    JTable table = new JTable(data);
    JScrollPane scrollPane = new JScrollPane(table);
    
    JButton previewButton = new JButton("Preview Report");
    previewButton.setActionCommand("PREVIEW");
    previewButton.addActionListener(this);
    
    content.add(scroll, BorderLayout.NORTH);
    content.add(scrollPane);
    content.add(previewButton, BorderLayout.SOUTH);
    return content;
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
        URL in = getClass().getResource("/com/jrefinery/report/demo/PercentageDemo.xml");
        this.report = parseReport(in);
      }
      if (this.report == null)
      {
        JOptionPane.showMessageDialog(this, "The report definition is null");
        return;
      }
      
      this.report.setData(this.data);
            
      PreviewFrame frame = new PreviewFrame(this.report);
      frame.getBase().setToolbarFloatable(true);
      frame.pack();
      RefineryUtilities.positionFrameRandomly(frame);
      frame.setVisible(true);
      frame.requestFocus();
    }
    catch (ReportProcessingException pre)
    {
      Log.error("Failed", pre);
    }
    // force reparsing on next preview ... for debugging ...
    report = null;
  }

  /**
   * Reads the report from the specified template file.
   *
   * @param templateURL  the template location.
   *
   * @return a report.
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
      Log.error("Failed to parse the report definition", e);
    }
    return result;

  }

  /**
   * Creates a sample dataset.
   * 
   * @return A <code>TableModel</code>.
   */
  private TableModel createData() 
  {
      DefaultTableModel data = new DefaultTableModel();
      data.addColumn("A");
      data.addColumn("B");
      data.addRow(new Object[] { new Double(43.0), new Double(127.5) });    
      data.addRow(new Object[] { new Double(57.0), new Double(108.5) });    
      data.addRow(new Object[] { new Double(35.0), new Double(164.8) });    
      data.addRow(new Object[] { new Double(86.0), new Double(164.0) });    
      data.addRow(new Object[] { new Double(12.0), new Double(103.2) });    
      return data;
  }
  
  /**
   * Entry point for running the demo application...
   *
   * @param args  ignored.
   */
  public static void main(String[] args)
  {
    PercentageDemo frame = new PercentageDemo("Percentage Demo");
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }

}