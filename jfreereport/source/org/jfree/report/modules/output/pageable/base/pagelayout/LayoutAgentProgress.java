/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ------------------------
 * LayoutAgentProgress.java
 * ------------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LayoutAgentProgress.java,v 1.1 2003/07/07 22:44:07 taqua Exp $
 *
 * Changes
 * -------
 * 04-Dec-2002 : Added Javadocs (DG);
 *
 */

package org.jfree.report.modules.output.pageable.base.pagelayout;

/**
 * A class that conveys the completion status of a LayoutTask.
 *  No real implementation ... next release ..
 *
 * @author Thomas Morgner.
 */
public final class LayoutAgentProgress
{
  /** Indicates that the layout agent will not process the task. */
  public static final LayoutAgentProgress WONT_PROCESS = new LayoutAgentProgress("WONT_PROCESS");

  /** Indicates that the layout agent has completed processing the task. */
  public static final LayoutAgentProgress PROCESSING_COMPLETE
      = new LayoutAgentProgress("PROCESSING_COMPLETE");

  /** Indicates that the layout agent has only partially completed processing the task. */
  public static final LayoutAgentProgress PROCESSING_INCOMPLETE
      = new LayoutAgentProgress("PROCESSING_INCOMPLETE");

  /** The name. */
  private final String myName; // for debug only

  /**
   * Creates a new progress object.
   *
   * @param name  the name.
   */
  private LayoutAgentProgress(final String name)
  {
    myName = name;
  }

  /**
   * Returns the name of the progress object.
   *
   * @return the name.
   */
  public String toString()
  {
    return myName;
  }
}
