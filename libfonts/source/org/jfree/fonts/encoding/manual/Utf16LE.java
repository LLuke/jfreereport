/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/libfonts/
 * Project Lead:  Thomas Morgner;
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
 * Utf16LE.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: Utf16LE.java,v 1.1 2006/04/29 15:12:58 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.fonts.encoding.manual;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.jfree.fonts.encoding.ByteBuffer;
import org.jfree.fonts.encoding.ByteStream;
import org.jfree.fonts.encoding.CodePointBuffer;
import org.jfree.fonts.encoding.CodePointStream;
import org.jfree.fonts.encoding.ComplexEncoding;
import org.jfree.fonts.encoding.EncodingErrorType;
import org.jfree.fonts.encoding.EncodingException;
import org.jfree.util.Log;

/**
 * Creation-Date: 20.04.2006, 18:01:39
 *
 * @author Thomas Morgner
 */
public class Utf16LE implements ComplexEncoding
{
  public static final int MAX_CHAR = 0x10FFFD;
  private static Utf16LE instance;

  public synchronized static Utf16LE getInstance ()
  {
    if (instance == null)
    {
      instance = new Utf16LE();
    }
    return instance;
  }

  public Utf16LE ()
  {
  }

  public String getName ()
  {
    return "UTF-16LE";
  }

  public String getName (Locale locale)
  {
    return "UTF-16LE";
  }

  public boolean isUnicodeCharacterSupported (int c)
  {
    return (c > 0) && (c < MAX_CHAR) && // this is the maximum number of characters defined.
            (c & 0xFFFFF800) == 0xD800; // this is the replacement zone.
  }

  /**
   * Encode, but ignore errors.
   *
   * @param text
   * @param buffer
   * @return
   */
  public ByteBuffer encode (CodePointBuffer text, ByteBuffer buffer)
  {
    final int textLength = text.getLength();
    if (buffer == null)
    {
      buffer = new ByteBuffer(textLength * 2);
    }
    else if ((buffer.getLength() * 2) < textLength)
    {
      buffer.ensureSize(textLength * 2);
    }

    final ByteStream target = new ByteStream(buffer, textLength);
    final int[] sourceArray = text.getData();
    final int endPos = text.getCursor();
    for (int i = text.getOffset(); i < endPos; i++)
    {
      final int sourceItem = sourceArray[i];
      if (sourceItem < 0 || sourceItem > MAX_CHAR)
      {
        continue;
      }

      if (sourceItem <= 0xFFFF)
      {
        if (sourceItem >= 0xD800 && sourceItem <= 0xDFFF)
        {
          // this is an error condition. We ignore it for now ..
          continue;
        }

        target.put((byte) ((sourceItem & 0xff00) >> 8));
        target.put((byte) (sourceItem & 0xff));
      }
      else
      {
        // compute the weird replacement mode chars ..
        final int derivedSourceItem = sourceItem - 0x10000;
        final int highWord = 0xD800 | ((derivedSourceItem & 0xFFC00) >> 10);
        target.put((byte) ((highWord & 0xff00) >> 8));
        target.put((byte) (highWord & 0xff));

        final int lowWord = 0xDC00 | (derivedSourceItem & 0x3FF);
        target.put((byte) ((lowWord & 0xff00) >> 8));
        target.put((byte) (lowWord & 0xff));
      }
    }

    target.close();
    return buffer;
  }

  public CodePointBuffer decode (ByteBuffer text, CodePointBuffer buffer)
  {
    final int textLength = text.getLength();
    if (buffer == null)
    {
      buffer = new CodePointBuffer(textLength / 2);
    }
    else if ((buffer.getLength() / 2) < textLength)
    {
      buffer.ensureSize(textLength / 2);
    }


    final int[] targetData = buffer.getData();
    final ByteStream sourceBuffer = new ByteStream(text, 10);

    // this construct gives us an even number ...
    int position = buffer.getOffset();
    while (sourceBuffer.getReadSize() >= 2)
    {
      final int highByte = (sourceBuffer.get() & 0xff);
      final int lowByte = (sourceBuffer.get() & 0xff);

      if ((highByte & 0xFC) == 0xD8)
      {
        if (sourceBuffer.getReadSize() < 2)
        {
          // we reached the end of the parsable stream ...
          // this is an error condition
          // Log.debug("Reached the end ..");
          break;
        }

        final int highByteL = (sourceBuffer.get() & 0xff);
        final int lowByteL = (sourceBuffer.get() & 0xff);


        if ((highByteL & 0xFC) == 0xDC)
        {
          // decode the extended CodePoint ...
          int result = lowByteL;
          result |= (highByteL & 0x03) << 8;
          result |= lowByte << 10;
          result |= (highByte & 0x03) << 18;
          targetData[position] = result + 0x10000;
          position += 1;
        }
        else
        {
          // this is an error condition.
          // Log.debug("error 1..");
        }
      }
      else if ((highByte & 0xFC) == 0xDC)
      {
        // this is an error condition ..
        // skip this word ..
        // Log.debug("error 2..");
      }
      else
      {
        // decode the simple mode ...
        targetData[position] = (highByte << 8) | lowByte;
        position += 1;
      }
    }
    buffer.setCursor(position);
    return buffer;
  }

