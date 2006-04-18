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
 * DefaultXmlDocumentInfo.java
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

import java.util.Collections;
import java.util.Map;

/**
 * Creation-Date: 07.04.2006, 16:37:03
 *
 * @author Thomas Morgner
 */
public class DefaultXmlDocumentInfo implements XmlDocumentInfo
{
  private String rootElement;
  private String rootElementNameSpace;
  private Map namespaces;
  private String publicDTDId;
  private String systemDTDId;
  private String defaultNameSpace;

  public DefaultXmlDocumentInfo()
  {
  }

  public String getRootElement()
  {
    return rootElement;
  }

  public void setRootElement(final String rootElement)
  {
    this.rootElement = rootElement;
  }

  public String getRootElementNameSpace()
  {
    return rootElementNameSpace;
  }

  public void setRootElementNameSpace(final String rootElementNameSpace)
  {
    this.rootElementNameSpace = rootElementNameSpace;
  }

  public Map getNamespaces()
  {
    return namespaces;
  }

  public void setNamespaces(final Map namespaces)
  {
    this.namespaces = Collections.unmodifiableMap(namespaces);
  }

  public String getPublicDTDId()
  {
    return publicDTDId;
  }

  public void setPublicDTDId(final String publicDTDId)
  {
    this.publicDTDId = publicDTDId;
  }

  public String getSystemDTDId()
  {
    return systemDTDId;
  }

  public void setSystemDTDId(final String systemDTDId)
  {
    this.systemDTDId = systemDTDId;
  }

  public String toString ()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("XmlDocumentInfo={rootElementTag=");
    buffer.append(rootElement);
    buffer.append(", rootElementNS=");
    buffer.append(rootElementNameSpace);
    buffer.append(", SystemDTD-ID=");
    buffer.append(systemDTDId);
    buffer.append(", PublicDTD-ID=");
    buffer.append(publicDTDId);
    buffer.append(", namespaces=");
    buffer.append(namespaces);
    buffer.append(", defaultnamespace=");
    buffer.append(defaultNameSpace);
    buffer.append("}");
    new Exception().printStackTrace();
    return buffer.toString();
  }

  public String getDefaultNameSpace()
  {
    return defaultNameSpace;
  }

  public void setDefaultNameSpace(final String defaultNameSpace)
  {
    this.defaultNameSpace = defaultNameSpace;
  }
}
