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
 * ------------------------
 * CSVReaderDemo.java
 * ------------------------
 * (C)opyright 2000-2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Mimil;
 *
 * $Id: CSVReaderDemo.java,v 1.2 2004/08/07 17:45:47 mimil Exp $
 *
 * $Log: CSVReaderDemo.java,v $
 * Revision 1.2  2004/08/07 17:45:47  mimil
 * Some JavaDocs
 *
 * Revision 1.1  2004/08/07 14:35:14  mimil
 * Initial version
 *
 */

package org.jfree.report.ext.input;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.table.TableModel;

import org.jfree.report.ElementAlignment;
import org.jfree.report.ItemBand;
import org.jfree.report.JFreeReport;
import org.jfree.report.PageHeader;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.demo.helper.AbstractDemoFrame;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.modules.gui.base.components.ExceptionDialog;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.FontDefinition;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.action.AbstractActionDowngrade;
import org.jfree.ui.action.ActionMenuItem;

/**
 * Demo that show how to use <code>CSVReader</code> to generate <code>TableModel</code>
 * for JFreeReport input data.
 *
 * @see CSVReader
 */
public class CSVReaderDemo extends AbstractDemoFrame
{

  private TableModel data;
  private boolean columnfirst = false;

  private String text = "R0C0; R0C1; R0C2;\n"
          + "R1C0; R1C1; R1C2;\n"
          + "R2C0; R2C1; R2C2;";
  private String textColumn = "C0; C1; C2;\n";

  /**
   * Creates the demo workspace.
   */
  public CSVReaderDemo ()
  {
    this.setJMenuBar(this.createMenuBar());
    final JPanel panel = new JPanel(new FlowLayout());
    panel.add(new JLabel("Text:"));
    panel.add(new JTextArea(this.text));
    setContentPane(panel);
    this.setTitle("CSVReader Demos");
  }

