package org.jfree.report.modules.parser.ext.readhandlers;

import java.util.ArrayList;

import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.coretypes.StringReadHandler;
import org.jfree.report.Group;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class GroupFieldsReadHandler extends AbstractXmlReadHandler
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
                                               final Attributes atts)
          throws XmlReaderException, SAXException
  {
    if (tagName.equals("field"))
    {
      final StringReadHandler readHandler = new StringReadHandler ();
      fieldHandlers.add (readHandler);
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
      final StringReadHandler readHandler =
              (StringReadHandler) fieldHandlers.get(i);
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
}
