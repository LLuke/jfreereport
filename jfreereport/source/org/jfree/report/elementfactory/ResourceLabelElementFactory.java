/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 06.07.2003 : Initial version
 *  
 */

package org.jfree.report.elementfactory;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Element;
import org.jfree.report.ElementAlignment;
import org.jfree.report.TextElement;
import org.jfree.report.filter.templates.ResourceLabelTemplate;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.FontDefinition;
import org.jfree.ui.FloatDimension;

public class ResourceLabelElementFactory extends TextElementFactory
{
  private String resourceBase;
  private String nullString;
  private String resourceKey;

  public ResourceLabelElementFactory()
  {
  }

  public String getResourceBase()
  {
    return resourceBase;
  }

  public void setResourceBase(String resourceBase)
  {
    this.resourceBase = resourceBase;
  }

  public String getNullString()
  {
    return nullString;
  }

  public void setNullString(String nullString)
  {
    this.nullString = nullString;
  }

  public String getResourceKey()
  {
    return resourceKey;
  }

  public void setResourceKey(String resourceKey)
  {
    this.resourceKey = resourceKey;
  }

  public Element createElement()
  {
    if (getResourceKey() == null)
    {
      throw new IllegalStateException("ResourceKey is not set.");
    }

    ResourceLabelTemplate template = new ResourceLabelTemplate();
    template.setResourceClassName(getResourceBase());
    template.setContent(getResourceKey());
    template.setNullValue(getNullString());

    TextElement element = new TextElement();
    element.setDataSource(template);
    if (getName() != null)
    {
      element.setName(getName());
    }
    final ElementStyleSheet style = element.getStyle();
    applyStyle(style);
    return element;
  }


  /**
   * Creates a ResourceElement. ResourceElements resolve their value using a
   * <code>java.util.ResourceBundle</code>.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds of the new element.
   * @param paint  the text color of this text element.
   * @param alignment  the horizontal alignment.
   * @param valign  the vertical alignment.
   * @param font  the font for this element.
   * @param resourceKey the key which is used to query the resource bundle
   * @param resourceBase the classname/basename of the assigned resource bundle
   * @return the created ResourceElement
   */
  public static TextElement createResourceLabel(final String name,
                                                final Rectangle2D bounds,
                                                final Color paint,
                                                final ElementAlignment alignment,
                                                final ElementAlignment valign,
                                                final FontDefinition font,
                                                final String nullValue,
                                                final String resourceBase,
                                                final String resourceKey)
  {
    ResourceLabelElementFactory factory = new ResourceLabelElementFactory();
    factory.setAbsolutePosition(new Point2D.Double(bounds.getX(), bounds.getY()));
    factory.setMinimumSize(new FloatDimension
        ((float) bounds.getWidth(), (float) bounds.getHeight()));
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
    factory.setResourceKey(resourceKey);
    return (TextElement) factory.createElement();
  }


}
