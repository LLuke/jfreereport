/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * PersonBoundCard.java
 * --------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PersonBoundCard.java,v 1.4 2003/06/29 16:59:23 taqua Exp $
 *
 * Changes
 * -------
 * 29.03.2003 : Initial version
 */
package org.jfree.report.demo.cards;

/**
 * A card that is bound to a person's identity.
 *
 * @author Thomas Morgner.
 */
public abstract class PersonBoundCard extends Card
{
  /** The person's first name. */
  private String firstName;

  /** The person's last name. */
  private String lastName;

  /** The card number. */
  private String cardNr;

  /**
   * Creates a new card.
   *
   * @param firstName  the first name.
   * @param lastName  the last name.
   * @param cardNr  the card number.
   */
  public PersonBoundCard(final String firstName, final String lastName, final String cardNr)
  {
    if (firstName == null)
    {
      throw new NullPointerException("FirstName");
    }
    if (lastName == null)
    {
      throw new NullPointerException("LastName");
    }
    if (cardNr == null)
    {
      throw new NullPointerException("CardNr");
    }

    this.firstName = firstName;
    this.lastName = lastName;
    this.cardNr = cardNr;
  }

  /**
   * Returns the first name.
   *
   * @return The first name.
   */
  public String getFirstName()
  {
    return firstName;
  }

  /**
   * Returns the last name.
   *
   * @return The last name.
   */
  public String getLastName()
  {
    return lastName;
  }

  /**
   * Returns the card number.
   *
   * @return The card number.
   */
  public String getCardNr()
  {
    return cardNr;
  }

}
