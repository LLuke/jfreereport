/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
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
 * ---------------------
 * NullOutputStream.java
 * ---------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: NullOutputStream.java,v 1.2 2002/11/07 21:45:29 taqua Exp $
 *
 * Changes
 * -------
 * 12-Nov-2002 : Fixed errors reported by Checkstyle 2.4 (DG).
 *
 */

package com.jrefinery.report.util;

import java.io.OutputStream;
import java.io.IOException;

/**
 * A null output stream.
 *
 * @author TM
 */
public class NullOutputStream extends OutputStream
{
  /**
   * Default constructor.
   */
  public NullOutputStream ()
  {
  }

  /**
   * Writes to the stream (in this case, does nothing).
   *
   * @param i  the value.
   *
   * @throws IOException if there is an I/O problem.
   */
  public void write(int i) throws IOException
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
  public void write(byte[] bytes) throws IOException
  {
    // no i wont do anything here ...
  }

  /**
   * Writes to the stream (in this case, does nothing).
   *
   * @param bytes  the bytes.
   * @param i  ??.
   * @param i1  ??.
   *
   * @throws IOException if there is an I/O problem.
   */
  public void write(byte[] bytes, int i, int i1) throws IOException
  {
    // no i wont do anything here ...
  }

}
