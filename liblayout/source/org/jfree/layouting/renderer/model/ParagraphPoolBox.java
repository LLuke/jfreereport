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
 * ParagraphPoolBox.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ParagraphPoolBox.java,v 1.1 2006/10/17 17:31:57 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.output.OutputProcessorMetaData;

/**
 * Creation-Date: 04.10.2006, 17:50:44
 *
 * @author Thomas Morgner
 */
public class ParagraphPoolBox extends InlineRenderBox
{
  private boolean alwaysPropagate;
  private RenderLength lineHeight;

  public ParagraphPoolBox(final BoxDefinition boxDefinition)
  {
    super(boxDefinition);
    alwaysPropagate = true;
  }

  public void appyStyle(LayoutContext context, OutputProcessorMetaData metaData)
  {
    super.appyStyle(context, metaData);
    CSSValue lineHeightValue =
        context.getStyle().getValue(LineStyleKeys.LINE_HEIGHT);
    lineHeight =
        RenderLength.convertToInternal(lineHeightValue, context, metaData);

  }

  public RenderLength getLineHeight()
  {
    return lineHeight;
  }

  public void close()
  {
    if (isOpen() == false)
    {
      return;
    }

    RenderBox parent = getParent();
    super.close();
    if (parent != null)
    {
      parent.close();
    }
  }

  public void trim()
  {
    // remove leading and trailing spacer ...
    RenderNode node = getFirstChild();
    while (node != null)
    {
      if (node.isIgnorableForRendering() == false)
      {
        break;
      }
      remove(node);
      node = getFirstChild();
    }

    node = getLastChild();
    while (node != null)
    {
      if (node.isIgnorableForRendering() == false)
      {
        break;
      }
      remove(node);
      node = getLastChild();
    }
  }

  /**
   * Derive creates a disconnected node that shares all the properties of the
   * original node. The derived node will no longer have any parent, silbling,
   * child or any other relationships with other nodes.
   *
   * @return
   */
  public RenderNode derive(boolean deepDerive)
  {
    final ParagraphPoolBox renderNode = (ParagraphPoolBox) super.derive(deepDerive);
    renderNode.alwaysPropagate = false;
    return renderNode;
  }

  public boolean isAlwaysPropagateEvents()
  {
    return alwaysPropagate || super.isAlwaysPropagateEvents();
  }

  public void setParent (RenderBox parent)
  {
    super.setParent(parent);
  }
}
