package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.report.GroupList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class GroupsReadHandler extends AbstractXmlReadHandler
{
  private GroupList groupList;

  public GroupsReadHandler (final GroupList groupList)
  {
    this.groupList = groupList;
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
                                               final Attributes atts)
          throws XmlReaderException, SAXException
  {
    if (tagName.equals("group"))
    {
      return new GroupReadHandler (groupList);
    }
    return null;
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
    return groupList;
  }
}
