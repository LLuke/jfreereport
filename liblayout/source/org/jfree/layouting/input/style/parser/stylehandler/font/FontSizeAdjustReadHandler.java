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
 * FontSizeAdjustReadHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: FontSizeAdjustReadHandler.java,v 1.1 2006/02/12 21:57:20 taqua Exp $
 *
 * Changes
 * -------------------------
 * 28.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser.stylehandler.font;

import org.jfree.layouting.input.style.parser.CSSValueReadHandler;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.StyleKey;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 28.11.2005, 17:02:52
 *
 * @author Thomas Morgner
 */
public class FontSizeAdjustReadHandler implements CSSValueReadHandler
{
  public FontSizeAdjustReadHandler()
  {
  }

  public CSSValue createValue(StyleKey name, LexicalUnit value)
  {
    if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      if (value.getStringValue().equalsIgnoreCase("none"))
      {
        return new CSSConstant ("none");
      }
    }
    return CSSValueFactory.createNumericValue(value);
  }
}
