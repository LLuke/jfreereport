/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * -------------------------
 * ExcelCellDataFactory.java
 * -------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Heiko Evermann
 * Contributor(s):   Thomas Morgner; David Gilbert (for Simba Management Limited);
 *
 * $Id: ExcelCellDataFactory.java,v 1.3 2003/08/24 15:06:10 taqua Exp $
 *
 * Changes
 * -------
 * 15-Jan-2003 : Initial version
 * 23-May-2003 : Enabled configurable enhanced POI cell formats.
 */
package org.jfree.report.modules.output.table.xls;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.text.ParseException;
import java.util.Date;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.filter.DataSource;
import org.jfree.report.filter.templates.DateFieldTemplate;
import org.jfree.report.filter.templates.NumberFieldTemplate;
import org.jfree.report.filter.templates.Template;
import org.jfree.report.modules.output.table.base.AbstractTableCellDataFactory;
import org.jfree.report.modules.output.table.base.TableCellData;
import org.jfree.report.util.Log;

/**
 * The cell data factory is responsible for converting elements into
 * excel cell data. The element style is converted using an external
 * style factory. This factory reuses previously defined styles if
 * possible, to increase the file creating efficiency.
 *
 * @author Heiko Evermann
 */
public class ExcelCellDataFactory extends AbstractTableCellDataFactory
{
  /** the style factory, which is used to create the excel styles. */
  private ExcelCellStyleFactory styleFactory;

  /** A flag defining whether to use excel data formats (POI 1.10). */
  private boolean defineDataFormats;

  /**
   * Creates a new ExcelCellDataFactory.
   *
   * @param styleFactory the stylefactory for creating the cell styles.
   */
  public ExcelCellDataFactory(final ExcelCellStyleFactory styleFactory)
  {
    if (styleFactory == null)
    {
      throw new NullPointerException();
    }
    this.styleFactory = styleFactory;
    defineDataFormats = false;
  }

  /**
   * Defines whether to map java objects into excel extended cell formats. This
   * feature can be used to create numeric and date cells in the excel sheet,
   * but the mapping may contain errors.
   * <p>
   * We try to directly map the java.text.SimpleDateFormat and java.text.DecimalFormat
   * into their excel counter parts and hope that everything works fine. If not,
   * you will have to adjust the format afterwards.
   *
   * @return true if cells should contain a custom data format for numeric or date
   * cells or false when all cells should contain strings.
   */
  public boolean isDefineDataFormats()
  {
    return defineDataFormats;
  }

  /**
   * Defines whether to map java objects into excel extended cell formats. This
   * feature can be used to create numeric and date cells in the excel sheet,
   * but the mapping may contain errors.
   * <p>
   * We try to directly map the java.text.SimpleDateFormat and java.text.DecimalFormat
   * into their excel counter parts and hope that everything works fine. If not,
   * you will have to adjust the format afterwards.
   *
   * @param defineDataFormats set to true if cells should contain a custom data
   * format for numeric or date cells or false when all cells should contain strings.
   */
  public void setDefineDataFormats(final boolean defineDataFormats)
  {
    this.defineDataFormats = defineDataFormats;
  }

  /**
   * Gets the style factory, which should be used in this factory.
   *
   * @return the style factory, never null.
   */
  public ExcelCellStyleFactory getStyleFactory()
  {
    return styleFactory;
  }

  /**
   * Creates the TableCellData for the given Element. The generated CellData
   * should contain copies of all needed element attributes, as the element instance
   * will be reused in the later report processing.
   * <p>
   * If the tablemodel does not support the element type, return null.
   * <p>
   * If the data factory is configured to return data formats, this method will
   * create advanced cell format strings based on the template system of JFreeReport.
   *
   * @param element the element that should be converted into TableCellData.
   * @param bounds the elements bounds within the table. The bounds are specified
   * in points.
   * @return null if element type is not supported or the generated TableCellData object.
   */
  public TableCellData createCellData(final Element element, final Rectangle2D bounds)
  {
    if (element.isVisible() == false)
    {
      return null;
    }

    if (element instanceof Band)
    {
      return createBandCell(bounds);
    }

    /**
     * POI 1.9 has support for more formats ...
     */

    final DataSource ds = element.getDataSource();
    if (isDefineDataFormats() && ds instanceof Template)
    {
      final TableCellData retval = handleTemplate((Template) ds, element, bounds);
      if (retval != null)
      {
        return retval;
      }
    }

    // fallback, no template or unknown template
    return handleFormats(element, bounds);
  }

  /**
   * A generic content creation method, which translates shapes and strings.
   * All other content is ignored. Suitable Shapes get transformed into
   * background information, and strings are used to fill the cells with the
   * report data.
   * <p>
   * Advanced formating can be done by using template handling methods. This
   * requires POI 2.0
   *
   * @param e the element that should be converted into data or backgrounds.
   * @param bounds the layouted bounds of the element.
   *
   * @return the generated data or null, if the data is not handled by this
   * implementation.
   */
  private TableCellData handleFormats(final Element e, final Rectangle2D bounds)
  {
    ExcelCellData retval = null;

    final Object value = e.getValue();
    if ((value != null) && (value instanceof String))
    {
      final ExcelDataCellStyle style = styleFactory.getExcelDataCellStyle(e);
      final String svalue = String.valueOf(e.getValue());
      retval = new DefaultExcelCellData(bounds, style, svalue);
    }
    else if (value instanceof Shape)
    {
      return createBackground(e, (Shape) value, bounds);
    }
    return retval;
  }

  /**
   * Converts a template element into a excel data cell. Date and
   * number templates can be handled using this implementation.
   * <p>
   * Uses POI 2.0 functions, so this method is not active for now ...
   *
   * @param template the template that was used to create the content for the element.
   * @param e the element which contained the template.
   * @param bounds the layouted element bounds.
   * @return the generated content or null, if the content is not handled by
   * this implementation.
   */
  private ExcelCellData handleTemplate(final Template template, final Element e,
                                       final Rectangle2D bounds)
  {

    /**
     * DataFormats will need at least POI 1.9, we are currently using the
     * latest stable version 1.5.1, so we have to wait ...
     */

    if (template instanceof DateFieldTemplate)
    {
      try
      {
        final DateFieldTemplate dft = (DateFieldTemplate) template;
        final String value = (String) dft.getValue();
        final Date date = dft.getDateFormat().parse(value);
        final String format = (String) e.getStyle().getStyleProperty
            (ExcelProcessor.DATA_FORMAT_STRING, dft.getFormat());

        final ExcelDataCellStyle style = styleFactory.getExcelDataCellStyle(e, format);
        return new DateExcelCellData(bounds, style, date);
      }
      catch (ParseException pe)
      {
        Log.debug("Unable to restore date:", pe);
      }
    }
    else if (template instanceof NumberFieldTemplate)
    {
      try
      {
        final NumberFieldTemplate nft = (NumberFieldTemplate) template;
        final String value = (String) nft.getValue();
        final Number number = nft.getDecimalFormat().parse(value);
        final String format = (String) e.getStyle().getStyleProperty
            (ExcelProcessor.DATA_FORMAT_STRING, nft.getFormat());

        final ExcelDataCellStyle style = styleFactory.getExcelDataCellStyle(e, format);
        return new NumericExcelCellData(bounds, style, number);
      }
      catch (ParseException pe)
      {
        Log.debug("Unable to restore number:", pe);
      }
    }
    return null;
  }

}
