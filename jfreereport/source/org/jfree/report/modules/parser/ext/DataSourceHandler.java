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
 * ----------------------
 * DataSourceHandler.java
 * ----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DataSourceHandler.java,v 1.5 2003/08/20 17:24:35 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext;

import org.jfree.report.filter.DataSource;
import org.jfree.report.modules.parser.base.CommentHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceCollector;
import org.jfree.xml.ParseException;
import org.jfree.xml.Parser;
import org.jfree.xml.factory.objects.ObjectDescription;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A datasource handler. Handles the creation of a DataSource or DataFilter.
 * This allows a fine grained element content processing. Templates can be used
 * to create predefined DataSource compositions.
 *
 * @author Thomas Morgner.
 * @see DataSource
 * @see org.jfree.report.filter.DataFilter
 * @see TemplateHandler
 */
public class DataSourceHandler extends CompoundObjectHandler
{
  /** The datasource tag name. */
  public static final String DATASOURCE_TAG = "datasource";

  /** The handler. */
  private DataSourceHandler dataSourceHandler;

  /**
   * Creates a new handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   * @param type  the datasource type.
   * @param commentKey the path on where to search for ext-parser comments
   * in the report builder hints.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public DataSourceHandler(final ReportParser parser, final String finishTag,
                           final String type, final CommentHintPath commentKey)
      throws SAXException
  {
    super(parser, finishTag, lookupObjectDescription(parser, type), commentKey);
  }

  /**
   * Looks up the object description for the data source type and throws an
   * exception if the data source type is not known.
   *
   * @param parser  the parser.
   * @param type  the data source type.
   *
   * @return The object description.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  private static ObjectDescription lookupObjectDescription(final Parser parser, final String type)
      throws SAXException
  {
    final DataSourceCollector fc = (DataSourceCollector) parser.getHelperObject(
        ParserConfigHandler.DATASOURCE_FACTORY_TAG);
    final ObjectDescription od = fc.getDataSourceDescription(type);
    if (od == null)
    {
      throw new ParseException("The specified DataSource type is not defined");
    }
    return od;
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
    if (tagName.equals(DATASOURCE_TAG) == false)
    {
      super.startElement(tagName, attrs);
      return;
    }

    final String typeName = attrs.getValue("type");
    if (typeName == null)
    {
      throw new ParseException("The datasource type must be specified", getParser().getLocator());
    }
    CommentHintPath path = createCommentKey(tagName);
    addComment(path, CommentHandler.OPEN_TAG_COMMENT);
    dataSourceHandler = new DataSourceHandler(getReportParser(), tagName, typeName, path);
    getParser().pushFactory(dataSourceHandler);
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
    if (tagName.equals(DATASOURCE_TAG) == false)
    {
      super.endElement(tagName);
      return;
    }

    if (dataSourceHandler == null)
    {
      super.endElement(tagName);
    }
    else
    {
      final DataSource ds = (DataSource) dataSourceHandler.getValue();
      getTargetObjectDescription().setParameter("dataSource", ds);
      CommentHintPath path = createCommentKey(tagName);
      addComment(path, CommentHandler.CLOSE_TAG_COMMENT);
      dataSourceHandler = null;
    }
  }

}
