package org.jfree.report.ext.junit.base.basic.modules.output;

import java.awt.Color;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.TreeSet;
import java.util.Iterator;

import org.jfree.report.modules.output.table.xls.util.ExcelColorSupport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.util.PageFormatFactory;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * Creation-Date: 19.10.2005, 20:02:57
 *
 * @author Thomas Morgner
 */
public class ExcelColorTest
{
  public static void main(String[] args)
  {
    System.out.println(PageFormatFactory.getInstance().convertMmToPoints(10));

//    JFreeReportBoot.getInstance().start();
//
//    final Color color = new Color (0xEEEEFF);
//    final short excelColor = ExcelColorSupport.getNearestColor(color);
//    System.out.println("Found " + excelColor);
//    final Hashtable map = HSSFColor.getIndexHash();
//
//    TreeSet keys = new TreeSet (map.keySet());
//    for (Iterator iterator = keys.iterator(); iterator.hasNext();)
//    {
//      Object o = iterator.next();
//      final HSSFColor excelColorObject = (HSSFColor) map.get(o);
//      System.out.println("Color " + o + " = " +
//              excelColorObject.getTriplet()[0] + ":" +
//              excelColorObject.getTriplet()[1] + ":" +
//              excelColorObject.getTriplet()[2]);
//
//    }

  }
}
