/**
 * Date: Jan 15, 2003
 * Time: 5:01:29 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.excel;

import com.jrefinery.report.Element;
import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.templates.Template;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import java.awt.geom.Rectangle2D;

public class ExcelCellDataFactory
{
  private ExcelCellStyleFactory styleFactory;

  public ExcelCellDataFactory(ExcelCellStyleFactory styleFactory)
  {
    this.styleFactory = styleFactory;
  }

  public ExcelCellData createCellData (Element element, Rectangle2D bounds)
  {
    DataSource ds = element.getDataSource();
    if (ds instanceof Template)
    {
      return handleTemplate((Template) ds, element, bounds);
    }
    else
    {
      return handleFormats (element ,bounds);
    }
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
    String value = String.valueOf(e.getValue());
    return new DefaultExcelCellData (bounds, style, value);
  }

  private ExcelCellData handleTemplate (Template template, Element e, Rectangle2D bounds)
  {
    HSSFCellStyle style = styleFactory.getExcelCellStyle(e);
    /**
     * DataFormats will need at least POI 1.9, we are currently using the
     * latest stable version 1.5.1, so we have to wait ...
     */
    /*
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
    */
    String value = String.valueOf(e.getValue());
    return new DefaultExcelCellData (bounds, style, value);
  }

}
