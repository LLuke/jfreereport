package org.jfree.report.modules.parser.simple.readhandlers;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import org.jfree.report.Element;
import org.jfree.report.elementfactory.ElementFactory;
import org.jfree.report.modules.parser.base.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.ui.FloatDimension;
import org.jfree.xml.ParserUtil;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public abstract class AbstractElementReadHandler
        extends AbstractPropertyXmlReadHandler
{

  /**
   * Literal text for an XML attribute.
   */
  public static final String FONT_NAME_ATT = "fontname";

  /**
   * Literal text for an XML attribute.
   */
  public static final String FONT_STYLE_ATT = "fontstyle";

  /**
   * Literal text for an XML attribute.
   */
  public static final String FONT_SIZE_ATT = "fontsize";

  /**
   * Literal text for an XML attribute value.
   */
  public static final String FS_BOLD = "fsbold";

  /**
   * Literal text for an XML attribute value.
   */
  public static final String FS_ITALIC = "fsitalic";

  /**
   * Literal text for an XML attribute value.
   */
  public static final String FS_UNDERLINE = "fsunderline";

  /**
   * Literal text for an XML attribute value.
   */
  public static final String FS_STRIKETHR = "fsstrikethr";

  /**
   * Literal text for an XML attribute value.
   */
  public static final String FS_EMBEDDED = "font-embedded";

  /**
   * Literal text for an XML attribute value.
   */
  public static final String FS_ENCODING = "font-encoding";

  /**
   * Literal text for an XML attribute value.
   */
  public static final String LINEHEIGHT = "line-height";

  /**
   * Literal text for an XML attribute.
   */
  public static final String NAME_ATT = "name";

  /**
   * Literal text for an XML attribute.
   */
  public static final String ALIGNMENT_ATT = "alignment";

  /**
   * Literal text for an XML attribute.
   */
  public static final String VALIGNMENT_ATT = "vertical-alignment";

  /**
   * Literal text for an XML attribute.
   */
  public static final String COLOR_ATT = "color";

  /**
   * Literal text for an XML attribute.
   */
  public static final String FIELDNAME_ATT = "fieldname";

  /**
   * Literal text for an XML attribute.
   */
  public static final String FUNCTIONNAME_ATT = "function";

  /**
   * Literal text for an XML attribute.
   */
  public static final String NULLSTRING_ATT = "nullstring";
  private static final String DYNAMIC_ATT = "dynamic";
  private static final String LAYOUT_CACHABLE_ATT = "layout-cachable";
  private static final String VISIBLE_ATT = "visible";
  private static final String HREF_ATT = "href";

  private Element element;

  public AbstractElementReadHandler ()
  {
  }

  protected abstract ElementFactory getElementFactory ();

  /**
   * Starts parsing.
   *
   * @param atts the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes atts)
          throws SAXException, XmlReaderException
  {
    final ElementFactory factory = getElementFactory();
    factory.setName(atts.getValue(NAME_ATT));
    factory.setAbsolutePosition(getElementPosition(atts));
    factory.setMinimumSize(getElementDimension(atts));

    final String dynamicValue = atts.getValue(DYNAMIC_ATT);
    if (dynamicValue != null)
    {
      factory.setDynamicHeight(new Boolean(ParserUtil.parseBoolean(dynamicValue, false)));
    }

    final String layoutCachableValue = atts.getValue(LAYOUT_CACHABLE_ATT);
    if (layoutCachableValue != null)
    {
      factory.setLayoutCachable(new Boolean(ParserUtil.parseBoolean(layoutCachableValue, true)));
    }

    final String visibleValue = atts.getValue(VISIBLE_ATT);
    if (visibleValue != null)
    {
      factory.setVisible(new Boolean(ParserUtil.parseBoolean(visibleValue, true)));
    }

    final String href = atts.getValue(HREF_ATT);
    if (href != null)
    {
      factory.setHRefTarget(href);
    }
  }

  /**
   * Parses the element position.
   *
   * @param atts the attribute set containing the "x" and "y" attributes.
   * @return the parsed element position, never null.
   *
   * @throws SAXException if parsing the element position failed.
   */
  protected final Point2D getElementPosition (final PropertyAttributes atts)
          throws SAXException
  {
    final float x = ParserUtil.parseRelativeFloat(atts.getValue("x"),
            "Element x not specified");
    final float y = ParserUtil.parseRelativeFloat(atts.getValue("y"),
            "Element y not specified");
    return new Point2D.Float(x, y);
  }

  /**
   * Parses the element dimension.
   *
   * @param atts the attribute set containing the "width" and "height" attributes.
   * @return the parsed element dimensions, never null.
   *
   * @throws SAXException if parsing the element dimensions failed.
   */
  private Dimension2D getElementDimension (final PropertyAttributes atts)
          throws SAXException
  {
    final float w = ParserUtil.parseRelativeFloat(atts.getValue("width"),
            "Element width not specified");
    final float h = ParserUtil.parseRelativeFloat(atts.getValue("height"),
            "Element height not specified");
    return new FloatDimension(w, h);
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected void doneParsing ()
          throws SAXException, XmlReaderException
  {
    element = getElementFactory().createElement();
    super.doneParsing();
  }

  protected void storeComments ()
          throws SAXException
  {
    final CommentHintPath commentHintPath = new CommentHintPath(element);
    defaultStoreComments(commentHintPath);
  }

  /**
   * Returns the object for this element or null, if this element does not create an
   * object.
   *
   * @return the object.
   *
   * @throws org.jfree.xml.parser.XmlReaderException
   *          if there is a parsing error.
   */
  public Object getObject ()
          throws XmlReaderException
  {
    return element;
  }
}
