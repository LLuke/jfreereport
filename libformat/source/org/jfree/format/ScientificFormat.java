package org.jfree.format;

import java.text.Format;
import java.text.FieldPosition;
import java.text.ParsePosition;

public class ScientificFormat extends Format
{
  public ScientificFormat ()
  {
  }

  /**
   * Formats an object and appends the resulting text to a given string buffer. If the
   * <code>pos</code> argument identifies a field used by the format, then its indices are
   * set to the beginning and end of the first such field encountered.
   *
   * @param obj        The object to format
   * @param toAppendTo where the text is to be appended
   * @param pos        A <code>FieldPosition</code> identifying a field in the formatted
   *                   text
   * @return the string buffer passed in as <code>toAppendTo</code>, with formatted text
   *         appended
   *
   * @throws NullPointerException     if <code>toAppendTo</code> or <code>pos</code> is
   *                                  null
   * @throws IllegalArgumentException if the Format cannot format the given object
   */
  public StringBuffer format (Object obj, StringBuffer toAppendTo, FieldPosition pos)
  {
    return toAppendTo.append(obj);
  }

  /**
   * Parses text from a string to produce an object.
   * <p/>
   * The method attempts to parse text starting at the index given by <code>pos</code>. If
   * parsing succeeds, then the index of <code>pos</code> is updated to the index after
   * the last character used (parsing does not necessarily use all characters up to the
   * end of the string), and the parsed object is returned. The updated <code>pos</code>
   * can be used to indicate the starting point for the next call to this method. If an
   * error occurs, then the index of <code>pos</code> is not changed, the error index of
   * <code>pos</code> is set to the index of the character where the error occurred, and
   * null is returned.
   *
   * @param source A <code>String</code>, part of which should be parsed.
   * @param pos    A <code>ParsePosition</code> object with index and error index
   *               information as described above.
   * @return An <code>Object</code> parsed from the string. In case of error, returns
   *         null.
   *
   * @throws NullPointerException if <code>pos</code> is null.
   */
  public Object parseObject (String source, ParsePosition pos)
  {
    // todo implement me
    return null;
  }
}
