/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
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
 * ExcelMetaBandProducer.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ExcelMetaBandProducer.java,v 1.10 2005/10/05 13:35:40 taqua Exp $
 *
 * Changes 
 * -------------------------
 * Mar 15, 2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.xls;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jfree.report.DefaultImageReference;
import org.jfree.report.Element;
import org.jfree.report.content.AnchorContentFactoryModule;
import org.jfree.report.content.Content;
import org.jfree.report.content.ContentCreationException;
import org.jfree.report.content.ContentFactory;
import org.jfree.report.content.DefaultContentFactory;
import org.jfree.report.content.EmptyContent;
import org.jfree.report.content.ImageContent;
import org.jfree.report.content.ShapeContentFactoryModule;
import org.jfree.report.content.TextContentFactoryModule;
import org.jfree.report.content.DrawableContentFactoryModule;
import org.jfree.report.content.ImageContentFactoryModule;
import org.jfree.report.filter.DataSource;
import org.jfree.report.filter.DataTarget;
import org.jfree.report.filter.DateFormatFilter;
import org.jfree.report.filter.NumberFormatFilter;
import org.jfree.report.filter.RawDataSource;
import org.jfree.report.filter.templates.DateFieldTemplate;
import org.jfree.report.filter.templates.NumberFieldTemplate;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.modules.output.table.base.RawContent;
import org.jfree.report.modules.output.table.base.TableMetaBandProducer;
import org.jfree.report.modules.output.table.xls.metaelements.ExcelDateMetaElement;
import org.jfree.report.modules.output.table.xls.metaelements.ExcelImageElement;
import org.jfree.report.modules.output.table.xls.metaelements.ExcelMetaElement;
import org.jfree.report.modules.output.table.xls.metaelements.ExcelNumberMetaElement;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.ElementLayoutInformation;
import org.jfree.report.util.ImageUtils;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.ui.Drawable;
import org.jfree.util.Log;

