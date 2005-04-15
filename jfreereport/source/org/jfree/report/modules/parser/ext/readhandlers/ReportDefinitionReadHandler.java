/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * ReportDefinitionReadHandler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ReportDefinitionReadHandler.java,v 1.5 2005/03/03 23:00:21 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.parser.ext.readhandlers;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.base.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.modules.parser.base.common.FunctionsReadHandler;
import org.jfree.report.modules.parser.base.common.IncludeReadHandler;
import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceCollector;
import org.jfree.report.modules.parser.ext.factory.elements.ElementFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.stylekey.StyleKeyFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateCollector;
import org.jfree.util.ObjectUtilities;
import org.jfree.xml.factory.objects.ClassFactoryCollector;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class ReportDefinitionReadHandler extends AbstractPropertyXmlReadHandler
{
  public static final String REPORT_KEY = ReportParser.HELPER_OBJ_REPORT_NAME;
  public static final String ELEMENT_FACTORY_KEY = "::element-factory";
  public static final String STYLE_FACTORY_KEY = "::stylekey-factory";
  public static final String CLASS_FACTORY_KEY = "::class-factory";
  public static final String DATASOURCE_FACTORY_KEY = "::datasource-factory";
  public static final String TEMPLATE_FACTORY_KEY = "::template-factory";

  private JFreeReport report;

  public ReportDefinitionReadHandler ()
  {
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes attrs)
          throws SAXException, XmlReaderException
  {
    final Object maybeReport = getRootHandler().getHelperObject(REPORT_KEY);
    final JFreeReport report;
    if (maybeReport instanceof JFreeReport == false)
    {
      // replace it ..
      report = new JFreeReport();
    }
    else
    {
      report = (JFreeReport) maybeReport;
    }

    final ReportParser parser = (ReportParser) getRootHandler();
    if (parser.isIncluded() == false)
    {
      final String value = attrs.getValue("name");
      if (value != null)
      {
        report.setName(value);
      }
    }
    getRootHandler().setHelperObject(REPORT_KEY, report);
    getRootHandler().setHelperObject(ELEMENT_FACTORY_KEY, new ElementFactoryCollector());
    getRootHandler().setHelperObject(STYLE_FACTORY_KEY, new StyleKeyFactoryCollector());
    getRootHandler().setHelperObject(CLASS_FACTORY_KEY, new ClassFactoryCollector());
    getRootHandler().setHelperObject(DATASOURCE_FACTORY_KEY, new DataSourceCollector());
    getRootHandler().setHelperObject(TEMPLATE_FACTORY_KEY, new TemplateCollector());

    this.report = report;
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected XmlReadHandler getHandlerForChild (final String tagName,
                                               final PropertyAttributes atts)
          throws XmlReaderException, SAXException
  {
    if (tagName.equals("parser-config"))
    {
      return new ParserConfigReadHandler();
    }
    else if (tagName.equals("report-config"))
    {
      return new ReportConfigReadHandler();
    }
    else if (tagName.equals("styles"))
    {
      return new StylesReadHandler();
    }
    else if (tagName.equals("templates"))
    {
      return new TemplatesReadHandler();
    }
    else if (tagName.equals("report-description"))
    {
      return new ReportDescriptionReadHandler();
    }
    else if (tagName.equals("functions"))
    {
      return new FunctionsReadHandler(report);
    }
    else if (tagName.equals("include"))
    {
      return new IncludeReadHandler();
    }
    return null;
  }

  /**
   * Returns the object for this element or null, if this element does not create an
   * object.
   *
   * @return the object.
   *
   * @throws org.jfree.xml.parser.XmlReaderException
   *          if there is a parsing error.
   */
  public Object getObject ()
          throws XmlReaderException
  {
    return getRootHandler().getHelperObject(REPORT_KEY);
  }

  protected void storeComments ()
          throws SAXException
  {
    defaultStoreComments(new CommentHintPath("report-definition"));
  }
}
