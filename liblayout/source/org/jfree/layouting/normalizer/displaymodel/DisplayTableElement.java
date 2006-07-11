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
 * DisplayTableElement.java
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
package org.jfree.layouting.normalizer.displaymodel;

import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.normalizer.content.NormalizationException;

/**
 * A table.
 *
 * @author Thomas Morgner
 */
public class DisplayTableElement extends DisplayElement
{
  public static final int EMPTY = 0;
  public static final int THEAD_SEEN = 1;
  public static final int TFOOT_SEEN = 2;
  public static final int TBODY_SEEN = 3;

  private int feedState;

  public DisplayTableElement(final LayoutContext context)
  {
    super(context);
  }

  public int getFeedState()
  {
    return feedState;
  }

  public void setFeedState(final int feedState)
  {
    this.feedState = feedState;
  }

  public void add(DisplayNode node) throws NormalizationException
  {
    if (node instanceof DisplayTableSectionElement == false)
    {
      throw new NormalizationException(node + " is no table-section.");
    }
    feedState = 1;
    addInternal(node);
  }

  public void markFinished() throws NormalizationException
  {
    if (isFinished())
    {
      return;
    }

    if (feedState == 0)
    {
      // autogenerate an empty table ...

    }

    super.markFinished();
    getRootFlow().getContentGenerator().finishedTable();
  }

}
