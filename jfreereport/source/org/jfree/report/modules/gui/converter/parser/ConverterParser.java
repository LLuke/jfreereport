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
 * ------------------------------
 * ConverterParser.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ConverterParser.java,v 1.3 2003/08/31 21:06:09 taqua Exp $
 *
 * Changes
 * -------------------------
 * 25-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.converter.parser;

import java.util.Stack;

import org.jfree.xml.ElementDefinitionHandler;
import org.jfree.xml.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * The ConverterParser is a filtering proxy implementation that uses the
 * mappings defined in this package to modify the parsed attribute values 
 * on the fly into the new values.
 * 
 * @author Thomas Morgner
 */
public class ConverterParser extends Parser
{
  /** The backend parser. */
  private Parser base;
  /** the context collection used to create the correct mappings. */
  private Stack currentContext;

  /**
   * Creates a new ConverterParser using the given parser as backend.
   * 
   * @param base the backend parser that will do all the work.
   */
  public ConverterParser(Parser base)
  {
    this.base = base;
    currentContext = new Stack();
  }

  /**
   * Receive notification of the end of the document.
   * Updates the context and forwards the event to the base parser.
   *
   * @exception SAXException Any SAX exception, possibly wrapping another exception.
   *
   * @param uri  the URI.
   * @param localName  the element type name.
   * @param qName  the name.
   * @see org.xml.sax.ContentHandler#endDocument
   */
  public void endElement(String uri, String localName, String qName)
      throws SAXException
  {
    currentContext.pop();
    base.endElement(uri, localName, qName);
  }

  /**
   * Receive notification of the start of an element.
   * Updates the context and forwards the event to the base parser.
   *
   * @param uri  the URI.
   * @param localName  the element type name.
   * @param qName  the name.
   * @param attributes  the specified or defaulted attributes.
   *
   * @exception SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.ContentHandler#startElement
   */
  public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
      throws SAXException
  {
    TranslationTableFactory.ContextRule rule = null;
    if (currentContext.isEmpty() == false)
    {
      Object o = currentContext.peek();
      if (o instanceof TranslationTableFactory.ContextRule)
      {
        rule = (TranslationTableFactory.ContextRule) o;
      }
    }
    rule = TranslationTableFactory.getInstance().buildContext(rule, qName);
    if (rule == null)
    {
      // do not translate ..
      currentContext.push(qName);
      base.startElement(uri, localName, qName, attributes);
    }
    else
    {
      // do translate ..
      currentContext.push(rule);
      TranslationTable table =
          TranslationTableFactory.getInstance().getTranslationTable(rule);
      base.startElement(uri, localName, qName, new ConverterAttributes(attributes, table));
    }
  }

  /**
   * Allow the application to resolve external entities.
   *
   * <p>The Parser will call this method before opening any external
   * entity except the top-level document entity (including the
   * external DTD subset, external entities referenced within the
   * DTD, and external entities referenced within the document
   * element): the application may request that the parser resolve
   * the entity itself, that it use an alternative URI, or that it
   * use an entirely different input source.</p>
   *
   * <p>If the system identifier is a URL, the SAX parser must
   * resolve it fully before reporting it to the application.</p>
   *
   * <p>The official SAX implementation declares that it also throws an
   * IOException, while the JDK1.4 SAX2 implementation doesn't.
   * We catch all exceptions here and map them into the SAXException
   * to resolve that issue.</p>
   *
   * @param publicId The public identifier of the external entity
   *        being referenced, or null if none was supplied.
   * @param systemId The system identifier of the external entity
   *        being referenced.
   * @return An InputSource object describing the new input source,
   *         or null to request that the parser open a regular
   *         URI connection to the system identifier.
   * @exception SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see InputSource
   */
  public InputSource resolveEntity(String publicId,
                                   String systemId)
      throws SAXException
  {
    try
    {
      return base.resolveEntity(publicId, systemId);
    }
    catch (Exception oe)
    {
      // this one drives me crazy, in JDK 1.4 the IOException
      // is not defined, but in the official SAX sources it is
      // defined. Now depending on which version you compile it
      // gives an differen error. I HATE THIS!
      throw new SAXException(oe);
    }
  }

