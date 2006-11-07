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
 * Converter.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   Cedric Pronzato;
 *
 * $Id: Converter.java,v 1.2 2006/11/07 19:53:54 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.input.swing.converter;

import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.jfree.layouting.input.style.keys.font.FontStyle;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.input.style.keys.font.FontWeight;
import org.jfree.layouting.input.swing.Converter;
import org.jfree.util.Log;

/**
 *
 */
public class FontConverter implements Converter {

  public AttributeSet convertToCSS (Object key, Object value, AttributeSet cssAttr, Element context)
  {
    final SimpleAttributeSet attr = new SimpleAttributeSet();

    if(key instanceof StyleConstants.FontConstants)
    {
      final StyleConstants.FontConstants fontConstant = (StyleConstants.FontConstants)key;
      return handleFontConstants(fontConstant, attr, value);
    }

    return null;
  }

  protected AttributeSet handleFontConstants (StyleConstants.FontConstants fontConstant,
                                              SimpleAttributeSet attr, Object value)
  {
    if(fontConstant == StyleConstants.FontFamily)
    {
      // just attribute name conversion
      attr.addAttribute(FontStyleKeys.FONT_FAMILY.getName(), value);

    }
    else if(fontConstant == StyleConstants.FontSize)
    {
      // just attribute name conversion
      attr.addAttribute(FontStyleKeys.FONT_SIZE.getName(), value);
    }
    else if (fontConstant == StyleConstants.Bold)
    {
      Boolean b = (Boolean)value;
      if(Boolean.TRUE.equals(b))
      {
        attr.addAttribute(FontStyleKeys.FONT_WEIGHT.getName(), FontWeight.BOLD);
      }
      else
      {
        attr.addAttribute(FontStyleKeys.FONT_WEIGHT.getName(), FontWeight.NORMAL);
      }
    }
    else if (fontConstant == StyleConstants.Italic)
    {
      Boolean b = (Boolean)value;
      if(Boolean.TRUE.equals(b))
      {
        attr.addAttribute(FontStyleKeys.FONT_STYLE.getName(), FontStyle.ITALIC);
      }
      else
      {
        attr.addAttribute(FontStyleKeys.FONT_STYLE.getName(), FontStyle.NORMAL);
      }
    }
    else
    {
      Log.debug(new Log.SimpleMessage("Unkown type of font attribute", fontConstant));
      return null;
    }

    return attr;
  }

}
