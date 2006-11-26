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
 * FontSizeReadHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: FontSizeReadHandler.java,v 1.2 2006/04/17 20:51:08 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.input.style.parser.stylehandler.font;

import org.jfree.layouting.input.style.keys.font.FontSizeConstant;
import org.jfree.layouting.input.style.keys.font.RelativeFontSize;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.stylehandler.OneOfConstantsReadHandler;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 28.11.2005, 16:51:33
 *
 * @author Thomas Morgner
 */
public class FontSizeReadHandler extends OneOfConstantsReadHandler
{
  public FontSizeReadHandler()
  {
    this(false);
  }

  protected FontSizeReadHandler(final boolean autoAllowed)
  {
    super(autoAllowed);
    addValue(FontSizeConstant.LARGE);
    addValue(FontSizeConstant.MEDIUM);
    addValue(FontSizeConstant.SMALL);
    addValue(FontSizeConstant.X_LARGE);
    addValue(FontSizeConstant.XX_LARGE);
    addValue(FontSizeConstant.X_SMALL);
    addValue(FontSizeConstant.XX_SMALL);
    addValue(RelativeFontSize.LARGER);
    addValue(RelativeFontSize.SMALLER);
  }

  protected CSSValue lookupValue(final LexicalUnit value)
  {
    if (value.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE)
    {
      return new CSSNumericValue(CSSNumericType.PERCENTAGE, value.getFloatValue());
    }
    CSSValue constant = super.lookupValue(value);
    if (constant != null)
    {
      return constant;
    }
    return CSSValueFactory.createLengthValue(value);
  }
}
