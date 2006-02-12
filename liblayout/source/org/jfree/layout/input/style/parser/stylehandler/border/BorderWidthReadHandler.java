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
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 27.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser.stylehandler.border;

import java.util.HashMap;
import java.util.Map;

import org.jfree.layouting.input.style.keys.border.BorderStyleKeys;
import org.jfree.layouting.input.style.keys.border.BorderWidth;
import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.parser.stylehandler.AbstractWidthReadHandler;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 27.11.2005, 19:07:11
 *
 * @author Thomas Morgner
 */
public class BorderWidthReadHandler extends AbstractWidthReadHandler
        implements CSSCompoundValueReadHandler
{
  public BorderWidthReadHandler()
  {
    super(true, false);
  }

  protected BorderWidthReadHandler(boolean allowPercentages, boolean allowAuto)
  {
    super(allowPercentages, allowAuto);
  }

  protected CSSValue parseWidth(final LexicalUnit value)
  {
    if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      if (value.getStringValue().equalsIgnoreCase("thin"))
      {
        return BorderWidth.THIN;
      }
      if (value.getStringValue().equalsIgnoreCase("medium"))
      {
        return BorderWidth.MEDIUM;
      }
      if (value.getStringValue().equalsIgnoreCase("thick"))
      {
        return BorderWidth.THICK;
      }
    }
    return super.parseWidth(value);
  }

  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public Map createValues(LexicalUnit unit)
  {
    final CSSValue topWidth = parseWidth(unit);
    if (topWidth == null)
    {
      return null;
    }

    unit = unit.getNextLexicalUnit();

    final CSSValue rightWidth;
    if (unit == null)
    {
      rightWidth = topWidth;
    }
    else
    {
      rightWidth = parseWidth(unit);
      if (rightWidth == null)
      {
        return null;
      }
      unit = unit.getNextLexicalUnit();
    }

    final CSSValue bottomWidth;
    if (unit == null)
    {
      bottomWidth = topWidth;
    }
    else
    {
      bottomWidth = parseWidth(unit);
      if (bottomWidth == null)
      {
        return null;
      }
      unit = unit.getNextLexicalUnit();
    }

    final CSSValue leftWidth;
    if (unit == null)
    {
      leftWidth = rightWidth;
    }
    else
    {
      leftWidth = parseWidth(unit);
      if (leftWidth == null)
      {
        return null;
      }
    }

    final Map map = new HashMap();
    map.put(BorderStyleKeys.BORDER_TOP_WIDTH, topWidth);
    map.put(BorderStyleKeys.BORDER_RIGHT_WIDTH, rightWidth);
    map.put(BorderStyleKeys.BORDER_BOTTOM_WIDTH, bottomWidth);
    map.put(BorderStyleKeys.BORDER_LEFT_WIDTH, leftWidth);
    return map;
  }
}
