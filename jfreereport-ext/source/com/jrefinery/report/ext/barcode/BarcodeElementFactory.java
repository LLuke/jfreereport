/**
 * Date: Jan 31, 2003
 * Time: 5:13:52 PM
 *
 * $Id: BarcodeElementFactory.java,v 1.1 2003/02/25 20:58:36 taqua Exp $
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
  public static Element createBarcodeElement (String name,
                                              Rectangle2D bounds,
                                              Color textColor,
                                              Color barColor,
                                              Barcode barcode,
                                              String field,
                                              boolean dynamic)
  {
    DataRowDataSource drds = new DataRowDataSource();
    drds.setDataSourceColumnName(field);

    BarcodeFilter bcf = new BarcodeFilter();
    bcf.setBarcode(barcode);
    bcf.setBarColor(barColor);
    bcf.setTextColor(textColor);
    bcf.setDataSource(drds);

    ImageRefFilter irf = new ImageRefFilter();
    irf.setDataSource(bcf);

    ImageElement ie = new ImageElement();
    ie.setName(name);
    ItemFactory.setElementBounds(ie, bounds);
    ie.getStyle().setStyleProperty(ElementStyleSheet.KEEP_ASPECT_RATIO, new Boolean(true));
    ie.getStyle().setStyleProperty(ElementStyleSheet.SCALE, new Boolean(false));
    ie.getStyle().setStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT, new Boolean(dynamic));
    ie.setDataSource(irf);
    return ie;
  }

  public static Element createBarcodeElement (String name,
                                              Rectangle2D bounds,
                                              BarcodeFieldTemplate barcode,
                                              boolean dynamic)
  {
    ImageRefFilter irf = new ImageRefFilter();
    irf.setDataSource(barcode);

    ImageElement ie = new ImageElement();
    ie.setName(name);
    ItemFactory.setElementBounds(ie, bounds);
    ie.getStyle().setStyleProperty(ElementStyleSheet.KEEP_ASPECT_RATIO, new Boolean(true));
    ie.getStyle().setStyleProperty(ElementStyleSheet.SCALE, new Boolean(false));
    ie.getStyle().setStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT, new Boolean(dynamic));
    ie.setDataSource(irf);
    return ie;
  }
}
