/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * TextContentTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TextContentTest.java,v 1.2 2003/07/23 16:06:24 taqua Exp $
 *
 * Changes
 * -------------------------
 * 10.06.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.basic.content;

import java.awt.geom.Rectangle2D;

import junit.framework.TestCase;
import org.jfree.report.TextElement;
import org.jfree.report.content.Content;
import org.jfree.report.content.DefaultContentFactory;
import org.jfree.report.content.TextContent;
import org.jfree.report.content.TextContentFactoryModule;
import org.jfree.report.content.TextLine;
import org.jfree.report.content.TextParagraph;
import org.jfree.report.filter.StaticDataSource;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.layout.SizeCalculator;
import org.jfree.report.util.ElementLayoutInformation;

public class TextContentTest extends TestCase
{
  private static class DebugSizeCalculator implements SizeCalculator
  {
    private float lineHeight;
    private float charWidth;

    public DebugSizeCalculator(final float lineHeight, final float charWidth)
    {
      this.lineHeight = lineHeight;
      this.charWidth = charWidth;
    }

    /**
     * Calculates the width of a <code>String<code> in the current <code>Graphics</code> context.
     *
     * @param text the text.
     * @param lineStartPos the start position of the substring to be measured.
     * @param endPos the position of the last character to be measured.
     *
     * @return the width of the string in Java2D units.
     */
    public float getStringWidth(final String text, final int lineStartPos, final int endPos)
    {
      return (endPos - lineStartPos) * charWidth;
    }

    /**
     * Returns the line height.  This includes the font's ascent, descent and leading.
     *
     * @return the line height.
     */
    public float getLineHeight()
    {
      return lineHeight;
    }
  }

  public TextContentTest()
  {
  }

  public TextContentTest(final String s)
  {
    super(s);
  }

  public void testNullContent() throws Exception
  {
    final TextElement se = new TextElement();
    se.setNullString("");
    final DefaultContentFactory df = new DefaultContentFactory();
    df.addModule(new TextContentFactoryModule());
    assertTrue(df.canHandleContent(se.getContentType()));
    ElementLayoutInformation eli = new ElementLayoutInformation(new Rectangle2D.Float(0, 0, 10, 10));
    assertNull(df.createContentForElement(se, eli, new DefaultLayoutSupport()));

    eli = new ElementLayoutInformation(new Rectangle2D.Float(0, 0, 0, 0));
    assertNull(df.createContentForElement(se, eli, new DefaultLayoutSupport()));
  }

  public void testInvisibleContent() throws Exception
  {
    final TextElement se = new TextElement();
    se.setDataSource(new StaticDataSource("Hello World :)"));
    final DefaultContentFactory df = new DefaultContentFactory();
    df.addModule(new TextContentFactoryModule());
    assertTrue(df.canHandleContent(se.getContentType()));
    ElementLayoutInformation eli = new ElementLayoutInformation(new Rectangle2D.Float(0, 0, 10, 10));
    assertNotNull(df.createContentForElement(se, eli, new DefaultLayoutSupport()));

    eli = new ElementLayoutInformation(new Rectangle2D.Float(0, 0, 0, 0));
    assertNull(df.createContentForElement(se, eli, new DefaultLayoutSupport()));
  }

  public void testLeadingWhiteSpaces () throws Exception
  {
    final String textLeadWhite = "  abc\n  abc";
    final TextContent content = new TextContent
        (textLeadWhite, 10, new Rectangle2D.Float (0,0, 100, 10),
            new DebugSizeCalculator(10, 10));
    assertEquals(new Rectangle2D.Float(0,0, 50, 10), content.getMinimumContentSize());

    final TextParagraph tp = (TextParagraph) content.getContentPart(0);
    final TextLine tl = (TextLine) tp.getContentPart(0);
    assertEquals(new Rectangle2D.Float(0,0, 50, 10), tl.getMinimumContentSize());
    assertEquals(tl.getContent(), "  abc");
  }

  public static void test1a()
  {
    final String content = "123 thousand people";
    final SizeCalculator ds = new DebugSizeCalculator(10, 10);
    final TextParagraph tp = new TextParagraph(ds, 10);
    tp.setContent(content, 0, 0, 100, 10);

    final TextLine tl = (TextLine) tp.getContentPart(0);
    assertEquals("123 thou..", tl.getContent());
    assertEquals(new Rectangle2D.Float(0,0, 100, 10), tl.getBounds());
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

    final TextParagraph tp = new TextParagraph(new DebugSizeCalculator(10, 10), 10);
    tp.setContent(content, 0, 0, 345, 5000);
    assertEquals(new Rectangle2D.Float(0,0, 330, 100), tp.getMinimumContentSize());

    for (int i = 0; i < tp.getContentPartCount(); i++)
    {
      final TextLine tl = (TextLine) tp.getContentPart(i);
      assertTrue(tl.getBounds().getWidth() < 345);
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
        (content, 10, new Rectangle2D.Float(0, 0, 500, 5000), new DebugSizeCalculator(10, 10));
    tc = tc.getContentForBounds(tc.getMinimumContentSize());
    final Content tp = tc.getContentPart(0);
    for (int i = 0; i < tp.getContentPartCount(); i++)
    {
      final TextLine tl = (TextLine) tp.getContentPart(i);
      assertTrue(tl.getBounds().getWidth() <= 500);
      assertEquals(results[i], tl.getContent());
    }

    assertEquals(tc.getContentForBounds(tc.getMinimumContentSize()).getBounds(),
        tc.getMinimumContentSize());
  }


  public void testLineBreaking2()
  {
    final String content = "Thisisareallylongword, noone thought thatawordcanbethatlong, itwontfitonaline, but these words do, so heres the test!";
    Content tc = new TextContent(content, 10, new Rectangle2D.Float(0, 0, 200, 5000),
        new DebugSizeCalculator(10, 10));

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
    final Content tp = tc.getContentPart(0);
    for (int i = 0; i < tp.getContentPartCount(); i++)
    {
      final TextLine tl = (TextLine) tp.getContentPart(i);
      assertTrue(tl.getBounds().getWidth() <= 500);
      assertEquals(results[i], tl.getContent());
    }
    assertEquals(new Rectangle2D.Float(0,0, 200, 70), tp.getMinimumContentSize());
  }

}
