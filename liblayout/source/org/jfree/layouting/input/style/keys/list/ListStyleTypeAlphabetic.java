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
 * ListStyleTypeAlphabetic.java
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
 * Creation-Date: 01.12.2005, 18:59:44
 *
 * @author Thomas Morgner
 */
public class ListStyleTypeAlphabetic extends CSSConstant
{
  // afar |  amharic | amharic-abegede | cjk-earthly-branch |
  public static final ListStyleTypeAlphabetic AFAR =
          new ListStyleTypeAlphabetic("afar");
  public static final ListStyleTypeAlphabetic AMHARIC =
          new ListStyleTypeAlphabetic("amharic");
  public static final ListStyleTypeAlphabetic AMHARIC_ABEGEDE =
          new ListStyleTypeAlphabetic("amharic-abegede");
  public static final ListStyleTypeAlphabetic CJK_EARTHLY_BRANCH =
          new ListStyleTypeAlphabetic("cjk-earthly-branch");

  // cjk-heavenly-stem | ethiopic | ethiopic-abegede |
  public static final ListStyleTypeAlphabetic CJK_HEAVENLY_STEM =
          new ListStyleTypeAlphabetic("cjk-heavenly-stem");
  public static final ListStyleTypeAlphabetic ETHIOPIC =
          new ListStyleTypeAlphabetic("ethiopic");
  public static final ListStyleTypeAlphabetic ETHIOPIC_ABEGEDE =
          new ListStyleTypeAlphabetic("ethiopic-abegede");

  // ethiopic-abegede-am-et |  ethiopic-abegede-gez |
  public static final ListStyleTypeAlphabetic ETHIOPIC_ABEGEDE_AM_ET =
          new ListStyleTypeAlphabetic("ethiopic-abegede-am-et");
  public static final ListStyleTypeAlphabetic ETHIOPIC_ABEGEDE_GEZ =
          new ListStyleTypeAlphabetic("ethiopic-abegede-gez");

  // ethiopic-abegede-ti-er |  ethiopic-abegede-ti-et |
  public static final ListStyleTypeAlphabetic ETHIOPIC_ABEGEDE_TI_ER =
          new ListStyleTypeAlphabetic("ethiopic-abegede-ti-er");
  public static final ListStyleTypeAlphabetic ETHIOPIC_ABEGEDE_TI_ET =
          new ListStyleTypeAlphabetic("ethiopic-abegede-ti-et");

  // ethiopic-halehame-aa-er | ethiopic-halehame-aa-et |
  public static final ListStyleTypeAlphabetic ETHIOPIC_HALEHAME_AA_ER =
          new ListStyleTypeAlphabetic("ethiopic-halehame-aa-er");
  public static final ListStyleTypeAlphabetic ETHIOPIC_HALEHAME_AA_ET =
          new ListStyleTypeAlphabetic("ethiopic-halehame-aa-et");

  // ethiopic-halehame-am-et | ethiopic-halehame-gez |
  public static final ListStyleTypeAlphabetic ETHIOPIC_HALEHAME_AM_ET =
          new ListStyleTypeAlphabetic("ethiopic-halehame-am-et");
  public static final ListStyleTypeAlphabetic ETHIOPIC_HALEHAME_GEZ =
          new ListStyleTypeAlphabetic("ethiopic-halehame-gez");

  // ethiopic-halehame-om-et | ethiopic-halehame-sid-et |
  public static final ListStyleTypeAlphabetic ETHIOPIC_HALEHAME_OM_ET =
          new ListStyleTypeAlphabetic("ethiopic-halehame-om-et");
  public static final ListStyleTypeAlphabetic ETHIOPIC_HALEHAME_SID_ET =
          new ListStyleTypeAlphabetic("ethiopic-halehame-sid-et");

  // ethiopic-halehame-so-et |  ethiopic-halehame-ti-er |
  public static final ListStyleTypeAlphabetic ETHIOPIC_HALEHAME_SO_ET =
          new ListStyleTypeAlphabetic("ethiopic-halehame-so-et");
  public static final ListStyleTypeAlphabetic ETHIOPIC_HALEHAME_TI_ER =
          new ListStyleTypeAlphabetic("ethiopic-halehame-ti-er");

