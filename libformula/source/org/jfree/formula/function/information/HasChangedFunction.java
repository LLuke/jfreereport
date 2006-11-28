/**
 * =========================================
 * LibFormula : a free Java formula library
 * =========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libformula
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
 *
 * ------------
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.formula.function.information;

import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.EvaluationException;
import org.jfree.formula.typing.coretypes.LogicalType;

/**
 * Creation-Date: 24.11.2006, 13:02:41
 *
 * @author Thomas Morgner
 */
public class HasChangedFunction implements Function
{
  public HasChangedFunction()
  {
  }

  public String getCanonicalName()
  {
    return "HASCHANGED";
  }

  public TypeValuePair evaluate(FormulaContext context,
                                ParameterCallback parameters)
      throws EvaluationException
  {
    // we expect strings and will check, whether the reference for theses
    // strings is dirty.

    final int parCount = parameters.getParameterCount();
    for (int i = 0; i < parCount; i++)
    {
      // Make this configurable ...
//      final LValue rawValue = parameters.getRaw(i);
//      if (rawValue instanceof ContextLookup)
//      {
//        // OK, it is a context lookup, so lookup.
//        // this will fail if the context does not track changes.
//        final ContextLookup cl = (ContextLookup) rawValue;
//        final String name = cl.getName();
//        if (context.isReferenceDirty(name))
//        {
//          return new TypeValuePair(LogicalType.TYPE, Boolean.TRUE);
//        }
//      }

      final Object value = parameters.getValue(i);
      if (value == null)
      {
        continue;
      }

      if (context.isReferenceDirty(value))
      {
        return new TypeValuePair(LogicalType.TYPE, Boolean.TRUE);
      }
    }
    return new TypeValuePair(LogicalType.TYPE, Boolean.FALSE);
  }
}
