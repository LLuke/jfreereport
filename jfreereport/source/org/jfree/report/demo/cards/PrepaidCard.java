/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * PrepaidCard.java
 * ----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: PrepaidCard.java,v 1.2 2003/08/24 15:13:21 taqua Exp $
 *
 * Changes
 * -------
 * 29.03.2003 : Initial version
 */
package org.jfree.report.demo.cards;

/**
 * A 'prepaid' card.
 *
 * @author Thomas Morgner.
 */
public class PrepaidCard extends PersonBoundCard
{
  /**
   * Creates a new 'prepaid' card.
   *
   * @param firstName  the first name.
   * @param lastName  the last name.
   * @param cardNr  the card number.
   */
  public PrepaidCard(final String firstName, final String lastName, final String cardNr)
  {
    super(firstName, lastName, cardNr);
  }

  /**
   * Returns the card type.
   *
   * @return The card type.
   */
  public CardType getType()
  {
    return CardType.PREPAID;
  }
}
