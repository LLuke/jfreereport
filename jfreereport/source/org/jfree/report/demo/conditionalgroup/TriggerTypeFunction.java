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
 * TriggerTypeFunction.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TriggerTypeFunction.java,v 1.1.2.1 2004/04/27 15:02:07 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 24.04.2004 : Initial version
 *  
 */

package org.jfree.report.demo.conditionalgroup;

import org.jfree.report.Element;
import org.jfree.report.Group;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.AbstractFunction;
import org.jfree.report.function.FunctionUtilities;

public class TriggerTypeFunction extends AbstractFunction
{
  /**
   * Creates an unnamed function. Make sure the name of the function is set using {@link
   * #setName} before the function is added to the report's function collection.
   */
  public TriggerTypeFunction ()
  {
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event the event.
   */
  public void groupStarted (final ReportEvent event)
  {
    // do nothing if this is the wrong group ...
    if (FunctionUtilities.isDefinedGroup("type-group", event) == false)
    {
      return;
    }

    final String type = (String) event.getDataRow().get ("type");
    final Group g = FunctionUtilities.getCurrentGroup(event);

    final Element headerIncomeBand =
            g.getHeader().getElement("income-band");
    final Element headerExpenseBand =
            g.getHeader().getElement("expense-band");
    final Element footerIncomeBand =
            g.getFooter().getElement("income-band");
    final Element footerExpenseBand =
            g.getFooter().getElement("expense-band");

    // and now apply the visibility ...
    if (type.equals("Income"))
    {
      headerExpenseBand.setVisible(false);
      footerExpenseBand.setVisible(false);
      headerIncomeBand.setVisible(true);
      footerIncomeBand.setVisible(true);
    }
    else
    {
      headerExpenseBand.setVisible(true);
      footerExpenseBand.setVisible(true);
      headerIncomeBand.setVisible(false);
      footerIncomeBand.setVisible(false);
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
    return null;
  }
}
