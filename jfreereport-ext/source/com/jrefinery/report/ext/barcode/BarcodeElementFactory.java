/**
 * Date: Jan 31, 2003
 * Time: 5:13:52 PM
 *
 * $Id: BarcodeElementFactory.java,v 1.2 2003/05/14 22:36:46 taqua Exp $
 */
package com.jrefinery.report.ext.barcode;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import com.jrefinery.report.Element;
import com.jrefinery.report.ImageElement;
import com.jrefinery.report.ItemFactory;
import com.jrefinery.report.ext.barcode.filter.BarcodeFilter;
import com.jrefinery.report.ext.barcode.filter.templates.BarcodeFieldTemplate;
import com.jrefinery.report.filter.DataRowDataSource;
import com.jrefinery.report.filter.ImageRefFilter;
import com.jrefinery.report.targets.style.ElementStyleSheet;

public class BarcodeElementFactory
{
  public static Element createBarcodeElement (final String name,
                                              final Rectangle2D bounds,
                                              final Color textColor,
                                              final Color barColor,
                                              final Barcode barcode,
                                              final String field,
                                              final boolean dynamic)
  {
    final DataRowDataSource drds = new DataRowDataSource();
    drds.setDataSourceColumnName(field);

    final BarcodeFilter bcf = new BarcodeFilter();
    bcf.setBarcode(barcode);
    bcf.setBarColor(barColor);
    bcf.setTextColor(textColor);
    bcf.setDataSource(drds);

    final ImageRefFilter irf = new ImageRefFilter();
    irf.setDataSource(bcf);

    final ImageElement ie = new ImageElement();
    ie.setName(name);
    ItemFactory.setElementBounds(ie, bounds);
    ie.getStyle().setStyleProperty(ElementStyleSheet.KEEP_ASPECT_RATIO, new Boolean(true));
    ie.getStyle().setStyleProperty(ElementStyleSheet.SCALE, new Boolean(false));
    ie.getStyle().setStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT, new Boolean(dynamic));
    ie.setDataSource(irf);
    return ie;
  }

  public static Element createBarcodeElement (final String name,
                                              final Rectangle2D bounds,
                                              final BarcodeFieldTemplate barcode,
                                              final boolean dynamic)
  {
    final ImageRefFilter irf = new ImageRefFilter();
    irf.setDataSource(barcode);

    final ImageElement ie = new ImageElement();
    ie.setName(name);
    ItemFactory.setElementBounds(ie, bounds);
    ie.getStyle().setStyleProperty(ElementStyleSheet.KEEP_ASPECT_RATIO, new Boolean(true));
    ie.getStyle().setStyleProperty(ElementStyleSheet.SCALE, new Boolean(false));
    ie.getStyle().setStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT, new Boolean(dynamic));
    ie.setDataSource(irf);
    return ie;
  }
}
