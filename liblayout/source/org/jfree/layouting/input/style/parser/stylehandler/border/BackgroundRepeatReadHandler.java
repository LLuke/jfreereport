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
 * BackgroundRepeatReadHandler.java
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
package org.jfree.layouting.input.style.parser.stylehandler.border;

import java.util.ArrayList;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.border.BackgroundRepeat;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.CSSValueReadHandler;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.input.style.values.CSSValuePair;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 27.11.2005, 18:36:29
 *
 * @author Thomas Morgner
 */
public class BackgroundRepeatReadHandler implements CSSValueReadHandler
{
  public BackgroundRepeatReadHandler()
  {
  }

  public CSSValue createValue(StyleKey name, LexicalUnit value) 
  {
    ArrayList values = new ArrayList();

    while (value != null)
    {
      if (value.getLexicalUnitType() != LexicalUnit.SAC_IDENT)
      {
        return null;
      }

      final BackgroundRepeat horizontal;
      final BackgroundRepeat vertical;

      final String horizontalString = value.getStringValue();
      if (horizontalString.equalsIgnoreCase("repeat-x"))
      {
        horizontal = BackgroundRepeat.REPEAT;
        vertical = BackgroundRepeat.NOREPEAT;
      }
      else if (value.getStringValue().equalsIgnoreCase("repeat-y"))
      {
        horizontal = BackgroundRepeat.NOREPEAT;
        vertical = BackgroundRepeat.REPEAT;
      }
      else
      {
        horizontal = translateRepeat(horizontalString);
        if (horizontal == null)
        {
          return null;
        }

        value = value.getNextLexicalUnit();
        if (value == null)
        {
          vertical = horizontal;
        }
        else if (value.getLexicalUnitType() != LexicalUnit.SAC_IDENT)
        {
          return null;
        }
        else
        {
          vertical = translateRepeat(value.getStringValue());
          if (vertical == null)
          {
            return null;
          }
        }
      }

      values.add(new CSSValuePair(horizontal, vertical));
      value = CSSValueFactory.parseComma(value);
    }

    return new CSSValueList(values);
  }

  private BackgroundRepeat translateRepeat(final String value)
  {
    if (value.equalsIgnoreCase("repeat"))
    {
      return BackgroundRepeat.REPEAT;
    }
    if (value.equalsIgnoreCase("no-repeat"))
    {
      return BackgroundRepeat.NOREPEAT;
    }
    if (value.equalsIgnoreCase("space"))
    {
      return BackgroundRepeat.SPACE;
    }
    return null;
  }
}
