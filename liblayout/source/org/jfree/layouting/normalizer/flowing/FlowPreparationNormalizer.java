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
 * FlowPreparationNormalizer.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.normalizer.flowing;

import java.util.ArrayList;
import java.util.Stack;

import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.model.LayoutTextNode;
import org.jfree.layouting.normalizer.Normalizer;
import org.jfree.layouting.normalizer.common.BlockCompositionStrategy;
import org.jfree.layouting.normalizer.common.CompositionStrategy;
import org.jfree.layouting.normalizer.common.ContentBox;
import org.jfree.layouting.normalizer.common.ContentNode;
import org.jfree.layouting.normalizer.common.ContentText;
import org.jfree.layouting.normalizer.common.InlineCompositionStrategy;
import org.jfree.layouting.input.style.keys.box.DisplayModel;

/**
 * Creation-Date: 03.01.2006, 12:58:54
 *
 * @author Thomas Morgner
 */
public class FlowPreparationNormalizer implements Normalizer
{
  private Stack stack;

  public FlowPreparationNormalizer()
  {
    stack = new Stack();
  }

  protected ContentBox getCurrentContext() throws CloneNotSupportedException
  {
    ContentBox currentBox = (ContentBox) stack.peek();
    if (currentBox.isFinished() == false)
    {
      return currentBox;
    }

    // recurse down the stack, until we find a non-finished element
    // this is our block context. We 'pop' all elements from the stack
    // into a separate list.
    final ArrayList contexts = new ArrayList();

    while (currentBox.isFinished())
    {
      // remove the known finished element from the stack ...
      stack.pop();
      // but add it to our list of elements that should be reopened
      contexts.add(currentBox);
      // now look at the next one...
      currentBox = (ContentBox) stack.peek();
    }
    // now we have reached the block context.
    // ContentBox blockContext = currentBox;

    // From there we go up, re-adding new elements.
    for (int i = contexts.size() - 1; i >= 0; i -= 1)
    {
      final ContentBox contentBox = (ContentBox) contexts.get(i);
      final ContentBox derived = contentBox.derive();
      currentBox.add(derived);
      stack.push(derived);
      currentBox = derived;
    }

    return currentBox;
  }

  public void startElement(LayoutElement element)
  {
    try
    {
      ContentBox currentBox = getCurrentContext();

      CompositionStrategy cs;
      final DisplayModel displayModel =
              element.getLayoutContext().getBoxSpecification().getDisplayModel();
      if (displayModel == DisplayModel.INLINE_INSIDE)
      {
        cs = new InlineCompositionStrategy();
      }
      else
      {
        cs = new BlockCompositionStrategy();
      }
      ContentBox newBox = new ContentBox(element, cs);
      currentBox.add(newBox);

      stack.push(newBox);
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException("Ups, clone failed?");
    }

  }

  public void addText(LayoutTextNode text)
  {
    try
    {
      ContentBox currentBox = getCurrentContext();
      currentBox.add(new ContentText(text));
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException("Ups, clone failed?");
    }
  }

  public void endElement(final LayoutElement element)
  {
    stack.pop();
  }

  public void startDocument()
  {
    ContentBox rootBox = new ContentBox(null, new BlockCompositionStrategy());
    stack.push(rootBox);
  }

  public void endDocument()
  {
    stack.pop();
    if (stack.isEmpty() == false)
    {
      throw new IllegalStateException();
    }
  }

  public void print()
  {
    ContentBox root = (ContentBox) stack.get(0);
    print(root);
  }

  private void print(ContentBox box)
  {
    System.out.println("Start: " + box);
    ContentNode[] cns = box.getElements();
    for (int i = 0; i < cns.length; i++)
    {
      ContentNode cn = cns[i];
      if (cn instanceof ContentText)
      {
        ContentText text = (ContentText) cn;
        System.out.println("Text: " + text.getLayoutElement());
      }
      else if (cn instanceof ContentBox)
      {
        print((ContentBox) cn);
      }
      else
      {
        System.out.println("AWRASDASKLDJLK");
      }
    }
    System.out.println("End  : " + box);
  }

  public void addReplacedElement(LayoutElement element)
  {

  }
}
