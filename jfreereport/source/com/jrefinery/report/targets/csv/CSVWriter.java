/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ----------------
 * CSVWriter.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CSVWriter.java,v 1.5 2003/02/09 18:43:05 taqua Exp $
 *
 * Changes
 * -------
 * 07-Jan-2003 : Initial Version
 * 09-Feb-2003 : Documentation
 */
package com.jrefinery.report.targets.csv;

import com.jrefinery.report.DataRow;
import com.jrefinery.report.Group;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.function.AbstractFunction;
import com.jrefinery.report.function.FunctionProcessingException;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * The CSV Writer is the content creation function used to create the CSV content.
 * This implementation does no layouting, the DataRow's raw data is written to the
 * supplied writer.
 */
public class CSVWriter extends AbstractFunction
{
  /**
   * The CSVRow is used to collect the data of a single row of data.
   */
  private static class CSVRow
  {
    private ArrayList data;
    private CSVQuoter quoter;
    private String lineSeparator;

    /**
     * Creates a new CSVQuoter. The Quoter uses the system's default
     * line separator.
     *
     * @param quoter
     */
    public CSVRow(CSVQuoter quoter)
    {
      data = new ArrayList();
      this.quoter = quoter;
      lineSeparator = System.getProperty("line.separator", "\n");
    }

    /**
     * appends the given integer value as java.lang.Integer to this row.
     *
     * @param value the appended int value
     */
    public void append (int value)
    {
      data.add (new Integer (value));
    }

    /**
     * appends the given Object to this row.
     *
     * @param o the appended value
     */
    public void append (Object o)
    {
      data.add (o);
    }

    /**
     * Writes the contents of the collected row, separated by colon.
     *
     * @param w the writer
     * @throws IOException if an I/O error occured.
     */
    public void write (Writer w) throws IOException
    {
      Iterator it = data.iterator();
      while (it.hasNext())
      {
        w.write(quoter.doQuoting(String.valueOf (it.next())));
        if (it.hasNext())
          w.write(",");
      }
      w.write(lineSeparator);
    }
  }

  /** the writer used to output the generated data */
  private Writer w;
  /** the functions dependency level, -1 by default */
  private int depLevel;
  /** the CSVQuoter used to encode the column values */
  private CSVQuoter quoter;
  /** a flag indicating whether to writer data row names as column header */
  private boolean writeDataRowNames;

  /**
   * DefaulConstructor. Creates a CSVWriter with a dependency level of -1 and
   * a default CSVQuoter.
   */
  public CSVWriter()
  {
    setDependencyLevel(-1);
    quoter = new CSVQuoter();
  }

  /**
   * Returns whether to print dataRow column names as header.
   *
   * @return true, if column names are printed, false otherwise.
   */
  public boolean isWriteDataRowNames()
  {
    return writeDataRowNames;
  }

  /**
   * Defines, whether to print column names in the first row.
   *
   * @param writeDataRowNames true, if column names are printed, false otherwise
   */
  public void setWriteDataRowNames(boolean writeDataRowNames)
  {
    this.writeDataRowNames = writeDataRowNames;
  }

  /**
   * Returns the writer used to output the generated data.
   *
   * @return the writer
   */
  public Writer getWriter()
  {
    return w;
  }

  /**
   * Defines the writer which should be used to output the generated data.
   *
   * @param w the writer
   */
  public void setWriter(Writer w)
  {
    this.w = w;
  }

  /**
   * Defines the sepator, which is used to separate columns in a row.
   *
   * @param separator the separator string, never null.
   * @throws NullPointerException if the separator is null.
   * @throws IllegalArgumentException if the separator is an empty string.
   */
  public void setSeparator(String separator)
  {
    if (separator == null)
      throw new NullPointerException();
    if (separator.length() == 0)
      throw new IllegalArgumentException("Separator must not be an empty string");
    this.quoter.setSeparator(separator);
  }

  /**
   * Gets the separator which is used to separate columns in a row.
   *
   * @return the separator, never null.
   */
  public String getSeparator()
  {
    return quoter.getSeparator();
  }

