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
 * $Id: HtmlMetaBandProducer.java,v 1.8 2005/02/23 21:05:34 taqua Exp $
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

import org.jfree.report.DefaultImageReference;
import org.jfree.report.Element;
import org.jfree.report.ImageContainer;
import org.jfree.report.content.ImageContent;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.modules.output.table.base.RawContent;
import org.jfree.report.modules.output.table.base.TableMetaBandProducer;
import org.jfree.report.modules.output.table.html.metaelements.HtmlImageMetaElement;
import org.jfree.report.modules.output.table.html.metaelements.HtmlTextMetaElement;
import org.jfree.report.modules.output.table.html.metaelements.HtmlMetaElement;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.ImageUtils;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.ui.Drawable;
import org.jfree.util.Log;

public class HtmlMetaBandProducer extends TableMetaBandProducer
{
  private boolean useXHTML;

  public HtmlMetaBandProducer (final boolean useXHTML)
  {
    super(new DefaultLayoutSupport());
    this.useXHTML = useXHTML;
  }

  protected MetaElement createTextCell
          (final Element e, final long x, final long y)
  {
    final Object o = e.getValue();
    if (o instanceof String == false)
    {
      return null;
    }
    final StrictBounds rect = (StrictBounds)
            e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
    final MetaElement me = new HtmlTextMetaElement(new RawContent(rect, o),
            createStyleForTextElement(e, x, y), useXHTML);
    me.setName(e.getName());
    if ("RH-02".equals(e.getName()))
    {
      Log.debug ("HERE");
    }
    return me;
  }

  protected MetaElement createImageCell
          (final Element e, final long x, final long y)
  {
    final Object o = e.getValue();
    if (o instanceof ImageContainer == false)
    {
      return null;
    }

    final StrictBounds rect = (StrictBounds)
            e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
    final ImageContent ic = new ImageContent((ImageContainer) o, (StrictBounds) rect.clone());
    final HtmlMetaElement me = new HtmlImageMetaElement(ic, createStyleForImageElement(e, x, y), useXHTML);
    me.setName(e.getName());
    return me;
  }

  protected MetaElement createDrawableCell
          (final Element e, final long x, final long y)
  {
    final Object o = e.getValue();
    if (o instanceof Drawable == false)
    {
      return null;
    }

    final Drawable drawable = (Drawable) o;
    final StrictBounds rect = (StrictBounds)
            e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);

    final int imageWidth = (int) StrictGeomUtility.toExternalValue(rect.getWidth());
    final int imageHeight = (int) StrictGeomUtility.toExternalValue(rect.getHeight());

    if (imageWidth == 0 && imageHeight == 0)
    {
      return null;
    }
    final Image image = ImageUtils.createTransparentImage(imageWidth, imageHeight);
    final Graphics2D g2 = (Graphics2D) image.getGraphics();
    // the clipping bounds are a sub-area of the whole drawable
    // we only want to print a certain area ...

    drawable.draw(g2, new Rectangle2D.Double(0, 0, imageWidth, imageHeight));
    g2.dispose();
    final DefaultImageReference imgref = new DefaultImageReference(image);
    final ImageContent ic = new ImageContent(imgref, (StrictBounds) rect.clone());
    return new HtmlImageMetaElement(ic, createStyleForDrawableElement(e, x, y), useXHTML);
  }
}
