/**
 * Date: Jan 7, 2003
 * Time: 5:15:08 PM
 *
 * $Id: XMLWriter.java,v 1.1 2003/01/07 17:34:29 taqua Exp $
 */
package com.jrefinery.report.targets.csv;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.Group;
import com.jrefinery.report.TextElement;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.function.AbstractFunction;
import com.jrefinery.report.util.Log;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class CSVWriter extends AbstractFunction
{
  private static class CSVRow
  {
    private ArrayList data;
    private CSVQuoter quoter;

    public CSVRow()
    {
      data = new ArrayList();
      quoter = new CSVQuoter();
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
    }
  }

  private Writer w;
  private int depLevel;

  public CSVWriter()
  {
    setDependencyLevel(-1);
  }

  public Writer getWriter()
  {
    return w;
  }

  public void setWriter(Writer w)
  {
    this.w = w;
  }

  private void writeBand (Band b,CSVRow row)
    throws IOException
  {
    List l = b.getElements();
    Iterator it = l.iterator();
    while (it.hasNext())
    {
      Element e = (Element) it.next();
      if (e instanceof Band)
      {
        writeBand((Band) e, row);
        continue;
      }

      if (e.getContentType().equals(TextElement.CONTENT_TYPE) == false)
        continue;

      row.append(e.getValue());
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
      CSVRow row = new CSVRow();
      row.append(-1);
      row.append("reportheader");
      writeBand(event.getReport().getReportHeader(), row);
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
      CSVRow row = new CSVRow();
      row.append(-1);
      row.append("reportfooter");
      writeBand(event.getReport().getReportFooter(), row);
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

      CSVRow row = new CSVRow();
      row.append(currentIndex);

      Group g = event.getReport().getGroup(currentIndex);
      String bandInfo = "groupheader name=\"" + g.getName()+ "\"";
      row.append(bandInfo);
      writeBand(g.getHeader(), row);
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

      CSVRow row = new CSVRow();
      row.append(currentIndex);

      Group g = event.getReport().getGroup(currentIndex);
      String bandInfo = "groupfooter name=\"" + g.getName()+ "\"";
      row.append(bandInfo);
      writeBand(g.getFooter(), row);
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
      CSVRow row = new CSVRow();
      row.append(event.getState().getCurrentGroupIndex());
      row.append("itemband");
      writeBand(event.getReport().getItemBand(), row);
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
