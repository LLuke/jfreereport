package org.jfree.fonts.encoding;

import org.jfree.fonts.encoding.manual.Utf16LE;

/**
 * Creation-Date: 21.07.2007, 19:44:31
 *
 * @author Thomas Morgner
 */
public class EncodingUtility
{
  private EncodingUtility()
  {
  }

  public static String encode (final byte[] data, final String encoding) throws EncodingException
  {
    final Encoding enc;
    if ("UTF-16".equals(encoding))
    {
      enc = EncodingRegistry.getInstance().getEncoding("UTF-16LE");
    }
    else
    {
      enc = EncodingRegistry.getInstance().getEncoding(encoding);
    }

    final ByteBuffer byteBuffer = new ByteBuffer(data);
    final CodePointBuffer cp = enc.decode(byteBuffer, null);
    return Utf16LE.getInstance().encodeString(cp);
  }
}
