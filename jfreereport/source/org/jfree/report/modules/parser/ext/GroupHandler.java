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
 * -----------------
 * GroupHandler.java
 * -----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: GroupHandler.java,v 1.6 2003/07/23 16:02:21 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext;

import org.jfree.report.Band;
import org.jfree.report.Group;
import org.jfree.report.GroupFooter;
import org.jfree.report.GroupHeader;
import org.jfree.report.modules.parser.base.CommentHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.util.CharacterEntityParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A group handler. Handles the definition of a single group.
 *
 * @author Thomas Morgner.
 */
public class GroupHandler extends AbstractExtReportParserHandler
{
  /** The 'fields' tag name. */
  public static final String FIELDS_TAG = "fields";

  /** The 'field' tag name. */
  public static final String FIELD_TAG = "field";

  /** The 'group-header' tag name. */
  public static final String GROUP_HEADER_TAG = "group-header";

  /** The 'group-footer' tag name. */
  public static final String GROUP_FOOTER_TAG = "group-footer";

  /** The group. */
  private Group group;

  /** A buffer. */
  private StringBuffer buffer;

  /** The band handler. */
  private BandHandler bandFactory;

  /** A character entity parser. */
  private CharacterEntityParser entityParser;

  /** temporary storage of group field comments. */
  private String[] fieldComments;

  /**
   * Creates a new handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   * @param group  the group.
   */
  public GroupHandler(final ReportParser parser, final String finishTag, final Group group)
  {
    super(parser, finishTag);
    if (group == null)
    {
      throw new NullPointerException("Group parameter is null");
    }
    this.entityParser = CharacterEntityParser.createXMLEntityParser();
    this.group = group;
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
    if (tagName.equals(GROUP_HEADER_TAG))
    {
      final Band band = group.getHeader();
      final String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }

      CommentHintPath path = createCommentPath(band);
      addComment(path, CommentHandler.OPEN_TAG_COMMENT);
      bandFactory = new BandHandler(getReportParser(), tagName, band, createRootCommentPath());
      getParser().pushFactory(bandFactory);
    }
    else if (tagName.equals(GROUP_FOOTER_TAG))
    {
      final Band band = group.getFooter();
      final String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      CommentHintPath path = createCommentPath(band);
      addComment(path, CommentHandler.OPEN_TAG_COMMENT);
      bandFactory = new BandHandler(getReportParser(), tagName, band, createRootCommentPath());
      getParser().pushFactory(bandFactory);
    }
    else if (tagName.equals(FIELDS_TAG))
    {
      addComment(createCommentPath(tagName), CommentHandler.OPEN_TAG_COMMENT);
    }
    else if (tagName.equals(FIELD_TAG))
    {
      fieldComments = getReportParser().getComments();
      buffer = new StringBuffer();
    }
    else
    {
      throw new SAXException("Invalid TagName: " + tagName + ", expected one of: "
          + GROUP_HEADER_TAG + ", "
          + GROUP_FOOTER_TAG + ", "
          + FIELDS_TAG + ", "
          + FIELD_TAG);
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
    if (buffer != null)
    {
      buffer.append(ch, start, length);
    }
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
    else if (tagName.equals(GROUP_HEADER_TAG))
    {
      group.setHeader((GroupHeader) bandFactory.getElement());
      addComment(createCommentPath(bandFactory.getElement()), CommentHandler.CLOSE_TAG_COMMENT);
    }
    else if (tagName.equals(GROUP_FOOTER_TAG))
    {
      group.setFooter((GroupFooter) bandFactory.getElement());
      addComment(createCommentPath(bandFactory.getElement()), CommentHandler.CLOSE_TAG_COMMENT);
    }
    else if (tagName.equals(FIELDS_TAG))
    {
      addComment(createCommentPath(tagName), CommentHandler.CLOSE_TAG_COMMENT);
    }
    else if (tagName.equals(FIELD_TAG))
    {
      String fieldName = entityParser.decodeEntities(buffer.toString());
      group.addField(fieldName);
      addFieldComment(fieldName);
      buffer = null;
    }
    else
    {
      throw new SAXException("Invalid TagName: " + tagName + ", expected one of: "
          + GROUP_HEADER_TAG + ", "
          + GROUP_FOOTER_TAG + ", "
          + FIELDS_TAG + ", "
          + FIELD_TAG + ", "
          + getFinishTag());
    }
  }

  /**
   * Returns the group.
   *
   * @return The group.
   */
  public Group getGroup()
  {
    return group;
  }

  /**
   * Adds group fields comments for the given name.
   * 
   * @param name the field name for which to add comments.
   */
  private void addFieldComment (String name)
  {
    CommentHintPath path = new CommentHintPath();
    path.addName(ExtParserModuleInit.REPORT_DEFINITION_TAG);
    path.addName(ExtReportHandler.REPORT_DESCRIPTION_TAG);
    path.addName(ReportDescriptionHandler.GROUPS_TAG);
    path.addName(getGroup());
    path.addName(FIELDS_TAG);
    path.addName(name);
    getReport().getReportBuilderHints().putHint
        (path, CommentHandler.OPEN_TAG_COMMENT, fieldComments);
  }

  /**
   * Creates the root comment path for all groups tags. All comment
   * path will be descendents from this path.
   *   
   * @return the root path.
   */
  private CommentHintPath createRootCommentPath ()
  {
    CommentHintPath path = new CommentHintPath();
    path.addName(ExtParserModuleInit.REPORT_DEFINITION_TAG);
    path.addName(ExtReportHandler.REPORT_DESCRIPTION_TAG);
    path.addName(ReportDescriptionHandler.GROUPS_TAG);
    path.addName(getGroup());
    return path;
  }

  /**
   * Creates a new comment hint path for the given name by appending
   * it to a copy of the root comment path.
   * 
   * @param name the name of the new path segment.
   * @return the new comment path.
   */
  private CommentHintPath createCommentPath (Object name)
  {
    CommentHintPath path = createRootCommentPath();
    path.addName(name);
    return path;
  }

}
