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
 * GroupReadHandler.java
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

import org.jfree.report.Group;
import org.jfree.report.GroupList;
import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.base.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.common.GroupFieldsReadHandler;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class GroupReadHandler extends AbstractPropertyXmlReadHandler
{
  /**
   * Literal text for an XML report element.
   */
  public static final String GROUP_HEADER_TAG = "group-header";

  /**
   * Literal text for an XML report element.
   */
  public static final String GROUP_FOOTER_TAG = "group-footer";

  /**
   * Literal text for an XML report element.
   */
  public static final String FIELDS_TAG = "fields";

  /**
   * Literal text for an XML report element.
   */
  public static final String FIELD_TAG = "field";


  private static final String NAME_ATT = "name";

  private GroupList groupList;
  private Group group;

  public GroupReadHandler (final GroupList groupList)
  {
    this.groupList = groupList;
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes attrs)
          throws SAXException
  {
    final String groupName = attrs.getValue(NAME_ATT);
    if (groupName != null)
    {
      final JFreeReport report = (JFreeReport)
              getRootHandler().getHelperObject(ReportDefinitionReadHandler.REPORT_KEY);
      group = report.getGroupByName(groupName);
      if (group == null)
      {
        group = new Group();
        group.setName(groupName);
      }
    }
    else
    {
      group = new Group();
    }
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
    if (tagName.equals(GROUP_HEADER_TAG))
    {
      return new BandReadHandler(group.getHeader());
    }
    else if (tagName.equals(GROUP_FOOTER_TAG))
    {
      return new BandReadHandler(group.getFooter());
    }
    else if (tagName.equals(FIELDS_TAG))
    {
      return new GroupFieldsReadHandler(group);
    }
    return null;
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
    groupList.add(group);
  }

  /**
   * Returns the object for this element.
   *
   * @return the object.
   *
   * @throws org.jfree.xml.parser.XmlReaderException
   *          if there is a parsing error.
   */
  public Object getObject ()
          throws XmlReaderException
  {
    return group;
  }

  protected void storeComments ()
          throws SAXException
  {
    final CommentHintPath hintPath = new CommentHintPath(group);
    defaultStoreComments(hintPath);
  }
}
