/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * $Id: GroupsHandler.java,v 1.5 2003/07/23 13:56:43 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext;

import org.jfree.report.Group;
import org.jfree.report.modules.parser.base.CommentHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.ReportParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A groups handler. Handles the group definition for the report. The defined groups
 * need to define all fields of their parent group and at least one additional field.
 *
 * @see org.jfree.report.JFreeReport#setGroups
 * @see GroupList
 * @author Thomas Morgner.
 */
public class GroupsHandler extends AbstractExtReportParserHandler
{
  /** The 'group' tag name. */
  public static final String GROUP_TAG = "group";

  /** Contains the reference to the new group. Contains null if the group is redefined. */
  private Group currentGroup;
  private boolean isNewGroup;

  /**
   * Creates a new handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   */
  public GroupsHandler(final ReportParser parser, final String finishTag)
  {
    super(parser, finishTag);
  }

  /**
   * Callback to indicate that an XML element start tag has been read by the parser.
   *
   * @param tagName  the tag name.
   * @param attrs  the attributes.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void startElement(final String tagName, final Attributes attrs) throws SAXException
  {
    if (tagName.equals(GROUP_TAG))
    {
      currentGroup = null;
      final String name = attrs.getValue("name");
      if (name != null)
      {
        currentGroup = getReport().getGroupByName(name);
      }
      if (currentGroup == null)
      {
        currentGroup = new Group();
        if (name != null)
        {
          currentGroup.setName(name);
        }
        isNewGroup = true;
        // the new group must be added after the group fields were defined.
      }

      addComment(currentGroup, CommentHandler.OPEN_TAG_COMMENT);
      final GroupHandler handler = new GroupHandler(getReportParser(), tagName, currentGroup);
      getParser().pushFactory(handler);
    }
    else
    {
      throw new SAXException("Invalid TagName: " + tagName + ", expected one of: "
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
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void characters(final char[] ch, final int start, final int length) throws SAXException
  {
    // not used here
  }

  /**
   * Callback to indicate that an XML element end tag has been read by the parser.
   *
   * @param tagName  the tag name.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void endElement(final String tagName) throws SAXException
  {
    if (tagName.equals(getFinishTag()))
    {
      getParser().popFactory().endElement(tagName);
    }
    else if (tagName.equals(GROUP_TAG))
    {
      if (isNewGroup)
      {
        getReport().addGroup(currentGroup);
      }
      addComment(currentGroup, CommentHandler.CLOSE_TAG_COMMENT);
      currentGroup = null;
    }
    else
    {
      throw new SAXException("Invalid TagName: " + tagName + ", expected one of: "
          + GROUP_TAG + ", " + getFinishTag());
    }
  }

  private void addComment (Object name, String hint)
  {
    CommentHintPath path = new CommentHintPath();
    path.addName(ExtParserModuleInit.REPORT_DEFINITION_TAG);
    path.addName(ExtReportHandler.REPORT_DESCRIPTION_TAG);
    path.addName(ReportDescriptionHandler.GROUPS_TAG);
    path.addName(name);
    String[] comments = getReportParser().getComments();
    getReport().getReportBuilderHints().putHint
        (path, hint, comments);
  }


}
