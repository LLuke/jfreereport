/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ------------------------
 * NoCloseOutputStream.java
 * ------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * $Id: NoCloseOutputStream.java,v 1.4 2003/02/26 13:58:05 mungady Exp $
 *
 * Changes
 * -------
 * 29-Jan-2003 : Initial version
 *
 */
package com.jrefinery.report.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A Wrapper stream that does never calls close on its parent. This implementation
 * is needed when creating ZipOutputStream, as the final ZipDirectory is written
 * when close is called on the ZipOutputSteam.
 *
 * @author Thomas Morgner
 */
public class NoCloseOutputStream extends FilterOutputStream
{
  /**
   * Create a new NoCloseOutputStream with the given output stream a parent.
   *
   * @param out the parent stream
   */
  public NoCloseOutputStream(OutputStream out)
  {
    super(out);
  }

  /**
   * Closes this output stream and releases any system resources
   * associated with the stream, but does not close the underlying
   * output stream.
   * <p>
   * The <code>close</code> method of <code>FilterOutputStream</code>
   * calls its <code>flush</code> method.
   *
   * @exception  IOException  if an I/O error occurs.
   * @see        FilterOutputStream#flush()
   * @see        FilterOutputStream#out
   */
  public void close() throws IOException
  {
    flush();
    // do not close the parent stream ... !
  }
}
