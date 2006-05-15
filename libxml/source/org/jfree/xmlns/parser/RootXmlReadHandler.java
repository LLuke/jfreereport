/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * RootXmlReadHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: RootXmlReadHandler.java,v 1.1 2006/04/18 11:45:16 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.xmlns.parser;

import java.util.HashMap;
import java.util.Stack;

import org.jfree.resourceloader.DependencyCollector;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.util.DefaultConfiguration;
import org.jfree.xmlns.parser.CommentHandler;
import org.jfree.xmlns.parser.ParseException;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/** A base root SAX handler. */
public class RootXmlReadHandler extends DefaultHandler
{
  /** Storage for the parser configuration. */
  private DefaultConfiguration parserConfiguration;

  /** The DocumentLocator can be used to resolve the current parse position. */
  private Locator documentLocator;

  /** The current handlers. */
  private Stack currentHandlers;

  /** ??. */
  private Stack outerScopes;

  /** The root handler. */
  private XmlReadHandler rootHandler;

  /** The object registry. */
  private HashMap objectRegistry;

  private boolean rootHandlerInitialized;

  /** The current comment handler used to receive xml comments. */
  private CommentHandler commentHandler;

  private DependencyCollector dependencyCollector;
  private ResourceKey source;
  private ResourceManager manager;

  /** Creates a new root SAX handler. */
  public RootXmlReadHandler(final ResourceManager manager,
                            final ResourceKey source,
                            final long version)
  {
    if (manager == null)
    {
      throw new NullPointerException();
    }
    if (source == null)
    {
      throw new NullPointerException();
    }
    this.manager = manager;
    this.source = source;
    this.dependencyCollector = new DependencyCollector(source, version);
    this.objectRegistry = new HashMap();
    this.parserConfiguration = new DefaultConfiguration();
    this.commentHandler = new CommentHandler();
  }

  public ResourceManager getResourceManager()
  {
    return manager;
  }

  public ResourceKey getSource()
  {
    return source;
  }

  public DependencyCollector getDependencyCollector()
  {
    return dependencyCollector;
  }

  /**
   * Returns the comment handler that is used to collect comments.
   *
   * @return the comment handler.
   */
  public CommentHandler getCommentHandler()
  {
    return this.commentHandler;
  }

  public DefaultConfiguration getParserConfiguration()
  {
    return parserConfiguration;
  }

  /**
   * Receive an object for locating the origin of SAX document events.
   * <p/>
   * The documentLocator allows the application to determine the end position of
   * any document-related event, even if the parser is not reporting an error.
   * Typically, the application will use this information for reporting its own
   * errors (such as character content that does not match an application's
   * business rules). The information returned by the documentLocator is
   * probably not sufficient for use with a search engine.
   *
   * @param locator the documentLocator.
   */
  public void setDocumentLocator(final Locator locator)
  {
    this.documentLocator = locator;
  }

  /**
   * Returns the current documentLocator.
   *
   * @return the documentLocator.
   */
  public Locator getDocumentLocator()
  {
    return this.documentLocator;
  }

  /**
   * Adds an object to the registry.
   *
   * @param key   the key.
   * @param value the object.
   */
  public void setHelperObject(final String key, final Object value)
  {
    if (value == null)
    {
      this.objectRegistry.remove(key);
    }
    else
    {
      this.objectRegistry.put(key, value);
    }
  }

  /**
   * Returns an object from the registry.
   *
   * @param key the key.
   * @return The object.
   */
  public Object getHelperObject(final String key)
  {
    return this.objectRegistry.get(key);
  }

  /**
   * Sets the root SAX handler.
   *
   * @param handler the SAX handler.
   */
  protected void setRootHandler(final XmlReadHandler handler)
  {
    if (handler == null)
    {
      throw new NullPointerException();
    }
    this.rootHandler = handler;
    this.rootHandlerInitialized = false;
  }

  /**
   * Returns the root SAX handler.
   *
   * @return the root SAX handler.
   */
  protected XmlReadHandler getRootHandler()
  {
    return this.rootHandler;
  }

