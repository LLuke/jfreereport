/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ----------------
 * HtmlWriter.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes
 * -------
 * 23.04.2003 : Initial version
 */
package com.jrefinery.report.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.OutputStreamWriter;

public class HtmlWriter extends PrintStream
{
  private String encoding;
  private OutputStreamWriter cout;
  /**
   * Create a new print stream.  This stream will not flush automatically.
   *
   * @param  out        The output stream to which values and objects will be
   *                    printed
   *
   * @see java.io.PrintWriter#PrintWriter(OutputStream)
   */
  public HtmlWriter(OutputStream out, String encoding)
    throws UnsupportedEncodingException
  {
    super(out);
    if (encoding == null)
      throw new NullPointerException();

    this.encoding = encoding;
    this.cout = new OutputStreamWriter(out, encoding);
  }

  /**
   * Create a new print stream.
   *
   * @param  out        The output stream to which values and objects will be
   *                    printed
   * @param  autoFlush  A boolean; if true, the output buffer will be flushed
   *                    whenever a byte array is written, one of the
   *                    <code>println</code> methods is invoked, or a newline
   *                    character or byte (<code>'\n'</code>) is written
   *
   * @see java.io.PrintWriter#PrintWriter(OutputStream, boolean)
   */
  public HtmlWriter(OutputStream out, boolean autoFlush, String encoding)
      throws UnsupportedEncodingException
  {
    super(out, autoFlush);
    if (encoding == null)
      throw new NullPointerException();

    this.encoding = encoding;
    this.cout = new OutputStreamWriter(out, encoding);
  }

  /**
   * Print an array of characters.  The characters are converted into bytes
   * according to the platform's default character encoding, and these bytes
   * are written in exactly the manner of the
   * <code>{@link #write(int)}</code> method.
   *
   * @param      s   The array of chars to be printed
   *
   * @throws  NullPointerException  If <code>s</code> is <code>null</code>
   */
  public void printEncoded(char s[])
  {
    try
    {
      cout.write(s);
      cout.flush();
    }
    catch (IOException e)
    {
      setError();
    }
  }

  /**
   * Print a string.  If the argument is <code>null</code> then the string
   * <code>"null"</code> is printed.  Otherwise, the string's characters are
   * converted into bytes according to the platform's default character
   * encoding, and these bytes are written in exactly the manner of the
   * <code>{@link #write(int)}</code> method.
   *
   * @param      s   The <code>String</code> to be printed
   */
  public void printEncoded(String s)
  {
    try
    {
      cout.write(s);
      cout.flush();
    }
    catch (IOException e)
    {
      setError();
    }
  }

}
