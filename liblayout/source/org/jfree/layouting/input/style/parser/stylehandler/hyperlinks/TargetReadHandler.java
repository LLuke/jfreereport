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
 * TargetReadHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: TargetReadHandler.java,v 1.1 2006/02/12 21:57:21 taqua Exp $
 *
 * Changes
 * -------------------------
 * 28.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser.stylehandler.hyperlinks;

import java.util.Map;
import java.util.HashMap;

import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.keys.hyperlinks.HyperlinkStyleKeys;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 28.11.2005, 19:34:19
 *
 * @author Thomas Morgner
 */
public class TargetReadHandler implements CSSCompoundValueReadHandler
{
  private TargetNameReadHandler nameReadHandler;
  private TargetNewReadHandler newReadHandler;
  private TargetPositionReadHandler positionReadHandler;

  public TargetReadHandler()
  {
    nameReadHandler = new TargetNameReadHandler();
    newReadHandler = new TargetNewReadHandler();
    positionReadHandler = new TargetPositionReadHandler();
  }

  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public Map createValues(LexicalUnit unit)
  {
    CSSValue nameValue = nameReadHandler.createValue(null, unit);
    if (nameValue != null)
    {
      unit = unit.getNextLexicalUnit();
    }

    CSSValue newValue = null;
    if (unit != null)
    {
      newValue = newReadHandler.createValue(null, unit);
      if (newValue != null)
      {
        unit = unit.getNextLexicalUnit();
      }
    }
    CSSValue positionValue = null;
    if (unit != null)
    {
      positionValue = positionReadHandler.createValue(null, unit);
    }

    Map map = new HashMap();
    if (nameValue != null)
    {
      map.put (HyperlinkStyleKeys.TARGET_NAME, nameValue);
    }
    if (newValue != null)
    {
      map.put (HyperlinkStyleKeys.TARGET_NEW, newValue);
    }
    if (positionValue != null)
    {
      map.put (HyperlinkStyleKeys.TARGET_POSITION, positionValue);
    }
    return map;
  }
}
