/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * --------------------
 * PostItemGroupState.java
 * --------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: PostItemGroupState.java,v 1.2 2002/11/07 21:45:28 taqua Exp $
 *
 * Changes
 * -------
 */
package com.jrefinery.report.states;



/**
 * The only purpose for this state is to fire the itemsFinished event. After that task is done,
 * a PreGroupFooterState-State gets active.
 */
public class PostItemGroupState extends ReportState
{
  /**
   * Creates a new 'post-item-group' state.
   *
   * @param previous  the previous state.
   */
  public PostItemGroupState (ReportState previous)
  {
    super (previous);
  }

  /**
   * Advances from this state to the next.
   * <p>
   * Just inform everybody that the itemband is no longer printed. Next state will be
   * PreGroupFooterState.
   *
   * @return the next report state.
   */
  public ReportState advance ()
  {
    fireItemsFinishedEvent ();
    return new PreGroupFooterState (this);
  }
}
