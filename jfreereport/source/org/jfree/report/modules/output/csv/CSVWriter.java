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
 * --------------
 * CSVWriter.java
 * --------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: CSVWriter.java,v 1.7 2005/09/07 14:25:10 taqua Exp $
 *
 * Changes
 * -------
 * 07-Jan-2003 : Initial Version
 * 09-Feb-2003 : Documentation
 * 24-Feb-2003 : Fixed Checkstyle issues (DG);
 *
 */

package org.jfree.report.modules.output.csv;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

import org.jfree.report.DataRow;
import org.jfree.report.Group;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.AbstractFunction;
import org.jfree.report.function.FunctionProcessingException;

/**
 * The CSV Writer is the content creation function used to create the CSV content. This
 * implementation does no layouting, the DataRow's raw data is written to the supplied
 * writer.
 *
 * @author Thomas Morgner.
 */
public class CSVWriter extends AbstractFunction
{
  /**
   * The CSVRow is used to collect the data of a single row of data.
   */
  private static class CSVRow
  {
    /**
     * The data.
     */
    private final ArrayList data;

    /**
     * A quoter utility object.
     */
    private final CSVQuoter quoter;

    /**
     * The line separator.
     */
    private final String lineSeparator;

    /**
     * Creates a new CSVQuoter. The Quoter uses the system's default line separator.
     *
     * @param quoter a utility class for quoting CSV strings.
     */
    public CSVRow (final CSVQuoter quoter)
    {
      data = new ArrayList();
      this.quoter = quoter;
      lineSeparator =
              JFreeReportBoot.getInstance().getGlobalConfig().getConfigProperty
                      ("line.separator", "\n");
    }

    /**
     * appends the given integer value as java.lang.Integer to this row.
     *
     * @param value the appended int value
     */
    public void append (final int value)
    {
      data.add(new Integer(value));
    }

    /**
     * appends the given Object to this row.
     *
     * @param o the appended value
     */
    public void append (final Object o)
    {
      data.add(o);
    }

    /**
     * Writes the contents of the collected row, separated by colon.
     *
     * @param w the writer.
     * @throws IOException if an I/O error occurred.
     */
    public void write (final Writer w)
            throws IOException
    {
      final Iterator it = data.iterator();
      while (it.hasNext())
      {
        w.write(quoter.doQuoting(String.valueOf(it.next())));
        if (it.hasNext())
        {
          w.write(",");
        }
      }
      w.write(lineSeparator);
    }
  }

  /**
   * the writer used to output the generated data.
   */
  private Writer w;

  /**
   * the functions dependency level, -1 by default.
   */
  private int depLevel;

  /**
   * the CSVQuoter used to encode the column values.
   */
  private final CSVQuoter quoter;

  /**
   * a flag indicating whether to writer data row names as column header.
   */
  private boolean writeDataRowNames;
  private boolean writeStateColumns;
  private boolean enableReportHeader;
  private boolean enableReportFooter;
  private boolean enableGroupHeader;
  private boolean enableGroupFooter;
  private boolean enableItemband;

  /**
   * DefaulConstructor. Creates a CSVWriter with a dependency level of -1 and a default
   * CSVQuoter.
   */
  public CSVWriter ()
  {
    setDependencyLevel(-1);
    quoter = new CSVQuoter();
  }

  /**
   * Returns whether to print dataRow column names as header.
   *
   * @return true, if column names are printed, false otherwise.
   */
  public boolean isWriteDataRowNames ()
  {
    return writeDataRowNames;
  }

  /**
   * Defines, whether to print column names in the first row.
   *
   * @param writeDataRowNames true, if column names are printed, false otherwise
   */
  public void setWriteDataRowNames (final boolean writeDataRowNames)
  {
    this.writeDataRowNames = writeDataRowNames;
  }

  public boolean isWriteStateColumns ()
  {
    return writeStateColumns;
  }

