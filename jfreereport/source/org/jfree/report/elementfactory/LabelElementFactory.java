/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * $Id: LabelElementFactory.java,v 1.5 2003/10/05 21:52:32 taqua Exp $
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
import org.jfree.report.filter.templates.LabelTemplate;
import org.jfree.report.style.FontDefinition;
import org.jfree.ui.FloatDimension;

/**
 * A factory to define LabelElements. LabelElements are considered immutable and
 * should not be modified once they are created. The label expects plain text.
 *
 * @author Thomas Morgner
 */
public class LabelElementFactory extends TextElementFactory
{
  /** The label text. */
  private String text;

  /**
   * DefaultConstructor.
   *
   */
  public LabelElementFactory()
  {
  }

  /**
   * Returns the label text.
   *
   * @return the text of the label.
   */
  public String getText()
  {
    return text;
  }

  /**
   * Defines the text of the label.
   *
   * @param text the plain text of the label.
   */
  public void setText(final String text)
  {
    this.text = text;
  }

  /**
   * Creates the label element.
   *
   * @see org.jfree.report.elementfactory.ElementFactory#createElement()
   *
   * @return the generated label.
   * @throws IllegalStateException if the text is not defined.
   */
  public Element createElement()
  {
    if (getText() == null)
    {
      throw new IllegalStateException("Content is not set.");
    }

    final LabelTemplate template = new LabelTemplate();
    template.setContent(getText());
    final TextElement element = new TextElement();
    element.setDataSource(template);
    applyElementName(element);
    applyStyle(element.getStyle());
    return element;
  }

  /**
   * Creates a new TextElement containing a label.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment  the horizontal text alignment.
   * @param font the font for this element
   * @param labeltext the text to display
   *
   * @return a report element for displaying a label (static text).
   *
   * @throws NullPointerException if bounds, name, format or field are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createLabelElement(final String name,
                                               final Rectangle2D bounds,
                                               final Color paint,
                                               final ElementAlignment alignment,
                                               final FontDefinition font,
                                               final String labeltext)
  {
    return createLabelElement(name, bounds, paint, alignment,
        ElementAlignment.TOP,
        font, labeltext);
  }

  /**
   * Creates a new {@link org.jfree.report.TextElement} containing a label.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds of the new element.
   * @param paint  the text color of this text element.
   * @param alignment  the horizontal alignment.
   * @param valign  the vertical alignment.
   * @param font  the font for this element.
   * @param labeltext  the text to display.
   *
   * @return a report element for displaying a label (static text).
   *
   * @throws NullPointerException if bounds, name, format or field are <code>null</code>.
   * @throws IllegalArgumentException if the given alignment is invalid.
   */
  public static TextElement createLabelElement(final String name,
                                               final Rectangle2D bounds,
                                               final Color paint,
                                               final ElementAlignment alignment,
                                               final ElementAlignment valign,
                                               final FontDefinition font,
                                               final String labeltext)
  {
    final LabelElementFactory factory = new LabelElementFactory();
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
    factory.setText(labeltext);
    return (TextElement) factory.createElement();
  }


}
