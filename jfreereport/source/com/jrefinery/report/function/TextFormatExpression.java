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
 * -------------------------
 * TextFormatExpression.java
 * -------------------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *

 * $Id: TextFormatExpression.java,v 1.4 2002/09/13 15:38:08 mungady Exp $
 *
 * Changes
 * -------
 * 20-Aug-2002 : Initial version
 * 31-Aug-2002 : Documentation
 */
package com.jrefinery.report.function;

import com.jrefinery.report.util.PropertiesIterator;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A TextFormatExpression uses a java.text.MessageFormat to concat and format one or more values
 * evaluated from an expression, function or report datasource.
 * <p>
 * The TextFormatExpression uses the pattern property to define the global format-pattern used
 * when evaluating the expression. The dataRow fields used to fill the expressions placeholders
 * are defined in a list of properties where the property-names are numbers. The property counting
 * starts at "0".
 * <p>
 * The Syntax of the <code>pattern</code> property is explained in java.text.MessageFormat.
 * <p>
 * Example:
 <pre>
 <expression name="expr" class="com.jrefinery.report.function.TextFormatExpression">
 <properties>
 <property name="pattern">Invoice for your order from {0, date, EEE, MMM d, yyyy}</property>
 <property name="0">printdate</property>
 </properties>
 </expression>
 </pre>
 *
 * @author TM
 */
public class TextFormatExpression extends AbstractExpression
{
  /** The property key for the pattern property */
  public static final String PATTERN_PROPERTY = "pattern";

  /** An ordered list containing the fieldnames used in the expression. */
  private ArrayList fieldList;

  /**
   * Default constructor, creates a new unnamed TextFormatExpression.
   */
  public TextFormatExpression()
  {
    fieldList = new ArrayList();
  }

  /**
   * Evaluates the expression by collecting all values defined in the fieldlist from the
   * datarow. The collected values are then parsed and formated by the MessageFormat-object.
   *
   * @return a string containing the pattern inclusive the formatted values from the datarow
   */
  public Object getValue()
  {
    return MessageFormat.format(getPattern(), collectValues());
  }

  /**
   * collects the values of all fields defined in the fieldList.
   *
   * @return an Objectarray containing all defined values from the datarow
   */
  private Object[] collectValues()
  {
    Object[] retval = new Object[fieldList.size()];
    for (int i = 0; i < fieldList.size(); i++)
    {
      String field = (String) fieldList.get(i);
      retval[i] = getDataRow().get(field);
    }
    return retval;
  }

  /**
   * Initializes the expression and creates the fieldlist.
   *
   * @throws FunctionInitializeException if the expression has no name set
   */
  public void initialize() throws FunctionInitializeException
  {
    super.initialize();
    fieldList.clear();
    Iterator textFormatIterator = new PropertiesIterator(getProperties());
    while (textFormatIterator.hasNext())
    {
      fieldList.add(textFormatIterator.next());
    }
  }

  /**
   * Returns the pattern defined for this expression.
   *
   * @return the pattern.
   */
  public String getPattern()
  {
    return getProperty(PATTERN_PROPERTY);
  }

  /**
   * Defines the pattern for this expression. The pattern syntax is defined by the
   * java.text.MessageFormat object and the given pattern string has to be valid according to
   * the rules defined there.
   *
   * @param pattern  the pattern string
   */
  public void setPattern(String pattern)
  {
    setProperty(PATTERN_PROPERTY, pattern);
  }

  /**
   * Clones the expression.
   *
   * @return a copy of this expression.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    TextFormatExpression tex = (TextFormatExpression) super.clone();
    tex.fieldList = new ArrayList(fieldList);
    return tex;
  }
}
