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
 * $Id: ConverterParser.java,v 1.2 2003/08/31 19:27:57 taqua Exp $
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

public class ConverterParser extends Parser
{
  private Parser base;
  private Stack currentContext;

  public ConverterParser(Parser base)
  {
    this.base = base;
    currentContext = new Stack();
  }

  public void endElement(String uri, String localName, String qName)
      throws SAXException
  {
    currentContext.pop();
    base.endElement(uri, localName, qName);
  }

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
   * <p>Application writers can use this method to redirect external
   * system identifiers to secure and/or local URIs, to look up
   * public identifiers in a catalogue, or to read an entity from a
   * database or other input source (including, for example, a dialog
   * box).</p>
   *
   * <p>If the system identifier is a URL, the SAX parser must
   * resolve it fully before reporting it to the application.</p>
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

  public Object getHelperObject(String s)
  {
    return base.getHelperObject(s);
  }

  public void setDocumentLocator(Locator locator)
  {
    base.setDocumentLocator(locator);
  }

  public Locator getLocator()
  {
    return base.getLocator();
  }

  public void pushFactory(ElementDefinitionHandler elementDefinitionHandler)
  {
    base.pushFactory(elementDefinitionHandler);
  }

  public ElementDefinitionHandler peekFactory()
  {
    return base.peekFactory();
  }

  public ElementDefinitionHandler popFactory()
  {
    return base.popFactory();
  }

  public void endDocument() throws SAXException
  {
    base.endDocument();
  }

  public void startDocument() throws SAXException
  {
    base.startDocument();
  }

  public void characters(char[] chars, int i, int i1) throws SAXException
  {
    base.characters(chars, i, i1);
  }

  public void setInitialFactory(ElementDefinitionHandler elementDefinitionHandler)
  {
    base.setInitialFactory(elementDefinitionHandler);
  }

  public ElementDefinitionHandler getInitialFactory()
  {
    return base.getInitialFactory();
  }

  public String getConfigProperty(String s)
  {
    return base.getConfigProperty(s);
  }

  public String getConfigProperty(String s, String s1)
  {
    return base.getConfigProperty(s, s1);
  }

  public void setConfigProperty(String s, String s1)
  {
    base.setConfigProperty(s, s1);
  }

  public void setHelperObject(String s, Object o)
  {
    base.setHelperObject(s, o);
  }

  public Parser getInstance()
  {
    return new ConverterParser(base);
  }

  /**
   * A proxy implementation. Forwards all calls to the base parser.
   *
   * @see Parser#getResult
   * @return the result
   */
  public Object getResult()
  {
    return base.getResult();
  }


}
