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
 * $Id: TextContentTest.java,v 1.9 2005/10/02 19:47:57 taqua Exp $
 *
 * Changes
 * -------------------------
 * 10.06.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.basic.content;

import junit.framework.TestCase;
import org.jfree.report.TextElement;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.content.ContentContainer;
import org.jfree.report.content.DefaultContentFactory;
import org.jfree.report.content.EmptyContent;
import org.jfree.report.content.TextContent;
import org.jfree.report.content.TextContentFactoryModule;
import org.jfree.report.content.TextLine;
import org.jfree.report.content.TextParagraph;
import org.jfree.report.filter.StaticDataSource;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.layout.SizeCalculator;
import org.jfree.report.layout.SizeCalculatorException;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.report.util.ElementLayoutInformation;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictGeomUtility;

public class TextContentTest extends TestCase
{
  public static class DebugSizeCalculator implements SizeCalculator
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

  private static final long STRICT_FACTOR = StrictGeomUtility.toInternalValue(1);

  public TextContentTest()
  {
  }

  public TextContentTest(final String s)
  {
    super(s);
  }

  /**
   *
   * @throws Exception
   */
  public void testNullContent() throws Exception
  {
    final TextElement se = new TextElement();
    se.setNullString("");
    final DefaultContentFactory df = new DefaultContentFactory();
    df.addModule(new TextContentFactoryModule());
    assertTrue(df.canHandleContent(se.getContentType()));
    ElementLayoutInformation eli = 
      new ElementLayoutInformation(new StrictBounds(0, 0, 10 * STRICT_FACTOR, 10 * STRICT_FACTOR));
    assertEquals(EmptyContent.getDefaultEmptyContent(), df.createContentForElement(se, eli, new DefaultLayoutSupport()));

    eli = new ElementLayoutInformation(new StrictBounds(0, 0, 0, 0));
    assertEquals(EmptyContent.getDefaultEmptyContent(), df.createContentForElement(se, eli, new DefaultLayoutSupport()));
  }

  public void testInvisibleContent() throws Exception
  {
    final TextElement se = new TextElement();
    se.setDataSource(new StaticDataSource("Hello World :)"));
    final DefaultContentFactory df = new DefaultContentFactory();
    df.addModule(new TextContentFactoryModule());
    assertTrue(df.canHandleContent(se.getContentType()));

    ElementLayoutInformation eli =
      new ElementLayoutInformation(new StrictBounds(0, 0, 10 * STRICT_FACTOR, 10 * STRICT_FACTOR));
    assertNotSame(EmptyContent.getDefaultEmptyContent(), df.createContentForElement(se, eli, new DefaultLayoutSupport()));

    eli = new ElementLayoutInformation(new StrictBounds(0, 0, 0, 0));
    assertEquals(EmptyContent.getDefaultEmptyContent(), df.createContentForElement(se, eli, new DefaultLayoutSupport()));
  }

  public void testLeadingWhiteSpaces () throws Exception
  {
    final String textLeadWhite = "  abc\n  abc";
    final StrictBounds inputBounds = new StrictBounds (0,0, StrictGeomUtility.toInternalValue(100),
                            StrictGeomUtility.toInternalValue(10));

    final TextContent content = new TextContent
        (textLeadWhite, StrictGeomUtility.toInternalValue(10), inputBounds,
            new DebugSizeCalculator(10, 10), "..", false);

    final StrictBounds expectedResultBounds =
            new StrictBounds(0,0, StrictGeomUtility.toInternalValue(50),
                StrictGeomUtility.toInternalValue(10));
    assertEquals(expectedResultBounds, content.getMinimumContentSize());

    final TextParagraph tp = (TextParagraph) content.getContentPart(0);
    final TextLine tl = (TextLine) tp.getContentPart(0);

    assertEquals(expectedResultBounds, tl.getMinimumContentSize());
    assertEquals(tl.getContent(), "  abc");
  }

