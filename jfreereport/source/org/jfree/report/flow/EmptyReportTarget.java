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

package org.jfree.report.flow;

import org.jfree.report.structure.Node;
import org.jfree.report.structure.ContentElement;
import org.jfree.report.structure.Element;
import org.jfree.report.expressions.ExpressionRuntime;
import org.jfree.report.DataSourceException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.DataFlags;
import org.jfree.report.JFreeReport;

/**
 * This target does nothing.
 *
 * @author Thomas Morgner
 */
public class EmptyReportTarget implements ReportTarget
{
  private ReportJob job;
  private String reportDescriptor;

  public EmptyReportTarget(final ReportJob job, final String reportDescriptor)
  {
    this.job = job;
    this.reportDescriptor = reportDescriptor;
  }

  public void processNode(Node node, ExpressionRuntime runtime)
      throws DataSourceException, ReportProcessingException
  {

  }

  public void processContentElement(ContentElement node,
                                    DataFlags value,
                                    ExpressionRuntime runtime)
      throws DataSourceException, ReportProcessingException
  {

  }

  public void startElement(Element node, ExpressionRuntime runtime)
      throws DataSourceException, ReportProcessingException
  {

  }

  public void endElement(Element node, ExpressionRuntime runtime)
      throws DataSourceException, ReportProcessingException
  {

  }

  public void startReport(JFreeReport report)
      throws DataSourceException, ReportProcessingException
  {

  }

  public void endReport(JFreeReport report)
      throws DataSourceException, ReportProcessingException
  {

  }

  public ReportJob getReportJob()
  {
    return job;
  }

  public String getExportDescriptor()
  {
    return reportDescriptor;
  }

  public void commit() throws ReportProcessingException
  {

  }
}
