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
 * BackgroundPositionReadHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 26.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser.stylehandler.border;

import java.util.ArrayList;

import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.stylehandler.ListOfPairReadHandler;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 26.11.2005, 18:29:10
 *
 * @author Thomas Morgner
 */
public class BackgroundPositionReadHandler extends ListOfPairReadHandler
{
  public static final CSSNumericValue CENTER =
          new CSSNumericValue(CSSNumericType.PERCENTAGE, 50);
  public static final CSSNumericValue TOP =
          new CSSNumericValue(CSSNumericType.PERCENTAGE, 0);
  public static final CSSNumericValue LEFT =
          new CSSNumericValue(CSSNumericType.PERCENTAGE, 0);
  public static final CSSNumericValue BOTTOM =
          new CSSNumericValue(CSSNumericType.PERCENTAGE, 100);
  public static final CSSNumericValue RIGHT =
          new CSSNumericValue(CSSNumericType.PERCENTAGE, 100);

  public BackgroundPositionReadHandler()
  {
  }

  protected CSSValue parseFirstPosition(final LexicalUnit value)
  {
    if (value == null)
    {
      return null;
    }

    if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      if ("left".equalsIgnoreCase(value.getStringValue()))
      {
        return LEFT;
      }
      else if ("center".equalsIgnoreCase(value.getStringValue()))
      {
        return CENTER;
      }
      else if ("right".equalsIgnoreCase(value.getStringValue()))
      {
        return RIGHT;
      }
      else if ("top".equalsIgnoreCase(value.getStringValue()))
      {
        return TOP;
      }
      else if ("bottom".equalsIgnoreCase(value.getStringValue()))
      {
        return BOTTOM;
      }

      // ignore this rule.
      return null;
    }

    if (value.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE)
    {
      return new CSSNumericValue(CSSNumericType.PERCENTAGE,
              value.getFloatValue());
    }
    if (CSSValueFactory.isLengthValue(value))
    {
      return CSSValueFactory.createLengthValue(value);
    }
    // contains errors, we ignore this rule.
    return null;
  }

  protected void addToResultList(ArrayList values,
                                 CSSValue firstPosition,
                                 CSSValue secondPosition)
  {
    if (firstPosition == TOP || firstPosition == BOTTOM)
    {
      super.addToResultList(values, secondPosition, firstPosition);
    }
    else if (secondPosition == LEFT || secondPosition == RIGHT)
    {
      super.addToResultList(values, secondPosition, firstPosition);
    }
    else
    {
      super.addToResultList(values, firstPosition, secondPosition);
    }
  }

  protected CSSValue parseSecondPosition(final LexicalUnit value,
                                         final CSSValue firstValue)
  {
    if (value == null)
    {
      return CENTER;
    }
    if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      if ("left".equalsIgnoreCase(value.getStringValue()))
      {
        return LEFT;
      }
      else if ("center".equalsIgnoreCase(value.getStringValue()))
      {
        return CENTER;
      }
      else if ("right".equalsIgnoreCase(value.getStringValue()))
      {
        return RIGHT;
      }
      else if ("top".equalsIgnoreCase(value.getStringValue()))
      {
        return TOP;
      }
      else if ("bottom".equalsIgnoreCase(value.getStringValue()))
      {
        return BOTTOM;
      }
      return null; // ignore this rule, it contains errors.
    }
    if (value.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE)
    {
      return new CSSNumericValue(CSSNumericType.PERCENTAGE,
              value.getFloatValue());
    }
    else if (CSSValueFactory.isLengthValue(value))
    {
      return CSSValueFactory.createLengthValue(value);
    }
    return CENTER;
  }
}
