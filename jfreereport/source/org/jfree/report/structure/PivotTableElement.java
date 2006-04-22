/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * PivotTableElement.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PivotTableElement.java,v 1.2 2006/04/21 17:31:23 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.report.structure;

import org.jfree.report.flow.ReportProcessor;
import org.jfree.report.flow.FlowControler;
import org.jfree.report.flow.LayoutControler;

public class PivotTableElement extends ReportComponent
{


  public PivotTableElement ()
  {
    setType("pivot-table");
  }

  public ReportComponentState advance (ReportComponentState state,
                                       ReportProcessor processor,
                                       FlowControler flowControler,
                                       LayoutControler layoutControler)
  {
    // todo implement me
    return null;
  }

  /**
   * If this is a finish state, finish it and then return true. If this is not a finish
   * state, return false and do nothing at all.
   *
   * @param state
   * @return true or false, depending on whether the processing has been finished.
   */
  public boolean finishState (ReportComponentState state)
  {
    // todo implement me
    return false;
  }

  public ReportComponentState createInitialState
          (ReportProcessor processor,
           FlowControler flowControler,
           LayoutControler layoutControler)
  {
    return null;
  }
}
