package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.elementfactory.ElementFactory;
import org.jfree.report.elementfactory.TextElementFactory;
import org.jfree.report.ElementAlignment;
import org.jfree.xml.ParserUtil;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public abstract class AbstractTextElementReadHandler extends AbstractElementReadHandler
{
  /** A constant defining the name of the reserved-literal attribute. */
  public static final String RESERVED_LITERAL_ATT = "reserved-literal";
  /** A constant defining the name of the trim-text-content attribute. */
  public static final String TRIM_TEXT_CONTENT_ATT = "trim-text-content";

  public AbstractTextElementReadHandler ()
  {
  }

  protected abstract TextElementFactory getTextElementFactory ();

  protected final ElementFactory getElementFactory ()
  {
    return getTextElementFactory();
  }

  /**
   * Starts parsing.
   *
   * @param atts the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final Attributes atts)
          throws SAXException, XmlReaderException
  {
    super.startParsing(atts);
    final TextElementFactory factory = getTextElementFactory();
    factory.setHorizontalAlignment(parseTextAlignment(atts.getValue(ALIGNMENT_ATT)));
    if (atts.getValue(COLOR_ATT) != null)
    {
      factory.setColor(ParserUtil.parseColor(atts.getValue(COLOR_ATT)));
    }
    factory.setVerticalAlignment(parseTextVerticalAlignment(atts.getValue(VALIGNMENT_ATT)));
    factory.setBold(parseBoolean(atts.getValue(FS_BOLD)));
    factory.setEmbedFont(parseBoolean(atts.getValue(FS_EMBEDDED)));
    factory.setEncoding(atts.getValue(FS_ENCODING));
    factory.setFontName(atts.getValue(FONT_NAME_ATT));
    factory.setFontSize(parseInteger(atts.getValue(FONT_SIZE_ATT)));
    factory.setItalic(parseBoolean(atts.getValue(FS_ITALIC)));
    factory.setLineHeight(parseFloat(atts.getValue(LINEHEIGHT)));
    factory.setStrikethrough(parseBoolean(atts.getValue(FS_STRIKETHR)));
    factory.setUnderline(parseBoolean(atts.getValue(FS_UNDERLINE)));
    factory.setReservedLiteral(atts.getValue(RESERVED_LITERAL_ATT));
    factory.setTrimTextContent(parseBoolean(atts.getValue(TRIM_TEXT_CONTENT_ATT)));
    parseSimpleFontStyle(atts.getValue(FONT_STYLE_ATT), factory);
  }

  /**
   * Parses the text looking for a text alignment, which is one of "left", "center" or "right".
   * <p>
   * The method returns one of the values:  Element.LEFT, Element.CENTER and Element.RIGHT.
   *
   * @param alignment  the alignment.
   * @return an alignment code.
   */
  private ElementAlignment parseTextAlignment(final String alignment)
  {
    ElementAlignment elementAlignment = null;
    if (alignment != null)
    {
      if (alignment.equals("left"))
      {
        elementAlignment = ElementAlignment.LEFT;
      }
      if (alignment.equals("center"))
      {
        elementAlignment = ElementAlignment.CENTER;
      }
      if (alignment.equals("right"))
      {
        elementAlignment = ElementAlignment.RIGHT;
      }
    }
    return elementAlignment;
  }

  /**
   * Parses the text looking for a text alignment, which is one of "top", "middle"/"center"
   * or "bottom".
   * <p>
   * The method returns one of the values:  Element.TOP, Element.BOTTOM and Element.MIDDLE.
   *
   * @param alignment  the alignment.
   * @return an alignment code.
   */
  private ElementAlignment parseTextVerticalAlignment
      (final String alignment)
  {
    ElementAlignment elementAlignment = null;
    if (alignment != null)
    {
      if (alignment.equals("top"))
      {
        elementAlignment = ElementAlignment.TOP;
      }
      if ((alignment.equals("center")) || (alignment.equals("middle")))
      {
        elementAlignment = ElementAlignment.MIDDLE;
      }
      if (alignment.equals("bottom"))
      {
        elementAlignment = ElementAlignment.BOTTOM;
      }
    }
    return elementAlignment;
  }

  /**
   * Reads an attribute as int and returns <code>def</code> if that fails.
   *
   * @param val the attribute value.
   *
   * @return the int value.
   * @throws SAXException if an parse error occured.
   */
  private Integer parseInteger(final String val) throws SAXException
  {
    if (val == null)
    {
      return null;
    }
    try
    {
      return new Integer(val);
    }
    catch (NumberFormatException e)
    {
      throw new SAXException("Failed to parse value", e);
    }
  }

  /**
   * Reads an attribute as float and returns <code>def</code> if that fails.
   *
   * @param value the attribute value.
   * @return the float value.
   * @throws SAXException if an parse error occured.
   */
  private Float parseFloat(final String value) throws SAXException
  {
    if (value == null)
    {
      return null;
    }
    try
    {
      return new Float(value);
    }
    catch (Exception ex)
    {
      throw new SAXException("Failed to parse value", ex);
    }
  }

  /**
   * Parses a simple font style for text elements. These styles contain "bold", "italic"
   * and "bold-italic". The style constants are included for compatibility with older
   * releases and should no longer be used. Use the boolean flags instead.
   *
   * @param fontStyle the font style string.
   * @param target the text element factory that should receive the parsed values.
   */
  private void parseSimpleFontStyle
      (final String fontStyle, final TextElementFactory target)
  {
    if (fontStyle != null)
    {
      if (fontStyle.equals("bold"))
      {
        target.setBold(Boolean.TRUE);
        target.setItalic(Boolean.FALSE);
      }
      else if (fontStyle.equals("italic"))
      {
        target.setBold(Boolean.FALSE);
        target.setItalic(Boolean.TRUE);
      }
      else if (fontStyle.equals("bold-italic"))
      {
        target.setBold(Boolean.TRUE);
        target.setItalic(Boolean.TRUE);
      }
      else if (fontStyle.equals("plain"))
      {
        target.setBold(Boolean.FALSE);
        target.setItalic(Boolean.FALSE);
      }
    }
  }

  /**
   * Translates an boolean string ("true" or "false") into the corresponding
   * Boolean object.
   *
   * @param value the string that represents the boolean.
   * @return Boolean.TRUE or Boolean.FALSE
   * @throws SAXException if an parse error occured.
   */
  private Boolean parseBoolean(final String value) throws SAXException
  {
    if (value == null)
    {
      return null;
    }
    if (value.equals("true"))
    {
      return Boolean.TRUE;
    }
    else if (value.equals("false"))
    {
      return Boolean.FALSE;
    }
    throw new SAXException("Failed to parse value: Expected 'true' or 'false'");
  }


}
