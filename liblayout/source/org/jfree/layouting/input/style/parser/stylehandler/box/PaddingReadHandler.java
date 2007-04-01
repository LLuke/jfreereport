/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/liblayout/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.box;

import java.util.HashMap;
import java.util.Map;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.parser.stylehandler.AbstractWidthReadHandler;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 27.11.2005, 19:07:11
 *
 * @author Thomas Morgner
 */
public class PaddingReadHandler extends AbstractWidthReadHandler
        implements CSSCompoundValueReadHandler
{
  public PaddingReadHandler()
  {
    super(true, false);
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
    map.put(BoxStyleKeys.PADDING_TOP, topWidth);
    map.put(BoxStyleKeys.PADDING_RIGHT, rightWidth);
    map.put(BoxStyleKeys.PADDING_BOTTOM, bottomWidth);
    map.put(BoxStyleKeys.PADDING_LEFT, leftWidth);
    return map;
  }

  public StyleKey[] getAffectedKeys()
  {
    return new StyleKey[] {
            BoxStyleKeys.PADDING_TOP,
            BoxStyleKeys.PADDING_RIGHT,
            BoxStyleKeys.PADDING_BOTTOM,
            BoxStyleKeys.PADDING_LEFT
    };
  }
}
