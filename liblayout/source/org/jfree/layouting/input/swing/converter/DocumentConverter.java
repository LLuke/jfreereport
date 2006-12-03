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

import org.jfree.layouting.input.style.keys.page.PageSize;
import org.jfree.layouting.input.style.keys.page.PageStyleKeys;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.swing.Converter;
import org.jfree.layouting.input.swing.ConverterAttributeSet;
import org.jfree.util.Log;

import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;

/**
 * This class handles convertions of document properties to css style attributes.
 */
public class DocumentConverter implements Converter
{
  public static final String PAGE_RULE_TYPE = "@page";

  public static final String RTF_PAGEWIDTH = "paperw";
  public static final String RTF_PAGEHEIGHT = "paperh";
  public static final String RTF_MARGINLEFT = "margl";
  public static final String RTF_MARGINRIGHT = "margr";
  public static final String RTF_MARGINTOP = "margt";
  public static final String RTF_MARGINBOTTOM = "margb";
  public static final String RTF_GUTTERWIDTH = "gutter";
  public static final String RTF_LANDSCAPE = "landscape";

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
  public ConverterAttributeSet convertToCSS(Object key, Object value, ConverterAttributeSet cssAttr, Element context)
  {
    if (key instanceof String)
    {
      return handleStringAttributes(key, value, cssAttr);
    }

    return null;
  }

  /**
   * Handles the convertions of <code>String</code> key type.
   *
   * @param key The style key.
   * @param value The style value.
   * @param cssAttr The current converted CSS attributes.
   * @return The conversion result or null if no converstion has been done.
   */
  private ConverterAttributeSet handleStringAttributes(Object key, Object value, ConverterAttributeSet cssAttr)
  {
    final ConverterAttributeSet attr = new ConverterAttributeSet();

    final String styleKey = (String) key;

    if (styleKey.equals(RTF_PAGEWIDTH) || styleKey.equals(RTF_PAGEHEIGHT))
    {
      final float floatValue = ((Float) value).floatValue();
      final PageSize pageSize = (PageSize) cssAttr.getAttribute(PageStyleKeys.SIZE.getName());
      int width = 0;
      int height = 0;

      if (pageSize != null)
      {
        width = (int) pageSize.getWidth();
        height = (int) pageSize.getHeight();
      }

      if (styleKey.equals(RTF_PAGEWIDTH))
      {
        attr.addAttribute(PAGE_RULE_TYPE, PageStyleKeys.SIZE.getName(), new PageSize(twipToInt(floatValue), height));
      }
      else
      {
        attr.addAttribute(PAGE_RULE_TYPE, PageStyleKeys.SIZE.getName(), new PageSize(width, twipToInt(floatValue)));
      }
    }
    else if(styleKey.equals(RTF_MARGINLEFT))
    {
      final float floatValue = ((Float) value).floatValue();
      attr.addAttribute(PAGE_RULE_TYPE, BoxStyleKeys.MARGIN_LEFT, new CSSNumericValue(CSSNumericType.PT, floatValue));
    }
    else if(styleKey.equals(RTF_MARGINRIGHT))
    {
      final float floatValue = ((Float) value).floatValue();
      attr.addAttribute(PAGE_RULE_TYPE, BoxStyleKeys.MARGIN_RIGHT, new CSSNumericValue(CSSNumericType.PT, floatValue));
    }
    else if(styleKey.equals(RTF_MARGINTOP))
    {
      final float floatValue = ((Float) value).floatValue();
      attr.addAttribute(PAGE_RULE_TYPE, BoxStyleKeys.MARGIN_TOP, new CSSNumericValue(CSSNumericType.PT, floatValue));
    }
    else if(styleKey.equals(RTF_MARGINBOTTOM))
    {
      final float floatValue = ((Float) value).floatValue();
      attr.addAttribute(PAGE_RULE_TYPE, BoxStyleKeys.MARGIN_BOTTOM, new CSSNumericValue(CSSNumericType.PT, floatValue));
    }
    //else if(styleKey.equals(RTF_LANDSCAPE))
    //{
    //todo merge pagesize()
      //attr.addAttribute(PAGE_RULE_TYPE, null, new CSSNumericValue(CSSNumericType.PT, floatValue));
    //}
    else
    {
      Log.debug(new Log.SimpleMessage("Unkown type of document attribute", styleKey));
      return null;
    }

    return attr;
  }


  /**
   * Measurements in RTF are in twips. A twip is 1/20 point.
   *
   * @param twips The measurement in twips.
   * @return The measurement in points.
   */
  private int twipToInt(float twips)
  {
    return (int) (twips * 20);
  }
}
