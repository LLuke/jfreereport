/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
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
 * HtmlMetaBandProducer.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: HtmlMetaBandProducer.java,v 1.1 2004/03/16 18:03:37 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 09.03.2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.html;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Band;
import org.jfree.report.DefaultImageReference;
import org.jfree.report.DrawableContainer;
import org.jfree.report.DrawableElement;
import org.jfree.report.Element;
import org.jfree.report.ImageElement;
import org.jfree.report.ShapeElement;
import org.jfree.report.TextElement;
import org.jfree.report.ImageContainer;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.content.ContentCreationException;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.modules.output.table.base.RawContent;
import org.jfree.report.modules.output.table.base.TableMetaBandProducer;
import org.jfree.report.modules.output.table.html.metaelements.HtmlImageMetaElement;
import org.jfree.report.modules.output.table.html.metaelements.HtmlTextMetaElement;
import org.jfree.report.util.ImageUtils;
import org.jfree.ui.Drawable;

public class HtmlMetaBandProducer extends TableMetaBandProducer
{
  private boolean useXHTML;

  public HtmlMetaBandProducer (final boolean useXHTML)
  {
    super(new DefaultLayoutSupport());
    this.useXHTML = useXHTML;
  }

  protected MetaElement createTextCell
          (final Element e, final float x, final float y)
  {
    final Object o = e.getValue();
    if (o instanceof String == false)
    {
      return null;
    }
    final Rectangle2D rect = (Rectangle2D)
            e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
    return new HtmlTextMetaElement (new RawContent (rect, o),
            createStyleForTextElement(e, x, y), useXHTML);
  }

  protected MetaElement createImageCell
          (final Element e, final float x, final float y)
  {
    final Object o = e.getValue();
    if (o instanceof ImageContainer == false)
    {
      return null;
    }

    final Rectangle2D rect = (Rectangle2D)
            e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
    return new HtmlImageMetaElement (new RawContent (rect, o),
            createStyleForImageElement(e, x, y), useXHTML);
  }

  protected MetaElement createDrawableCell
          (final Element e, final float x, final float y)
  {
    final Object o = e.getValue();
    if (o instanceof DrawableContainer == false)
    {
      return null;
    }

    // todo: This is not a really valid implementation.
    // In fact, none of the code
    // below is valid, as it does not care about the various content transformations
    // possibly required to display the content correctly.
    //
    // we have to rework that .. all of that!
    final DrawableContainer container = (DrawableContainer) o;
    final Drawable drawable = container.getDrawable();
    final Rectangle2D rect = (Rectangle2D)
            e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
    final Image image = ImageUtils.createTransparentImage
        ((int) rect.getWidth(), (int) rect.getHeight());
    final Graphics2D g2 = (Graphics2D) image.getGraphics();
    // the clipping bounds are a sub-area of the whole drawable
    // we only want to print a certain area ...
    drawable.draw(g2, new Rectangle2D.Double
        (rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()));
    g2.dispose();
    final DefaultImageReference imgref = new DefaultImageReference(image);

    return new HtmlImageMetaElement (new RawContent (rect, imgref),
            createStyleForDrawableElement(e, x, y), useXHTML);
  }
}
