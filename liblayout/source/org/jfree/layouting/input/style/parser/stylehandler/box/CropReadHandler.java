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
 * CropReadHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: CropReadHandler.java,v 1.2 2006/04/17 20:51:03 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.input.style.parser.stylehandler.box;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.CSSValueReadHandler;
import org.jfree.layouting.input.style.values.CSSAutoValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSRectangleType;
import org.jfree.layouting.input.style.values.CSSRectangleValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 28.11.2005, 15:36:05
 *
 * @author Thomas Morgner
 */
public class CropReadHandler implements CSSValueReadHandler
{
  public CropReadHandler()
  {
  }

  public CSSValue createValue(StyleKey name, LexicalUnit value)
  {
    if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      final String stringValue = value.getStringValue();
      if (stringValue.equalsIgnoreCase("auto") ||
          stringValue.equalsIgnoreCase("none"))
      {
        return CSSAutoValue.getInstance();
      }
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_FUNCTION)
    {
      if (value.getFunctionName().equalsIgnoreCase("inset-rect"))
      {
        return getRectangle(CSSRectangleType.INSET_RECT, value.getParameters());
      }
      return null;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_RECT_FUNCTION)
    {
      return getRectangle(CSSRectangleType.RECT, value.getParameters());
    }
    return null;
  }


  private static CSSRectangleValue getRectangle
          (CSSRectangleType type, LexicalUnit value)
  {
    final CSSNumericValue[] list = new CSSNumericValue[4];
    for (int index = 0; index < 4; index++)
    {
      if (value == null)
      {
        return null;
      }
      CSSNumericValue nval = CSSValueFactory.createLengthValue(value);
      if (nval != null)
      {
        list[index] = nval;
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE)
      {
        list[index] = new CSSNumericValue(CSSNumericType.PERCENTAGE, value.getFloatValue());
      }
      else
      {
        return null;
      }
      value = CSSValueFactory.parseComma(value);
    }

    return new CSSRectangleValue (type, list[0], list[1], list[2], list[3]);
  }

}
