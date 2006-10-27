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
 * OutputProcessor.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: OutputProcessor.java,v 1.4 2006/10/22 14:58:25 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.output;

import org.jfree.fonts.registry.FontStorage;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.normalizer.content.Normalizer;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.normalizer.displaymodel.ModelBuilder;
import org.jfree.layouting.renderer.Renderer;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;

/**
 * The output processor defines, which processing-step implementation will
 * be used during the layout process.
 *
 * Obviously, what computations are required depends heavily on the desired
 * result.
 *
 * @author Thomas Morgner
 */
public interface OutputProcessor
{
  public OutputProcessorMetaData getMetaData();

  public InputFeed createInputFeed (LayoutProcess layoutProcess);

  /**
   * Returns the content normalizer implementation for this OP. The content
   * normalizer is responsible for resolving the styles and for initiating the
   * display model building.
   *
   * @param layoutProcess the layout process that governs all.
   * @return the created content normalizer.
   */
  public Normalizer createNormalizer(LayoutProcess layoutProcess);

  /**
   * The model builder normalizes the input and builds the Display-Model. The
   * DisplayModel enriches and normalizes the logical document model so that
   * it is better suited for rendering.
   *
   * @param layoutProcess the layout process that governs all.
   * @return the created model builder.
   */
  public ModelBuilder createModelBuilder(LayoutProcess layoutProcess);

  public Renderer createRenderer (LayoutProcess layoutProcess);

  public void processContent (LogicalPageBox logicalPage);

  /**
   * Declares, whether the logical page given in process-content must have a
   * valid physical page set. Non-pageable targets may want to access the
   * logical pagebox directly. 
   *
   * @return
   */
  public boolean isPhysicalPageOutput();
}
