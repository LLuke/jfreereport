/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * InvoiceTableModel.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: InvoiceTableModel.java,v 1.1.2.2 2004/04/05 16:49:34 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 26.03.2004 : Initial version
 *  
 */

package org.jfree.report.demo.invoice;

import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.AbstractTableModel;

public class InvoiceTableModel extends AbstractTableModel
{
  private static Class[] COLUMN_TYPES =
    {
      Invoice.class,
      String.class, String.class, String.class, 
      String.class, String.class, String.class,
      String.class, Date.class, String.class,
      String.class, String.class, String.class,
      Float.class, Integer.class
    };
  
  private static String[] COLUMN_NAMES =
    {
      "invoice",
      "customer.firstName", "customer.lastName", "customer.street",
      "customer.town", "customer.postalCode", "customer.country",
      "customer.salutation", "invoice.date", "invoice.number",
      "article.name", "article.number", "article.details",
      "article.price", "article.count"
    };

  private transient Invoice[] invoicePerRow;
  private transient Article []articlesPerRow;

  private ArrayList invoices;
  private int totalSize;

  public InvoiceTableModel ()
  {
    invoices = new ArrayList();
  }

  public void addInvoice (final Invoice invoice)
  {
    invoices.add (invoice);
    invalidateCaches();
    fireTableDataChanged();
  }

  public void removeInvoice (final Invoice invoice)
  {
    invoices.remove (invoice);
    invalidateCaches();
    fireTableDataChanged();
  }

  public Invoice getInvoice (final int invoice)
  {
    return (Invoice) invoices.get (invoice);
  }

  public void invalidateCaches()
  {
    int size = 0;
    for (int i = 0; i < invoices.size(); i++)
    {
      final Invoice inv = getInvoice(i);
      size += inv.getArticleCount();
    }
    this.totalSize = size;
    this.invoicePerRow = null;
    this.articlesPerRow = null;
  }

  /**
   * Returns the number of columns in the model. A <code>JTable</code> uses this method to
   * determine how many columns it should create and display by default.
   *
   * @return the number of columns in the model
   *
   * @see #getRowCount
   */
  public int getColumnCount ()
  {
    return COLUMN_NAMES.length;
  }

  /**
   * Returns the number of rows in the model. A <code>JTable</code> uses this method to
   * determine how many rows it should display.  This method should be quick, as it is
   * called frequently during rendering.
   *
   * @return the number of rows in the model
   *
   * @see #getColumnCount
   */
  public int getRowCount ()
  {
    return totalSize;
  }

  /**
   * Returns a default name for the column using spreadsheet conventions: A, B, C, ... Z,
   * AA, AB, etc.  If <code>column</code> cannot be found, returns an empty string.
   *
   * @param column the column being queried
   * @return a string containing the default name of <code>column</code>
   */
  public String getColumnName (final int column)
  {
    return COLUMN_NAMES[column];
  }

  /**
   * Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
   *
   * @param columnIndex the column being queried
   * @return the Object.class
   */
  public Class getColumnClass (final int columnIndex)
  {
    return COLUMN_TYPES[columnIndex];
  }

  private void fillCache()
  {
    if (invoicePerRow != null && articlesPerRow != null)
    {
      // nothing to do...
      return;
    }
    // ensure that we have enough space ...
    this.invoicePerRow = new Invoice[totalSize];
    this.articlesPerRow = new Article[totalSize];


    int currentRow = 0;
    final int invoiceSize = invoices.size();
    for (int i = 0; i < invoiceSize; i++)
    {
      final Invoice inv = (Invoice) invoices.get (i);
      final int articleCount = inv.getArticleCount();
      for (int ac = 0; ac < articleCount; ac++)
      {
        invoicePerRow[currentRow] = inv;
        articlesPerRow[currentRow] = inv.getArticle(ac);
        currentRow += 1;
      }
    }
  }

  /**
   * Returns the value for the cell at <code>columnIndex</code> and
   * <code>rowIndex</code>.
   *
   * @param	rowIndex	the row whose value is to be queried
   * @param	columnIndex the column whose value is to be queried
   * @return	the value Object at the specified cell
   */
  public Object getValueAt (final int rowIndex, final int columnIndex)
  {
    // just make sure we can access the invoices by the array
    fillCache();
    final Invoice inv = invoicePerRow[rowIndex];
    final Article art = articlesPerRow[rowIndex];

    switch (columnIndex)
    {
      case 0: return inv;
      case 1: return inv.getCustomer().getFirstName();
      case 2: return inv.getCustomer().getLastName();
      case 3: return inv.getCustomer().getStreet();
      case 4: return inv.getCustomer().getTown();
      case 5: return inv.getCustomer().getPostalCode();
      case 6: return inv.getCustomer().getCountry();
      case 7: return inv.getCustomer().getSalutation();
      case 8: return inv.getDate();
      case 9: return inv.getInvoiceNumber();
      case 10: return art.getName();
      case 11: return art.getArticleNumber();
      case 12: return art.getArticleDetails();
      case 13: return new Float(art.getPrice());
      case 14: return new Integer(inv.getArticleCount());
    }
    throw new IndexOutOfBoundsException("ColumnIndex");
  }
}
