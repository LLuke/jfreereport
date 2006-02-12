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
 * ColumnRuleColorReadHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: ColumnRuleColorReadHandler.java,v 1.1 2006/02/12 21:57:20 taqua Exp $
 *
 * Changes
 * -------------------------
 * 04.12.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser.stylehandler.column;

import org.jfree.layouting.input.style.parser.stylehandler.color.ColorReadHandler;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSAutoValue;
import org.jfree.layouting.input.style.StyleKey;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 04.12.2005, 13:24:46
 *
 * @author Thomas Morgner
 */
public class ColumnRuleColorReadHandler extends ColorReadHandler
{
  public ColumnRuleColorReadHandler()
  {
  }

  public CSSValue createValue(StyleKey name, LexicalUnit value)
  {
    if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      if (value.getStringValue().equalsIgnoreCase("auto"))
      {
        return CSSAutoValue.getInstance();
      }
    }
    return super.createValue(name, value);
  }
}
