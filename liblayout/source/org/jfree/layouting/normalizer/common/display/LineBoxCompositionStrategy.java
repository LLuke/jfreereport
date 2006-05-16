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
 * LineBoxCompositionStrategy.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: LineBoxCompositionStrategy.java,v 1.1 2006/04/17 21:04:27 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.normalizer.common.display;

import org.jfree.layouting.normalizer.common.display.CompositionStrategy;
import org.jfree.layouting.normalizer.common.display.ContentBox;
import org.jfree.layouting.normalizer.common.display.ContentNode;

/**
 * This strategy is not accessible from outside. It shall only be used for
 * lineboxes, as it does not perform any checks at all. It always assumes
 * that the elements are inline elements.
 *
 * Elements get stacked into each other.
 *
 * @author Thomas Morgner
 */
public class LineBoxCompositionStrategy implements CompositionStrategy
{

  public LineBoxCompositionStrategy()
  {
  }

  public void addElement(ContentBox parent, ContentNode node)
  {
    parent.addElementInternal(node);
  }

  public CompositionStrategy getInstance()
  {
    return this;
  }
}