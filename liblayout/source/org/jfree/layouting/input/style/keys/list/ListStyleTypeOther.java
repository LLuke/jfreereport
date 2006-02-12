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
 * ListStyleTypeOther.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: ListStyleTypeOther.java,v 1.1 2006/02/12 21:54:27 taqua Exp $
 *
 * Changes
 * -------------------------
 * 01.12.2005 : Initial version
 */
package org.jfree.layouting.input.style.keys.list;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 01.12.2005, 19:11:23
 *
 * @author Thomas Morgner
 */
public class ListStyleTypeOther extends CSSConstant
{
  public static final ListStyleTypeOther NORMAL =
          new ListStyleTypeOther("normal");


  public static final ListStyleTypeOther ASTERISKS =
          new ListStyleTypeOther("asterisks");
  public static final ListStyleTypeOther FOOTNOTES =
          new ListStyleTypeOther("footnotes");
  //  circled-decimal  | circled-lower-latin | circled-upper-latin |
  public static final ListStyleTypeOther CIRCLED_DECIMAL =
          new ListStyleTypeOther("circled-decimal");
  public static final ListStyleTypeOther CIRCLED_LOWER_LATIN =
          new ListStyleTypeOther("circled-lower-latin");
  public static final ListStyleTypeOther CIRCLED_UPPER_LATIN =
          new ListStyleTypeOther("circled-upper-latin");
  // dotted-decimal | double-circled-decimal | filled-circled-decimal |
  public static final ListStyleTypeOther DOTTED_DECIMAL =
          new ListStyleTypeOther("dotted-decimal");
  public static final ListStyleTypeOther DOUBLE_CIRCLED_DECIMAL =
          new ListStyleTypeOther("double-circled-decimal");
  public static final ListStyleTypeOther FILLED_CIRCLED_DECIMAL =
          new ListStyleTypeOther("filled-circled-decimal");
  // parenthesised-decimal | parenthesised-lower-latin
  public static final ListStyleTypeOther PARANTHESISED_DECIMAL =
          new ListStyleTypeOther("parenthesised-decimal");
  public static final ListStyleTypeOther PARANTHESISED_LOWER_LATIN =
          new ListStyleTypeOther("parenthesised-lower-latin");

  private ListStyleTypeOther(final String constant)
  {
    super(constant);
  }
}
