/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * UrlFunction.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: UrlFunction.java,v 1.1 2006/04/17 21:01:50 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.style.functions;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.layouter.style.values.CSSResourceValue;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.model.LayoutElement;

/**
 * Creation-Date: 16.04.2006, 14:14:42
 *
 * @author Thomas Morgner
 */
public class UrlFunction extends AbstractStyleFunction
{
  public UrlFunction()
  {
  }

  public CSSValue getValue(final LayoutProcess layoutProcess,
                           final LayoutElement element,
                           final CSSFunctionValue function)
          throws FunctionEvaluationException
  {
    CSSValue[] params = function.getParameters();
    if (params.length != 1)
    {
      throw new FunctionEvaluationException();
    }
    CSSValue value = getParameter(layoutProcess, element, params[0]);
    if (value instanceof CSSResourceValue)
    {
      return value;
    }
    if (value instanceof CSSStringValue == false)
    {
      throw new FunctionEvaluationException("Not even remotely an URI: " + value);
    }
    final CSSStringValue strval = (CSSStringValue) value;
    return convertToURI(layoutProcess, strval.getValue());
  }
}
