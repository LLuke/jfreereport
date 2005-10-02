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
 * StackedLayoutManagerTest.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: StackedLayoutManagerTest.java,v 1.2 2005/09/19 13:34:24 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.ext.junit.base.basic.layout;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;

import junit.framework.TestCase;
import org.jfree.report.Band;
import org.jfree.report.TextElement;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.layout.BandLayoutManagerUtil;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.layout.StackedLayoutManager;
import org.jfree.report.layout.StaticLayoutManager;
import org.jfree.ui.FloatDimension;

public class StackedLayoutManagerTest extends TestCase
{
  public StackedLayoutManagerTest (final String s)
  {
    super(s);
  }

  public void testForEmptyBand ()
  {

    LabelElementFactory lfe = new LabelElementFactory();
    lfe.setName("T2");
    lfe.setMinimumSize(new FloatDimension(-100, 30));
    lfe.setText("Bend It Like Beckham UK IMPORT");
    lfe.setFontSize(new Integer (30));
    lfe.setDynamicHeight(Boolean.TRUE);

    final Band b = new Band();
    b.setLayout(new StackedLayoutManager());
    b.addElement(lfe.createElement());

    lfe.setFontSize(new Integer (10));
    lfe.setName("T3");
    b.addElement(lfe.createElement());
    b.setLayoutCacheable(false);
    b.getStyle().setStyleProperty
            (StaticLayoutManager.ABSOLUTE_POS, new Point2D.Double(90, 0));


    BandLayoutManagerUtil.doLayout(b, new DefaultLayoutSupport(), 210000, 500000);

    assertEquals("BandBounds", new StrictBounds (0,0,210000, 120000),
            b.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS));

    final Band b2 = new Band();
    b2.addElement(b);

    BandLayoutManagerUtil.doLayout(b2, new DefaultLayoutSupport(), 210000, 500000);
  }
}