  public static void test1a()
  {
    final String content = "123 thousand people";
    final SizeCalculator ds = new DebugSizeCalculator(10, 10);
    final TextParagraph tp = new TextParagraph
            (ds, 10 * STRICT_FACTOR, "..", false);
    tp.setContent
            (content, 0, 0, 100*STRICT_FACTOR, 10 * STRICT_FACTOR);

    final TextLine tl = (TextLine) tp.getContentPart(0);
    assertEquals("123 thou..", tl.getContent());
    assertEquals(new StrictBounds(0,0, STRICT_FACTOR * 100, STRICT_FACTOR * 10),
            tl.getBounds());
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
        (new DebugSizeCalculator(10, 10), 10 * STRICT_FACTOR, "..", false);
    tp.setContent(content, 0, 0, 345 * STRICT_FACTOR, 5000 * STRICT_FACTOR);
    assertEquals(new StrictBounds(0,0, STRICT_FACTOR * 330, STRICT_FACTOR * 100), tp.getMinimumContentSize());

    for (int i = 0; i < tp.getContentPartCount(); i++)
    {
      final TextLine tl = (TextLine) tp.getContentPart(i);
      assertTrue(tl.getBounds().getWidth() < 345 * STRICT_FACTOR);
      assertTrue(tl.getBounds().getY() == (i * 10 * STRICT_FACTOR));
      assertTrue(tl.getBounds().getHeight() == (10 * STRICT_FACTOR));
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
    final StrictBounds bounds =
            new StrictBounds (0, 0, STRICT_FACTOR * 500, STRICT_FACTOR * 500);
    ContentContainer tc = new TextContent (content, 10 * STRICT_FACTOR, bounds,
            new DebugSizeCalculator(10, 10), "..", false);
    tc = (ContentContainer) tc.getContentForBounds(tc.getMinimumContentSize());

    final ContentContainer tp = (ContentContainer) tc.getContentPart(0);
    for (int i = 0; i < tp.getContentPartCount(); i++)
    {
      final TextLine tl = (TextLine) tp.getContentPart(i);
      assertTrue(tl.getBounds().getWidth() <= 500 * STRICT_FACTOR);
      assertTrue(tl.getBounds().getY() == (i * 10 * STRICT_FACTOR));
      assertTrue(tl.getBounds().getHeight() == (10 * STRICT_FACTOR));
      assertEquals(results[i], tl.getContent());
    }

    assertEquals(tc.getContentForBounds(tc.getMinimumContentSize()).getBounds(),
        tc.getMinimumContentSize());
  }


  public void testLineBreaking2()
  {
    final String content = 
        "Thisisareallylongword, noone thought thatawordcanbethatlong, " +
        "itwontfitonaline, but these words do, so heres the test!";
    ContentContainer tc = new TextContent(content, 10,
            new StrictBounds(0, 0, STRICT_FACTOR * 200, STRICT_FACTOR * 5000),
        new DebugSizeCalculator(10, 10),"..", false);

    final String[] results = {
      "Thisisareallylongwor", // 20 chars
      "d, noone thought ",    // 17 chars
      "thatawordcanbethatlo", // 20
      "ng, ",                 // 4
      "itwontfitonaline, ",   // 18
      "but these words do, ", // 20
      "so heres the test!"    // 18
    };
    tc = (ContentContainer) tc.getContentForBounds(tc.getMinimumContentSize());
    final ContentContainer tp = (ContentContainer) tc.getContentPart(0);
    for (int i = 0; i < tp.getContentPartCount(); i++)
    {
      final TextLine tl = (TextLine) tp.getContentPart(i);
      assertTrue(tl.getBounds().getWidth() <= 200 * STRICT_FACTOR);
      assertTrue(tl.getBounds().getY() == (i * 10 * STRICT_FACTOR));
      assertTrue(tl.getBounds().getHeight() == (10 * STRICT_FACTOR));
      assertEquals(results[i], tl.getContent());
    }
    assertEquals(new StrictBounds(0,0, STRICT_FACTOR * 200, STRICT_FACTOR * 70), tp.getMinimumContentSize());
  }


   public void testLineHeight() throws SizeCalculatorException
    {
        LayoutSupport ot = new DefaultLayoutSupport();
        StrictBounds tBounds = new StrictBounds(0, 0, 114 * 1000, 38 * 1000);
        FontDefinition fontDefinition = new FontDefinition("dialog", 12);
        long lineHeight = StrictGeomUtility.toInternalValue(20);

        TextContent tc = new TextContent("asd as da sd a sd as da sda sd a sdas d  asd asd a sd a sd a sd a sd ",
                                         lineHeight,
                                         tBounds,
                                         ot.createTextSizeCalculator(fontDefinition),
                                         "..",
                                         false);
    }
}
