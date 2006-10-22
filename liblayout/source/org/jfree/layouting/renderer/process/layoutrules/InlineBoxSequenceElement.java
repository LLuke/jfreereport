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
 * InlineBoxSequenceElement.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: InlineBoxSequenceElement.java,v 1.1 2006/10/17 17:31:57 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.process.layoutrules;

import org.jfree.layouting.renderer.model.BoxLayoutProperties;
import org.jfree.layouting.renderer.model.NodeLayoutProperties;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.StaticBoxLayoutProperties;

/**
 * Anthing that is not text. This could be an image or an inline-block element.
 * For now, we assume that these beasts are not breakable at the end of the line
 * (outer linebreaks).
 *
 * @author Thomas Morgner
 */
public class InlineBoxSequenceElement extends InlineNodeSequenceElement
{
  private RenderBox box;

  public InlineBoxSequenceElement(final RenderBox box)
  {
    super(box);
    this.box = box;
  }

  /**
   * The width of the element. This is the minimum width of the element.
   *
   * @return
   */
  public long getMinimumWidth()
  {
    final StaticBoxLayoutProperties blp = box.getStaticBoxLayoutProperties();
    final NodeLayoutProperties nlp = box.getNodeLayoutProperties();
    return nlp.getMinimumChunkWidth() +
        blp.getMarginLeft() + blp.getMarginRight();
  }

  public long getMaximumWidth()
  {
    final StaticBoxLayoutProperties blp = box.getStaticBoxLayoutProperties();
    final NodeLayoutProperties nlp = box.getNodeLayoutProperties();
    return nlp.getMaximumBoxWidth() +
        blp.getMarginLeft() + blp.getMarginRight();
  }

  public boolean isPreserveWhitespace()
  {
    return box.isPreserveSpace();
  }
}
