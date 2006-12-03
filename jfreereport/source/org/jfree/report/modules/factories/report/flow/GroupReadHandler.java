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

import org.jfree.report.structure.Element;
import org.jfree.report.structure.Group;
import org.jfree.xmlns.parser.XmlReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Creation-Date: 09.04.2006, 15:37:31
 *
 * @author Thomas Morgner
 */
public class GroupReadHandler extends SectionReadHandler
{
  private Group group;
  private GroupingExpressionReadHandler groupingExpressionReadHandler;

  public GroupReadHandler()
  {
    group = new Group();
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
      if ("grouping-expression".equals(tagName))
      {
        groupingExpressionReadHandler = new GroupingExpressionReadHandler();
        return groupingExpressionReadHandler;
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
    if (groupingExpressionReadHandler == null)
    {
      throw new SAXException("Required element 'grouping-expression' is missing.");
    }

    super.doneParsing();
    Group group = (Group) getElement();
    group.setGroupingExpression(groupingExpressionReadHandler.getExpression());
  }

  protected Element getElement()
  {
    return group;
  }
}
