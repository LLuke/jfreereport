package org.jfree.report.ext.junit.bugs;

import java.awt.Font;

import org.jfree.report.JFreeReportBoot;
import org.jfree.report.modules.output.support.itext.BaseFontFactory;
import org.jfree.report.modules.output.support.itext.BaseFontSupport;
import com.lowagie.text.pdf.BaseFont;

/**
 * Creation-Date: 02.11.2005, 00:19:22
 *
 * @author Thomas Morgner
 */
public class FontMetricsTest
{
  public static void main(String[] args) throws NoSuchFieldException
  {
    JFreeReportBoot.getInstance().start();
    final BaseFontSupport bsf = new BaseFontSupport();
    BaseFont bf = bsf.awtToPdf
            (new Font ("Arial", Font.BOLD, 10));
    System.out.println(bf);
    Object wm = bf.getClass().getField("os_2");

  }
}
