/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * -----------------------------
 * ReportDescriptionHandler.java
 * -----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportDescriptionHandler.java,v 1.9 2003/08/25 14:29:32 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext;

import org.jfree.report.Band;
import org.jfree.xml.CommentHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.ReportParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A report description handler. The report description defines the visual
 * appearance of an report and defines the various bands and elements.
 *
 * @author Thomas Morgner
 */
public class ReportDescriptionHandler extends AbstractExtReportParserHandler
{
  /** The predefined comment hint path for all root-level bands. */
  private static final CommentHintPath ROOT_BAND_PATH = new CommentHintPath(new String[]{
    ExtParserModuleInit.REPORT_DEFINITION_TAG,
    ExtReportHandler.REPORT_DESCRIPTION_TAG
  });


  /** The 'report-header' tag name. */
  public static final String REPORT_HEADER_TAG = "report-header";

  /** The 'report-footer' tag name. */
  public static final String REPORT_FOOTER_TAG = "report-footer";

  /** The 'page-header' tag name. */
  public static final String PAGE_HEADER_TAG = "page-header";

  /** The 'page-footer' tag name. */
  public static final String PAGE_FOOTER_TAG = "page-footer";

  /** The 'itemband' tag name. */
  public static final String ITEMBAND_TAG = "itemband";

  /** The 'groups' tag name. */
  public static final String GROUPS_TAG = "groups";

  /** The band handler. */
  private BandHandler bandFactory;

  /**
   * Creates a new handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   */
  public ReportDescriptionHandler(final ReportParser parser, final String finishTag)
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
  public void startElement(final String tagName, final Attributes attrs)
      throws SAXException
  {
    if (tagName.equals(REPORT_HEADER_TAG))
    {
      final Band band = getReport().getReportHeader();
      final String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      final CommentHintPath path = createCommentPath(band);
      addComment(path, CommentHandler.OPEN_TAG_COMMENT);
      bandFactory = new BandHandler(getReportParser(), tagName, band, ROOT_BAND_PATH);
      getParser().pushFactory(bandFactory);
    }
    else if (tagName.equals(REPORT_FOOTER_TAG))
    {
      final Band band = getReport().getReportFooter();
      final String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      final CommentHintPath path = createCommentPath(band);
      addComment(path, CommentHandler.OPEN_TAG_COMMENT);
      bandFactory = new BandHandler(getReportParser(), tagName, band, ROOT_BAND_PATH);
      getParser().pushFactory(bandFactory);
    }
    else if (tagName.equals(PAGE_HEADER_TAG))
    {
      final Band band = getReport().getPageHeader();
      final String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      final CommentHintPath path = createCommentPath(band);
      addComment(path, CommentHandler.OPEN_TAG_COMMENT);
      bandFactory = new BandHandler(getReportParser(), tagName, band, ROOT_BAND_PATH);
      getParser().pushFactory(bandFactory);
    }
    else if (tagName.equals(PAGE_FOOTER_TAG))
    {
      final Band band = getReport().getPageFooter();
      final String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      final CommentHintPath path = createCommentPath(band);
      addComment(path, CommentHandler.OPEN_TAG_COMMENT);
      bandFactory = new BandHandler(getReportParser(), tagName, band, ROOT_BAND_PATH);
      getParser().pushFactory(bandFactory);
    }
    else if (tagName.equals(ITEMBAND_TAG))
    {
      final Band band = getReport().getItemBand();
      final String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      final CommentHintPath path = createCommentPath(band);
      addComment(path, CommentHandler.OPEN_TAG_COMMENT);
      bandFactory = new BandHandler(getReportParser(), tagName, band, ROOT_BAND_PATH);
      getParser().pushFactory(bandFactory);
    }
    else if (tagName.equals(GROUPS_TAG))
    {
      addComment(createCommentPath(tagName), CommentHandler.OPEN_TAG_COMMENT);
      final GroupsHandler groupFactory = new GroupsHandler(getReportParser(), tagName);
      getParser().pushFactory(groupFactory);
    }
    else
    {
      throw new SAXException("Invalid TagName: " + tagName + ", expected one of: "
          + REPORT_HEADER_TAG + ", "
          + REPORT_FOOTER_TAG + ", "
          + PAGE_HEADER_TAG + ", "
          + PAGE_FOOTER_TAG + ", "
          + ITEMBAND_TAG + ", "
          + GROUPS_TAG);
    }

  }

  /**
   * Callback to indicate that some character data has been read.
   *
   * @param ch  the character array.
   * @param start  the start index for the characters.
   * @param length  the length of the character sequence.
   */
  public void characters(final char[] ch, final int start, final int length)
  {
    // ignore the characters ...
  }

  /**
   * Callback to indicate that an XML element end tag has been read by the parser.
   *
   * @param tagName  the tag name.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void endElement(final String tagName)
      throws SAXException
  {
    if (tagName.equals(REPORT_HEADER_TAG))
    {
      addComment(createCommentPath(bandFactory.getElement()), CommentHandler.CLOSE_TAG_COMMENT);
    }
    else if (tagName.equals(REPORT_FOOTER_TAG))
    {
      addComment(createCommentPath(bandFactory.getElement()), CommentHandler.CLOSE_TAG_COMMENT);
    }
    else if (tagName.equals(PAGE_HEADER_TAG))
    {
      addComment(createCommentPath(bandFactory.getElement()), CommentHandler.CLOSE_TAG_COMMENT);
    }
    else if (tagName.equals(PAGE_FOOTER_TAG))
    {
      addComment(createCommentPath(bandFactory.getElement()), CommentHandler.CLOSE_TAG_COMMENT);
    }
    else if (tagName.equals(ITEMBAND_TAG))
    {
      addComment(createCommentPath(bandFactory.getElement()), CommentHandler.CLOSE_TAG_COMMENT);
    }
    else if (tagName.equals(GROUPS_TAG))
    {
      addComment(createCommentPath(tagName), CommentHandler.CLOSE_TAG_COMMENT);
    }
    else if (tagName.equals(getFinishTag()))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException("Invalid TagName: " + tagName + ", expected one of: "
          + REPORT_HEADER_TAG + ", "
          + REPORT_FOOTER_TAG + ", "
          + PAGE_HEADER_TAG + ", "
          + PAGE_FOOTER_TAG + ", "
          + ITEMBAND_TAG + ", "
          + GROUPS_TAG + ", "
          + getFinishTag());
    }
  }

  /**
   * Creates a new comment hint path for the given name by appending
   * it to a copy of the current path.
   *
   * @param name the name of the new path segment.
   * @return the new comment path.
   */
  private CommentHintPath createCommentPath(final Object name)
  {
    final CommentHintPath path = new CommentHintPath();
    path.addName(ExtParserModuleInit.REPORT_DEFINITION_TAG);
    path.addName(ExtReportHandler.REPORT_DESCRIPTION_TAG);
    path.addName(name);
    return path;
  }
}
