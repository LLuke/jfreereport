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
 * AndCSSCondition.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: AndCSSCondition.java,v 1.2 2006/04/17 20:51:09 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.input.style.selectors.conditions;

import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;

/**
 * Creation-Date: 24.11.2005, 19:45:12
 *
 * @author Thomas Morgner
 */
public final class AndCSSCondition implements CombinatorCondition, CSSCondition
{
  private CSSCondition firstCondition;
  private CSSCondition secondCondition;

  public AndCSSCondition(final CSSCondition firstCondition,
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
    return firstCondition.isMatch(resolveState) &&
            secondCondition.isMatch(resolveState);
  }
}
