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
 * ElementChooserAgent.java
 * ------------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ElementChooserAgent.java,v 1.5 2003/06/29 16:59:29 taqua Exp $
 *
 * Changes
 * -------
 */

package org.jfree.report.modules.output.pageable.base.pagelayout;

/**
 * ElementChooserAgent. No real implementation ... next release ..
 *
 * @author Thomas Morgner
 */
public class ElementChooserAgent extends LayoutAgent
{
  /** ??. */
  private static final String CHOOSING_DONE = ElementChooserAgent.class.getName();

  /**
   * Default constructor.
   */
  public ElementChooserAgent()
  {
  }

  /**
   * ??
   *
   * @param task  ??.
   *
   * @return ??.
   */
  public LayoutAgentProgress processTask(final LayoutTask task)
  {
    final EventType type = task.getEventType();
    if (type == EventType.PAGE_START)
    {
    }
    else if (type == EventType.PAGE_END)
    {
    }
    else if (type == EventType.REPORT_START)
    {
    }
    else if (type == EventType.REPORT_END)
    {
    }
    else if (type == EventType.GROUP_START)
    {
    }
    else if (type == EventType.GROUP_END)
    {
    }
    else if (type == EventType.ITEM_ADVANCE)
    {
    }
    return LayoutAgentProgress.WONT_PROCESS;
  }

}
