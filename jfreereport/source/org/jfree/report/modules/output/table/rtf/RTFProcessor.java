/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * $Id: RTFProcessor.java,v 1.5 2003/08/24 15:06:10 taqua Exp $
 *
 * Changes
 * -------
 * 01-Feb-2003 : Initial version
 */
package org.jfree.report.modules.output.table.rtf;

import java.io.OutputStream;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.function.FunctionInitializeException;
import org.jfree.report.modules.output.table.base.TableLayoutInfo;
import org.jfree.report.modules.output.table.base.TableProcessor;
import org.jfree.report.modules.output.table.base.TableProducer;

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
  public RTFProcessor(final JFreeReport report)
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
  public void setOutputStream(final OutputStream outputStream)
  {
    this.outputStream = outputStream;
  }

  /**
   * Creates a TableProducer. The TableProducer is responsible to create the table.
   *
   * @param gridLayoutBounds the grid layout that contain the bounds from the pagination run.
   * @return the created table producer, never null.
   */
  protected TableProducer createProducer(final TableLayoutInfo gridLayoutBounds)
  {
    return new RTFProducer((RTFLayoutInfo) gridLayoutBounds, getOutputStream());
  }

  /**
   * Creates a dummy TableProducer. The TableProducer is responsible to compute the layout.
   *
   * @return the created table producer, never null.
   */
  protected TableProducer createDummyProducer()
  {
    return new RTFProducer
        (new RTFLayoutInfo(false, getReport().getDefaultPageFormat()), isStrictLayout());
  }

  /**
   * Gets the report configuration prefix for that processor. This prefix defines
   * how to map the property names into the global report configuration.
   *
   * @return the report configuration prefix.
   */
  protected String getReportConfigurationPrefix()
  {
    return "org.jfree.report.targets.table.rtf.";
  }
}
