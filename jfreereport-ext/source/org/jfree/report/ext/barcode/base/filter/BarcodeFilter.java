/**
 * Date: Jan 31, 2003
 * Time: 4:24:01 PM
 *
 * $Id: BarcodeFilter.java,v 1.2 2003/07/03 16:06:17 taqua Exp $
 */
package org.jfree.report.ext.barcode.base.filter;

import org.jfree.report.ext.barcode.base.Barcode;
import org.jfree.report.filter.DataFilter;
import org.jfree.report.filter.DataSource;

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

  public void setBarColor(final Color barColor)
  {
    if (barColor == null) throw new NullPointerException();
    this.barColor = barColor;
  }

  public void setTextColor(final Color textColor)
  {
    if (textColor == null) throw new NullPointerException();
    this.textColor = textColor;
  }

  public Barcode getBarcode()
  {
    return barcode;
  }

  public void setBarcode(final Barcode barcode)
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
    final Object o = dataSource.getValue();
    if (o instanceof String == false)
    {
      // only accept strings ...
      return null;
    }

    final String code = (String) o;
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
    final BarcodeFilter filter = (BarcodeFilter) super.clone();
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
  public void setDataSource(final DataSource ds)
  {
    this.dataSource = ds;
  }
}
