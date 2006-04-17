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
 * LangCSSCondition.java
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
package org.jfree.layouting.input.style.selectors.conditions;

import org.w3c.css.sac.LangCondition;

/**
 * Creation-Date: 24.11.2005, 19:54:48
 *
 * @author Thomas Morgner
 */
public class LangCSSCondition implements LangCondition, CSSCondition
{
  private String lang;

  public LangCSSCondition(final String value)
  {
    this.lang = value;
  }

  /** An integer indicating the type of <code>Condition</code>. */
  public short getConditionType()
  {
    return SAC_LANG_CONDITION;
  }

  public boolean isMatch(final Object resolveState)
  {
    // todo: Language is a special, inheritable style attribute
    return false;
  }

  /** Returns the language */
  public String getLang()
  {
    return lang;
  }
}
