/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * CreateGroupAnchorsFunction.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.function;

import org.jfree.report.Anchor;
import org.jfree.report.Group;
import org.jfree.report.event.ReportEvent;

public class CreateGroupAnchorsFunction extends AbstractFunction
{
  private String group;
  private String anchorPrefix;
  private Anchor anchor;
  private int count;

  /**
   * Creates an unnamed function. Make sure the name of the function is set using {@link
   * #setName} before the function is added to the report's function collection.
   */
  public CreateGroupAnchorsFunction ()
  {
  }

  public String getAnchorPrefix ()
  {
    return anchorPrefix;
  }

  public void setAnchorPrefix (final String anchorPrefix)
  {
    this.anchorPrefix = anchorPrefix;
  }

  public String getGroup ()
  {
    return group;
  }

  public void setGroup (final String group)
  {
    this.group = group;
  }

  /**
   * Receives notification that report generation initializes the current run. <P> The
   * event carries a ReportState.Started state.  Use this to initialize the report.
   *
   * @param event The event.
   */
  public void reportInitialized (final ReportEvent event)
  {
    anchor = null;
    count = 0;
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event the event.
   */
  public void groupStarted (final ReportEvent event)
  {
    if (FunctionUtilities.isDefinedGroup(getGroup(), event) == false)
    {
      return;
    }

    final StringBuffer targetBuffer = new StringBuffer();
    targetBuffer.append(getAnchorPrefix());
    final Group g = FunctionUtilities.getCurrentGroup(event);
    targetBuffer.append(g.getName());
    targetBuffer.append("%3D");
    targetBuffer.append(count);
    anchor = new Anchor(targetBuffer.toString());
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on the
   * expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue ()
  {
    return anchor;
  }
}
