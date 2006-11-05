/**
 * =========================================
 * LibLayout : a free Java layouting library
 * =========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2006, by Pentaho Corperation and Contributors.
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
 * PostfixTerm.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PostfixTerm.java,v 1.2 2006/11/04 17:27:37 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula.lvalues;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.operators.PostfixOperator;

/**
 * Creation-Date: 02.11.2006, 10:20:27
 *
 * @author Thomas Morgner
 */
public class PostfixTerm extends AbstractLValue
{
  private PostfixOperator operator;
  private LValue value;

  public PostfixTerm(final LValue value, final PostfixOperator operator)
  {
    if (operator == null)
    {
      throw new NullPointerException();
    }
    if (value == null)
    {
      throw new NullPointerException();
    }
    this.operator = operator;
    this.value = value;
  }

  public PostfixOperator getOperator()
  {
    return operator;
  }

  public LValue getValue()
  {
    return value;
  }

  public TypeValuePair evaluate() throws EvaluationException
  {
    return operator.evaluate(getContext(), value.evaluate());
  }


  public String toString()
  {
    return "(" + value + operator + ')';
  }

  /**
   * Checks, whether the LValue is constant. Constant lvalues always return the
   * same value.
   *
   * @return
   */
  public boolean isConstant()
  {
    return value.isConstant();
  }

  /**
   * Returns any dependent lvalues (parameters and operands, mostly).
   *
   * @return
   */
  public LValue[] getChildValues()
  {
    return new LValue[]{ value };
  }

  public Object clone() throws CloneNotSupportedException
  {
    final PostfixTerm o = (PostfixTerm) super.clone();
    o.value = (LValue) value.clone();
    return o;
  }
}
