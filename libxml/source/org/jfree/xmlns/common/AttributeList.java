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
 * $Id: AttributeList.java,v 1.4 2007/01/19 14:05:29 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.xmlns.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jfree.util.ObjectUtilities;

/**
 * The attribute list is used by a writer to specify the attributes of an XML
 * element in a certain order.
 *
 * @author Thomas Morgner
 */
public class AttributeList
{
  public static final String XMLNS_NAMESPACE = "http://www.w3.org/2000/xmlns/";
  public static final String XML_NAMESPACE = "http://www.w3.org/XML/1998/namespace";

  /**
   * A name/value pair of the attribute list.
   */
  public static class AttributeEntry
  {
    private String namespace;

    /**
     * The name of the attribute entry.
     */
    private String name;

    /**
     * The value of the attribute entry.
     */
    private String value;

    /**
     * Creates a new attribute entry for the given name and value.
     *
     * @param namespace the namespace of the attribute.
     * @param name      the attribute name (<code>null</code> not permitted).
     * @param value     the attribute value (<code>null</code> not permitted).
     */
    public AttributeEntry(final String namespace, final String name,
                          final String value)
    {
      if (name == null)
      {
        throw new NullPointerException("Name must not be null. ["
                                       + name + ", " + value + "]");
      }
      if (value == null)
      {
        throw new NullPointerException("Value must not be null. ["
                                       + name + ", " + value + "]");
      }
      this.namespace = namespace;
      this.name = name;
      this.value = value;
    }

    /**
     * Returns the attribute name.
     *
     * @return the name.
     */
    public String getName()
    {
      return this.name;
    }

    /**
     * Returns the value of this attribute entry.
     *
     * @return the value of the entry.
     */
    public String getValue()
    {
      return this.value;
    }

    public String getNamespace()
    {
      return namespace;
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

      final AttributeEntry that = (AttributeEntry) o;

      if (!name.equals(that.name))
      {
        return false;
      }
      if (namespace != null ? !namespace.equals(
          that.namespace) : that.namespace != null)
      {
        return false;
      }

      return true;
    }

    public int hashCode()
    {
      int result;
      result = (namespace != null ? namespace.hashCode() : 0);
      result = 29 * result + name.hashCode();
      return result;
    }
  }

  /**
   * The storage for all entries of this list.
   */
  private List entryList;

  /**
   * Creates an empty attribute list with no default values.
   */
  public AttributeList()
  {
    this.entryList = new ArrayList();
  }

  public Iterator iterator()
  {
    return entryList.iterator();
  }

  /**
   * Defines an attribute.
   *
   * @param namespace the namespace of the attribute.
   * @param name      the name of the attribute to be defined
   * @param value     the value of the attribute.
   */
  public void setAttribute(final String namespace,
                           final String name,
                           final String value)
  {
    final AttributeEntry entry =
        new AttributeEntry(namespace, name, value);
    final int pos = this.entryList.indexOf(entry);
    if (pos != -1)
    {
      this.entryList.remove(pos);
    }
    this.entryList.add(entry);
  }

  /**
   * Returns the attribute value for the given attribute name or null, if the
   * attribute is not defined in this list.
   *
   * @param namespace the namespace of the attribute.
   * @param name      the name of the attribute
   * @return the attribute value or null.
   */
  public String getAttribute(final String namespace,
                             final String name)
  {
    return getAttribute(namespace, name, null);
  }

  /**
   * Returns the attribute value for the given attribute name or the given
   * defaultvalue, if the attribute is not defined in this list.
   *
   * @param namespace    the namespace of the attribute.
   * @param name         the name of the attribute.
   * @param defaultValue the default value.
   * @return the attribute value or the defaultValue.
   */
  public String getAttribute(final String namespace,
                             final String name,
                             final String defaultValue)
  {
    for (int i = 0; i < this.entryList.size(); i++)
    {
      final AttributeEntry ae = (AttributeEntry) this.entryList.get(i);
      if (ae.getName().equals(name) && namespace.equals(name))
      {
        return ae.getValue();
      }
    }
    return defaultValue;
  }

  /**
   * Removes the attribute with the given name from the list.
   *
   * @param namespace the namespace of the attribute that should be removed.
   * @param name      the name of the attribute which should be removed..
   */
  public void removeAttribute(final String namespace,
                              final String name)
  {
    for (int i = 0; i < this.entryList.size(); i++)
    {
      final AttributeEntry ae = (AttributeEntry) this.entryList.get(i);
      if (ae.getName().equals(name) && namespace.equals(name))
      {
        this.entryList.remove(ae);
        return;
      }
    }
  }

  public boolean isEmpty()
  {
    return this.entryList.isEmpty();
  }

  public void addNamespaceDeclaration(final String prefix,
                                      final String namespaceUri)
  {
    if (prefix == null || "".equals(prefix))
    {
      setAttribute(AttributeList.XMLNS_NAMESPACE, "", namespaceUri);
    }
    else
    {
      setAttribute(AttributeList.XMLNS_NAMESPACE, prefix, namespaceUri);
    }
  }

  public boolean isNamespacePrefixDefined(final String prefix)
  {
    return getAttribute(AttributeList.XMLNS_NAMESPACE, prefix) != null;
  }

  public boolean isNamespaceUriDefined(final String uri)
  {
    for (int i = 0; i < this.entryList.size(); i++)
    {
      final AttributeEntry ae = (AttributeEntry) this.entryList.get(i);
      if (ObjectUtilities.equal(ae.getValue(), uri) &&
          AttributeList.XMLNS_NAMESPACE.equals(ae.getNamespace()))
      {
        return true;
      }
    }
    return false;
  }
}
