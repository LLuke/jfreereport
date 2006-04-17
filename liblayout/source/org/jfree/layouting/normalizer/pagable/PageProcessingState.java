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
 * PageProcessingState.java
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
package org.jfree.layouting.normalizer.pagable;

import org.jfree.layouting.layouter.feed.InputFeedState;

/**
 * This class holds all information, which are needed to restart a page on
 * a certain point. It contains all open flows and all pending elements. All
 * events up to the state specified by this object will be ignored; processing
 * restarts as soon as this state has been reached.
 *
 * @author Thomas Morgner
 */
public class PageProcessingState
{
  private InputFeedState inputFeedState;

  public PageProcessingState(final InputFeedState inputFeedState)
  {
    if (inputFeedState == null)
    {
      throw new NullPointerException();
    }
    this.inputFeedState = inputFeedState;
  }

  public InputFeedState getInputFeedState()
  {
    return inputFeedState;
  }
}
