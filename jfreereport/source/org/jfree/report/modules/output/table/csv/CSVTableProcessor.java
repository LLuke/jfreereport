/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: CSVTableProcessor.java,v 1.9 2005/01/25 00:13:08 taqua Exp $
 *
 * Changes
 * -------
 * 21-Jan-2003 : Initial version;
 * 24-Feb-2003 : Fixed Checkstyle issues (DG);
 *
 */

package org.jfree.report.modules.output.table.csv;

import java.io.Writer;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.modules.output.csv.CSVProcessor;
import org.jfree.report.modules.output.meta.MetaBandProducer;
import org.jfree.report.modules.output.table.base.LayoutCreator;
import org.jfree.report.modules.output.table.base.TableCreator;
import org.jfree.report.modules.output.table.base.TableProcessor;

/**
 * The <code>CSVTableProcessor</code> coordinates the output for the layouted CSV output.
 * The bands are layouted and the layouted contents are printed into the csv-file.
 * <p/>
 * For data oriented csv output try the <code>org.jfree.report.targets.csv.CSVProcessor</code>.
 * The used writer is not closed after the processing, the caller is responsible to close
 * the writer.
 * <p/>
 * The CellSeparator can be used to alter the separator character, f.i. to create
 * tab-separated files.
 *
 * @author Thomas Morgner
 * @see CSVProcessor
 */
public class CSVTableProcessor extends TableProcessor
{
  /**
   * The writer used for the output.
   */
  private Writer writer;

  /**
   * the configuration prefix for the csv table producer.
   */
  public static final String CONFIGURATION_PREFIX =
          "org.jfree.report.modules.output.table.csv";

  /**
   * the key for the separator string.
   */
  public static final String SEPARATOR_KEY = "Separator";

  /**
   * The default value for the separator string (",").
   */
  public static final String SEPARATOR_DEFAULT = ",";

  /**
   * Creates a new CSV table processor for the given report and uses the given separator
   * string.
   *
   * @param report the report to process.
   * @throws ReportProcessingException if the report initialization failed
   * @throws NullPointerException      if the given separator is <code>null</code>.
   */
  public CSVTableProcessor (final JFreeReport report)
          throws ReportProcessingException
  {
    super(report);
  }

  /**
   * Gets the separator string for the generated output.
   *
   * @return The defined separator for the output.
   */
  public String getSeparator ()
  {
    return getReport().getReportConfiguration().getConfigProperty
            (getReportConfigurationPrefix() + "." + SEPARATOR_KEY, ",");
  }

  /**
   * Defines the separator string for the generated output.
   *
   * @param separator the defined separator for the output (<code>null</code> not
   *                  permitted).
   * @throws NullPointerException if the given separator is <code>null</code>.
   */
  public void setSeparator (final String separator)
  {
    if (separator == null)
    {
      throw new NullPointerException("Separator is null");
    }
    getReport().getReportConfiguration().setConfigProperty
            (getReportConfigurationPrefix() + "." + SEPARATOR_KEY, separator);
  }

  /**
   * Gets the writer, which should be used to output the generated content.
   *
   * @return the writer.
   */
  public Writer getWriter ()
  {
    return writer;
  }

  /**
   * Sets the writer, that should be used to write the generated content.
   *
   * @param writer the writer.
   */
  public void setWriter (final Writer writer)
  {
    this.writer = writer;
  }

  /**
   * Creates the CSVTableProducer. The TableProducer is responsible to create the table.
   *
   * @return the created table producer, never null.
   *
   * @throws IllegalStateException if the writer is not defined or no layout creator was
   *                               found.
   */
  protected TableCreator createContentCreator ()
  {
    if (writer == null)
    {
      throw new IllegalStateException("There is no writer defined for the export.");
    }
    final LayoutCreator lc = getLayoutCreator();
    return new CSVContentCreator(lc.getSheetLayoutCollection(), getWriter());
  }

  /**
   * Gets the report configuration prefix for that processor. This prefix defines how to
   * map the property names into the global report configuration.
   *
   * @return the report configuration prefix.
   */
  protected String getReportConfigurationPrefix ()
  {
    return CONFIGURATION_PREFIX;
  }

  protected MetaBandProducer createMetaBandProducer ()
  {
    return new CSVMetaBandProducer(new DefaultLayoutSupport());
  }
}
