/**
 * Date: Jan 15, 2003
 * Time: 5:01:29 PM
 *
 * $Id: ExcelCellDataFactory.java,v 1.1 2003/01/18 20:47:36 taqua Exp $
 */
package com.jrefinery.report.targets.table.excel;

import com.jrefinery.report.Element;
import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.templates.DateFieldTemplate;
import com.jrefinery.report.filter.templates.NumberFieldTemplate;
import com.jrefinery.report.filter.templates.Template;
import com.jrefinery.report.targets.table.TableCellData;
import com.jrefinery.report.targets.table.TableCellDataFactory;
import com.jrefinery.report.util.Log;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import java.awt.geom.Rectangle2D;
import java.text.ParseException;
import java.util.Date;

public class ExcelCellDataFactory implements TableCellDataFactory
{
  private ExcelCellStyleFactory styleFactory;

  public ExcelCellDataFactory(ExcelCellStyleFactory styleFactory)
  {
    this.styleFactory = styleFactory;
  }

  public TableCellData createCellData (Element element, Rectangle2D bounds)
  {
    DataSource ds = element.getDataSource();
    if (ds instanceof Template)
    {
      TableCellData retval = handleTemplate((Template) ds, element, bounds);
      if (retval != null)
        return retval;
    }

    // fallback, no template or unknown template
    return handleFormats (element ,bounds);
  }

  /**
   * Todo A heuristic to detect and use the filters ..
   * @param e
   * @param bounds
   * @return
   */
  private ExcelCellData handleFormats (Element e, Rectangle2D bounds)
  {
    HSSFCellStyle style = styleFactory.getExcelCellStyle(e);
    Object value = e.getValue();
    if ((value != null) && (value instanceof String))
    {
      String svalue = String.valueOf(e.getValue());
      return new DefaultExcelCellData (bounds, style, svalue);
    }
    else
    {
      return new DefaultExcelCellData (bounds, style, "");
    }
  }

  private ExcelCellData handleTemplate (Template template, Element e, Rectangle2D bounds)
  {
    HSSFCellStyle style = styleFactory.getExcelCellStyle(e);
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
