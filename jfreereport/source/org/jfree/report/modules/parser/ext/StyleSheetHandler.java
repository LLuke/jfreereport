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
 * ------------------
 * StylesHandler.java
 * ------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StyleSheetHandler.java,v 1.9 2003/08/25 14:29:32 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext;

import org.jfree.xml.CommentHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleSheetCollection;
import org.jfree.report.util.Log;
import org.jfree.xml.ParseException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A style sheet handler. Handles the definition of a single style sheet, either
 * for an element/band or for the styles collection.
 *
 * @author Thomas Morgner.
 * @see ElementStyleSheet
 */
public class StyleSheetHandler extends AbstractExtReportParserHandler
{
  /** The 'compound-key' tag name. */
  public static final String COMPOUND_KEY_TAG = "compound-key";

  /** The 'basic-key' tag name. */
  public static final String BASIC_KEY_TAG = "basic-key";

  /** The 'extends' tag name. */
  public static final String EXTENDS_TAG = "extends";

  /** The element style sheet. */
  private ElementStyleSheet sheet;

  /** The basic style key handler. */
  private BasicStyleKeyHandler basicFactory;

  /** The style collection. */
  private StyleSheetCollection styleCollection;

  /**
   * The comment hint path is used to store xml comments in the
   * report builder hints collection.
   */
  private CommentHintPath commentKey;

  /**
   * Creates a new handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   * @param styleSheet  the style sheet.
   * @param path the path on where to search for ext-parser comments
   * in the report builder hints.
   */
  public StyleSheetHandler(final ReportParser parser, final String finishTag,
                           final ElementStyleSheet styleSheet, final CommentHintPath path)
  {
    super(parser, finishTag);
    if (styleSheet == null)
    {
      throw new NullPointerException("StyleSheet is null");
    }

    this.sheet = styleSheet;
    styleCollection = getReport().getStyleSheetCollection();
    this.commentKey = path.getInstance();
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
    if (tagName.equals(BASIC_KEY_TAG))
    {
      final String name = attrs.getValue("name");
      if (name == null)
      {
        throw new ParseException("Attribute 'name' is missing.", getParser().getLocator());
      }
      final String className = attrs.getValue("class");
      Class c = null;
      if (className != null)
      {
        try
        {
          c = getClass().getClassLoader().loadClass(className);
        }
        catch (ClassNotFoundException cnfe)
        {
          throw new ParseException
              ("The specified class for key " + name + " was not valid: " + className,
                  getParser().getLocator());
        }
        catch (Exception e)
        {
          // ignore me ...
          // if the specified class could not be loaded, the default implementation
          // will be used.
          Log.warn("Non-fatal Exception while parsing: ", e);
        }
      }

      basicFactory = new BasicStyleKeyHandler(getReportParser(), tagName, name, c);
      addComment(createCommentPath(basicFactory.getStyleKey()), CommentHandler.OPEN_TAG_COMMENT);
      getParser().pushFactory(basicFactory);
    }
    else if (tagName.equals(COMPOUND_KEY_TAG))
    {
      final String name = attrs.getValue("name");
      if (name == null)
      {
        throw new ParseException("Attribute 'name' is missing.", getParser().getLocator());
      }
      final String className = attrs.getValue("class");
      Class c = null;
      try
      {
        c = getClass().getClassLoader().loadClass(className);
      }
      catch (Exception e)
      {
        // ignore me ...
      }

      final CompoundStyleKeyHandler factory =
          new CompoundStyleKeyHandler(getReportParser(), tagName, name, c);
      final CommentHintPath path = createCommentPath(factory.getStyleKey());
      factory.setCommentPath(path);
      addComment(path, CommentHandler.OPEN_TAG_COMMENT);
      basicFactory = factory;
      getParser().pushFactory(basicFactory);
    }
    else if (tagName.equals(EXTENDS_TAG))
    {
      final String extend = attrs.getValue("name");
      if (extend != null)
      {
        final ElementStyleSheet exSheet = styleCollection.getFirst(extend);
        if (exSheet == null)
        {
          throw new ParseException("Invalid parent styleSheet, StyleSheet not defined: " + extend,
              getParser().getLocator());
        }
        addComment(createCommentPath(extend), CommentHandler.OPEN_TAG_COMMENT);
        sheet.addParent(exSheet);
      }
      else
      {
        new ParseException("Extends tag without attribute 'name'", getParser().getLocator());
      }
    }
    else
    {
      throw new SAXException("Invalid TagName: " + tagName + ", expected one of: "
          + BASIC_KEY_TAG + ", "
          + COMPOUND_KEY_TAG + ". ");
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
    // no characters expected here ...
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
    if (tagName.equals(BASIC_KEY_TAG))
    {
      addComment(createCommentPath(basicFactory.getStyleKey()), CommentHandler.CLOSE_TAG_COMMENT);
      sheet.setStyleProperty(basicFactory.getStyleKey(), basicFactory.getValue());
      basicFactory = null;
    }
    else if (tagName.equals(COMPOUND_KEY_TAG))
    {
      addComment(createCommentPath(basicFactory.getStyleKey()), CommentHandler.CLOSE_TAG_COMMENT);
      sheet.setStyleProperty(basicFactory.getStyleKey(), basicFactory.getValue());
      basicFactory = null;
    }
    else if (tagName.equals(EXTENDS_TAG))
    {
      // ignored ...
    }
    else if (tagName.equals(getFinishTag()))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException("Invalid TagName: " + tagName + ", expected one of: "
          + BASIC_KEY_TAG + ", "
          + COMPOUND_KEY_TAG + ", "
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
    final CommentHintPath path = commentKey.getInstance();
    path.addName(name);
    return path;
  }
}
