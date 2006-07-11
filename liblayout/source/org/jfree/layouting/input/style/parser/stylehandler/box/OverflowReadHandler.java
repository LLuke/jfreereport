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
 * OverflowReadHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: OverflowReadHandler.java,v 1.2 2006/04/17 20:51:03 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.input.style.parser.stylehandler.box;

import java.util.Map;
import java.util.HashMap;

import org.jfree.layouting.input.style.parser.stylehandler.OneOfConstantsReadHandler;
import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.keys.box.Overflow;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.StyleKey;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 28.11.2005, 16:06:42
 *
 * @author Thomas Morgner
 */
public class OverflowReadHandler extends OneOfConstantsReadHandler
  implements CSSCompoundValueReadHandler
{
  public OverflowReadHandler()
  {
    super(true);
    addValue(Overflow.HIDDEN);
    addValue(Overflow.VISIBLE);
    addValue(Overflow.SCROLL);
  }

  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public Map createValues(LexicalUnit unit)
  {
    final CSSValue value = lookupValue(unit);
    if (value == null)
    {
      return null;
    }

    final Map map = new HashMap();
    map.put (BoxStyleKeys.OVERFLOW_X, value);
    map.put (BoxStyleKeys.OVERFLOW_Y, value);
    return map;
  }

  public StyleKey[] getAffectedKeys()
  {
    return new StyleKey[] {
            BoxStyleKeys.OVERFLOW_X,
            BoxStyleKeys.OVERFLOW_Y
    };
  }
}
