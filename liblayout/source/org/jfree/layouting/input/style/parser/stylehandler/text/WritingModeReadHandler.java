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
 * WritingModeReadHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: WritingModeReadHandler.java,v 1.1 2006/02/12 21:57:22 taqua Exp $
 *
 * Changes
 * -------------------------
 * 02.12.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser.stylehandler.text;

import java.util.Map;
import java.util.HashMap;

import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSInheritValue;
import org.jfree.layouting.input.style.keys.text.Direction;
import org.jfree.layouting.input.style.keys.text.BlockProgression;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 02.12.2005, 17:38:27
 *
 * @author Thomas Morgner
 */
public class WritingModeReadHandler implements CSSCompoundValueReadHandler
{
  public WritingModeReadHandler()
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
    if (unit.getLexicalUnitType() == LexicalUnit.SAC_INHERIT)
    {
      Map map = new HashMap();
      map.put (TextStyleKeys.DIRECTION, CSSInheritValue.getInstance());
      map.put (TextStyleKeys.BLOCK_PROGRESSION, CSSInheritValue.getInstance());
      return map;
    }

    if (unit.getLexicalUnitType() != LexicalUnit.SAC_IDENT)
    {
      return null;
    }

    CSSValue direction;
    CSSValue blockProgression;
    final String strValue = unit.getStringValue();
    // lr-tb | rl-tb | tb-rl | tb-lr
    if (strValue.equalsIgnoreCase("lr-tb"))
    {
      direction = Direction.LTR;
      blockProgression = BlockProgression.TB;
    }
    else if (strValue.equalsIgnoreCase("rl-tb"))
    {
      direction = Direction.RTL;
      blockProgression = BlockProgression.TB;
    }
    else if (strValue.equalsIgnoreCase("tb-rl"))
    {
      direction = Direction.LTR;
      blockProgression = BlockProgression.RL;
    }
    else if (strValue.equalsIgnoreCase("tb-lr"))
    {
      direction = Direction.RTL;
      blockProgression = BlockProgression.LR;
    }
    else
    {
      return null;
    }

    Map map = new HashMap();
    map.put (TextStyleKeys.DIRECTION, direction);
    map.put (TextStyleKeys.BLOCK_PROGRESSION, blockProgression);
    return map;
  }
}