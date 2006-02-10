/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * TestShapeAndDrawableLayout.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TestShapeAndDrawableLayout.java,v 1.5 2005/09/07 11:24:08 taqua Exp $
 *
 * Changes
 * -------
 * 30.03.2003 : Initial version
 */
package org.jfree.report.ext.junit;

import javax.swing.table.DefaultTableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.layout.BandLayoutManagerUtil;
import org.jfree.report.layout.DefaultLayoutSupport;

public class TestShapeAndDrawableLayout
{
  public static void main(final String[] args)
      throws Exception
  {
    final JFreeReport report = TestSystem.loadReport("org/jfree/report/demo/layouts/shape-and-drawable.xml",
        new DefaultTableModel());
    if (report == null)
    {
      System.exit(1);
    }

    BandLayoutManagerUtil.doLayout(report.getReportHeader(),
        new DefaultLayoutSupport(false), 550, 200);

  }
}
