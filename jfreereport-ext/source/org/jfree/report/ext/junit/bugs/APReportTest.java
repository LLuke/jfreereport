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
 * $Id: APReportTest.java,v 1.1 2005/03/24 23:14:10 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.report.ext.junit.bugs;

import java.net.URL;

import org.jfree.report.JFreeReport;
import org.jfree.report.ext.junit.TestSystem;
import org.jfree.report.modules.parser.base.ReportGenerator;

public class APReportTest
{
  private static final String URLNAME = "/org/jfree/report/ext/junit/bugs/resource/ki.xml";

  public APReportTest()
  {
  }

  public static void main(final String[] args) throws Exception
  {
    final URL in = APReportTest.class.getResource(URLNAME);
    final JFreeReport report = ReportGenerator.getInstance().parseReport(in);
    TestSystem.showPreview(report);
    System.exit(0);
  }
}
