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
 * NettoProfitFunction.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: NettoProfitFunction.java,v 1.2 2005/01/25 01:13:41 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 26.04.2004 : Initial version
 *  
 */

package org.jfree.report.demo.conditionalgroup;

import java.math.BigDecimal;

import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.AbstractFunction;
import org.jfree.report.function.FunctionUtilities;

public class NettoProfitFunction extends AbstractFunction
{
  private BigDecimal income;
  private BigDecimal expense;

  /**
   * Creates an unnamed function. Make sure the name of the function is set using {@link
   * #setName} before the function is added to the report's function collection.
   */
  public NettoProfitFunction ()
  {
  }

  /**
   * Receives notification that report generation initializes the current run. <P> The
   * event carries a ReportState.Started state.  Use this to initialize the report.
   *
   * @param event The event.
   */
  public void reportInitialized (final ReportEvent event)
  {
    income = null;
    expense = null;
  }

  /**
   * Receives notification that a group has finished.
   *
   * @param event the event.
   */
  public void groupFinished (final ReportEvent event)
  {
    // do nothing if this is the wrong group ...
    if (FunctionUtilities.isDefinedGroup("type-group", event) == false)
    {
      return;
    }
    final String type = (String) event.getDataRow().get("type");

    if (type.equals("Income"))
    {
      final Number n = (Number) event.getDataRow().get("sum");
      income = new BigDecimal(n.toString());
    }
    else
    {
      final Number n = (Number) event.getDataRow().get("sum");
      expense = new BigDecimal(n.toString());
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
    if (income != null && expense != null)
    {
      return income.subtract(expense);
    }
    else
    {
      return null;
    }
  }
}
