/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ------------------------------
 * LineBreakIteratorTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LineBreakIteratorTest.java,v 1.1 2003/06/20 12:05:13 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 16.06.2003 : Initial version
 *  
 */

package com.jrefinery.report.ext.junit.base.basic.util;

import com.jrefinery.report.util.LineBreakIterator;
import junit.framework.TestCase;

public class LineBreakIteratorTest extends TestCase
{
  public LineBreakIteratorTest()
  {
  }

  public LineBreakIteratorTest(final String s)
  {
    super(s);
  }

  public void testLineBreaking ()
  {
    final String[] tests = {"The lazy \n fox \r\n jumps \nover the funny tree\n",
                      "FirstName AVerLongLastName", "Test\n\n\n\n\n\ntest\n"};

    final String[][] results = {
      { "The lazy "," fox "," jumps ","over the funny tree",""},
      { "FirstName AVerLongLastName"},
      { "Test","","","","","","test",""}
    };
    for (int j = 0; j < tests.length; j++)
    {
      final String text = tests[j];

      final LineBreakIterator iterator = new LineBreakIterator(text);
      int count = 0;
      while (iterator.hasNext())
      {
        assertEquals(results[j][count], iterator.next());
        count++;
      }
    }
  }
}

