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
 * $Id: GroupHandler.java,v 1.9 2003/06/27 14:25:18 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext;

import com.jrefinery.report.Band;
import com.jrefinery.report.Group;
import com.jrefinery.report.GroupFooter;
import com.jrefinery.report.GroupHeader;
import com.jrefinery.report.util.CharacterEntityParser;
import org.jfree.xml.ElementDefinitionHandler;
import org.jfree.xml.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A group handler. Handles the definition of a single group.
 *
 * @author Thomas Morgner.
 */
public class GroupHandler implements ElementDefinitionHandler
{
  /** The 'fields' tag name. */
  public static final String FIELDS_TAG = "fields";

  /** The 'field' tag name. */
  public static final String FIELD_TAG = "field";

  /** The 'group-header' tag name. */
  public static final String GROUP_HEADER_TAG = "group-header";

  /** The 'group-footer' tag name. */
  public static final String GROUP_FOOTER_TAG = "group-footer";

  /** The parser. */
  private Parser parser;

  /** The finish tag. */
  private String finishTag;

  /** The group. */
  private Group group;

  /** A buffer. */
  private StringBuffer buffer;

  /** The band handler. */
  private BandHandler bandFactory;

  /** A character entity parser. */
  private CharacterEntityParser entityParser;

  /**
   * Creates a new handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   * @param group  the group.
   */
  public GroupHandler(final Parser parser, final String finishTag, final Group group)
  {
    this.entityParser = CharacterEntityParser.createXMLEntityParser();
    this.parser = parser;
    this.finishTag = finishTag;
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
      final Band band = new GroupHeader();
      final String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      bandFactory = new BandHandler(getParser(), tagName, band);
      getParser().pushFactory(bandFactory);
    }
    else if (tagName.equals(GROUP_FOOTER_TAG))
    {
      final Band band = new GroupFooter();
      final String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      bandFactory = new BandHandler(getParser(), tagName, band);
      getParser().pushFactory(bandFactory);
    }
    else if (tagName.equals(FIELDS_TAG))
    {
      // unused
    }
    else if (tagName.equals(FIELD_TAG))
    {
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
    if (tagName.equals(finishTag))
    {
      getParser().popFactory().endElement(tagName);
    }
    else if (tagName.equals(GROUP_HEADER_TAG))
    {
      group.setHeader((GroupHeader) bandFactory.getElement());
    }
    else if (tagName.equals(GROUP_FOOTER_TAG))
    {
      group.setFooter((GroupFooter) bandFactory.getElement());
    }
    else if (tagName.equals(FIELDS_TAG))
    {
      // ignore ...
    }
    else if (tagName.equals(FIELD_TAG))
    {
      group.addField(entityParser.decodeEntities(buffer.toString()));
      buffer = null;
    }
    else
    {
      throw new SAXException("Invalid TagName: " + tagName + ", expected one of: "
          + GROUP_HEADER_TAG + ", "
          + GROUP_FOOTER_TAG + ", "
          + FIELDS_TAG + ", "
          + FIELD_TAG + ", "
          + finishTag);
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
   * Returns the parser.
   *
   * @return The parser.
   */
  public Parser getParser()
  {
    return parser;
  }
}
