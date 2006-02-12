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
 * AndCSSCondition.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: OrCSSCondition.java,v 1.1 2006/02/12 21:54:27 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.selectors.conditions;

import org.w3c.css.sac.Condition;
import org.w3c.css.sac.CombinatorCondition;

/**
 * Creation-Date: 24.11.2005, 19:45:12
 *
 * @author Thomas Morgner
 */
public final class OrCSSCondition implements CombinatorCondition, CSSCondition
{
  private CSSCondition firstCondition;
  private CSSCondition secondCondition;

  public OrCSSCondition(final CSSCondition firstCondition,
                        final CSSCondition secondCondition)
  {
    this.firstCondition = firstCondition;
    this.secondCondition = secondCondition;
  }

  /** Returns the first condition. */
  public Condition getFirstCondition()
  {
    return firstCondition;
  }

  /** Returns the second condition. */
  public Condition getSecondCondition()
  {
    return secondCondition;
  }

  /** An integer indicating the type of <code>Condition</code>. */
  public short getConditionType()
  {
    return Condition.SAC_AND_CONDITION;
  }

  public boolean isMatch(final Object resolveState)
  {
    return firstCondition.isMatch(resolveState) ||
            secondCondition.isMatch(resolveState);
  }
}