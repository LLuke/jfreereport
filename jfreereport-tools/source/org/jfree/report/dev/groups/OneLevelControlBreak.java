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
 * OneLevelControlBreak.java
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
package org.jfree.report.dev.groups;

public class OneLevelControlBreak
{
  public static final int COUNTRY_COL = 0;
  public static final int COUNTRY_ID_COL = 1;
  public static final int CONTINENT_COL = 2;
  public static final int POPULATION_COL = 3;

  public OneLevelControlBreak ()
  {
  }

  public static Object[][] initData ()
  {
    final Object[][] data = new Object[23][4];
    data[0] = new Object[]{"Morocco", "MA", "Africa", new Integer(29114497)};
    data[1] = new Object[]{"South Africa", "ZA", "Africa", new Integer(40583573)};
    data[2] = new Object[]{"China", "CN", "Asia", new Integer(1254400000)};
    data[3] = new Object[]{"Iran", "IR", "Asia", new Integer(66000000)};
    data[4] = new Object[]{"Iraq", "IQ", "Asia", new Integer(19700000)};
    data[5] = new Object[]{"Australia", "AU", "Australia", new Integer(18751000)};
    data[6] = new Object[]{"Austria", "AT", "Europe", new Integer(8015000)};
    data[7] = new Object[]{"Belgium", "BE", "Europe", new Integer(10213752)};
    data[8] = new Object[]{"Estonia", "EE", "Europe", new Integer(1445580)};
    data[9] = new Object[]{"Finland", "FI", "Europe", new Integer(5171000)};
    data[10] = new Object[]{"France", "FR", "Europe", new Integer(60186184)};
    data[11] = new Object[]{"Germany", "DE", "Europe", new Integer(82037000)};
    data[12] = new Object[]{"Hungary", "HU", "Europe", new Integer(10044000)};
    data[13] = new Object[]{"Italy", "IT", "Europe", new Integer(57612615)};
    data[14] = new Object[]{"Norway", "NO", "Europe", new Integer(4445460)};
    data[15] = new Object[]{"Poland", "PL", "Europe", new Integer(38608929)};
    data[16] = new Object[]{"Portugal", "PT", "Europe", new Integer(9918040)};
    data[17] = new Object[]{"Spain", "ES", "Europe", new Integer(39669394)};
    data[18] = new Object[]{"Sweden", "SE", "Europe", new Integer(8854322)};
    data[19] = new Object[]{"Switzerland", "CH", "Europe", new Integer(7123500)};
    data[20] = new Object[]{"Canada", "CA", "North America", new Integer(30491300)};
    data[21] = new Object[]{"United States of America", "US", "North America", new Integer(273866000)};
    data[22] = new Object[]{"Brazil", "BR", "South America", new Integer(165715400)};
    return data;
  }

  private static void printReportHeader (final Object[][] data,
                                         final int position)
  {
    System.out.println ("                ... STARTING REPORTING ...");
    System.out.println ("-------------------------------------------------------");
  }

  private static void printReportFooter (final Object[][] data,
                                         final int position)
  {
    System.out.println ("-------------------------------------------------------");
    System.out.println ("                ... REPORTING DONE ...");
  }

  private static void printGroupFooter (final Object[][] data,
                                        final int position)
  {
    System.out.println ("-------------------------------------------------------");
    System.out.print ("Finished: ------------- " + data[position][CONTINENT_COL]);
    System.out.println ();
  }

  private static void printGroupHeader (final Object[][] data,
                                        final int position)
  {
    System.out.println ();
    System.out.println ();
    System.out.print (data[position][CONTINENT_COL]);
    System.out.println ();
    System.out.println ("-------------------------------------------------------");
  }

  private static void printItems (final Object[][] data,
                                  final int position)
  {
    System.out.print (data[position][COUNTRY_ID_COL]);
    System.out.print (" ");
    System.out.print (data[position][COUNTRY_COL]);
    System.out.print (", ");
    System.out.print (data[position][POPULATION_COL]);
    System.out.println();
  }

  private static boolean isEndOfFile (final Object[][] data, final int position)
  {
    return position == data.length;
  }
}
