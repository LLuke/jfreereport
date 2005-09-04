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
 * --------------------
 * OpenSourceAPIDemoHandler.java
 * --------------------
 * (C)opyright 2003, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: OpenSourceAPIDemoHandler.java,v 1.1 2005/08/29 17:41:32 taqua Exp $
 *
 * Changes
 * -------
 * 03-Jan-2003 : Version 1, based on OpenSourceXMLDemoHandler.java (DG);
 *
 */

package org.jfree.report.demo.opensource;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.net.URL;
import javax.swing.JComponent;
import javax.swing.table.TableModel;

import org.jfree.report.ElementAlignment;
import org.jfree.report.ItemBand;
import org.jfree.report.JFreeReport;
import org.jfree.report.PageFooter;
import org.jfree.report.demo.helper.AbstractDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.elementfactory.NumberFieldElementFactory;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.function.PageFunction;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.FontDefinition;
import org.jfree.ui.FloatDimension;
import org.jfree.util.ObjectUtilities;

/**
 * This demo application replicates the report generated by OpenSourceXMLDemoHandler.java, but
 * creates the report in code rather than using an XML report template.
 *
 * @author David Gilbert
 */
public class OpenSourceAPIDemoHandler extends AbstractDemoHandler
{
  /**
   * The data for the report.
   */
  private TableModel data;

  public OpenSourceAPIDemoHandler ()
  {
    data = new OpenSourceProjects();
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("opensource-api.html", OpenSourceAPIDemoHandler.class);
  }

  public String getDemoName()
  {
    return "Open Source Demo (API)";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    JFreeReport report = createStaticReport();
    report.setData(data);
    return report;
  }

  /**
   * Creates a report definition in code.
   * <p/>
   * It is more base to read the definition from an XML report template file, but
   * sometimes you might need to create a report dynamically.
   *
   * @return a report.
   */
  public static JFreeReport createStaticReport ()
  {

    final JFreeReport result = new JFreeReport();

    // set up the functions...
    final PageFunction f1 = new PageFunction("page_number");
    result.addExpression(f1);

    // set up the item band...
    final ItemBand itemBand = result.getItemBand();
    configureItemBand(itemBand);

    // set up the page footer...
    final PageFooter pageFooter = result.getPageFooter();
    configurePageFooter(pageFooter);

    return result;

  }

  /**
   * Configures a blank item band.
   *
   * @param band the item band to be configured.
   */
  private static void configureItemBand (final ItemBand band)
  {
    final ElementStyleSheet ess = band.getStyle();
    ess.setFontDefinitionProperty(new FontDefinition("SansSerif", 9));

    TextFieldElementFactory factory = new TextFieldElementFactory();
    factory.setName("Name_Field");
    factory.setAbsolutePosition(new Point2D.Float(0, 7));
    factory.setMinimumSize(new FloatDimension(140, 10));
    factory.setColor(Color.black);
    factory.setHorizontalAlignment(ElementAlignment.LEFT);
    factory.setVerticalAlignment(ElementAlignment.TOP);
    factory.setFontName("SansSerif");
    factory.setFontSize(new Integer(10));
    factory.setBold(Boolean.TRUE);
    factory.setNullString("No Name");
    factory.setFieldname("Name");
    band.addElement(factory.createElement());

    factory = new TextFieldElementFactory();
    factory.setName("URL_Field");
    factory.setAbsolutePosition(new Point2D.Float(0, 9));
    factory.setMinimumSize(new FloatDimension(-100, 10));
    factory.setColor(Color.black);
    factory.setHorizontalAlignment(ElementAlignment.RIGHT);
    factory.setVerticalAlignment(ElementAlignment.TOP);
    factory.setFontName("Monospaced");
    factory.setFontSize(new Integer(8));
    factory.setNullString("No URL");
    factory.setFieldname("URL");
    band.addElement(factory.createElement());

    factory = new TextFieldElementFactory();
    factory.setName("Description_Field");
    factory.setAbsolutePosition(new Point2D.Float(0, 20));
    factory.setMinimumSize(new FloatDimension(-100, 10));
    factory.setColor(Color.black);
    factory.setHorizontalAlignment(ElementAlignment.LEFT);
    factory.setVerticalAlignment(ElementAlignment.TOP);
    factory.setNullString("No description available");
    factory.setFieldname("Description");
    factory.setDynamicHeight(Boolean.TRUE);
    band.addElement(factory.createElement());
  }

  /**
   * Configures a blank page footer.
   *
   * @param footer the page footer to be configured.
   */
  private static void configurePageFooter (final PageFooter footer)
  {
    footer.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE, new Dimension(0, 20));

    final ElementStyleSheet ess = footer.getStyle();
    ess.setFontDefinitionProperty(new FontDefinition("SansSerif", 9));

    final NumberFieldElementFactory factory = new NumberFieldElementFactory();
    factory.setName("PageNumber_Field");
    factory.setAbsolutePosition(new Point2D.Float(0, 0));
    factory.setMinimumSize(new Dimension(-100, -100));
    factory.setColor(Color.black);
    factory.setHorizontalAlignment(ElementAlignment.RIGHT);
    factory.setNullString("-");
    factory.setFormatString("Page 0");
    factory.setFieldname("page_number");
    factory.setFontName("SansSerif");
    factory.setFontSize(new Integer(10));
    factory.setBold(Boolean.TRUE);
    footer.addElement(factory.createElement());
  }

  

}