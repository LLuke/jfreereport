/**
 * Date: Jan 22, 2003
 * Time: 3:40:12 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext;

import com.jrefinery.report.io.ReportDefinitionHandler;
import com.jrefinery.report.io.Parser;
import com.jrefinery.report.Group;
import com.jrefinery.report.GroupList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class GroupsHandler implements ReportDefinitionHandler
{
  public static final String GROUP_TAG = "group";

  private String finishTag;
  private Parser parser;
  private GroupList groupList;

  public GroupsHandler(Parser parser, String finishTag)
  {
    this.parser = parser;
    this.finishTag = finishTag;
    this.groupList = new GroupList();
  }

  public Parser getParser()
  {
    return parser;
  }

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
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " +
                              GROUP_TAG);

  }

  public void characters(char ch[], int start, int length) throws SAXException
  {
    // not used here
  }

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
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " +
                              GROUP_TAG + ", " +
                              finishTag);
    }
  }
}
