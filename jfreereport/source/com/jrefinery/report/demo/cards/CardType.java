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
 * ----------------
 * ${NAME}.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes
 * -------
 * 29.03.2003 : Initial version
 */
package com.jrefinery.report.demo.cards;

public class CardType
{
  public static final CardType Account = new CardType("Account");
  public static final CardType Admin = new CardType("Admin");
  public static final CardType User = new CardType("User");
  public static final CardType Prepaid = new CardType("Prepaid");
  public static final CardType Free = new CardType("Free");
  public static final CardType Empty = new CardType("Empty");

  private final String myName; 

  private CardType(String name)
  {
    myName = name;
  }

  public String getTypeName()
  {
    return myName;
  }

  public String toString()
  {
    return myName;
  }
}
