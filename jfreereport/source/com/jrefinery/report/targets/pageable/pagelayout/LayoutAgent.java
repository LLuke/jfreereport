/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * ----------------
 * LayoutAgent.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LayoutAgent.java,v 1.2 2002/12/04 16:20:57 mungady Exp $
 *
 * Changes
 * -------
 * 04-Dec-2002 : Added Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.pageable.pagelayout;

/**
 * An abstract base class that decides how some layout tasks are done.
 *  No real implementation ... next release ..
 * 
 * @author Thomas Morgner
 */
public abstract class LayoutAgent
{
  /**
   * Default constructor.
   */
  public LayoutAgent()
  {
  }

  /**
   * Processes a layout task.  The method will return the status of the task processing:
   * <ul>
   *   <li> PROCESSING_COMPLETE - the processing has been completed;</li>
   *   <li> PROCESSING_INCOMPLETE - the processing has only been partially completed;</li>
   *   <li> WONT_PROCESS - the agent won't process the task.</li>
   * </ul>
   *
   * @param task  the layout task.
   *
   * @return the progress status.
   */
  public abstract LayoutAgentProgress processTask (LayoutTask task);
}
