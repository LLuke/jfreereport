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
 * MultiplexRootElementHandler.java
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
package org.jfree.report.modules.factories.common;

import java.io.IOException;
import java.util.HashMap;

import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.util.Log;
import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Creation-Date: 08.04.2006, 13:29:54
 *
 * @author Thomas Morgner
 */
public class MultiplexRootElementHandler extends RootXmlReadHandler
{
  private static class RootEntityResolver implements EntityResolver
  {
    private ParserEntityResolver entityResolver;
    private String publicId;
    private String systemId;

    public RootEntityResolver()
    {
      entityResolver = ParserEntityResolver.getDefaultResolver();
    }

    public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException, IOException
    {
      this.publicId = publicId;
      this.systemId = systemId;
      return entityResolver.resolveEntity(publicId, systemId);
    }

    public String getPublicId()
    {
      return publicId;
    }

    public String getSystemId()
    {
      return systemId;
    }
  }

  private boolean firstCall;
  private DefaultXmlDocumentInfo documentInfo;
  private XmlFactoryModule[] rootHandlers;
  private RootEntityResolver entityResolver;
  private boolean xmlnsUrisNotAvailable;

  public MultiplexRootElementHandler
          (final ResourceManager manager,
          final ResourceKey source,
          final long version,
          final XmlFactoryModule[] rootHandlers)
  {
    super(manager, source, version);
    this.documentInfo = new DefaultXmlDocumentInfo();
    this.entityResolver = new RootEntityResolver();
    this.rootHandlers = (XmlFactoryModule[]) rootHandlers.clone();
    this.firstCall = true;
  }

  public boolean isXmlnsUrisNotAvailable()
  {
    return xmlnsUrisNotAvailable;
  }

  public void setXmlnsUrisNotAvailable(final boolean xmlnsUrisNotAvailable)
  {
    this.xmlnsUrisNotAvailable = xmlnsUrisNotAvailable;
  }

  public EntityResolver getEntityResolver()
  {
    return entityResolver;
  }

  protected XmlFactoryModule[] getRootHandlers()
  {
    return rootHandlers;
  }

  public boolean isFirstCall()
  {
    return firstCall;
  }

  public DefaultXmlDocumentInfo getDocumentInfo()
  {
    return documentInfo;
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
  public void startElement(final String uri,
                           final String localName,
                           final String qName,
                           Attributes attributes) throws SAXException
  {
    if (firstCall)
    {
      // build the document info and select the root handler that will
      // deal with the document content.
      documentInfo.setPublicDTDId(entityResolver.getPublicId());
      documentInfo.setSystemDTDId(entityResolver.getSystemId());
      documentInfo.setRootElement(localName);
      documentInfo.setRootElementNameSpace(uri);

      final int length = attributes.getLength();
      final HashMap prefixes = new HashMap();
      for (int i = 0; i < length; i++)
      {
        final String attrQName = attributes.getQName(i);
        final String nsuri = attributes.getValue(i);
        if ("xmlns".equals(attrQName))
        {
          documentInfo.setDefaultNameSpace(nsuri);
          prefixes.put ("", nsuri);
        }
        else if (attrQName.startsWith("xmlns:"))
        {
          final String prefix = attrQName.substring(6);
          prefixes.put (prefix, nsuri);
        }
      }

      documentInfo.setNamespaces(prefixes);

      if (documentInfo.getDefaultNameSpace() == null)
      {
        documentInfo.setDefaultNameSpace("");
      }

      if (xmlnsUrisNotAvailable)
      {
        attributes = new FixNamespaceUriAttributes(uri, attributes);
      }

      // ok, now find the best root handler and start parsing ...
      XmlFactoryModule bestRootHandler = null;
      int bestRootHandlerWeight = -1;
      for (int i = 0; i < rootHandlers.length; i++)
      {
        final XmlFactoryModule rootHandler = rootHandlers[i];
        final int weight = rootHandler.getDocumentSupport(documentInfo);
        if (weight > bestRootHandlerWeight)
        {
          bestRootHandler = rootHandler;
          bestRootHandlerWeight = weight;
        }
      }
      if (bestRootHandlerWeight < 0 || bestRootHandler == null)
      {
        throw new SAXException("No suitable root handler known for this document.");
      }
      final XmlReadHandler readHandler =
              bestRootHandler.createReadHandler(documentInfo);
      if (readHandler == null)
      {
        throw new SAXException("Unable to create the root handler." + bestRootHandler);
      }
      installRootHandler(readHandler, uri, localName, attributes);
      firstCall = false;
      return;
    }
    else
    {
      if (xmlnsUrisNotAvailable)
      {
        attributes = new FixNamespaceUriAttributes(uri, attributes);
      }
    }
    super.startElement(uri, localName, qName, attributes);
  }


  /**
   * Returns the object for this element or null, if this element does not
   * create an object.
   *
   * @return the object.
   * @throws XmlReaderException if there is a parsing error.
   */
  public Object getObject() throws SAXException
  {
    return null;
  }
}
