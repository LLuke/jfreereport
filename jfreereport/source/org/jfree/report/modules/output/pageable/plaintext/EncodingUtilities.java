package org.jfree.report.modules.output.pageable.plaintext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class EncodingUtilities
{
  private String encoding;
  private byte[] header;
  private byte[] space;
  private byte[] encodingHeader;

  public EncodingUtilities (final String codepage)
    throws UnsupportedEncodingException
  {
    this.encoding = codepage;

    encodingHeader = " ".getBytes(codepage);
    final byte[] spacesWithHeader = "  ".getBytes(codepage);
    final int length = spacesWithHeader.length - encodingHeader.length;
    space = new byte[length];
    header = new byte[length - encodingHeader.length];

    System.arraycopy(spacesWithHeader, encodingHeader.length, space, 0, length);
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
          (final char[] textString, final OutputStream out) throws IOException
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
          (final String textString, final OutputStream out) throws IOException
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