  /**
   * Start a new handler stack and delegate to another handler.
   *
   * @param handler the handler.
   * @param tagName the tag name.
   * @param attrs   the attributes.
   * @throws SAXException       if there is a problem with the parser.
   */
  public void recurse(final XmlReadHandler handler,
                      final String uri,
                      final String tagName,
                      final Attributes attrs)
          throws SAXException
  {
    if (handler == null)
    {
      throw new NullPointerException();
    }

    this.outerScopes.push(this.currentHandlers);
    this.currentHandlers = new Stack();
    this.currentHandlers.push(handler);
    handler.startElement(uri, tagName, attrs);

  }

  /**
   * Delegate to another handler.
   *
   * @param handler the new handler.
   * @param tagName the tag name.
   * @param attrs   the attributes.
   * @throws SAXException       if there is a problem with the parser.
   */
  public void delegate(final XmlReadHandler handler,
                       final String uri,
                       final String tagName,
                       final Attributes attrs)
          throws SAXException
  {
    if (handler == null)
    {
      throw new NullPointerException();
    }
    this.currentHandlers.push(handler);
    handler.init(this, uri, tagName);
    handler.startElement(uri, tagName, attrs);
  }

  /**
   * Hand control back to the previous handler.
   *
   * @param tagName the tagname.
   * @throws SAXException       if there is a problem with the parser.
   */
  public void unwind(final String uri, final String tagName)
          throws SAXException
  {
    // remove current handler from stack ..
    this.currentHandlers.pop();
    if (this.currentHandlers.isEmpty() && !this.outerScopes.isEmpty())
    {
      // if empty, but "recurse" had been called, then restore the old handler stack ..
      // but do not end the recursed element ..
      this.currentHandlers = (Stack) this.outerScopes.pop();
    }
    else if (!this.currentHandlers.isEmpty())
    {
      // if there are some handlers open, close them too (these handlers must be delegates)..
      getCurrentHandler().endElement(uri, tagName);
    }
  }

  /**
   * Returns the current handler.
   *
   * @return The current handler.
   */
  protected XmlReadHandler getCurrentHandler()
  {
    return (XmlReadHandler) this.currentHandlers.peek();
  }

  /**
   * Starts processing a document.
   *
   * @throws SAXException not in this implementation.
   */
  public void startDocument() throws SAXException
  {
    this.outerScopes = new Stack();
    this.currentHandlers = new Stack();
    if (rootHandler != null)
    {
      // When dealing with the multiplexing beast, we cant define a
      // root handler unless we've seen the first element and all its
      // namespace declarations ...
      this.currentHandlers.push(this.rootHandler);
    }
  }

  /**
   * Starts processing an element.
   *
   * @param uri        the URI.
   * @param localName  the local name.
   * @param qName      the qName.
   * @param attributes the attributes.
   * @throws SAXException if there is a parsing problem.
   */
  public void startElement(final String uri, final String localName,
                           final String qName, final Attributes attributes)
          throws SAXException
  {
    if (rootHandlerInitialized == false)
    {
      rootHandler.init(this, uri, localName);
      rootHandlerInitialized = true;
    }

    getCurrentHandler().startElement(uri, localName, attributes);
  }

  protected void installRootHandler(final XmlReadHandler handler,
                                    final String uri,
                                    final String localName,
                                    final Attributes attributes)
          throws SAXException
  {
    if (handler == null)
    {
      throw new NullPointerException();
    }
    this.rootHandler = handler;
    this.rootHandler.init(this, uri, localName);
    this.currentHandlers.push(handler);
    this.rootHandlerInitialized = true;
    this.rootHandler.startElement(uri, localName, attributes);
  }

  /**
   * Process character data.
   *
   * @param ch     the character buffer.
   * @param start  the start index.
   * @param length the length of the character data.
   * @throws SAXException if there is a parsing error.
   */
  public void characters(final char[] ch, final int start, final int length)
          throws SAXException
  {
    try
    {
      getCurrentHandler().characters(ch, start, length);
    }
    catch (SAXException se)
    {
      throw se;
    }
    catch (Exception e)
    {
      throw new ParseException(e, getDocumentLocator());
    }
  }

  /**
   * Finish processing an element.
   *
   * @param uri       the URI.
   * @param localName the local name.
   * @param qName     the qName.
   * @throws SAXException if there is a parsing error.
   */
  public void endElement(final String uri,
                         final String localName,
                         final String qName)
          throws SAXException
  {
    getCurrentHandler().endElement(uri, localName);
  }

  public Object getResult() throws SAXException
  {
    if (this.rootHandler != null)
    {
      return this.rootHandler.getObject();
    }
    return null;
  }
}
