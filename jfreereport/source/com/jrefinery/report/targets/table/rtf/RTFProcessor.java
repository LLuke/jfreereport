/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * -----------------
 * RTFProcessor.java
 * -----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: RTFProcessor.java,v 1.4 2003/02/25 11:57:58 taqua Exp $
 *
 * Changes
 * -------
 * 01-Feb-2003 : Initial version
 */
package com.jrefinery.report.targets.table.rtf;

import java.io.OutputStream;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.targets.table.TableProcessor;
import com.jrefinery.report.targets.table.TableProducer;
import com.jrefinery.report.util.NullOutputStream;

/**
 * The ExcelProcessor coordinates the output process for generating
 * RTF files using the iText library.
 * 
 * @author Thomas Morgner
 */
public class RTFProcessor extends TableProcessor
{
  /** the target output stream for writing the generated content. */
  private OutputStream outputStream;

  /**
   * Creates a new RTF processor for the Report.
   *
   * @param report the report that should be written as RTF.
   * @throws ReportProcessingException if the report initialization failed
   * @throws FunctionInitializeException if the table writer initialization failed.
   */
  public RTFProcessor(JFreeReport report)
      throws ReportProcessingException, FunctionInitializeException
  {
    super(report);
  }

  /**
   * Gets the output stream, that should be used to write the generated content.
   *
   * @return the output stream.
   */
  public OutputStream getOutputStream()
  {
    return outputStream;
  }

  /**
   * Sets the output stream, that should be used to write the generated content.
   *
   * @param outputStream the output stream.
   */
  public void setOutputStream(OutputStream outputStream)
  {
    this.outputStream = outputStream;
  }

  /**
   * Creates the RTFProducer. The TableProducer is responsible to create the table.
   *
   * @param dummy true, if dummy mode is enabled, and no writing should be done, false otherwise.
   * @return the created table producer, never null.
   */
  public TableProducer createProducer(boolean dummy)
  {
    RTFProducer prod;
    if (dummy == true)
    {
      prod = new RTFProducer(new NullOutputStream(),
                              isStrictLayout());
      prod.setDummy(true);
    }
    else
    {
      prod = new RTFProducer(getOutputStream(), isStrictLayout());
      prod.setDummy(false);
    }

    return prod;
  }
}
