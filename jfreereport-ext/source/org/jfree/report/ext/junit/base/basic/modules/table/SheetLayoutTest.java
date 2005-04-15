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
 * SheetLayoutTest.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: SheetLayoutTest.java,v 1.2 2005/04/11 13:33:46 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.ext.junit.base.basic.modules.table;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import junit.framework.TestCase;
import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.TextElement;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.content.ContentCreationException;
import org.jfree.report.content.Content;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.layout.BandLayoutManagerUtil;
import org.jfree.report.modules.output.meta.MetaBand;
import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.modules.output.table.base.TableMetaBandProducer;
import org.jfree.report.modules.output.table.base.SheetLayout;
import org.jfree.report.modules.output.table.base.RawContent;
import org.jfree.report.modules.output.table.base.TableCellBackground;
import org.jfree.ui.FloatDimension;
import org.jfree.util.Log;
import org.jfree.base.BaseBoot;

public class SheetLayoutTest extends TestCase
{
  private static class TestMetaBandProducer extends TableMetaBandProducer
  {
    public TestMetaBandProducer ()
    {
      super(new DefaultLayoutSupport());
    }

    protected MetaElement createDrawableCell (final Element e,
                                              final long x, final long y)
            throws ContentCreationException
    {
      return null;
    }

    protected MetaElement createImageCell (final Element e,
                                           final long x, final long y)
            throws ContentCreationException
    {
      return null;
    }

    protected MetaElement createTextCell (final Element e,
                                          final long x, final long y)
            throws ContentCreationException
    {
      // we only handle plain text elements at the moment ...
      if (e.getContentType().equals(TextElement.CONTENT_TYPE) == false)
      {
        return null;
      }
      if (e.isVisible() == false)
      {
        return null;
      }

      final StrictBounds bounds = (StrictBounds)
              e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);

