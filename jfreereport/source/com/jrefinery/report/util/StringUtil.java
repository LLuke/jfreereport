/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ---------------
 * StringUtil.java
 * ---------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StringUtil.java,v 1.10 2003/06/27 14:25:26 taqua Exp $
 *
 * Changes
 * -------
 * 10-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 * 25-Jan-2003 : Added URL-Encoding and CSS-Encoding methods to support HTML-Output
 */

package com.jrefinery.report.util;

/**
 * String utility functions.
 *
 * @author Thomas Morgner
 */
public class StringUtil
{
  /**
   * Helper functions to query a strings start portion. The comparison is case insensitive.
   *
   * @param base  the base string.
   * @param start  the starting text.
   *
   * @return true, if the string starts with the given starting text.
   */
  public static boolean startsWithIgnoreCase(final String base, final String start)
  {
    if (base.length() < start.length())
    {
      return false;
    }
    return base.regionMatches(true, 0, start, 0, start.length());
  }

  /**
   * Helper functions to query a strings end portion. The comparison is case insensitive.
   *
   * @param base  the base string.
   * @param end  the ending text.
   *
   * @return true, if the string ends with the given ending text.
   */
  public static boolean endsWithIgnoreCase(final String base, final String end)
  {
    if (base.length() < end.length())
    {
      return false;
    }
    return base.regionMatches(true, base.length() - end.length(), end, 0, end.length());
  }

  /**
   * Provides a method to encode any string into a URL-safe form.
   * Non-ASCII characters are first encoded as sequences of two or three bytes, using the
   * UTF-8 algorithm, before being encoded as %HH escapes.
   */
  private static final String[] HEX_URL_ENCODING = {
    "%00", "%01", "%02", "%03", "%04", "%05", "%06", "%07",
    "%08", "%09", "%0a", "%0b", "%0c", "%0d", "%0e", "%0f",
    "%10", "%11", "%12", "%13", "%14", "%15", "%16", "%17",
    "%18", "%19", "%1a", "%1b", "%1c", "%1d", "%1e", "%1f",
    "%20", "%21", "%22", "%23", "%24", "%25", "%26", "%27",
    "%28", "%29", "%2a", "%2b", "%2c", "%2d", "%2e", "%2f",
    "%30", "%31", "%32", "%33", "%34", "%35", "%36", "%37",
    "%38", "%39", "%3a", "%3b", "%3c", "%3d", "%3e", "%3f",
    "%40", "%41", "%42", "%43", "%44", "%45", "%46", "%47",
    "%48", "%49", "%4a", "%4b", "%4c", "%4d", "%4e", "%4f",
    "%50", "%51", "%52", "%53", "%54", "%55", "%56", "%57",
    "%58", "%59", "%5a", "%5b", "%5c", "%5d", "%5e", "%5f",
    "%60", "%61", "%62", "%63", "%64", "%65", "%66", "%67",
    "%68", "%69", "%6a", "%6b", "%6c", "%6d", "%6e", "%6f",
    "%70", "%71", "%72", "%73", "%74", "%75", "%76", "%77",
    "%78", "%79", "%7a", "%7b", "%7c", "%7d", "%7e", "%7f",
    "%80", "%81", "%82", "%83", "%84", "%85", "%86", "%87",
    "%88", "%89", "%8a", "%8b", "%8c", "%8d", "%8e", "%8f",
    "%90", "%91", "%92", "%93", "%94", "%95", "%96", "%97",
    "%98", "%99", "%9a", "%9b", "%9c", "%9d", "%9e", "%9f",
    "%a0", "%a1", "%a2", "%a3", "%a4", "%a5", "%a6", "%a7",
    "%a8", "%a9", "%aa", "%ab", "%ac", "%ad", "%ae", "%af",
    "%b0", "%b1", "%b2", "%b3", "%b4", "%b5", "%b6", "%b7",
    "%b8", "%b9", "%ba", "%bb", "%bc", "%bd", "%be", "%bf",
    "%c0", "%c1", "%c2", "%c3", "%c4", "%c5", "%c6", "%c7",
    "%c8", "%c9", "%ca", "%cb", "%cc", "%cd", "%ce", "%cf",
    "%d0", "%d1", "%d2", "%d3", "%d4", "%d5", "%d6", "%d7",
    "%d8", "%d9", "%da", "%db", "%dc", "%dd", "%de", "%df",
    "%e0", "%e1", "%e2", "%e3", "%e4", "%e5", "%e6", "%e7",
    "%e8", "%e9", "%ea", "%eb", "%ec", "%ed", "%ee", "%ef",
    "%f0", "%f1", "%f2", "%f3", "%f4", "%f5", "%f6", "%f7",
    "%f8", "%f9", "%fa", "%fb", "%fc", "%fd", "%fe", "%ff"
  };

