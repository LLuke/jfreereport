/**
 * Date: Jan 31, 2003
 * Time: 4:24:01 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.barcode.filter;

import com.jrefinery.report.ext.barcode.Barcode;
import com.jrefinery.report.filter.DataFilter;
import com.jrefinery.report.filter.DataSource;

import java.awt.Color;
import java.awt.Image;

public class BarcodeFilter implements DataFilter
{
  private Barcode barcode;
  private DataSource dataSource;
  private Color barColor;
  private Color textColor;

  public BarcodeFilter()
  {
    barColor = Color.black;
    textColor = Color.black;
  }

  public Color getBarColor()
  {
    return barColor;
  }

  public Color getTextColor()
  {
    return textColor;
  }

  public void setBarColor(Color barColor)
  {
    if (barColor == null) throw new NullPointerException();
    this.barColor = barColor;
  }

  public void setTextColor(Color textColor)
  {
    if (textColor == null) throw new NullPointerException();
    this.textColor = textColor;
  }

  public Barcode getBarcode()
  {
    return barcode;
  }

  public void setBarcode(Barcode barcode)
  {
    if (barcode == null) throw new NullPointerException();
    try
    {
      this.barcode = (Barcode) barcode.clone();
    }
    catch (Exception e)
    {
      throw new IllegalArgumentException("Clone not supported.");
    }
  }

  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue()
  {
    if (dataSource == null) return null;
    Object o = dataSource.getValue();
    if (o instanceof String == false)
    {
      // only accept strings ...
      return null;
    }

    String code = (String) o;
    Image image = null;
    synchronized (barcode)
    {
      barcode.setCode(code);
      image = barcode.createImageWithBarcode(getBarColor(), getTextColor());
    }
    return image;
  }

  /**
   * Clones this <code>DataSource</code>.
   *
   * @return the clone.
   *
   * @throws java.lang.CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    BarcodeFilter filter = (BarcodeFilter) super.clone();
    filter.barcode = (Barcode) barcode.clone();
    filter.dataSource = (DataSource) dataSource.clone();
    return filter;
  }

  /**
   * Returns the assigned DataSource for this Target.
   *
   * @return The datasource.
   */
  public DataSource getDataSource()
  {
    return dataSource;
  }

  /**
   * Assigns a DataSource for this Target.
   *
   * @param ds The data source.
   */
  public void setDataSource(DataSource ds)
  {
    this.dataSource = ds;
  }
}
