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
 * ReportDescriptionReadHandler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
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
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class ReportDescriptionReadHandler extends AbstractPropertyXmlReadHandler
{
  public ReportDescriptionReadHandler ()
  {
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
    final JFreeReport report = (JFreeReport)
            getRootHandler().getHelperObject(ReportDefinitionReadHandler.REPORT_KEY);

    if (tagName.equals("report-header"))
    {
      return new BandReadHandler(report.getReportHeader());
    }
    else if (tagName.equals("report-footer"))
    {
      return new BandReadHandler(report.getReportFooter());
    }
    else if (tagName.equals("page-header"))
    {
      return new BandReadHandler(report.getPageHeader());
    }
    else if (tagName.equals("page-footer"))
    {
      return new BandReadHandler(report.getPageFooter());
    }
    else if (tagName.equals("watermark"))
    {
      return new BandReadHandler(report.getWatermark());
    }
    else if (tagName.equals("groups"))
    {
      return new GroupsReadHandler(report.getGroups());
    }
    else if (tagName.equals("itemband"))
    {
      return new BandReadHandler(report.getItemBand());
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
    return null;
  }

  protected void storeComments ()
          throws SAXException
  {
    final JFreeReport report = (JFreeReport)
            getRootHandler().getHelperObject(ReportDefinitionReadHandler.REPORT_KEY);

    final CommentHintPath path = new CommentHintPath(report);
    defaultStoreComments(path);
  }
}
