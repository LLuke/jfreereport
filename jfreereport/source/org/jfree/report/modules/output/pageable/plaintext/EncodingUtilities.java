/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * EncodingUtilities.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.output.pageable.plaintext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class EncodingUtilities
{
  /**
   * The encoding name.
   */
  private String encoding;
  /**
   * The header sequence of the encoding (-2, -1 for UTF-16).
   */
  private byte[] header;
  /**
   * A single encoded space character.
   */
  private byte[] space;
  /**
   * The header plus the single space.
   */
  private byte[] encodingHeader;

  public EncodingUtilities (final String codepage)
          throws UnsupportedEncodingException
  {
    this.encoding = codepage;

    encodingHeader = " ".getBytes(codepage);
    final byte[] spacesWithHeader = "  ".getBytes(codepage);
    final int spaceCharLength = spacesWithHeader.length - encodingHeader.length;
    space = new byte[spaceCharLength];
    header = new byte[encodingHeader.length - spaceCharLength];

    System.arraycopy(spacesWithHeader, encodingHeader.length, space, 0, spaceCharLength);
    System.arraycopy(encodingHeader, 0, header, 0, header.length);
  }

  public byte[] getSpace ()
  {
    return space;
  }

  public byte[] getHeader ()
  {
    return header;
  }

  /**
   * Writes encoded text for the current encoding into the output stream.
   *
   * @param textString the text that should be written.
   * @throws java.io.IOException if an error occures.
   */
  public void writeEncodedText
          (final char[] textString, final OutputStream out)
          throws IOException
  {
    final StringBuffer buffer = new StringBuffer(" ");
    buffer.append(textString);
    final byte[] text = buffer.toString().getBytes(encoding);
    out.write(text, encodingHeader.length, text.length - encodingHeader.length);
  }


  /**
   * Writes encoded text for the current encoding into the output stream.
   *
   * @param textString the text that should be written.
   * @throws java.io.IOException if an error occures.
   */
  public void writeEncodedText
          (final String textString, final OutputStream out)
          throws IOException
  {
    final StringBuffer buffer = new StringBuffer(" ");
    buffer.append(textString);
    final byte[] text = buffer.toString().getBytes(encoding);
    out.write(text, encodingHeader.length, text.length - encodingHeader.length);
  }

  public String getEncoding ()
  {
    return encoding;
  }

  public byte[] getEncodingHeader ()
  {
    return header;
  }
}
