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
 * Invoice.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Invoice.java,v 1.1.2.1 2004/03/26 21:57:54 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 26.03.2004 : Initial version
 *  
 */

package org.jfree.report.demo.invoice;

import java.util.ArrayList;
import java.util.Date;

public class Invoice
{
  private ArrayList articles;
  private ArrayList articleCounts;

  private Customer customer;
  private Date date;
  private String invoiceNumber;

  public Invoice (final Customer customer, final Date date,
                  final String invoiceNumber)
  {
    this.customer = customer;
    this.date = date;
    this.invoiceNumber = invoiceNumber;
    this.articles = new ArrayList();
    this.articleCounts = new ArrayList();
  }

  public synchronized void addArticle (final Article article)
  {
    final int index = articles.indexOf(article);
    if (index == -1)
    {
      articles.add(article);
      articleCounts.add (new Integer (1));
    }
    else
    {
      final Integer oldCount = (Integer) articleCounts.get (index);
      articleCounts.set (index, new Integer(oldCount.intValue() + 1));
    }
  }

  public synchronized void removeArticle (final Article article)
  {
    final int index = articles.indexOf(article);
    if (index != -1)
    {
      final Integer oldCount = (Integer) articleCounts.get (index);
      if (oldCount.intValue() == 1)
      {
        articleCounts.remove(index);
        articles.remove(index);
      }
      else
      {
        articleCounts.set (index, new Integer(oldCount.intValue() - 1));
      }
    }
  }

  public Article getArticle (final int index)
  {
    return (Article) articles.get(index);
  }

  public int getArticleCount (final int index)
  {
    final Integer i = (Integer) articleCounts.get(index);
    return i.intValue();
  }

  public int getArticleCount ()
  {
    return articles.size();
  }

  public Customer getCustomer ()
  {
    return customer;
  }

  public Date getDate ()
  {
    return date;
  }

  public String getInvoiceNumber ()
  {
    return invoiceNumber;
  }
}
