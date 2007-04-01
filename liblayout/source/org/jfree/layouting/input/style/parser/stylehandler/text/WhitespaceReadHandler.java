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
package org.jfree.layouting.input.style.parser.stylehandler.text;

import java.util.HashMap;
import java.util.Map;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.keys.text.TextWrap;
import org.jfree.layouting.input.style.keys.text.WhitespaceCollapse;
import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.values.CSSInheritValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 01.12.2005, 22:18:58
 *
 * @author Thomas Morgner
 */
public class WhitespaceReadHandler implements CSSCompoundValueReadHandler
{
  public WhitespaceReadHandler()
  {
  }

  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public Map createValues(LexicalUnit unit)
  {
    // http://cheeaun.phoenity.com/weblog/2005/06/whitespace-and-generated-content.html
    // is a good overview about the whitespace stuff ..

    CSSValue whitespace;
    CSSValue textWrap;
    if (unit.getLexicalUnitType() == LexicalUnit.SAC_INHERIT)
    {
      whitespace = CSSInheritValue.getInstance();
      textWrap = CSSInheritValue.getInstance();
    }
    else if (unit.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      String strVal = unit.getStringValue();
      if (strVal.equalsIgnoreCase("normal"))
      {
        whitespace = WhitespaceCollapse.COLLAPSE;
        textWrap = TextWrap.NORMAL;
      }
      else if (strVal.equalsIgnoreCase("pre"))
      {
        whitespace = WhitespaceCollapse.PRESERVE;
        textWrap = TextWrap.SUPPRESS;
      }
      else if (strVal.equalsIgnoreCase("pre-line"))
      {
        whitespace = WhitespaceCollapse.PRESERVE_BREAKS;
        textWrap = TextWrap.NORMAL;
      }
      else if (strVal.equalsIgnoreCase("pre-wrap"))
      {
        whitespace = WhitespaceCollapse.PRESERVE;
        textWrap = TextWrap.NORMAL;
      }
      else
      {
        return null;
      }
    }
    else
    {
      return null;
    }

    Map map = new HashMap();
    map.put(TextStyleKeys.WHITE_SPACE_COLLAPSE, whitespace);
    map.put(TextStyleKeys.TEXT_WRAP, textWrap);
    return map;
  }

  public StyleKey[] getAffectedKeys()
  {
    return new StyleKey[] {
            TextStyleKeys.WHITE_SPACE_COLLAPSE,
            TextStyleKeys.TEXT_WRAP
    };
  }
}
