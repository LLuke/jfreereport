/**
 * Date: Jan 7, 2003
 * Time: 5:15:08 PM
 *
 * $Id: CSVWriter.java,v 1.2 2003/01/22 19:38:30 taqua Exp $
 */
package com.jrefinery.report.targets.csv;

import com.jrefinery.report.DataRow;
import com.jrefinery.report.Group;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.function.AbstractFunction;
import com.jrefinery.report.util.Log;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

public class CSVWriter extends AbstractFunction
{
  private static class CSVRow
  {
    private ArrayList data;
    private CSVQuoter quoter;
    private String lineSeparator;

    public CSVRow(CSVQuoter quoter)
    {
      data = new ArrayList();
      this.quoter = quoter;
      lineSeparator = System.getProperty("line.separator", "\n");
    }

    public void append (int d)
    {
      data.add (new Integer (d));
    }

    public void append (Object o)
    {
      data.add (o);
    }

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

  private Writer w;
  private int depLevel;
  private CSVQuoter quoter;

  public CSVWriter()
  {
    setDependencyLevel(-1);
    quoter = new CSVQuoter();
  }

  public Writer getWriter()
  {
    return w;
  }

  public void setWriter(Writer w)
  {
    this.w = w;
  }

  public void setSeparator(String separator)
  {
    if (separator == null) throw new NullPointerException();
    this.quoter.setSeparator(separator);
  }

  public String getSeparator()
  {
    return quoter.getSeparator();
  }

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

  private void writeDataRowNames (DataRow dr,CSVRow row)
  {
    for (int i = 0; i < dr.getColumnCount(); i++)
    {
      row.append(dr.getColumnName(i));
    }
  }

  /**
   * Receives notification that the report has started.
   *
   * @param event  the event.
   */
  public void reportStarted(ReportEvent event)
  {
    try
    {
      CSVRow names = new CSVRow(quoter);
      names.append ("level");
      writeDataRowNames(event.getDataRow(), names);
      names.write(getWriter());
      
      CSVRow row = new CSVRow(quoter);
      row.append(-1);
      row.append("reportheader");
      writeDataRow(event.getDataRow(), row);
      row.write(getWriter());
    }
    catch (IOException ioe)
    {
      Log.error ("Error writing the band", ioe);
    }
  }

  /**
   * Receives notification that the report has finished.
   *
   * @param event  the event.
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
      Log.error ("Error writing the band", ioe);
    }
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event  the event.
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
      Log.error ("Error writing the band", ioe);
    }
  }

  /**
   * Receives notification that a group has finished.
   *
   * @param event  the event.
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
      Log.error ("Error writing the band", ioe);
    }
  }

  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event  the event.
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
      Log.error ("Error writing the band", ioe);
    }
  }

  /**
   * Return the current expression value.
   * <P>
   * The value depends (obviously) on the expression implementation.
   *
   * @return the value of the function.
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
