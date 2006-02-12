/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * HtmlOutputTarget.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 02.01.2006 : Initial version
 */
package org.jfree.layouting.output.streaming.html;

import java.io.OutputStream;

import org.jfree.fonts.awt.AWTFontRegistry;
import org.jfree.fonts.registry.DefaultFontStorage;
import org.jfree.fonts.registry.FontRegistry;
import org.jfree.fonts.registry.FontStorage;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.PageContext;
import org.jfree.layouting.normalizer.streaming.StreamingNormalizer;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.output.streaming.StreamingOutputProcessor;

/**
 * A later version should allow the same degree of flexibility as jfreereport's
 * original HTML export.
 *
 * @author Thomas Morgner
 */
public class HtmlOutputProcessor implements StreamingOutputProcessor
{
  private FontRegistry fontRegistry;
  private FontStorage fontStorage;
  private OutputProcessorMetaData metaData;
  private OutputStream outstream;

  public HtmlOutputProcessor(final OutputStream outstream)
  {
    this.outstream = outstream;
    this.fontRegistry = new AWTFontRegistry();
    this.fontStorage = new DefaultFontStorage(fontRegistry);
    this.metaData = new HtmlOutputProcessorMetaData(fontRegistry);
  }

  public void processNode(LayoutNode node)
  {
    
  }

  public OutputProcessorMetaData getMetaData()
  {
    return metaData;
  }

  public FontStorage getFontStorage()
  {
    return fontStorage;
  }

  public StreamingNormalizer createNormalizer()
  {
    return new HtmlStreamingNormalizer(outstream);
  }

  public PageContext createPageContext(int pageNumber)
  {
    return null;
  }
}
