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
 * CounterModificationReadHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: CounterModificationReadHandler.java,v 1.1 2006/02/12 21:57:20 taqua Exp $
 *
 * Changes
 * -------------------------
 * 01.12.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser.stylehandler.content;

import java.util.ArrayList;

import org.jfree.layouting.input.style.parser.CSSValueReadHandler;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSCounterValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.StyleKey;
import org.w3c.css.sac.LexicalUnit;

/**
 * Handles both the counter-increment and the counter-reset
 *
 * @author Thomas Morgner
 */
public class CounterModificationReadHandler implements CSSValueReadHandler
{
  public static final CSSNumericValue ZERO =
          new CSSNumericValue(CSSNumericType.NUMBER, 0);

  public CounterModificationReadHandler()
  {
  }

  public CSSValue createValue(StyleKey name, LexicalUnit value)
  {
    final ArrayList counterSpecs = new ArrayList();
    while (value != null)
    {
      if (value.getLexicalUnitType() != LexicalUnit.SAC_IDENT)
      {
        return null;
      }
      final String identifier = value.getStringValue();
      if ("none".equalsIgnoreCase(identifier))
      {
        return new CSSConstant("none");
      }
      value = value.getNextLexicalUnit();
      CSSValue counterValue = ZERO;
      if (value != null)
      {
        if (value.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
        {
          counterValue = new CSSNumericValue
                  (CSSNumericType.NUMBER, value.getIntegerValue());
          value = value.getNextLexicalUnit();
        }
        else if (value.getLexicalUnitType() == LexicalUnit.SAC_ATTR ||
                 value.getLexicalUnitType() == LexicalUnit.SAC_FUNCTION)
        {
          counterValue = CSSValueFactory.parseAttrFunction(value);
          value = value.getNextLexicalUnit();
        }
      }
      counterSpecs.add(new CSSCounterValue(identifier, counterValue));
    }

    return new CSSValueList(counterSpecs);
  }
}
