/**
 * Date: Feb 18, 2003
 * Time: 6:30:13 PM
 *
 * $Id: PlainTextAlignmentTest.java,v 1.3 2003/07/03 16:06:17 taqua Exp $
 */
package org.jfree.report.ext.junit;

import java.awt.print.PageFormat;
import java.awt.print.Paper;

import org.jfree.report.modules.output.pageable.plaintext.PlainTextOutputTarget;
import org.jfree.report.modules.output.pageable.plaintext.PrinterCommandSet;
import org.jfree.report.util.Log;
import org.jfree.report.util.PageFormatFactory;

public class PlainTextAlignmentTest
{
  public static void main(final String[] args)
      throws Exception
  {
    final PageFormatFactory pff = PageFormatFactory.getInstance();
    final Paper p = pff.createPaper(PageFormatFactory.A4);
    pff.setBorders(p, 72, 72, 72, 72);
    final PageFormat pf = pff.createPageFormat(p, PageFormat.PORTRAIT);

    final PrinterCommandSet pc = new PrinterCommandSet(System.out, pf, 6, 10);
    final PlainTextOutputTarget target = new PlainTextOutputTarget(pf, pc);
    target.open();

    final float valign = target.getVerticalAlignmentBorder();
    final float halign = target.getHorizontalAlignmentBorder();
    Log.debug("LogicalPage: " + target.getLogicalPage());
    final double width = target.getLogicalPage().getWidth();
    final double height = target.getLogicalPage().getHeight();

    Log.debug("valign : " + valign);
    Log.debug("halign : " + halign);
    Log.debug("width  : " + width);
    Log.debug("height : " + height);
  }
}
