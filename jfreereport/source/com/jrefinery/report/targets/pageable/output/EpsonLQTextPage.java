/**
 * Date: Jan 29, 2003
 * Time: 5:05:41 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.output;

import java.io.OutputStream;
import java.io.IOException;

public class EpsonLQTextPage extends PlainTextPage
{
  

  public EpsonLQTextPage(int w, int h)
  {
    super(w, h);
  }

  protected void startLine(OutputStream out)
      throws IOException
  {
    // do nothing...
    super.startLine(out);
  }

  protected void endLine(OutputStream out)
      throws IOException
  {
    // default behaviour ...
    super.endLine(out);
  }

  protected void printEmptyChunk(OutputStream out)
      throws IOException
  {
    super.printEmptyChunk(out);
  }

  protected void startPage(OutputStream out)
      throws IOException
  {
    super.startPage(out);
  }

  protected void endPage(OutputStream out)
      throws IOException
  {
    super.endPage(out);
  }

  protected void printChunk(OutputStream out, PlainTextPage.TextDataChunk chunk, int x)
      throws IOException
  {
    super.printChunk(out, chunk, x);
  }
}
