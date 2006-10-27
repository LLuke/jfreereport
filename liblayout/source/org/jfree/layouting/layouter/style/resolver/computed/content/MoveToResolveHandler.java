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
 * MoveToResolveHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: MoveToResolveHandler.java,v 1.3 2006/07/11 13:29:52 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.layouting.layouter.style.resolver.computed.content;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.content.ContentStyleKeys;
import org.jfree.layouting.input.style.keys.content.MoveToValues;
import org.jfree.layouting.input.style.values.CSSStringType;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.content.computed.CounterToken;
import org.jfree.layouting.layouter.content.computed.CountersToken;
import org.jfree.layouting.layouter.content.resolved.ResolvedToken;
import org.jfree.layouting.layouter.context.ContentSpecification;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;

public class MoveToResolveHandler implements ResolveHandler
{
  public MoveToResolveHandler ()
  {
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more dependent
   * is the resolver on other resolvers to be complete.
   *
   * @return the array of required style keys.
   */
  public StyleKey[] getRequiredStyles ()
  {
    // no further dependecies. (We depend on the parent, not the current element)
    return new StyleKey[0];
  }

  private boolean isCounterUsed (LayoutElement element, String counter)
  {
    final LayoutContext layoutContext = element.getLayoutContext();
    final ContentSpecification contentSpecification =
        layoutContext.getContentSpecification();
    final ContentToken[] contents = contentSpecification.getContents();
    for (int i = 0; i < contents.length; i++)
    {
      ContentToken content = contents[i];
      if (content instanceof ResolvedToken)
      {
        ResolvedToken computedToken = (ResolvedToken) content;
        content = computedToken.getParent();
      }

      if (content instanceof CounterToken)
      {
        CounterToken counterToken = (CounterToken) content;
        if (counterToken.getName().equals(counter))
        {
          return true;
        }
      }
      else if (content instanceof CountersToken)
      {
        CountersToken counterToken = (CountersToken) content;
        if (counterToken.getName().equals(counter))
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Resolves a single property.
   *
   * @param currentNode
   * @param style
   */
  public void resolve (LayoutProcess process,
                       LayoutElement currentNode,
                       LayoutStyle style,
                       StyleKey key)
  {
    final CSSValue value = style.getValue(ContentStyleKeys.MOVE_TO);
    LayoutContext layoutContext = currentNode.getLayoutContext();

    // Maybe this is a 'normal'.
    if (MoveToValues.NORMAL.equals(value))
    {
      if ("alternate".equals(layoutContext.getPseudoElement()))
      {
        // For '::alternate' pseudo-elements, if the superior parent uses
        // the 'footnote' counter in its 'content' property then the computed
        // value of 'move-to' is 'footnotes'.
        if (isCounterUsed(currentNode.getParent(), "footnote"))
        {
          style.setValue(ContentStyleKeys.MOVE_TO,
              new CSSStringValue(CSSStringType.STRING, "footnotes"));
          return;
        }

        // For '::alternate' pseudo-elements, if the superior parent uses
        // the 'endnote' counter in its 'content' property then the computed
        // value of 'move-to' is 'endnotes'.
        if (isCounterUsed(currentNode.getParent(), "endnote"))
        {
          style.setValue(ContentStyleKeys.MOVE_TO,
              new CSSStringValue(CSSStringType.STRING, "endnotes"));
          return;
        }

        // For '::alternate' pseudo-elements, if the superior parent uses
        // the 'section-note' counter in its 'content' property then the
        // computed value of 'move-to' is 'section-notes'.
        if (isCounterUsed(currentNode.getParent(), "section-note"))
        {
          style.setValue(ContentStyleKeys.MOVE_TO,
              new CSSStringValue(CSSStringType.STRING, "section-notes"));
          return;
        }
      }
      style.setValue(ContentStyleKeys.MOVE_TO, MoveToValues.HERE);
    }
  }
}
