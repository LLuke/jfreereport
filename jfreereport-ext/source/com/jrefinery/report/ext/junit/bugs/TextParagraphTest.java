package com.jrefinery.report.ext.junit.bugs;

import java.awt.Font;

import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.base.content.TextParagraph;
import com.jrefinery.report.targets.base.layout.DefaultSizeCalculator;
import com.jrefinery.report.targets.pageable.operations.TextOperationModule;
import com.jrefinery.report.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 18.03.2003
 * Time: 17:27:01
 * To change this template use Options | File Templates.
 */
public class TextParagraphTest
{
  public static void main (String [] args)
  {
    test1();
    test2();
  }

  public static void test1 ()
  {
    String content = "This    report   lists   a    number   of    Open    Source    Java    projects   that    you                       might   find   useful.    The   report  has    been    generated   using   JFreeReport.    For   an  up-to-date    list,    please   visit    the   following   web   page:";
    DefaultSizeCalculator ds = new DefaultSizeCalculator(new FontDefinition (new Font ("Serif", Font.PLAIN, 10)));
    TextParagraph tp = new TextParagraph(ds, 10);
    tp.setContent(content, 0,0, 345, 5000);

    TextOperationModule.print(tp);

    Log.debug (tp.getMinimumContentSize());
  }

  public static void test2 ()
  {
    String content = "Thisisareallylongword, noone thought thatawordcanbethatlong, itwontfitonaline, but these words do, so heres the test!";
    DefaultSizeCalculator ds = new DefaultSizeCalculator(new FontDefinition (new Font ("Serif", Font.PLAIN, 10)));
    TextParagraph tp = new TextParagraph(ds, 10);
    tp.setContent(content, 0,0, 75, 5000);

    TextOperationModule.print(tp);

    Log.debug (tp.getMinimumContentSize());
  }
}
