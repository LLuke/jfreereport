package org.jfree.report.modules.parser.base.common;

import java.util.ArrayList;

import org.jfree.report.Group;
import org.jfree.report.modules.parser.base.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class GroupFieldsReadHandler extends AbstractPropertyXmlReadHandler
{
  private Group group;
  private ArrayList fieldHandlers;

  public GroupFieldsReadHandler (final Group group)
  {
    this.group = group;
    this.fieldHandlers = new ArrayList();
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
    if (tagName.equals("field"))
    {
      final CommentHintPath commentHintPath = new CommentHintPath(group);
      commentHintPath.addName("fields");
      commentHintPath.addName("field");
      defaultStoreComments(commentHintPath);
      final GroupFieldReadHandler readHandler =
              new GroupFieldReadHandler(commentHintPath);
      fieldHandlers.add(readHandler);
      return readHandler;
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
    for (int i = 0; i < fieldHandlers.size(); i++)
    {
      final GroupFieldReadHandler readHandler =
              (GroupFieldReadHandler) fieldHandlers.get(i);
      group.addField((String) readHandler.getObject());
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
    return group;
  }

  protected void storeComments ()
          throws SAXException
  {
    final CommentHintPath commentHintPath = new CommentHintPath(group);
    commentHintPath.addName("fields");
    defaultStoreComments(commentHintPath);
  }
}
