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
 * ListOfPairReadHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 26.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser.stylehandler;

import java.util.ArrayList;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.CSSValueReadHandler;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 26.11.2005, 19:23:09
 *
 * @author Thomas Morgner
 */
public abstract class ListOfPairReadHandler  implements CSSValueReadHandler
{
  protected ListOfPairReadHandler()
  {
  }


  public CSSValue createValue(StyleKey name, LexicalUnit value) 
  {
    ArrayList values = new ArrayList();

    while (value != null)
    {
      final CSSValue firstPosition = parseFirstPosition (value);
      if (firstPosition == null)
      {
        return null;
      }

      value = value.getNextLexicalUnit();
      final CSSValue secondPosition = parseSecondPosition(value, firstPosition);
      if (secondPosition == null)
      {
        return null;
      }

      addToResultList(values, firstPosition, secondPosition);
      value = CSSValueFactory.parseComma(value);
    }

    return new CSSValueList(values);
  }

  protected void addToResultList (ArrayList values,
                                  CSSValue firstPosition,
                                  CSSValue secondPosition)
  {
    values.add(firstPosition);
    values.add(secondPosition);
  }

  protected abstract CSSValue parseFirstPosition(final LexicalUnit value);
  protected abstract CSSValue parseSecondPosition(final LexicalUnit value,
                                                  final CSSValue first);

}
