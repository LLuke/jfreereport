/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
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
 * $Id: AbstractOutputProcessor.java,v 1.1 2006/11/12 14:29:58 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.layouting.output;

import org.jfree.util.Configuration;
import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.normalizer.content.Normalizer;
import org.jfree.layouting.normalizer.content.ContentNormalizer;
import org.jfree.layouting.normalizer.displaymodel.ModelBuilder;
import org.jfree.layouting.normalizer.displaymodel.DisplayModelBuilder;
import org.jfree.layouting.normalizer.generator.DefaultContentGenerator;
import org.jfree.layouting.normalizer.generator.PrintContentGenerator;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.layouter.feed.DefaultInputFeed;

/**
 * This base class configures the layouter for the normal DOM processing.
 * The display model assumes, that all elements can be nested freely, without
 * imposing any limitations at all.
 *
 * Using this as base process for text-processing document output (like RTF
 * or OpenOffice-Writer) is a sure way to the hell of funny behaviour. 
 *
 * @author Thomas Morgner
 */
public abstract class AbstractOutputProcessor implements OutputProcessor
{
  private Configuration configuration;

  public AbstractOutputProcessor(final Configuration configuration)
  {
    if (configuration == null)
    {
      this.configuration = LibLayoutBoot.getInstance().getGlobalConfig();
    }
    else
    {
      this.configuration = configuration;
    }
  }

  public Configuration getConfiguration()
  {
    return configuration;
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
    //return new DisplayModelBuilder(new PrintContentGenerator(layoutProcess), layoutProcess);
    return new DisplayModelBuilder(new DefaultContentGenerator(layoutProcess), layoutProcess);
  }

}
