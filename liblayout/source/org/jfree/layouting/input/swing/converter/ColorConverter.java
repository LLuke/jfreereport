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

import java.awt.Color;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.jfree.layouting.input.style.keys.border.BorderStyleKeys;
import org.jfree.layouting.input.style.keys.color.ColorStyleKeys;
import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.input.swing.Converter;
import org.jfree.util.Log;

/**
 *
 */
public class ColorConverter implements Converter {
  public AttributeSet convertToCSS (Object key, Object value, AttributeSet cssAttr,
                                    Element context)
  {
    SimpleAttributeSet attr = new SimpleAttributeSet();
    if(key instanceof StyleConstants.ColorConstants)
    {
      final StyleConstants.ColorConstants colorConstant = (StyleConstants.ColorConstants)key;

      return handleColorConstants(colorConstant, value, attr);
    }

    return null;
  }

  private AttributeSet handleColorConstants (StyleConstants.ColorConstants colorConstant,
                                     Object value, SimpleAttributeSet attr)
  {
    if(colorConstant == StyleConstants.Foreground)
    {
      final CSSColorValue cssColorValue = new CSSColorValue((Color) value);
      attr.addAttribute(ColorStyleKeys.COLOR.getName(), cssColorValue);
    }
    else if(colorConstant == StyleConstants.Background)
    {
      final CSSColorValue cssColorValue = new CSSColorValue((Color) value);
      attr.addAttribute(BorderStyleKeys.BACKGROUND_COLOR.getName(), cssColorValue);
    }
    else
    {
      Log.debug(new Log.SimpleMessage("Unkown type of color attribute", colorConstant));
      return null;
    }

    return attr;
  }
}
