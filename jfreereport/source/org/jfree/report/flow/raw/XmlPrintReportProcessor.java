/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2000-2007, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * (C) Copyright 2000-2005, by Object Refinery Limited.
 * (C) Copyright 2005-2007, by Pentaho Corporation.
 */

package org.jfree.report.flow.raw;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.jfree.report.ReportProcessingException;
import org.jfree.report.flow.ReportJob;
import org.jfree.report.flow.ReportTarget;
import org.jfree.report.flow.SinglePassReportProcessor;
import org.jfree.xmlns.writer.XmlWriter;

/**
 * Todo: Document me!
 *
 * @author Thomas Morgner
 * @since 20.03.2007
 */
public class XmlPrintReportProcessor extends SinglePassReportProcessor
{
  private OutputStream outputStream;
  private String encoding;


  public XmlPrintReportProcessor(final OutputStream outputStream,
                                 final String encoding)
  {
    this.outputStream = outputStream;
    this.encoding = encoding;
  }

  protected ReportTarget createReportTarget(final ReportJob job)
      throws ReportProcessingException
  {
    try
    {
      final OutputStreamWriter osw =
          new OutputStreamWriter(outputStream, encoding);
      final XmlWriter writer = new XmlWriter(osw);
      writer.writeXmlDeclaration(encoding);
      return new XmlPrintReportTarget(job, writer);
    }
    catch (IOException e)
    {
      throw new ReportProcessingException("Failed to create writer", e);
    }
  }
}
