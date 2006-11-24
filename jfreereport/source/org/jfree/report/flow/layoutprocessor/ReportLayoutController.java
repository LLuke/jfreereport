/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.flow.layoutprocessor;

import org.jfree.report.flow.ReportTarget;
import org.jfree.report.flow.FlowController;
import org.jfree.report.DataSourceException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportDataFactoryException;

/**
 * Creation-Date: 24.11.2006, 13:56:50
 *
 * @author Thomas Morgner
 */
public class ReportLayoutController extends SectionLayoutController
{
  public ReportLayoutController()
  {
  }

  protected FlowController startData(final ReportTarget target,
                                     final FlowController fc)
      throws DataSourceException,
      ReportProcessingException, ReportDataFactoryException
  {
    final JFreeReport report = (JFreeReport) getNode();
    target.startReport(report);
    return fc.performQuery(report);
  }

  protected FlowController finishData(final ReportTarget target,
                                      final FlowController fc)
      throws DataSourceException, ReportProcessingException
  {
    final JFreeReport report = (JFreeReport) getNode();
    target.endReport(report);
    // do something fancy ... query the data, for instance.
    // this implies that we create a new global datarow.
    return fc.performReturnFromQuery();
  }
}