public class ExcelMetaBandProducer
        extends TableMetaBandProducer
{
  private boolean defineDataFormats;

  public ExcelMetaBandProducer (final boolean defineDataFormats)
  {
    super(new DefaultLayoutSupport(createExcelContentFactory()));
    this.defineDataFormats = defineDataFormats;
  }

  private static ContentFactory createExcelContentFactory()
  {
    final DefaultContentFactory contentFactory = new DefaultContentFactory();
    contentFactory.addModule(new TextContentFactoryModule());
    contentFactory.addModule(new ShapeContentFactoryModule());
    contentFactory.addModule(new AnchorContentFactoryModule());
    contentFactory.addModule(new DrawableContentFactoryModule());
    contentFactory.addModule(new ImageContentFactoryModule());
    return contentFactory;
  }

  /**
   * Defines whether to map java objects into excel extended cell formats. This feature
   * can be used to create numeric and date cells in the excel sheet, but the mapping may
   * contain errors.
   * <p/>
   * We try to directly map the java.text.SimpleDateFormat and java.text.DecimalFormat
   * into their excel counter parts and hope that everything works fine. If not, you will
   * have to adjust the format afterwards.
   *
   * @return true if cells should contain a custom data format for numeric or date cells
   *         or false when all cells should contain strings.
   */
  public boolean isDefineDataFormats ()
  {
    return defineDataFormats;
  }


  protected MetaElement createDrawableCell
          (final Element e, final long x, final long y)
  {
    final Object o = e.getValue();
    if (o instanceof Drawable == false)
    {
      return null;
    }

    final Drawable drawable = (Drawable) o;
    final StrictBounds rect = (StrictBounds)
            e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);

    final int imageWidth = (int) StrictGeomUtility.toExternalValue(rect.getWidth());
    final int imageHeight = (int) StrictGeomUtility.toExternalValue(rect.getHeight());

    if (imageWidth == 0 && imageHeight == 0)
    {
      return null;
    }
    final Image image = ImageUtils.createTransparentImage(imageWidth, imageHeight);
    final Graphics2D g2 = (Graphics2D) image.getGraphics();
    // the clipping bounds are a sub-area of the whole drawable
    // we only want to print a certain area ...

    drawable.draw(g2, new Rectangle2D.Double(0, 0, imageWidth, imageHeight));
    g2.dispose();
    final DefaultImageReference imgref;
    try
    {
      imgref = new DefaultImageReference(image);
    }
    catch (IOException e1)
    {
      Log.warn ("Unable to fully load a given image. (It should not happen here.)");
      return null;
    }
    final ImageContent ic = new ImageContent(imgref, (StrictBounds) rect.clone());
    final ExcelImageElement me = new ExcelImageElement
            (new RawContent(rect, ic.getContent()),
                    // we have to use the text style here, for this cheap hack.
                    // this code will be removed in the next,
                    // libLayout-enabled version anyway
                    createStyleForTextElement(e, x, y));
    me.setName(e.getName());
    return me;
  }

  protected MetaElement createImageCell
          (final Element e, final long x, final long y)
  {
    final ContentFactory contentFactory = getLayoutSupport().getContentFactory();
    if (contentFactory.canHandleContent(e.getContentType()) == false)
    {
      return null;
    }

    final StrictBounds rect = (StrictBounds)
            e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
    if (rect.getWidth() == 0 || rect.getHeight() == 0)
    {
      // remove emtpy images.
      return null;
    }

    try
    {
      final ElementLayoutInformation eli = new ElementLayoutInformation(rect);
      final Content content =
              contentFactory.createContentForElement(e, eli, getLayoutSupport());
      if (EmptyContent.getDefaultEmptyContent().equals(content))
      {
        return null;
      }
      final ImageContent ic = (ImageContent) content;
      final ExcelImageElement me = new ExcelImageElement
              (new RawContent(rect, ic.getContent()),
                      createStyleForTextElement(e, x, y));
      me.setName(e.getName());
      return me;
    }
    catch(ContentCreationException cce)
    {
      return null;
    }
  }

  protected MetaElement createTextCell (final Element e,
                                        final long x, final long y)
  {
    final Object o = e.getValue();
    if (o == null)
    {
      return null;
    }

    final DataSource template = e.getDataSource();
    if (template instanceof RawDataSource)
    {
      final RawDataSource rawSource = (RawDataSource) template;
      final Object rawValue = rawSource.getRawValue();
      if (rawValue instanceof Date)
      {
        return createDateCell(e, x, y);
      }
      else if (rawValue instanceof Number)
      {
        return createNumberCell(e, x, y);
      }
    }

    final StrictBounds rect = (StrictBounds)
            e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);

    final MetaElement me = new ExcelMetaElement(new RawContent(rect, String.valueOf(o)),
            createStyleForTextElement(e, x, y));
    me.setName(e.getName());
    return me;
  }

  private String getFormatString (final DataSource ds)
  {
    if (ds instanceof NumberFieldTemplate)
    {
      final NumberFieldTemplate nft = (NumberFieldTemplate) ds;
      return nft.getFormat();
    }
    else if (ds instanceof DateFieldTemplate)
    {
      final DateFieldTemplate dft = (DateFieldTemplate) ds;
      return dft.getFormat();
    }
    else if (ds instanceof DateFormatFilter)
    {
      final DateFormatFilter filter = (DateFormatFilter) ds;
      if (filter.getDateFormat() instanceof SimpleDateFormat)
      {
        final SimpleDateFormat dateFormat = (SimpleDateFormat) filter.getDateFormat();
        return dateFormat.toLocalizedPattern();
      }
    }
    else if (ds instanceof NumberFormatFilter)
    {
      final NumberFormatFilter filter = (NumberFormatFilter) ds;
      if (filter.getNumberFormat() instanceof DecimalFormat)
      {
        final DecimalFormat dateFormat = (DecimalFormat) filter.getNumberFormat();
        return dateFormat.toLocalizedPattern();
      }
    }
    if (ds instanceof DataTarget)
    {
      final DataTarget dt = (DataTarget) ds;
      return getFormatString(dt.getDataSource());
    }
    return null;
  }

  private MetaElement createNumberCell (final Element e, final long x, final long y)
  {
    final RawDataSource nft = (RawDataSource) e.getDataSource();
    final Number number = (Number) nft.getRawValue();


    final StrictBounds rect = (StrictBounds)
            e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
    final ElementStyleSheet styleSheet =
            createStyleForTextElement(e, x, y);
    styleSheet.setStyleProperty(ElementStyleSheet.EXCEL_DATA_FORMAT_STRING,
            e.getStyle().getStyleProperty(ElementStyleSheet.EXCEL_DATA_FORMAT_STRING,
                    getFormatString(nft)));

    return new ExcelNumberMetaElement(new RawContent(rect, number), styleSheet);
  }

  private MetaElement createDateCell (final Element e, final long x, final long y)
  {
    final RawDataSource dft = (RawDataSource) e.getDataSource();
    final Date date = (Date) dft.getRawValue();

    final StrictBounds rect = (StrictBounds)
            e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
    final ElementStyleSheet styleSheet =
            createStyleForTextElement(e, x, y);
    styleSheet.setStyleProperty(ElementStyleSheet.EXCEL_DATA_FORMAT_STRING,
            e.getStyle().getStyleProperty(ElementStyleSheet.EXCEL_DATA_FORMAT_STRING,
                    getFormatString(dft)));

    return new ExcelDateMetaElement(new RawContent(rect, date), styleSheet);
  }
}
