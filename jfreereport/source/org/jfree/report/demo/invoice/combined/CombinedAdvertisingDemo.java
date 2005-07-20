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
 * CombinedAdvertisingDemo.java
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
package org.jfree.report.demo.invoice.combined;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.text.MessageFormat;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReportBoot;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.demo.helper.AbstractDemoFrame;
import org.jfree.report.demo.invoice.AdvertisingTableModel;
import org.jfree.report.demo.invoice.InvoiceTableModel;
import org.jfree.report.demo.invoice.SimpleInvoiceDemo;
import org.jfree.report.demo.invoice.model.Advertising;
import org.jfree.report.demo.invoice.model.Article;
import org.jfree.report.demo.invoice.model.Customer;
import org.jfree.report.demo.invoice.model.Invoice;
import org.jfree.report.modules.misc.tablemodel.JoiningTableModel;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.util.Log;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.action.ActionButton;
import org.jfree.ui.action.ActionMenuItem;
import org.jfree.util.ObjectUtilities;

public class CombinedAdvertisingDemo extends AbstractDemoFrame
{
  private TableModel data;

  public CombinedAdvertisingDemo ()
  {
    data = initData();

    setTitle("Simple Invoice Demo");
    setJMenuBar(createMenuBar());
    setContentPane(createContent());
  }


  /**
   * Creates a menu bar.
   *
   * @return the menu bar.
   */
  private JMenuBar createMenuBar ()
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
  private JComponent createContent ()
  {
    final JPanel content = new JPanel(new BorderLayout());
    content.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

    final JEditorPane editorPane = new JEditorPane();
    editorPane.setEditable(false);
    editorPane.setPreferredSize(new Dimension (400, 200));
    final URL url = ObjectUtilities.getResourceRelative
            ("combined-invoice.html", CombinedAdvertisingDemo.class);
    if (url != null)
    {
      try
      {
        editorPane.setPage(url);
      }
      catch (IOException e)
      {
        Log.error("Failed to load demo description", e);
        editorPane.setText("Unable to load the demo description. Error: " + e.getMessage());
      }
    }
    else
    {
      editorPane.setText("Unable to load the demo description. No such resource.");
    }

    final JScrollPane scroll = new JScrollPane(editorPane,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    final JTable table = new JTable(this.data);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    final Enumeration enum = table.getColumnModel().getColumns();
    while (enum.hasMoreElements())
    {
      final TableColumn tc = (TableColumn) enum.nextElement();
      tc.setMinWidth(50);
    }

    final JScrollPane scrollPane = new JScrollPane
            (table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    final JButton previewButton = new ActionButton(getPreviewAction());

    final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    splitPane.setTopComponent(scroll);
    splitPane.setBottomComponent(scrollPane);
    content.add(splitPane, BorderLayout.CENTER);
    content.add(previewButton, BorderLayout.SOUTH);
    return content;

  }


  /**
   * Displays a print preview screen for the sample report.
   */
  protected void attemptPreview ()
  {
    final URL in = ObjectUtilities.getResource
            ("org/jfree/report/demo/invoice/combined/joined.xml", SimpleInvoiceDemo.class);

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
  
  private TableModel initData ()
  {
    final Customer customer =
            new Customer("Will", "Snowman", "Mr.", "12 Federal Plaza",
                    "12346", "AnOtherTown", "Lilliput");
    final InvoiceTableModel iData = initInvoiceData(customer);
    final AdvertisingTableModel aData = initAdData(customer);
    final JoiningTableModel joiningTableModel = new JoiningTableModel();
    joiningTableModel.addTableModel("invoice", iData);
    joiningTableModel.addTableModel("ad", aData);
    return joiningTableModel;
  }

  private InvoiceTableModel initInvoiceData (final Customer customer)
  {
    final GregorianCalendar gc = new GregorianCalendar(2000, 10, 23);
    final Invoice invoice = new Invoice(customer, gc.getTime(), "A-000-0123");

    final Article mainboard = new Article("MB.001", "Ancient Mainboard", 199.50f);
    final Article hardDisk = new Article
            ("HD.201", "Very Slow Harddisk", 99.50f, "No warranty");
    final Article memory = new Article("MEM.36", "Dusty RAM modules", 59.99f);
    final Article operatingSystem = new Article
            ("OS.36", "QDOS with C/PM compatibility module", 259.99f, "Serial #44638-444-123");
    invoice.addArticle(mainboard);
    invoice.addArticle(hardDisk);
    invoice.addArticle(memory);
    invoice.addArticle(memory);
    invoice.addArticle(operatingSystem);

    final InvoiceTableModel invoiceData = new InvoiceTableModel();
    invoiceData.addInvoice(invoice);
    return invoiceData;
  }

  private AdvertisingTableModel initAdData (final Customer customer)
  {
    final GregorianCalendar gc = new GregorianCalendar(2000, 10, 23);
    final Advertising ad = new Advertising(customer, gc.getTime(), "A-000-0123");

    final Article mainboard = new Article("MB.A02", "ZUSE Z0001 Mainboard", 1299.50f);
    final Article hardDisk = new Article
            ("HD.201", "Sillicium Core HDD", 99.50f,
                    "Even the babylonians used stone for long term document archiving, so why shouldn't you?");
    final Article memory = new Article("MEM.30", "ferrit core memory", 119.99f);
    final Article operatingSystem = new Article
            ("OS.36", "Windows XP", 259.99f, "Experience the world of tomorrow by spreading trojans today.");
    ad.addArticle(mainboard, 999.99d);
    ad.addArticle(hardDisk, 79.50);
    ad.addArticle(memory, 99.99f);
    ad.addArticle(operatingSystem, 199.99);

    final AdvertisingTableModel adData = new AdvertisingTableModel();
    adData.addAdvertising(ad);
    return adData;
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

    final CombinedAdvertisingDemo frame = new CombinedAdvertisingDemo();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }
}
