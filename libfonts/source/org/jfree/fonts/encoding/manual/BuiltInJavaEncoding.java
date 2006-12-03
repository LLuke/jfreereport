/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libfonts/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.fonts.encoding.manual;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.jfree.fonts.encoding.ByteBuffer;
import org.jfree.fonts.encoding.CodePointBuffer;
import org.jfree.fonts.encoding.Encoding;
import org.jfree.fonts.encoding.EncodingErrorType;
import org.jfree.fonts.encoding.EncodingException;

public class BuiltInJavaEncoding implements Encoding
{
  private String name;
  private boolean fastMode;

  public BuiltInJavaEncoding (String name, final boolean fastMode)
  {
    if (name == null)
    {
      throw new NullPointerException();
    }
    this.name = name;
    this.fastMode = fastMode;
  }

  public CodePointBuffer decode (ByteBuffer text, CodePointBuffer buffer)
          throws EncodingException
  {
    try
    {
      final byte[] rawData = text.getData();
      final String decoded =
              new String (rawData, text.getOffset(), text.getLength(), name);
      return Utf16LE.getInstance().decodeString(decoded, buffer);
    }
    catch (UnsupportedEncodingException e)
    {
      // this should not happen, as the encodings should have been checked by
      // the system already ...
      throw new EncodingException("Failed to encode the string: " + e.getMessage());
    }
  }

  public CodePointBuffer decode (ByteBuffer text, CodePointBuffer buffer,
                                 EncodingErrorType errorHandling)
          throws EncodingException
  {
    return decode(text, buffer);
  }

  /**
   * Encode, but ignore errors.
   *
   * @param text
   * @param buffer
   * @return
   */
  public ByteBuffer encode (CodePointBuffer text, ByteBuffer buffer)
          throws EncodingException
  {
    final String javaText =
            Utf16LE.getInstance().encodeString(text);
    try
    {
      final byte[] data = javaText.getBytes(name);

      final int textLength = text.getLength();
      if (buffer == null)
      {
        buffer = new ByteBuffer(textLength * 2);
      }
      else if ((buffer.getLength() * 2) < textLength)
      {
        buffer.ensureSize(textLength * 2);
      }

      System.arraycopy(data, 0, buffer.getData(), buffer.getOffset(), data.length);
    }
    catch (UnsupportedEncodingException e)
    {
      // this should not happen, as the encodings should have been checked by
      // the system already ...
      throw new EncodingException("Failed to encode the string: " + e.getMessage());
    }
    return buffer;
  }

  public ByteBuffer encode (CodePointBuffer text, ByteBuffer buffer,
                            EncodingErrorType errorHandling)
          throws EncodingException
  {
    return encode(text, buffer);
  }

  public String getName ()
  {
    return name;
  }

  public String getName (Locale locale)
  {
    return name;
  }

  public boolean isUnicodeCharacterSupported (int c)
  {
    // Damn, either fast or dead slow
    if (fastMode)
    {
      return true;
    }

    // cant test that one ...
    if (c == 0x3f) return true;

    String testEncoding = ("" + (char) c);
    try
    {
      byte[] bytes = testEncoding.getBytes(name);
      if (bytes.length != 1)
      {
        // Assume that everything went well, as if it didn't,
        // it should have created a single-byte sequence
        return true;
      }

      return (0x3f != bytes[0]);
    }
    catch (UnsupportedEncodingException e)
    {
      // this should not happen, as the encodings should have been checked by
      // the system already ...
      throw new IllegalStateException("Failed to encode the string: " + e.getMessage());
    }
  }
}
