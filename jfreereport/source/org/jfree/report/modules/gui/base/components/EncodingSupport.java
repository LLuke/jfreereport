/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * --------------------
 * EncodingSupport.java
 * --------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: EncodingSupport.java,v 1.3 2003/08/24 15:08:18 taqua Exp $
 *
 * Changes
 * -------
 * 13.03.2003 : Initial version
 *
 */
package org.jfree.report.modules.gui.base.components;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.jfree.util.Log;

/**
 * A global registry for all supported encodings.
 *
 * @author Thomas Morgner.
 */
public final class EncodingSupport
{
  /**
   * Default Constructor.
   */
  private EncodingSupport()
  {
  }

  /** Storage for the known encodings. */
  private static HashMap knownEncodings;

  /** the string that should be encoded. */
  private static final String TEST_STRING = " ";

  /**
   * Returns <code>true</code> if the encoding is valid, and <code>false</code> otherwise.
   *
   * @param encoding  the encoding (name).
   *
   * @return A boolean.
   */
  public static boolean isSupportedEncoding(final String encoding)
  {
    if (encoding == null)
    {
      throw new NullPointerException();
    }
    if (knownEncodings == null)
    {
      knownEncodings = new HashMap();
    }

    final Boolean value = (Boolean) knownEncodings.get(encoding);
    if (value != null)
    {
      return value.booleanValue();
    }

    try
    {
      TEST_STRING.getBytes(encoding);
      knownEncodings.put(encoding, Boolean.TRUE);
      return true;
    }
    catch (UnsupportedEncodingException ue)
    {
      knownEncodings.put(encoding, Boolean.FALSE);
      Log.info(new Log.SimpleMessage("Encoding ", encoding, " is not supported."));
      return false;
    }
  }

}
