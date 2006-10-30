package org.jfree.layouting.input.swing.convertor;

import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.jfree.layouting.input.style.keys.font.FontStyle;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.input.style.keys.font.FontWeight;
import org.jfree.layouting.input.swing.Convertor;
import org.jfree.util.Log;

/**
 *
 */
public class FontConvertor implements Convertor
{

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
