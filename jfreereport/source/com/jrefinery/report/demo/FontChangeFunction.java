/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -----------------------
 * FontChangeFunction.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id$
 *
 * Changes
 * -------
 * 26-May-2002 : Initial version
 * 05-Jun-2002 : Documentation
 * 28-Aug-2002 : ChangeLog
 */
package com.jrefinery.report.demo;

import com.jrefinery.report.Element;
import com.jrefinery.report.TextElement;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.function.AbstractFunction;
import com.jrefinery.report.function.FunctionInitializeException;

import javax.swing.table.TableModel;
import java.awt.Font;

/**
 * This is a function used in report4-demo. The function demonstrates how to alter an
 * elements property during the report generation. The elements font is changed base on
 * the data provided in the reports datasource.
 * <p>
 * For every new item row in the report, the font for that row is changed to the fontname
 * specified in the second column of the report data source.
 * <p>
 * Parameters:<br>
 * The function expects the name of a field in the item band in the parameter "element".
 * This functions value will always be null.
 *
 * @author TM
 */
public class FontChangeFunction extends AbstractFunction
{

  /**
   * DefaultConstructor
   */
  public FontChangeFunction ()
  {
  }

  /**
   * Before an ItemBand is printed, the report generator will call itemsAdvanced
   * for all functions in the function collection. This is the right place to alter
   * the font of the element defined in the "element" property, so that every ItemBand
   * has the font set, that is defined in the data model.
   *
   * @param event the report event.
   */
  public void itemsAdvanced (ReportEvent event)
  {
    // if this is a preparerun, nothing gets printed and so no font change is required.
    if (event.getState ().isPrepareRun ())
    {
      return;
    }

    TableModel data = event.getReport ().getData ();
    int row = event.getState ().getCurrentDataItem ();

    // Try to get the name of the font to be set.
    // If the name is null, return without an excpetion, just do nothing.
    String fontname = (String) data.getValueAt (row, 1);
    if (fontname == null)
    {
      return;
    }

    // Lookup the element by name. If there no element found, the getElement function
    // returns null, so we have to check this case.
    Element e = event.getReport ().getItemBand ().getElement (getElement ());

    // set the font if an element was found.
    if (e != null && (e instanceof TextElement))
    {
      TextElement tx = (TextElement) e;
      tx.setFont (new Font (fontname, Font.PLAIN, 10));
    }
  }

  /**
   * Performs the functions initialisation. The initialisation will fail, if the
   * function has no valid name or the required parameter "element" is missing.
   * <p>
   * @see #setElement(String)
   *
   * @throws FunctionInitializeException if the element name has not been specified.
   */
  public void initialize () throws FunctionInitializeException
  {
    super.initialize ();
    if (getProperty ("element") == null)
    {
      throw new FunctionInitializeException ("Element name must be specified");
    }
  }

  /**
   * Defines the name of the text element that gets its font altered. If the element
   * does not exist or is no text element, the function will do nothing.
   * <p>
   * This functions property is reachable by using the key "element" on getProperty.
   *
   * @param name  the element name.
   */
  public void setElement (String name)
  {
    setProperty ("element", name);
  }

  /**
   * Returns the name of the element that should get the font set. Returns an empty string,
   * if the property is not set.
   *
   * @return the element name.
   */
  public String getElement ()
  {
    return getProperty ("element", "");
  }

  /**
   * Returns the value calculated by this function. As this function does not calculate values,
   * this method does always return null.
   *
   * @return null.
   */
  public Object getValue ()
  {
    return null;
  }
}
