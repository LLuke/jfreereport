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
 * Article.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Article.java,v 1.1.2.1 2004/03/26 21:57:54 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 26.03.2004 : Initial version
 *  
 */

package org.jfree.report.demo.invoice;

public class Article
{
  // how is the article called?
  private String name;
  // how much does it cost
  private float price;
  // the article number
  private String articleNumber;
  // may contain a serial number or special notes
  private String articleDetails;

  public Article (final String articleNumber, final String name, final float price)
  {
    this (articleNumber, name, price, null);
  }

  public Article (final String articleNumber, final String name,
                  final float price, final String articleDetails)
  {
    this.articleNumber = articleNumber;
    this.name = name;
    this.price = price;
    this.articleDetails = articleDetails;
  }

  public String getArticleDetails ()
  {
    return articleDetails;
  }

  public String getArticleNumber ()
  {
    return articleNumber;
  }

  public String getName ()
  {
    return name;
  }

  public float getPrice ()
  {
    return price;
  }
}
