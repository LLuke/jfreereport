/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
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
 * -----------------------------------
 * ReportDefinitionContentHandler.java
 * -----------------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: ReportDefinitionContentHandler.java,v 1.8 2002/09/13 15:38:08 mungady Exp $
 *
 * Changes
 * -------
 * 01-Mar-2002 : Version 1, based on the XML format contributed by Thomas Morgner and modified
 *               by DG (DG);
 * 15-Apr-2002 : name element is now optional. Anonymous elements are generated as needed
 *               a default group is generated if no groups are defined.
 *               The parsing of numeric values has been secured against number-format exceptions
 * 10-May-2002 : Uses different factories to create some sort of parsing state.
 * 08-Jun-2002 : Documentation
 */

package com.jrefinery.report.io;

import com.jrefinery.report.JFreeReport;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Stack;

/**
 * Used to construct a JFreeReport instance from a definition in XML format.
 * <p>
 * This is the default implementation used by the JFreeReportDemo. This parser
 * is formed to use reports formed after the included "report.dtd".
 * <p>
 * The DTD can also be downloaded from "http://jfreereport.sourceforge.net/report.dtd"
 * <p>
 * Different factories are used to create the report. The topmost factory on the stack
 * is expected to be able to handle the next element. EndElement, startElement and characters
 * is forwarded to the active factory.
 *
 * @author TM
 */
public class ReportDefinitionContentHandler extends AbstractReportDefinitionHandler
        implements ReportDefinitionTags
{
  /** Storage for the handlers. */
  private Stack currentHandler;

  /** The font handler. */
  private FontFactory fontFactory;

  /** The report handler. */
  private ReportFactory reportFactory;

  /** The report under construction. */
  private JFreeReport report;

  /**
   * Default constructor.
   */
  public ReportDefinitionContentHandler ()
  {
    currentHandler = new Stack ();
    currentHandler.push (getReportFactory());
  }

  /**
   * Returns a single instance of FontFactory which is used to create Fonts for bands and
   * elements, where the element fonts depend on the bands font.
   *
   * @return the font handler.
   */
  public FontFactory getFontFactory ()
  {
    if (fontFactory == null)
    {
      fontFactory = new FontFactory();
    }
    return fontFactory;
  }

  /**
   * Returns the ReportFactory for this contentHandler. It is guaranteed that there is only one
   * report factory per contentHandler.
   *
   * @return the report handler.
   */
  protected ReportFactory getReportFactory ()
  {
    if (reportFactory == null)
    {
      reportFactory = new ReportFactory(this);
    }
    return reportFactory;
  }

  /**
   * Sets the next expected handler for SAX-Events.
   *
   * @param handler  the next SAX handler.
   */
  public void setExpectedHandler (DefaultHandler handler)
  {
    currentHandler.push (handler);
  }

  /**
   * Restores the previous active SAX-Handler when the current handler has finished its task.
   */
  public void finishedHandler ()
  {
    Object o = currentHandler.pop ();
  }

  /**
   * Returns the currently active SAXHandler which will process the next SAXEvent.
   *
   * @return  the current SAX handler.
   */
  public DefaultHandler getExpectedHandler ()
  {
    return (DefaultHandler) currentHandler.peek ();
  }

  /**
   * Returns the report for this content handler. This method will return null, until the report is
   * completely built and parsing has finished.
   *
   * @return the completely build report or null, if the parsing is still in progress
   */
  public JFreeReport getReport ()
  {
    return this.report;
  }

  /**
   * Sets the report, which also indicates that the handler is finished.
   *
   * @param report  the parsed report.
   */
  public void setReport (JFreeReport report)
  {
    this.report = report;
    finishedHandler ();
  }

  /**
   * An element start tag has been reached.  The SAXEvent is forwarded to the currently active
   * handler.
   *
   * @param namespaceURI  the namespace URI.
   * @param localName  the local name.
   * @param qName  the element name.
   * @param atts  the element attributes.
   *
   * @throws SAXException if there is a problem parsing the XML report template.
   */
  public void startElement (String namespaceURI,
                            String localName,
                            String qName,
                            Attributes atts) throws SAXException
  {
    DefaultHandler handler = getExpectedHandler ();
    handler.startElement (namespaceURI, localName, qName, atts);
  }

  /**
   * Forward the characters method to the currently active handler.
   *
   * @param chars  storage for character data.
   * @param i  the start index.
   * @param i1  the number of valid characters.
   *
   * @throws SAXException if there is a problem parsing the XML report template.
   */
  public void characters (char[] chars, int i, int i1) throws SAXException
  {
    DefaultHandler handler = getExpectedHandler ();
    handler.characters (chars, i, i1);
  }

  /**
   * An element ending tag has been reached.  This is a chance to make use of the element
   * text that has been accumulated.
   *
   * @param namespaceURI  the namespace URI.
   * @param localName  the local name.
   * @param qName  the element name.
   *
   * @throws SAXException if there is a problem parsing the XML report template.
   */
  public void endElement (String namespaceURI, String localName, String qName) throws SAXException
  {
    DefaultHandler handler = getExpectedHandler ();
    handler.endElement (namespaceURI, localName, qName);
  }

  /**
   * Creates a new ElementFactory.
   *
   * @return an element handler.
   */
  public ElementFactory createElementFactory ()
  {
    return new ElementFactory (getReportFactory ());
  }

  /**
   * Creates a new FunctionFactory.
   *
   * @return a function handler.
   */
  public FunctionFactory createFunctionFactory ()
  {
    return new FunctionFactory (getReportFactory ());
  }

  /**
   * Creates a new GroupFactory.
   *
   * @return a group handler.
   */
  public GroupFactory createGroupFactory ()
  {
    return new GroupFactory (getReportFactory ());
  }

  /**
   * Creates a new BandFactory.
   *
   * @return a band handler.
   */
  public BandFactory createBandFactory ()
  {
    return new BandFactory (getReportFactory ());
  }

  /**
   * Returns a new Instance of this ReportDefinitionHandler, using this implementation as
   * prototype.
   *
   * @return a report handler.
   */
  public AbstractReportDefinitionHandler getInstance ()
  {
    return new ReportDefinitionContentHandler ();
  }
}
