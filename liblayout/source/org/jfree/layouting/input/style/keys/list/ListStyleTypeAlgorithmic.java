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
 * ListStyleTypeAlgorithmic.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: ListStyleTypeAlgorithmic.java,v 1.1 2006/02/12 21:54:27 taqua Exp $
 *
 * Changes
 * -------------------------
 * 01.12.2005 : Initial version
 */
package org.jfree.layouting.input.style.keys.list;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 01.12.2005, 18:42:51
 *
 * @author Thomas Morgner
 */
public class ListStyleTypeAlgorithmic extends CSSConstant
{
  // armenian | cjk-ideographic | ethiopic-numeric | georgian |
  // hebrew | japanese-formal | japanese-informal |   lower-armenian |
  // lower-roman | simp-chinese-formal | simp-chinese-informal |
  // syriac | tamil | trad-chinese-formal | trad-chinese-informal |
  // upper-armenian | upper-roman

  public static final ListStyleTypeAlgorithmic ARMENIAN =
          new ListStyleTypeAlgorithmic("armenian");
  public static final ListStyleTypeAlgorithmic CJK_IDEOGRAPHIC =
          new ListStyleTypeAlgorithmic("cjk-ideographic");
  public static final ListStyleTypeAlgorithmic ETHIOPIC_NUMERIC =
          new ListStyleTypeAlgorithmic("ethiopic-numeric");
  public static final ListStyleTypeAlgorithmic GEORGIAN =
          new ListStyleTypeAlgorithmic("georgian");
  public static final ListStyleTypeAlgorithmic HEBREW =
          new ListStyleTypeAlgorithmic("hebrew");
  public static final ListStyleTypeAlgorithmic JAPANESE_FORMAL =
          new ListStyleTypeAlgorithmic("japanese-formal");
  public static final ListStyleTypeAlgorithmic JAPANESE_INFORMAL =
          new ListStyleTypeAlgorithmic("japanese-informal");
  public static final ListStyleTypeAlgorithmic LOWER_ARMENIAN =
          new ListStyleTypeAlgorithmic("lower-armenian");
  public static final ListStyleTypeAlgorithmic LOWER_ROMAN =
          new ListStyleTypeAlgorithmic("lower-roman");
  public static final ListStyleTypeAlgorithmic SIMP_CHINESE_FORMAL =
          new ListStyleTypeAlgorithmic("simp-chinese-formal");
  public static final ListStyleTypeAlgorithmic SIMP_CHINESE_INFORMAL =
          new ListStyleTypeAlgorithmic("simp-chinese-informal");
  public static final ListStyleTypeAlgorithmic TRAD_CHINESE_FORMAL =
          new ListStyleTypeAlgorithmic("trad-chinese-formal");
  public static final ListStyleTypeAlgorithmic TRAD_CHINESE_INFORMAL =
          new ListStyleTypeAlgorithmic("trad-chinese-informal");
  public static final ListStyleTypeAlgorithmic UPPER_ARMENIAN =
          new ListStyleTypeAlgorithmic("upper-armenian");
  public static final ListStyleTypeAlgorithmic UPPER_ROMAN =
          new ListStyleTypeAlgorithmic("upper-roman");
  public static final ListStyleTypeAlgorithmic SYRIAC =
          new ListStyleTypeAlgorithmic("syriac");
  public static final ListStyleTypeAlgorithmic TAMIL =
          new ListStyleTypeAlgorithmic("tamil");

  private ListStyleTypeAlgorithmic(final String constant)
  {
    super(constant);
  }
}
