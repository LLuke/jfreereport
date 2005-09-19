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
 * FunctionsReadHandler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: FunctionsReadHandler.java,v 1.3 2005/03/03 23:00:20 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.parser.base.common;

import java.util.ArrayList;

import org.jfree.report.JFreeReport;
import org.jfree.report.function.Expression;
import org.jfree.report.modules.parser.base.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class FunctionsReadHandler extends AbstractPropertyXmlReadHandler
{
  private JFreeReport report;
  private ArrayList expressionHandlers;
  private ArrayList propertyRefs;

  public FunctionsReadHandler (final JFreeReport report)
  {
    this.report = report;
    this.expressionHandlers = new ArrayList();
    this.propertyRefs = new ArrayList();
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
    if (tagName.equals("expression") || tagName.equals("function"))
    {
      final ExpressionReadHandler readHandler = new ExpressionReadHandler();
      expressionHandlers.add(readHandler);
      return readHandler;

    }
    else if (tagName.equals("property-ref"))
    {
      final CommentHintPath commentHintPath = new CommentHintPath("report-definition");
      commentHintPath.addName("functions");
      commentHintPath.addName("property-ref");

      final PropertyReferenceReadHandler readHandler =
              new PropertyReferenceReadHandler(commentHintPath);
      propertyRefs.add(readHandler);
      return readHandler;

    }
    return super.getHandlerForChild(tagName, atts);
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected void doneParsing ()
          throws SAXException, XmlReaderException
  {
    for (int i = 0; i < expressionHandlers.size(); i++)
    {
      final ExpressionReadHandler readHandler =
              (ExpressionReadHandler) expressionHandlers.get(i);
      report.addExpression((Expression) readHandler.getObject());
    }

    for (int i = 0; i < propertyRefs.size(); i++)
    {
      final PropertyReferenceReadHandler readHandler =
              (PropertyReferenceReadHandler) propertyRefs.get(i);
      final Object object = readHandler.getObject();
      report.setPropertyMarked(readHandler.getPropertyName(), true);
      if (object != null)
      {
        if (object instanceof String)
        {
          String text = (String) object;
          if (text.length() == 0)
          {
            continue;
          }
        }
        report.setProperty(readHandler.getPropertyName(), object);
      }
    }
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
    final CommentHintPath commentHintPath = new CommentHintPath("functions");
    defaultStoreComments(commentHintPath);
  }
}