      final Content content = new RawContent(bounds, e.getValue());
      final ElementStyleSheet style = new MetaElementStyleSheet("meta-" + e.getName());
      style.setStyleProperty(ElementStyleSheet.BOUNDS,
              createElementBounds(e.getStyle(), x, y));
      final MetaElement element = new MetaElement(content, style);
      return element;
    }
  }

  public SheetLayoutTest (final String s)
  {
    super(s);
    BaseBoot.getInstance().start();
  }

  public void testCreateVerticalLineBackground ()
          throws ContentCreationException
  {
    final Band band = new Band();
    band.setMinimumSize(new FloatDimension(500, 200));

    band.addElement(StaticShapeElementFactory.createVerticalLine
            ("vline-top", Color.black, null, 0));
    band.addElement(StaticShapeElementFactory.createVerticalLine
            ("vline-middle", Color.red, null, 250));
    band.addElement(StaticShapeElementFactory.createVerticalLine
            ("vline-bottom", Color.blue, null, 500));
    band.addElement(LabelElementFactory.createLabelElement
            ("label", new Rectangle2D.Float(0, 100, 250, 100),
                    Color.green, null, null, "Test"));

    final TestMetaBandProducer producer = new TestMetaBandProducer();

    final StrictBounds bounds = BandLayoutManagerUtil.doLayout(band, new DefaultLayoutSupport(), 500000, 250000);
    bounds.setRect(0, 0, bounds.getWidth(), bounds.getHeight());
    assertEquals(500000, bounds.getWidth());
    assertEquals(200000, bounds.getHeight());

    final long y1 = bounds.getHeight();

    final MetaBand mband = producer.createBand(band, false);

    final SheetLayout sheetLayout = new SheetLayout(true);
    sheetLayout.add(mband); // pos == 0
    sheetLayout.add(mband.getElementAt(0)); // pos == 0
    sheetLayout.add(mband.getElementAt(1));
    sheetLayout.add(mband.getElementAt(2));
    sheetLayout.add(mband.getElementAt(3));

    final StrictBounds bounds2 = BandLayoutManagerUtil.doLayout(band, new DefaultLayoutSupport(), 500000, 250000);
    bounds2.setRect(0, y1, bounds.getWidth(), bounds.getHeight());
    final MetaBand mband2 = producer.createBand(band, false);

    sheetLayout.add(mband2);
    sheetLayout.add(mband2.getElementAt(0)); // pos == 200
    // middle line is not added, the border is created by the label and
    // triggers a copy-row operation.
    sheetLayout.add(mband2.getElementAt(2)); // pos == 300
    sheetLayout.add(mband2.getElementAt(3)); // pos == 400

    assertEquals("ColumnCount", 2, sheetLayout.getColumnCount());
    assertEquals("RowCount", 4, sheetLayout.getRowCount());

    assertLeftLine(Color.black, sheetLayout.getElementAt(0, 0));
    assertLeftLine(Color.red, sheetLayout.getElementAt(0, 1));
    assertLeftLine(Color.blue, sheetLayout.getElementAt(0, 2));

    assertLeftLine(Color.black, sheetLayout.getElementAt(1, 0));
    assertLeftLine(Color.red, sheetLayout.getElementAt(1, 1));
    assertLeftLine(Color.blue, sheetLayout.getElementAt(1, 2));

    assertLeftLine(Color.black, sheetLayout.getElementAt(2, 0));
    assertLeftLine(null, sheetLayout.getElementAt(2, 1));
    assertLeftLine(Color.blue, sheetLayout.getElementAt(2, 2));

    assertLeftLine(Color.black, sheetLayout.getElementAt(3, 0));
    assertLeftLine(null, sheetLayout.getElementAt(3, 1));
    assertLeftLine(Color.blue, sheetLayout.getElementAt(3, 2));
    Log.debug ("Done");
  }

  private void assertLeftLine (final Color c, final TableCellBackground background)
  {
    if (c == null)
    {
      if (background == null)
      {
        return;
      }
      assertNull(background.getColorLeft());
    }
    else
    {
      assertEquals(c, background.getColorLeft());
      assertNotNull(background);
    }
    assertNull(background.getColorTop());
    assertNull(background.getColorBottom());
    assertNull(background.getColorRight());
    assertNull(background.getColor());
  }

  public void testCreateLineBackground ()
          throws ContentCreationException
  {
    final Band band = new Band();
    band.setMinimumSize(new FloatDimension(500, 200));

    band.addElement(StaticShapeElementFactory.createHorizontalLine
            ("hline-top", Color.black, null, 0));
    band.addElement(StaticShapeElementFactory.createHorizontalLine
            ("hline-middle", Color.red, null, 100));
    band.addElement(StaticShapeElementFactory.createHorizontalLine
            ("hline-bottom", Color.blue, null, 200));
    band.addElement(LabelElementFactory.createLabelElement
            ("label", new Rectangle2D.Float(0, 100, 500, 100),
                    Color.green, null, null, "Test"));

    final TestMetaBandProducer producer = new TestMetaBandProducer();

    final StrictBounds bounds = BandLayoutManagerUtil.doLayout(band, new DefaultLayoutSupport(), 500000, 250000);
    bounds.setRect(0, 0, bounds.getWidth(), bounds.getHeight());
    assertEquals(500000, bounds.getWidth());
    assertEquals(200000, bounds.getHeight());
    final long y1 = bounds.getHeight();

    final MetaBand mband = producer.createBand(band, false);

    final SheetLayout sheetLayout = new SheetLayout(true);
    sheetLayout.add(mband);
    sheetLayout.add(mband.getElementAt(0));
    sheetLayout.add(mband.getElementAt(1));
    sheetLayout.add(mband.getElementAt(2));
    sheetLayout.add(mband.getElementAt(3));

    final StrictBounds bounds2 = BandLayoutManagerUtil.doLayout(band, new DefaultLayoutSupport(), 500000, 250000);
    bounds2.setRect(0, y1, bounds.getWidth(), bounds.getHeight());
    final MetaBand mband2 = producer.createBand(band, false);

    sheetLayout.add(mband2);
    sheetLayout.add(mband2.getElementAt(0)); // pos == 200
    // middle line is not added, the border is created by the label and
    // triggers a copy-row operation.
    sheetLayout.add(mband2.getElementAt(2)); // pos == 300
    sheetLayout.add(mband2.getElementAt(3)); // pos == 400

    // One column inside ..
    assertEquals("ColumnCount", 1, sheetLayout.getColumnCount());
    // two columns for the first band and two for the second one ..
    // the 1st botton and 2nd top line are shared ..
    assertEquals("RowCount", 4, sheetLayout.getRowCount());

    assertTopLine(Color.black, sheetLayout.getElementAt(0, 0));
    assertTopLine(Color.red, sheetLayout.getElementAt(1, 0));
    // this is a conflicting situation. The bottom border of the
    // first band is replaced by the top border of the second band.
    assertTopLine(Color.black, sheetLayout.getElementAt(2, 0));
    assertTopLine(null, sheetLayout.getElementAt(3, 0));
    assertTopLine(Color.blue, sheetLayout.getElementAt(4, 0));
    Log.debug ("Done");
  }

  private void assertTopLine (final Color c, final TableCellBackground background)
  {
    if (c == null)
    {
      if (background == null)
      {
        return;
      }
      assertNull(background.getColorTop());
    }
    else
    {
      assertEquals(c, background.getColorTop());
      assertNotNull(background);
    }
    assertNull(background.getColorLeft());
    assertNull(background.getColorBottom());
    assertNull(background.getColorRight());
    assertNull(background.getColor());
  }

  public void testCreateAreaBackground ()
          throws ContentCreationException
  {
    final Band band = new Band();
    band.setMinimumSize(new FloatDimension(500, 200));

    band.addElement(StaticShapeElementFactory.createRectangleShapeElement
            ("rect-top", Color.black, null, new Rectangle2D.Float(0,0,-100, 100), false, true));
    band.addElement(StaticShapeElementFactory.createRectangleShapeElement
            ("rect-bottom", Color.blue, null, new Rectangle2D.Float(0,100,-100, 100), false, true));
    band.addElement(LabelElementFactory.createLabelElement
            ("label", new Rectangle2D.Float(0, 100, 500, 100),
                    Color.green, null, null, "Test"));

    final TestMetaBandProducer producer = new TestMetaBandProducer();

    final StrictBounds bounds = BandLayoutManagerUtil.doLayout(band, new DefaultLayoutSupport(), 500000, 250000);
    assertEquals(500000, bounds.getWidth());
    assertEquals(200000, bounds.getHeight());

    bounds.setRect(0, 0, bounds.getWidth(), bounds.getHeight());
    final long y1 = bounds.getHeight();

    final MetaBand mband = producer.createBand(band, false);

    final SheetLayout sheetLayout = new SheetLayout(true);
    sheetLayout.add(mband);
    sheetLayout.add(mband.getElementAt(0));
    sheetLayout.add(mband.getElementAt(1));
    sheetLayout.add(mband.getElementAt(2));

    final StrictBounds bounds2 = BandLayoutManagerUtil.doLayout(band, new DefaultLayoutSupport(), 500000, 250000);
    bounds2.setRect(0, y1, bounds.getWidth(), bounds.getHeight());
    final MetaBand mband2 = producer.createBand(band, false);

    sheetLayout.add(mband2);
    sheetLayout.add(mband2.getElementAt(0));
    sheetLayout.add(mband2.getElementAt(1));
    sheetLayout.add(mband2.getElementAt(2));

    // One column inside ..
    assertEquals("ColumnCount", 1, sheetLayout.getColumnCount());
    // two columns for the first band and two for the second one ..
    // the 1st botton and 2nd top line are shared ..
    assertEquals("RowCount", 4, sheetLayout.getRowCount());

    assertBackground(Color.black, sheetLayout.getElementAt(0,0));
    assertBackground(Color.black, sheetLayout.getElementAt(2,0));
    assertBackground(Color.blue, sheetLayout.getElementAt(1,0));
    assertBackground(Color.blue, sheetLayout.getElementAt(3,0));
  }

  private void assertBackground (final Color color, final TableCellBackground background)
  {
    assertNotNull(background);
    assertEquals(0, background.getBorderSizeTop(),0);
    assertEquals(0, background.getBorderSizeLeft(),0);
    assertEquals(0, background.getBorderSizeBottom(),0);
    assertEquals(0, background.getBorderSizeRight(),0);
    assertEquals(color, background.getColor());
  }

  public void testCombinedTable () throws Exception
  {
    final Band band = new Band();
    band.setMinimumSize(new FloatDimension(500, 200));

    band.addElement(StaticShapeElementFactory.createRectangleShapeElement
            ("rect-top", Color.yellow, null, new Rectangle2D.Float(0,0,-100, 100), false, true));
    band.addElement(StaticShapeElementFactory.createRectangleShapeElement
            ("rect-bottom", Color.orange, null, new Rectangle2D.Float(0,100,-100, 100), false, true));
    band.addElement(StaticShapeElementFactory.createHorizontalLine
            ("hline-top", Color.black, null, 0));
    band.addElement(StaticShapeElementFactory.createHorizontalLine
            ("hline-middle", Color.red, null, 100));
    band.addElement(StaticShapeElementFactory.createHorizontalLine
            ("hline-bottom", Color.blue, null, 200));
    band.addElement(StaticShapeElementFactory.createVerticalLine
            ("vline-top", Color.black, null, 0));
    band.addElement(StaticShapeElementFactory.createVerticalLine
            ("vline-middle", Color.red, null, 250));
    band.addElement(StaticShapeElementFactory.createVerticalLine
            ("vline-bottom", Color.blue, null, 500));
    band.addElement(LabelElementFactory.createLabelElement
            ("label", new Rectangle2D.Float(0, 100, 500, 100),
                    Color.green, null, null, "Test"));

    final TestMetaBandProducer producer = new TestMetaBandProducer();

    final StrictBounds bounds = BandLayoutManagerUtil.doLayout(band, new DefaultLayoutSupport(), 500000, 250000);
    assertEquals(500000, bounds.getWidth());
    assertEquals(200000, bounds.getHeight());

    bounds.setRect(0, 0, bounds.getWidth(), bounds.getHeight());
    final long y1 = bounds.getHeight();

    final MetaBand mband = producer.createBand(band, false);

    final SheetLayout sheetLayout = new SheetLayout(true);
    sheetLayout.add(mband);
    sheetLayout.add(mband.getElementAt(0));
    sheetLayout.add(mband.getElementAt(1));
    sheetLayout.add(mband.getElementAt(2));
    sheetLayout.add(mband.getElementAt(3));
    sheetLayout.add(mband.getElementAt(4));
    sheetLayout.add(mband.getElementAt(5));
    sheetLayout.add(mband.getElementAt(6));
    sheetLayout.add(mband.getElementAt(7));
    sheetLayout.add(mband.getElementAt(8));

    final StrictBounds bounds2 = BandLayoutManagerUtil.doLayout(band, new DefaultLayoutSupport(), 500000, 250000);
    bounds2.setRect(0, y1, bounds.getWidth(), bounds.getHeight());
    final MetaBand mband2 = producer.createBand(band, false);

    sheetLayout.add(mband2);
    sheetLayout.add(mband2.getElementAt(0));
    sheetLayout.add(mband2.getElementAt(1));
    sheetLayout.add(mband2.getElementAt(2));
    sheetLayout.add(mband2.getElementAt(4));
    sheetLayout.add(mband2.getElementAt(5));
    sheetLayout.add(mband2.getElementAt(7));
    sheetLayout.add(mband2.getElementAt(8));

    assertEquals("ColumnCount", 2, sheetLayout.getColumnCount());
    assertEquals("RowCount", 4, sheetLayout.getRowCount());

    // BACKGROUNDS:_______________________________________________
    // first column
    assertBackgroundLazy(Color.yellow, sheetLayout.getElementAt(0,0));
    assertBackgroundLazy(Color.orange, sheetLayout.getElementAt(1,0));
    assertBackgroundLazy(Color.yellow, sheetLayout.getElementAt(2,0));
    assertBackgroundLazy(Color.orange, sheetLayout.getElementAt(3,0));
    // second column
    assertBackgroundLazy(Color.yellow, sheetLayout.getElementAt(0,1));
    assertBackgroundLazy(Color.orange, sheetLayout.getElementAt(1,1));
    assertBackgroundLazy(Color.yellow, sheetLayout.getElementAt(2,1));
    assertBackgroundLazy(Color.orange, sheetLayout.getElementAt(3,1));
    // third column
    assertBackgroundLazy(null, sheetLayout.getElementAt(0,2));
    assertBackgroundLazy(null, sheetLayout.getElementAt(1,2));
    assertBackgroundLazy(null, sheetLayout.getElementAt(2,2));
    assertBackgroundLazy(null, sheetLayout.getElementAt(3,2));

    // HORIZONTAL LINES:__________________________________________
    // first row is black
    assertTopLineLazy(Color.black, sheetLayout.getElementAt(0, 0));
    assertTopLineLazy(Color.black, sheetLayout.getElementAt(0, 1));
    assertTopLineLazy(null, sheetLayout.getElementAt(0, 2));
    // red in 2nd row
    assertTopLineLazy(Color.red, sheetLayout.getElementAt(1, 0));
    assertTopLineLazy(Color.red, sheetLayout.getElementAt(1, 1));
    assertTopLineLazy(null, sheetLayout.getElementAt(1, 2));
    // black, as this is the top row of the second band ...
    assertTopLineLazy(Color.black, sheetLayout.getElementAt(2, 0));
    assertTopLineLazy(Color.black, sheetLayout.getElementAt(2, 1));
    assertTopLineLazy(null, sheetLayout.getElementAt(2, 2));
    // null, the middle row has never been added
    assertTopLineLazy(null, sheetLayout.getElementAt(3, 0));
    assertTopLineLazy(null, sheetLayout.getElementAt(3, 1));
    assertTopLineLazy(null, sheetLayout.getElementAt(3, 2));
    // and blue for the last row ..
    assertTopLineLazy(Color.blue, sheetLayout.getElementAt(4, 0));
    assertTopLineLazy(Color.blue, sheetLayout.getElementAt(4, 1));
    assertTopLineLazy(null, sheetLayout.getElementAt(4, 2));

    // VERTICAL LINES:__________________________________________
    assertLeftLineLazy(Color.black, sheetLayout.getElementAt(0, 0));
    assertLeftLineLazy(Color.black, sheetLayout.getElementAt(1, 0));

    assertLeftLineLazy(Color.black, sheetLayout.getElementAt(2, 0));
    assertLeftLineLazy(Color.black, sheetLayout.getElementAt(3, 0));

    assertLeftLineLazy(Color.red, sheetLayout.getElementAt(0, 1));
    assertLeftLineLazy(Color.red, sheetLayout.getElementAt(1, 1));
    assertLeftLineLazy(null, sheetLayout.getElementAt(2, 1));
    assertLeftLineLazy(null, sheetLayout.getElementAt(3, 1));

    assertLeftLineLazy(Color.blue, sheetLayout.getElementAt(0, 2));
    assertLeftLineLazy(Color.blue, sheetLayout.getElementAt(1, 2));
    assertLeftLineLazy(Color.blue, sheetLayout.getElementAt(2, 2));
    assertLeftLineLazy(Color.blue, sheetLayout.getElementAt(3, 2));
  }

  private void assertTopLineLazy (final Color c, final TableCellBackground background)
  {
    if (c == null)
    {
      if (background == null)
      {
        return;
      }
      assertNull(background.getColorTop());
    }
    else
    {
      assertEquals(c, background.getColorTop());
      assertNotNull(background);
    }
  }

  private void assertLeftLineLazy (final Color c, final TableCellBackground background)
  {
    if (c == null)
    {
      if (background == null)
      {
        return;
      }
      assertNull(background.getColorLeft());
    }
    else
    {
      assertEquals(c, background.getColorLeft());
      assertNotNull(background);
    }
  }


  private void assertBackgroundLazy (final Color color, final TableCellBackground background)
  {
    assertNotNull(background);
    assertEquals(color, background.getColor());
  }

  public void testCominationTable2 ()
          throws ContentCreationException
  {
    final Band hband = new Band();
    hband.setMinimumSize(new FloatDimension(500, 200));

    hband.addElement(StaticShapeElementFactory.createHorizontalLine
            ("hline-top", Color.black, null, 0));
    hband.addElement(StaticShapeElementFactory.createHorizontalLine
            ("hline-middle", Color.red, null, 100));
    hband.addElement(StaticShapeElementFactory.createHorizontalLine
            ("hline-bottom", Color.blue, null, 200));
    hband.addElement(LabelElementFactory.createLabelElement
            ("label", new Rectangle2D.Float(0, 100, 500, 100),
                    Color.green, null, null, "Test"));

    final Band vband = new Band();
    vband.setMinimumSize(new FloatDimension(500, 200));

    vband.addElement(StaticShapeElementFactory.createVerticalLine
            ("vline-top", Color.black, null, 0));
    vband.addElement(StaticShapeElementFactory.createVerticalLine
            ("vline-middle", Color.red, null, 250));
    vband.addElement(StaticShapeElementFactory.createVerticalLine
            ("vline-bottom", Color.blue, null, 500));
    vband.addElement(LabelElementFactory.createLabelElement
            ("label", new Rectangle2D.Float(0, 100, 250, 100),
                    Color.green, null, null, "Test"));

    final TestMetaBandProducer producer = new TestMetaBandProducer();

    final StrictBounds bounds = BandLayoutManagerUtil.doLayout(hband, new DefaultLayoutSupport(), 500000, 250000);
    bounds.setRect(0, 0, bounds.getWidth(), bounds.getHeight());
    assertEquals(500000, bounds.getWidth());
    assertEquals(200000, bounds.getHeight());
    final long y1 = bounds.getHeight();

    final MetaBand mband = producer.createBand(hband, false);

    final SheetLayout sheetLayout = new SheetLayout(true);
    sheetLayout.add(mband);
    sheetLayout.add(mband.getElementAt(0));
    sheetLayout.add(mband.getElementAt(1));
    sheetLayout.add(mband.getElementAt(2));
    sheetLayout.add(mband.getElementAt(3));

    final StrictBounds bounds2 = BandLayoutManagerUtil.doLayout(vband, new DefaultLayoutSupport(), 500000, 250000);
    bounds2.setRect(0, y1, bounds.getWidth(), bounds.getHeight());
    final MetaBand mband2 = producer.createBand(vband, false);
    sheetLayout.add(mband2);
    sheetLayout.add(mband2.getElementAt(0));
    sheetLayout.add(mband2.getElementAt(1));
    sheetLayout.add(mband2.getElementAt(2));
    sheetLayout.add(mband2.getElementAt(3));


    // One column inside ..
    assertEquals("ColumnCount", 2, sheetLayout.getColumnCount());
    // two columns for the first band and two for the second one ..
    // the 1st botton and 2nd top line are shared ..
    assertEquals("RowCount", 4, sheetLayout.getRowCount());

    assertTopLine(Color.black, sheetLayout.getElementAt(0, 0));
    assertTopLine(Color.red, sheetLayout.getElementAt(1, 0));
    // this is a conflicting situation. The bottom border of the
    // first band is replaced by the top border of the second band.
    assertTopLineLazy(Color.blue, sheetLayout.getElementAt(2, 0));

    assertTopLine(Color.black, sheetLayout.getElementAt(0, 1));
    assertTopLine(Color.red, sheetLayout.getElementAt(1, 1));
    // this is a conflicting situation. The bottom border of the
    // first band is replaced by the top border of the second band.
    assertTopLineLazy(Color.blue, sheetLayout.getElementAt(2, 1));

    assertTopLine(null, sheetLayout.getElementAt(0, 2));
    assertTopLine(null, sheetLayout.getElementAt(1, 2));
    assertTopLineLazy(null, sheetLayout.getElementAt(2, 2));

    assertLeftLineLazy(Color.black, sheetLayout.getElementAt(2, 0));
    assertLeftLineLazy(Color.red, sheetLayout.getElementAt(2, 1));
    assertLeftLineLazy(Color.blue, sheetLayout.getElementAt(2, 2));

    assertLeftLine(Color.black, sheetLayout.getElementAt(3, 0));
    assertLeftLine(Color.red, sheetLayout.getElementAt(3, 1));
    assertLeftLine(Color.blue, sheetLayout.getElementAt(3, 2));
    Log.debug ("Done");
  }

  public void testInnerRectangle ()
          throws ContentCreationException
  {
    final Band band = new Band();
    band.setMinimumSize(new FloatDimension(500, 300));

    band.addElement(StaticShapeElementFactory.createRectangleShapeElement
            ("rect-full", Color.black, null, new Rectangle2D.Float(0,0,500,300), false, true));
    band.addElement(StaticShapeElementFactory.createRectangleShapeElement
            ("rect-inner", Color.blue, null, new Rectangle2D.Float(100,100,300, 100), false, true));
    band.addElement(StaticShapeElementFactory.createRectangleShapeElement
            ("border-inner", Color.green, null, new Rectangle2D.Float(100,100,300, 100), true, false));

    final StrictBounds bounds = BandLayoutManagerUtil.doLayout(band, new DefaultLayoutSupport(), 500000, 360000);
    assertEquals(500000, bounds.getWidth());
    assertEquals(300000, bounds.getHeight());

    bounds.setRect(0, 0, bounds.getWidth(), bounds.getHeight());
  //  final long y1 = bounds.getHeight();

    final TestMetaBandProducer producer = new TestMetaBandProducer();
    final MetaBand mband = producer.createBand(band, false);

    final SheetLayout sheetLayout = new SheetLayout(true);
    sheetLayout.add(mband);
    sheetLayout.add(mband.getElementAt(0));
    sheetLayout.add(mband.getElementAt(1));
    sheetLayout.add(mband.getElementAt(2));

    // One column inside ..
    assertEquals("ColumnCount", 3, sheetLayout.getColumnCount());
    // two columns for the first band and two for the second one ..
    // the 1st bottom and 2nd top line are shared ..
    assertEquals("RowCount", 3, sheetLayout.getRowCount());

    assertBackgroundLazy(Color.black, sheetLayout.getElementAt(0,0));
    assertBackgroundLazy(Color.black, sheetLayout.getElementAt(0,1));
    assertBackgroundLazy(Color.black, sheetLayout.getElementAt(0,2));
    assertBackgroundLazy(Color.black, sheetLayout.getElementAt(1,0));
    assertBackgroundLazy(Color.blue,  sheetLayout.getElementAt(1,1));
    assertBackgroundLazy(Color.black, sheetLayout.getElementAt(1,2));
    assertBackgroundLazy(Color.black, sheetLayout.getElementAt(2,0));
    assertBackgroundLazy(Color.black, sheetLayout.getElementAt(2,1));
    assertBackgroundLazy(Color.black, sheetLayout.getElementAt(2,2));

    assertLeftLineLazy(Color.green,  sheetLayout.getElementAt(1,1));
    assertTopLineLazy(Color.green,  sheetLayout.getElementAt(1,1));
  }

  public void testCreateBorderBackground ()
          throws ContentCreationException
  {
    final Band band = new Band();
    band.setMinimumSize(new FloatDimension(500, 200));

    band.addElement(StaticShapeElementFactory.createRectangleShapeElement
            ("rect-top", Color.black, null, new Rectangle2D.Float(0,0,-100, -100), true, false));
    band.addElement(LabelElementFactory.createLabelElement
            ("label", new Rectangle2D.Float(100, 100, 400, 100),
                    Color.green, null, null, "Test"));

    final TestMetaBandProducer producer = new TestMetaBandProducer();

    final StrictBounds bounds = BandLayoutManagerUtil.doLayout(band, new DefaultLayoutSupport(), 500000, 250000);
    assertEquals(500000, bounds.getWidth());
    assertEquals(200000, bounds.getHeight());

    bounds.setRect(0, 0, bounds.getWidth(), bounds.getHeight());
    final long y1 = bounds.getHeight();

    final MetaBand mband = producer.createBand(band, false);

    final SheetLayout sheetLayout = new SheetLayout(true);
    sheetLayout.add(mband);
    sheetLayout.add(mband.getElementAt(0));
    sheetLayout.add(mband.getElementAt(1));

    assertBorder(Color.black, true, true, false, false, sheetLayout.getElementAt(0,0));
    assertBorder(Color.black, true, false, false, true, sheetLayout.getElementAt(0,1));
    assertBorder(Color.black, false, true, true, false, sheetLayout.getElementAt(1,0));
    assertBorder(Color.black, false, false, true, true, sheetLayout.getElementAt(1,1));

    final StrictBounds bounds2 = BandLayoutManagerUtil.doLayout(band, new DefaultLayoutSupport(), 500000, 250000);
    bounds2.setRect(0, y1, bounds.getWidth(), bounds.getHeight());
    final MetaBand mband2 = producer.createBand(band, false);

    sheetLayout.add(mband2);
    assertBorder(Color.black, true, true, false, false, sheetLayout.getElementAt(0,0));
    assertBorder(Color.black, true, false, false, true, sheetLayout.getElementAt(0,1));
    assertBorder(Color.black, false, true, true, false, sheetLayout.getElementAt(1,0));
    assertBorder(Color.black, false, false, true, true, sheetLayout.getElementAt(1,1));
    assertBorder(null, false, false, false, false, sheetLayout.getElementAt(2,0));
    assertBorder(null, false, false, false, false, sheetLayout.getElementAt(2,1));
    sheetLayout.add(mband2.getElementAt(0));
    assertBorder(Color.black, true, true, true, false, sheetLayout.getElementAt(2,0));
    assertBorder(Color.black, true, false, true, true, sheetLayout.getElementAt(2,1));
    sheetLayout.add(mband2.getElementAt(1));

    // One column inside ..
    assertEquals("ColumnCount", 2, sheetLayout.getColumnCount());
    // two columns for the first band and two for the second one ..
    // the 1st bottom and 2nd top line are shared ..
    assertEquals("RowCount", 4, sheetLayout.getRowCount());

    assertBorder(Color.black, true, true, false, false, sheetLayout.getElementAt(0,0));
    assertBorder(Color.black, true, false, false, true, sheetLayout.getElementAt(0,1));
    assertBorder(Color.black, false, true, true, false, sheetLayout.getElementAt(1,0));
    assertBorder(Color.black, false, false, true, true, sheetLayout.getElementAt(1,1));
    assertBorder(Color.black, true, true, false, false, sheetLayout.getElementAt(2,0));
    assertBorder(Color.black, true, false, false, true, sheetLayout.getElementAt(2,1));
    assertBorder(Color.black, false, true, true, false, sheetLayout.getElementAt(3,0));
    assertBorder(Color.black, false, false, true, true, sheetLayout.getElementAt(3,1));
  }

  private void assertBorder (final Color c,
                             final boolean top, final boolean left,
                             final boolean bottom, final boolean right,
                             final TableCellBackground background)
  {
    if (background == null)
    {
      assertFalse("No border defined", top || left || bottom || right);
      assertNull("Color defined", c);
      return;
    }

    assertNull(background.getColor());
    if (top)
    {
      assertEquals(c, background.getColorTop());
      assertTrue(background.getBorderSizeTop() != 0);
    }
    else
    {
      assertNull(background.getColorTop());
    }
    if (bottom)
    {
      assertEquals(c, background.getColorBottom());
      assertTrue( background.getBorderSizeBottom() != 0);
    }
    else
    {
      assertNull(background.getColorBottom());
    }
    if (left)
    {
      assertEquals(c, background.getColorLeft());
      assertTrue(background.getBorderSizeLeft() != 0);
    }
    else
    {
      assertNull(background.getColorLeft());
    }
    if (right)
    {
      assertEquals(c, background.getColorRight());
      assertTrue(background.getBorderSizeRight() != 0);
    }
    else
    {
      assertNull(background.getColorRight());
    }

  }
}
