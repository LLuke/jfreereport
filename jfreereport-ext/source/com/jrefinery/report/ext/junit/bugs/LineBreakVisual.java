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
 * LineBreakVisual.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LineBreakVisual.java,v 1.2 2003/06/10 18:17:28 taqua Exp $
 *
 * Changes
 * -------
 * 23.04.2003 : Initial version
 */
package com.jrefinery.report.ext.junit.bugs;

import javax.swing.table.DefaultTableModel;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ext.junit.TestSystem;

public class LineBreakVisual
{
  public static void main(final String[] args)
      throws Exception
  {
    final JFreeReport report = TestSystem.loadReport("/com/jrefinery/report/ext/junit/bugs/resource/LineBreak.xml", new DefaultTableModel());
    if (report == null)
      System.exit(1);

    TestSystem.showPreviewFrameWExit(report, true);
  }
}