  public ByteBuffer encode (CodePointBuffer text,
                            ByteBuffer buffer,
                            EncodingErrorType errorHandling)
          throws EncodingException
  {
    return encode(text, buffer);
  }

  public CodePointBuffer decode (ByteBuffer text,
                                 CodePointBuffer buffer,
                                 EncodingErrorType errorHandling)
          throws EncodingException
  {
    return decode(text, buffer);
  }

  /**
   * Checks, whether this implementation supports encoding of character data.
   *
   * @return
   */
  public boolean isEncodingSupported ()
  {
    return true;
  }

  public CodePointBuffer decodeString (String text, CodePointBuffer buffer)
  {
    final char[] chars = text.toCharArray();
    final int textLength = chars.length;
    if (buffer == null)
    {
      buffer = new CodePointBuffer(textLength);
    }
    else if ((buffer.getLength()) < textLength)
    {
      buffer.ensureSize(textLength);
    }

    CodePointStream cps = new CodePointStream(buffer, 10);
    for (int i = 0; i < chars.length; i++)
    {
      final char c = chars[i];
      if ((c & 0xFC00) == 0xD800)
      {
        i += 1;
        if (i < chars.length)
        {
          final char c2 = chars[i];
          if ((c2 & 0xFC00) == 0xDC00)
          {
            final int codePoint = 0x10000 +
                    ((c2 & 0x3FF) | ((c & 0x3FF) << 10));
            cps.put(codePoint);
          }
          else
          {
            // Should not happen ..
          }
        }
        else
        {
          // illegal char .. ignore it ..
          // of course: This should not happen, as this produced by JDK code
          break;
        }
      }
      else
      {
        cps.put(c);
      }
    }
    cps.close();
    return buffer;
  }

  public String encodeString (CodePointBuffer buffer)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    final int[] data = buffer.getData();
    final int endPos = buffer.getCursor();

    for (int i = buffer.getOffset(); i < endPos; i++)
    {
      final int codePoint = data[i];
      if (codePoint < 0x10000)
      {
        stringBuffer.append((char)codePoint);
      }
      else
      {
        // oh, no, we have to decode ...
        // compute the weird replacement mode chars ..
        final int derivedSourceItem = codePoint - 0x10000;
        final int highWord = 0xD800 | ((derivedSourceItem & 0xFFC00) >> 10);
        final int lowWord = 0xDC00 | (derivedSourceItem & 0x3FF);
        stringBuffer.append((char)highWord);
        stringBuffer.append((char)lowWord);
      }
    }
//    Log.debug ("Encoded:" + stringBuffer + " (" + buffer.getOffset() + ", " + endPos + ")");
    return stringBuffer.toString();
  }

//  public static void main (String[] args)
//          throws UnsupportedEncodingException
//  {
//    Utf16LE utf = new Utf16LE();
//    final String text = "The lazy fox jumps over the lemon tree";
//    byte[] bytes = text.getBytes("UTF16");
//    CodePointBuffer cp = utf.decode
//            (new ByteBuffer(bytes), new CodePointBuffer(text.length()));
//    int[] cps = cp.getData();
//
//    final int length = cp.getLength();
//    for (int i = 1; i < length; i++)
//    {
//      int cp1 = cps[i];
//      if (cp1 != text.charAt(i - 1))
//      {
//        throw new IllegalStateException("Error at " + i + ": " +
//                Integer.toHexString(cp1) + " vs " +
//                Integer.toHexString(text.charAt(i - 1)));
//      }
//    }
//  }
}