  // ethiopic-halehame-ti-et | ethiopic-halehame-tig |
  public static final ListStyleTypeAlphabetic ETHIOPIC_HALEHAME_TI_ET =
          new ListStyleTypeAlphabetic("ethiopic-halehame-ti-et");
  public static final ListStyleTypeAlphabetic ETHIOPIC_HALEHAME_TIG =
          new ListStyleTypeAlphabetic("ethiopic-halehame-tig");

  // hangul | hangul-consonant | hiragana | hiragana-iroha |
  public static final ListStyleTypeAlphabetic HANGUL =
          new ListStyleTypeAlphabetic("hangul");
  public static final ListStyleTypeAlphabetic HANGUL_CONSONANT =
          new ListStyleTypeAlphabetic("hangul-consonant");
  public static final ListStyleTypeAlphabetic HIRAGANA =
          new ListStyleTypeAlphabetic("hiragana");
  public static final ListStyleTypeAlphabetic HIRAGANA_IROHA =
          new ListStyleTypeAlphabetic("hiragana-iroha");

  // katakana | katakana-iroha | lower-alpha | lower-greek |
  public static final ListStyleTypeAlphabetic KATAKANA =
          new ListStyleTypeAlphabetic("katakana");
  public static final ListStyleTypeAlphabetic KATAKANA_IROHA =
          new ListStyleTypeAlphabetic("katakana-iroha");
  public static final ListStyleTypeAlphabetic LOWER_ALPHA =
          new ListStyleTypeAlphabetic("lower-alpha");
  public static final ListStyleTypeAlphabetic LOWER_GREEK =
          new ListStyleTypeAlphabetic("lower-greek");

  // lower-norwegian | lower-latin | oromo |  sidama |  somali |
  public static final ListStyleTypeAlphabetic LOWER_NORWEGIAN =
          new ListStyleTypeAlphabetic("lower-norwegian");
  public static final ListStyleTypeAlphabetic LOWER_LATIN =
          new ListStyleTypeAlphabetic("lower-latin");
  public static final ListStyleTypeAlphabetic OROMO =
          new ListStyleTypeAlphabetic("oromo");
  public static final ListStyleTypeAlphabetic SIDAMA =
          new ListStyleTypeAlphabetic("sidama");
  public static final ListStyleTypeAlphabetic SOMALI =
          new ListStyleTypeAlphabetic("somali");

  // tigre |  tigrinya-er | tigrinya-er-abegede |  tigrinya-et |
  public static final ListStyleTypeAlphabetic TIGRE =
          new ListStyleTypeAlphabetic("tigre");
  public static final ListStyleTypeAlphabetic TIGRINYA_ER =
          new ListStyleTypeAlphabetic("tigrinya-er");
  public static final ListStyleTypeAlphabetic TIGRINYA_ER_ABEGEDE =
          new ListStyleTypeAlphabetic("tigrinya-er-abegede");
  public static final ListStyleTypeAlphabetic TIGRINYA_ET =
          new ListStyleTypeAlphabetic("tigrinya-et");
  // tigrinya-et-abegede |  upper-alpha | upper-greek |
  public static final ListStyleTypeAlphabetic TIGRINYA_ET_ABEGEDE =
          new ListStyleTypeAlphabetic("tigrinya-et-abegede");
  public static final ListStyleTypeAlphabetic UPPER_ALPHA =
          new ListStyleTypeAlphabetic("upper-alpha");
  public static final ListStyleTypeAlphabetic UPPER_GREEK =
          new ListStyleTypeAlphabetic("upper-greek");
  // upper-norwegian | upper-latin
  public static final ListStyleTypeAlphabetic UPPER_NORWEGIAN =
          new ListStyleTypeAlphabetic("upper-norwegian");
  public static final ListStyleTypeAlphabetic UPPER_LATIN =
          new ListStyleTypeAlphabetic("upper-latin");

  private ListStyleTypeAlphabetic(final String constant)
  {
    super(constant);
  }
}
