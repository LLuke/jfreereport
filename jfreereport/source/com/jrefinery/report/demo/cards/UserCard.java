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
 * UserCard.java
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

import java.util.Date;

public class UserCard extends PersonBoundCard
{
  private String login;
  private String password;
  private Date expires;

  public UserCard(String firstName, String lastName, String cardNr, String login, String password, Date expires)
  {
    super(firstName, lastName, cardNr);
    if (login == null) throw new NullPointerException();
    if (password == null) throw new NullPointerException();
    if (expires == null) throw new NullPointerException();

    this.login = login;
    this.password = password;
    this.expires = expires;
  }

  public String getLogin()
  {
    return login;
  }

  public String getPassword()
  {
    return password;
  }

  public Date getExpires()
  {
    return expires;
  }

  public CardType getType()
  {
    return CardType.User;
  }
}
