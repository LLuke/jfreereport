/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
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
 * -------------------------
 * TextFormatExpression.java
 * -------------------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: TextFormatExpression.java,v 1.11 2006/01/24 18:58:29 taqua Exp $
 *
 * Changes
 * -------
 * 20-Aug-2002 : Initial version
 * 31-Aug-2002 : Documentation
 */
package org.jfree.report.function.strings;

import java.text.MessageFormat;

import org.jfree.report.DataSourceException;
import org.jfree.report.function.ColumnAggregationExpression;

/**
 * A TextFormatExpression uses a java.text.MessageFormat to concat and format one or more
 * values evaluated from an expression, function or report datasource.
 * <p/>
 * The TextFormatExpression uses the pattern property to define the global format-pattern
 * used when evaluating the expression. The dataRow fields used to fill the expressions
 * placeholders are defined in a list of properties where the property-names are numbers.
 * The property counting starts at "0".
 * <p/>
 * The Syntax of the <code>pattern</code> property is explained in
 * java.text.MessageFormat.
 * <p/>
 * Example:
 * <pre>
 * <expression name="expr" class="org.jfree.report.function.TextFormatExpression">
 * <properties>
 * <property name="pattern">Invoice for your order from {0, date, EEE, MMM d,
 * yyyy}</property>
 * <property name="fields[0]">printdate</property>
 * </properties>
 * </expression>
 * </pre>
 *
 * @author Thomas Morgner
 */
public class TextFormatExpression extends ColumnAggregationExpression
{
  private String pattern;

  /**
   * Default constructor, creates a new unnamed TextFormatExpression.
   */
  public TextFormatExpression ()
  {
  }

  /**
   * Evaluates the expression by collecting all values defined in the fieldlist from the
   * datarow. The collected values are then parsed and formated by the
   * MessageFormat-object.
   *
   * @return a string containing the pattern inclusive the formatted values from the
   *         datarow
   */
  public Object getValue () throws DataSourceException
  {
    final MessageFormat format = new MessageFormat("");
    format.setLocale(getParentLocale());
    format.applyPattern(getPattern());
    return format.format(getFieldValues());
  }

  /**
   * Returns the pattern defined for this expression.
   *
   * @return the pattern.
   */
  public String getPattern ()
  {
    return pattern;
  }

  /**
   * Defines the pattern for this expression. The pattern syntax is defined by the
   * java.text.MessageFormat object and the given pattern string has to be valid according
   * to the rules defined there.
   *
   * @param pattern the pattern string
   */
  public void setPattern (final String pattern)
  {
    this.pattern = pattern;
  }
}
