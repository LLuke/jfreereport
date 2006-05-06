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
 * DocumentContext.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DocumentContext.java,v 1.2 2006/04/17 20:51:17 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.model;

import org.jfree.layouting.layouter.counters.CounterStyle;
import org.jfree.layouting.namespace.NamespaceCollection;

/**
 * The document context holds general document-wide data.
 *
 * @author Thomas Morgner
 */
public interface DocumentContext extends DocumentMetaNode
{
  public static final String BASE_RESOURCE_ATTR = "base-resource";
  public static final String DATE_ATTR = "date";
  public static final String LOCALIZATION_ATTR = "localization-context";
  public static final String RESOURCE_MANAGER_ATTR = "resource-manager";
  public static final String STYLE_RESOLVER_ATTR = "style-resolver";
  public static final String STYLE_MATCHER_ATTR = "style-matcher";
  public static final String TITLE_ATTR = "title";
  public static final String STRICT_STYLE_MODE = "strict-style-mode";

  /**
   * This method is called once after the input-feed received all the document
   * meta-data.
   */
  public void initialize();

  public void addMetaNode (DocumentMetaNode node);
  public void removeMetaNode (DocumentMetaNode node);
  public DocumentMetaNode getMetaNode (int index);
  public int getMetaNodeCount ();

  public void addPendingContent (String name, LayoutElement element);
  public LayoutElement[] getPendingContent(String name);
  public void clearPendingContent (String name);

  /**
   * The namespace collection is not available until initialize() has been called
   * by the input-feed.
   * 
   * @return
   */
  public NamespaceCollection getNamespaces();

  /**
   * Sets strings. See http://www.w3.org/TR/css3-content/ (named strings)
   * for the specification.
   *
   * @param name the name of the string
   * @return the value or null if none is set.
   */
  public String getString (String name);

  public void setString (String name, String value);

  public void setCounterStyle (String counterName, CounterStyle style);
  public CounterStyle getCounterStyle (String counterName);

  public int getQuoteLevel();
  public void openQuote();
  public void closeQuote();
}
