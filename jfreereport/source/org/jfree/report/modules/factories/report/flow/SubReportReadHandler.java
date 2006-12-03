/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.report.modules.factories.report.flow;

import java.util.ArrayList;

import org.jfree.report.structure.Element;
import org.jfree.report.structure.SubReport;
import org.jfree.xmlns.parser.XmlReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Creation-Date: 09.04.2006, 14:57:38
 *
 * @author Thomas Morgner
 */
public class SubReportReadHandler extends SectionReadHandler
{
  private SubReport subReport;
  private ArrayList importParameters;
  private ArrayList exportParameters;

  /**
   * Creates a new generic read handler. The given namespace and tagname can be
   * arbitary values and should not be confused with the ones provided by the
   * XMLparser itself.
   *
   * @param namespace
   * @param tagName
   */
  public SubReportReadHandler()
  {
    subReport = new SubReport();
    importParameters = new ArrayList();
    exportParameters = new ArrayList();
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing(final Attributes attrs) throws SAXException
  {
    super.startParsing(attrs);
    final String source = attrs.getValue(getUri(), "href");
    if (source != null)
    {

    }

  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws SAXException       if there is a parsing error.
   * @throws XmlReaderException if there is a reader error.
   */
  protected XmlReadHandler getHandlerForChild(final String uri,
                                              final String tagName,
                                              final Attributes atts)
          throws SAXException
  {
    XmlReadHandler base = super.getHandlerForChild(uri, tagName, atts);
    if (base != null)
    {
      return base;
    }
    if (FlowReportFactoryModule.NAMESPACE.equals(uri))
    {
      if ("import-parameter".equals(tagName))
      {
        ParameterMappingReadHandler handler = new ParameterMappingReadHandler();
        importParameters.add(handler);
        return handler;
      }
      if ("export-parameter".equals(tagName))
      {
        ParameterMappingReadHandler handler = new ParameterMappingReadHandler();
        exportParameters.add(handler);
        return handler;
      }
    }
    return null;
  }

  /**
   * Done parsing.
   *
   * @throws SAXException       if there is a parsing error.
   * @throws XmlReaderException if there is a reader error.
   */
  protected void doneParsing() throws SAXException
  {
    super.doneParsing();
    SubReport report = (SubReport) getElement();
    for (int i = 0; i < importParameters.size(); i++)
    {
      final ParameterMappingReadHandler handler =
              (ParameterMappingReadHandler) importParameters.get(i);
      report.addInputParameter(handler.getName(), handler.getAlias());
    }
    for (int i = 0; i < exportParameters.size(); i++)
    {
      final ParameterMappingReadHandler handler =
              (ParameterMappingReadHandler) exportParameters.get(i);
      report.addExportParameter(handler.getAlias(), handler.getName());
    }
  }

  protected Element getElement()
  {
    return subReport;
  }
}
