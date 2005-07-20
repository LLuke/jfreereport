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
 * Advertising.java
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
package org.jfree.report.demo.invoice.model;

import java.util.ArrayList;
import java.util.Date;

import org.jfree.report.demo.invoice.model.Customer;
import org.jfree.report.demo.invoice.model.Article;

public class Advertising
{
  private Customer customer;
  private ArrayList articles;
  private ArrayList articleReducedPrices;

  private Date date;
  private String adNumber;

  public Advertising (final Customer customer, final Date date,
                    final String adNumber)
  {
    this.customer = customer;
    this.date = date;
    this.adNumber = adNumber;
    this.articles = new ArrayList();
    this.articleReducedPrices = new ArrayList();
  }

  public synchronized void addArticle (final Article article, final double reduced)
  {
    final int index = articles.indexOf(article);
    if (index == -1)
    {
      articles.add(article);
      articleReducedPrices.add(new Double (reduced));
    }
  }

  public synchronized void removeArticle (final Article article)
  {
    final int index = articles.indexOf(article);
    if (index != -1)
    {
      articleReducedPrices.remove(index);
      articles.remove(index);
    }
  }

  public Article getArticle (final int index)
  {
    return (Article) articles.get(index);
  }

  public double getArticleReducedPrice (final int index)
  {
    final Double i = (Double) articleReducedPrices.get(index);
    return i.doubleValue();
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

  public String getAdNumber ()
  {
    return adNumber;
  }
}
