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
 * BandLayoutTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BandLayoutTest.java,v 1.2 2003/07/03 16:06:18 taqua Exp $
 *
 * Changes
 * -------------------------
 * 10.06.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.basic.layout;

import java.awt.geom.Rectangle2D;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.layout.BandLayoutManagerUtil;
import org.jfree.report.layout.DefaultLayoutSupport;
import junit.framework.TestCase;

public class BandLayoutTest extends TestCase
{
  public BandLayoutTest()
  {
  }

  public BandLayoutTest(final String s)
  {
    super(s);
  }

  public void testBasicLayout()
  {
    final Band band = new Band();
    BandLayoutManagerUtil.doLayout(band, new DefaultLayoutSupport(), 500, 200);
    // width is preserved  ...
    assertEquals(new Rectangle2D.Float(0, 0, 500, 0), BandLayoutManagerUtil.getBounds(band, null));

    final Element e = StaticShapeElementFactory.createRectangleShapeElement(null, null, null,
        new Rectangle2D.Float(0, 0, 10, 10), true, true);
    band.addElement(e);
    BandLayoutManagerUtil.doLayout(band, new DefaultLayoutSupport(), 500, 200);
    // width is preserved  ... height is the one given in the element
    assertEquals(new Rectangle2D.Float(0, 0, 500, 10), BandLayoutManagerUtil.getBounds(band, null));

  }
}
