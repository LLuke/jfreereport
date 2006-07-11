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
 * StreamingReportProcessor.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: StreamingReportProcessor.java,v 1.3 2006/05/15 12:56:56 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.flow.streaming;

import org.jfree.layouting.output.streaming.StreamingOutputProcessor;
import org.jfree.layouting.StreamingLayoutProcess;
import org.jfree.layouting.DefaultStreamingLayoutProcess;
import org.jfree.report.flow.ReportTarget;
import org.jfree.report.flow.SinglePassReportProcessor;
import org.jfree.report.flow.LibLayoutReportTarget;
import org.jfree.report.flow.ReportJob;
import org.jfree.report.ReportProcessingException;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.ResourceKey;

/**
 * Creation-Date: 02.04.2006, 14:27:00
 *
 * @author Thomas Morgner
 */
public class StreamingReportProcessor extends SinglePassReportProcessor
{
  private StreamingOutputProcessor outputProcessor;

  public StreamingReportProcessor()
  {
  }

  public StreamingOutputProcessor getOutputProcessor()
  {
    return outputProcessor;
  }

  public void setOutputProcessor(final StreamingOutputProcessor outputProcessor)
  {
    this.outputProcessor = outputProcessor;
  }

  protected ReportTarget createReportTarget(final ReportJob job)
          throws ReportProcessingException
  {
    if (outputProcessor == null)
    {
      throw new IllegalStateException(
              "OutputProcessor is invalid.");
    }
    final StreamingLayoutProcess layoutProcess
            ;
    try
    {
      layoutProcess = new DefaultStreamingLayoutProcess(outputProcessor);
    }
    catch (Exception e)
    {
      throw new ReportProcessingException("Blah", e);
    }
    final ResourceManager resourceManager = job.getReport().getResourceManager();
    final ResourceKey resourceKey = job.getReport().getBaseResource();

    return new LibLayoutReportTarget
            (job, resourceKey, resourceManager, layoutProcess);
  }
}
