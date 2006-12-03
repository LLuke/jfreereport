/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.layouting.normalizer.displaymodel;

import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.normalizer.content.NormalizationException;

/**
 * A display element has a tag-name and a namespace and a set of attributes.
 *
 * DisplayElements do not have a direct relation ship to their childs (as this
 * would only complicate the process for no gain). Each child knows how its
 * parent, and thus can walk the tree towards the root - but there is no way to
 * walk from the tree's root to any of the childs.
 *
 * @author Thomas Morgner
 */
public abstract class DisplayElement extends DisplayNode
{
  public DisplayElement(final LayoutContext context)
  {
    super(context);
  }

  public abstract void add (DisplayNode node) throws NormalizationException;



  protected void addInternal (DisplayNode node) throws NormalizationException
  {
    node.setParent(this);

    signalStartOfChild(node);
    // else ignore it .. (should not happen anyway)
  }

  protected void signalStartOfChild(final DisplayNode node)
          throws NormalizationException
  {
    node.signalStart();
  }

  public void markFinished() throws NormalizationException
  {
    if (isFinished())
    {
      return;
    }
    super.markFinished();
    signalFinish();
  }

  protected abstract void signalFinish() throws NormalizationException;

}
