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

import java.util.Locale;

import org.jfree.fonts.encoding.Encoding;
import org.jfree.fonts.encoding.EncodingException;
import org.jfree.fonts.encoding.ByteBuffer;
import org.jfree.fonts.encoding.CodePointBuffer;
import org.jfree.fonts.encoding.EncodingErrorType;

/**
 * This is a lucky case, as ASCII can be transformed directly. There is no
 * lookup step needed.
 *
 * @author Thomas Morgner
 */
public final class Ascii implements Encoding
{
  public Ascii()
  {
  }

  public String getName()
  {
    return "ASCII";
  }

  public String getName(Locale locale)
  {
    return getName();
  }

  public boolean isUnicodeCharacterSupported(int c)
  {
    return (c & 0xFFFFFF80) == 0;
  }

  /**
   * Encode, but ignore errors.
   *
   * @param text
   * @param buffer
   * @return
   */
  public ByteBuffer encode(CodePointBuffer text, ByteBuffer buffer)
  {
    final int textLength = text.getLength();
    if (buffer == null)
    {
      buffer = new ByteBuffer(textLength);
    }
    else if (buffer.getLength() < textLength)
    {
      buffer.ensureSize(textLength);
    }

    final byte[] targetArray = buffer.getData();
    final int[] sourceArray = text.getData();

    int targetIdx = buffer.getOffset();
    final int endPos = text.getCursor();
    for (int i = text.getOffset(); i < endPos; i++)
    {
      final int sourceItem = sourceArray[i];
      if (isUnicodeCharacterSupported(sourceItem))
      {
        targetArray[targetIdx] = (byte) (sourceItem & 0x7f);
        targetIdx += 1;
      }
    }

    buffer.setCursor(targetIdx);
    return buffer;
  }

  public CodePointBuffer decode(ByteBuffer text, CodePointBuffer buffer)
  {
    final int textLength = text.getLength();
    if (buffer == null)
    {
      buffer = new CodePointBuffer(textLength);
    }
    else if (buffer.getLength() < textLength)
    {
      buffer.ensureSize(textLength);
    }

    final int[] targetArray = buffer.getData();
    final byte[] sourceArray = text.getData();

    int targetIdx = buffer.getOffset();
    final int endPos = text.getCursor();
    for (int i = text.getOffset(); i < endPos; i++)
    {
      targetArray[targetIdx] = (sourceArray[i] & 0x7f);
      targetIdx += 1;
    }

    buffer.setCursor(targetIdx);
    return buffer;
  }

  public ByteBuffer encode(CodePointBuffer text,
                           ByteBuffer buffer,
                           EncodingErrorType errorHandling)
          throws EncodingException
  {
    final int textLength = text.getLength();
    if (buffer == null)
    {
      buffer = new ByteBuffer(textLength);
    }
    else if (buffer.getLength() < textLength)
    {
      buffer.ensureSize(textLength);
    }

    final byte[] targetArray = buffer.getData();
    final int[] sourceArray = text.getData();

    int targetIdx = buffer.getOffset();
    final int endPos = text.getCursor();
    for (int i = text.getOffset(); i < endPos; i++)
    {
      final int sourceItem = sourceArray[i];
      if (isUnicodeCharacterSupported(sourceItem))
      {
        targetArray[targetIdx] = (byte) (sourceItem & 0x7f);
        targetIdx += 1;
      }
      else
      {
        if (errorHandling == EncodingErrorType.REPLACE)
        {
          targetArray[targetIdx] = (byte) ('?' & 0x7f);
          targetIdx += 1;
        }
        else if (errorHandling == EncodingErrorType.FAIL)
        {
          throw new EncodingException();
        }
      }
    }

    buffer.setCursor(targetIdx);
    return buffer;
  }

  public CodePointBuffer decode(ByteBuffer text,
                                CodePointBuffer buffer,
                                EncodingErrorType errorHandling)
          throws EncodingException
  {
    final int textLength = text.getLength();
    if (buffer == null)
    {
      buffer = new CodePointBuffer(textLength);
    }
    else if (buffer.getLength() < textLength)
    {
      buffer.ensureSize(textLength);
    }

    final int[] targetArray = buffer.getData();
    final byte[] sourceArray = text.getData();

    int targetIdx = buffer.getOffset();
    final int endPos = text.getCursor();
    for (int i = text.getOffset(); i < endPos; i++)
    {
      targetArray[targetIdx] = (sourceArray[i] & 0x7f);
      targetIdx += 1;
    }

    buffer.setCursor(targetIdx);
    return buffer;
  }

}
