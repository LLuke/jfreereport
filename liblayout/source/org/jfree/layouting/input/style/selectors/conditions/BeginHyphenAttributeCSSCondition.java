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
 * OneOfAttributeCSSCondition.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: BeginHyphenAttributeCSSCondition.java,v 1.1 2006/02/12 21:54:27 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.selectors.conditions;

/**
 * Creation-Date: 24.11.2005, 20:13:53
 *
 * @author Thomas Morgner
 */
public class BeginHyphenAttributeCSSCondition extends AttributeCSSCondition
{
  public BeginHyphenAttributeCSSCondition(final String name,
                                          final String namespace,
                                          final boolean specified,
                                          final String value)
  {
    super(name, namespace, specified, value);
  }

  public boolean isMatch(final Object resolveState)
  {
    return false; // todo
  }

  /** An integer indicating the type of <code>Condition</code>. */
  public short getConditionType()
  {
    return SAC_BEGIN_HYPHEN_ATTRIBUTE_CONDITION;
  }
}