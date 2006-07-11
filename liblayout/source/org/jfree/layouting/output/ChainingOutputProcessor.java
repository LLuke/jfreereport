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
 * ChainedOutputProcessor.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.output;

import org.jfree.fonts.registry.FontStorage;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.layouter.feed.DefaultInputFeed;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.normalizer.ChainingNormalizer;
import org.jfree.layouting.normalizer.content.Normalizer;
import org.jfree.layouting.normalizer.displaymodel.ModelBuilder;
import org.jfree.layouting.renderer.ChainingRenderer;
import org.jfree.layouting.renderer.Renderer;

/**
 * Creation-Date: 16.06.2006, 14:44:55
 *
 * @author Thomas Morgner
 */
public class ChainingOutputProcessor implements OutputProcessor
{
  private OutputProcessor outputProcessor;

  public ChainingOutputProcessor(final OutputProcessor outputProcessor)
  {
    this.outputProcessor = outputProcessor;
  }

  public InputFeed createInputFeed(LayoutProcess layoutProcess)
  {
    return new DefaultInputFeed(layoutProcess);
  }

  public OutputProcessorMetaData getMetaData()
  {
    return outputProcessor.getMetaData();
  }

  /**
   * Returns the content normalizer implementation for this OP. The content
   * normalizer is responsible for resolving the styles and for initiating the
   * display model building.
   *
   * @param layoutProcess the layout process that governs all.
   * @return the created content normalizer.
   */
  public Normalizer createNormalizer(LayoutProcess layoutProcess)
  {
    ChainingNormalizer chainingNormalizer = new ChainingNormalizer
            (outputProcessor.createNormalizer(layoutProcess));
    return chainingNormalizer;
  }

  /**
   * The model builder normalizes the input and builds the Display-Model. The
   * DisplayModel enriches and normalizes the logical document model so that it
   * is better suited for rendering.
   *
   * @param layoutProcess the layout process that governs all.
   * @return the created model builder.
   */
  public ModelBuilder createModelBuilder(LayoutProcess layoutProcess)
  {
    // register the normalizer ..
    return outputProcessor.createModelBuilder(layoutProcess);
  }

  public Renderer createRenderer(LayoutProcess layoutProcess)
  {
    ChainingRenderer chainingRenderer = new ChainingRenderer
            (outputProcessor.createRenderer(layoutProcess));
    return chainingRenderer;
  }
}
