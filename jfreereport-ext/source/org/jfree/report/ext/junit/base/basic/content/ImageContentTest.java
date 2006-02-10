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
 * $Id: ImageContentTest.java,v 1.7 2005/03/24 23:09:10 taqua Exp $
 *
 * Changes
 * -------------------------
 * 10.06.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.basic.content;

import java.awt.image.BufferedImage;

import junit.framework.TestCase;
import org.jfree.report.DefaultImageReference;
import org.jfree.report.ImageElement;
import org.jfree.report.content.Content;
import org.jfree.report.content.DefaultContentFactory;
import org.jfree.report.content.EmptyContent;
import org.jfree.report.content.ImageContentFactoryModule;
import org.jfree.report.filter.StaticDataSource;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.util.ElementLayoutInformation;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictDimension;
import org.jfree.report.util.geom.StrictPoint;

public class ImageContentTest extends TestCase
{
  public ImageContentTest()
  {
  }

  public ImageContentTest(final String s)
  {
    super(s);
  }

  public void testNullContent() throws Exception
  {
    final ImageElement se = new ImageElement();
    final DefaultContentFactory df = new DefaultContentFactory();
    df.addModule(new ImageContentFactoryModule());
    assertTrue(df.canHandleContent(se.getContentType()));
    ElementLayoutInformation eli =
      new ElementLayoutInformation(new StrictBounds(0, 0, 10, 10));
    assertEquals(EmptyContent.getDefaultEmptyContent(), df.createContentForElement(se, eli, new DefaultLayoutSupport(false)));

    eli = new ElementLayoutInformation(new StrictBounds(0, 0, 0, 0));
    assertEquals(EmptyContent.getDefaultEmptyContent(), df.createContentForElement(se, eli, new DefaultLayoutSupport(false)));
  }

  public void testInvisibleContent() throws Exception
  {
    final ImageElement se = new ImageElement();
    se.setDataSource(new StaticDataSource
        (new DefaultImageReference(new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB))));

    final DefaultContentFactory df = new DefaultContentFactory();
    df.addModule(new ImageContentFactoryModule());
    assertTrue(df.canHandleContent(se.getContentType()));
    ElementLayoutInformation eli =
      new ElementLayoutInformation(new StrictBounds(0, 0, 10, 10));
    assertNotSame(EmptyContent.getDefaultEmptyContent(), df.createContentForElement(se, eli, new DefaultLayoutSupport(false)));

    eli = new ElementLayoutInformation(new StrictBounds(0, 0, 0, 0));
    assertEquals(EmptyContent.getDefaultEmptyContent(), df.createContentForElement(se, eli, new DefaultLayoutSupport(false)));
  }

  public void testSizedContent() throws Exception
  {
    final ImageElement se = new ImageElement();
    se.setDataSource(new StaticDataSource
        (new DefaultImageReference(new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB))));

    final DefaultContentFactory df = new DefaultContentFactory();
    df.addModule(new ImageContentFactoryModule());
    assertTrue(df.canHandleContent(se.getContentType()));
    final StrictPoint absPos = new StrictPoint(0,0);
    final StrictDimension minSize = new StrictDimension(0,0);
    final StrictDimension prefSize = new StrictDimension(10,10);
    final StrictDimension maxSize = new StrictDimension(100,1000);
    final ElementLayoutInformation eli = new ElementLayoutInformation(absPos, minSize, maxSize, prefSize);
    final Content c = df.createContentForElement(se, eli, new DefaultLayoutSupport(false));
    // content size is defined by the image .. the ELI just defines the view window
    assertEquals(new StrictBounds(0,0, 10000, 10000), c.getBounds());

  }

}
