/**
 * Date: Jan 29, 2003
 * Time: 5:11:40 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.output;

import java.io.OutputStream;
import java.io.IOException;

public class IBMPrinterTextPage extends PlainTextPage
{
  public static final byte[] START_BOLD = { 0x1b, 0x45 };
  public static final byte[] END_BOLD =   { 0x1b, 0x46 };

  public static final byte[] SELECT_10_CPI = { 0x12 };
  public static final byte[] SELECT_12_CPI = { 0x12 };
  public static final byte[] SELECT_15_CPI = { 0x12 };
  public static final byte[] SELECT_17_CPI = { 0x12 };
  public static final byte[] SELECT_20_CPI = { 0x12 };

  public IBMPrinterTextPage(int w, int h)
  {
    super(w, h);
  }

  protected void startLine(OutputStream out)
      throws IOException
  {
    super.startLine(out);
  }

  protected void endLine(OutputStream out)
      throws IOException
  {
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