  public void setWriteStateColumns (boolean writeStateColumns)
  {
    this.writeStateColumns = writeStateColumns;
  }

  public boolean isEnableGroupFooter ()
  {
    return enableGroupFooter;
  }

  public void setEnableGroupFooter (boolean enableGroupFooter)
  {
    this.enableGroupFooter = enableGroupFooter;
  }

  public boolean isEnableGroupHeader ()
  {
    return enableGroupHeader;
  }

  public void setEnableGroupHeader (boolean enableGroupHeader)
  {
    this.enableGroupHeader = enableGroupHeader;
  }

  public boolean isEnableItemband ()
  {
    return enableItemband;
  }

  public void setEnableItemband (boolean enableItemband)
  {
    this.enableItemband = enableItemband;
  }

  public boolean isEnableReportFooter ()
  {
    return enableReportFooter;
  }

  public void setEnableReportFooter (boolean enableReportFooter)
  {
    this.enableReportFooter = enableReportFooter;
  }

  public boolean isEnableReportHeader ()
  {
    return enableReportHeader;
  }

  public void setEnableReportHeader (boolean enableReportHeader)
  {
    this.enableReportHeader = enableReportHeader;
  }

  /**
   * Returns the writer used to output the generated data.
   *
   * @return the writer
   */
  public Writer getWriter ()
  {
    return w;
  }

  /**
   * Defines the writer which should be used to output the generated data.
   *
   * @param w the writer
   */
  public void setWriter (final Writer w)
  {
    this.w = w;
  }

  /**
   * Defines the separator, which is used to separate columns in a row.
   *
   * @param separator the separator string, never null.
   * @throws NullPointerException     if the separator is null.
   * @throws IllegalArgumentException if the separator is an empty string.
   */
  public void setSeparator (final String separator)
  {
    if (separator == null)
    {
      throw new NullPointerException();
    }
    if (separator.length() == 0)
    {
      throw new IllegalArgumentException("Separator must not be an empty string");
    }
    this.quoter.setSeparator(separator);
  }

  /**
   * Gets the separator which is used to separate columns in a row.
   *
   * @return the separator, never null.
   */
  public String getSeparator ()
  {
    return quoter.getSeparator();
  }

  /**
   * Writes the contents of the dataRow into the CSVRow.
   *
   * @param dr  the dataRow which should be written
   * @param row the CSVRow used to collect the RowData.
   */
  private void writeDataRow (final DataRow dr, final CSVRow row)
  {
    for (int i = 0; i < dr.getColumnCount(); i++)
    {
      final Object o = dr.get(i);
      if (o == null)
      {
        row.append(o);
      }
      else if (o != this)
      {
        row.append(o);
      }
    }
  }

  /**
   * Writes the names of the columns of the dataRow into the CSVRow.
   *
   * @param dr  the dataRow which should be written
   * @param row the CSVRow used to collect the RowData.
   */
  private void writeDataRowNames (final DataRow dr, final CSVRow row)
  {
    for (int i = 0; i < dr.getColumnCount(); i++)
    {
      row.append(dr.getColumnName(i));
    }
  }

  /**
   * Writes the ReportHeader and (if defined) the dataRow names.
   *
   * @param event the event.
   */
  public void reportStarted (final ReportEvent event)
  {
    try
    {
      if (isWriteDataRowNames())
      {
        final CSVRow names = new CSVRow(quoter);
        names.append("report.currentgroup");
        names.append("report.eventtype");
        writeDataRowNames(event.getDataRow(), names);
        names.write(getWriter());
      }

      if (isEnableReportHeader() == false)
      {
        return;
      }

      final CSVRow row = new CSVRow(quoter);
      if (isWriteStateColumns())
      {
        row.append(-1);
        row.append("reportheader");
      }
      writeDataRow(event.getDataRow(), row);
      row.write(getWriter());
    }
    catch (IOException ioe)
    {
      throw new FunctionProcessingException("Error writing the current datarow", ioe);
    }
  }

