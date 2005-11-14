/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * PageItemSumFunction.java
 * --------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ItemSumFunction.java,v 1.10 2005/08/08 15:36:29 taqua Exp $
 *
 * Changes
 * -------
 * 01-Oct-2005 : Initial version.
 */
package org.jfree.report.function;

import org.jfree.report.event.PageEventListener;
import org.jfree.report.event.ReportEvent;

/**
 * An ItemSum function, that is reset to zero on every new page.
 *
 * @author Thomas Morgner
 */
public class PageItemSumFunction extends ItemSumFunction
        implements PageEventListener
{
  private boolean pageStarted;

  public PageItemSumFunction()
  {
  }

  public void itemsAdvanced(final ReportEvent event)
  {
    if (pageStarted)
    {
      setSum(ZERO);
      pageStarted = false;
    }
    super.itemsAdvanced(event);
  }

  public void pageStarted(ReportEvent event)
  {
    pageStarted = true;
  }

  public void pageFinished(ReportEvent event)
  {
  }

  public void pageCanceled(ReportEvent event)
  {
  }

  public void pageRolledBack(ReportEvent event)
  {
  }
}