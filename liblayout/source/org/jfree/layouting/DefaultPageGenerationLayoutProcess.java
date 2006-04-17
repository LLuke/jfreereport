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
 * DefaultPageGenerationLayoutProcess.java
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
package org.jfree.layouting;

import org.jfree.layouting.layouter.feed.DefaultInputFeed;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.layouter.feed.InputFeedState;
import org.jfree.layouting.normalizer.Normalizer;
import org.jfree.layouting.normalizer.pagable.PageProcessingState;
import org.jfree.layouting.output.pageable.PageableOutputProcessor;

/**
 * Creation-Date: 05.12.2005, 19:16:56
 *
 * @author Thomas Morgner
 */
public class DefaultPageGenerationLayoutProcess extends AbstractLayoutProcess
  implements PageGenerationLayoutProcess
{
  private PageProcessingState startingSavePoint;
  private PageProcessingState endingSavePoint;
  private PageableOutputProcessor outputProcessor;
  private InputFeed inputFeed;

  public DefaultPageGenerationLayoutProcess(final PageableOutputProcessor outputProcessor,
                                            final PageProcessingState startingSavePoint,
                                            final PageProcessingState endingSavePoint)
  {
    super(outputProcessor);
    this.outputProcessor = outputProcessor;
    this.startingSavePoint = startingSavePoint;
    this.endingSavePoint = endingSavePoint;

    if (startingSavePoint != null)
    {
      final InputFeedState state = startingSavePoint.getInputFeedState();
      setNextId(state.getCurrentId());
      this.inputFeed = new DefaultInputFeed(this, state);
    }
    else
    {
      setNextId(0);
      this.inputFeed = new DefaultInputFeed(this);
    }

    this.startingSavePoint = startingSavePoint;
    this.endingSavePoint = endingSavePoint;
    this.outputProcessor = outputProcessor;
  }

  public PageProcessingState getStartingSavePoint()
  {
    return startingSavePoint;
  }

  public PageProcessingState getEndingSavePoint()
  {
    return endingSavePoint;
  }

  protected InputFeed createInputFeed()
  {
    return inputFeed;
  }


  public Normalizer getNormalizer()
  {
    return outputProcessor.createGenerateNormalizer();
  }
}
