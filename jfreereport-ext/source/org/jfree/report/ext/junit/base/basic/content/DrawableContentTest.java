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
 * ImageContentTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DrawableContentTest.java,v 1.6 2005/02/19 16:15:46 taqua Exp $
 *
 * Changes
 * -------------------------
 * 10.06.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.basic.content;

import junit.framework.TestCase;
import org.jfree.report.DrawableElement;
import org.jfree.report.content.DefaultContentFactory;
import org.jfree.report.content.DrawableContentFactoryModule;
import org.jfree.report.content.EmptyContent;
import org.jfree.report.ext.junit.TestDrawable;
import org.jfree.report.filter.StaticDataSource;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.util.ElementLayoutInformation;
import org.jfree.report.util.geom.StrictBounds;

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
    ElementLayoutInformation eli = new ElementLayoutInformation
      (new StrictBounds(0, 0, 10, 10));
    assertEquals(EmptyContent.getDefaultEmptyContent(), df.createContentForElement(se, eli, new DefaultLayoutSupport(false)));

    eli = new ElementLayoutInformation(new StrictBounds(0, 0, 0, 0));
    assertEquals(EmptyContent.getDefaultEmptyContent(), df.createContentForElement(se, eli, new DefaultLayoutSupport(false)));
  }

  public void testInvisibleContent() throws Exception
  {
    final DrawableElement se = new DrawableElement();
    se.setDataSource(new StaticDataSource (new TestDrawable()));

    final DefaultContentFactory df = new DefaultContentFactory();
    df.addModule(new DrawableContentFactoryModule());
    assertTrue(df.canHandleContent(se.getContentType()));
    ElementLayoutInformation eli = 
      new ElementLayoutInformation(new StrictBounds(0, 0, 10, 10));
    assertNotSame(EmptyContent.getDefaultEmptyContent(), df.createContentForElement(se, eli, new DefaultLayoutSupport(false)));

    eli = new ElementLayoutInformation(new StrictBounds(0, 0, 0, 0));
    assertEquals(EmptyContent.getDefaultEmptyContent(), df.createContentForElement(se, eli, new DefaultLayoutSupport(false)));
  }

}
