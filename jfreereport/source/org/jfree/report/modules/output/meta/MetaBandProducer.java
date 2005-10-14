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
 * MetaBandProducer.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: MetaBandProducer.java,v 1.14 2005/08/12 12:09:41 taqua Exp $
 *
 * Changes
 * -------------------------
 * 14.02.2004 : Initial version
 *
 */

package org.jfree.report.modules.output.meta;

import java.util.ArrayList;

import org.jfree.report.Band;
import org.jfree.report.DrawableElement;
import org.jfree.report.Element;
import org.jfree.report.ImageElement;
import org.jfree.report.ShapeElement;
import org.jfree.report.TextElement;
import org.jfree.report.content.Content;
import org.jfree.report.content.ContentCreationException;
import org.jfree.report.content.EmptyContent;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleSheetCarrier;
import org.jfree.report.util.ElementLayoutInformation;
import org.jfree.report.util.geom.StrictBounds;

/**
 * The MetaBandProducer is responsible for converting a report band into an output target
 * dependent metaband. The metabands use a global coordinate system, so that every
 * metaelement contains the absolute position on the page.
 */
public class MetaBandProducer
{
  protected static class MetaElementStyleSheet extends ElementStyleSheet
  {
    public MetaElementStyleSheet (final String name)
    {
      super(name);
    }

    protected StyleSheetCarrier createCarrier (final ElementStyleSheet styleSheet)
    {
      throw new UnsupportedOperationException
              ("At this point, we do not support inheritance anymore.");
    }
  }

  private LayoutSupport support;

  public MetaBandProducer (final LayoutSupport support)
  {
    if (support == null)
    {
      throw new NullPointerException("LayoutSupport is null.");
    }
    this.support = support;
  }

  public LayoutSupport getLayoutSupport ()
  {
    return support;
  }

  public MetaBand createBand (final Band band, final boolean spool)
          throws ContentCreationException
  {
    return createBand(band, spool, 0, 0);
  }

  public MetaBand createBand (final Band band, final boolean spool,
                              final long parentX, final long parentY)
          throws ContentCreationException
  {
    if (band.isVisible() == false)
    {
      return null;
    }
    if (band.getElementCount() == 0)
    {
      return null;
    }

    final Element[] elements = band.getElementArray();
    final ArrayList metaElements = new ArrayList(elements.length);
    final StrictBounds bounds = (StrictBounds)
            band.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
    final long x = bounds.getX() + parentX;
    final long y = bounds.getY() + parentY;

    for (int i = 0; i < elements.length; i++)
    {
      final Element element = elements[i];
      if (element.isVisible() == false)
      {
        continue;
      }

      if (element instanceof Band)
      {
        final MetaBand metaBand = createBand((Band) element, spool, x, y);
        if (metaBand != null)
        {
          metaElements.add(metaBand);
        }
      }
      else
      {
        final MetaElement metaElement = createElement(element, x, y);
        if (metaElement != null)
        {
          metaElements.add(metaElement);
        }
      }
    }

    final MetaElement[] metaElementArray = (MetaElement[]) metaElements.toArray
            (new MetaElement[metaElements.size()]);

    final ElementStyleSheet styleForBand = createStyleForBand(band, parentX, parentY);
    final MetaBand mband = createMetaBandInstace(band, styleForBand, metaElementArray, spool);
    mband.setName(band.getName());
    return mband;
  }

  protected MetaBand createMetaBandInstace (final Band band,
                                            final ElementStyleSheet style,
                                            final MetaElement[] elements,
                                            final boolean spool)
  {
    final MetaBand mband = new MetaBand(EmptyContent.getDefaultEmptyContent(),
            style,
            elements, spool);
    return mband;
  }

  /**
   * Creates a metaelement from the given report element. After that creation, the
   * metaelement will be independent from the original element.
   * <p/>
   * This method should return null,if the element contains invalid content, which cannot
   * be displayed by the particular output target.
   *
   * @param e
   * @param parentx
   * @param parenty
   * @return
   *
   * @throws ContentCreationException
   */
  protected MetaElement createElement
          (final Element e, final long parentx, final long parenty)
          throws ContentCreationException
  {
    if (support.getContentFactory().canHandleContent(e.getContentType()) == false)
    {
      return null;
    }
    if (e.isVisible() == false)
    {
      return null;
    }

    final ElementStyleSheet styleSheet = createStyleForElement(e, parentx, parenty);
    final Content content = createContent(e, styleSheet);
    final MetaElement element = new MetaElement
            (content, styleSheet);
    element.setName(e.getName());
    return element;
  }

  protected Content createContent (final Element e, final ElementStyleSheet styleSheet)
          throws ContentCreationException
  {
    final StrictBounds bounds = (StrictBounds)
            styleSheet.getStyleProperty(ElementStyleSheet.BOUNDS);
    final ElementLayoutInformation eli = new ElementLayoutInformation(bounds);
    final Content content =
            support.getContentFactory().createContentForElement(e, eli, support);
    return content;
  }