  /**
   * Encode a string to the "x-www-form-urlencoded" form, enhanced
   * with the UTF-8-in-URL proposal. This is what happens:
   *
   * <ul>
   * <li><p>The ASCII characters 'a' through 'z', 'A' through 'Z',
   *        and '0' through '9' remain the same.
   *
   * <li><p>The unreserved characters - _ . ! ~ * ' ( ) remain the same.
   *
   * <li><p>The space character ' ' is converted into a plus sign '+'.
   *
   * <li><p>All other ASCII characters are converted into the
   *        3-character string "%xy", where xy is
   *        the two-digit hexadecimal representation of the character
   *        code
   *
   * <li><p>All non-ASCII characters are encoded in two steps: first
   *        to a sequence of 2 or 3 bytes, using the UTF-8 algorithm;
   *        secondly each of these bytes is encoded as "%xx".
   * </ul>
   *
   * @param s The string to be encoded
   * @return The encoded string
   */
  public static String encodeUTF(final String s)
  {
    final StringBuffer sbuf = new StringBuffer();
    final int len = s.length();
    for (int i = 0; i < len; i++)
    {
      final int ch = s.charAt(i);
      if ('A' <= ch && ch <= 'Z')
      { // 'A'..'Z'
        sbuf.append((char) ch);
      }
      else if ('a' <= ch && ch <= 'z')
      { // 'a'..'z'
        sbuf.append((char) ch);
      }
      else if ('0' <= ch && ch <= '9')
      { // '0'..'9'
        sbuf.append((char) ch);
      }
      else if (ch == ' ')
      {  // space
        sbuf.append('+');
      }
      else if (ch == '-' || ch == '_'  // unreserved
          || ch == '.' || ch == '!'
          || ch == '~' || ch == '*'
          || ch == '\'' || ch == '('
          || ch == ')')
      {
        sbuf.append((char) ch);
      }
      else if (ch <= 0x007f)
      {  // other ASCII
        sbuf.append(HEX_URL_ENCODING[ch]);
      }
      else if (ch <= 0x07FF)
      {  // non-ASCII <= 0x7FF
        sbuf.append(HEX_URL_ENCODING[0xc0 | (ch >> 6)]);
        sbuf.append(HEX_URL_ENCODING[0x80 | (ch & 0x3F)]);
      }
      else
      {  // 0x7FF < ch <= 0xFFFF
        sbuf.append(HEX_URL_ENCODING[0xe0 | (ch >> 12)]);
        sbuf.append(HEX_URL_ENCODING[0x80 | ((ch >> 6) & 0x3F)]);
        sbuf.append(HEX_URL_ENCODING[0x80 | (ch & 0x3F)]);
      }
    }
    return sbuf.toString();
  }


  /**
   * Provides a method to encode any string into a URL-safe
   * form.
   * Non-ASCII characters are first encoded as sequences of
   * two or three bytes, using the UTF-8 algorithm, before being
   * encoded as %HH escapes.
   */
  private static final String[] HEX_CSS_ENCODING = {
    "\\00 ", "\\01 ", "\\02 ", "\\03 ", "\\04 ", "\\05 ", "\\06 ", "\\07 ",
    "\\08 ", "\\09 ", "\\0a ", "\\0b ", "\\0c ", "\\0d ", "\\0e ", "\\0f ",
    "\\10 ", "\\11 ", "\\12 ", "\\13 ", "\\14 ", "\\15 ", "\\16 ", "\\17 ",
    "\\18 ", "\\19 ", "\\1a ", "\\1b ", "\\1c ", "\\1d ", "\\1e ", "\\1f ",
    "\\20 ", "\\21 ", "\\22 ", "\\23 ", "\\24 ", "\\25 ", "\\26 ", "\\27 ",
    "\\28 ", "\\29 ", "\\2a ", "\\2b ", "\\2c ", "\\2d ", "\\2e ", "\\2f ",
    "\\30 ", "\\31 ", "\\32 ", "\\33 ", "\\34 ", "\\35 ", "\\36 ", "\\37 ",
    "\\38 ", "\\39 ", "\\3a ", "\\3b ", "\\3c ", "\\3d ", "\\3e ", "\\3f ",
    "\\40 ", "\\41 ", "\\42 ", "\\43 ", "\\44 ", "\\45 ", "\\46 ", "\\47 ",
    "\\48 ", "\\49 ", "\\4a ", "\\4b ", "\\4c ", "\\4d ", "\\4e ", "\\4f ",
    "\\50 ", "\\51 ", "\\52 ", "\\53 ", "\\54 ", "\\55 ", "\\56 ", "\\57 ",
    "\\58 ", "\\59 ", "\\5a ", "\\5b ", "\\5c ", "\\5d ", "\\5e ", "\\5f ",
    "\\60 ", "\\61 ", "\\62 ", "\\63 ", "\\64 ", "\\65 ", "\\66 ", "\\67 ",
    "\\68 ", "\\69 ", "\\6a ", "\\6b ", "\\6c ", "\\6d ", "\\6e ", "\\6f ",
    "\\70 ", "\\71 ", "\\72 ", "\\73 ", "\\74 ", "\\75 ", "\\76 ", "\\77 ",
    "\\78 ", "\\79 ", "\\7a ", "\\7b ", "\\7c ", "\\7d ", "\\7e ", "\\7f ",
    "\\80 ", "\\81 ", "\\82 ", "\\83 ", "\\84 ", "\\85 ", "\\86 ", "\\87 ",
    "\\88 ", "\\89 ", "\\8a ", "\\8b ", "\\8c ", "\\8d ", "\\8e ", "\\8f ",
    "\\90 ", "\\91 ", "\\92 ", "\\93 ", "\\94 ", "\\95 ", "\\96 ", "\\97 ",
    "\\98 ", "\\99 ", "\\9a ", "\\9b ", "\\9c ", "\\9d ", "\\9e ", "\\9f ",
    "\\a0 ", "\\a1 ", "\\a2 ", "\\a3 ", "\\a4 ", "\\a5 ", "\\a6 ", "\\a7 ",
    "\\a8 ", "\\a9 ", "\\aa ", "\\ab ", "\\ac ", "\\ad ", "\\ae ", "\\af ",
    "\\b0 ", "\\b1 ", "\\b2 ", "\\b3 ", "\\b4 ", "\\b5 ", "\\b6 ", "\\b7 ",
    "\\b8 ", "\\b9 ", "\\ba ", "\\bb ", "\\bc ", "\\bd ", "\\be ", "\\bf ",
    "\\c0 ", "\\c1 ", "\\c2 ", "\\c3 ", "\\c4 ", "\\c5 ", "\\c6 ", "\\c7 ",
    "\\c8 ", "\\c9 ", "\\ca ", "\\cb ", "\\cc ", "\\cd ", "\\ce ", "\\cf ",
    "\\d0 ", "\\d1 ", "\\d2 ", "\\d3 ", "\\d4 ", "\\d5 ", "\\d6 ", "\\d7 ",
    "\\d8 ", "\\d9 ", "\\da ", "\\db ", "\\dc ", "\\dd ", "\\de ", "\\df ",
    "\\e0 ", "\\e1 ", "\\e2 ", "\\e3 ", "\\e4 ", "\\e5 ", "\\e6 ", "\\e7 ",
    "\\e8 ", "\\e9 ", "\\ea ", "\\eb ", "\\ec ", "\\ed ", "\\ee ", "\\ef ",
    "\\f0 ", "\\f1 ", "\\f2 ", "\\f3 ", "\\f4 ", "\\f5 ", "\\f6 ", "\\f7 ",
    "\\f8 ", "\\f9 ", "\\fa ", "\\fb ", "\\fc ", "\\fd ", "\\fe ", "\\ff "
  };

