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
 * LineBreakTest.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LineBreakTest.java,v 1.1 2003/04/11 19:32:37 taqua Exp $
 *
 * Changes
 * -------
 * 11.04.2003 : Initial version
 */
package com.jrefinery.report.ext.junit.bugs;

import java.util.List;
import java.util.ArrayList;

import com.jrefinery.report.util.LineBreakIterator;

public class LineBreakTest
{
/*
  public static void main (String [] args)
    throws Exception
  {

    JFreeReport report = TestSystem.loadReport("/com/jrefinery/report/demo/OpenSourceDemo.xml", new SampleData5());
    if (report == null)
      System.exit (1);

    BandLayoutManagerUtil.doLayout(report.getReportHeader(), new DefaultLayoutSupport(),
                                   451, 500);
  }
*/

  public static void main(String[] args)
  {
    String[] tests = {"The lazy \n fox \r\n jumps \nover the funny tree\n",
    "FirstName AVerLongLastName", "Test\n\n\n\n\n\ntest\n"};

    for (int j = 0; j < tests.length; j++)
    {
      String text = tests[j];
      List lines = new ArrayList();

      LineBreakIterator iterator = new LineBreakIterator(text);

      while (iterator.hasNext())
      {
        lines.add(iterator.next());
      }
      System.out.println(lines);
    }
  }
}
