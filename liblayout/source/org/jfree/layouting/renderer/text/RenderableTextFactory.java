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
 * RenderableTextFactory.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: RenderableTextFactory.java,v 1.2 2006/07/14 14:34:41 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.text;

import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.renderer.model.RenderNode;

/**
 * Problem: Text may span more than one chunk, and text may influence the break
 * behaviour of the next chunk.
 * <p/>
 * Possible solution: TextFactory does not return the complete text. It returns
 * the text up to the last whitespace encountered and returns the text chunk
 * only if either finishText has been called or some more text comes in. The
 * ugly sideffect: Text may result in more than one renderable text chunk
 * returned.
 * <p/>
 * If we return lines (broken by an LineBreak-occurence) we can safe us a lot of
 * trouble later.
 *
 * @author Thomas Morgner
 */
public interface RenderableTextFactory
{
  /**
   * The text is given as CodePoints.
   *
   * @param text
   * @return
   */
  public RenderNode[] createText(final int[] text,
                                 final int offset,
                                 final int length,
                                 final LayoutContext layoutContext);

  public RenderNode[] finishText();

  public void startText();
}
