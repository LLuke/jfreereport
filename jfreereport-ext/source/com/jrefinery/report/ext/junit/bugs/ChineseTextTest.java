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
 * ChineseTextTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes
 * -------------------------
 * 09.06.2003 : Initial version
 *
 */

package com.jrefinery.report.ext.junit.bugs;

import java.awt.Font;
import java.awt.geom.Rectangle2D;

import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.ItemFactory;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.targets.support.ReportProcessorUtil;

public class ChineseTextTest
{
  public ChineseTextTest()
  {
  }

  public JFreeReport getReport()
  {
    // The chinese text, as unicode character sequence ...
    char[] text = {
      34987, 31216, 20026, 32593, 32476, 26102, 20195,
      30340, 36135, 24065, 65292, 23436, 20840, 22522,
      20110, 30340, 32593, 19978, 25903, 20184, 24179,
      21488, 65292, 21019, 24314, 20110, 24180, 24213,
      12290, 23427, 37027, 23436, 20840, 21311, 21517,
      65292, 24555, 25463, 65292, 26041, 20415, 65292,
      24443, 24213, 31881, 30862, 22320, 22495, 27010,
      24565, 30340, 31181, 31181, 29305, 28857, 19982,
      20248, 21183, 65292, 20351, 20043, 22312, 30701,
      30701, 24180, 30340, 26102, 38388, 37324, 65292,
      24471, 21040, 20102, 36805, 29467, 30340, 21457,
      23637, 22269, 38469, 19978, 36234, 26469, 36234,
      22810, 30340, 20844, 21496, 21644, 32593, 32476,
      21830, 24215, 24320, 22987, 25509, 21463, 25903,
      20184, 26041, 24335, 12290, 23427, 24050, 32463,
      25104, 20026, 20154, 20204, 36827, 34892, 30005,
      23376, 21830, 21153, 24378, 26377, 21147, 30340,
      24037, 20855, 12290, 21253, 25324, 35937, 38597,
      34382, 65292, 20122, 39532, 36874, 31561, 24456,
      22810, 32593, 32476, 20844, 21496, 37117, 24320,
      36890, 20102, 25903, 20184, 20132, 26131, 26041,
      24335, 65292, 20320, 21482, 38656, 33457, 19977,
      20998, 38047, 23601, 21487, 20197, 20813, 36153,
      30003, 35831, 19968, 20010, 36134, 21495, 65292,
      36134, 21495, 20043, 38388, 20114, 30456, 36716,
      36134, 21482, 38656, 31186, 38047, 65292, 20320,
      20063, 21487, 20197, 23558, 29616, 37329, 36890,
      36807, 38134, 34892, 30452, 25509, 36716, 21040,
      20320, 30340, 36134, 21495, 65292, 20063, 21487,
      20197, 25226, 36134, 21495, 37324, 30340, 25910,
      20837, 36716, 21040, 20840, 29699, 20219, 20309,
      19968, 23478, 38134, 34892, 20320, 30340, 31169,
      20154, 36134, 25143, 37324, 65292, 32780, 19981,
      35770, 20320, 36523, 23621, 20309, 22320, 12289,
      36523, 22788, 20309, 26041, 65281, 23427, 20197,
      40644, 37329, 20316, 20026, 31561, 20215, 22522,
      30784, 65292, 23436, 20840, 33073, 31163, 20219,
      20309, 22269, 23478, 20869, 37096, 36130, 25919,
      30340, 24433, 21709, 12290};
    JFreeReport report = new JFreeReport();
    report.getReportHeader().addElement(ItemFactory.createLabelElement(
        null, new Rectangle2D.Float(0, 0, -100, 12), null,
        ElementAlignment.LEFT.getOldAlignment(), null,
        "4 lines with chinese text follows ..."));

    // This font is a chinese font ... if you dont have this font, replace
    // it with another font, which is able to display chinese characters ...
    report.getReportHeader().addElement(ItemFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 12, -100, 48),
            null, ElementAlignment.LEFT.getOldAlignment(),
            new Font("mhya9gjp", Font.PLAIN, 10), new String(text)));

    // Embedding the fonts increases the size of the PDF file, but makes
    // sure, that everbody can read the file. If all users have all the fonts
    // installed, which were used to create the report, then you can remove
    // the font-embedding flag.
    //
    // Embedding can also be enabled for single elements (using a FontDefinition
    // object).
    report.getReportConfiguration().setPDFTargetEmbedFonts(true);

    // Make the PDF file an Unicode file. Without this, you would only see
    // ascii characters, all other characters would not be displayed.
    report.getReportConfiguration().setPdfTargetEncoding("Identity-H");
    return report;
  }

  public static void main(String[] args) throws Exception
  {
    JFreeReport report = new ChineseTextTest().getReport();
    ReportProcessorUtil.createPDF(report, "/tmp/chinese-text.pdf");
  }
}