  private ElementStyleSheet createCommonStyleForElement
          (final Element e, final long x, final long y)
  {
    final ElementStyleSheet elementStyle = e.getStyle();
    final ElementStyleSheet style =
            new MetaElementStyleSheet("meta-" + e.getName());
    style.setStyleProperty(ElementStyleSheet.BOUNDS,
            createElementBounds(elementStyle, x, y));
    style.setStyleProperty(ElementStyleSheet.VALIGNMENT,
            elementStyle.getStyleProperty(ElementStyleSheet.VALIGNMENT));
    style.setStyleProperty(ElementStyleSheet.ALIGNMENT,
            elementStyle.getStyleProperty(ElementStyleSheet.ALIGNMENT));
    style.setStyleProperty(ElementStyleSheet.HREF_TARGET,
            elementStyle.getStyleProperty(ElementStyleSheet.HREF_TARGET));
    style.setStyleProperty(ElementStyleSheet.HREF_WINDOW,
            elementStyle.getStyleProperty(ElementStyleSheet.HREF_WINDOW));
    style.setBooleanStyleProperty(ElementStyleSheet.HREF_INHERITED,
            elementStyle.isLocalKey(ElementStyleSheet.HREF_TARGET));
    return style;
  }

  protected ElementStyleSheet createStyleForImageElement
          (final Element e, final long x, final long y)
  {
    final ElementStyleSheet elementStyle = e.getStyle();
    final ElementStyleSheet style = createCommonStyleForElement(e, x, y);
    // check if necessary ...
    style.setStyleProperty(ElementStyleSheet.PAINT,
            elementStyle.getStyleProperty(ElementStyleSheet.PAINT));
    return style;
  }

  protected ElementStyleSheet createStyleForShapeElement
          (final Element e, final long x, final long y)
  {
    final ElementStyleSheet elementStyle = e.getStyle();
    final ElementStyleSheet style = createCommonStyleForElement(e, x, y);
    style.setStyleProperty(ElementStyleSheet.PAINT,
            elementStyle.getStyleProperty(ElementStyleSheet.PAINT));
    style.setStyleProperty(ElementStyleSheet.STROKE,
            elementStyle.getStyleProperty(ElementStyleSheet.STROKE));
    style.setStyleProperty(ShapeElement.FILL_SHAPE,
            elementStyle.getStyleProperty(ShapeElement.FILL_SHAPE));
    style.setStyleProperty(ShapeElement.DRAW_SHAPE,
            elementStyle.getStyleProperty(ShapeElement.DRAW_SHAPE));
    return style;
  }

  protected ElementStyleSheet createStyleForDrawableElement
          (final Element e, final long x, final long y)
  {
    //final ElementStyleSheet elementStyle = e.getStyle();
    final ElementStyleSheet style = createCommonStyleForElement(e, x, y);
    return style;
  }

  protected ElementStyleSheet createStyleForTextElement
          (final Element e, final long x, final long y)
  {
    final ElementStyleSheet elementStyle = e.getStyle();
    final ElementStyleSheet style = createCommonStyleForElement(e, x, y);
    style.setFontDefinitionProperty(elementStyle.getFontDefinitionProperty());
    style.setStyleProperty(ElementStyleSheet.PAINT,
            elementStyle.getStyleProperty(ElementStyleSheet.PAINT));
    style.setStyleProperty(ElementStyleSheet.STROKE,
            elementStyle.getStyleProperty(ElementStyleSheet.STROKE));
    return style;
  }

  protected ElementStyleSheet createStyleForElement
          (final Element e, final long x, final long y)
  {

    if (e.getContentType().equals(TextElement.CONTENT_TYPE))
    {
      return createStyleForTextElement(e, x, y);
    }
    else if (e.getContentType().equals(ShapeElement.CONTENT_TYPE))
    {
      return createStyleForShapeElement(e, x, y);
    }
    else if (e.getContentType().equals(DrawableElement.CONTENT_TYPE))
    {
      // nothing ...
      return createStyleForDrawableElement(e, x, y);
    }
    else if (e.getContentType().equals(ImageElement.CONTENT_TYPE))
    {
      return createStyleForImageElement(e, x, y);
    }
    return createCommonStyleForElement(e, x, y);
  }

  protected StrictBounds createElementBounds (final ElementStyleSheet style,
                                              final long x, final long y)
  {
    final StrictBounds bounds = (StrictBounds)
            style.getStyleProperty(ElementStyleSheet.BOUNDS);

    return new StrictBounds
            ((x + bounds.getX()),
                    (y + bounds.getY()),
                    bounds.getWidth(),
                    bounds.getHeight());

  }

  protected ElementStyleSheet createStyleForBand
          (final Band band, final long x, final long y)
  {
    final ElementStyleSheet bandStyle = band.getStyle();
    final ElementStyleSheet style =
            new MetaElementStyleSheet("meta-band");
    style.setStyleProperty
            (ElementStyleSheet.BOUNDS, createElementBounds(bandStyle, x, y));
    return style;
  }
}
