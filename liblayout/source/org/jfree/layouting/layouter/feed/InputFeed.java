/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * InputFeed.java
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
package org.jfree.layouting.layouter.feed;

/**
 * Creation-Date: 05.12.2005, 18:04:11
 *
 * @author Thomas Morgner
 */
public interface InputFeed
{
  /**
   * Starts the document processing. This is the first method to call. After
   * calling this method, the meta-data should be fed into the inputfeed.
   */
  public void startDocument();

  /**
   * Signals, that meta-data follows. Calling this method is only valid directly
   * after startDocument has been called.
   */
  public void startMetaInfo();

  /**
   * Adds document attributes. Document attributes hold object factories and
   * document wide resources which appear only once.
   *
   * @param namespace
   * @param name
   * @param attr
   */
  public void addDocumentAttribute(String name, Object attr);

  /**
   * Starts a new meta-node structure. Meta-Nodes are used to hold content that
   * can appear more than once (like stylesheet declarations).
   *
   * For now, only stylesheet declarations are defined as meta-node content;
   * more content types will surely arise in the future.
   *
   * Calling this method is only valid after 'startMetaInfo' has been called.
   */
  public void startMetaNode();

  /**
   * Defines an attribute for the meta-nodes. For each meta node, at least the
   * 'type' attribute (namespace: LibLayout) should be defined.
   *
   * @param namespace
   * @param name
   * @param attr
   */
  public void setMetaNodeAttribute(String name, Object attr);
  public void endMetaNode();

  public void endMetaInfo();

  public void startElement(String namespace, String name);

  public void setAttribute(String namespace, String name, Object attr);

  public void addContent(String text);

  public void endElement();

  public void endDocument();

  public Object getSavePointData();

  public void setSavePointData(Object savePoint);

}
