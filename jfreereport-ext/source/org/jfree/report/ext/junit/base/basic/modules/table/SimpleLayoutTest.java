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
 * SimpleLayoutTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: SimpleLayoutTest.java,v 1.1 2003/10/11 21:36:07 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 10.10.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.basic.modules.table;

import java.awt.geom.Rectangle2D;

import junit.framework.TestCase;
import org.jfree.report.Boot;
import org.jfree.report.ext.junit.base.basic.modules.table.tableverify.VerifyCellBackground;
import org.jfree.report.ext.junit.base.basic.modules.table.tableverify.VerifyCellContent;
import org.jfree.report.modules.output.table.base.TableGrid;
import org.jfree.report.modules.output.table.base.TableGridLayout;

public class SimpleLayoutTest extends TestCase
{
  public SimpleLayoutTest()
  {
  }

  public SimpleLayoutTest(String s)
  {
    super(s);
  }

  public void testSimpleLayout ()
  {
    Boot.start();

    TableGrid grid = new TableGrid(false);
    // this is the layout of the report header of sample report 1
    grid.addData(new VerifyCellBackground (new Rectangle2D.Float (0,0,592,18), "T0"));
    grid.addData(new VerifyCellBackground (new Rectangle2D.Float (0,18,592,0), "T1"));
    grid.addData(new VerifyCellBackground (new Rectangle2D.Float (0,18,592,90), "T2"));
    grid.addData(new VerifyCellContent (new Rectangle2D.Float (0,108,592,18), "T3"));
    grid.addData(new VerifyCellContent (new Rectangle2D.Float (0,126,592,10), "T4"));
    grid.addData(new VerifyCellBackground (new Rectangle2D.Float (0,136,592,0), "T5"));

    TableGridLayout layout = grid.performLayout();
    assertEquals(4, layout.getHeight());
    assertEquals(1, layout.getWidth());

    assertEquals("Row Start", 0, layout.getRowStart(0));
    assertEquals("Row Start", 18, layout.getRowStart(1));
    assertEquals("Row Start", 108, layout.getRowStart(2));
    assertEquals("Row Start", 126, layout.getRowStart(3));
    assertEquals("Row End", 18, layout.getRowEnd(0));
    assertEquals("Row End", 108, layout.getRowEnd(1));
    assertEquals("Row End", 126, layout.getRowEnd(2));
    assertEquals("Row End", 136, layout.getRowEnd(3));

//    Log.debug ("Last Element: " + layout.getData(0, 4));
  }
}
