/**
 * Date: Jan 15, 2003
 * Time: 5:01:29 PM
 *
 * $Id: ExcelCellDataFactory.java,v 1.3 2003/01/25 20:34:12 taqua Exp $
 */
package com.jrefinery.report.targets.table.excel;

import com.jrefinery.report.Element;
import com.jrefinery.report.Band;
import com.jrefinery.report.filter.templates.DateFieldTemplate;
import com.jrefinery.report.filter.templates.NumberFieldTemplate;
import com.jrefinery.report.filter.templates.Template;
import com.jrefinery.report.targets.table.AbstractTableCellDataFactory;
import com.jrefinery.report.targets.table.TableCellData;
import com.jrefinery.report.util.Log;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.text.ParseException;
import java.util.Date;

public class ExcelCellDataFactory extends AbstractTableCellDataFactory
{
  private ExcelCellStyleFactory styleFactory;

  public ExcelCellDataFactory(ExcelCellStyleFactory styleFactory)
  {
    this.styleFactory = styleFactory;
  }

  public ExcelCellStyleFactory getStyleFactory()
  {
    return styleFactory;
  }

  public TableCellData createEmptyData ()
  {
    return new EmptyExcelCellData(new Rectangle2D.Float(), new ExcelDataCellStyle());
  }

  public TableCellData createCellData (Element element, Rectangle2D bounds)
  {
    if (element.isVisible() == false)
    {
      return null;
    }

    if (element instanceof Band)
    {
      return createBandCell(element, bounds);
    }

    /**
     * POI 1.9 has support for more formats ...
     */
    /*
    DataSource ds = element.getDataSource();
    if (ds instanceof Template)
    {
      TableCellData retval = handleTemplate((Template) ds, element, bounds);
      if (retval != null)
        return retval;
    }
    */
    // fallback, no template or unknown template
    return handleFormats (element ,bounds);
  }

  /**
   * Todo A heuristic to detect and use the filters ..
   * @param e
   * @param bounds
   * @return
   */
  private TableCellData handleFormats (Element e, Rectangle2D bounds)
  {
    ExcelCellData retval = null;

    Object value = e.getValue();
    if ((value != null) && (value instanceof String))
    {
      ExcelDataCellStyle style = styleFactory.getExcelDataCellStyle(e);
      String svalue = String.valueOf(e.getValue());
      retval = new DefaultExcelCellData (bounds, style, svalue);
    }
    else if (value instanceof Shape)
    {
      return createBackground(e, (Shape) value, bounds);
    }
    return retval;
  }

  /**
   * Used when POI 2.0 is available ...
   *
   * @param template
   * @param e
   * @param bounds
   * @return
   */
  private ExcelCellData handleTemplate (Template template, Element e, Rectangle2D bounds)
  {
    ExcelDataCellStyle style = styleFactory.getExcelDataCellStyle(e);
    /**
     * DataFormats will need at least POI 1.9, we are currently using the
     * latest stable version 1.5.1, so we have to wait ...
     */

    if (template instanceof DateFieldTemplate)
    {
      try
      {
        DateFieldTemplate dft = (DateFieldTemplate) template;
        String value = (String) dft.getValue();
        Date date = dft.getDateFormat().parse(value);
        return new DateExcelCellData(bounds, style, date, dft.getFormat());
      }
      catch (ParseException pe)
      {
        Log.debug ("Unable to restore date:", pe);
      }
    }
    else if (template instanceof NumberFieldTemplate)
    {
      try
      {
        NumberFieldTemplate nft = (NumberFieldTemplate) template;
        String value = (String) nft.getValue();
        Number number = nft.getDecimalFormat().parse(value);
        return new NumericExcelCellData(bounds, style, number, nft.getFormat());
      }
      catch (ParseException pe)
      {
        Log.debug ("Unable to restore date:", pe);
      }
    }
    return null;
  }

}