  /**
   * Writes the ReportFooter.
   *
   * @param event the event.
   */
  public void reportFinished (final ReportEvent event)
  {
    if (isEnableReportFooter() == false)
    {
      return;
    }
    try
    {
      final CSVRow row = new CSVRow(quoter);
      if (isWriteStateColumns())
      {
        row.append(-1);
        row.append("reportfooter");
      }
      writeDataRow(event.getDataRow(), row);
      row.write(getWriter());
    }
    catch (IOException ioe)
    {
      throw new FunctionProcessingException("Error writing the current datarow", ioe);
    }
  }

  /**
   * Writes the GroupHeader of the current group.
   *
   * @param event the event.
   */
  public void groupStarted (final ReportEvent event)
  {
    if (isEnableGroupHeader() == false)
    {
      return;
    }

    try
    {
      final int currentIndex = event.getState().getCurrentGroupIndex();

      final CSVRow row = new CSVRow(quoter);
      if (isWriteStateColumns())
      {
        row.append(currentIndex);
        final Group g = event.getReport().getGroup(currentIndex);
        final String bandInfo = "groupheader name=\"" + g.getName() + "\"";
        row.append(bandInfo);
      }
      writeDataRow(event.getDataRow(), row);
      row.write(getWriter());
    }
    catch (IOException ioe)
    {
      throw new FunctionProcessingException("Error writing the current datarow", ioe);
    }
  }

  /**
   * Writes the GroupFooter of the active group.
   *
   * @param event the event.
   */
  public void groupFinished (final ReportEvent event)
  {
    if (isEnableGroupFooter() == false)
    {
      return;
    }

    try
    {
      final int currentIndex = event.getState().getCurrentGroupIndex();

      final CSVRow row = new CSVRow(quoter);
      if (isWriteStateColumns())
      {
        row.append(currentIndex);
        final Group g = event.getReport().getGroup(currentIndex);
        final String bandInfo = "groupfooter name=\"" + g.getName() + "\"";
        row.append(bandInfo);
      }
      writeDataRow(event.getDataRow(), row);
      row.write(getWriter());
    }
    catch (IOException ioe)
    {
      throw new FunctionProcessingException("Error writing the current datarow", ioe);
    }
  }

  /**
   * Writes the current ItemBand.
   *
   * @param event the event.
   */
  public void itemsAdvanced (final ReportEvent event)
  {
    if (isEnableItemband() == false)
    {
      return;
    }

    try
    {
      final CSVRow row = new CSVRow(quoter);
      if (isWriteStateColumns())
      {
        row.append(event.getState().getCurrentGroupIndex());
        row.append("itemband");
      }
      writeDataRow(event.getDataRow(), row);
      row.write(getWriter());
    }
    catch (IOException ioe)
    {
      throw new FunctionProcessingException("Error writing the current datarow", ioe);
    }
  }

  /**
   * Return a selfreference of this CSVWriter. This selfreference is used to confiugre the
   * output process.
   *
   * @return this CSVWriter.
   */
  public Object getValue ()
  {
    return this;
  }

  /**
   * The dependency level defines the level of execution for this function. Higher
   * dependency functions are executed before lower dependency functions. For ordinary
   * functions and expressions, the range for dependencies is defined to start from 0
   * (lowest dependency possible) to 2^31 (upper limit of int).
   * <p/>
   * PageLayouter functions override the default behaviour an place them self at depency
   * level -1, an so before any userdefined function.
   *
   * @return the level.
   */
  public int getDependencyLevel ()
  {
    return depLevel;
  }

  /**
   * Overrides the depency level. Should be lower than any other function depency.
   *
   * @param deplevel the new depency level.
   */
  public void setDependencyLevel (final int deplevel)
  {
    this.depLevel = deplevel;
  }

}
