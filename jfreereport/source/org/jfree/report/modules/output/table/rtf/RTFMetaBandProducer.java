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
 * RTFMetaBandProducer.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: RTFMetaBandProducer.java,v 1.7 2005/03/16 21:06:48 taqua Exp $
 *
 * Changes 
 * -------------------------
 * Mar 14, 2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.rtf;

import com.lowagie.text.pdf.BaseFont;
import org.jfree.report.Element;
import org.jfree.report.ImageContainer;
import org.jfree.report.content.AnchorContentFactoryModule;
import org.jfree.report.content.ContentCreationException;
import org.jfree.report.content.ContentFactory;
import org.jfree.report.content.DefaultContentFactory;
import org.jfree.report.content.ImageContent;
import org.jfree.report.content.ImageContentFactoryModule;
import org.jfree.report.content.ShapeContentFactoryModule;
import org.jfree.report.content.TextContentFactoryModule;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.modules.output.support.itext.BaseFontCreateException;
import org.jfree.report.modules.output.support.itext.BaseFontSupport;
import org.jfree.report.modules.output.support.itext.ITextImageCache;
import org.jfree.report.modules.output.table.base.RawContent;
import org.jfree.report.modules.output.table.base.TableMetaBandProducer;
import org.jfree.report.modules.output.table.rtf.metaelements.RTFImageMetaElement;
import org.jfree.report.modules.output.table.rtf.metaelements.RTFTextMetaElement;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.geom.StrictBounds;

public class RTFMetaBandProducer extends TableMetaBandProducer
{
  private BaseFontSupport baseFontSupport;
  private String encoding;
  private ITextImageCache imageCache;

  public RTFMetaBandProducer (final String encoding)
  {
    super(new DefaultLayoutSupport(createContentFactory()));
    if (encoding == null)
    {
      throw new NullPointerException("Encoding must be a non null value");
    }
    this.encoding = encoding;
    this.baseFontSupport = new BaseFontSupport();
    this.imageCache = new ITextImageCache();
  }


  private static ContentFactory createContentFactory()
  {
    final DefaultContentFactory contentFactory = new DefaultContentFactory();
    contentFactory.addModule(new TextContentFactoryModule());
    contentFactory.addModule(new ShapeContentFactoryModule());
    contentFactory.addModule(new AnchorContentFactoryModule());
    if (isImageSupportEnabled())
    {
      contentFactory.addModule(new ImageContentFactoryModule());
    }
    return contentFactory;

  }
  /**
   * The RTF-Target does not support drawable content.
   *
   * @param e
   * @param x
   * @param y
   * @return
   */
  protected MetaElement createDrawableCell (final Element e,
                                            final long x, final long y)
  {
    return null;
  }

  protected MetaElement createImageCell (final Element e,
                                         final long x, final long y)
  {
    final Object o = e.getValue();
    if (o instanceof ImageContainer == false)
    {
      return null;
    }

    if (isImageSupportEnabled() == false)
    {
      return null;
    }
    final StrictBounds rect = (StrictBounds)
            e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
    if (rect.getWidth() == 0 || rect.getHeight() == 0)
    {
      // remove emtpy images.
      return null;
    }
    return new RTFImageMetaElement(new ImageContent((ImageContainer) o, rect),
            createStyleForImageElement(e, x, y), imageCache);
  }

  private static boolean isImageSupportEnabled ()
  {
    return "true".equals(ReportConfiguration.getGlobalConfig().getConfigProperty
                ("org.jfree.report.modules.output.table.rtf.EnableImages"));
  }

  protected MetaElement createTextCell (final Element e,
                                        final long x, final long y)
          throws ContentCreationException
  {
    final Object o = e.getValue();
    if (o instanceof String == false)
    {
      return null;
    }
    final StrictBounds rect = (StrictBounds)
            e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);

    final FontDefinition def = e.getStyle().getFontDefinitionProperty();
    final String encoding = (String) e.getStyle().getStyleProperty
            (ElementStyleSheet.FONTENCODING, this.encoding);

    try
    {
      final BaseFont font =
              baseFontSupport.createBaseFont(def, encoding, false).getBaseFont();
      return new RTFTextMetaElement(new RawContent(rect, o),
              createStyleForTextElement(e, x, y), font);
    }
    catch (BaseFontCreateException e1)
    {
      throw new ContentCreationException
              ("Unable to create font for text element.", e1);
    }

  }
}
