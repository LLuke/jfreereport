/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ------------------------------
 * OpenSourceDemoApplet.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 19.06.2003 : Initial version
 *  
 */

package com.jrefinery.report.demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.ItemBand;
import com.jrefinery.report.ItemFactory;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.PageFooter;
import com.jrefinery.report.TextElement;
import com.jrefinery.report.function.PageFunction;
import com.jrefinery.report.preview.PreviewApplet;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.style.ElementStyleSheet;

public class OpenSourceDemoApplet extends PreviewApplet
{
  /** The report (created in code). */
  private JFreeReport report;

  /**
   * Constructs the demo application.
   *
   */
  public OpenSourceDemoApplet()
  {
  }

  /**
   * Creates a report definition in code.
   * <p>
   * It is more base to read the definition from an XML report template file, but sometimes you
   * might need to create a report dynamically.
   *
   * @return a report.
   */
  public JFreeReport getReport()
  {

    if (this.report != null)
    {
      return this.report;
    }

    this.report = new JFreeReport();

    // set up the functions...
    PageFunction f1 = new PageFunction("page_number");
    try
    {
      this.report.addFunction(f1);
    }
    catch (Exception e)
    {
        System.err.println(e.toString());
    }

    // set up the item band...
    ItemBand itemBand = this.report.getItemBand();
    configureItemBand(itemBand);

    // set up the page footer...
    PageFooter pageFooter = this.report.getPageFooter();
    configurePageFooter(pageFooter);

    this.report.setData(new OpenSourceProjects());
    return this.report;
  }

  /**
   * Configures a blank item band.
   *
   * @param band  the item band to be configured.
   */
  private void configureItemBand(ItemBand band)
  {
    ElementStyleSheet ess = band.getBandDefaults();
    ess.setFontDefinitionProperty(new FontDefinition("SansSerif", 9));

    TextElement field1 = ItemFactory.createStringElement(
        "Name_Field",
        new Rectangle2D.Double(0.0, 7.0, 140.0, 10.0),
        Color.black,
        ElementAlignment.LEFT.getOldAlignment(),
        ElementAlignment.TOP.getOldAlignment(),
        null, // font
        "No name", // null string
        "Name"
    );
    field1.getStyle().setFontDefinitionProperty(new FontDefinition("SansSerif",
                                                                   10, true, false, false, false));
    band.addElement(field1);

    TextElement field2 = ItemFactory.createStringElement(
        "URL_Field",
        new Rectangle2D.Double(0.0, 9.0, -100.0, 10.0),
        Color.black,
        ElementAlignment.RIGHT.getOldAlignment(),
        ElementAlignment.TOP.getOldAlignment(),
        null, // font
        "No URL", // null string
        "URL"
    );
    field2.getStyle().setFontDefinitionProperty(new FontDefinition("Monospaced", 8));
    band.addElement(field2);

    TextElement field3 = ItemFactory.createStringElement(
        "Description_Field",
        new Rectangle2D.Double(0.0, 20.0, -100.0, 0.0),
        Color.black,
        ElementAlignment.LEFT.getOldAlignment(),
        ElementAlignment.TOP.getOldAlignment(),
        null, // font
        "No description available", // null string
        "Description"
    );
    field3.getStyle().setStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT,
                                       new Boolean(true));
    band.addElement(field3);

  }

  /**
   * Configures a blank page footer.
   *
   * @param footer  the page footer to be configured.
   */
  private void configurePageFooter(PageFooter footer)
  {
    ElementStyleSheet ess = footer.getBandDefaults();
    ess.setFontDefinitionProperty(new FontDefinition("SansSerif", 9));

    footer.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE, new Dimension(0, 20));
    TextElement pageNumberField = ItemFactory.createNumberElement("PageNumber_Field",
        new Rectangle2D.Double(0.0, 0.0, -100.0, -100.0),
        Color.black,
        ElementAlignment.RIGHT.getOldAlignment(),
        null, // font
        "-", // null string
        "Page 0",
        "page_number"
    );
    pageNumberField.getStyle().setFontDefinitionProperty(new FontDefinition("SansSerif",
                                                                   10, true, false, false, false));
    footer.addElement(pageNumberField);
  }
}
