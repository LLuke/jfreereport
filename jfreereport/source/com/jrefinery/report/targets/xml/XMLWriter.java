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
 * --------------
 * XMLWriter.java
 * --------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: XMLWriter.java,v 1.6 2003/02/12 23:05:58 taqua Exp $
 *
 * Changes
 * -------
 * 07-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.xml;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.Group;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.function.AbstractFunction;
import com.jrefinery.report.util.CharacterEntityParser;
import com.jrefinery.report.util.Log;

/**
 * The XMLWriter is the content creation function used to create the XML content.
 * This implementation does no layouting, the bands and elements are written in the
 * defined order.
 * <p>
 * The xml writer is intended as simple example on how to write OutputFunctions,
 * the XML-code generated is very simple and easy to understand. If you seek
 * complexer XML-Outputs, have a look at the HTML-Writer, this implementation is able
 * to write XHTML output.
 * 
 * @author Thomas Morgner
 */
public class XMLWriter extends AbstractFunction
{
  /** the writer used to write the generated document. */
  private Writer w;
  
  /** the dependency level. */
  private int depLevel;

  /** the XMLEntity parser used to encode the xml characters. */
  private CharacterEntityParser entityParser;

  /**
   * Creates a new XMLWriter function. The Writer gets a dependency level of
   * -1.
   */
  public XMLWriter()
  {
    setDependencyLevel(-1);
    entityParser = CharacterEntityParser.createXMLEntityParser();
  }

  /**
   * returns the assigned writer for the output.
   *
   * @return the writer.
   */
  public Writer getWriter()
  {
    return w;
  }

  /**
   * Defines the writer for the XML-output.
   *
   * @param w the writer.
   */
  public void setWriter(Writer w)
  {
    this.w = w;
  }

  /**
   * Writes the band's elements into the assigned Writer.
   *
   * @param b the band that should be written.
   * @throws IOException if an IO-Error occurs.
   */
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
        w.write (entityParser.encodeEntities(e.getName()));
        w.write ("\">");
        w.write(entityParser.encodeEntities(String.valueOf(e.getValue())));
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
   * Writes the report header.
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
   * Writes the report footer.
   *
   * @param event  the event.
   */
  public void reportFinished(ReportEvent event)
  {
    try
    {
      w.write("<reportfooter>");
      writeBand(event.getReport().getReportFooter());
      w.write("</reportfooter>");
      w.write("</report>");
    }
    catch (IOException ioe)
    {
      Log.error ("Error writing the band", ioe);
    }
  }

  /**
   * Writes the header of the current group.
   *
   * @param event  the event.
   */
  public void groupStarted(ReportEvent event)
  {
    try
    {
      w.write("<groupheader name=\"");
      Group g = event.getReport().getGroup(event.getState().getCurrentGroupIndex());
      w.write(entityParser.encodeEntities(g.getName()));
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
   * Writes the footer of the current group.
   *
   * @param event  the event.
   */
  public void groupFinished(ReportEvent event)
  {
    try
    {
      w.write("<groupfooter name=\"");
      Group g = event.getReport().getGroup(event.getState().getCurrentGroupIndex());
      w.write(entityParser.encodeEntities(g.getName()));
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
   * Writes the itemband.
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
   * Starts the itembands section.
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
   * Closes the itemband section.
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
   * Return the self reference of this writer.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {
    return this;
  }

  /**
   * The dependency level defines the level of execution for this function. Higher dependency 
   * functions are executed before lower dependency functions. For ordinary functions and 
   * expressions, the range for dependencies is defined to start from 0 (lowest dependency 
   * possible) to 2^31 (upper limit of int).
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
