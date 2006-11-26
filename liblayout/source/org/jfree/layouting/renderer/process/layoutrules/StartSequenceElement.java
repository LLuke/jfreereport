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
 * StartSequenceElement.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: StartSequenceElement.java,v 1.2 2006/10/22 14:58:26 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.process.layoutrules;

import org.jfree.layouting.renderer.model.InlineRenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.StaticBoxLayoutProperties;

/**
 * Represents the opening of an inline element and represents the respective
 * border. There is no break after that element.
 *
 * @author Thomas Morgner
 */
public class StartSequenceElement implements InlineSequenceElement
{
  private InlineRenderBox node;

  public StartSequenceElement(final InlineRenderBox node)
  {
    this.node = node;
  }
  /**
   * The width of the element. This is the minimum width of the element.
   *
   * @return
   */
  public long getMinimumWidth()
  {
    final StaticBoxLayoutProperties blp = node.getStaticBoxLayoutProperties();
    return blp.getBorderLeft() + blp.getPaddingLeft() + blp.getMarginLeft();
  }

  /**
   * The extra-space width for an element. Some elements can expand to fill some
   * more space (justified text is a good example, adding some space between the
   * letters of each word to reduce the inner-word spacing).
   *
   * @return
   */
  public long getMaximumWidth()
  {
    return getMinimumWidth();
  }

  public RenderNode getNode()
  {
    return node;
  }

  public boolean isPreserveWhitespace()
  {
    return node.isPreserveSpace();
  }
}
