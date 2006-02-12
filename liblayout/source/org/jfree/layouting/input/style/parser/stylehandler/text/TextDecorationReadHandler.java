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
 * TextDecorationReadHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: TextDecorationReadHandler.java,v 1.1 2006/02/12 21:57:22 taqua Exp $
 *
 * Changes
 * -------------------------
 * 03.12.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser.stylehandler.text;

import java.util.Map;
import java.util.HashMap;

import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.parser.stylehandler.OneOfConstantsReadHandler;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSAutoValue;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.keys.text.TextDecorationMode;
import org.jfree.layouting.input.style.keys.text.TextDecorationStyle;
import org.jfree.layouting.input.style.keys.color.CSSSystemColors;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 03.12.2005, 19:06:09
 *
 * @author Thomas Morgner
 */
public class TextDecorationReadHandler extends OneOfConstantsReadHandler
        implements CSSCompoundValueReadHandler
{
  public TextDecorationReadHandler()
  {
    super(false);
    addValue(new CSSConstant("none"));
    addValue(new CSSConstant("underline"));
    addValue(new CSSConstant("overline"));
    addValue(new CSSConstant("line-through"));
    addValue(new CSSConstant("blink"));
  }

  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public Map createValues(LexicalUnit unit)
  {
    final Map map = new HashMap();
    map.put(TextStyleKeys.TEXT_UNDERLINE_POSITION, CSSAutoValue.getInstance());
    map.put(TextStyleKeys.TEXT_UNDERLINE_MODE, TextDecorationMode.CONTINUOUS);
    map.put(TextStyleKeys.TEXT_OVERLINE_MODE, TextDecorationMode.CONTINUOUS);
    map.put(TextStyleKeys.TEXT_LINE_THROUGH_MODE, TextDecorationMode.CONTINUOUS);
    map.put(TextStyleKeys.TEXT_UNDERLINE_COLOR, CSSSystemColors.CURRENT_COLOR);
    map.put(TextStyleKeys.TEXT_OVERLINE_COLOR, CSSSystemColors.CURRENT_COLOR);
    map.put(TextStyleKeys.TEXT_LINE_THROUGH_COLOR, CSSSystemColors.CURRENT_COLOR);
    map.put(TextStyleKeys.TEXT_UNDERLINE_WIDTH, CSSAutoValue.getInstance());
    map.put(TextStyleKeys.TEXT_OVERLINE_WIDTH, CSSAutoValue.getInstance());
    map.put(TextStyleKeys.TEXT_LINE_THROUGH_WIDTH, CSSAutoValue.getInstance());
    map.put(TextStyleKeys.TEXT_UNDERLINE_STYLE, TextDecorationStyle.NONE);
    map.put(TextStyleKeys.TEXT_OVERLINE_STYLE, TextDecorationStyle.NONE);
    map.put(TextStyleKeys.TEXT_LINE_THROUGH_STYLE, TextDecorationStyle.NONE);

    while (unit != null)
    {
      CSSValue constant = lookupValue(unit);
      if (constant == null)
      {
        return null;
      }
      if (constant.getCSSText().equals("none"))
      {
        map.put(TextStyleKeys.TEXT_UNDERLINE_STYLE, TextDecorationStyle.NONE);
        map.put(TextStyleKeys.TEXT_OVERLINE_STYLE, TextDecorationStyle.NONE);
        map.put(TextStyleKeys.TEXT_LINE_THROUGH_STYLE, TextDecorationStyle.NONE);
        return map;
      }
      if (constant.getCSSText().equals("blink"))
      {
        map.put(TextStyleKeys.TEXT_BLINK, new CSSConstant("blink"));
      }
      else if (constant.getCSSText().equals("underline"))
      {
        map.put(TextStyleKeys.TEXT_UNDERLINE_STYLE, TextDecorationStyle.NONE);
      }
      else if (constant.getCSSText().equals("overline"))
      {
        map.put(TextStyleKeys.TEXT_OVERLINE_STYLE, TextDecorationStyle.NONE);
      }
      else if (constant.getCSSText().equals("line-through"))
      {
        map.put(TextStyleKeys.TEXT_LINE_THROUGH_STYLE, TextDecorationStyle.NONE);
      }
      unit = unit.getNextLexicalUnit();
    }
    return map;
  }
}
