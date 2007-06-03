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
 * $Id: XmlDocumentInfo.java,v 1.4 2007/04/01 13:46:34 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.xmlns.parser;

/**
 * The XmlDocumentInfo class collects basic information about the document
 * that should be parsed.
 *
 * @author Thomas Morgner
 */
public interface XmlDocumentInfo
{
  /**
   * Returns the tag name of the root-level element.
   * @return the root-tag-name.
   */
  public String getRootElement();

  /**
   * Returns the namespace URI for the root-element of the document.
   * @return the namespace of the root-element.
   */
  public String getRootElementNameSpace();

  /**
   * Returns the Public-ID of the Document's DTD (if there's any).
   * @return the public id.
   */
  public String getPublicDTDId();

  /**
   * Returns the System-ID of the document's DTD.
   * @return the system-id.
   */
  public String getSystemDTDId();

  /**
   * Returns the default-namespace declared on the root-element. It is
   * not guaranteed that this information is filled until a XmlFactoryModule
   * has been selected.
   * 
   * @return the default-namespace.
   */
  public String getDefaultNameSpace();

}
