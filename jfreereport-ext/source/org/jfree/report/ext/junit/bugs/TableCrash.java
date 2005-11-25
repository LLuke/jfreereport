/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * TableCrash.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: TableCrash.java,v 1.1 2005/10/02 19:48:01 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.ext.junit.bugs;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.io.IOException;
import javax.swing.table.AbstractTableModel;

import org.jfree.report.Band;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.SimplePageDefinition;
import org.jfree.report.TextElement;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.layout.StackedLayoutManager;
import org.jfree.report.layout.StaticLayoutManager;
import org.jfree.report.modules.output.table.xls.ExcelReportUtil;
import org.jfree.report.util.PageFormatFactory;

public class TableCrash
{
  private TableCrash()
  {
  }

  public static void main (String[] args)
          throws ReportProcessingException, IOException
  {
    final String paper = "A7";

    ReportTableModel reportTableModel = new ReportTableModel();
    JFreeReport report = new JFreeReport();

//    ImageElement imageDataRowElement =
//            ImageFieldElementFactory.createImageDataRowElement
//                    ("I1", new Rectangle2D.Double(0.0, 0.0, 80, 80),
//                            "mediumImage", true, true);
//    report.getItemBand().addElement(imageDataRowElement);

    TextElement t2 = TextFieldElementFactory.createStringElement("T2",
            new Rectangle2D.Double(0, 0.0, -100, 30.0),
            null, null, null, null, "-", "title");
    t2.setFontSize(30);
    t2.setDynamicContent(true);

    TextElement t3 = TextFieldElementFactory.createStringElement("T3",
            new Rectangle2D.Double(0, 0, -100, 50),
            null, null, null, null, "-", "creators");
    t3.setDynamicContent(true);

    Band band = new Band();
    band.setName("Crasher");
    band.setLayout(new StackedLayoutManager());
    band.addElement(t2);
    band.addElement(t3);
    band.setLayoutCacheable(false);

    band.getStyle().setStyleProperty
            (StaticLayoutManager.ABSOLUTE_POS,
                    new Point2D.Double(90, 0));

    report.getItemBand().addElement(band);

    report.setData(reportTableModel);
    PageFormat pageFormat = new PageFormat();
    pageFormat.setOrientation(PageFormat.PORTRAIT);
    pageFormat.setPaper(PageFormatFactory.getInstance().createPaper(paper));
    PageFormatFactory.logPageFormat(pageFormat);
    report.setPageDefinition(new SimplePageDefinition(pageFormat));

    ExcelReportUtil.createXLS(report, "/tmp/test.xls");
  }

  private static class ReportTableModel extends AbstractTableModel
  {
    public ReportTableModel ()
    {
    }

    public int getRowCount ()
    {
      return 2;
    }

    public int getColumnCount ()
    {
      return 4;
    }

    public String getColumnName (int columnIndex)
    {
      switch (columnIndex)
      {
        case 0:
          return "itemId";
        case 1:
          return "mediumImage";
        case 2:
          return "title";
        case 3:
          return "creators";
      }

      return null;
    }

    public Class getColumnClass (int columnIndex)
    {

      return String.class;
    }

    public Object getValueAt (int rowIndex, int columnIndex)
    {
      switch (columnIndex)
      {
        case 0:
          return "a";
        case 1:
          return "b";
        case 2:
          return "Bend It Like Beckham UK IMPORT";
        case 3:
          return "test";
      }

      return null;
    }
  }
}
