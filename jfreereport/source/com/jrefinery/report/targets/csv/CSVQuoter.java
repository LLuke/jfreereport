/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ----------------
 * CSVQuoter.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CSVQuoter.java,v 1.5 2003/02/08 20:43:45 taqua Exp $
 *
 * Changes
 * -------
 * 18-Jan-2003 : Initial Version
 * 09-Feb-2003 : Documentation
 */
package com.jrefinery.report.targets.csv;

/**
 * The CSVQuoter is a helper class to encode a string for the CSV-fileformat.
 */
public class CSVQuoter
{
  /** the separator used in the CSV file */
  private String separator;

  /**
   * Creates a new CSVQuoter, which uses a colon as default separator.
   */
  public CSVQuoter()
  {
    this (",");
  }

  /**
   * Creates a new CSVQuoter, which uses the defined separator.
   *
   * @param separator the separator.
   * @throws NullPointerException if the given separator is null
   */
  public CSVQuoter(String separator)
  {
    setSeparator(separator);
  }

  /**
   * Encodes the string, so that the string can safely be used in CSV files.
   * If the string does not need quoting, the original string is returned unchanged.
   *
   * @param original the unquoted string
   * @return the quoted string
   * @see CSVQuoter#applyQuote
   */
  public String doQuoting(String original)
  {
    if (isQuotingNeeded(original))
    {
      StringBuffer retval = new StringBuffer();
      retval.append("\"");
      applyQuote(retval, original);
      retval.append("\"");
      return retval.toString();
    }
    else
      return original;
  }

  /**
   * Decodes the string, so that all escape sequences get removed.
   * If the string was not quoted, then the string is returned unchanged.
   *
   * @param nativeString the quoted string
   * @return the unquoted string.
   */
  public String undoQuoting(String nativeString)
  {
    if (isQuotingNeeded(nativeString))
    {
      StringBuffer b = new StringBuffer(nativeString.length());
      int length = nativeString.length() - 1;
      int start = 1;

      int pos = start;
      while (pos != -1)
      {
        pos = nativeString.indexOf("\"\"", start);
        if (pos == -1)
        {
          b.append(nativeString.substring(start, length));
        }
        else
        {
          b.append(nativeString.substring(start, pos));
          start = pos + 1;
        }
      }
      return b.toString();
    }
    else
      return nativeString;
  }

  /**
   * Tests, whether this string needs to be quoted. A string is encoded if
   * the string contains a newline character, a quote character or the defined
   * separator.
   *
   * @param str the string that should be tested.
   * @return true, if quoting needs to be applied, false otherwise.
   */
  private boolean isQuotingNeeded(String str)
  {
    if (str.indexOf(separator) != -1)
      return true;

    if (str.indexOf('\n') != -1)
      return true;

    if (str.indexOf('\"', 1) != -1)
      return true;

    return false;
  }

  /**
   * Applies the quoting to a given string, and stores the result in the
   * StringBuffer <code>b</code>
   *
   * @param b the result buffer
   * @param original the string, that should be quoted.
   */
  private void applyQuote(StringBuffer b, String original)
  {
    // This solution needs improvements. Copy blocks instead of single
    // characters.
    int length = original.length();

    for (int i = 0; i < length; i++)
    {
      char c = original.charAt(i);
      if (c == '"')
      {
        b.append("\"\"");
      }
      else
        b.append(c);
    }
  }

  /**
   * Gets the separator used in this quoter and the CSV file.
   *
   * @return the separator, never null
   */
  public String getSeparator()
  {
    return separator;
  }

  /**
   * Defines the separator, which is used in the CSV file. If you use
   * different separators for quoting and writing, the resulting file will
   * be invalid.
   *
   * @param separator
   */
  public void setSeparator(String separator)
  {
    if (separator == null) throw new NullPointerException();
    this.separator = separator;
  }
}
