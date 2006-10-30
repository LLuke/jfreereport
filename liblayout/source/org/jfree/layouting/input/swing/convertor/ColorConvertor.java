package org.jfree.layouting.input.swing.convertor;

import java.awt.Color;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.jfree.layouting.input.style.keys.border.BorderStyleKeys;
import org.jfree.layouting.input.style.keys.color.ColorStyleKeys;
import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.input.swing.Convertor;
import org.jfree.util.Log;

/**
 *
 */
public class ColorConvertor implements Convertor
{
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
