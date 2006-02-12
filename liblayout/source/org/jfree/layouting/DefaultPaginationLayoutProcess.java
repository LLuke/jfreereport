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
 * $Id: DefaultPaginationLayoutProcess.java,v 1.1 2006/02/12 21:38:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05.12.2005 : Initial version
 */
package org.jfree.layouting;

import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.layouter.feed.PaginationInputFeed;
import org.jfree.layouting.layouter.state.LayoutSavePoint;
import org.jfree.layouting.output.pageable.PageableOutputProcessor;
import org.jfree.layouting.normalizer.pagable.PaginatingNormalizer;
import org.jfree.layouting.normalizer.Normalizer;

/**
 * Creation-Date: 05.12.2005, 19:16:56
 *
 * @author Thomas Morgner
 */
public class DefaultPaginationLayoutProcess extends AbstractLayoutProcess
  implements PaginationLayoutProcess
{
  private PageableOutputProcessor processor;

  public DefaultPaginationLayoutProcess(PageableOutputProcessor processor)
  {
    super(processor);
    this.processor = processor;
  }

  protected InputFeed createInputFeed()
  {
    return new PaginationInputFeed(this);
  }

  // todo: Handle pages ...
  public Normalizer getNormalizer()
  {
    return null;
  }

  public void addSavePoint (final LayoutSavePoint sp)
  {
    // todo implement me

  }

  public LayoutSavePoint getSavePoint (int pos)
  {
    // todo implement me
    return null;
  }

  public int getSavePointCount ()
  {
    // todo implement me
    return 0;
  }
}