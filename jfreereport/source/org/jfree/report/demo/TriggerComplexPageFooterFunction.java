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
 * TriggerComplexPageFooterFunction.java
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
package org.jfree.report.demo;

import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.AbstractFunction;

public class TriggerComplexPageFooterFunction extends AbstractFunction
{
  /**
   * Creates an unnamed function. Make sure the name of the function is set using {@link
   * #setName} before the function is added to the report's function collection.
   */
  public TriggerComplexPageFooterFunction ()
  {
  }

  /**
   * Receives notification that report generation has completed, the report footer was
   * printed, no more output is done. This is a helper event to shut down the output
   * service.
   *
   * @param event The event.
   */
  public void reportDone (final ReportEvent event)
  {
    event.getReport().getPageFooter().getElement("page-footer-content").setVisible(false);
    event.getReport().getPageFooter().getElement("fake-report-footer").setVisible(true);
  }

  /**
   * Receives notification that report generation initializes the current run. <P> The
   * event carries a ReportState.Started state.  Use this to initialize the report.
   *
   * @param event The event.
   */
  public void reportInitialized (final ReportEvent event)
  {
    event.getReport().getPageFooter().getElement("page-footer-content").setVisible(true);
    event.getReport().getPageFooter().getElement("fake-report-footer").setVisible(false);
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on the
   * expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue ()
  {
    return null;
  }
}
