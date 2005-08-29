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
 * CombinedAdvertisingDemoHandler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: CombinedAdvertisingDemoHandler.java,v 1.4 2005/08/08 15:36:28 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.demo.invoice.combined;

import java.net.URL;
import java.util.GregorianCalendar;
import javax.swing.JComponent;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.demo.helper.AbstractXmlDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.demo.invoice.AdvertisingTableModel;
import org.jfree.report.demo.invoice.InvoiceTableModel;
import org.jfree.report.demo.invoice.model.Advertising;
import org.jfree.report.demo.invoice.model.Article;
import org.jfree.report.demo.invoice.model.Customer;
import org.jfree.report.demo.invoice.model.Invoice;
import org.jfree.report.modules.misc.tablemodel.JoiningTableModel;
import org.jfree.util.ObjectUtilities;

public class CombinedAdvertisingDemoHandler extends AbstractXmlDemoHandler
{
  private TableModel data;

  public CombinedAdvertisingDemoHandler ()
  {
    data = initData();
  }

  public String getDemoName()
  {
    return "Combined Advertising Demo";
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("combined-invoice.html", CombinedAdvertisingDemoHandler.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("joined.xml", CombinedAdvertisingDemoHandler.class);
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

  public JFreeReport createReport() throws ReportDefinitionException
  {
    final JFreeReport report = parseReport();
    report.setData(data);
    return report;
  }

}
