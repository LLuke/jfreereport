/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * TableMetaBandProducer.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: TableMetaBandProducer.java,v 1.1 2004/03/16 15:43:41 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 24.02.2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.base;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Element;
import org.jfree.report.ShapeElement;
import org.jfree.report.Band;
import org.jfree.report.DrawableElement;
import org.jfree.report.ImageElement;
import org.jfree.report.TextElement;
import org.jfree.report.content.Content;
import org.jfree.report.content.ContentCreationException;
import org.jfree.report.content.ContentFactory;
import org.jfree.report.content.ContentType;
import org.jfree.report.content.ShapeContent;
import org.jfree.report.content.EmptyContent;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.report.modules.output.meta.MetaBandProducer;
import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.ElementLayoutInformation;
import org.jfree.report.util.Log;

public abstract class TableMetaBandProducer extends MetaBandProducer
{
  public TableMetaBandProducer(final LayoutSupport support)
  {
    super(support);
  }

  /**
   * Create a band cell for the given element. A band cell is used to create
   * borders for the band bounds, and contains no other data or formats.
   *
   * @param rect  the bounds of the element.
   *
   * @return The band area or <code>null<code>, if the band has a height or width of 0.
   */
  public MetaElement createBandCell(final Rectangle2D rect)
  {
    if (rect.getHeight() == 0 || rect.getWidth() == 0)
    {
      // bands with a height of 0 are ignored ...
      return null;
    }
    final ElementStyleSheet style = new ElementStyleSheet("metaband");
    style.setStyleProperty(ElementStyleSheet.BOUNDS, rect);
    return new TableBandArea(EmptyContent.getDefaultEmptyContent(), style, null);
  }

  /**
   * Handles the creation of background cells. This implementation translates lines
   * and rectangles into border and background color specifications.
   * <p>
   * The shape must be either a horizontal or vertical line or a rectangle. Other
   * shape types are ignored, as they cannot be translated into the table cell space.
   *
   * @param e  the element that defines the background, usually a {@link org.jfree.report.ShapeElement}.
   *
   * @return the generated {@link TableCellBackground} or <code>null</code> if the background
   *         shape is not supported.
   */
  public TableCellBackground createBackground
      (final Element e, final float x, final float y)
  {
    if (e.getContentType().equals(ShapeElement.CONTENT_TYPE) == false)
    {
      return null;
    }

    final ContentFactory cf = getLayoutSupport().getContentFactory();
    if (cf.canHandleContent(e.getContentType()) == false)
    {
      return null;
    }

    final Content content;
    try
    {
      final Rectangle2D bounds = createElementBounds(e.getStyle(), x, y);
      content = cf.createContentForElement
          (e, new ElementLayoutInformation (bounds), getLayoutSupport());
    }
    catch (ContentCreationException ex)
    {
      Log.warn ("Failed to create content for background.", ex);
      return null;
    }

    // this implementation only handles shape content ...
    if (content.getContentType().equals(ContentType.SHAPE))
    {
      return createBackgroundFromShape((ShapeContent) content, e, x, y);
    }
    return null;
  }

  protected TableCellBackground createBackgroundFromShape
      (final ShapeContent shapeContent, final Element element,
           final float x, final float y)
  {
    final ElementStyleSheet backgroundStyle =
            createStyleForElement(element, x, y);
    final Color color = (Color) backgroundStyle.getStyleProperty
        (ElementStyleSheet.PAINT);
    final Rectangle2D shapeBounds = shapeContent.getBounds();
    final Shape shape = shapeContent.getShape();

    TableCellBackground bg = null;
    if (backgroundStyle.getBooleanStyleProperty(ShapeElement.DRAW_SHAPE))
    {
      final Object maybeStroke =
          backgroundStyle.getStyleProperty(ElementStyleSheet.STROKE);
      final float width;

      if (maybeStroke instanceof BasicStroke)
      {
        final BasicStroke stroke = (BasicStroke) maybeStroke;
        width = stroke.getLineWidth();
      }
      else
      {
        width = 1;
      }

      if (shape instanceof Line2D)
      {
        if ((shapeBounds.getWidth() == 0) && (shapeBounds.getHeight() == 0))
        {
          // this shape has no content ...
          return null;
        }
        else if (shapeBounds.getHeight() == 0)
        {
          // horizontal line
          bg = new TableCellBackground(shapeContent, backgroundStyle, null);
          bg.setBorderTop(color, width);
        }
        else if (shapeBounds.getWidth() == 0)
        {
          // vertical line
          bg = new TableCellBackground(shapeContent, backgroundStyle, null);
          bg.setBorderLeft(color, width);
        }
      }
      else if (shape instanceof Rectangle2D)
      {
        if (backgroundStyle.getBooleanStyleProperty(ShapeElement.FILL_SHAPE))
        {
          bg = new TableCellBackground(shapeContent, backgroundStyle, color);
        }
        else
        {
          bg = new TableCellBackground(shapeContent, backgroundStyle, null);
        }
        bg.setBorderLeft(color, width);
        bg.setBorderTop(color, width);
        bg.setBorderBottom(color, width);
        bg.setBorderRight(color, width);
      }
    }
    else if (backgroundStyle.getBooleanStyleProperty(ShapeElement.FILL_SHAPE))
    {
      if (shape instanceof Rectangle2D)
      {
        bg = new TableCellBackground(shapeContent, backgroundStyle, color);
      }
    }

    return bg;
  }

  protected MetaElement createElement (final Element e,
                                       final float x, final float y)
          throws ContentCreationException
  {
    if (e.isVisible() == false)
    {
      return null;
    }

    if (e instanceof Band)
    {
      final Rectangle2D rect = createElementBounds(e.getStyle(), x, y);
      return createBandCell(rect);
    }

    if (e.getContentType().equals(ShapeElement.CONTENT_TYPE))
    {
      return createBackground(e, x, y);
    }
    else if (e.getContentType().equals(DrawableElement.CONTENT_TYPE))
    {
      return createDrawableCell(e, x, y);
    }
    else if (e.getContentType().equals(ImageElement.CONTENT_TYPE))
    {
      return createImageCell (e, x, y);
    }
    else if (e.getContentType().equals(TextElement.CONTENT_TYPE))
    {
      return createTextCell (e, x, y);
    }
    return null;
  }

  protected abstract MetaElement createTextCell (Element e, float x, float y);

  protected abstract MetaElement createImageCell (Element e, float x, float y);

  protected abstract MetaElement createDrawableCell (Element e, float x, float y);

}