package org.jfree.report.modules.parser.simple.readhandlers;

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
  public static final String GROUP_HEADER_TAG = "groupheader";

  /**
   * Literal text for an XML report element.
   */
  public static final String GROUP_FOOTER_TAG = "groupfooter";

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
              getRootHandler().getHelperObject(JFreeReportReadHandler.REPORT_KEY);
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
      return new GroupHeaderReadHandler(group.getHeader());
    }
    else if (tagName.equals(GROUP_FOOTER_TAG))
    {
      return new GroupFooterReadHandler(group.getFooter());
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
