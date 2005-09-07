/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * AbstractElementReadHandler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: AbstractElementReadHandler.java,v 1.8 2005/08/29 17:56:47 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.parser.simple.readhandlers;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import org.jfree.report.Element;
import org.jfree.report.JFreeReport;
import org.jfree.report.elementfactory.ElementFactory;
import org.jfree.report.modules.parser.base.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.style.ElementStyleSheet;
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
  public static final String STYLE_CLASS_ATT = "styleClass";

  private Element element;
  private String styleClass;

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
    styleClass = atts.getValue(STYLE_CLASS_ATT);
    final Point2D elementPosition = getElementPosition(atts);
    if (elementPosition != null)
    {
      factory.setAbsolutePosition(elementPosition);
    }
    factory.setMinimumSize(getElementDimension(atts));

    final String dynamicValue = atts.getValue(DYNAMIC_ATT);
    if (dynamicValue != null)
    {
      final boolean dynamic = ParserUtil.parseBoolean(dynamicValue,false);
      factory.setDynamicHeight(dynamic ? Boolean.TRUE : Boolean.FALSE);
    }

    final String layoutCachableValue = atts.getValue(LAYOUT_CACHABLE_ATT);
    if (layoutCachableValue != null)
    {
      final boolean value = ParserUtil.parseBoolean(layoutCachableValue, true);
      factory.setLayoutCachable((value) ? Boolean.TRUE : Boolean.FALSE);
    }

    final String visibleValue = atts.getValue(VISIBLE_ATT);
    if (visibleValue != null)
    {
      final boolean value = ParserUtil.parseBoolean(visibleValue, true);
      factory.setVisible((value) ? Boolean.TRUE : Boolean.FALSE);
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
    final String xValue = atts.getValue("x");
    final String yValue = atts.getValue("y");
    if (xValue == null && yValue == null)
    {
      return null;
    }
    final float x;
    if (xValue != null)
    {
      x = ParserUtil.parseRelativeFloat(xValue,
              "Element x not specified");
    }
    else
    {
      x = 0;
    }
    final float y;
    if (yValue != null)
    {
      y = ParserUtil.parseRelativeFloat(yValue,
              "Element y not specified");
    }
    else
    {
      y = 0;
    }
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
    if (styleClass != null)
    {
      final JFreeReport report = (JFreeReport) getRootHandler().getHelperObject
              (ReportParser.HELPER_OBJ_REPORT_NAME);
      final ElementStyleSheet styleSheet =
              report.getStyleSheetCollection().getStyleSheet(styleClass);
      element.getStyle().addParent(styleSheet);
    }
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
