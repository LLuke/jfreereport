package org.jfree.report.modules.parser.ext.readhandlers;

import org.jfree.report.GroupList;
import org.jfree.report.modules.parser.base.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class GroupsReadHandler extends AbstractPropertyXmlReadHandler
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
                                               final PropertyAttributes atts)
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


  protected void storeComments ()
          throws SAXException
  {
    final CommentHintPath hintPath = new CommentHintPath(groupList);
    defaultStoreComments(hintPath);
  }
}
