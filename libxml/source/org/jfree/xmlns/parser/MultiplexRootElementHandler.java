/**
 * =========================================
 * LibXML : a free Java layouting library
 * =========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libxml/
 *
 * (C) Copyright 2006, by Object Refinery Ltd, Pentaho Corporation and Contributors.
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
 *
 * ------------
 * $Id: MultiplexRootElementHandler.java,v 1.5 2006/12/30 12:42:17 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.xmlns.parser;

import java.io.IOException;

import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;
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

    public ParserEntityResolver getEntityResolver()
    {
      return entityResolver;
    }
  }

  private XmlFactoryModule[] rootHandlers;
  private RootEntityResolver entityResolver;
  private boolean xmlnsUrisNotAvailable;

  public MultiplexRootElementHandler
      (final ResourceManager manager,
       final ResourceKey source,
       final ResourceKey context,
       final long version,
       final XmlFactoryModule[] rootHandlers)
  {
    super(manager, source, context, version);
    this.entityResolver = new RootEntityResolver();
    this.rootHandlers = (XmlFactoryModule[]) rootHandlers.clone();
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

  public ParserEntityResolver getParserEntityResolver()
  {
    return entityResolver.getEntityResolver();
  }

  protected XmlFactoryModule[] getRootHandlers()
  {
    return rootHandlers;
  }

  /**
   * Starts processing an element.
   *
   * @param originalUri the URI.
   * @param localName   the local name.
   * @param qName       the qName.
   * @param attributes  the attributes.
   * @throws SAXException if there is a parsing problem.
   */
  protected void interceptFirstStartElement(final String originalUri,
                                            final String localName,
                                            final String qName,
                                            Attributes attributes)
      throws SAXException
  {
    // build the document info and select the root handler that will
    // deal with the document content.
    final DefaultXmlDocumentInfo documentInfo = new DefaultXmlDocumentInfo();
    documentInfo.setPublicDTDId(entityResolver.getPublicId());
    documentInfo.setSystemDTDId(entityResolver.getSystemId());
    documentInfo.setRootElement(localName);
    documentInfo.setRootElementNameSpace(originalUri);

    final String nsuri = attributes.getValue("xmlns");
    if (nsuri != null)
    {
      documentInfo.setDefaultNameSpace(nsuri);
    }
    else
    {
      documentInfo.setDefaultNameSpace("");
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
      throw new SAXException("No suitable root handler known for this document: " + documentInfo);
    }
    final XmlReadHandler readHandler =
        bestRootHandler.createReadHandler(documentInfo);
    if (readHandler == null)
    {
      throw new SAXException("Unable to create the root handler." + bestRootHandler);
    }

    String defaultNamespace = documentInfo.getDefaultNameSpace();
    if (defaultNamespace == null)
    {
      // Now correct the namespace ..
      defaultNamespace = bestRootHandler.getDefaultNamespace(documentInfo);
      if (defaultNamespace != null)
      {
        documentInfo.setRootElementNameSpace(defaultNamespace);
      }
    }

    pushDefaultNamespace(defaultNamespace);

    String uri;
    if ((originalUri == null || "".equals(originalUri)) &&
        defaultNamespace != null)
    {
      uri = defaultNamespace;
    }
    else
    {
      uri = originalUri;
    }

    attributes = new FixNamespaceUriAttributes(uri, attributes);
    installRootHandler(readHandler, uri, localName, attributes);
  }

  /**
   * Returns the object for this element or null, if this element does not
   * create an object.
   *
   * @return the object.
   */
  public Object getObject() throws SAXException
  {
    return null;
  }
}
