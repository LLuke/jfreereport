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

import java.io.Serializable;

import org.jfree.base.config.HierarchicalConfiguration;
import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportDataFactory;
import org.jfree.report.util.ReportParameters;

/**
 * Creation-Date: 22.02.2006, 12:47:53
 *
 * @author Thomas Morgner
 */
public class ReportJob implements Serializable, Cloneable
{
  private JFreeReport report;
  private ReportDataFactory dataFactory;
  private ReportParameters parameters;
  private ModifiableConfiguration configuration;

  public ReportJob(final JFreeReport report)
  {
    this.report = report;
    final ReportDataFactory dataFactory = report.getDataFactory();
    if (dataFactory != null)
    {
      this.dataFactory = dataFactory.derive();
    }
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

  public ReportDataFactory getDataFactory()
  {
    return dataFactory;
  }

  public void setDataFactory(final ReportDataFactory dataFactory)
  {
    this.dataFactory = dataFactory;
  }

  public Object clone() throws CloneNotSupportedException
  {
    ReportJob job = (ReportJob) super.clone();
    if (dataFactory != null)
    {
      job.dataFactory = dataFactory.derive();
    }
    job.parameters = (ReportParameters) parameters.clone();
    job.configuration = (ModifiableConfiguration) configuration.clone();
    return job;
  }

  public ReportJob derive()
  {
    try
    {
      return (ReportJob) clone();
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException
          ("A report job should always be cloneable.");
    }
  }

  public synchronized void close()
  {
    if (dataFactory != null)
    {
      dataFactory.close();
    }
  }
}
