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
 * $Id$
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.lvalues;

import java.io.Serializable;

import org.jfree.formula.FormulaContext;
import org.jfree.formula.EvaluationException;
import org.jfree.formula.typing.Type;

/**
 * A reference is an indirection to hide the details of where the actual
 * value came from.
 *
 * The reference is responsible to report dependencies.
 *
 * @author Thomas Morgner
 */
public interface LValue extends Serializable, Cloneable
{
  public void initialize(FormulaContext context) throws EvaluationException;

  public TypeValuePair evaluate() throws EvaluationException;

  public Object clone() throws CloneNotSupportedException;

  /**
   * Querying the value type is only valid *after* the value has been evaluated.
   * @return
   */
  public Type getValueType();

  /**
   * Returns any dependent lvalues (parameters and operands, mostly).
   *
   * @return
   */
  public LValue[] getChildValues();

  /**
   * Checks, whether the LValue is constant. Constant lvalues always return
   * the same value.
   *
   * @return
   */
  public boolean isConstant();
}
