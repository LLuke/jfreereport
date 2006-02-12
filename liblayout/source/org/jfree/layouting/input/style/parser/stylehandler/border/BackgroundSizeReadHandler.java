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
 * $Id: BackgroundSizeReadHandler.java,v 1.1 2006/02/12 21:57:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 26.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser.stylehandler.border;

import java.util.ArrayList;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.border.BackgroundSize;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.CSSValueReadHandler;
import org.jfree.layouting.input.style.values.CSSAutoValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 26.11.2005, 18:29:10
 *
 * @author Thomas Morgner
 */
public class BackgroundSizeReadHandler implements CSSValueReadHandler
{

  public BackgroundSizeReadHandler()
  {
  }

  public CSSValue createValue(StyleKey name, LexicalUnit value) 
  {
    ArrayList values = new ArrayList();

    while (value != null)
    {
      CSSValue firstValue;
      if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
      {
        if (value.getStringValue().equalsIgnoreCase("round"))
        {
          values.add(CSSAutoValue.getInstance());
          values.add(CSSAutoValue.getInstance());
          values.add(BackgroundSize.ROUND);

          value = CSSValueFactory.parseComma(value);
          continue;
        }
        else if (value.getStringValue().equalsIgnoreCase("auto"))
        {
          firstValue = CSSAutoValue.getInstance();
        }
        else
        {
          return null;
        }
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE)
      {
        firstValue = new CSSNumericValue(CSSNumericType.PERCENTAGE, value.getFloatValue());
      }
      else
      {
        firstValue = CSSValueFactory.createLengthValue(value);
        if (firstValue == null)
        {
          return null;
        }
      }

      value = value.getNextLexicalUnit();
      if (value == null)
      {
        values.add(firstValue);
        values.add(CSSAutoValue.getInstance());
        values.add(BackgroundSize.ROUND);
        continue;
      }

      CSSValue secondValue;
      if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
      {
        if (value.getStringValue().equalsIgnoreCase("round"))
        {
          values.add(firstValue);
          values.add(CSSAutoValue.getInstance());
          values.add(BackgroundSize.ROUND);

          value = CSSValueFactory.parseComma(value);
          continue;
        }
        else if (value.getStringValue().equalsIgnoreCase("auto"))
        {
          secondValue = CSSAutoValue.getInstance();
        }
        else
        {
          return null;
        }
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_OPERATOR_COMMA)
      {
        values.add(firstValue);
        values.add(CSSAutoValue.getInstance());
        values.add(BackgroundSize.ROUND);
        value = value.getNextLexicalUnit();
        continue;
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE)
      {
        secondValue = new CSSNumericValue(CSSNumericType.PERCENTAGE, value.getFloatValue());
      }
      else
      {
        secondValue = CSSValueFactory.createLengthValue(value);
        if (secondValue == null)
        {
          return null;
        }
      }

      value = value.getNextLexicalUnit();
      values.add(firstValue);
      values.add(secondValue);

      if (value == null)
      {
        values.add(BackgroundSize.NO_ROUND);
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_OPERATOR_COMMA)
      {
        values.add(BackgroundSize.NO_ROUND);
        value = value.getNextLexicalUnit();
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
      {
        if (value.getStringValue().equalsIgnoreCase("round") == false)
        {
          return null;
        }
        values.add(BackgroundSize.ROUND);
        value = CSSValueFactory.parseComma(value);
      }
      else
      {
        return null;
      }
    }

    return new CSSValueList(values);
  }

}
