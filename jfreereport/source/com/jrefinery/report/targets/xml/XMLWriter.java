/**
 * Date: Jan 7, 2003
 * Time: 5:15:08 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.xml;

import com.jrefinery.report.function.AbstractFunction;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.Group;
import com.jrefinery.report.util.Log;

import java.io.Writer;
import java.io.IOException;
import java.util.List;

public class XMLWriter extends AbstractFunction
{
  private Writer w;
  private int depLevel;

  public XMLWriter()
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

  private void writeBand (Band b)
    throws IOException
  {

    List l = b.getElements();
    for (int i = 0; i < l.size(); i++)
    {
      Element e = (Element) l.get(i);
      if (e.getContentType().startsWith("text"))
      {
        w.write ("<element name=\"");
        w.write (e.getName());
        w.write ("\">");
        w.write(String.valueOf(e.getValue()));
        w.write ("</element>");
      }
      else if (e instanceof Band)
      {
        w.write("<band>");
        writeBand((Band) e);
        w.write("</band>");
      }
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
      w.write("<report>");
      w.write("<reportheader>");
      writeBand(event.getReport().getReportHeader());
      w.write("</reportheader>");
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
      w.write("<reportfooter>");
      writeBand(event.getReport().getReportHeader());
      w.write("</reportfooter>");
      w.write("</report>");
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
      w.write("<groupheader name=\"");
      Group g = event.getReport().getGroup(event.getState().getCurrentGroupIndex());
      w.write(g.getName());
      w.write("\">");
      writeBand(g.getHeader());
      w.write("</groupheader>");
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
      w.write("<groupfooter name=\"");
      Group g = event.getReport().getGroup(event.getState().getCurrentGroupIndex());
      w.write(g.getName());
      w.write("\">");
      writeBand(g.getFooter());
      w.write("</groupfooter>");
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
      w.write("<itemband>");
      writeBand(event.getReport().getItemBand());
      w.write("</itemband>");
    }
    catch (IOException ioe)
    {
      Log.error ("Error writing the band", ioe);
    }
  }

  /**
   * Receives notification that a group of item bands is about to be processed.
   * <P>
   * The next events will be itemsAdvanced events until the itemsFinished event is raised.
   *
   * @param event The event.
   */
  public void itemsStarted(ReportEvent event)
  {
    try
    {
      w.write("<items>");
    }
    catch (IOException ioe)
    {
      Log.error ("Error writing the items tag", ioe);
    }
  }

  /**
   * Receives notification that a group of item bands has been completed.
   * <P>
   * The itemBand is finished, the report starts to close open groups.
   *
   * @param event The event.
   */
  public void itemsFinished(ReportEvent event)
  {
    try
    {
      w.write("</items>");
    }
    catch (IOException ioe)
    {
      Log.error ("Error writing the items tag", ioe);
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
