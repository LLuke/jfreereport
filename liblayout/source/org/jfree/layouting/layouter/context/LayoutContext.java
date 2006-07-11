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
 * LayoutContext.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: LayoutContext.java,v 1.2 2006/04/17 20:51:17 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.context;

import java.util.Locale;

import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.util.AttributeMap;

/**
 * This is where the computed style goes into.
 *
 * @author Thomas Morgner
 */
public interface LayoutContext extends Cloneable
{
  public ContextId getContextId();

  public BackgroundSpecification getBackgroundSpecification();
  public FontSpecification getFontSpecification();
  public ContentSpecification getContentSpecification();
  public ListSpecification getListSpecification();
  public LayoutStyle getStyle();

  /**
   * An element can be exactly one pseudo-element type. It is not possible
   * for an element to fullfill two roles, an element is either a 'before'
   * or a 'marker', but can as well be a 'before' of an 'marker' (where
   * the marker element would be the parent).
   *
   * @return
   */
  public String getPseudoElement();

  /**
   * May be null.
   * @return
   */
  public String getNamespace();
  /**
   * May be null.
   * @return
   */
  public String getTagName();
  /**
   * May never be null.
   * @return
   */
  public AttributeMap getAttributes();

  /**
   * Returns the language definition of this layout context. If not set, it
   * defaults to the parent's language. If the root's language is also not
   * defined, then use the system default.
   *
   * @return the defined language, never null.
   */
  public Locale getLanguage();

  public boolean isPseudoElement();

  public Object clone();

  public LayoutContext derive();

  public LayoutContext deriveAnonymous();
}
