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
 * XorFunction.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: XorFunction.java,v 1.2 2006/11/05 14:27:27 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula.function.logical;

import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.EvaluationException;
import org.jfree.formula.typing.coretypes.LogicalType;

/**
 * Creation-Date: 04.11.2006, 18:28:15
 *
 * @author Thomas Morgner
 */
public class XorFunction implements Function
{
  public XorFunction()
  {
  }

  public String getCanonicalName()
  {
    return "XOR";
  }


  public TypeValuePair evaluate(FormulaContext context,
                                ParameterCallback parameters)
      throws EvaluationException
  {
    int count = 0;
    final int parameterCount = parameters.getParameterCount();
    for (int i = 0; i < parameterCount; i++)
    {
      if (Boolean.TRUE.equals(parameters.getValue(i)))
      {
        count += 1;
      }
    }
    if ((count & 1) == 1)
    {
      return new TypeValuePair(LogicalType.TYPE, Boolean.FALSE);
    }
    else
    {
      return new TypeValuePair(LogicalType.TYPE, Boolean.TRUE);
    }

  }
}
