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
 * ListStyleTypeNumeric.java
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
package org.jfree.layouting.input.style.keys.list;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 01.12.2005, 18:53:03
 *
 * @author Thomas Morgner
 */
public class ListStyleTypeNumeric extends CSSConstant
{
  public static final ListStyleTypeNumeric ARABIC_INDIC =
          new ListStyleTypeNumeric("arabic-indic");
  public static final ListStyleTypeNumeric BINARY =
          new ListStyleTypeNumeric("binary");
  public static final ListStyleTypeNumeric BENGALI =
          new ListStyleTypeNumeric("bengali");
  public static final ListStyleTypeNumeric CAMBODIAN =
          new ListStyleTypeNumeric("cambodian");
  public static final ListStyleTypeNumeric DECIMAL =
          new ListStyleTypeNumeric("decimal");
  public static final ListStyleTypeNumeric DECIMAL_LEADING_ZERO =
          new ListStyleTypeNumeric("decimal-leading-zero");
  public static final ListStyleTypeNumeric DEVANAGARI =
          new ListStyleTypeNumeric("devanagari");
  public static final ListStyleTypeNumeric GUJARATI =
          new ListStyleTypeNumeric("gujarati");
  public static final ListStyleTypeNumeric GURMUKHI =
          new ListStyleTypeNumeric("gurmukhi");
  public static final ListStyleTypeNumeric KANNADA =
          new ListStyleTypeNumeric("kannada");
  public static final ListStyleTypeNumeric KHMER =
          new ListStyleTypeNumeric("khmer");
  public static final ListStyleTypeNumeric LAO =
          new ListStyleTypeNumeric("lao");
  public static final ListStyleTypeNumeric LOWER_HEXADECIMAL =
          new ListStyleTypeNumeric("lower-hexadecimal");
  public static final ListStyleTypeNumeric MALAYALAM =
          new ListStyleTypeNumeric("malayalam");
  public static final ListStyleTypeNumeric MONGOLIAN =
          new ListStyleTypeNumeric("mongolian");
  public static final ListStyleTypeNumeric MYANMAR =
          new ListStyleTypeNumeric("myanmar");
  public static final ListStyleTypeNumeric OCTAL =
          new ListStyleTypeNumeric("octal");
  public static final ListStyleTypeNumeric ORIYA =
          new ListStyleTypeNumeric("oriya");
  public static final ListStyleTypeNumeric PERSIAN =
          new ListStyleTypeNumeric("persian");
  public static final ListStyleTypeNumeric TELUGU =
          new ListStyleTypeNumeric("telugu");
  public static final ListStyleTypeNumeric TIBETIAN =
          new ListStyleTypeNumeric("tibetian");
  public static final ListStyleTypeNumeric THAI =
          new ListStyleTypeNumeric("thai");
  public static final ListStyleTypeNumeric UPPER_HEXADECIMAL =
          new ListStyleTypeNumeric("upper-hexadecimal");
  public static final ListStyleTypeNumeric URDU =
          new ListStyleTypeNumeric("urdu");

  private ListStyleTypeNumeric(String name)
  {
    super(name);
  }
}
