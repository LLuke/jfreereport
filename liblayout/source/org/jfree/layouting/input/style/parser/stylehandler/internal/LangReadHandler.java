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
 * LangReadHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: LangReadHandler.java,v 1.1 2006/02/12 21:57:21 taqua Exp $
 *
 * Changes
 * -------------------------
 * 12.02.2006 : Initial version
 */
package org.jfree.layouting.input.style.parser.stylehandler.internal;

import org.jfree.layouting.input.style.parser.CSSValueReadHandler;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSStringType;
import org.jfree.layouting.input.style.StyleKey;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 12.02.2006, 20:37:12
 *
 * @author Thomas Morgner
 */
public class LangReadHandler implements CSSValueReadHandler
{
  public LangReadHandler()
  {
  }

  public CSSValue createValue(StyleKey name, LexicalUnit value)
  {
    if (value.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE ||
        value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      return new CSSStringValue(CSSStringType.STRING, value.getStringValue());
    }
    return null;
  }
}
