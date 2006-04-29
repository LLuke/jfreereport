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
