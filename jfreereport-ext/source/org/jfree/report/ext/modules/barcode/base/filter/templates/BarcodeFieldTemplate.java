/**
 * Date: Jan 31, 2003
 * Time: 5:17:05 PM
 *
 * $Id: BarcodeFieldTemplate.java,v 1.1 2003/07/11 20:05:37 taqua Exp $
 */
package org.jfree.report.ext.modules.barcode.base.filter.templates;

import java.awt.Color;

import org.jfree.report.DataRow;
import org.jfree.report.ElementAlignment;
import org.jfree.report.ext.modules.barcode.base.content.Barcode;
import org.jfree.report.ext.modules.barcode.base.filter.BarcodeFilter;
import org.jfree.report.filter.DataRowConnectable;
import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.ImageRefFilter;
import org.jfree.report.filter.templates.AbstractTemplate;
import org.jfree.report.style.FontDefinition;

public abstract class BarcodeFieldTemplate extends AbstractTemplate
    implements DataRowConnectable
{
  private DataRowDataSource dataRowDataSource;
  private ImageRefFilter imageRefFilter;
  private BarcodeFilter barcodeFilter;

  public BarcodeFieldTemplate()
  {
    dataRowDataSource = new DataRowDataSource();
    barcodeFilter = new BarcodeFilter();
    barcodeFilter.setDataSource(dataRowDataSource);
    barcodeFilter.setBarcode(createBarcode());
    imageRefFilter = new ImageRefFilter();
    imageRefFilter.setDataSource(barcodeFilter);
  }

  protected abstract Barcode createBarcode ();

  protected BarcodeFilter getBarcodeFilter ()
  {
    return barcodeFilter;
  }

  public Color getTextColor()
  {
    return barcodeFilter.getTextColor();
  }

  public void setTextColor(final Color textColor)
  {
    barcodeFilter.setTextColor(textColor);
  }

  public Color getBarColor()
  {
    return barcodeFilter.getBarColor();
  }

  public void setBarColor(final Color barColor)
  {
    barcodeFilter.setBarColor(barColor);
  }

  public String getField()
  {
    return dataRowDataSource.getDataSourceColumnName();
  }

  public void setField(final String field)
  {
    dataRowDataSource.setDataSourceColumnName(field);
  }

  public float getMinWidth()
  {
    return barcodeFilter.getBarcode().getMinWidth();
  }

  public void setMinWidth(final float minWidth)
  {
    barcodeFilter.getBarcode().setMinWidth(minWidth);
  }

  public FontDefinition getFont()
  {
    return barcodeFilter.getBarcode().getFont();
  }

  public void setFont(final FontDefinition font)
  {
    barcodeFilter.getBarcode().setFont(font);
  }

  public float getBaseline()
  {
    return barcodeFilter.getBarcode().getBaseline();
  }

  public void setBaseline(final float baseline)
  {
    barcodeFilter.getBarcode().setBaseline(baseline);
  }

  public float getBarHeight()
  {
    return barcodeFilter.getBarcode().getBarHeight();
  }

  public void setBarHeight(final float barHeight)
  {
    barcodeFilter.getBarcode().setBarHeight(barHeight);
  }

  public ElementAlignment getTextAlignment()
  {
    return barcodeFilter.getBarcode().getTextAlignment();
  }

  public void setTextAlignment(final ElementAlignment textAlignment)
  {
    barcodeFilter.getBarcode().setTextAlignment(textAlignment);
  }

  public boolean isGenerateChecksum()
  {
    return barcodeFilter.getBarcode().isGenerateChecksum();
  }

  public void setGenerateChecksum(final boolean generateChecksum)
  {
    barcodeFilter.getBarcode().setGenerateChecksum(generateChecksum);
  }

  public boolean isDisplayChecksumInText()
  {
    return barcodeFilter.getBarcode().isDisplayChecksumText();
  }

  public void setDisplayChecksumInText(final boolean displayChecksumInText)
  {
    barcodeFilter.getBarcode().setDisplayChecksumText(displayChecksumInText);
  }

  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue()
  {
    return imageRefFilter.getValue();
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
    final BarcodeFieldTemplate template = (BarcodeFieldTemplate) super.clone();
    template.imageRefFilter = (ImageRefFilter) imageRefFilter.clone();
    template.barcodeFilter = (BarcodeFilter) template.imageRefFilter.getDataSource();
    template.dataRowDataSource = (DataRowDataSource) template.barcodeFilter.getDataSource();
    return template;
  }

  /**
   * Connects the DataRow to the data source.
   *
   * @param row  the data row.
   *
   * @throws java.lang.IllegalStateException if there is already a data row connected.
   */
  public void connectDataRow(final DataRow row) throws IllegalStateException
  {
    dataRowDataSource.connectDataRow(row);
  }

  /**
   * Releases the connection to the data row.
   * <p>
   * If no data row is connected, an <code>IllegalStateException</code> is thrown to indicate the
   * programming error.
   *
   * @param row  the data row.
   *
   * @throws java.lang.IllegalStateException if there is already a data row connected.
   */
  public void disconnectDataRow(final DataRow row) throws IllegalStateException
  {
    dataRowDataSource.disconnectDataRow(row);
  }
}
