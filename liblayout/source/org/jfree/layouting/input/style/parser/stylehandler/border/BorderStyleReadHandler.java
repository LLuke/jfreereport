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
 * BorderStyleReadHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: BorderStyleReadHandler.java,v 1.1 2006/02/12 21:57:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 27.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser.stylehandler.border;

import java.util.HashMap;
import java.util.Map;

import org.jfree.layouting.input.style.keys.border.BorderStyle;
import org.jfree.layouting.input.style.keys.border.BorderStyleKeys;
import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.parser.stylehandler.OneOfConstantsReadHandler;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 27.11.2005, 19:17:22
 *
 * @author Thomas Morgner
 */
public class BorderStyleReadHandler extends OneOfConstantsReadHandler
  implements CSSCompoundValueReadHandler
{
  public BorderStyleReadHandler()
  {
    super( false);
    addValue(BorderStyle.DASHED);
    addValue(BorderStyle.DOT_DASH);
    addValue(BorderStyle.DOT_DOT_DASH);
    addValue(BorderStyle.DOTTED);
    addValue(BorderStyle.DOUBLE);
    addValue(BorderStyle.GROOVE);
    addValue(BorderStyle.HIDDEN);
    addValue(BorderStyle.INSET);
    addValue(BorderStyle.NONE);
    addValue(BorderStyle.OUTSET);
    addValue(BorderStyle.RIDGE);
    addValue(BorderStyle.SOLID);
    addValue(BorderStyle.WAVE);
  }


  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public Map createValues(LexicalUnit unit)
  {
    final BorderStyle topStyle = (BorderStyle) lookupValue(unit);
    if (topStyle == null)
    {
      return null;
    }

    unit = unit.getNextLexicalUnit();

    final BorderStyle rightStyle;
    if (unit == null)
    {
      rightStyle = topStyle;
    }
    else
    {
      rightStyle = (BorderStyle) lookupValue(unit);
      if (rightStyle == null)
      {
        return null;
      }
      unit = unit.getNextLexicalUnit();
    }

    final BorderStyle bottomStyle;
    if (unit == null)
    {
      bottomStyle = topStyle;
    }
    else
    {
      bottomStyle = (BorderStyle) lookupValue(unit);
      if (bottomStyle == null)
      {
        return null;
      }
      unit = unit.getNextLexicalUnit();
    }

    final BorderStyle leftStyle;
    if (unit == null)
    {
      leftStyle = rightStyle;
    }
    else
    {
      leftStyle = (BorderStyle) lookupValue(unit);
      if (leftStyle == null)
      {
        return null;
      }
    }

    final Map map = new HashMap();
    map.put(BorderStyleKeys.BORDER_TOP_STYLE, topStyle);
    map.put(BorderStyleKeys.BORDER_RIGHT_STYLE, rightStyle);
    map.put(BorderStyleKeys.BORDER_BOTTOM_STYLE, bottomStyle);
    map.put(BorderStyleKeys.BORDER_LEFT_STYLE, leftStyle);
    return map;
  }
}