/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * TextOperationsTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TextOperationsTest.java,v 1.2 2003/11/01 19:57:03 taqua Exp $
 *
 * Changes
 * -------------------------
 * 12.09.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.basic.modules.output;

import java.awt.geom.Rectangle2D;

import junit.framework.TestCase;
import org.jfree.report.content.Content;
import org.jfree.report.content.TextContent;
import org.jfree.report.content.TextLine;
import org.jfree.report.content.TextParagraph;
import org.jfree.report.ext.junit.base.basic.content.TextContentTest;

// todo Alignment is not working ..
public class TextOperationsTest extends TestCase
{
  public TextOperationsTest(String s)
  {
    super(s);
  }

  public static void testLineBreaking1()
  {
    final String content = "This    report   lists   a    number   of    " +
        "Open    Source    Java    projects   that    you           " +
        "            might   find   useful.    The   report  has    " +
        "been    generated   using   JFreeReport.    For   an  up-to-date" +
        "    list,    please   visit    the   following   web   page:";

    final String[] results = {
      "This    report   lists   a    ",
      "number   of    Open    Source    ",
      "Java    projects   that    you",
      "                       might   ",
      "find   useful.    The   report  ",
      "has    been    generated   using",
      "   JFreeReport.    For   an  ",
      "up-to-date    list,    please   ",
      "visit    the   following   web   ",
      "page:"
    };

    final TextParagraph tp = new TextParagraph
        (new TextContentTest.DebugSizeCalculator(10, 10), 10, "..", false);
    tp.setContent(content, 0, 0, 345, 5000);
    assertEquals(new Rectangle2D.Float(0,0, 330, 100), tp.getMinimumContentSize());

    for (int i = 0; i < tp.getContentPartCount(); i++)
    {
      final TextLine tl = (TextLine) tp.getContentPart(i);
      assertTrue(tl.getBounds().getWidth() < 345);
      assertTrue(tl.getBounds().getY() == (i * 10));
      assertTrue(tl.getBounds().getHeight() == (10));
      assertEquals(results[i], tl.getContent());
    }

  }

  public static void testLineBreaking3() throws Exception
  {

    final String content = "This is a test. It will check to see if word wrapping " +
        "is working correctly or not. If it does not work some of the words " +
        "will be broken in the middle and some of them will be missing all together.";

    final String[] results = {
      "This is a test. It will check to see if word ",
      "wrapping is working correctly or not. If it does ",
      "not work some of the words will be broken in the ",
      "middle and some of them will be missing all ",
      "together."
    };

    Content tc = new TextContent
        (content, 10, new Rectangle2D.Float(0, 0, 500, 5000),
            new TextContentTest.DebugSizeCalculator(10, 10), "..", false);
    tc = tc.getContentForBounds(tc.getMinimumContentSize());

  }


  public void testLineBreaking2()
  {
    final String content =
      "Thisisareallylongword, noone thought thatawordcanbethatlong, " +
      "itwontfitonaline, but these words do, so heres the test!";
    Content tc = new TextContent(content, 10, new Rectangle2D.Float(0, 0, 200, 5000),
        new TextContentTest.DebugSizeCalculator(10, 10),"..", false);

    final String[] results = {
      "Thisisareallylongwor",
      "d, noone thought ",
      "thatawordcanbethatlo",
      "ng, ",
      "itwontfitonaline, ",
      "but these words do, ",
      "so heres the test!"
    };
    tc = tc.getContentForBounds(tc.getMinimumContentSize());
  }

  public void testAlignment()
  {
    final int ESHIFT = 3;
    final String content = "This is a test";

    // Specifying an y for the content is a trap for the evil programmer
    // the root content will always ignore the x and y values and set this to
    // 0. If this y influences the resulting operations, we discovered a
    // Bug (getBounds() instead of getMinimumBounds() used)
    Content tc = new TextContent(content, 10, new Rectangle2D.Float(0, 110, 200, 5000),
        new TextContentTest.DebugSizeCalculator(10, 10),"..", false);

  }
}
