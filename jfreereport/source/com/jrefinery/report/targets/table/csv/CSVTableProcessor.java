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
 * ----------------------
 * CSVTableProcessor.java
 * ----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CSVTableProcessor.java,v 1.7 2003/02/24 15:06:32 mungady Exp $
 *
 * Changes
 * -------
 * 21-Jan-2003 : Initial version;
 * 24-Feb-2003 : Fixed Checkstyle issues (DG);
 * 
 */

package com.jrefinery.report.targets.table.csv;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.targets.csv.CSVProcessor;
import com.jrefinery.report.targets.table.TableProcessor;
import com.jrefinery.report.targets.table.TableProducer;
import com.jrefinery.report.util.NullOutputStream;

import java.io.PrintWriter;
import java.io.Writer;

/**
 * The <code>CSVTableProcessor</code> coordinates the output for the layouted CSV output.
 * The bands are layouted and the layouted contents are printed into the csv-file.
 * <p>
 * For data oriented csv output try the <code>com.jrefinery.report.targets.csv.CSVProcessor</code>.
 * The used writer is not closed after the processing, the caller is responsible to
 * close the writer.
 * <p>
 * The CellSeparator can be used to alter the separator character, f.i. to
 * create tab-separated files.
 *
 * @see CSVProcessor
 * 
 * @author Thomas Morgner
 * 
 */
public class CSVTableProcessor extends TableProcessor
{
  /** The writer used for the output. */
  private Writer writer;
  
  /** The separator that should be used to separate cells. */
  private String separator;

  /**
   * Creates a new CSV table processor for the given report. 
   * <p>
   * The default separator is a comma (","), but this can be overridden in the 
   * report configuration (key: <code>com.jrefinery.report.targets.csv.separator</code>).
   *
   * @param report  the report to process.
   * 
   * @throws ReportProcessingException if the report initialization failed
   * @throws FunctionInitializeException if the table writer initialization failed.
   */
  public CSVTableProcessor(JFreeReport report)
      throws ReportProcessingException, FunctionInitializeException
  {
    this(report,
         report.getReportConfiguration().getConfigProperty(CSVProcessor.CSV_SEPARATOR, ","));
  }

  /**
   * Creates a new CSV table processor for the given report and uses the given
   * separator string.
   *
   * @param report  the report to process.
   * @param separator  the value separator (<code>null</code> not permitted).
   * 
   * @throws ReportProcessingException if the report initialization failed
   * @throws FunctionInitializeException if the table writer initialization failed.
   * @throws NullPointerException if the given separator is <code>null</code>.
   */
  public CSVTableProcessor(JFreeReport report, String separator) 
      throws ReportProcessingException, FunctionInitializeException
  {
    super(report);
    if (separator == null) 
    {
      throw new NullPointerException("Separator is null");
    }
    this.separator = separator;
  }

  /**
   * Gets the separator string for the generated output.
   *
   * @return The defined separator for the output.
   */
  public String getSeparator()
  {
    return separator;
  }

  /**
   * Defines the separator string for the generated output.
   *
   * @param separator the defined separator for the output (<code>null</code> not permitted).
   * 
   * @throws NullPointerException if the given separator is <code>null</code>.
   */
  public void setSeparator(String separator)
  {
    if (separator == null) 
    {
      throw new NullPointerException("Separator is null");
    }
    this.separator = separator;
  }

  /**
   * Gets the writer, which should be used to output the generated content.
   *
   * @return the writer.
   */
  public Writer getWriter()
  {
    return writer;
  }

  /**
   * Sets the writer, that should be used to write the generated content.
   *
   * @param writer the writer.
   */
  public void setWriter(Writer writer)
  {
    this.writer = writer;
  }

  /**
   * Creates the CSVTableProducer. The TableProducer is responsible to create the table.
   *
   * @param dummy true, if dummy mode is enabled, and no writing should be done, false otherwise.
   * 
   * @return the created table producer, never null.
   */
  public TableProducer createProducer(boolean dummy)
  {
    CSVTableProducer prod;
    if (dummy)
    {
      prod = new CSVTableProducer(new PrintWriter(new NullOutputStream()), 
                                  isStrictLayout(), separator);
    }
    else
    {
      prod = new CSVTableProducer(new PrintWriter(getWriter()), isStrictLayout(), separator);
    }
    prod.setDummy(dummy);
    return prod;
  }
}
