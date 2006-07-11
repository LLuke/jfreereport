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
 * ResolveHandlerModule.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ResolveHandlerModule.java,v 1.2 2006/04/17 20:51:14 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.style.resolver;

import java.util.HashSet;

import org.jfree.layouting.input.style.StyleKey;

/**
 * Creation-Date: 14.12.2005, 12:06:22
 *
 * @author Thomas Morgner
 */
public final class ResolveHandlerModule
{
  private StyleKey key;
  // direct dependencies, indirect ones are handled by the
  // dependent classes ...
  private ResolveHandler autoValueHandler;
  private ResolveHandler computedValueHandler;
  private ResolveHandler percentagesValueHandler;
  private StyleKey[] dependentKeys;
  private int weight;

  public ResolveHandlerModule(final StyleKey key,
                              final ResolveHandler autoValueHandler,
                              final ResolveHandler computedValueHandler,
                              final ResolveHandler percentagesValueHandler)
  {
    this.key = key;
    this.autoValueHandler = autoValueHandler;
    this.computedValueHandler = computedValueHandler;
    this.percentagesValueHandler = percentagesValueHandler;

    final HashSet dependentKeys = new HashSet();
    if (autoValueHandler != null)
    {
      final StyleKey[] keys = autoValueHandler.getRequiredStyles();
      for (int i = 0; i < keys.length; i++)
      {
        StyleKey styleKey = keys[i];
        dependentKeys.add(styleKey);
      }
    }

    if (computedValueHandler != null)
    {
      final StyleKey[] keys = computedValueHandler.getRequiredStyles();
      for (int i = 0; i < keys.length; i++)
      {
        StyleKey styleKey = keys[i];
        dependentKeys.add(styleKey);
      }
    }

    if (percentagesValueHandler != null)
    {
      final StyleKey[] keys = percentagesValueHandler.getRequiredStyles();
      for (int i = 0; i < keys.length; i++)
      {
        StyleKey styleKey = keys[i];
        dependentKeys.add(styleKey);
      }
    }

    this.dependentKeys = (StyleKey[])
            dependentKeys.toArray(new StyleKey[dependentKeys.size()]);
  }

  public StyleKey getKey()
  {
    return key;
  }

  public ResolveHandler getAutoValueHandler()
  {
    return autoValueHandler;
  }

  public ResolveHandler getComputedValueHandler()
  {
    return computedValueHandler;
  }

  public ResolveHandler getPercentagesValueHandler()
  {
    return percentagesValueHandler;
  }

  public StyleKey[] getDependentKeys()
  {
    return dependentKeys;
  }

  public int getWeight()
  {
    return weight;
  }

  public void setWeight(final int weight)
  {
    this.weight = weight;
  }
}
