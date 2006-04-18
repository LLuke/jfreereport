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
 * AttributeExpressionReadHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.factories.report.flow;

import java.util.Map;

import org.jfree.report.modules.factories.common.MultiplexRootElementHandler;
import org.jfree.report.modules.factories.common.StringReadHandler;
import org.jfree.report.modules.factories.common.XmlReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Creation-Date: 09.04.2006, 13:40:08
 *
 * @author Thomas Morgner
 */
public class AttributeExpressionReadHandler
        extends AbstractExpressionReadHandler
{
  private StringReadHandler nameReadHandler;
  private StringReadHandler namespacePrefixReadHandler;
  private StringReadHandler namespaceUriReadHandler;
  private String attributeName;
  private String namespace;

  public AttributeExpressionReadHandler()
  {
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws SAXException       if there is a parsing error.
   * @throws XmlReaderException if there is a reader error.
   */
  protected XmlReadHandler getHandlerForChild(final String uri,
                                              final String tagName,
                                              final Attributes atts)
          throws SAXException
  {
    if (isSameNamespace(uri) == false)
    {
      return null;
    }
    if (tagName.equals("attribute-name"))
    {
      nameReadHandler = new StringReadHandler();
      return nameReadHandler;
    }
    if (tagName.equals("attribute-namespace-uri"))
    {
      namespaceUriReadHandler = new StringReadHandler();
      return namespaceUriReadHandler;
    }
    if (tagName.equals("attribute-namespace-prefix"))
    {
      namespacePrefixReadHandler = new StringReadHandler();
      return namespacePrefixReadHandler;
    }
    return super.getHandlerForChild(uri, tagName, atts);
  }

  /**
   * Done parsing.
   *
   * @throws SAXException       if there is a parsing error.
   * @throws XmlReaderException if there is a reader error.
   */
  protected void doneParsing() throws SAXException
  {
    super.doneParsing();
    if (nameReadHandler == null)
    {
      throw new SAXException("Required element 'attribute-name' is missing.");
    }

    attributeName = nameReadHandler.getResult();

    if (namespaceUriReadHandler != null)
    {
      namespace = namespaceUriReadHandler.getResult();
    }
    final MultiplexRootElementHandler mpr =
            (MultiplexRootElementHandler) getRootHandler();
    if (namespace == null)
    {
      final String prefix = namespacePrefixReadHandler.getResult();
      final Map namespaces = mpr.getDocumentInfo().getNamespaces();
      namespace = (String) namespaces.get(prefix);
    }
    if (namespace == null)
    {
      namespace = mpr.getDocumentInfo().getDefaultNameSpace();
    }

  }

  public String getNamespace()
  {
    return namespace;
  }

  public String getAttributeName()
  {
    return attributeName;
  }
}