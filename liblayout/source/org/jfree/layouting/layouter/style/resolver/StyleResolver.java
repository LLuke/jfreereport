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
 * StyleResolver.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: StyleResolver.java,v 1.2 2006/04/17 20:51:14 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.style.resolver;

import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.LayoutProcess;

/**
 * Creation-Date: 05.12.2005, 18:03:52
 *
 * @author Thomas Morgner
 */
public interface StyleResolver
{
  public StyleResolver deriveInstance();

  /**
   * Resolves the style. This is guaranteed to be called in the order of the
   * document elements traversing the document tree using the
   * 'deepest-node-first' strategy.
   *
   * @param node
   */
  public void resolveStyle (LayoutElement node);

  public void initialize(LayoutProcess layoutProcess);
}
