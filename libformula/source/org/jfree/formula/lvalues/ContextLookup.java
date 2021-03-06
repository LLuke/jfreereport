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

import org.jfree.formula.typing.Type;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.EvaluationException;

/**
 * A reference that queries the datarow.
 *
 * @author Thomas Morgner
 */
public class ContextLookup extends AbstractLValue
{
  private String name;

  public ContextLookup(final String name)
  {
    this.name = name;
  }

  public TypeValuePair evaluate() throws EvaluationException
  {
    final FormulaContext context = getContext();
    final Object value = context.resolveReference(name);
    final Type type = context.resolveReferenceType(name);
    return new TypeValuePair(type, value);
  }


  public String toString()
  {
    return "[" +  name + "]";
  }

  /**
   * Checks, whether the LValue is constant. Constant lvalues always return the
   * same value.
   *
   * @return
   */
  public boolean isConstant()
  {
    return false;
  }

  public String getName()
  {
    return name;
  }
}
