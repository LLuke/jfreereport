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
 * SizeReadHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: SizeReadHandler.java,v 1.1 2006/02/12 21:57:21 taqua Exp $
 *
 * Changes
 * -------------------------
 * 30.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser.stylehandler.page;

import java.awt.print.PageFormat;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.page.PageSize;
import org.jfree.layouting.input.style.keys.page.PageSizeFactory;
import org.jfree.layouting.input.style.keys.page.PageSizeValue;
import org.jfree.layouting.input.style.keys.page.NamedPageSizeValue;
import org.jfree.layouting.input.style.parser.CSSValueReadHandler;
import org.jfree.layouting.input.style.parser.stylehandler.AbstractWidthReadHandler;
import org.jfree.layouting.input.style.values.CSSAutoValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 30.11.2005, 18:04:27
 *
 * @author Thomas Morgner
 */
public class SizeReadHandler extends AbstractWidthReadHandler
        implements CSSValueReadHandler
{
  public SizeReadHandler()
  {
    super(true, false);
  }

  public CSSValue createValue(StyleKey name, LexicalUnit value)
  {
    if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      String ident = value.getStringValue();
      if (ident.equalsIgnoreCase("auto"))
      {
        return CSSAutoValue.getInstance();
      }
      final PageSize ps = PageSizeFactory.getInstance().getPageSizeByName(ident);
      if (ps == null)
      {
        return null;
      }

      int pageOrientation = PageFormat.PORTRAIT;
      value = value.getNextLexicalUnit();
      if (value != null)
      {
        if (value.getLexicalUnitType() != LexicalUnit.SAC_IDENT)
        {
          return null;
        }

        if (value.getStringValue().equalsIgnoreCase("landscape"))
        {
          pageOrientation = PageFormat.LANDSCAPE;
        }
        else
        if (value.getStringValue().equalsIgnoreCase("reverse-landscape"))
        {
          pageOrientation = PageFormat.REVERSE_LANDSCAPE;
        }
        else if (value.getStringValue().equalsIgnoreCase("portrait"))
        {
          pageOrientation = PageFormat.PORTRAIT;
        }
        else
        {
          return null;
        }
      }

      return new NamedPageSizeValue
              (new CSSConstant(ident.toLowerCase()), pageOrientation);
    }
    else
    {
      final CSSNumericValue topWidth = (CSSNumericValue) parseWidth(value);
      if (topWidth == null)
      {
        return null;
      }

      value = value.getNextLexicalUnit();

      final CSSNumericValue rightWidth;
      if (value == null)
      {
        rightWidth = topWidth;
      }
      else
      {
        rightWidth = (CSSNumericValue) parseWidth(value);
        if (rightWidth == null)
        {
          return null;
        }
      }

      return new PageSizeValue (topWidth, rightWidth);
    }
  }

}