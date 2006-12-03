/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.layouting.input.swing.converter;

import java.util.StringTokenizer;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
import org.jfree.layouting.input.style.keys.line.VerticalAlign;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.keys.text.UnicodeBidi;
import org.jfree.layouting.input.swing.Converter;
import org.jfree.layouting.input.swing.ConverterAttributeSet;
import org.jfree.util.Log;

/**
 * This class handles convertions of character style attributes to css style attributes.
 */
public class CharacterConverter implements Converter {
  public static final String TEXT_DECORATION_KEY = "text-decoration";
  //none | [ underline || overline || line-through || blink]
  public static final String NONE_TEXT_DECORATION = "none";
  public static final String UNDERLINE_TEXT_DECORATION = "underline";
  public static final String LINETHROUGH_TEXT_DECORATION = "line-through";

  private Object mergeTextDecorationValues(Object current, Object newone) {
    boolean none = false;
    boolean underline = false;
    boolean strikethrough = false;

    if(newone == null)
    {
      return current;
    }

    String str = (String) current;
    str = str+" "+newone;
    final StringTokenizer tokenizer = new StringTokenizer(str);
    while(tokenizer.hasMoreTokens())
    {
      final String s = tokenizer.nextToken().trim();
      if(NONE_TEXT_DECORATION.equals(s))
      {
        none = true;
      }
      else if(UNDERLINE_TEXT_DECORATION.equals(s))
      {
        underline = true;
      }
      else if(LINETHROUGH_TEXT_DECORATION.equals(s))
      {
        strikethrough = true;
      }
    }

    if(underline && strikethrough)
    {
      return UNDERLINE_TEXT_DECORATION +" "+ LINETHROUGH_TEXT_DECORATION;
    }
    if(underline)
    {
      return UNDERLINE_TEXT_DECORATION;
    }
    if(strikethrough)
    {
      return LINETHROUGH_TEXT_DECORATION;
    }

    return NONE_TEXT_DECORATION;
  }

  /**
   * Converts a style key and a style value to a CSS compatible style key and style value.
   * A conversion can result in more than one key and value.
   *
   * @param key The style key to convert.
   * @param value The style value to convert.
   * @param cssAttr The current converted CSS attributes for the current element.
   * @param context The current Element.
   * @return The conversion result or null if no converstion has been done.
   */
  public ConverterAttributeSet convertToCSS (Object key, Object value, ConverterAttributeSet cssAttr, Element context)
  {
    if(key instanceof StyleConstants.CharacterConstants)
    {
      final StyleConstants.CharacterConstants characterConstant = (StyleConstants.CharacterConstants) key;

      return handleCharacterConstants(characterConstant, value, cssAttr);
    }

    return null;
  }

  /**
   * Handles the convertions of <code>StyleConstants.CharacterConstants</code> key type.
   *
   * @param characterConstant The style key.
   * @param value The style value.
   * @param cssAttr The current converted CSS attributes.
   * @return The conversion result or null if no converstion has been done.
   */
  private ConverterAttributeSet handleCharacterConstants (
          StyleConstants.CharacterConstants characterConstant, Object value,
          ConverterAttributeSet cssAttr)
  {
    final ConverterAttributeSet attr = new ConverterAttributeSet();

    if(characterConstant == StyleConstants.Underline)
    {
      Boolean b = (Boolean)value;
      if(Boolean.TRUE.equals(b))
      {
        final Object current = cssAttr.getAttribute(TEXT_DECORATION_KEY);
        attr.addAttribute(TEXT_DECORATION_KEY, mergeTextDecorationValues(current, UNDERLINE_TEXT_DECORATION));
      }
      else
      {
        attr.addAttribute(TEXT_DECORATION_KEY, NONE_TEXT_DECORATION);
      }
    }
    else if(characterConstant == StyleConstants.StrikeThrough)
    {
      Boolean b = (Boolean)value;
      if(Boolean.TRUE.equals(b))
      {
        final Object current = cssAttr.getAttribute(TEXT_DECORATION_KEY);
        attr.addAttribute(TEXT_DECORATION_KEY, mergeTextDecorationValues(current, LINETHROUGH_TEXT_DECORATION));
      }
      else
      {
        attr.addAttribute(TEXT_DECORATION_KEY, NONE_TEXT_DECORATION);
      }
    }
    else if(characterConstant == StyleConstants.Superscript)
    {
      Boolean b = (Boolean)value;
      if(Boolean.TRUE.equals(b))
      {
        attr.addAttribute(LineStyleKeys.VERTICAL_ALIGN.getName(), VerticalAlign.SUPER);
      }
    }
    else if(characterConstant == StyleConstants.Subscript)
    {
      Boolean b = (Boolean)value;
      if(Boolean.TRUE.equals(b))
      {
        attr.addAttribute(LineStyleKeys.VERTICAL_ALIGN.getName(), VerticalAlign.SUB);
      }
    }
    else if(characterConstant == StyleConstants.BidiLevel)
    {
      Boolean b = (Boolean)value;
      if(Boolean.TRUE.equals(b))
      {
        attr.addAttribute(TextStyleKeys.UNICODE_BIDI.getName(), UnicodeBidi.EMBED);
      }
      else
      {
        attr.addAttribute(TextStyleKeys.UNICODE_BIDI.getName(), UnicodeBidi.NORMAL);
      }
    }
    else
    {
      Log.debug(new Log.SimpleMessage("Unkown type of character attribute", characterConstant));
      return null;
    }

    return cssAttr;
  }
}
