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
 * ShapeElementTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ShapeElementTest.java,v 1.3 2003/07/03 16:06:17 taqua Exp $
 *
 * Changes
 * -------------------------
 * 09.06.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.basic;

import java.awt.Shape;
import java.awt.geom.Line2D;

import junit.framework.TestCase;
import org.jfree.report.Element;
import org.jfree.report.elementfactory.StaticShapeElementFactory;

public class ShapeElementTest extends TestCase
{
  public ShapeElementTest(final String s)
  {
    super(s);
  }

  public void testCreate()
  {
    final Line2D line = new Line2D.Float(0, 0, 100, 100);
    Element e = StaticShapeElementFactory.createLineShapeElement(null, null, null, line);
    Shape s = (Shape) e.getValue();
    assertEquals(s.getBounds2D(), line.getBounds2D());

    final Line2D line2 = new Line2D.Float(22, 22, 122, 122);
    e = StaticShapeElementFactory.createLineShapeElement(null, null, null, line2);
    s = (Shape) e.getValue();
    assertEquals(s.getBounds2D(), line.getBounds2D());

    try
    {
      StaticShapeElementFactory.createLineShapeElement(null, null, null,
          new Line2D.Double(26.0, 8.0, 26.0, -5.0));
      fail();
    }
    catch (IllegalArgumentException iae)
    {
    }

  }

}
