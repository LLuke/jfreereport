/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Simba Management Limited and Contributors.
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
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * Mar 15, 2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.xls;

import java.awt.geom.Rectangle2D;
import java.text.ParseException;
import java.util.Date;

import org.jfree.report.Element;
import org.jfree.report.filter.DataSource;
import org.jfree.report.filter.templates.DateFieldTemplate;
import org.jfree.report.filter.templates.NumberFieldTemplate;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.modules.output.table.base.RawContent;
import org.jfree.report.modules.output.table.base.TableMetaBandProducer;
import org.jfree.report.modules.output.table.xls.metaelements.ExcelNumberMetaElement;
import org.jfree.report.modules.output.table.xls.metaelements.ExcelDateMetaElement;
import org.jfree.report.modules.output.table.xls.metaelements.ExcelMetaElement;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.Log;

public class ExcelMetaBandProducer
        extends TableMetaBandProducer
{
  private boolean defineDataFormats;

  public ExcelMetaBandProducer (final boolean defineDataFormats)
  {
    super(new DefaultLayoutSupport());
    this.defineDataFormats = defineDataFormats;
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

  /**
   * The Excel-Target does not support drawable content.
   *
   * @param e
   * @param x
   * @param y
   * @return
   */
  protected MetaElement createDrawableCell (final Element e,
                                            final float x, final float y)
  {
    return null;
  }

  /**
   * The Excel-Target does not support image content.
   *
   * @param e
   * @param x
   * @param y
   * @return
   */
  protected MetaElement createImageCell (final Element e,
                                         final float x, final float y)
  {
    return null;
  }

  protected MetaElement createTextCell (final Element e,
                                        final float x, final float y)
  {
    final Object o = e.getValue();
    if (o instanceof String == false)
    {
      return null;
    }

    try
    {
      final DataSource template = e.getDataSource();
      if (template instanceof DateFieldTemplate)
      {
        return createDateCell(e, x ,y);
      }
      else if (template instanceof NumberFieldTemplate)
      {
        return createNumberCell(e, x, y);
      }
    }
    catch (ParseException pe)
    {
      Log.debug("Unable to create extended format cells:", pe);
    }

    final Rectangle2D rect = (Rectangle2D)
            e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);

    return new ExcelMetaElement(new RawContent(rect, o),
            createStyleForTextElement(e, x, y));
  }

  private MetaElement createNumberCell (final Element e, final float x, final float y)
          throws ParseException
  {
    final NumberFieldTemplate nft = (NumberFieldTemplate) e.getDataSource();
    final Number number = nft.getDecimalFormat().parse((String) nft.getValue());

    final Rectangle2D rect = (Rectangle2D)
            e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
    final ElementStyleSheet styleSheet =
            createStyleForTextElement(e, x, y);
    styleSheet.setStyleProperty(ExcelProcessor.DATA_FORMAT_STRING,
            e.getStyle().getStyleProperty (ExcelProcessor.DATA_FORMAT_STRING,
                    nft.getFormat()));

    return new ExcelNumberMetaElement(new RawContent(rect, number), styleSheet);
  }

  private MetaElement createDateCell (final Element e, final float x, final float y)
          throws ParseException
  {
    final DateFieldTemplate dft = (DateFieldTemplate) e.getDataSource();
    final String value = (String) dft.getValue();
    final Date date = dft.getDateFormat().parse(value);

    final Rectangle2D rect = (Rectangle2D)
            e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
    final ElementStyleSheet styleSheet =
            createStyleForTextElement(e, x, y);
    styleSheet.setStyleProperty(ExcelProcessor.DATA_FORMAT_STRING,
            e.getStyle().getStyleProperty (ExcelProcessor.DATA_FORMAT_STRING,
                    dft.getFormat()));

    return new ExcelDateMetaElement(new RawContent(rect, date), styleSheet);
  }
}