  /**
   * Encode a string to the "x-www-form-urlencoded" form, enhanced
   * with the UTF-8-in-URL proposal. This is what happens:
   *
   * <ul>
   * <li><p>The ASCII characters 'a' through 'z', 'A' through 'Z',
   *        and '0' through '9' remain the same.
   *
   * <li><p>The unreserved characters - _ . ! ~ * ' ( ) remain the same.
   *
   * <li><p>The space character ' ' is converted into a plus sign '+'.
   *
   * <li><p>All other ASCII characters are converted into the
   *        3-character string "%xy", where xy is
   *        the two-digit hexadecimal representation of the character
   *        code
   *
   * <li><p>All non-ASCII characters are encoded in two steps: first
   *        to a sequence of 2 or 3 bytes, using the UTF-8 algorithm;
   *        secondly each of these bytes is encoded as "%xx".
   * </ul>
   *
   * @param s The string to be encoded
   * @return The encoded string
   */
  public static String encodeCSS(final String s)
  {
    final StringBuffer sbuf = new StringBuffer();
    final int len = s.length();
    for (int i = 0; i < len; i++)
    {
      final int ch = s.charAt(i);
      if ('A' <= ch && ch <= 'Z')
      {  // 'A'..'Z'
        sbuf.append((char) ch);
      }
      else if ('a' <= ch && ch <= 'z')
      {  // 'a'..'z'
        sbuf.append((char) ch);
      }
      else if ('0' <= ch && ch <= '9')
      {  // '0'..'9'
        sbuf.append((char) ch);
      }
      else if (ch == '-')
      {
        sbuf.append((char) ch);
      }
      else if (ch <= 0x007f)
      {  // other ASCII
        sbuf.append(HEX_CSS_ENCODING[ch]);
      }
      else if (ch <= 0x07FF)
      {  // non-ASCII <= 0x7FF
        sbuf.append(HEX_CSS_ENCODING[0xc0 | (ch >> 6)]);
        sbuf.append(HEX_CSS_ENCODING[0x80 | (ch & 0x3F)]);
      }
      else
      {  // 0x7FF < ch <= 0xFFFF
        sbuf.append(HEX_CSS_ENCODING[0xe0 | (ch >> 12)]);
        sbuf.append(HEX_CSS_ENCODING[0x80 | ((ch >> 6) & 0x3F)]);
        sbuf.append(HEX_CSS_ENCODING[0x80 | (ch & 0x3F)]);
      }
    }
    return sbuf.toString();
  }

}
