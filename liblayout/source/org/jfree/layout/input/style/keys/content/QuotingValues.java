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
 * QuotingValues.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 01.12.2005 : Initial version
 */
package org.jfree.layouting.input.style.keys.content;

import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSStringType;

/**
 * This class holds a sample of well-known quoting characters.
 * These values are non-normative and there are no CSS-constants
 * defined for them.
 *
 * @author Thomas Morgner
 */
public class QuotingValues extends CSSStringValue
{
  public static final QuotingValues QUOTATION_MARK = new QuotingValues("\"");
  public static final QuotingValues APOSTROPHE = new QuotingValues("\u0027");
  public static final QuotingValues SINGLE_LEFT_POINTING_ANGLE_QUOTATION_MARK =
          new QuotingValues("\u2039");
  public static final QuotingValues SINGLE_RIGHT_POINTING_ANGLE_QUOTATION_MARK =
          new QuotingValues("\u203A");
  public static final QuotingValues DOUBLE_LEFT_POINTING_ANGLE_QUOTATION_MARK =
          new QuotingValues("\u00AB");
  public static final QuotingValues DOUBLE_RIGHT_POINTING_ANGLE_QUOTATION_MARK =
          new QuotingValues("\u00BB");
  public static final QuotingValues SINGLE_LEFT_QUOTATION_MARK =
          new QuotingValues("\u2018");
  public static final QuotingValues SINGLE_RIGHT_QUOTATION_MARK =
          new QuotingValues("\u2019");
  public static final QuotingValues DOUBLE_LEFT_QUOTATION_MARK =
          new QuotingValues("\u201C");
  public static final QuotingValues DOUBLE_RIGHT_QUOTATION_MARK =
          new QuotingValues("\u201D");
  public static final QuotingValues DOUBLE_LOW9_QUOTATION_MARK =
          new QuotingValues("\u201E");

  private QuotingValues(final String value)
  {
    super(CSSStringType.STRING, value);
  }
}
