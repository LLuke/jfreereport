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
 * FlowControler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.flow;

import org.jfree.report.DataSourceException;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.data.GlobalMasterRow;
import org.jfree.report.structure.Element;
import org.jfree.report.structure.SubReport;

/**
 * Creation-Date: 19.02.2006, 18:14:36
 *
 * @author Thomas Morgner
 */
public interface FlowControler
{
  public FlowControler performOperation(FlowControlOperation operation)
          throws DataSourceException;

  public GlobalMasterRow getMasterRow();

  public boolean isAdvanceRequested();

  public FlowControler performQuery(final JFreeReport report)
          throws ReportDataFactoryException, DataSourceException;

  public FlowControler performQuery(final SubReport report)
          throws ReportDataFactoryException, DataSourceException;

  public FlowControler activateExpressions(final Element element)
          throws DataSourceException;

  public FlowControler deactivateExpressions() throws DataSourceException;

  public ReportJob getReportJob();

  public FlowControler performReturnFromQuery() throws DataSourceException;
}
