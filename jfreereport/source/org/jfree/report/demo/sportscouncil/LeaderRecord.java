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
 * LeaderRecord.java
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
package org.jfree.report.demo.sportscouncil;

public class LeaderRecord extends Record
{
  private String firstName;
  private String lastName;
  private String position;
  private String leadershipPhoneNumber;
  private String email;

  public LeaderRecord (final String orgID,
                       final String firstName, final String lastName,
                       final String position, final String leadershipPhoneNumber,
                       final String email)
  {
    super("leader", orgID);
    this.firstName = firstName;
    this.lastName = lastName;
    this.position = position;
    this.leadershipPhoneNumber = leadershipPhoneNumber;
    this.email = email;
  }

  public String getEmail ()
  {
    return email;
  }

  public String getFirstName ()
  {
    return firstName;
  }

  public String getLastName ()
  {
    return lastName;
  }

  public String getLeadershipPhoneNumber ()
  {
    return leadershipPhoneNumber;
  }

  public String getPosition ()
  {
    return position;
  }
}
