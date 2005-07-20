/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Simba Management Limited and Contributors.
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
 * HideArticleDetailsFunction.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HideArticleDetailsFunction.java,v 1.3 2005/02/23 21:04:43 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 26.03.2004 : Initial version
 *  
 */

package org.jfree.report.demo.invoice;

import org.jfree.report.Element;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.AbstractFunction;
import org.jfree.report.function.FunctionUtilities;
import org.jfree.report.util.Log;

public class HideArticleDetailsFunction extends AbstractFunction
{
  private String element;
  private String column;

  /**
   * Creates an unnamed function. Make sure the name of the function is set using {@link
   * #setName} before the function is added to the report's function collection.
   */
  public HideArticleDetailsFunction ()
  {
  }

  public String getColumn ()
  {
    return column;
  }

  public void setColumn (final String column)
  {
    this.column = column;
  }

  public String getElement ()
  {
    return element;
  }

  public void setElement (final String element)
  {
    this.element = element;
  }

  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event the event.
   */
  public void itemsAdvanced (final ReportEvent event)
  {
    final Element[] e = FunctionUtilities.findAllElements
            (event.getReport().getItemBand(), getElement());
    if (e == null)
    {
      Log.warn("HideArticleDetailsFunction: No 'detail' element found in the itemband.");
      return;
    }

    for (int i = 0; i < e.length; i++)
    {
      final Element element = e[i];

      // now hide the element if there are no details ...
      element.setVisible(event.getDataRow().get(getColumn()) != null);
    }
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on the
   * expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue ()
  {
    // we don't have to return a value, as this function does no computation
    return null;
  }
}