  /**
   * Receive notification of a notation declaration event.
   *
   * <p>It is up to the application to record the notation for later
   * reference, if necessary.</p>
   *
   * <p>At least one of publicId and systemId must be non-null.
   * If a system identifier is present, and it is a URL, the SAX
   * parser must resolve it fully before passing it to the
   * application through this event.</p>
   *
   * <p>There is no guarantee that the notation declaration will be
   * reported before any unparsed entities that use it.</p>
   *
   * @param name The notation name.
   * @param publicId The notation's public identifier, or null if
   *        none was given.
   * @param systemId The notation's system identifier, or null if
   *        none was given.
   * @exception SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see #unparsedEntityDecl
   */
  public void notationDecl(String name,
                           String publicId,
                           String systemId)
      throws SAXException
  {
    base.notationDecl(name, publicId, systemId);
  }

  /**
   * Receive notification of an unparsed entity declaration event.
   *
   * <p>Note that the notation name corresponds to a notation
   * reported by the {@link #notationDecl notationDecl} event.
   * It is up to the application to record the entity for later
   * reference, if necessary.</p>
   *
   * <p>If the system identifier is a URL, the parser must resolve it
   * fully before passing it to the application.</p>
   *
   * @exception SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @param name The unparsed entity's name.
   * @param publicId The entity's public identifier, or null if none
   *        was given.
   * @param systemId The entity's system identifier.
   * @param notationName name The name of the associated notation.
   * @see #notationDecl
   */
  public void unparsedEntityDecl(String name,
                                 String publicId,
                                 String systemId,
                                 String notationName)
      throws SAXException
  {
    base.unparsedEntityDecl(name, publicId, systemId, notationName);
  }

  /**
   * Receive notification of a warning.
   *
   * <p>SAX parsers will use this method to report conditions that
   * are not errors or fatal errors as defined by the XML 1.0
   * recommendation.  The default behaviour is to take no action.</p>
   *
   * <p>The SAX parser must continue to provide normal parsing events
   * after invoking this method: it should still be possible for the
   * application to process the document through to the end.</p>
   *
   * <p>Filters may use this method to report other, non-XML warnings
   * as well.</p>
   *
   * @param exception The warning information encapsulated in a
   *                  SAX parse exception.
   * @exception SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see SAXParseException
   */
  public void warning(SAXParseException exception)
      throws SAXException
  {
    base.warning(exception);
  }

  /**
   * Receive notification of a recoverable error.
   *
   * <p>This corresponds to the definition of "error" in section 1.2
   * of the W3C XML 1.0 Recommendation.  For example, a validating
   * parser would use this callback to report the violation of a
   * validity constraint.  The default behaviour is to take no
   * action.</p>
   *
   * <p>The SAX parser must continue to provide normal parsing events
   * after invoking this method: it should still be possible for the
   * application to process the document through to the end.  If the
   * application cannot do so, then the parser should report a fatal
   * error even if the XML 1.0 recommendation does not require it to
   * do so.</p>
   *
   * <p>Filters may use this method to report other, non-XML errors
   * as well.</p>
   *
   * @param exception The error information encapsulated in a
   *                  SAX parse exception.
   * @exception SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see SAXParseException
   */
  public void error(SAXParseException exception)
      throws SAXException
  {
    base.error(exception);
  }

  /**
   * Receive notification of a non-recoverable error.
   *
   * <p>This corresponds to the definition of "fatal error" in
   * section 1.2 of the W3C XML 1.0 Recommendation.  For example, a
   * parser would use this callback to report the violation of a
   * well-formedness constraint.</p>
   *
   * <p>The application must assume that the document is unusable
   * after the parser has invoked this method, and should continue
   * (if at all) only for the sake of collecting addition error
   * messages: in fact, SAX parsers are free to stop reporting any
   * other events once this method has been invoked.</p>
   *
   * @param exception The error information encapsulated in a
   *                  SAX parse exception.
   * @exception SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see SAXParseException
   */
  public void fatalError(SAXParseException exception)
      throws SAXException
  {
    base.fatalError(exception);
  }

  /**
   * Returns a helper object.
   *
   * @param key  the key.
   *
   * @return The object.
   */
  public Object getHelperObject(String key)
  {
    return base.getHelperObject(key);
  }

