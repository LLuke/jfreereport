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
 * ListStyleReadHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: ListStyleReadHandler.java,v 1.1 2006/02/12 21:57:21 taqua Exp $
 *
 * Changes
 * -------------------------
 * 01.12.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser.stylehandler.list;

import java.util.HashMap;
import java.util.Map;

import org.jfree.layouting.input.style.keys.list.ListStyleKeys;
import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 01.12.2005, 19:36:10
 *
 * @author Thomas Morgner
 */
public class ListStyleReadHandler implements CSSCompoundValueReadHandler
{
  private ListStyleImageReadHandler imageReadHandler;
  private ListStylePositionReadHandler positionReadHandler;
  private ListStyleTypeReadHandler typeReadHandler;

  public ListStyleReadHandler()
  {
    imageReadHandler = new ListStyleImageReadHandler();
    positionReadHandler = new ListStylePositionReadHandler();
    typeReadHandler = new ListStyleTypeReadHandler();
  }

  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public Map createValues(LexicalUnit unit)
  {
    CSSValue type = typeReadHandler.createValue(null, unit);
    if (type != null)
    {
      unit = unit.getNextLexicalUnit();
    }
    CSSValue position = null;
    if (unit != null)
    {
      position = positionReadHandler.createValue(null, unit);
      if (position != null)
      {
        unit = unit.getNextLexicalUnit();
      }
    }
    CSSValue image = null;
    if (unit != null)
    {
      image = imageReadHandler.createValue(null, unit);
    }

    Map map = new HashMap();
    if (type != null)
    {
      map.put(ListStyleKeys.LIST_STYLE_TYPE, type);
    }
    if (position != null)
    {
      map.put(ListStyleKeys.LIST_STYLE_POSITION, position);
    }
    if (image != null)
    {
      map.put(ListStyleKeys.LIST_STYLE_IMAGE, image);
    }
    return map;
  }
}
