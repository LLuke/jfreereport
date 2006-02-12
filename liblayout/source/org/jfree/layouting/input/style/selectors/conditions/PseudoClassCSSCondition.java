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
 * AttributeCSSCondition.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: PseudoClassCSSCondition.java,v 1.1 2006/02/12 21:54:27 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.selectors.conditions;

import org.w3c.css.sac.AttributeCondition;

/**
 * Creation-Date: 24.11.2005, 19:54:48
 *
 * @author Thomas Morgner
 */
public class PseudoClassCSSCondition implements AttributeCondition, CSSCondition
{
  private String namespace;
  private String value;

  public PseudoClassCSSCondition(final String namespace,
                                 final String value)
  {
    this.namespace = namespace;
    this.value = value;
  }

  /** An integer indicating the type of <code>Condition</code>. */
  public short getConditionType()
  {
    return SAC_PSEUDO_CLASS_CONDITION;
  }

  public boolean isMatch(final Object resolveState)
  {
    // todo Pseudoclass is a special style attribute ...
    return false;
  }

  /**
   * Returns the <a href="http://www.w3.org/TR/REC-xml-names/#dt-NSName">namespace
   * URI</a> of this attribute condition. <p><code>NULL</code> if : <ul>
   * <li>this attribute condition can match any namespace. <li>this attribute is
   * an id attribute. </ul>
   */
  public String getNamespaceURI()
  {
    return namespace;
  }

  /**
   * Returns <code>true</code> if the attribute must have an explicit value in
   * the original document, <code>false</code> otherwise.
   */
  public final boolean getSpecified()
  {
    return false;
  }

  public String getValue()
  {
    return value;
  }

  /**
   * Returns the <a href="http://www.w3.org/TR/REC-xml-names/#NT-LocalPart">local
   * part</a> of the <a href="http://www.w3.org/TR/REC-xml-names/#ns-qualnames">qualified
   * name</a> of this attribute. <p><code>NULL</code> if : <ul> <li><p>this
   * attribute condition can match any attribute. <li><p>this attribute is a
   * class attribute. <li><p>this attribute is an id attribute. <li><p>this
   * attribute is a pseudo-class attribute. </ul>
   */
  public String getLocalName()
  {
    return null;
  }
}
