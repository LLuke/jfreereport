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
 * ReportJob.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ReportJob.java,v 1.1 2006/04/18 11:49:11 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.flow;

import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportDataFactory;
import org.jfree.report.util.ReportParameters;
import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.base.config.HierarchicalConfiguration;

/**
 * Creation-Date: 22.02.2006, 12:47:53
 *
 * @author Thomas Morgner
 */
public class ReportJob
{
  private JFreeReport report;
  private OutputProcessorMetaData metaData;
  private ReportDataFactory dataFactory;
  private ReportParameters parameters;
  private ModifiableConfiguration configuration;

  public ReportJob(final JFreeReport report)
  {
    this.report = report;
    this.dataFactory = report.getDataFactory();
    this.parameters = new ReportParameters(report.getInputParameters());
    this.configuration = new HierarchicalConfiguration(report.getConfiguration());
  }

  public ModifiableConfiguration getConfiguration()
  {
    return configuration;
  }

  public ReportParameters getParameters()
  {
    return parameters;
  }

  public JFreeReport getReport()
  {
    return report;
  }

  public OutputProcessorMetaData getMetaData()
  {
    return metaData;
  }

  public void setMetaData(final OutputProcessorMetaData metaData)
  {
    this.metaData = metaData;
  }

  public ReportDataFactory getDataFactory()
  {
    return dataFactory;
  }

  public void setDataFactory(final ReportDataFactory dataFactory)
  {
    this.dataFactory = dataFactory;
  }
}
