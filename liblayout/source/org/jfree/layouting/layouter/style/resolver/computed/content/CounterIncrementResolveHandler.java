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
 * CounterIncrementResolveHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: CounterIncrementResolveHandler.java,v 1.2 2006/04/17 20:51:15 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.layouting.layouter.style.resolver.computed.content;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.content.ContentStyleKeys;
import org.jfree.layouting.input.style.values.CSSAttrFunction;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.input.style.values.CSSValuePair;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;

public class CounterIncrementResolveHandler implements ResolveHandler
{
  public CounterIncrementResolveHandler ()
  {
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more dependent
   * is the resolver on other resolvers to be complete.
   *
   * @return the array of required style keys.
   */
  public StyleKey[] getRequiredStyles ()
  {
    return new StyleKey[] {
            ContentStyleKeys.COUNTER_RESET,
    };
  }

  /**
   * Resolves a single property.
   *
   * @param currentNode
   * @param style
   */
  public void resolve (final LayoutProcess process,
                       final LayoutElement element,
                       final LayoutStyle style,
                       final StyleKey key)
  {
    final CSSValue value = style.getValue(key);
    if (value instanceof CSSValueList == false)
    {
      return; // do nothing.
    }

    final CSSValueList valueList = (CSSValueList) value;
    for (int i = 0; i < valueList.getLength(); i++)
    {
      final CSSValue item = valueList.getItem(i);
      if (item instanceof CSSValuePair == false)
      {
        continue;
      }
      CSSValuePair counter = (CSSValuePair) item;
      final CSSValue counterName = counter.getFirstValue();
      if (counterName instanceof CSSConstant == false)
      {
        continue;
      }

      final CSSValue counterValue = counter.getSecondValue();
      final int counterIntValue = parseCounterValue(counterValue, element);
      element.incrementCounter(counterName.getCSSText(), counterIntValue);
    }
  }

  private int parseCounterValue (final CSSValue rawValue,
                                 final LayoutElement element)
  {

    if (rawValue instanceof CSSNumericValue)
    {
      final CSSNumericValue nval = (CSSNumericValue) rawValue;
      return nval.intValue();
    }
    if (rawValue instanceof CSSAttrFunction)
    {
      final CSSAttrFunction attrFunction = (CSSAttrFunction) rawValue;
      final String attrName = attrFunction.getName();
      final String attrNamespace = attrFunction.getNamespace();
      final Object rawAttribute =
              element.getLayoutContext().getAttributes().getAttribute
                      (attrNamespace, attrName);
      if (rawAttribute instanceof Number)
      {
        final Number nAttr = (Number) rawAttribute;
        return nAttr.intValue();
      }
    }
    return 0;
  }
}
