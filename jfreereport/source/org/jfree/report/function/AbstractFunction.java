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
 * ---------------------
 * AbstractFunction.java
 * ---------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractFunction.java,v 1.7 2005/01/25 00:00:07 taqua Exp $
 *
 * Changes
 * -------
 * 15-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 * 24-Apr-2002 : Added property support and removed the get/set Field/Group
 *               functions.
 * 10-May-2002 : Support for ReportListenerInterface added. All old eventFunctions are
 *               marked deprecated. The name-attribute must not be null, or the validity check
 *               will fail.
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 * 27-Aug-2002 : Documentation and removed the deprecated functions
 * 31-Aug-2002 : Documentation update and removed isInitializedFunction
 * 10-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 *
 */

package org.jfree.report.function;

import java.io.Serializable;

import org.jfree.report.event.ReportEvent;

/**
 * Base class for implementing new report functions.  Provides empty implementations of all the
 * methods in the Function interface.
 * <p>
 * The function is initialized when it gets added to the report. The method <code>initialize</code>
 * gets called to perform the required initializations. At this point, all function properties must
 * have been set to a valid state and the function must be named. If the initialisation fails, a
 * FunctionInitializeException is thrown and the function get not added to the report.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractFunction extends AbstractExpression
    implements Function, Serializable
{
  /**
   * Creates an unnamed function. Make sure the name of the function is set using
   * {@link #setName} before the function is added to the report's function collection.
   */
  protected AbstractFunction()
  {
  }

  /**
   * Creates an named function.
   *
   * @param name the name of the function.
   */
  protected AbstractFunction(final String name)
  {
    setName(name);
  }

  /**
   * Receives notification that report generation initializes the current run.
   * <P>
   * The event carries a ReportState.Started state.  Use this to initialize the report.
   *
   * @param event The event.
   */
  public void reportInitialized(final ReportEvent event)
  {
  }

  /**
   * Receives notification that the report has started.
   *
   * @param event  the event.
   */
  public void reportStarted(final ReportEvent event)
  {
  }

  /**
   * Receives notification that the report has finished.
   *
   * @param event  the event.
   */
  public void reportFinished(final ReportEvent event)
  {
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event  the event.
   */
  public void groupStarted(final ReportEvent event)
  {
  }

  /**
   * Receives notification that a group has finished.
   *
   * @param event  the event.
   */
  public void groupFinished(final ReportEvent event)
  {
  }

  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event  the event.
   */
  public void itemsAdvanced(final ReportEvent event)
  {
  }

  /**
   * Receives notification that a group of item bands is about to be processed.
   * <P>
   * The next events will be itemsAdvanced events until the itemsFinished event is raised.
   *
   * @param event The event.
   */
  public void itemsStarted(final ReportEvent event)
  {
  }

  /**
   * Receives notification that a group of item bands has been completed.
   * <P>
   * The itemBand is finished, the report starts to close open groups.
   *
   * @param event The event.
   */
  public void itemsFinished(final ReportEvent event)
  {
  }

  /**
   * Receives notification that report generation has completed, the report footer was printed,
   * no more output is done. This is a helper event to shut down the output service.
   *
   * @param event The event.
   */
  public void reportDone(final ReportEvent event)
  {
    // does nothing...
  }

  /**
   * Clones the function.
   * <P>
   * Be aware, this does not create a deep copy. If you have complex
   * strucures contained in objects, you have to override this function.
   *
   * @return a clone of this function.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final AbstractFunction function = (AbstractFunction) super.clone();
    return function;
  }

}
