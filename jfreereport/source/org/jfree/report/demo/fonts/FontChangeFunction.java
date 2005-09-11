/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * $Id: FontChangeFunction.java,v 1.1 2005/08/29 17:34:40 taqua Exp $
 *
 * Changes
 * -------
 * 26-May-2002 : Initial version
 * 05-Jun-2002 : Documentation
 * 28-Aug-2002 : ChangeLog
 */
package org.jfree.report.demo.fonts;

import java.awt.Font;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.TextElement;
import org.jfree.report.function.AbstractElementFormatFunction;
import org.jfree.report.style.FontDefinition;

/**
 * This is a function used in report4-demo. The function demonstrates how to alter an
 * elements property during the report generation. The elements font is changed base on
 * the data provided in the reports datasource.
 * <p/>
 * For every new item row in the report, the font for that row is changed to the fontname
 * specified in the second column of the report data source.
 * <p/>
 * Parameters:<br> The function expects the name of a field in the item band in the
 * parameter "element". This functions value will always be null.
 *
 * @author Thomas Morgner
 */
public class FontChangeFunction extends AbstractElementFormatFunction
{
  /**
   * DefaultConstructor.
   */
  public FontChangeFunction ()
  {
  }


  protected void processRootBand(Band b)
  {
    // Try to get the name of the font to be set.
    // If the name is null, return without an excpetion, just do nothing.
    final String fontname = (String) getDataRow().get(1);
    if (fontname == null)
    {
      return;
    }

    // Lookup the element by name. If there no element found, the getElement function
    // returns null, so we have to check this case.
    final Element e = b.getElement(getElement());

    // set the font if an element was found.
    if (e != null && (e instanceof TextElement))
    {
      final TextElement tx = (TextElement) e;
      tx.getStyle().setFontDefinitionProperty(new FontDefinition(new Font(fontname, Font.PLAIN, 10)));
    }
  }

  /**
   * Returns the value calculated by this function. As this function does not calculate
   * values, this method does always return null.
   *
   * @return always null, as this function does not calculate something.
   */
  public Object getValue ()
  {
    return null;
  }
}
