/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
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
 * HtmlOutputProcessor.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: HtmlOutputProcessor.java,v 1.4 2006/07/11 13:29:54 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.output.streaming.html;

import java.io.OutputStream;

import org.jfree.fonts.awt.AWTFontRegistry;
import org.jfree.fonts.registry.DefaultFontStorage;
import org.jfree.fonts.registry.FontRegistry;
import org.jfree.fonts.registry.FontStorage;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.renderer.Renderer;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.normalizer.content.Normalizer;
import org.jfree.layouting.normalizer.content.ContentNormalizer;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.layouter.feed.DefaultInputFeed;
import org.jfree.layouting.normalizer.displaymodel.ModelBuilder;
import org.jfree.layouting.normalizer.displaymodel.EmptyModelBuilder;
import org.jfree.layouting.normalizer.generator.ContentGenerator;
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
    this.metaData = new HtmlOutputProcessorMetaData(fontStorage);
  }

  public OutputProcessorMetaData getMetaData()
  {
    return metaData;
  }

  public Renderer createRenderer(LayoutProcess layoutProcess)
  {
    return null;
  }

  public InputFeed createInputFeed(LayoutProcess layoutProcess)
  {
    return new DefaultInputFeed(layoutProcess);
  }
  
  public FontStorage getFontStorage()
  {
    return fontStorage;
  }

  /**
   * Returns the content normalizer implementation for this OP. The content
   * normalizer is responsible for resolving the styles and for initiating the
   * DOM building.
   *
   * @return
   */
  public Normalizer createNormalizer(LayoutProcess layoutProcess)
  {
    return new ContentNormalizer(layoutProcess);
  }

  /**
   * The model builder normalizes the input and builds the Display-Model. The
   * DisplayModel enriches and normalizes the logical document model so that it
   * is better suited for rendering.
   *
   * @return
   */
  public ModelBuilder createModelBuilder(LayoutProcess layoutProcess)
  {
    return new EmptyModelBuilder();
  }

  /**
   * Creates a new content generator. The content generator is responsible for
   * creating the visual content from the display model.
   *
   * @param layoutProcess the layout process that governs all.
   * @return the created content generator.
   */
  public ContentGenerator createContentGenerator(LayoutProcess layoutProcess)
  {
    return null;
  }

  public void processContent(LogicalPageBox logicalPage)
  {

  }
}
