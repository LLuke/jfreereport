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
 * ListOfConstantsReadHandler.java
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

import java.util.HashMap;

import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSAutoValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 26.11.2005, 19:16:43
 *
 * @author Thomas Morgner
 */
public abstract class ListOfConstantsReadHandler extends ListOfValuesReadHandler
{
  private HashMap constants;
  private boolean autoAllowed;

  protected ListOfConstantsReadHandler(final boolean auto)
  {
    this(Integer.MAX_VALUE, auto,  false);
  }

  protected ListOfConstantsReadHandler(final int maxCount,
                                       final boolean auto,
                                       final boolean distinct)
  {
    super(maxCount, distinct);
    constants = new HashMap();
    this.autoAllowed = auto;
    if (autoAllowed)
    {
      constants.put("auto", CSSAutoValue.getInstance());
    }
  }

  public void addValue (CSSConstant constant)
  {
    constants.put (constant.getCSSText().toLowerCase(), constant);
  }

  protected CSSValue parseValue(final LexicalUnit value)
  {
    if (value.getLexicalUnitType() != LexicalUnit.SAC_IDENT)
    {
      return null;
    }
    return (CSSValue) constants.get(value.getStringValue().toLowerCase());
  }

  public boolean isAutoAllowed()
  {
    return autoAllowed;
  }
}
