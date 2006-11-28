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
 * DataRowReference.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ContextLookup.java,v 1.3 2006/11/20 21:05:30 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula.lvalues;

import org.jfree.formula.typing.Type;
import org.jfree.formula.FormulaContext;

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

  public TypeValuePair evaluate()
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
