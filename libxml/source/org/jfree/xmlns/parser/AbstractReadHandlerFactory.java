/**
 * =========================================
 * LibXML : a free Java layouting library
 * =========================================
 *
 * Project Info:  http://reporting.pentaho.org/libxml/
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
 * $Id: AbstractReadHandlerFactory.java,v 1.2 2007/04/01 13:46:34 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.xmlns.parser;

import java.util.HashMap;
import java.util.Iterator;

import org.jfree.util.Configuration;
import org.jfree.util.ObjectUtilities;
import org.xml.sax.SAXException;

/**
 * The AbstractReadHandlerFactory provides a base implementation for all
 * read-handler factories. A read-handler factory decouples the tag-handlers
 * of a SAX parser and allows to configure alternate parser configuations
 * at runtime, resulting in a more flexible parsing process.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractReadHandlerFactory
{
  /**
   * The TagDefinitionKey is a compund key to lookup handler implementations
   * using a namespace and tagname.
   */
  private static class TagDefinitionKey
  {
    private String namespace;
    private String tagName;

    /**
     * Creates a new key.
     *
     * @param namespace the namespace (can be null for undefined).
     * @param tagName the tagname (can be null for undefined).
     */
    public TagDefinitionKey(String namespace, String tagName)
    {
      this.namespace = namespace;
      this.tagName = tagName;
    }

    /**
     * Compares this key for equality with an other object.
     *
     * @param o the other object.
     * @return true, if this key is the same as the given object, false otherwise.
     */
    public boolean equals(Object o)
    {
      if (this == o)
      {
        return true;
      }
      if (o == null || getClass() != o.getClass())
      {
        return false;
      }

      final TagDefinitionKey that = (TagDefinitionKey) o;

      if (namespace != null ? !namespace.equals(that.namespace) : that.namespace != null)
      {
        return false;
      }
      if (tagName != null ? !tagName.equals(that.tagName) : that.tagName != null)
      {
        return false;
      }

      return true;
    }

    /**
     * Computes the hashcode for this key.
     *
     * @return the hashcode.
     */
    public int hashCode()
    {
      int result = (namespace != null ? namespace.hashCode() : 0);
      result = 29 * result + (tagName != null ? tagName.hashCode() : 0);
      return result;
    }
  }

  private HashMap defaultDefinitions;
  private HashMap tagData;
  private String defaultNamespace;

  /**
   * A default-constructor.
   */
  protected AbstractReadHandlerFactory()
  {
    defaultDefinitions = new HashMap();
    tagData = new HashMap();
  }

  /**
   * Configures this factory from the given configuration using the speoified
   * prefix as filter.
   *
   * @param conf   the configuration.
   * @param prefix the key-prefix.
   */
  public void configure(Configuration conf, String prefix)
  {
    final HashMap knownNamespaces = new HashMap();

    final String nsConfPrefix = prefix + "namespace.";
    final Iterator namespaces = conf.findPropertyKeys(nsConfPrefix);
    while (namespaces.hasNext())
    {
      String key = (String) namespaces.next();
      String nsPrefix = key.substring(nsConfPrefix.length());
      String nsUri = conf.getConfigProperty(key);
      knownNamespaces.put(nsPrefix, nsUri);
    }

    defaultNamespace = (String) knownNamespaces.get
        (conf.getConfigProperty(prefix + "namespace"));

    final String globalDefaultKey = prefix + "default";
    final String globalValue = conf.getConfigProperty(globalDefaultKey);
    if (isValidHandler(globalValue))
    {
      defaultDefinitions.put(null, globalValue);
    }
    else
    {
      // let the loading fail ..
      defaultDefinitions.put(null, "");
    }

    final String nsDefaultPrefix = prefix + "default.";
    final Iterator defaults = conf.findPropertyKeys(nsDefaultPrefix);
    while (defaults.hasNext())
    {
      String key = (String) defaults.next();
      String nsPrefix = key.substring(nsDefaultPrefix.length());
      String nsUri = (String) knownNamespaces.get(nsPrefix);
      if (nsUri == null)
      {
        continue;
      }

      String tagData = conf.getConfigProperty(key);
      if (tagData == null)
      {
        continue;
      }
      if (isValidHandler(tagData))
      {
        defaultDefinitions.put(nsUri, tagData);
      }
      else
      {
        // let the loading fail .. to indicate we want no parsing ..
        defaultDefinitions.put(nsUri, "");
      }
    }

    final String nsTagsPrefix = prefix + "tag.";
    final Iterator tags = conf.findPropertyKeys(nsTagsPrefix);
    while (tags.hasNext())
    {
      String key = (String) tags.next();
      String tagDef = key.substring(nsTagsPrefix.length());
      String tagData = conf.getConfigProperty(key);
      if (tagData == null)
      {
        continue;
      }
      if (isValidHandler(tagData) == false)
      {
        continue;
      }

      final int delim = tagDef.indexOf('.');
      if (delim == -1)
      {
        this.tagData.put(new TagDefinitionKey(null, tagDef), tagData);
      }
      else
      {
        String nsPrefix = tagDef.substring(0, delim);
        String nsUri = (String) knownNamespaces.get(nsPrefix);
        if (nsUri == null)
        {
          continue;
        }

        final String tagName = tagDef.substring(delim + 1);
        this.tagData.put(new TagDefinitionKey(nsUri, tagName), tagData);
      }
    }
  }

  /**
   * Checks, whether the given handler classname can be instantiated
   * and is in fact an object of the required target-type.
   *
   * @param className the classname that should be checked.
   * @return true, if the handler is valid, false otherwise.
   */
  private boolean isValidHandler(String className)
  {
    if (className == null)
    {
      return false;
    }
    final Object o = ObjectUtilities.loadAndInstantiate
        (className, getClass(), getTargetClass());
    return o != null;
  }

  /**
   * Returns the implementation class for this read-handler factory.
   *
   * @return the implementation class.
   */
  protected abstract Class getTargetClass();

  /**
   * The returned handler can be null, in case no handler is registered.
   *
   * @param namespace the namespace of the xml-tag for which a handler should be returned.
   * @param tagname   the tagname of the xml-tag.
   * @return the instantiated read handler, never null.
   * @throws org.xml.sax.SAXException if the handler cannot be instantiated or
   *                                  if there is no handler defined at all.
   */
  public XmlReadHandler getHandler(String namespace, String tagname)
      throws SAXException
  {
    if (namespace == null)
    {
      namespace = defaultNamespace;
    }

    final TagDefinitionKey key = new TagDefinitionKey(namespace, tagname);
    String tagVal = (String) tagData.get(key);
    if (tagVal != null)
    {
      final Object o = ObjectUtilities.loadAndInstantiate
          (tagVal, getClass(), getTargetClass());
      return (XmlReadHandler) o;
    }

    final String className = (String) defaultDefinitions.get(namespace);
    if (className != null)
    {
      final Object o = ObjectUtilities.loadAndInstantiate
          (className, getClass(), getTargetClass());
      return (XmlReadHandler) o;
    }

    final String fallbackName = (String) defaultDefinitions.get(null);
    final Object fallbackValue = ObjectUtilities.loadAndInstantiate
        (fallbackName, getClass(), getTargetClass());
    if (fallbackValue != null)
    {
      return (XmlReadHandler) fallbackValue;
    }

    return null;
  }


}
