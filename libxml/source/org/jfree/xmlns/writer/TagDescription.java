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
 * $Id: TagDescription.java,v 1.3 2007/04/01 13:46:34 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.xmlns.writer;

/**
 * A tag-description provides information about xml tags. At the moment, we
 * simply care whether an element can contain CDATA. In such cases, we do not
 * indent the inner elements.
 *
 * @author Thomas Morgner
 */
public interface TagDescription
{
  /**
   * Checks, whether the element specified by the tagname and namespace can
   * contain CDATA.
   *
   * @param namespace the namespace (as URI)
   * @param tagname the tagname
   * @return true, if the element can contain CDATA, false otherwise
   */
  boolean hasCData (String namespace, String tagname);
}
