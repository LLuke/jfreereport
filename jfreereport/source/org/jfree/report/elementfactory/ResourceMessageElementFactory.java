/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ------------------------------
 * LabelElementFactory.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ResourceLabelElementFactory.java,v 1.10 2005/02/23 21:04:44 taqua Exp $
 *
 * Changes
 * -------------------------
 * 06-Jul-2003 : Initial version
 *
 */

package org.jfree.report.elementfactory;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Element;
import org.jfree.report.ElementAlignment;
import org.jfree.report.TextElement;
import org.jfree.report.filter.templates.ResourceMessageTemplate;
import org.jfree.report.style.FontDefinition;
import org.jfree.ui.FloatDimension;

/**
 * A factory to define translateable LabelElements. LabelElements are considered immutable
 * and should not be modified once they are created. The label expects plain text. The
 * content of the label will be translated using an assigned resource bundle.
 *
 * @author Thomas Morgner
 */
public class ResourceMessageElementFactory extends TextElementFactory
{
  /**
   * The resource base from which to read the translations.
   */
  private String resourceBase;

  /**
   * The nullstring of the text element if the translation was not found.
   */
  private String nullString;
  /**
   * The resource key which is used to retrieve the translation.
   */
  private String formatKey;

  /**
   * DefaultConstructor.
   */
  public ResourceMessageElementFactory ()
  {
  }

  /**
   * Returns the base name of the resource bundle used to translate the content later.
   *
   * @return the resource bundle name of the element.
   */
  public String getResourceBase ()
  {
    return resourceBase;
  }

  /**
   * Defines the base name of the resource bundle used to translate the content later.
   *
   * @param resourceBase the resource bundle name of the element.
   */
  public void setResourceBase (final String resourceBase)
  {
    this.resourceBase = resourceBase;
  }

  /**
   * Returns the null string for the text element. The null string is used when no content
   * is found for that element.
   *
   * @return the null string.
   */
  public String getNullString ()
  {
    return nullString;
  }

  /**
   * Defines the null string for the text element. The null string is used when no content
   * is found for that element. The nullstring itself can be null.
   *
   * @param nullString the null string.
   */
  public void setNullString (final String nullString)
  {
    this.nullString = nullString;
  }

  /**
   * Returns the resource key that contains the label text.
   *
   * @return the label resource bundle key.
   */
  public String getFormatKey ()
  {
    return formatKey;
  }

  /**
   * Defines the resource key, which will be used to read the translated label text.
   *
   * @param formatKey the resource bundle key.
   */
  public void setFormatKey (final String formatKey)
  {
    this.formatKey = formatKey;
  }

  /**
   * Generates the element based on the defined properties.
   *
   * @return the generated element.
   *
   * @throws NullPointerException  if the resource class name is null.
   * @throws IllegalStateException if the resource key is not defined.
   * @see ElementFactory#createElement()
   */
  public Element createElement ()
  {
    if (getFormatKey() == null)
    {
      throw new IllegalStateException("ResourceKey is not set.");
    }

    final ResourceMessageTemplate template = new ResourceMessageTemplate();
    template.setResourceIdentifier(getResourceBase());
    template.setFormatKey(getFormatKey());
    template.setNullValue(getNullString());

    final TextElement element = new TextElement();
    applyElementName(element);
    element.setDataSource(template);
    applyStyle(element.getStyle());
    return element;
  }


  /**
   * Creates a ResourceElement. ResourceElements resolve their value using a
   * <code>java.util.ResourceBundle</code>.
   *
   * @param name         the name of the new element.
   * @param bounds       the bounds of the new element.
   * @param paint        the text color of this text element.
   * @param alignment    the horizontal alignment.
   * @param valign       the vertical alignment.
   * @param font         the font for this element.
   * @param resourceKey  the key which is used to query the resource bundle
   * @param resourceBase the classname/basename of the assigned resource bundle
   * @param nullValue    the null string of the text element (can be null).
   * @return the created ResourceElement
   */
  public static TextElement createResourceLabel (final String name,
                                                 final Rectangle2D bounds,
                                                 final Color paint,
                                                 final ElementAlignment alignment,
                                                 final ElementAlignment valign,
                                                 final FontDefinition font,
                                                 final String nullValue,
                                                 final String resourceBase,
                                                 final String resourceKey)
  {
    final ResourceMessageElementFactory factory = new ResourceMessageElementFactory();
    factory.setAbsolutePosition(new Point2D.Double(bounds.getX(), bounds.getY()));
    factory.setMinimumSize(new FloatDimension((float) bounds.getWidth(), (float) bounds.getHeight()));
    factory.setName(name);
    factory.setColor(paint);
    factory.setHorizontalAlignment(alignment);
    factory.setVerticalAlignment(valign);

    if (font != null)
    {
      factory.setFontName(font.getFontName());
      factory.setFontSize(new Integer(font.getFontSize()));
      factory.setBold(new Boolean(font.isBold()));
      factory.setItalic(new Boolean(font.isItalic()));
      factory.setEncoding(font.getFontEncoding(null));
      factory.setUnderline(new Boolean(font.isUnderline()));
      factory.setStrikethrough(new Boolean(font.isStrikeThrough()));
      factory.setEmbedFont(new Boolean(font.isEmbeddedFont()));
    }
    factory.setNullString(nullValue);
    factory.setResourceBase(resourceBase);
    factory.setFormatKey(resourceKey);
    return (TextElement) factory.createElement();
  }


}
