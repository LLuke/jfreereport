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
 * ListStyleTypeOther.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ListStyleTypeOther.java,v 1.2 2006/04/17 20:51:02 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.input.style.keys.list;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 01.12.2005, 19:11:23
 *
 * @author Thomas Morgner
 */
public class ListStyleTypeOther
{
  public static final CSSConstant NORMAL =
          new CSSConstant("normal");


  public static final CSSConstant ASTERISKS =
          new CSSConstant("asterisks");
  public static final CSSConstant FOOTNOTES =
          new CSSConstant("footnotes");
  //  circled-decimal  | circled-lower-latin | circled-upper-latin |
  public static final CSSConstant CIRCLED_DECIMAL =
          new CSSConstant("circled-decimal");
  public static final CSSConstant CIRCLED_LOWER_LATIN =
          new CSSConstant("circled-lower-latin");
  public static final CSSConstant CIRCLED_UPPER_LATIN =
          new CSSConstant("circled-upper-latin");
  // dotted-decimal | double-circled-decimal | filled-circled-decimal |
  public static final CSSConstant DOTTED_DECIMAL =
          new CSSConstant("dotted-decimal");
  public static final CSSConstant DOUBLE_CIRCLED_DECIMAL =
          new CSSConstant("double-circled-decimal");
  public static final CSSConstant FILLED_CIRCLED_DECIMAL =
          new CSSConstant("filled-circled-decimal");
  // parenthesised-decimal | parenthesised-lower-latin
  public static final CSSConstant PARANTHESISED_DECIMAL =
          new CSSConstant("parenthesised-decimal");
  public static final CSSConstant PARANTHESISED_LOWER_LATIN =
          new CSSConstant("parenthesised-lower-latin");

  private ListStyleTypeOther()
  {
  }
}
