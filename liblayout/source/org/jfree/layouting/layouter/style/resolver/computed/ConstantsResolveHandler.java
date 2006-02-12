/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * ConstantsResolveHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: ConstantsResolveHandler.java,v 1.1 2006/02/12 21:49:32 taqua Exp $
 *
 * Changes
 * -------------------------
 * 11.12.2005 : Initial version
 */
package org.jfree.layouting.layouter.style.resolver.computed;

import java.util.HashMap;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;

/**
 * Creation-Date: 11.12.2005, 23:15:57
 *
 * @author Thomas Morgner
 */
public abstract class ConstantsResolveHandler implements ResolveHandler
{
  private static final StyleKey[] EMPTY_STYLE_KEYS = new StyleKey[0];

  private HashMap constants;
  private CSSValue fallback;

  protected ConstantsResolveHandler()
  {
    constants = new HashMap();
  }

  public CSSValue getFallback()
  {
    return fallback;
  }

  protected void setFallback(final CSSValue fallback)
  {
    this.fallback = fallback;
  }

  protected CSSValue lookupValue(final CSSConstant value)
  {
    return (CSSValue) constants.get(value);
  }

  protected void addValue(CSSConstant constant, CSSValue value)
  {
    constants.put(constant, value);
  }

  protected void addNormalizeValue(CSSConstant constant)
  {
    constants.put(constant, constant);
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more
   * dependent is the resolver on other resolvers to be complete.
   *
   * @return  the array of required style keys.
   */
  public StyleKey[] getRequiredStyles()
  {
    return EMPTY_STYLE_KEYS;
  }

  /**
   * Resolves a single property.
   *
   * @param style
   * @param currentNode
   */
  public void resolve(final LayoutProcess process,
                      LayoutNode currentNode,
                      LayoutStyle style,
                      StyleKey key)
  {

    CSSValue value = resolveValue(process, currentNode, style, key);
    if (value != null)
    {
      style.setValue(key, value);
    }
  }

  protected CSSValue resolveValue (final LayoutProcess process,
                                   final LayoutNode currentNode,
                                   final LayoutStyle style,
                                   final StyleKey key)
  {
    CSSValue value = style.getValue(key);
    if (value instanceof CSSConstant == false)
    {
      final CSSValue fallback = getFallback();
      if (fallback != null)
      {
        return fallback;
      }
      return null;
    }

    CSSConstant constant = (CSSConstant) value;
    CSSValue resolvedValue = lookupValue(constant);
    if (resolvedValue != null)
    {
      style.setValue(key, resolvedValue);
      return resolvedValue;
    }

    final CSSValue fallback = getFallback();
    if (fallback != null)
    {
      style.setValue(key, fallback);
      return fallback;
    }

    return null;
  }
}