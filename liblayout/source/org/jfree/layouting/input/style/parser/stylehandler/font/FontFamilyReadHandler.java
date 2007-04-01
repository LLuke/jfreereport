/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/liblayout/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.font;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.font.FontFamilyValues;
import org.jfree.layouting.input.style.parser.stylehandler.ListOfValuesReadHandler;
import org.jfree.layouting.input.style.values.CSSStringType;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 28.11.2005, 16:18:46
 *
 * @author Thomas Morgner
 */
public class FontFamilyReadHandler extends ListOfValuesReadHandler
{
  public FontFamilyReadHandler()
  {
  }

  public CSSValue createValue(StyleKey name, LexicalUnit value)
  {
    if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      if (value.getStringValue().equalsIgnoreCase("none"))
      {
        return FontFamilyValues.NONE;
      }
    }
    return super.createValue(name, value);
  }

  protected CSSValue parseValue(final LexicalUnit value)
  {
    if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      if (value.getStringValue().equalsIgnoreCase("serif"))
      {
        return FontFamilyValues.SERIF;
      }
      if (value.getStringValue().equalsIgnoreCase("sans-serif"))
      {
        return FontFamilyValues.SANS_SERIF;
      }
      if (value.getStringValue().equalsIgnoreCase("fantasy"))
      {
        return FontFamilyValues.FANTASY;
      }
      if (value.getStringValue().equalsIgnoreCase("fantasy"))
      {
        return FontFamilyValues.CURSIVE;
      }
      if (value.getStringValue().equalsIgnoreCase("monospace"))
      {
        return FontFamilyValues.MONOSPACE;
      }
      return null;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
    {
      return new CSSStringValue(CSSStringType.STRING, value.getStringValue());
    }
    return null;
  }
}