  /**
   * Receive an object for locating the origin of SAX document events.
   *
   * The locator allows the application to determine the end position of
   * any document-related event, even if the parser is not reporting an
   * error. Typically, the application will use this information for
   * reporting its own errors (such as character content that does not
   * match an application's business rules). The information returned by
   * the locator is probably not sufficient for use with a search engine.
   *
   * @param locator  the locator.
   */
  public void setDocumentLocator(Locator locator)
  {
    base.setDocumentLocator(locator);
  }

  /**
   * Returns the current locator.
   *
   * @return the locator.
   */
  public Locator getLocator()
  {
    return base.getLocator();
  }

  /**
   * Pushes a handler onto the stack.
   *
   * @param elementDefinitionHandler  the handler.
   */
  public void pushFactory(ElementDefinitionHandler elementDefinitionHandler)
  {
    base.pushFactory(elementDefinitionHandler);
  }

  /**
   * Reads a handler off the stack without removing it.
   *
   * @return The handler.
   */
  public ElementDefinitionHandler peekFactory()
  {
    return base.peekFactory();
  }

  /**
   * Pops a handler from the stack.
   *
   * @return The handler.
   */
  public ElementDefinitionHandler popFactory()
  {
    return base.popFactory();
  }

  /**
   * Receive notification of the end of the document.
   * Forwards the event to the base parser implementation.
   *
   * @exception SAXException Any SAX exception, possibly wrapping another exception.
   *
   * @see org.xml.sax.ContentHandler#endDocument
   */
  public void endDocument() throws SAXException
  {
    base.endDocument();
  }

  /**
   * Receive notification of the beginning of the document.
   * Forwards the event to the base parser implementation.
   *
   * @exception SAXException Any SAX exception, possibly wrapping another exception.
   * @see org.xml.sax.ContentHandler#startDocument
   */
  public void startDocument() throws SAXException
  {
    base.startDocument();
  }

  /**
   * Receive notification of character data inside an element.
   * Forwards the event to the base parser implementation.
   *
   * @param chars  the characters.
   * @param start  the start position in the character array.
   * @param length  the number of characters to use from the character array.
   *
   * @exception SAXException Any SAX exception, possibly wrapping another exception.
   * @see org.xml.sax.ContentHandler#characters
   */
  public void characters(char[] chars, int start, int length) throws SAXException
  {
    base.characters(chars, start, length);
  }

  /**
   * Sets the initial handler.
   *
   * @param elementDefinitionHandler  the initial handler.
   */
  public void setInitialFactory(ElementDefinitionHandler elementDefinitionHandler)
  {
    base.setInitialFactory(elementDefinitionHandler);
  }

  /**
   * Returns the initial handler.
   *
   * @return The initial handler.
   */
  public ElementDefinitionHandler getInitialFactory()
  {
    return base.getInitialFactory();
  }

  /**
   * Returns the configuration property with the specified key.
   *
   * @param key  the property key.
   *
   * @return the property value.
   */
  public String getConfigProperty(String key)
  {
    return base.getConfigProperty(key);
  }

  /**
   * Returns the configuration property with the specified key (or the specified default value
   * if there is no such property).
   * <p>
   * If the property is not defined in this configuration, the code will lookup the property in
   * the parent configuration.
   *
   * @param key  the property key.
   * @param defaultValue  the default value.
   *
   * @return the property value.
   */
  public String getConfigProperty(String key, String defaultValue)
  {
    return base.getConfigProperty(key, defaultValue);
  }

  /**
   * Sets a parser configuration value.
   *
   * @param key  the key.
   * @param value  the value.
   */
  public void setConfigProperty(String key, String value)
  {
    base.setConfigProperty(key, value);
  }

  /**
   * Sets a helper object.
   *
   * @param key  the key.
   * @param value  the value.
   */
  public void setHelperObject(String key, Object value)
  {
    base.setHelperObject(key, value);
  }

  /**
   * Returns a new instance of the parser.
   *
   * @return a new instance of the parser.
   */
  public Parser getInstance()
  {
    return new ConverterParser(base);
  }

  /**
   * Returns the parsed result object after the parsing is complete. Calling
   * this function during the parsing is undefined and may result in an
   * IllegalStateException.
   * <p>
   * This is a proxy implementation. Forwards all calls to the base parser.
   *
   * @see Parser#getResult
   * @return the result
   */
  public Object getResult()
  {
    return base.getResult();
  }


}
