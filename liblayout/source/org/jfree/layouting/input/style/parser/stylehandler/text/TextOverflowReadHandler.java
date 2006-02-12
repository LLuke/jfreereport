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
 * TextOverflowReadHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: TextOverflowReadHandler.java,v 1.1 2006/02/12 21:57:22 taqua Exp $
 *
 * Changes
 * -------------------------
 * 02.12.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser.stylehandler.text;

import java.util.HashMap;
import java.util.Map;

import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSInheritValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 02.12.2005, 19:36:00
 *
 * @author Thomas Morgner
 */
public class TextOverflowReadHandler implements CSSCompoundValueReadHandler
{
  private TextOverflowModeReadHandler modeReadHandler;
  private TextOverflowEllipsisReadHandler ellipsisReadHandler;

  public TextOverflowReadHandler()
  {
    modeReadHandler = new TextOverflowModeReadHandler();
    ellipsisReadHandler = new TextOverflowEllipsisReadHandler();
  }

  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public Map createValues(LexicalUnit unit)
  {
    if (unit.getLexicalUnitType() == LexicalUnit.SAC_INHERIT)
    {
      Map map = new HashMap();
      map.put(TextStyleKeys.TEXT_OVERFLOW_MODE, CSSInheritValue.getInstance());
      map.put(TextStyleKeys.TEXT_OVERFLOW_ELLIPSIS, CSSInheritValue.getInstance());
      return map;
    }


    CSSValue mode = modeReadHandler.createValue(null, unit);
    if (mode != null)
    {
      unit = unit.getNextLexicalUnit();
    }
    CSSValue ellipsis;
    if (unit != null)
    {
      ellipsis = ellipsisReadHandler.createValue(null, unit);
    }
    else
    {
      ellipsis = null;
    }
    Map map = new HashMap();
    if (mode != null)
    {
      map.put(TextStyleKeys.TEXT_OVERFLOW_MODE, mode);
    }
    if (ellipsis != null)
    {
      map.put(TextStyleKeys.TEXT_OVERFLOW_ELLIPSIS, ellipsis);
    }
    return map;
  }
}
