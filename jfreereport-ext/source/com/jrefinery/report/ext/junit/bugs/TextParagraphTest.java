/**
 *
 */
package com.jrefinery.report.ext.junit.bugs;

import java.awt.Font;
import java.awt.geom.Rectangle2D;

import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.base.content.TextContent;
import com.jrefinery.report.targets.base.content.TextParagraph;
import com.jrefinery.report.targets.base.layout.DefaultSizeCalculator;
import com.jrefinery.report.targets.pageable.operations.TextOperationModule;
import com.jrefinery.report.util.Log;

public class TextParagraphTest
{
  public static void main(String[] args) throws Exception
  {
//    test3();
    test2();
//    test1a();
  }

  public static void test1a()
  {
    String content = "123 thousand people";
    DefaultSizeCalculator ds = new DefaultSizeCalculator(new FontDefinition(new Font("Serif", Font.PLAIN, 10)));
    TextParagraph tp = new TextParagraph(ds, 10);
    tp.setContent(content, 0, 0, 45, 10);

    TextOperationModule.print(tp);

    Log.debug(tp.getMinimumContentSize());
  }

  public static void test1()
  {
    String content = "This    report   lists   a    number   of    Open    Source    Java    projects   that    you                       might   find   useful.    The   report  has    been    generated   using   JFreeReport.    For   an  up-to-date    list,    please   visit    the   following   web   page:";
    DefaultSizeCalculator ds = new DefaultSizeCalculator(new FontDefinition(new Font("Serif", Font.PLAIN, 10)));
    TextParagraph tp = new TextParagraph(ds, 10);
    tp.setContent(content, 0, 0, 345, 5000);

    TextOperationModule.print(tp);

    Log.debug(tp.getMinimumContentSize());
  }

  public static void test3() throws Exception
  {

    String content = "This is a test. It will check to see if word wrapping is working correctly or not. If it does not work some of the words will be broken in the middle and some of them will be missing all together.";
    DefaultSizeCalculator ds = new DefaultSizeCalculator(new FontDefinition(new Font("Times New Roman", Font.PLAIN, 10)));
    TextContent tc = new TextContent(content, 10, new Rectangle2D.Float(0, 0, 500, 5000), ds);
    //TextOperationModule.print(tc);

    Log.debug(tc.getMinimumContentSize());
    tc.getContentForBounds(tc.getMinimumContentSize());

/*
    PDFOutputTarget ot = new PDFOutputTarget(new NullOutputStream(), new PageFormat(), false);
    SizeCalculator c = ot.createTextSizeCalculator(new FontDefinition (new Font ("Times New Roman", Font.PLAIN, 10)));
    TextParagraph tp2 = new TextParagraph(ds, 10);
    tp2.setContent(content, 0,0, 500, 5000);

    TextOperationModule.print(tp2);

    Log.debug (tp2.getMinimumContentSize());
    */
  }

  public static void test2()
  {
    String content = "Test\n\n\n\n\n\ntest\n";
    //String content = "Thisisareallylongword, noone thought thatawordcanbethatlong, itwontfitonaline, but these words do, so heres the test!";
    DefaultSizeCalculator ds = new DefaultSizeCalculator(new FontDefinition(new Font("Serif", Font.PLAIN, 10)));
    TextContent tp = new TextContent(content, 10, new Rectangle2D.Float(0, 0, 41, 5000), ds);
    //tp.setContent(content, 0,0, 41, 5000);

    TextOperationModule.print(tp);

    Log.debug(tp.getMinimumContentSize());
  }
}
