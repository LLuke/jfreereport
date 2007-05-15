/**
 * =========================================
 * LibFormula : a free Java formula library
 * =========================================
 *
 * Project Info:  http://reporting.pentaho.org/libformula/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 *
 * ------------
 * $Id: StaticValue.java,v 1.6 2007/05/07 22:51:12 mimil Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.lvalues;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.AnyType;

/**
 * Creation-Date: 08.10.2006, 11:34:40
 *
 * @author Thomas Morgner
 */
public class StaticValue extends AbstractLValue
{
  private Object value;
  private Type type;

  public StaticValue(final Object value)
  {
    this(value, AnyType.TYPE);
  }
  
  public StaticValue(final Object value, Type type)
  {
    this.value = value;
    this.type = type;
  }

  public void initialize(FormulaContext context) throws EvaluationException
  {
  }

  public TypeValuePair evaluate()
  {
    return new TypeValuePair(type, value);
  }


  public String toString()
  {
    return String.valueOf(value);
  }

  /**
   * Checks, whether the LValue is constant. Constant lvalues always return the
   * same value.
   *
   * @return
   */
  public boolean isConstant()
  {
    return true;
  }

  public Object getValue()
  {
    return value;
  }

  /**
   * This function allows a program traversing the LibFormula object model
   * to know what type this static value is.
   *
   * @return the type of the static value
   *
   */ 
  public Type getValueType()
  {
    return type;
  }
}