  /**
   * Starts JFreeReport system.
   */
  protected void attemptPreview ()
  {
    final JFreeReport report = new JFreeReport();
    report.setName("CSVReader API demos");
    report.setData(this.data);

    this.configurePageHeader(report.getPageHeader());
    this.configureItemBand(report.getItemBand());

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

  protected void attemptStringApiPreview ()
          throws IOException
  {
    this.data = new CSVReader(new BufferedReader(new StringReader(text))).getTableModel();
    this.attemptPreview();
  }

  protected void attemptStringDefApiPreview ()
          throws IOException
  {
    final CSVReader csv = new CSVReader(new BufferedReader(new StringReader(textColumn + text)));
    csv.setColumnNameFirstLine(true);
    this.data = csv.getTableModel();
    this.attemptPreview();
  }

  protected void attemptFileApiPreview ()
          throws IOException
  {
    this.data = new CSVReader(getClass().getResource("/org/jfree/report/ext/input/file.csv")
            .openStream()).getTableModel();
    this.attemptPreview();
  }

  /**
   * Adds elements to the report item band.
   *
   * @param band The report item band
   */
  private void configureItemBand (final ItemBand band)
  {
    final ElementStyleSheet ess = band.getStyle();
    ess.setFontDefinitionProperty(new FontDefinition("SansSerif", 10));

    final TextFieldElementFactory facto = new TextFieldElementFactory();
    facto.setColor(Color.blue);
    facto.setNullString("-");

    for (int i = 0; i < this.data.getColumnCount(); i++)
    {
      facto.setFieldname(this.data.getColumnName(i));
      facto.setName(this.data.getColumnName(i));
      facto.setAbsolutePosition(new Point2D.Float(i * 70, 0));
      facto.setMinimumSize(new Dimension(70, 10));
      band.addElement(facto.createElement());
    }
  }

  /**
   * Adds elements to the report page header.
   *
   * @param header The report page header
   */
  private void configurePageHeader (final PageHeader header)
  {
    final LabelElementFactory title = new LabelElementFactory();
    title.setText("CSVReader Demos by Mimil");
    title.setHorizontalAlignment(ElementAlignment.CENTER);
    title.setBold(Boolean.TRUE);
    title.setFontSize(new Integer(20));
    title.setColor(Color.black);
    title.setMinimumSize(new Dimension(-100, 70));
    title.setAbsolutePosition(new Point2D.Float(0, 0));
    title.setName("Title");

    header.addElement(title.createElement());

    if (this.columnfirst)
    {
      final ElementStyleSheet ess = header.getStyle();
      ess.setFontDefinitionProperty(new FontDefinition("SansSerif", 10));

      final LabelElementFactory lbl = new LabelElementFactory();
      lbl.setColor(Color.RED);

      for (int i = 0; i < this.data.getColumnCount(); i++)
      {
        lbl.setText(this.data.getColumnName(i));
        lbl.setName(this.data.getColumnName(i));
        lbl.setAbsolutePosition(new Point2D.Float(i * 70, 70));
        lbl.setMinimumSize(new Dimension(70, 10));
        header.addElement(lbl.createElement());
      }
    }
  }


  /**
   * Creates the menu bar for this demo application.
   *
   * @return the menu bar.
   */
  public JMenuBar createMenuBar ()
  {
    final JMenuBar mb = new JMenuBar();
    final JMenu fileMenu = new JMenu("Demos");


    final JMenuItem exitItem = new ActionMenuItem(getCloseAction());
    final JMenuItem api = new ActionMenuItem(new CSVReaderStringApiAction());
    final JMenuItem file = new ActionMenuItem(new CSVReaderFileApiAction());
    final JMenuItem autoApi = new ActionMenuItem(new CSVReaderStringAutoApiAction());
    final JMenuItem DefApi = new ActionMenuItem(new CSVReaderStringDefApiAction());

    fileMenu.add(api);
    fileMenu.add(autoApi);
    fileMenu.add(DefApi);
    fileMenu.add(file);
    fileMenu.addSeparator();

    fileMenu.add(exitItem);
    mb.add(fileMenu);
    return mb;
  }

  protected class CSVReaderStringAutoApiAction extends AbstractActionDowngrade
  {

    public CSVReaderStringAutoApiAction ()
    {
      putValue(NAME, "String Preview (column names auto)");
    }

    public void actionPerformed (final ActionEvent e)
    {
      columnfirst = true;
      try
      {
        attemptStringApiPreview();
      }
      catch (IOException e1)
      {
        e1.printStackTrace();
      }
    }
  }

  protected class CSVReaderStringDefApiAction extends AbstractActionDowngrade
  {

    public CSVReaderStringDefApiAction ()
    {
      putValue(NAME, "String Preview (column names on first line)");
    }

    public void actionPerformed (final ActionEvent e)
    {
      columnfirst = true;
      try
      {
        attemptStringDefApiPreview();
      }
      catch (IOException e1)
      {
        handleError("Failed to load the file.", e1);
      }
    }
  }

  protected class CSVReaderStringApiAction extends AbstractActionDowngrade
  {

    public CSVReaderStringApiAction ()
    {
      putValue(NAME, "String Preview");
    }

    public void actionPerformed (final ActionEvent e)
    {
      try
      {
        attemptStringApiPreview();
      }
      catch (IOException e1)
      {
        handleError("Failed to load the file.", e1);
      }
    }
  }

  protected class CSVReaderFileApiAction extends AbstractActionDowngrade
  {

    public CSVReaderFileApiAction ()
    {
      putValue(NAME, "File Preview");
    }

    public void actionPerformed (final ActionEvent e)
    {
      try
      {
        attemptFileApiPreview();
      }
      catch (IOException e1)
      {
        handleError("Failed to load the file.", e1);
      }
    }
  }

  private void handleError (final String message, final  Exception e)
  {
    ExceptionDialog.showExceptionDialog("Error", message, e);
  }

  public static void main (final String args[])
  {
    final CSVReaderDemo demo = new CSVReaderDemo();
    demo.pack();
    RefineryUtilities.centerFrameOnScreen(demo);
    demo.setVisible(true);
  }
}
