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

package org.jfree.fonts.encoding;

import java.io.Writer;
import java.io.OutputStream;
import java.io.IOException;

import org.jfree.fonts.encoding.manual.Utf16LE;

/**
 *
 * @author Thomas Morgner
 */
public class EncodedOutputStreamWriter extends Writer
{
  private OutputStream outputStream;
  private EncodingErrorType errorType;
  private Encoding encoding;
  private ByteBuffer buffer;
  private CodePointBuffer text;

  public EncodedOutputStreamWriter(final OutputStream outputStream,
                                   final Encoding encoding,
                                   final EncodingErrorType errorType)
  {
    this.encoding = encoding;
    this.outputStream = outputStream;
    this.errorType = errorType;
  }

  /**
   * Write a portion of an array of characters.
   *
   * @param cbuf Array of characters
   * @param off  Offset from which to start writing characters
   * @param len  Number of characters to write
   * @throws java.io.IOException If an I/O error occurs
   */
  public void write(char cbuf[], int off, int len) throws IOException
  {
    text = Utf16LE.getInstance().decode(cbuf, off, len, text);
    if (buffer != null)
    {
      buffer.setCursor(0);
    }

    if (errorType == null)
    {
      buffer = encoding.encode(text, buffer);
    }
    else
    {
      buffer = encoding.encode(text, buffer, errorType);
    }

    outputStream.write(buffer.getData(), buffer.getOffset(), buffer.getLength());
  }

  /**
   * Flush the stream.  If the stream has saved any characters from the various
   * write() methods in a buffer, write them immediately to their intended
   * destination.  Then, if that destination is another character or byte
   * stream, flush it.  Thus one flush() invocation will flush all the buffers
   * in a chain of Writers and OutputStreams.
   * <p/>
   * If the intended destination of this stream is an abstraction provided by
   * the underlying operating system, for example a file, then flushing the
   * stream guarantees only that bytes previously written to the stream are
   * passed to the operating system for writing; it does not guarantee that they
   * are actually written to a physical device such as a disk drive.
   *
   * @throws java.io.IOException If an I/O error occurs
   */
  public void flush() throws IOException
  {
    outputStream.flush();
  }

  /**
   * Close the stream, flushing it first.  Once a stream has been closed,
   * further write() or flush() invocations will cause an IOException to be
   * thrown.  Closing a previously-closed stream, however, has no effect.
   *
   * @throws java.io.IOException If an I/O error occurs
   */
  public void close() throws IOException
  {
    outputStream.close();
  }
}
