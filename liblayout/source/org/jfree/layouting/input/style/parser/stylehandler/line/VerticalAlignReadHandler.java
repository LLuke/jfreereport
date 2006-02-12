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
 * DominantBaselineReadHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: VerticalAlignReadHandler.java,v 1.1 2006/02/12 21:57:21 taqua Exp $
 *
 * Changes
 * -------------------------
 * 28.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser.stylehandler.line;

import org.jfree.layouting.input.style.keys.line.VerticalAlign;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.stylehandler.OneOfConstantsReadHandler;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Warning: This *is* a compound property, but one if its values depend on
 * the element structure and it changes its meaning if used in Tables. 
 *
 * @author Thomas Morgner
 */
public class VerticalAlignReadHandler extends OneOfConstantsReadHandler
{
  public VerticalAlignReadHandler()
  {
    super(true);
    addValue(VerticalAlign.BASELINE);
    addValue(VerticalAlign.BOTTOM);
    addValue(VerticalAlign.CENTRAL);
    addValue(VerticalAlign.MIDDLE);
    addValue(VerticalAlign.SUB);
    addValue(VerticalAlign.SUPER);
    addValue(VerticalAlign.TEXT_BOTTOM);
    addValue(VerticalAlign.TEXT_TOP);
    addValue(VerticalAlign.USE_SCRIPT);
    addValue(VerticalAlign.TOP);
  }

  protected CSSValue lookupValue(final LexicalUnit value)
  {
    CSSValue constant = super.lookupValue(value);
    if (constant != null)
    {
      return constant;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE)
    {
      return new CSSNumericValue(CSSNumericType.PERCENTAGE,
              value.getFloatValue());
    }

    return CSSValueFactory.createLengthValue(value);

  }
}
