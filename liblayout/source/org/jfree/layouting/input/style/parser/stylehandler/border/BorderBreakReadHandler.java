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
 * BorderBreakReadHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: BorderBreakReadHandler.java,v 1.2 2006/04/17 20:51:03 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.input.style.parser.stylehandler.border;

import java.util.Map;
import java.util.HashMap;

import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.stylehandler.color.ColorReadHandler;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.keys.border.BorderStyle;
import org.jfree.layouting.input.style.keys.border.BorderStyleKeys;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 27.11.2005, 19:52:12
 *
 * @author Thomas Morgner
 */
public class BorderBreakReadHandler extends BorderStyleReadHandler
        implements CSSCompoundValueReadHandler
{
  public BorderBreakReadHandler()
  {
  }

  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public Map createValues(LexicalUnit unit)
  {
    final CSSValue width = CSSValueFactory.createLengthValue(unit);
    if (width != null)
    {
      unit = unit.getNextLexicalUnit();
    }

    final CSSConstant style;
    if (unit != null)
    {
      style = (CSSConstant) lookupValue(unit);
      if (style != null)
      {
        unit = unit.getNextLexicalUnit();
      }
    }
    else
    {
      style = null;
    }

    final CSSValue color;
    if (unit != null)
    {
      color = ColorReadHandler.createColorValue(unit);
    }
    else
    {
      color = null;
    }

    final Map map = new HashMap();
    if (width != null)
    {
      map.put(BorderStyleKeys.BORDER_BREAK_WIDTH, width);
    }
    if (style != null)
    {
      map.put(BorderStyleKeys.BORDER_BREAK_STYLE, style);
    }
    if (color != null)
    {
      map.put(BorderStyleKeys.BORDER_BREAK_COLOR, color);
    }
    return map;
  }
}
