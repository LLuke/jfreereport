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
 * $Id: AbstractXmlResourceFactory.java,v 1.5 2006/12/19 17:46:36 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.xmlns.parser;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jfree.resourceloader.CompoundResource;
import org.jfree.resourceloader.FactoryParameterKey;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceCreationException;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceFactory;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.util.Configuration;
import org.jfree.util.DefaultConfiguration;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Creation-Date: 07.04.2006, 14:53:07
 *
 * @author Thomas Morgner
 */
public abstract class AbstractXmlResourceFactory implements ResourceFactory
{
  /**
   * A key for the content base.
   */
  public static final String CONTENTBASE_KEY = "content-base";

  private HashSet modules;
  private SAXParserFactory factory;

  protected AbstractXmlResourceFactory()
  {
    modules = new HashSet();
  }


  /**
   * Returns a SAX parser.
   *
   * @return a SAXParser.
   * @throws ParserConfigurationException if there is a problem configuring the
   *                                      parser.
   * @throws SAXException                 if there is a problem with the parser
   *                                      initialisation
   */
  protected SAXParser getParser()
      throws ParserConfigurationException, SAXException
  {
    if (this.factory == null)
    {
      this.factory = SAXParserFactory.newInstance();
    }
    return this.factory.newSAXParser();
  }


  /**
   * Configures the xml reader. Use this to set features or properties before
   * the documents get parsed.
   *
   * @param handler the parser implementation that will handle the
   *                SAX-Callbacks.
   * @param reader  the xml reader that should be configured.
   */
  protected void configureReader(final XMLReader reader,
                                 final MultiplexRootElementHandler handler)
  {
    try
    {
      reader.setProperty
          ("http://xml.org/sax/properties/lexical-handler",
              handler.getCommentHandler());
    }
    catch (SAXException se)
    {
      Log.debug("Comments are not supported by this SAX implementation.");
    }

    try
    {
      reader.setFeature("http://xml.org/sax/features/xmlns-uris", true);
    }
    catch (SAXException e)
    {
      handler.setXmlnsUrisNotAvailable(true);
    }
    try
    {
      reader.setFeature("http://xml.org/sax/features/namespaces", true);
      reader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
    }
    catch (SAXException e)
    {
      Log.warn("No Namespace features will be available. (Yes, this is serious)");
    }
  }

  public Resource create(final ResourceManager manager,
                         final ResourceData data,
                         final ResourceKey context)
      throws ResourceCreationException, ResourceLoadingException
  {
    try
    {
      final SAXParser parser = getParser();

      final XMLReader reader = parser.getXMLReader();
      final XmlFactoryModule[] rootHandlers =
          (XmlFactoryModule[]) modules.toArray
              (new XmlFactoryModule[modules.size()]);
      final ResourceDataInputSource input = new ResourceDataInputSource(data, manager);


      final ResourceKey contextKey;
      final long version;
      final ResourceKey targetKey = data.getKey();
      if (context == null)
      {
        contextKey = targetKey;
        version = data.getVersion(manager);
      }
      else
      {
        contextKey = context;
        version = -1;
      }

      final MultiplexRootElementHandler handler =
          new MultiplexRootElementHandler(manager, targetKey,
              contextKey, version, rootHandlers);

      final DefaultConfiguration parserConfiguration = handler.getParserConfiguration();
      final URL value = contextKey.toURL();
      if (value != null)
      {
        parserConfiguration.setConfigProperty(CONTENTBASE_KEY, value.toExternalForm());
      }

      configureReader(reader, handler);
      reader.setContentHandler(handler);
      reader.setDTDHandler(handler);
      reader.setEntityResolver(handler.getEntityResolver());
      reader.setErrorHandler(getErrorHandler());

      final Map parameters = targetKey.getParameters();
      Iterator it = parameters.keySet().iterator();
      while (it.hasNext())
      {
        Object o = it.next();
        if (o instanceof FactoryParameterKey)
        {
          FactoryParameterKey fpk = (FactoryParameterKey) o;
          handler.setHelperObject(fpk.getName(), parameters.get(fpk));
        }
      }

      reader.parse(input);

      final Object createdProduct = finishResult
          (handler.getResult(), manager, data, contextKey);
      handler.getDependencyCollector().add(targetKey, data.getVersion(manager));
      return createResource(targetKey, handler, createdProduct);
    }
    catch (ParserConfigurationException e)
    {
      throw new ResourceCreationException
          ("Unable to initialize the XML-Parser", e);
    }
    catch (SAXException e)
    {
      throw new ResourceCreationException("Unable to parse the document", e);
    }
    catch (IOException e)
    {
      throw new ResourceLoadingException("Unable to read the stream", e);
    }
  }

  protected Resource createResource(final ResourceKey targetKey,
                                  final MultiplexRootElementHandler handler,
                                  final Object createdProduct)
  {
    return new CompoundResource
        (targetKey, handler.getDependencyCollector(), createdProduct);
  }

  protected Object finishResult(final Object res,
                                final ResourceManager manager,
                                final ResourceData data,
                                final ResourceKey context)
      throws ResourceCreationException, ResourceLoadingException
  {
    return res;
  }

  protected abstract Configuration getConfiguration();

  public void initializeDefaults()
  {
    final String type = getFactoryType().getName();
    final String prefix = ResourceFactory.CONFIG_PREFIX + type;
    final Configuration config = getConfiguration();
    final Iterator itType = config.findPropertyKeys(prefix);
    while (itType.hasNext())
    {
      final String key = (String) itType.next();
      final String modClass = config.getConfigProperty(key);
      final Object maybeFactory = ObjectUtilities.loadAndInstantiate
          (modClass, AbstractXmlResourceFactory.class, XmlFactoryModule.class);
      if (maybeFactory instanceof XmlFactoryModule == false)
      {
        continue;
      }
      registerModule((XmlFactoryModule) maybeFactory);
    }
  }

  public void registerModule(final XmlFactoryModule factoryModule)
  {
    if (factoryModule == null)
    {
      throw new NullPointerException();
    }
    modules.add(factoryModule);
  }

  protected ErrorHandler getErrorHandler()
  {
    return new LoggingErrorHandler();
  }
}
