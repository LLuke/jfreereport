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
 * DefaultPaginationLayoutProcess.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: DefaultContentLayoutProcess.java,v 1.1 2006/02/12 21:38:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05.12.2005 : Initial version
 */
package org.jfree.layouting;

import org.jfree.layouting.layouter.feed.ContentInputFeed;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.layouter.state.InputFeedState;
import org.jfree.layouting.layouter.state.LayoutProcessingSavePoint;
import org.jfree.layouting.layouter.state.LayoutSavePoint;
import org.jfree.layouting.output.OutputProcessor;
import org.jfree.layouting.output.pageable.PageableOutputProcessor;
import org.jfree.layouting.normalizer.pagable.ContentGeneratingNormalizer;
import org.jfree.layouting.normalizer.Normalizer;

/**
 * Creation-Date: 05.12.2005, 19:16:56
 *
 * @author Thomas Morgner
 */
public class DefaultContentLayoutProcess extends AbstractLayoutProcess
  implements ContentLayoutProcess
{
  private LayoutSavePoint startingSavePoint;
  private LayoutSavePoint endingSavePoint;
  private PageableOutputProcessor outputProcessor;
  private InputFeed inputFeed;

  public DefaultContentLayoutProcess(final PageableOutputProcessor outputProcessor,
                                     final LayoutSavePoint startingSavePoint,
                                     final LayoutSavePoint endingSavePoint)
  {
    super(outputProcessor);
    this.outputProcessor = outputProcessor;
    this.startingSavePoint = startingSavePoint;
    this.endingSavePoint = endingSavePoint;

    if (startingSavePoint instanceof LayoutProcessingSavePoint)
    {
      LayoutProcessingSavePoint gp = (LayoutProcessingSavePoint) startingSavePoint;
      final InputFeedState state = (InputFeedState) gp.getParent();
      setNextId(state.getCurrentId());
      this.inputFeed = new ContentInputFeed(this, state);
    }
    else
    {
      setNextId(0);
      this.inputFeed = new ContentInputFeed(this, null);
    }

    this.startingSavePoint = startingSavePoint;
    this.endingSavePoint = endingSavePoint;
    this.outputProcessor = outputProcessor;
  }

  protected InputFeed createInputFeed()
  {
    return inputFeed;
  }


  public Normalizer getNormalizer()
  {
    return null;
  }
}
