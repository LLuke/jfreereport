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
 * StageOnePageableOutputProcessor.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: StageOnePageableOutputProcessor.java,v 1.3 2006/07/18 14:40:28 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.output.junit;

import org.jfree.fonts.awt.AWTFontRegistry;
import org.jfree.fonts.registry.DefaultFontStorage;
import org.jfree.fonts.registry.FontRegistry;
import org.jfree.fonts.registry.FontStorage;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.layouter.feed.DefaultInputFeed;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.normalizer.content.ContentNormalizer;
import org.jfree.layouting.normalizer.content.Normalizer;
import org.jfree.layouting.normalizer.displaymodel.DisplayModelBuilder;
import org.jfree.layouting.normalizer.displaymodel.ModelBuilder;
import org.jfree.layouting.normalizer.generator.DefaultContentGenerator;
import org.jfree.layouting.output.OutputProcessor;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.output.streaming.html.HtmlOutputProcessorMetaData;
import org.jfree.layouting.renderer.DefaultRenderer;
import org.jfree.layouting.renderer.Renderer;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;

/**
 * Creation-Date: 31.05.2006, 16:28:33
 *
 * @author Thomas Morgner
 */
public class StageOnePageableOutputProcessor implements OutputProcessor
{
  private OutputProcessorMetaData metaData;

  public StageOnePageableOutputProcessor()
  {
    final FontRegistry fontRegistry = new AWTFontRegistry();
    final FontStorage fontStorage = new DefaultFontStorage(fontRegistry);
    this.metaData = new HtmlOutputProcessorMetaData(fontStorage);
  }

  public OutputProcessorMetaData getMetaData()
  {
    return metaData;
  }

  public InputFeed createInputFeed(LayoutProcess layoutProcess)
  {
    return new DefaultInputFeed(layoutProcess);
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
    return new DisplayModelBuilder(new DefaultContentGenerator(layoutProcess), layoutProcess);
  }

  public Renderer createRenderer(LayoutProcess layoutProcess)
  {
    return new DefaultRenderer(layoutProcess);
  }

  public void processContent(LogicalPageBox logicalPage)
  {

  }
}
