/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
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

  private CSSValueList createList (final CSSValue first,
                                   final CSSValue second,
                                   final CSSValue third)
  {
    return new CSSValueList(new CSSValue[]{first, second, third});
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
          values.add(createList(CSSAutoValue.getInstance(),
                  CSSAutoValue.getInstance(),
                  BackgroundSize.ROUND));

          value = CSSValueFactory.parseComma(value);
          continue;
        }

        if (value.getStringValue().equalsIgnoreCase("auto"))
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
        values.add(createList(firstValue,
                  CSSAutoValue.getInstance(),
                  BackgroundSize.ROUND));
        continue;
      }

      CSSValue secondValue;
      if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
      {
        if (value.getStringValue().equalsIgnoreCase("round"))
        {
          values.add(createList(firstValue,
                  CSSAutoValue.getInstance(),
                  BackgroundSize.ROUND));
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
        values.add(createList(firstValue,
                  CSSAutoValue.getInstance(),
                  BackgroundSize.ROUND));
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
      if (value == null)
      {
        values.add(createList(firstValue,
                  secondValue,
                  BackgroundSize.NO_ROUND));
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_OPERATOR_COMMA)
      {
        values.add(createList(firstValue,
                  secondValue,
                  BackgroundSize.NO_ROUND));
        value = value.getNextLexicalUnit();
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
      {
        if (value.getStringValue().equalsIgnoreCase("round") == false)
        {
          return null;
        }
        values.add(createList(firstValue,
                  secondValue,
                  BackgroundSize.ROUND));
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
