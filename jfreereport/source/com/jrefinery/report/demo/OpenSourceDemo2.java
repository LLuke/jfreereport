/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * --------------------
 * OpenSourceDemo2.java
 * --------------------
 * (C)opyright 2003, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 * 03-Jan-2003 : Version 1, based on OpenSourceDemo.java (DG);
 *
 */

package com.jrefinery.report.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import com.jrefinery.report.Element;
import com.jrefinery.report.ItemBand;
import com.jrefinery.report.ItemFactory;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.PageFooter;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.TextElement;
import com.jrefinery.report.function.PageFunction;
import com.jrefinery.report.preview.PreviewFrame;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.base.bandlayout.StaticLayoutManager;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.ReportConfiguration;
import com.jrefinery.ui.ApplicationFrame;
import com.jrefinery.ui.RefineryUtilities;

/**
 * This demo application replicates the report generated by OpenSourceDemo.java, but creates
 * the report in code rather than using an XML report template.
 *
 * @author David Gilbert
 */
public class OpenSourceDemo2 extends ApplicationFrame implements ActionListener
{

  /** The data for the report. */
  private TableModel data;

  /** The report (created in code). */
  private JFreeReport report;

  /**
   * Constructs the demo application.
   *
   * @param title  the frame title.
   */
  public OpenSourceDemo2(String title)
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
    this.data = new OpenSourceProjects();
    JTable table = new JTable(data);
    JScrollPane scrollPane = new JScrollPane(table);
    content.add(scrollPane);
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
        this.report = createReport();
      }

      report.setData(this.data);
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
    // force regeneration on next preview ... for debugging ...
    report = null;
  }

  /**
   * Creates a report definition in code.
   * <p>
   * It is more base to read the definition from an XML report template file, but sometimes you
   * might need to create a report dynamically.
   *
   * @return a report.
   */
  private JFreeReport createReport()
  {

    JFreeReport result = new JFreeReport();
    
    ReportConfiguration config = result.getReportConfiguration();
    config.setConfigProperty("com.jrefinery.report.preview.PreferredWidth", "640.0");
    config.setConfigProperty("com.jrefinery.report.preview.PreferredHeight", "480.0");

    // set up the functions...
    PageFunction f1 = new PageFunction("page_number");
    try {
      result.addFunction(f1);
    }
    catch (Exception e) 
    {
        System.err.println(e.toString());
    }
    
    // set up the item band...    
    ItemBand itemBand = result.getItemBand();
    configureItemBand(itemBand);

    // set up the page footer...
    PageFooter pageFooter = result.getPageFooter();
    configurePageFooter(pageFooter);

    return result;

  }

  /**
   * Configures a blank item band.
   *
   * @param band  the item band to be configured.
   */
  private void configureItemBand(ItemBand band)
  {
    ElementStyleSheet ess = band.getBandDefaults();
    ess.setFontDefinitionProperty(new FontDefinition("SansSerif", 9));

    TextElement field1 = ItemFactory.createStringElement(
        "Name_Field",
        new Rectangle2D.Double(0.0, 7.0, 140.0, 10.0),
        Color.black,
        Element.LEFT,
        Element.TOP,
        null, // font
        "No name", // null string
        "Name"
    );
    field1.getStyle().setFontDefinitionProperty(new FontDefinition("SansSerif",
                                                                   10, true, false, false, false));
    band.addElement(field1);

    TextElement field2 = ItemFactory.createStringElement(
        "URL_Field",
        new Rectangle2D.Double(0.0, 9.0, -100.0, 10.0),
        Color.black,
        Element.RIGHT,
        Element.TOP,
        null, // font
        "No URL", // null string
        "URL"
    );
    field2.getStyle().setFontDefinitionProperty(new FontDefinition("Monospaced", 8));
    band.addElement(field2);

    TextElement field3 = ItemFactory.createStringElement(
        "Description_Field",
        new Rectangle2D.Double(0.0, 20.0, -100.0, 0.0),
        Color.black,
        Element.LEFT,
        Element.TOP,
        null, // font
        "No description available", // null string
        "Description"
    );
    field3.getStyle().setStyleProperty(StaticLayoutManager.DYNAMIC_HEIGHT,
                                       new Boolean(true));
    band.addElement(field3);

  }

  /**
   * Configures a blank page footer.
   *
   * @param footer  the page footer to be configured.
   */
  private void configurePageFooter(PageFooter footer)
  {
    ElementStyleSheet ess = footer.getBandDefaults();
    ess.setFontDefinitionProperty(new FontDefinition("SansSerif", 9));

    footer.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE, new Dimension(0, 20));
    TextElement pageNumberField = ItemFactory.createNumberElement("PageNumber_Field",
        new Rectangle2D.Double(0.0, 0.0, -100.0, -100.0),
        Color.black,
        Element.RIGHT,
        null, // font
        "-", // null string
        "Page 0",
        "page_number"
    );
    pageNumberField.getStyle().setFontDefinitionProperty(new FontDefinition("SansSerif",
                                                                   10, true, false, false, false));
    footer.addElement(pageNumberField);
  }
  
  /**
   * Entry point for running the demo application...
   *
   * @param args  ignored.
   */
  public static void main(String[] args)
  {
    OpenSourceDemo2 frame = new OpenSourceDemo2("Open Source Demo 2");
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }

}