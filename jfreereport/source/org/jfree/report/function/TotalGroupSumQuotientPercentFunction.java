/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info: http://www.jfree.org/jfreereport/index.html
 * Project Lead: Thomas Morgner;
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
 * ---------------------------------
 * TotalGroupSumQuotienFunction.java
 * ---------------------------------
 * (C)opyright 2002, 2003, by Heiko Evermann and Contributors.
 *
 * Original Author: Heiko Evermann (for Hawesko GmbH & Co KG);
 * Contributor(s): Thomas Morgner, David Gilbert (for Simba Management Limited)
 * for programming TotalGroupSumFunction
 *
 * $Id: TotalGroupSumQuotientPercentFunction.java,v 1.7 2005/08/08 15:36:30 taqua Exp $
 *
 * Changes
 * -------
 * 09-Sep-2003 : Initial version, based on TotalGroupSumFunction.java
 *
 */

package org.jfree.report.function;

/**
 * A report function that calculates the quotient of two summed fields (columns) from the
 * TableModel. This function produces a global total. The total sum of the group is known
 * when the group processing starts and the report is not performing a prepare-run. The
 * sum is calculated in the prepare run and recalled in the printing run.
 * <p/>
 * The function can be used in two ways: <ul> <li>to calculate a quotient for the entire
 * report;</li> <li>to calculate a quotient within a particular group;</li> </ul> This
 * function expects its input values to be either java.lang.Number instances or Strings
 * that can be parsed to java.lang.Number instances using a java.text.DecimalFormat.
 * <p/>
 * The function undestands tree parameters. The <code>dividend</code> parameter is
 * required and denotes the name of an ItemBand-field which gets summed up as dividend.
 * The <code>divisor</code> parameter is required and denotes the name of an
 * ItemBand-field which gets summed up as divisor.
 * <p/>
 * The parameter <code>group</code> denotes the name of a group. When this group is
 * started, the counter gets reseted to null. This parameter is optional.
 *
 * @author Thomas Morgner
 */
public class TotalGroupSumQuotientPercentFunction extends TotalGroupSumQuotientFunction
{
  public TotalGroupSumQuotientPercentFunction()
  {
  }

  public Object getValue()
  {
    Double value = (Double) super.getValue();
    if (value.isNaN())
    {
      return value;
    }
    return new Double(value.doubleValue() * 100);
  }
}