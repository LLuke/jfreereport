/**
 * ----------------------
 * StringUtil.java
 * ----------------------
 * 
 * ChangeLog
 * ---------
 */
package com.jrefinery.report.util;

public class StringUtil
{
  /**
   * Helper functions to query a strings start portion.
   *
   * @see String#startsWith
   */
  public static boolean startsWithIgnoreCase(String base, String start)
  {
    return base.regionMatches(true, 0, start, 0, start.length());
  }

  /**
   * Helper functions to query a strings end portion.
   *
   * @see String#endsWith
   */
  public static boolean endsWithIgnoreCase(String base, String end)
  {
    if (base.length() < end.length())
    {
      return false;
    }
    return base.regionMatches(true, base.length() - end.length(), end, 0, end.length());
  }


}
