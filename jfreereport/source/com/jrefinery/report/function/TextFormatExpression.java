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
 * ---------------------------
 * TextFormatExpression.java
 * ---------------------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *

 * $Id$
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
 * A TextFormatExpression uses an java.text.MessageFormat to concat and format one or more values
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
 */
public class TextFormatExpression extends AbstractExpression
{
  /** the property key for the pattern property */
  public static final String PATTERN_PROPERTY = "pattern";

  /** a ordered list containing the fieldnames used in the expression */
  private ArrayList fieldList;

  /**
   * defaultconstructor
   */
  public TextFormatExpression()
  {
    fieldList = new ArrayList();
  }

  public Object getValue()
  {
    return MessageFormat.format(getPattern(), collectValues());
  }

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

  public String getPattern()
  {
    return getProperty(PATTERN_PROPERTY);
  }

  public void setPattern(String pattern)
  {
    setProperty(PATTERN_PROPERTY, pattern);
  }

  public Object clone() throws CloneNotSupportedException
  {
    TextFormatExpression tex = (TextFormatExpression) super.clone();
    tex.fieldList = new ArrayList(fieldList);
    return tex;
  }
}
