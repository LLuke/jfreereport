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
 * -------------------------------
 * ItemColumnQuotientFunction.java
 * -------------------------------
 * (C)opyright 2002, 2003, by Heiko Evermann and Contributors.
 *
 * Original Author:  Heiko Evermann (for Hawesko GmbH & Co KG);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ItemColumnQuotientExpression.java,v 1.12 2005/07/20 18:47:30 taqua Exp $
 *
 * Changes
 * -------
 * 23-Dec-2002 : Initial version
 *
 */

package org.jfree.report.function;



/**
 * A report function that calculates the quotient of two fields (columns) from the current
 * row.
 * <p/>
 * This function expects its input values to be java.lang.Number instances.
 * <p/>
 * The function undestands two parameters. The <code>dividend</code> parameter is required
 * and denotes the name of an ItemBand-field which is used as dividend. The
 * <code>divisor</code> parameter is required and denotes the name of an ItemBand-field
 * which is uses as divisor.
 * <p/>
 *
 * @author Heiko Evermann
 * @deprecated Use PercentageExpression instead, it's name is much clearer
 */
public class ItemColumnQuotientExpression extends PercentageExpression
{
  /**
   * Constructs a new function. <P> Initially the function has no name...be sure to assign
   * one before using the function.
   */
  public ItemColumnQuotientExpression ()
  {
  }
}