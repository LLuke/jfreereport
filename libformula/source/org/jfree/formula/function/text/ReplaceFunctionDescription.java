/**
 * =========================================
 * LibFormula : a free Java formula library
 * =========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libformula
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * $Id: ReptFunctionDescription.java,v 1.2 2007/01/23 21:33:16 mimil Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.function.text;

import org.jfree.formula.function.AbstractFunctionDescription;
import org.jfree.formula.function.FunctionCategory;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.NumberType;
import org.jfree.formula.typing.coretypes.TextType;

/**
 * Describes ReplaceFunction function.
 * @see ReplaceFunction
 * 
 * @author Cedric Pronzato
 *
 */
public class ReplaceFunctionDescription extends AbstractFunctionDescription
{
  public ReplaceFunctionDescription()
  {
    super("org.jfree.formula.function.text.Replace-Function");
  }

  public FunctionCategory getCategory()
  {
    return TextFunctionCategory.CATEGORY;
  }

  public int getParameterCount()
  {
    return 4;
  }

  public Type getParameterType(int position)
  {
    if(position == 0 || position == 3)
    {
      return TextType.TYPE;
    }
    return NumberType.GENERIC_NUMBER;
  }

  public Type getValueType()
  {
    return TextType.TYPE;
  }

  public boolean isParameterMandatory(int position)
  {
    return true;
  }

}
