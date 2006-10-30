package org.jfree.layouting.input.swing.convertor;

import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
import org.jfree.layouting.input.style.keys.text.TextAlign;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.swing.Convertor;
import org.jfree.util.Log;

/**
 * 
 */
public class ParagraphConvertor implements Convertor
{
  public AttributeSet convertToCSS (Object key, Object value, AttributeSet cssAttr,
                                    Element context)
  {
    final SimpleAttributeSet attr = new SimpleAttributeSet();
    if(key instanceof StyleConstants.ParagraphConstants)
    {
      final StyleConstants.ParagraphConstants paragraphConstant = (StyleConstants.ParagraphConstants)key;

      return handleParagraphConstants(paragraphConstant, value, attr);

    }

    return null;
  }

  private AttributeSet handleParagraphConstants (
          StyleConstants.ParagraphConstants paragraphConstant, Object value,
          SimpleAttributeSet attr)
  {
    if(paragraphConstant == StyleConstants.FirstLineIndent)
    {
      final CSSNumericValue cssNumericValue = new CSSNumericValue(CSSNumericType.PT, Double.valueOf(value.toString()));
      attr.addAttribute(TextStyleKeys.TEXT_INDENT.getName(), cssNumericValue);
    }
    else if(paragraphConstant == StyleConstants.RightIndent)
    {
      final CSSNumericValue cssNumericValue = new CSSNumericValue(CSSNumericType.PT, Double.valueOf(value.toString()));
      attr.addAttribute(BoxStyleKeys.MARGIN_RIGHT.getName(), cssNumericValue);
    }
    else if(paragraphConstant == StyleConstants.LeftIndent)
    {
      final CSSNumericValue cssNumericValue = new CSSNumericValue(CSSNumericType.PT, Double.valueOf(value.toString()));
      attr.addAttribute(BoxStyleKeys.MARGIN_LEFT.getName(), cssNumericValue);
    }
    else if(paragraphConstant == StyleConstants.LineSpacing)
    {
      final CSSNumericValue cssNumericValue = new CSSNumericValue(CSSNumericType.EM, Double.valueOf(value.toString()));
      attr.addAttribute(LineStyleKeys.LINE_HEIGHT, cssNumericValue);
    }
    else if(paragraphConstant == StyleConstants.SpaceAbove)
    {
      final CSSNumericValue cssNumericValue = new CSSNumericValue(CSSNumericType.PT, Double.valueOf(value.toString()));
      attr.addAttribute(BoxStyleKeys.MARGIN_TOP.getName(), cssNumericValue);
    }
    else if(paragraphConstant == StyleConstants.SpaceBelow)
    {
      final CSSNumericValue cssNumericValue = new CSSNumericValue(CSSNumericType.PT, Double.valueOf(value.toString()));
      attr.addAttribute(BoxStyleKeys.MARGIN_BOTTOM.getName(), cssNumericValue);
    }
    else if(paragraphConstant == StyleConstants.Alignment)
    {
      Object val = null;
      int interger = Integer.parseInt(value.toString());
      if(interger == StyleConstants.ALIGN_CENTER)
      {
        val = TextAlign.CENTER;
      }
      else if(interger == StyleConstants.ALIGN_JUSTIFIED)
      {
        val = TextAlign.JUSTIFY;
      }
      else if(interger == StyleConstants.ALIGN_LEFT)
      {
        val = TextAlign.LEFT;
      }
      else if(interger == StyleConstants.ALIGN_RIGHT)
      {
        val = TextAlign.RIGHT;
      }

      attr.addAttribute(TextStyleKeys.TEXT_ALIGN.getName(), val);
    }
/*else if(paragraphConstant == StyleConstants.Orientation)
      {
        //attr.addAttribute(TextStyleKeys.BLOCK_PROGRESSION.getName(), null);
      }*/
    else
    {
      // StyleConstants.TabSet @see TabSet class
      Log.debug(new Log.SimpleMessage("Unkown type of paragraphe attribute", paragraphConstant));
      return null;
    }

    return attr;
  }
}
