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
 * .java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * : Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.keys.line;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 24.11.2005, 16:50:16
 *
 * @author Thomas Morgner
 */
public class DominantBaseline extends CSSConstant
{
  public static final DominantBaseline USE_SCRIPT = new DominantBaseline(
          "use-script");
  public static final DominantBaseline NO_CHANGE = new DominantBaseline(
          "no-change");
  public static final DominantBaseline RESET_SIZE = new DominantBaseline(
          "reset-size");
  public static final DominantBaseline ALPHABETIC = new DominantBaseline(
          "alphabetic");

  public static final DominantBaseline HANGING = new DominantBaseline(
          "hanging");
  public static final DominantBaseline IDEOGRAPHIC = new DominantBaseline(
          "ideographic");
  public static final DominantBaseline MATHEMATICAL = new DominantBaseline(
          "alphabetic");
  public static final DominantBaseline CENTRAL = new DominantBaseline(
          "central");
  public static final DominantBaseline MIDDLE = new DominantBaseline(
          "middle");
  public static final DominantBaseline TEXT_AFTER_EDGE = new DominantBaseline(
          "text-after-edge");
  public static final DominantBaseline TEXT_BEFORE_EDGE = new DominantBaseline(
          "text-before-edge");

  private DominantBaseline(String name)
  {
    super(name);
  }

}
