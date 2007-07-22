package org.jfree.fonts.afm;

import java.io.IOException;

import org.jfree.fonts.io.FontDataInputSource;

/**
 * Creation-Date: 22.07.2007, 14:03:40
 *
 * @author Thomas Morgner
 */
public class FontDataAsciiReader
{
  private byte[] buffer;
  private int bufferFill;
  private int cursor;

  private FontDataInputSource inputSource;
  private long readPosition;
  private boolean eol;

  public FontDataAsciiReader(final FontDataInputSource inputSource)
  {
    this(inputSource, 4096);
  }

  public FontDataAsciiReader (final FontDataInputSource inputSource, final int bufferSize)
  {
    if (bufferSize < 1)
    {
      throw new IllegalArgumentException();
    }
    if (inputSource == null)
    {
      throw new NullPointerException();
    }
    this.buffer = new byte[bufferSize];
    this.cursor = bufferSize;
    this.inputSource = inputSource;
    this.readPosition = 0;
    this.bufferFill = 0;
  }

  private int read () throws IOException
  {
    if (cursor >= bufferFill)
    {
      bufferFill = inputSource.readAt(readPosition, buffer, 0, buffer.length);
      if (bufferFill == 0)
      {
        return -1;
      }
      readPosition += bufferFill;
      cursor = 0;
    }
    final int retval = (0xff & buffer[cursor]);
    cursor += 1;
    return retval;
  }

  public String readLine () throws IOException
  {
    int data = read();
    if (data == -1)
    {
      return null;
    }

    final StringBuffer retval = new StringBuffer();
    for (;;)
    {
      if (data == -1)
      {
        return retval.toString();
      }
      else if (data == '\n')
      {
        // the next time we will skip the \r
        eol = true;
        return retval.toString();
      }
      else if (data == '\r')
      {
        if (eol == false)
        {
          return retval.toString();
        }
        eol = false;
      }
      else
      {
        eol = false;
        if (data > 0x7f)
        {
          retval.append('?');
        }
        else
        {
          retval.append((char) data);
        }
      }
      data = read();
    }
  }
}
