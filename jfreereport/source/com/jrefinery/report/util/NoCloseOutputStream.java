/**
 * Date: Jan 29, 2003
 * Time: 6:49:31 PM
 *
 * $Id$
 */
package com.jrefinery.report.util;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.io.IOException;

public class NoCloseOutputStream extends FilterOutputStream
{
  public NoCloseOutputStream(OutputStream out)
  {
    super(out);
  }

  /**
   * Closes this output stream and releases any system resources
   * associated with the stream.
   * <p>
   * The <code>close</code> method of <code>FilterOutputStream</code>
   * calls its <code>flush</code> method, and then calls the
   * <code>close</code> method of its underlying output stream.
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
