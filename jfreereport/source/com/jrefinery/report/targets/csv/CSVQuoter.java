/**
 * Date: Jan 18, 2003
 * Time: 9:04:24 PM
 *
 * $Id: CSVQuoter.java,v 1.1 2003/01/18 20:47:35 taqua Exp $
 */
package com.jrefinery.report.targets.csv;

import com.jrefinery.report.util.ReportConfiguration;

public class CSVQuoter
{
  private String separator;

  public CSVQuoter(String separator)
  {
    this.separator = separator;
  }

  public CSVQuoter()
  {
    this(ReportConfiguration.getGlobalConfig().getConfigProperty("quoting.excelcsv.separator", ","));
  }

  public String doQuoting(String original)
  {
    boolean quoting = isQuotingNeeded(original);
    if (quoting)
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

  public String undoQuoting(String nativeString)
  {
    boolean quoting = isQuotingNeeded(nativeString);
    if (quoting)
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
   * This solution needs improvements. Copy blocks instead of single
   * characters.
   */
  private void applyQuote(StringBuffer b, String original)
  {
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

  public String getSeparator()
  {
    return separator;
  }

  public void setSeparator(String separator)
  {
    this.separator = separator;
  }
}
