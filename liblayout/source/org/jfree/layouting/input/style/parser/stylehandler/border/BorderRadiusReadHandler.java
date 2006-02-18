/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * BorderReadHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: BorderRadiusReadHandler.java,v 1.1 2006/02/12 21:57:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 27.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser.stylehandler.border;

import java.util.HashMap;
import java.util.Map;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.border.BorderRadiusValue;
import org.jfree.layouting.input.style.keys.border.BorderStyleKeys;
import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.CSSValueReadHandler;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * This looks a bit funny, as if the standard has not been completed.
 * THe compound property may change ...
 *
 * @author Thomas Morgner
 */
public class BorderRadiusReadHandler implements CSSValueReadHandler, CSSCompoundValueReadHandler
{
  public BorderRadiusReadHandler()
  {
  }

  public CSSValue createValue(StyleKey name, LexicalUnit value)
  {
    CSSNumericValue firstValue = CSSValueFactory.createLengthValue(value);
    if (firstValue == null)
    {
      return null;
    }
    value = value.getNextLexicalUnit();
    CSSNumericValue secondValue;
    if (value == null)
    {
      secondValue = firstValue;
    }
    else
    {
      secondValue = CSSValueFactory.createLengthValue(value);
      if (secondValue == null)
      {
        return null;
      }
    }

    return new BorderRadiusValue(firstValue, secondValue);
  }

  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public Map createValues(LexicalUnit unit)
  {
    final CSSValue value = createValue(null, unit);
    if (value == null)
    {
      return null;
    }

    final Map map = new HashMap();
    map.put(BorderStyleKeys.BORDER_TOP_RIGHT_RADIUS, value);
    map.put(BorderStyleKeys.BORDER_BOTTOM_RIGHT_RADIUS, value);
    map.put(BorderStyleKeys.BORDER_BOTTOM_LEFT_RADIUS, value);
    map.put(BorderStyleKeys.BORDER_TOP_LEFT_RADIUS, value);
    return map;
  }
}