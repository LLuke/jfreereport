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
 * IfFunction.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: IfFunction.java,v 1.1 2006/11/04 18:06:09 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula.function.logical;

import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.coretypes.ErrorType;

/**
 * Creation-Date: 04.11.2006, 18:28:15
 *
 * @author Thomas Morgner
 */
public class IfFunction implements Function
{
  public IfFunction()
  {
  }

  public String getCanonicalName()
  {
    return "IF";
  }

  public TypeValuePair evaluate(FormulaContext context,
                                TypeValuePair[] parameter)
  {
    if (parameter.length < 3)
    {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(1));
    }

    if (Boolean.TRUE.equals(parameter[0].getValue()))
    {
      return parameter[1];
    }
    return parameter[2];

  }
}
