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
 * CouncilRecord.java
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


public class CouncilRecord extends Record
{
  private String orgName;
  private String internalWebsite;
  private String orgEmail;
  private String street1;
  private String street2;
  private String city;
  private String state;
  private String zip;
  private String phoneNumber;
  private String extension;
  private String faxNumber;
  private int yearEventCount;
  private int thisMonthEventCount;
  private int lastMonthEventCount;
  private int futureEventCount;

  public CouncilRecord (final String orgID,
                        final String orgName, final String internalWebsite,
                        final String orgEmail, final String street1,
                        final String street2, final String city,
                        final String state, final String zip,
                        final String phoneNumber, final String extension,
                        final String faxNumber, final int yearEventCount,
                        final int thisMonthEventCount,
                        final int lastMonthEventCount,
                        final int futureEventCount)
  {
    super("council", orgID);
    this.orgName = orgName;
    this.internalWebsite = internalWebsite;
    this.orgEmail = orgEmail;
    this.street1 = street1;
    this.street2 = street2;
    this.city = city;
    this.state = state;
    this.zip = zip;
    this.phoneNumber = phoneNumber;
    this.extension = extension;
    this.faxNumber = faxNumber;
    this.yearEventCount = yearEventCount;
    this.thisMonthEventCount = thisMonthEventCount;
    this.lastMonthEventCount = lastMonthEventCount;
    this.futureEventCount = futureEventCount;
  }

  public String getCity ()
  {
    return city;
  }

  public String getExtension ()
  {
    return extension;
  }

  public String getFaxNumber ()
  {
    return faxNumber;
  }

  public int getFutureEventCount ()
  {
    return futureEventCount;
  }

  public String getInternalWebsite ()
  {
    return internalWebsite;
  }

  public int getLastMonthEventCount ()
  {
    return lastMonthEventCount;
  }

  public String getOrgEmail ()
  {
    return orgEmail;
  }

  public String getOrgName ()
  {
    return orgName;
  }

  public String getPhoneNumber ()
  {
    return phoneNumber;
  }

  public String getState ()
  {
    return state;
  }

  public String getStreet1 ()
  {
    return street1;
  }

  public String getStreet2 ()
  {
    return street2;
  }

  public int getThisMonthEventCount ()
  {
    return thisMonthEventCount;
  }

  public int getYearEventCount ()
  {
    return yearEventCount;
  }

  public String getZip ()
  {
    return zip;
  }
}
