/**
 * Date: Feb 18, 2003
 * Time: 6:30:13 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.junit;

import com.jrefinery.report.targets.pageable.output.PlainTextOutputTarget;
import com.jrefinery.report.targets.pageable.output.PrinterCommandSet;
import com.jrefinery.report.util.PageFormatFactory;
import com.jrefinery.report.util.Log;

import java.awt.print.PageFormat;
import java.awt.print.Paper;

public class PlainTextAlignmentTest
{
  public static void main (String [] args)
    throws Exception
  {
    PageFormatFactory pff = PageFormatFactory.getInstance();
    Paper p = pff.createPaper(PageFormatFactory.A4);
    pff.setBorders(p, 72, 72, 72, 72);
    PageFormat pf = pff.createPageFormat(p, PageFormat.PORTRAIT);

    PrinterCommandSet pc = new PrinterCommandSet(System.out, pf, 6, 10);
    PlainTextOutputTarget target = new PlainTextOutputTarget(pf, pc);
    target.open();

    float valign = target.getVerticalAlignmentBorder();
    float halign = target.getHorizontalAlignmentBorder();
    Log.debug ("LogicalPage: " + target.getLogicalPage());
    double width = target.getLogicalPage().getWidth();
    double height = target.getLogicalPage().getHeight();

    Log.debug ("valign : " + valign);
    Log.debug ("halign : " + halign);
    Log.debug ("width  : " + width);
    Log.debug ("height : " + height);
  }
}
