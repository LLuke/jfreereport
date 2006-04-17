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
 * LineStackingReadHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.input.style.parser.stylehandler.line;

import java.util.HashMap;
import java.util.Map;

import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 28.11.2005, 18:06:12
 *
 * @author Thomas Morgner
 */
public class LineStackingReadHandler implements CSSCompoundValueReadHandler
{
  private LineStackingRubyReadHandler rubyReadHandler;
  private LineStackingShiftReadHandler shiftReadHandler;
  private LineStackingStrategyReadHandler strategyReadHandler;

  public LineStackingReadHandler()
  {
    rubyReadHandler = new LineStackingRubyReadHandler();
    shiftReadHandler = new LineStackingShiftReadHandler();
    strategyReadHandler = new LineStackingStrategyReadHandler();
  }

  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public Map createValues(LexicalUnit unit)
  {
    CSSValue rubyValue = rubyReadHandler.createValue(null, unit);
    if (rubyValue != null)
    {
      unit = unit.getNextLexicalUnit();
    }

    CSSValue shiftValue;
    if (unit != null)
    {
      shiftValue = shiftReadHandler.createValue(null, unit);
      if (shiftValue != null)
      {
        unit = unit.getNextLexicalUnit();
      }
    }
    else
    {
      shiftValue = null;
    }

    CSSValue strategy;
    if (unit != null)
    {
      strategy = strategyReadHandler.createValue(null, unit);
    }
    else
    {
      strategy = null;
    }

    final Map map = new HashMap();
    if (rubyValue != null)
    {
      map.put(LineStyleKeys.LINE_STACKING_RUBY, rubyValue);
    }
    if (shiftValue != null)
    {
      map.put(LineStyleKeys.LINE_STACKING_SHIFT, shiftValue);
    }
    if (strategy != null)
    {
      map.put(LineStyleKeys.LINE_STACKING_STRATEGY, strategy);
    }
    return map;
  }
}
