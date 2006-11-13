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
 * MappedFunction.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: MappedFunction.java,v 1.1 2006/11/05 14:32:10 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula.function.userdefined;

import org.jfree.formula.FormulaContext;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;

/**
 * A function, that calls a method on a java class. The class is reinstantiated
 * each time the function is called (unless the method is static).
 *
 * @author Thomas Morgner
 */
public class MappedFunction implements Function
{
  private String className;
  private String functionName;

  public MappedFunction()
  {
  }

  public String getClassName()
  {
    return className;
  }

  public String getCanonicalName()
  {
    return functionName;
  }

  public TypeValuePair evaluate(FormulaContext context,
                                ParameterCallback parameters)
  {
    return null;
  }

  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }
}
