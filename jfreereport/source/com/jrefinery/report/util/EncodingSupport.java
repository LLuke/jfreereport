/**
 * Date: Mar 13, 2003
 * Time: 7:32:40 PM
 *
 * $Id$
 */
package com.jrefinery.report.util;

import java.util.Hashtable;
import java.io.UnsupportedEncodingException;

/**
 * A global registry for all supported encodings.
 */
public class EncodingSupport
{
  /** Storage for the known encodings. */
  private static Hashtable knownEncodings;

  /** the string that should be encoded */
  private static final String TEST_STRING = " ";

  /**
   * Returns <code>true</code> if the encoding is valid, and <code>false</code> otherwise.
   *
   * @param encoding  the encoding (name).
   *
   * @return A boolean.
   */
  public static boolean isSupportedEncoding (String encoding)
  {
    if (encoding == null)
    {
      throw new NullPointerException();
    }
    if (knownEncodings == null)
    {
      knownEncodings = new Hashtable();
    }

    Boolean value = (Boolean) knownEncodings.get(encoding);
    if (value != null)
    {
      return value.booleanValue();
    }

    try
    {
      TEST_STRING.getBytes(encoding);
      knownEncodings.put (encoding, Boolean.TRUE);
      return true;
    }
    catch (UnsupportedEncodingException ue)
    {
      knownEncodings.put (encoding, Boolean.FALSE);
      Log.info (new Log.SimpleMessage ("Encoding ", encoding, " is not supported."));
      return false;
    }
  }

}
