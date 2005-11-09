package org.jfree.fonts;

/**
 * Creation-Date: 07.11.2005, 19:20:59
 *
 * @author Thomas Morgner
 */
public class StringUtilities
{
  /**
   * Default Constructor.
   */
  private StringUtilities ()
  {
  }

  /**
   * Helper functions to query a strings start portion. The comparison is case
   * insensitive.
   *
   * @param base  the base string.
   * @param start the starting text.
   * @return true, if the string starts with the given starting text.
   */
  public static boolean startsWithIgnoreCase (final String base, final String start)
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
   * @param base the base string.
   * @param end  the ending text.
   * @return true, if the string ends with the given ending text.
   */
  public static boolean endsWithIgnoreCase (final String base, final String end)
  {
    if (base.length() < end.length())
    {
      return false;
    }
    return base.regionMatches(true, base.length() - end.length(), end, 0, end.length());
  }

}
