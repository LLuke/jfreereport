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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.xmlns.writer;

import java.util.HashMap;
import java.util.Iterator;

import org.jfree.util.Configuration;

public class DefaultTagDescription implements TagDescription
{
  private static class TagDefinitionKey
  {
    private String namespace;
    private String tagName;

    public TagDefinitionKey(String namespace, String tagName)
    {
      this.namespace = namespace;
      this.tagName = tagName;
    }

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

    public int hashCode()
    {
      int result;
      result = (namespace != null ? namespace.hashCode() : 0);
      result = 29 * result + (tagName != null ? tagName.hashCode() : 0);
      return result;
    }


    public String toString()
    {
      return "TagDefinitionKey{" +
          "namespace='" + namespace + '\'' +
          ", tagName='" + tagName + '\'' +
          '}';
    }
  }

  private HashMap defaultDefinitions;
  private HashMap tagData;
  private String defaultNamespace;

  public DefaultTagDescription()
  {
    defaultDefinitions = new HashMap();
    tagData = new HashMap();
  }

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
    final boolean globalValue = "deny".equals(conf.getConfigProperty(globalDefaultKey));
    defaultDefinitions.put(null, (globalValue) ? Boolean.TRUE : Boolean.FALSE);

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

      boolean value = "deny".equals(conf.getConfigProperty(key));
      defaultDefinitions.put(nsUri, (value) ? Boolean.TRUE : Boolean.FALSE);
    }

    final String nsTagsPrefix = prefix + "tag.";
    final Iterator tags = conf.findPropertyKeys(nsTagsPrefix);
    while (tags.hasNext())
    {
      String key = (String) tags.next();
      String tagDef = key.substring(nsTagsPrefix.length());
      final boolean value = "deny".equals(conf.getConfigProperty(key));

      final int delim = tagDef.indexOf('.');
      if (delim == -1)
      {
        tagData.put(new TagDefinitionKey(null, tagDef), (value) ? Boolean.TRUE : Boolean.FALSE);
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
        tagData.put(new TagDefinitionKey(nsUri, tagName), (value) ? Boolean.TRUE : Boolean.FALSE);
      }
    }
  }


  public boolean hasCData(String namespace, String tagname)
  {
    if (namespace == null)
    {
      namespace = defaultNamespace;
    }

    TagDefinitionKey key = new TagDefinitionKey(namespace, tagname);
    Object tagVal = tagData.get(key);
    if (tagVal != null)
    {
      return Boolean.TRUE.equals(tagVal);
    }

    final Object obj = defaultDefinitions.get(namespace);
    if (obj != null)
    {
      return Boolean.TRUE.equals(obj);
    }

    return Boolean.TRUE.equals(defaultDefinitions.get(null));
  }
}
