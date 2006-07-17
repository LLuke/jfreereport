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
 * DisplayLineBox.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DisplayInlineElement.java,v 1.1 2006/07/11 13:45:08 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.normalizer.displaymodel;

import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.normalizer.content.NormalizationException;

/**
 * Creation-Date: 25.05.2006, 16:33:19
 *
 * @author Thomas Morgner
 */
public class DisplayInlineElement extends DisplayElement
{
  public DisplayInlineElement(final LayoutContext context)
  {
    super(context);
  }

  public void add(DisplayNode node) throws NormalizationException
  {
    final LayoutStyle style = node.getLayoutContext().getStyle();
    final CSSValue dr = style.getValue(BoxStyleKeys.DISPLAY_ROLE);
    if ((node instanceof DisplayElement == false) ||
        DisplayRole.INLINE.equals(dr))
    {
      addInternal(node);
      return;
    }

    // find the 'containing block' and add the element there.

    DisplayElement blockContext = this;
    while ((blockContext instanceof DisplayBlockElement) == false)
    {
      DisplayElement o = blockContext;
      blockContext.markFinished();
      blockContext = blockContext.getParent();
      if (blockContext == null)
      {
        // still: this should not happen in a sane environment.
        // at least the flow should catch this, as the flow is always a block
        // element.
        throw new NullPointerException("Last element in row refuses to be context: " + o);
      }
    }
    blockContext.add(node);
  }

  protected void signalFinish()
          throws NormalizationException
  {
    getRootFlow().getContentGenerator().finishedInline();
  }

  protected void signalStart() throws NormalizationException
  {
    getRootFlow().getContentGenerator().startedInline(this);
  }
}
