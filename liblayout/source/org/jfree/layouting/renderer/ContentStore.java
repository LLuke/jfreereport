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
 * ContentStore.java
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
package org.jfree.layouting.renderer;

import java.util.HashMap;

import org.jfree.layouting.renderer.model.RenderNode;

/**
 * For the first throw, the content ramins very simple. We support the 4 modes:
 * start - the initial content is used. first - the first value set in this page
 * is used (else the initial content) last - the last value is used. last-except
 * - the last value is used on the next page. (Contrary to the specification, we
 * fall back to the start-value instead of using an empty value).
 *
 * @author Thomas Morgner
 */
public class ContentStore
{
  private HashMap initialSet;
  private HashMap firstSet;
  private HashMap lastSet;

  public ContentStore()
  {
    initialSet = new HashMap();
    firstSet = new HashMap();
    lastSet = new HashMap();
  }

  public void add(String name, RenderNode[] contents)
  {
    if (firstSet.containsKey(name) == false)
    {
      firstSet.put(name, contents);
    }
    lastSet.put(name, contents);
  }

  public RenderNode[] getLast(String name)
  {
    if (lastSet.containsKey(name))
    {
      return deriveRetval((RenderNode[]) lastSet.get(name));
    }
    return deriveRetval((RenderNode[]) initialSet.get(name));
  }

  public RenderNode[] getFirst(String name)
  {
    if (firstSet.containsKey(name))
    {
      return deriveRetval((RenderNode[]) firstSet.get(name));
    }
    return deriveRetval((RenderNode[]) initialSet.get(name));
  }

  private RenderNode[] deriveRetval(RenderNode[] val)
  {
    if (val == null)
    {
      return new RenderNode[0];
    }
    final RenderNode[] reval = new RenderNode[val.length];
    for (int i = 0; i < val.length; i++)
    {
      final RenderNode node = val[i];
      reval[i] = node.derive(true);
    }
    return val;
  }

  public ContentStore derive()
  {
    final ContentStore contentStore = new ContentStore();
    contentStore.initialSet.putAll(lastSet);
    return contentStore;
  }
}
