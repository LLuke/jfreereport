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
 * ImageContentTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DrawableContentTest.java,v 1.1 2003/06/11 20:39:24 taqua Exp $
 *
 * Changes
 * -------------------------
 * 10.06.2003 : Initial version
 *
 */

package com.jrefinery.report.ext.junit.base.basic.content;

import java.awt.geom.Rectangle2D;

import com.jrefinery.report.DrawableContainer;
import com.jrefinery.report.DrawableElement;
import com.jrefinery.report.ext.junit.TestDrawable;
import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.targets.base.ElementLayoutInformation;
import com.jrefinery.report.targets.base.content.DefaultContentFactory;
import com.jrefinery.report.targets.base.content.DrawableContentFactoryModule;
import com.jrefinery.report.targets.base.layout.DefaultLayoutSupport;
import junit.framework.TestCase;
import org.jfree.ui.FloatDimension;

public class DrawableContentTest extends TestCase
{
  public DrawableContentTest()
  {
  }

  public DrawableContentTest(final String s)
  {
    super(s);
  }

  public void testNullContent() throws Exception
  {
    final DrawableElement se = new DrawableElement();
    final DefaultContentFactory df = new DefaultContentFactory();
    df.addModule(new DrawableContentFactoryModule());
    assertTrue(df.canHandleContent(se.getContentType()));
    ElementLayoutInformation eli = new ElementLayoutInformation(new Rectangle2D.Float(0, 0, 10, 10));
    assertNull(df.createContentForElement(se, eli, new DefaultLayoutSupport()));

    eli = new ElementLayoutInformation(new Rectangle2D.Float(0, 0, 0, 0));
    assertNull(df.createContentForElement(se, eli, new DefaultLayoutSupport()));
  }

  public void testInvisibleContent() throws Exception
  {
    final DrawableElement se = new DrawableElement();
    se.setDataSource(new StaticDataSource
        (new DrawableContainer(new TestDrawable(), new FloatDimension(10, 10),
            new Rectangle2D.Float(0, 0, 0, 0))));

    final DefaultContentFactory df = new DefaultContentFactory();
    df.addModule(new DrawableContentFactoryModule());
    assertTrue(df.canHandleContent(se.getContentType()));
    ElementLayoutInformation eli = new ElementLayoutInformation(new Rectangle2D.Float(0, 0, 10, 10));
    assertNotNull(df.createContentForElement(se, eli, new DefaultLayoutSupport()));

    eli = new ElementLayoutInformation(new Rectangle2D.Float(0, 0, 0, 0));
    assertNull(df.createContentForElement(se, eli, new DefaultLayoutSupport()));
  }

}
