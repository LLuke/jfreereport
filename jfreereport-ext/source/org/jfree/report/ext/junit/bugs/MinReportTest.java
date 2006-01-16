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
 * APReportTest.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: MissingColumnsReportTest.java,v 1.1 2005/10/27 18:37:31 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.report.ext.junit.bugs;

import java.net.URL;
import java.util.Date;

import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ext.junit.TestSystem;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.util.ObjectUtilities;

public class MinReportTest
{
  private static final String URLNAME = "org/jfree/report/ext/junit/bugs/resource/minimum.xml";

  private MinReportTest ()
  {
  }

  public static void main(final String[] args) throws Exception
  {
    // Capactiy, Cost
    // Group, Location, Type, Container
    // Date Acquired

    Object[][] data = new Object[][]{
            {new Integer (10), new Integer (11),
            "Group A1", "Location", "Type1", "Container",
            new Date()},
            {new Integer (10), new Integer (12),
            "Group A2", "Location", "Type2", "Container",
            new Date()},
            {new Integer (10), new Integer (13),
            "Group A3", "Location", "Type3", "Container",
            new Date()},
            {new Integer (10), new Integer (14),
            "Group A4", "Location", "Type4", "Container",
            new Date()},
            {new Integer (10), new Integer (15),
            "Group A5", "Location", "Type5", "Container",
            new Date()},
            {new Integer (10), new Integer (16),
            "Group A6", "Location6", "Type6", "Container",
            new Date()},

    };

    Object[] names = new Object[]{
            "Capacity", "Cost", "Group", "Location", "Type", "Container", "Date Acquired"
    };

    JFreeReportBoot.getInstance().start();
    final TableModel dataModel = new DefaultTableModel(data, names);

    final URL in = ObjectUtilities.getResource
            (URLNAME, MinReportTest.class);
    final JFreeReport report = ReportGenerator.getInstance().parseReport(in);
    report.setData(dataModel);
    TestSystem.showPreview(report);
    System.exit(0);
  }
}
