/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ShapeContentTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ShapeContentTest.java,v 1.4 2003/11/01 19:57:02 taqua Exp $
 *
 * Changes
 * -------------------------
 * 10.06.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.basic.content;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import junit.framework.TestCase;
import org.jfree.report.ShapeElement;
import org.jfree.report.content.Content;
import org.jfree.report.content.DefaultContentFactory;
import org.jfree.report.content.ShapeContentFactoryModule;
import org.jfree.report.content.EmptyContent;
import org.jfree.report.content.ShapeContent;
import org.jfree.report.filter.StaticDataSource;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.util.ElementLayoutInformation;
import org.jfree.report.util.geom.StrictBounds;

public class ShapeContentTest extends TestCase
{
  public ShapeContentTest()
  {
  }

  public ShapeContentTest(final String s)
  {
    super(s);
  }

  public void testNullContent() throws Exception
  {
    final ShapeElement se = new ShapeElement();
    final DefaultContentFactory df = new DefaultContentFactory();
    df.addModule(new ShapeContentFactoryModule());
    assertTrue(df.canHandleContent(se.getContentType()));
    ElementLayoutInformation eli = 
      new ElementLayoutInformation(new StrictBounds(0, 0, 10, 10));
    assertEquals(EmptyContent.getDefaultEmptyContent(), df.createContentForElement(se, eli, new DefaultLayoutSupport()));

    eli = new ElementLayoutInformation(new StrictBounds(0, 0, 0, 0));
    assertEquals(EmptyContent.getDefaultEmptyContent(), df.createContentForElement(se, eli, new DefaultLayoutSupport()));
  }

  public void testInvisibleContent() throws Exception
  {
    final ShapeElement se = new ShapeElement();
    se.setDataSource(new StaticDataSource(new Line2D.Float(0, 0, 10, 10)));
    final DefaultContentFactory df = new DefaultContentFactory();
    df.addModule(new ShapeContentFactoryModule());
    assertTrue(df.canHandleContent(se.getContentType()));
    ElementLayoutInformation eli = 
      new ElementLayoutInformation(new StrictBounds(0, 0, 10, 10));
    final Content contentForElement = df.createContentForElement(se, eli, new DefaultLayoutSupport());
    assertTrue(contentForElement instanceof EmptyContent);

    eli = new ElementLayoutInformation(new StrictBounds(0, 0, 0, 0));
    assertEquals(EmptyContent.getDefaultEmptyContent(), df.createContentForElement(se, eli, new DefaultLayoutSupport()));
  }

  public void testLineContent() throws Exception
  {
    final ShapeElement se = new ShapeElement();
    se.setShouldDraw(true);
    se.setDataSource(new StaticDataSource(new Line2D.Float(40, 70, 140, 70)));
    final DefaultContentFactory df = new DefaultContentFactory();
    df.addModule(new ShapeContentFactoryModule());
    assertTrue(df.canHandleContent(se.getContentType()));
    ElementLayoutInformation eli = 
      new ElementLayoutInformation(new StrictBounds(0, 0, 10, 10));
    assertFalse(df.createContentForElement(se, eli, new DefaultLayoutSupport()) instanceof EmptyContent);

    eli = new ElementLayoutInformation(new StrictBounds(40, 70, 100, 0));
    final Content c = df.createContentForElement(se, eli, new DefaultLayoutSupport());
    assertEquals(new StrictBounds(40, 70, 100, 0), c.getBounds());

    eli = new ElementLayoutInformation(new StrictBounds(0, 0, 0, 0));
    assertEquals(EmptyContent.getDefaultEmptyContent(), df.createContentForElement(se, eli, new DefaultLayoutSupport()));
  }
}
