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
 * $Id$
 *
 * Changes 
 * -------------------------
 * 11.10.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.basic.modules.table;

import java.awt.geom.Rectangle2D;
import java.awt.Color;

import junit.framework.TestCase;
import org.jfree.report.modules.output.table.base.TableCellBackground;
import org.jfree.report.Boot;

public class TableCellBackgroundTest extends TestCase
{
  public TableCellBackgroundTest()
  {
  }

  public TableCellBackgroundTest(String s)
  {
    super(s);
  }

  public void testMerge ()
  {
    Boot.start();

    TableCellBackground bg1 = new TableCellBackground(new Rectangle2D.Float(0,0, 500, 20), Color.black);
    TableCellBackground bgBottom = new TableCellBackground(new Rectangle2D.Float(0,20, 500, 0), Color.black);
    TableCellBackground bgRight = new TableCellBackground(new Rectangle2D.Float(500, 0, 0, 20), Color.black);

    bgRight.setBorderLeft(Color.black, 10);
    bgBottom.setBorderTop(Color.black, 10);

    TableCellBackground merge = bg1.merge(bgBottom, new Rectangle2D.Float(0,0, 500, 20));
    assertEquals(Color.black, merge.getColorBottom());
    assertEquals(10f, merge.getBorderSizeBottom(), 0);

    merge = bg1.merge(bgRight, new Rectangle2D.Float(0,0, 500, 20));
    assertEquals(Color.black, merge.getColorRight());
    assertEquals(10f, merge.getBorderSizeRight(), 0);
  }

  public void testMergeReport1 ()
  {
    Boot.start();

    TableCellBackground bgLineBottom = new TableCellBackground(new Rectangle2D.Float(0, 536, 592, 0), Color.black);
    TableCellBackground bgBackground = new TableCellBackground(new Rectangle2D.Float(0, 526, 592, 10), Color.black);

    bgLineBottom.setBorderTop(Color.black, 10);

    TableCellBackground merge = bgBackground.merge(bgLineBottom, new Rectangle2D.Float(0, 526, 592, 10));
    assertEquals(Color.black, merge.getColorBottom());
    assertEquals(10f, merge.getBorderSizeBottom(), 0);

  }

  public void testMergeLines ()
  {
    Boot.start();

    TableCellBackground bgLineTop = new TableCellBackground(new Rectangle2D.Float(0, 126, 592, 0), Color.black);
    TableCellBackground bgLineBottom = new TableCellBackground(new Rectangle2D.Float(0, 136, 592, 0), Color.black);

    bgLineTop.setBorderTop(Color.red, 10);
    bgLineBottom.setBorderTop(Color.black, 10);

    TableCellBackground merge = bgLineTop.merge(bgLineBottom, new Rectangle2D.Float(0, 126, 592, 10));
    assertEquals(Color.red, merge.getColorTop());
    assertEquals(Color.black, merge.getColorBottom());
    assertEquals(10f, merge.getBorderSizeBottom(), 0);
    assertEquals(10f, merge.getBorderSizeTop(), 0);

  }
}


