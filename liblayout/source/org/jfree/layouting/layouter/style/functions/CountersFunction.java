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
 * CountersFunction.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.style.functions;

import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSStringType;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.layouter.counters.CounterStyle;
import org.jfree.layouting.layouter.counters.CounterStyleFactory;
import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.model.ElementContext;
import org.jfree.layouting.model.DocumentContext;

/**
 * Creation-Date: 16.04.2006, 15:22:11
 *
 * @author Thomas Morgner
 */
public class CountersFunction extends AbstractStyleFunction
{
  public CountersFunction()
  {
  }

  public CSSValue getValue(final LayoutProcess layoutProcess,
                           final LayoutElement element,
                           final CSSFunctionValue function)
          throws FunctionEvaluationException
  {
    // Accepts one or two parameters ...
    final CSSValue[] params = function.getParameters();
    if (params.length < 2)
    {
      throw new FunctionEvaluationException();
    }
    final String counterName =
            getStringParameter(layoutProcess, element, params[0]);
    final String separator =
            getStringParameter(layoutProcess, element, params[1]);

    final DocumentContext documentContext = layoutProcess.getDocumentContext();
    CounterStyle cstyle = documentContext.getCounterStyle(counterName);

    if (params.length > 2)
    {
      final String styleName =
              getStringParameter(layoutProcess, element, params[2]);
      final CounterStyle style = CounterStyleFactory.getInstance().getCounterStyle(styleName);
      if (style != null)
      {
        cstyle = style;
      }
    }

    final StringBuffer buffer = new StringBuffer();
    buildCountersValue(element, counterName, separator, cstyle, buffer);
    return new CSSStringValue(CSSStringType.STRING, buffer.toString());
  }


  private void buildCountersValue (final LayoutElement element,
                                   final String counterName,
                                   final String separator,
                                   final CounterStyle cstyle,
                                   final StringBuffer buffer)
  {
    if (element.getParent() != null)
    {
      buildCountersValue(element.getParent(), counterName, separator, cstyle, buffer);
    }

    ElementContext elementContext = element.getElementContext();
    if (elementContext.isCounterDefined(counterName))
    {
      if (buffer.length() != 0)
      {
        buffer.append(separator);
      }

      final int cval = elementContext.getCounterValue(counterName);
      final String counterText = cstyle.getCounterValue(cval);
      buffer.append(counterText);
    }
  }

}
