/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ---------------
 * StartState.java
 * ---------------
 * (C)opyright 2000-2003, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: StartState.java,v 1.8 2005/01/28 19:27:00 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Updated Javadocs (DG);
 *
 */

package org.jfree.report.states;

import java.awt.print.PageFormat;
import java.util.Date;
import java.util.Iterator;

import org.jfree.report.JFreeReport;
import org.jfree.report.PageDefinition;
import org.jfree.report.event.ReportEvent;

/**
 * The first state in the JFreeReport state transition diagram.
 *
 * @author David Gilbert
 */
public final class StartState extends ReportState
{
  /**
   * Creates a start state for a given report.
   * <p/>
   * The report is cloned, and an internal reference to the clone is maintained.  Changing
   * the original report after report processing has begun will have no effect on the
   * report output for this run.
   * <p/>
   * This is the only state constructor to create a state without cloning another.
   *
   * @param report the report.
   * @throws CloneNotSupportedException if the initial cloning of the report definition
   *                                    fails.
   */
  public StartState (final JFreeReport report)
          throws CloneNotSupportedException
  {
    super(report);
    final Iterator it = getLevels();
    if (it.hasNext())
    {
      final Integer i = (Integer) getLevels().next();
      getFunctions().setLevel(i.intValue());
    }
    else
    {
      getFunctions().setLevel(0);
    }
  }

  /**
   * Creates a new <code>START</code> state.
   *
   * @param fstate the finish state.
   * @param level  the level.
   */
  public StartState (final FinishState fstate, final int level)
  {
    super(fstate, true);
    getFunctions().setLevel(level);
  }

  /**
   * Advances from the '<code>START</code>' state to the '<code>PRE-GROUP-HEADER</code>'
   * state (the only transition that is possible from this state).
   * <p/>
   * Initialises the 'report.date' property, and fires a 'report-started' event.
   *
   * @return the next state (<code>PRE-GROUP-HEADER</code>').
   */
  public ReportState advance ()
  {
    setCurrentPage(0);

    // a PropertyHandler should set the properties.
    setProperty(JFreeReport.REPORT_DATE_PROPERTY, new Date());

    // the pageformat is added to the report properties, PageFormat is not serializable,
    // so a repaginated report is no longer serializable.
    //
    // The pageformat will cause trouble in later versions, when printing over
    // multiple pages gets implemented. This property will be replaced by a more
    // suitable alternative.
    final PageDefinition p = getReport().getPageDefinition();
    final PageFormat pf = p.getPageFormat(0);
    setProperty(JFreeReport.REPORT_PAGEFORMAT_PROPERTY, pf);

    fireReportInitializedEvent();
    return new PostReportInitializedState(this);
  }

  /**
   * Returns <code>true</code> because this *is* the start state.
   *
   * @return always true, as this state starts the report processing.
   */
  public boolean isStart ()
  {
    return true;
  }

  /**
   * Returns the corrected display item for this state. As the currentItem has not yet
   * advanced we perform a readAHead lookup when populating elements.
   *
   * @return true; Header related states preview the next itemband DataRow.
   */
  public boolean isPrefetchState ()
  {
    return true;
  }

  /**
   * Resets the state.
   */
  public void resetState ()
  {
    super.resetState();
  }

  public int getEventCode ()
  {
    return ReportEvent.REPORT_STARTED;
  }
}
