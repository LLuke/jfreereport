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
 * $Id: FlowController.java,v 1.4 2006/12/03 20:24:09 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.report.flow;

import org.jfree.report.DataSourceException;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.data.ExpressionSlot;
import org.jfree.report.data.GlobalMasterRow;
import org.jfree.report.data.PrecomputedValueRegistry;

/**
 * A flow-controller is an immutable object. Whenever an method, that may change
 * the internal state of the controller, is invoked, a new instance of the
 * controller is returned.
 *
 * @author Thomas Morgner
 */
public interface FlowController
{
  public FlowController performOperation(FlowControlOperation operation)
      throws DataSourceException;

  public GlobalMasterRow getMasterRow();

  public ReportContext getReportContext();

  public String getExportDescriptor();

  public boolean isAdvanceRequested();

  public FlowController performQuery(final String query)
      throws ReportDataFactoryException, DataSourceException;

  public FlowController performSubReportQuery(final String query,
                                              final ParameterMapping[] inputParameters,
                                              final ParameterMapping[] outputParameters)
      throws ReportDataFactoryException, DataSourceException;

  /**
   * Activates expressions that compute running values. This does not activate
   * precomputed expressions.
   *
   * @param element
   * @param expressions
   * @return
   * @throws DataSourceException
   */
  public FlowController activateExpressions(final ExpressionSlot[] expressions)
      throws DataSourceException;

  /**
   * Returns the current expression slots of all currently active expressions.
   *
   * @return
   * @throws DataSourceException
   */
  public ExpressionSlot[] getActiveExpressions () throws DataSourceException;

  public FlowController deactivateExpressions() throws DataSourceException;

  public ReportJob getReportJob();

  public FlowController performReturnFromQuery() throws DataSourceException;

  public FlowController createPrecomputeInstance () throws DataSourceException;

  public PrecomputedValueRegistry getPrecomputedValueRegistry();
}
