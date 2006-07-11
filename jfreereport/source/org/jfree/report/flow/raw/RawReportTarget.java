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
 * RawReportTarget.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: RawReportTarget.java,v 1.2 2006/04/21 17:31:23 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.flow.raw;

import org.jfree.report.DataFlags;
import org.jfree.report.DataSourceException;
import org.jfree.report.JFreeReport;
import org.jfree.report.flow.ReportJob;
import org.jfree.report.flow.ReportTarget;
import org.jfree.report.function.ExpressionRuntime;
import org.jfree.report.structure.ContentElement;
import org.jfree.report.structure.Element;
import org.jfree.report.structure.Node;

/**
 * The Raw report processor defines the base for all non-layouting output
 * methods. As no layouting is involved, this output method is lightning
 * fast.
 *
 * @author Thomas Morgner
 */
public class RawReportTarget implements ReportTarget
{
  private ReportJob reportJob;

  public RawReportTarget(ReportJob job)
  {
    this.reportJob = job;
  }

  public ReportJob getReportJob()
  {
    return reportJob;
  }

  public void startReport(JFreeReport report) throws DataSourceException
  {
  }

  public void endReport(JFreeReport report) throws DataSourceException
  {
  }

  public void processNode(final Node node, final ExpressionRuntime runtime)
          throws DataSourceException
  {
  }

  public void startElement(final Element node,
                           final ExpressionRuntime runtime)
          throws DataSourceException
  {
  }

  public void processContentElement(ContentElement node,
                                    DataFlags value,
                                    ExpressionRuntime runtime)
          throws DataSourceException
  {
  }

  public void endElement(final Element node, final ExpressionRuntime runtime)
          throws DataSourceException
  {
  }

  public void commit()
  {

  }
}
