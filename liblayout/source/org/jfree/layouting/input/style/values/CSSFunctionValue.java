/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * CSSFunctionValue.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.input.style.values;

/**
 * Creation-Date: 27.11.2005, 20:18:52
 *
 * @author Thomas Morgner
 */
public class CSSFunctionValue implements CSSValue
{
  private String functionName;
  private CSSValue[] parameters;

  public CSSFunctionValue(final String functionName,
                          final CSSValue[] parameters)
  {
    if (functionName == null)
    {
      throw new NullPointerException();
    }
    if (parameters == null) throw new NullPointerException();
    this.functionName = functionName;
    this.parameters = (CSSValue[]) parameters.clone();
  }

  public String getFunctionName()
  {
    return functionName;
  }

  public CSSValue[] getParameters()
  {
    return (CSSValue[]) parameters.clone();
  }

  public String getCSSText()
  {
    StringBuffer b = new StringBuffer();
    b.append(functionName);
    b.append("(");
    for (int i = 0; i < parameters.length; i++)
    {
      if (i != 0)
      {
        b.append(", ");
      }
      CSSValue parameter = parameters[i];
      b.append(parameter.getCSSText());
    }
    b.append(")");
    return b.toString();
  }

  /**
   * Returns a string representation of the object.
   *
   * @return a string representation of the object.
   */
  public String toString()
  {
    return getCSSText();
  }
}