  /**
   * Writes the contents of the dataRow into the CSVRow.
   *
   * @param dr the dataRow which should be written
   * @param row the CSVRow used to collect the RowData.
   */
  private void writeDataRow (DataRow dr,CSVRow row)
  {
    for (int i = 0; i < dr.getColumnCount(); i++)
    {
      Object o = dr.get(i);
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
   * @param dr the dataRow which should be written
   * @param row the CSVRow used to collect the RowData.
   */
  private void writeDataRowNames (DataRow dr,CSVRow row)
  {
    for (int i = 0; i < dr.getColumnCount(); i++)
    {
      row.append(dr.getColumnName(i));
    }
  }

  /**
   * Writes the ReportHeader and (if defined) the dataRow names.
   *
   * @param event  the event.
   * @throws FunctionProcessingException if the writing failed.
   */
  public void reportStarted(ReportEvent event)
  {
    try
    {
      if (isWriteDataRowNames())
      {
        CSVRow names = new CSVRow(quoter);
        names.append ("report.currentgroup");
        names.append ("report.eventtype");
        writeDataRowNames(event.getDataRow(), names);
        names.write(getWriter());
      }

      CSVRow row = new CSVRow(quoter);
      row.append(-1);
      row.append("reportheader");
      writeDataRow(event.getDataRow(), row);
      row.write(getWriter());
    }
    catch (IOException ioe)
    {
      throw new FunctionProcessingException ("Error writing the current datarow", ioe);
    }
  }

  /**
   * Writes the ReportFooter.
   *
   * @param event  the event.
   * @throws FunctionProcessingException if the writing failed.
   */
  public void reportFinished(ReportEvent event)
  {
    try
    {
      CSVRow row = new CSVRow(quoter);
      row.append(-1);
      row.append("reportfooter");
      writeDataRow(event.getDataRow(), row);
      row.write(getWriter());
    }
    catch (IOException ioe)
    {
      throw new FunctionProcessingException ("Error writing the current datarow", ioe);
    }
  }

  /**
   * Writes the GroupHeader of the current group.
   *
   * @param event  the event.
   * @throws FunctionProcessingException if the writing failed.
   */
  public void groupStarted(ReportEvent event)
  {
    try
    {
      int currentIndex = event.getState().getCurrentGroupIndex();

      CSVRow row = new CSVRow(quoter);
      row.append(currentIndex);

      Group g = event.getReport().getGroup(currentIndex);
      String bandInfo = "groupheader name=\"" + g.getName()+ "\"";
      row.append(bandInfo);
      writeDataRow(event.getDataRow(), row);
      row.write(getWriter());
    }
    catch (IOException ioe)
    {
      throw new FunctionProcessingException ("Error writing the current datarow", ioe);
    }
  }

  /**
   * Writes the GroupFooter of the active group.
   *
   * @param event  the event.
   * @throws FunctionProcessingException if the writing failed.
   */
  public void groupFinished(ReportEvent event)
  {
    try
    {
      int currentIndex = event.getState().getCurrentGroupIndex();

      CSVRow row = new CSVRow(quoter);
      row.append(currentIndex);

      Group g = event.getReport().getGroup(currentIndex);
      String bandInfo = "groupfooter name=\"" + g.getName()+ "\"";
      row.append(bandInfo);
      writeDataRow(event.getDataRow(), row);
      row.write(getWriter());
    }
    catch (IOException ioe)
    {
      throw new FunctionProcessingException ("Error writing the current datarow", ioe);
    }
  }

  /**
   * Writes the current ItemBand.
   *
   * @param event  the event.
   * @throws FunctionProcessingException if the writing failed.
   */
  public void itemsAdvanced(ReportEvent event)
  {
    try
    {
      CSVRow row = new CSVRow(quoter);
      row.append(event.getState().getCurrentGroupIndex());
      row.append("itemband");
      writeDataRow(event.getDataRow(), row);
      row.write(getWriter());
    }
    catch (IOException ioe)
    {
      throw new FunctionProcessingException ("Error writing the current datarow", ioe);
    }
  }

  /**
   * Return a selfreference of this CSVWriter. This selfreference is used to
   * confiugre the output process.
   *
   * @return this CSVWriter.
   */
  public Object getValue()
  {
    return this;
  }

  /**
   * The dependency level defines the level of execution for this function. Higher dependency functions
   * are executed before lower dependency functions. For ordinary functions and expressions,
   * the range for dependencies is defined to start from 0 (lowest dependency possible)
   * to 2^31 (upper limit of int).
   * <p>
   * PageLayouter functions override the default behaviour an place them self at depency level -1,
   * an so before any userdefined function.
   *
   * @return the level.
   */
  public int getDependencyLevel()
  {
    return depLevel;
  }

  /**
   * Overrides the depency level. Should be lower than any other function depency.
   * @param deplevel the new depency level.
   */
  public void setDependencyLevel(int deplevel)
  {
    this.depLevel = deplevel;
  }

}
