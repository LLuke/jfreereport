/**
 * Date: Jan 31, 2003
 * Time: 5:13:52 PM
 *
 * $Id: BarcodeElementFactory.java,v 1.1 2003/07/08 14:21:46 taqua Exp $
 */
package org.jfree.report.ext.modules.barcode.base.content;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Element;
import org.jfree.report.ImageElement;
import org.jfree.report.ext.modules.barcode.base.filter.BarcodeFilter;
import org.jfree.report.ext.modules.barcode.base.filter.templates.BarcodeFieldTemplate;
import org.jfree.report.ext.modules.barcode.base.content.Barcode;
import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.ImageRefFilter;
import org.jfree.report.style.ElementStyleSheet;

// todo redefine later ..
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
   // ItemFactory.setElementBounds(ie, bounds);
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
   // ItemFactory.setElementBounds(ie, bounds);
    ie.getStyle().setStyleProperty(ElementStyleSheet.KEEP_ASPECT_RATIO, new Boolean(true));
    ie.getStyle().setStyleProperty(ElementStyleSheet.SCALE, new Boolean(false));
    ie.getStyle().setStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT, new Boolean(dynamic));
    ie.setDataSource(irf);
    return ie;
  }
}
