/**
 * Date: Jan 30, 2003
 * Time: 3:46:18 PM
 *
 * $Id: PrinterCommandSet.java,v 1.1 2003/01/30 22:58:44 taqua Exp $
 */
package com.jrefinery.report.targets.pageable.output;

import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.PageFormatFactory;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class PrinterCommandSet
{
  public static final byte SELECT_FONT_ROMAN = 0x00;
  public static final byte SELECT_FONT_SWISS = 0x01;
  public static final byte SELECT_FONT_COURIER = 0x02;
  public static final byte SELECT_FONT_PRESIGE = 0x03;
  public static final byte SELECT_FONT_OCR_A = 0x05;
  public static final byte SELECT_FONT_OCR_B = 0x06;
  public static final byte SELECT_FONT_ORATOR = 0x07;
  public static final byte SELECT_FONT_SWISS_BOLD = 0x7A;
  public static final byte SELECT_FONT_GOTHIC = 0x7C;
  public static final byte SELECT_FONT_FROM_MENU = 0x7F;

  public static final byte CARRIAGE_RETURN = 0x0D;
  public static final byte LINE_FEED = 0x0A;
  public static final byte FORM_FEED = 0x0C;
  public static final byte SPACE = 0x20;

  private OutputStream out;
  private byte font;
  private byte characterWidth;
  private int paperSize;
  private boolean bold;
  private boolean italic;
  private boolean underline;
  private boolean strikethrough;

  private int borderLeft;
  private int borderRight;
  private int borderTop;
  private int borderBottom;

  private String codepage;

  private int lineSpacing;
  private boolean autoLf;
  private boolean letterQuality;

  private int  defaultLPI;
  private int  defaultCPI;
  private PageFormat pageFormat;
  private int emptyCellCounter;

  public PrinterCommandSet(OutputStream out, PageFormat format, int defaultCPI, int defaultLPI)
  {
    this.out = out;
    this.defaultLPI = defaultLPI;
    this.defaultCPI = defaultCPI;
    pageFormat = format;
  }

  public PageFormat getPageFormat()
  {
    return pageFormat;
  }


  public int getDefaultCPI()
  {
    return defaultCPI;
  }

  public int getDefaultLPI()
  {
    return defaultLPI;
  }

  protected OutputStream getOut()
  {
    return out;
  }

  public void setFont (byte fontSelection) throws IOException
  {
    this.font = fontSelection;
  }

  public byte getFont ()
  {
    return font;
  }

  public void setCharacterWidth(byte charWidth) throws IOException
  {
    this.characterWidth = charWidth;
  }

  public byte getCharacterWidth ()
  {
    return characterWidth;
  }

  public void setFontStyle (boolean bold, boolean italic, boolean underline, boolean strike)
   throws IOException
  {
    this.bold = bold;
    this.italic = italic;
    this.underline = underline;
    this.strikethrough = strike;
  }

  public boolean isStrikethrough()
  {
    return strikethrough;
  }

  public boolean isUnderline()
  {
    return underline;
  }

  public boolean isItalic()
  {
    return italic;
  }

  public boolean isBold()
  {
    return bold;
  }

  public void setPaperSize (int lines) throws IOException
  {
    this.paperSize = lines;
  }

  public int getPaperSize()
  {
    return paperSize;
  }

  // seitenränder horizontal in zeichen ... (== leerzeichen)
  public void setHorizontalBorder (int left, int right) throws IOException
  {
    this.borderLeft = left;
    this.borderRight = right;
  }

  // vertical in 1/1440 zoll ..
  public void setVerticalBorder (int top, int bottom) throws IOException
  {
    this.borderTop = top;
    this.borderBottom = bottom;
  }

  public int getBorderBottom()
  {
    return borderBottom;
  }

  public int getBorderTop()
  {
    return borderTop;
  }

  public int getBorderRight()
  {
    return borderRight;
  }

  public int getBorderLeft()
  {
    return borderLeft;
  }

  // vertical in 1/1440 zoll ..
  public void setLineSpacing (int spaceInInch) throws IOException
  {
    if (spaceInInch <= 0) throw new IllegalArgumentException();
    this.lineSpacing = spaceInInch;
  }

  public int getLineSpacing()
  {
    return lineSpacing;
  }

  public void setCodePage (String  codepage) throws IOException
  {
    this.codepage = codepage;
  }

  public String getCodepage()
  {
    return codepage;
  }

  public void setAutoLF (boolean autoLF) throws IOException
  {
    this.autoLf = autoLF;
  }

  public void setPrintQuality (boolean letterQuality) throws IOException
  {
    this.letterQuality = letterQuality;
  }

  public boolean isLetterQuality()
  {
    return letterQuality;
  }

  public boolean isAutoLf()
  {
    return autoLf;
  }


  public void resetPrinter() throws IOException
  {
    // make sure that autoLF is disabled ..
    setCharacterWidth((byte) getDefaultCPI());
    setLineSpacing((byte) getDefaultLPI());
    setAutoLF(true); setAutoLF(false);
    setCodePage("Cp437");
    setFont(SELECT_FONT_FROM_MENU);
    setFontStyle(false, false, false, false);
    setPrintQuality(false);

    PageFormatFactory fact = PageFormatFactory.getInstance();
    Paper paper = pageFormat.getPaper();
    int cWidthPoints = 72/getCharacterWidth();
    int left = (int)(fact.getLeftBorder(paper) / cWidthPoints);
    int right = (int)(fact.getRightBorder(paper) / cWidthPoints);
    setHorizontalBorder(left, right);

    int top = (int) (fact.getTopBorder(paper) * 20);
    int bottom = (int) (fact.getBottomBorder(paper) * 20);
    setVerticalBorder(top, bottom);

    int lines = (int)((paper.getHeight() / 72) * getDefaultLPI());
    setPaperSize(lines);
  }

  public void startPage ()  throws IOException
  {
    int topBorderLines = ((getBorderTop()/ 1440) / getLineSpacing());
    Log.debug ("NumberOfBorderLines: " + topBorderLines);
    for (int i = 0; i < topBorderLines; i++)
    {
      startLine(); endLine();
    }
  }

  public void endPage ()  throws IOException
  {
    int bottomBorderLines = ((getBorderBottom() / 1440) / getLineSpacing());
    for (int i = 0; i < bottomBorderLines; i++)
    {
      startLine(); endLine();
    }
   out.write(FORM_FEED);
  }

  public void startLine ()  throws IOException
  {
    emptyCellCounter = getBorderLeft();
  }

  public void endLine ()  throws IOException
  {
    emptyCellCounter = 0;
    // CR = (ASCII #13) reset the print position to the start of the line
    // LF = (ASCII #10) scroll down a new line (? Auto-LF feature ?)
    out.write(CARRIAGE_RETURN);
    Log.debug ("AutoLF == " + isAutoLf());
    if (isAutoLf() == false)
    {
      out.write(LINE_FEED);
    }
  }

  public void printChunk (PlainTextPage.TextDataChunk chunk, int x)  throws IOException
  {
    if (emptyCellCounter != 0)
    {
      byte[] spaces = new byte[emptyCellCounter];
      Arrays.fill(spaces, SPACE);
      out.write(spaces);
      emptyCellCounter = 0;
    }

    if (chunk.getX() != x)
    {
      return;
    }

    FontDefinition fd = chunk.getFont();
    setFontStyle(fd.isBold(), fd.isItalic(), fd.isUnderline(), fd.isStrikeThrough());

    byte[] text = chunk.getText().getBytes(getCodepage());
    byte[] data = new byte[chunk.getWidth()];
    Arrays.fill(data, SPACE);
    System.arraycopy(text, 0, data, 0, Math.min (text.length, data.length));
    out.write (data);
  }

  public void printEmptyChunk ()  throws IOException
  {
    out.write(SPACE);
  }

  public void flush() throws IOException
  {
    getOut().flush();
  }
}
