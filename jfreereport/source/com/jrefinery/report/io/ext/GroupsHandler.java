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
 * ------------------
 * GroupsHandler.java
 * ------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id:$
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext;

import com.jrefinery.report.Group;
import com.jrefinery.report.GroupList;
import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A groups handler.
 * 
 * @author Thomas Morgner.
 */
public class GroupsHandler implements ReportDefinitionHandler
{
  /** The 'group' tag name. */
  public static final String GROUP_TAG = "group";

  /** The finish tag. */
  private String finishTag;
  
  /** The parser. */
  private Parser parser;
  
  /** The group list. */
  private GroupList groupList;

  /**
   * Creates a new handler.
   * 
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   */
  public GroupsHandler(Parser parser, String finishTag)
  {
    this.parser = parser;
    this.finishTag = finishTag;
    this.groupList = new GroupList();
  }

  /**
   * Returns the parser.
   * 
   * @return The parser.
   */
  public Parser getParser()
  {
    return parser;
  }

  /**
   * Callback to indicate that an XML element start tag has been read by the parser. 
   * 
   * @param tagName  the tag name.
   * @param attrs  the attributes.
   * 
   * @throws SAXException ??.
   */
  public void startElement(String tagName, Attributes attrs) throws SAXException
  {
    if (tagName.equals(GROUP_TAG))
    {
      Group group = new Group();
      String name = attrs.getValue("name");
      if (name != null)
      {
        group.setName(name);
      }
      GroupHandler handler = new GroupHandler(getParser(), tagName, group);
      groupList.add(group);
      getParser().pushFactory(handler);
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " 
                              + GROUP_TAG);
    }
  }

  /**
   * Callback to indicate that some character data has been read.
   * 
   * @param ch  the character array.
   * @param start  the start index for the characters.
   * @param length  the length of the character sequence.
   * 
   * @throws SAXException ??.
   */  
  public void characters(char ch[], int start, int length) throws SAXException
  {
    // not used here
  }

  /**
   * Callback to indicate that an XML element end tag has been read by the parser. 
   * 
   * @param tagName  the tag name.
   * 
   * @throws SAXException ??.
   */
  public void endElement(String tagName) throws SAXException
  {
    if (tagName.equals(finishTag))
    {
      getParser().getReport().setGroups(groupList);
      getParser().popFactory().endElement(tagName);
    }
    else if (tagName.equals (GROUP_TAG))
    {
      // ignore ...
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " 
                              + GROUP_TAG + ", " + finishTag);
    }
  }
}
