/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ---------------------
 * NullOutputStream.java
 * ---------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: NullOutputStream.java,v 1.1 2003/07/07 22:44:09 taqua Exp $
 *
 * Changes
 * -------
 * 12-Nov-2002 : Fixed errors reported by Checkstyle 2.4 (DG).
 *
 */
package org.jfree.report.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A null output stream.
 *
 * @author Thomas Morgner
 */
public class NullOutputStream extends OutputStream
{
  /**
   * Default constructor.
   */
  public NullOutputStream()
  {
  }

  /**
   * Writes to the stream (in this case, does nothing).
   *
   * @param i  the value.
   *
   * @throws IOException if there is an I/O problem.
   */
  public void write(final int i) throws IOException
  {
    // no i wont do anything here ...
  }

  /**
   * Writes to the stream (in this case, does nothing).
   *
   * @param bytes  the bytes.
   *
   * @throws IOException if there is an I/O problem.
   */
  public void write(final byte[] bytes) throws IOException
  {
    // no i wont do anything here ...
  }

  /**
   * Writes to the stream (in this case, does nothing).
   *
   * @param bytes  the bytes.
   * @param      off   the start offset in the data.
   * @param      len   the number of bytes to write.
   *
   * @throws IOException if there is an I/O problem.
   */
  public void write(final byte[] bytes, final int off, final int len) throws IOException
  {
    // no i wont do anything here ...
  }

}
