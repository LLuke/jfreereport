/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * $Id: ReportDefinitionContentHandler.java,v 1.3 2002/05/23 22:32:22 taqua Exp $
 *
 * Changes
 * -------
 * 01-Mar-2002 : Version 1, based on the XML format contributed by Thomas Morgner and modified
 *               by DG (DG);
 * 15-Apr-2002 : name element is now optional. Anonymous elements are generated as needed
 *               a default group is generated if no groups are defined.
 *               The parsing of numeric values has been secured against number-format exceptions
 * 10-May-2002 : Uses different factories to create some sort of parsing state.
 *
 */

package com.jrefinery.report.io;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.util.Log;
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
 */
public class ReportDefinitionContentHandler extends AbstractReportDefinitionHandler
        implements ReportDefinitionTags
{
  private Stack currentHandler;

  private FontFactory fontFactory;
  private ReportFactory factory;

  /**
   * The report under construction. This element is null until the complete report has been
   * parsed.
   */
  private JFreeReport report;

  public FontFactory getFontFactory ()
  {
    return fontFactory;
  }

  protected ReportFactory getReportFactory ()
  {
    return factory;
  }

  public void setExpectedHandler (DefaultHandler handler)
  {
    currentHandler.push (handler);
  }

  public void finishedHandler ()
  {
    Object o = currentHandler.pop ();
  }

  public DefaultHandler getExpectedHandler ()
  {
    return (DefaultHandler) currentHandler.peek ();
  }

  /**
   * Default constructor.
   */
  public ReportDefinitionContentHandler ()
  {
    factory = createReportFactory ();
    fontFactory = createFontFactory ();

    currentHandler = new Stack ();
    currentHandler.push (factory);
  }

  /**
   * Returns the report for this content handler.  Be careful when you get this reference - the
   * report may be half-built.
   */
  public JFreeReport getReport ()
  {
    return this.report;
  }

  public void setReport (JFreeReport report)
  {
    this.report = report;
    finishedHandler ();
  }

  /**
   * An element start tag has been reached.  We can read the attributes for the element, but
   * have to wait for the sub-elements and text...so store the attributes for later use.
   */
  public void startElement (String namespaceURI,
                            String localName,
                            String qName,
                            Attributes atts) throws SAXException
  {
    DefaultHandler handler = getExpectedHandler ();
    Log.debug ("Parsing delegated to : " + handler.getClass ().getName () + " for start tag " + qName);
    handler.startElement (namespaceURI, localName, qName, atts);
  }

  public void characters (char[] chars, int i, int i1) throws SAXException
  {
    DefaultHandler handler = getExpectedHandler ();
    handler.characters (chars, i, i1);
  }

  /**
   * An element ending tag has been reached.  This is a chance to make use of the element
   * text that has been accumulated.
   */
  public void endElement (String namespaceURI, String localName, String qName) throws SAXException
  {
    DefaultHandler handler = getExpectedHandler ();
    Log.debug ("Parsing delegated to : " + handler.getClass ().getName () + " for end tag " + qName);
    handler.endElement (namespaceURI, localName, qName);
  }

  public ElementFactory createElementFactory ()
  {
    return new ElementFactory (getReportFactory ());
  }

  public FontFactory createFontFactory ()
  {
    return new FontFactory ();
  }

  public FunctionFactory createFunctionFactory ()
  {
    return new FunctionFactory (getReportFactory ());
  }

  public GroupFactory createGroupFactory ()
  {
    return new GroupFactory (getReportFactory ());
  }

  public ReportFactory createReportFactory ()
  {
    return new ReportFactory (this);
  }

  public BandFactory createBandFactory ()
  {
    return new BandFactory (getReportFactory ());
  }

  public AbstractReportDefinitionHandler getInstance ()
  {
    return new ReportDefinitionContentHandler ();
  }
}
