/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * TableCellBackgroundTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TableCellBackgroundTest.java,v 1.2.2.1 2004/04/06 16:52:36 taqua Exp $
 *
 * Changes
 * -------------------------
 * 11.10.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.basic.modules.table;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import junit.framework.TestCase;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.content.EmptyContent;
import org.jfree.report.ext.junit.base.basic.style.DebugStyleSheet;
import org.jfree.report.modules.output.table.base.TableCellBackground;
import org.jfree.report.style.ElementStyleSheet;

public class TableCellBackgroundTest extends TestCase
{
  public TableCellBackgroundTest()
  {
  }

  public TableCellBackgroundTest(final String s)
  {
    super(s);
  }

  public void testMerge ()
  {
    JFreeReportBoot.getInstance().start();

    final TableCellBackground bg1 = createBackground
      (new Rectangle2D.Float(0,0, 500, 20), Color.black);
    final TableCellBackground bgBottom = createBackground
      (new Rectangle2D.Float(0,20, 500, 0), Color.black);
    final TableCellBackground bgRight = createBackground
      (new Rectangle2D.Float(500, 0, 0, 20), Color.black);

    bgRight.setBorderLeft(Color.black, 10);
    bgBottom.setBorderTop(Color.black, 10);

    TableCellBackground merge = bg1.merge(bgBottom);
    assertEquals(Color.black, merge.getColorBottom());
    assertEquals(10f, merge.getBorderSizeBottom(), 0);

    merge = bg1.merge(bgRight);
    assertEquals(Color.black, merge.getColorRight());
    assertEquals(10f, merge.getBorderSizeRight(), 0);
  }

  public void testMergeReport1 ()
  {
    JFreeReportBoot.getInstance().start();

    final TableCellBackground bgLineBottom =
      createBackground(new Rectangle2D.Float(0, 536, 592, 0), Color.black);
    final TableCellBackground bgBackground =
      createBackground(new Rectangle2D.Float(0, 526, 592, 10), Color.black);

    bgLineBottom.setBorderTop(Color.black, 10);

    final TableCellBackground merge = bgBackground.merge(bgLineBottom);
    assertEquals(Color.black, merge.getColorBottom());
    assertEquals(10f, merge.getBorderSizeBottom(), 0);

  }

  public void testMergeLines ()
  {
    JFreeReportBoot.getInstance().start();

    final TableCellBackground bgUnion =
      createBackground(new Rectangle2D.Float(0, 126, 592, 10), Color.green);

    final TableCellBackground bgLineTop =
      createBackground(new Rectangle2D.Float(0, 126, 592, 0), Color.red);
    final TableCellBackground bgLineBottom =
      createBackground(new Rectangle2D.Float(0, 136, 592, 0), Color.black);

    bgLineTop.setBorderTop(Color.red, 10);
    bgLineBottom.setBorderTop(Color.black, 10);

    final TableCellBackground immed = bgUnion.merge(bgLineTop);
    assertEquals(Color.red, immed.getColorTop());
    assertEquals(10f, immed.getBorderSizeTop(), 0);

    final TableCellBackground merge = immed.merge(bgLineBottom);
    assertEquals(Color.red, merge.getColorTop());
    assertEquals(Color.black, merge.getColorBottom());
    assertEquals(10f, merge.getBorderSizeBottom(), 0);
    assertEquals(10f, merge.getBorderSizeTop(), 0);

  }

  private TableCellBackground createBackground
          (final Rectangle2D bounds, final Color color)
  {

    final ElementStyleSheet es = new DebugStyleSheet("Name");
    es.setStyleProperty(ElementStyleSheet.BOUNDS, bounds);
    es.setStyleProperty(ElementStyleSheet.PAINT, color);

    return new TableCellBackground
            (EmptyContent.getDefaultEmptyContent(), es, color);
  }

  public void testJoin ()
  {
    JFreeReportBoot.getInstance().start();

    final TableCellBackground bg1 = createBackground
      (new Rectangle2D.Float(0,0, 500, 20), Color.black);
    bg1.setBorderLeft(Color.green, 11);
    bg1.setBorderTop(Color.red, 9);

    // join bg1 with a bottom cell (row + 1)
    final TableCellBackground bgBottom = createBackground
      (new Rectangle2D.Float(0,20, 500, 0), Color.black);
    bgBottom.setBorderLeft(Color.green, 11);
    bgBottom.setBorderBottom(Color.black, 10);

//    final TableCellBackground joined = bg1.joinBottom(bgBottom);
//    assertEquals(Color.black, joined.getColorBottom());
//    assertEquals(10f, joined.getBorderSizeBottom(), 0);
//    assertEquals(Color.green, joined.getColorLeft());
//    assertEquals(11f, joined.getBorderSizeLeft(), 0);
//    assertEquals(Color.red, joined.getColorTop());
//    assertEquals(9f, joined.getBorderSizeTop(), 0);
//
//    final TableCellBackground bgRight = new TableCellBackground
//      (new Rectangle2D.Float(500, 0, 0, 20), Color.black);
//    bgRight.setBorderRight(Color.black, 10);
//    bgRight.setBorderTop(Color.red, 9);
//
//    final TableCellBackground joined2 = bg1.joinRight(bgRight);
//    assertEquals(Color.black, joined2.getColorRight());
//    assertEquals(10f, joined2.getBorderSizeRight(), 0);
//    assertEquals(Color.green, joined2.getColorLeft());
//    assertEquals(11f, joined2.getBorderSizeLeft(), 0);
//    assertEquals(Color.red, joined2.getColorTop());
//    assertEquals(9f, joined.getBorderSizeTop(), 0);
  }


}


