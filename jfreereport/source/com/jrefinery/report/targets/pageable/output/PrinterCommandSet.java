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
 * ----------------------
 * PrinterCommandSet.java
 * ----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributoers.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);;
 *
 * $Id: PrinterCommandSet.java,v 1.12 2003/06/27 18:46:25 taqua Exp $
 *
 * Changes
 * -------
 * 30-Jan-2003 : Initial version
 *
 */
package com.jrefinery.report.targets.pageable.output;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.IOException;
import java.io.OutputStream;

import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.util.EncodingSupport;
import com.jrefinery.report.util.PageFormatFactory;

/**
 * Implements a printer command set for plain text output. The output is
 * not enriched with any printer specific control sequences.
 *
 * @author Thomas Morgner
 */
public class PrinterCommandSet
{
  /** the roman font. */
  public static final byte SELECT_FONT_ROMAN = 0x00;

  /** the swiss font. */
  public static final byte SELECT_FONT_SWISS = 0x01;

  /** the courier font. */
  public static final byte SELECT_FONT_COURIER = 0x02;

  /** the prestige font. */
  public static final byte SELECT_FONT_PRESTIGE = 0x03;

  /** the OCR-A font. */
  public static final byte SELECT_FONT_OCR_A = 0x05;

  /** the OCR-B font. */
  public static final byte SELECT_FONT_OCR_B = 0x06;

  /** the orator font. */
  public static final byte SELECT_FONT_ORATOR = 0x07;

  /** the swiss-bold font. */
  public static final byte SELECT_FONT_SWISS_BOLD = 0x7A;

  /** the gothic font. */
  public static final byte SELECT_FONT_GOTHIC = 0x7C;

  /** selects the font, which is selected on the printer menu. */
  public static final byte SELECT_FONT_FROM_MENU = 0x7F;

  /**
   * the Carriage Return control character,
   * the printer carriage returns to the start of the line.
   */
  public static final byte CARRIAGE_RETURN = 0x0D;

  /** scrolls the paper up a single line. */
  public static final byte LINE_FEED = 0x0A;

  /** the form feed character, ejects the current page and starts the next page. */
  public static final byte FORM_FEED = 0x0C;

  /** the space character. */
  public static final byte SPACE = 0x20;

  /** the output stream. */
  private OutputStream out;

  /** the font selector byte. */
  private byte font;

  /** character width. */
  private byte characterWidth;

  /** the paper height in lines. */
  private int paperSize;

  /** the current bold state for the font. */
  private boolean bold;

  /** the current italic state for the font. */
  private boolean italic;

  /** the current underline state for the font. */
  private boolean underline;

  /** the current strikethrough state for the font. */
  private boolean strikethrough;

  /** the left border in characters. */
  private int borderLeft;

  /** the right border in characters. */
  private int borderRight;

  /** the upper border in lines. */
  private int borderTop;

  /** the bottom border in lines. */
  private int borderBottom;

  /** the current codepage. */
  private String codepage;

  /** the current linespacing in 1/1440 inches. */
  private int lineSpacing;

  /** the AutoLF state. */
  private boolean autoLf;

  /** the printQuality flag, true for letter quality. */
  private boolean letterQuality;

  /** the lines per inch for this page. */
  private int defaultLPI;

  /** the characters per inch for this page. */
  private int defaultCPI;

  /** the pageformat used in this page. */
  private PageFormat pageFormat;

  /** the emptyCellCounter is used to optimize the printing. */
  private int emptyCellCounter;

  /** the encoding header, if any. */
  private byte[] encodingHeader;

  /** A single space character encoded in the current codepage. */
  private byte[] space;

  /**
   * A flag indicating whether this is the first page that is printed with
   * this command set.
   */
  private boolean firstPage;

  /**
   * Creates a new PrinterCommandSet.
   *
   * @param out the target output stream
   * @param format the pageformat of the used report
   * @param defaultCPI the characters-per-inch for the output.
   * @param defaultLPI the lines-per-inch for the output.
   */
  public PrinterCommandSet(final OutputStream out, final PageFormat format, final int defaultCPI, final int defaultLPI)
  {
    this.out = out;
    this.defaultLPI = defaultLPI;
    this.defaultCPI = defaultCPI;
    this.pageFormat = format;
    this.firstPage = true;
  }

  /**
   * Gets the pageformat used in this command set.
   * @return the pageformat.
   */
  public PageFormat getPageFormat()
  {
    return pageFormat;
  }

  /**
   * Gets the default character width in CPI.
   *
   * @return the default character width in CPI.
   */
  public int getDefaultCPI()
  {
    return defaultCPI;
  }

  /**
   * Gets the default character height in CPI.
   *
   * @return the default character height in CPI.
   */
  public int getDefaultLPI()
  {
    return defaultLPI;
  }

  /**
   * Gets the outputstream that is used to write the generated content.
   *
   * @return the output stream.
   */
  protected OutputStream getOut()
  {
    return out;
  }

  /**
   * Defines the font. Does nothing.
   *
   * @param fontSelection the font selection byte.
   * @throws IOException is not thrown here.
   */
  public void setFont(final byte fontSelection) throws IOException
  {
    this.font = fontSelection;
  }

  /**
   * Returns the defined font selection byte.
   * @return the font selection byte.
   */
  public byte getFont()
  {
    return font;
  }

  /**
   * Defines the character width for the current font. The width is specified in
   * Characters-per-inch. Valid values are 10, 12, 15, 17 and 20 cpi.
   *
   * @param charWidth the character width in CPI.
   * @throws IOException if there was an IOError while writing the command or if the
   *   character width is not supported by the printer.
   */
  public void setCharacterWidth(final byte charWidth) throws IOException
  {
    this.characterWidth = charWidth;
  }

  /**
   * Gets the character width in CPI.
   * @return the character width.
   */
  public byte getCharacterWidth()
  {
    return characterWidth;
  }

  /**
   * Defines the font style for the printed text. The IBM-CommandSet does not
   * support strike-through.
   *
   * @param bold true, if the text should be printed in bold mode.
   * @param italic true, if the text should be italic, false otherwise
   * @param underline true, if the text should be underlined, false otherwise
   * @param strike true, if the text should be strikethrough, false otherwise
   * @throws IOException if there was an IOError while writing the command
   */
  public void setFontStyle(final boolean bold, final boolean italic, final boolean underline, final boolean strike)
      throws IOException
  {
    this.bold = bold;
    this.italic = italic;
    this.underline = underline;
    this.strikethrough = strike;
  }

  /**
   * Gets the strikethrough format flag.
   *
   * @return the strikethrough font format flag.
   */
  public boolean isStrikethrough()
  {
    return strikethrough;
  }

  /**
   * Gets the underline format flag.
   *
   * @return the underline font format flag.
   */
  public boolean isUnderline()
  {
    return underline;
  }

  /**
   * Gets the italic format flag.
   *
   * @return the italic font format flag.
   */
  public boolean isItalic()
  {
    return italic;
  }

  /**
   * Gets the bold format flag.
   *
   * @return the bold font format flag.
   */
  public boolean isBold()
  {
    return bold;
  }

  /**
   * Defines the papersize in lines.
   *
   * @param lines the number of lines that could be printed on a single page.
   * @throws IOException if there was an IOError while writing the command
   */
  public void setPaperSize(final int lines) throws IOException
  {
    this.paperSize = lines;
  }

  /**
   * Returns the paper size in lines.
   * @return the page height in lines.
   */
  public int getPaperSize()
  {
    return paperSize;
  }

  /**
   * Defines the horizontal borders for the current paper. The borders are given
   * in characters.
   *
   * @param left the number of spaces printed on the start of a line.
   * @param right the number of spaces left free on the right paper border.
   * @throws IOException if an IOException occured while updating the printer state.
   */
  public void setHorizontalBorder(final int left, final int right) throws IOException
  {
    this.borderLeft = left;
    this.borderRight = right;
  }

  /**
   * Defines the horizontal borders for the current paper. The borders are given
   * in characters.
   *
   * @param top the number of blank lines printed on the start of a page.
   * @param bottom the number of blank lines printed at the end of a page
   * @throws IOException if an IOException occured while updating the printer state.
   */
  public void setVerticalBorder(final int top, final int bottom) throws IOException
  {
    this.borderTop = top;
    this.borderBottom = bottom;
  }

  /**
   * Returns the bottom border in lines.
   * @return the bottom border.
   */
  public int getBorderBottom()
  {
    return borderBottom;
  }

  /**
   * Returns the top border in lines.
   * @return the top border.
   */
  public int getBorderTop()
  {
    return borderTop;
  }

  /**
   * Returns the right border in characters.
   * @return the right border.
   */
  public int getBorderRight()
  {
    return borderRight;
  }

  /**
   * Returns the left border in characters.
   * @return the left border.
   */
  public int getBorderLeft()
  {
    return borderLeft;
  }

  /**
   * Defines the line spacing for the printer, the spacing is given in
   * 1/1440 inches.
   *
   * @param spaceInInch the linespacing in 1/1440 inches.
   * @throws IOException if an IOException occured while updating the printer state.
   */
  public void setLineSpacing(final int spaceInInch) throws IOException
  {
    if (spaceInInch <= 0)
    {
      throw new IllegalArgumentException();
    }
    this.lineSpacing = spaceInInch;
  }

  /**
   * Returns the line spacing for the printer, the spacing is given in
   * 1/1440 inches.
   *
   * @return the linespacing in 1/1440 inches.
   */
  public int getLineSpacing()
  {
    return lineSpacing;
  }

  /**
   * Defines the code page for the text to be printed. The codepage is an
   * 8-Bit encoding scheme to print non-ascii-characters.
   *
   * @param codepage the new codepage that should be used.
   * @throws IOException if there was an IOError while writing the command
   */
  public void setCodePage(final String codepage) throws IOException
  {
    if (codepage == null)
    {
      throw new NullPointerException("The codepage must not be null.");
    }
    encodingHeader = " ".getBytes(codepage);

    final byte[] spacesWithHeader = "  ".getBytes(codepage);
    final int length = spacesWithHeader.length - encodingHeader.length;
    space = new byte[length];
    System.arraycopy(spacesWithHeader, encodingHeader.length, space, 0, length);

    this.codepage = codepage;
  }

  /**
   * Returns the current codepage of the printer.
   *
   * @return the code page.
   */
  public String getCodepage()
  {
    return codepage;
  }

  /**
   * Defines whether to print in AutoLF mode. If AutoLF is enabled, then a linefeed
   * character is automaticly printed after every &lt;CR&gt; character.
   *
   * @param autoLF the new autoLF state
   * @throws IOException if there was an IOError while writing the command
   */
  public void setAutoLF(final boolean autoLF) throws IOException
  {
    this.autoLf = autoLF;
  }

  /**
   * Defines the printing quality for the printed text. Set to true, to
   * enable LetterQuality printing, false enables Draft-Quality.
   *
   * @param letterQuality true, if letter quality should be used, false for draft-quality
   * @throws IOException if there was an IOError while writing the command
   */
  public void setPrintQuality(final boolean letterQuality) throws IOException
  {
    this.letterQuality = letterQuality;
  }

  /**
   * Gets the letter quality flag.
   *
   * @return true, if letter quality is enabled.
   */
  public boolean isLetterQuality()
  {
    return letterQuality;
  }

  /**
   * Gets the AutoLF flag.
   *
   * @return true, if AutoLF is enabled.
   */
  public boolean isAutoLf()
  {
    return autoLf;
  }

  /**
   * Resets the printer to the default values. This performs a reset and then sets the
   * values defined for this CommandSet.
   *
   * @throws IOException if there was an IOError while writing the command
   */
  public void resetPrinter() throws IOException
  {
    // make sure that autoLF is disabled ..
    setCharacterWidth((byte) getDefaultCPI());
    setLineSpacing((byte) getDefaultLPI());
    setAutoLF(true);
    setAutoLF(false);
    setCodePage("Cp437");
    setFont(SELECT_FONT_FROM_MENU);
    setFontStyle(false, false, false, false);
    setPrintQuality(false);

    final PageFormatFactory fact = PageFormatFactory.getInstance();
    final Paper paper = pageFormat.getPaper();
    final int cWidthPoints = 72 / getCharacterWidth();
    final int left = (int) (fact.getLeftBorder(paper) / cWidthPoints);
    final int right = (int) (fact.getRightBorder(paper) / cWidthPoints);
    setHorizontalBorder(left, right);

    final int top = (int) (fact.getTopBorder(paper) * 20);
    final int bottom = (int) (fact.getBottomBorder(paper) * 20);
    setVerticalBorder(top, bottom);

    final int lines = (int) ((paper.getHeight() / 72) * getDefaultLPI());
    setPaperSize(lines);
  }

  /**
   * Starts the current page. Prints empty lines.
   *
   * @throws IOException if there was an IOError while writing the command
   */
  public void startPage() throws IOException
  {
    if (firstPage)
    {
      final int spaceUsage = encodingHeader.length - space.length;
      out.write(encodingHeader, 0, spaceUsage);
      setFirstPage(false);
    }

    final int topBorderLines = ((getBorderTop() / 1440) / getLineSpacing());
    for (int i = 0; i < topBorderLines; i++)
    {
      startLine();
      endLine();
    }
  }

  /**
   * Ends the current page. Prints empty lines.
   *
   * @throws IOException if there was an IOError while writing the command
   */
  public void endPage() throws IOException
  {
    final int bottomBorderLines = ((getBorderBottom() / 1440) / getLineSpacing());
    for (int i = 0; i < bottomBorderLines; i++)
    {
      startLine();
      endLine();
    }
    writeControlChar(FORM_FEED);
  }

  /**
   * Writes the given control character.
   *
   * @param ctrl the control character.
   * @throws IOException if an error occured.
   */
  protected void writeControlChar (final byte ctrl) throws IOException
  {
    // encode as ascii string ...
    final String s = new String (new byte[]{ctrl}, "ASCII");
    writeEncodedText(s);
  }

  /**
   * Starts a new line.
   *
   * @throws IOException if an IOError occures.
   */
  public void startLine() throws IOException
  {
    emptyCellCounter = getBorderLeft();
  }

  /**
   * Ends a new line.
   *
   * @throws IOException if an IOError occures.
   */
  public void endLine() throws IOException
  {
    emptyCellCounter = 0;
    // CR = (ASCII #13) reset the print position to the start of the line
    // LF = (ASCII #10) scroll down a new line (? Auto-LF feature ?)
    writeControlChar(CARRIAGE_RETURN);
    if (isAutoLf() == false)
    {
      writeControlChar(LINE_FEED);
    }
  }

  /**
   * Prints a single text chunk. The chunk is not printed, if an previous
   * chunk overlays this chunk,
   *
   * @param chunk the chunk that should be written
   * @param x the column where to start to print the chunk
   * @throws IOException if an IO error occured.
   */
  public void printChunk(final PlainTextPage.TextDataChunk chunk, final int x) throws IOException
  {
    if (emptyCellCounter != 0)
    {
      for (int i = 0; i < emptyCellCounter; i++)
      {
        out.write(space);
      }
      emptyCellCounter = 0;
    }

    if (chunk.getX() != x)
    {
      return;
    }

    final FontDefinition fd = chunk.getFont();
    setFontStyle(fd.isBold(), fd.isItalic(), fd.isUnderline(), fd.isStrikeThrough());

    final StringBuffer buffer = new StringBuffer(chunk.getText()); // this space is removed later ..
    for (int i = buffer.length(); i < chunk.getWidth(); i++)
    {
      buffer.append(' ');
    }
    writeEncodedText(buffer.toString());
  }

  /**
   * Writes encoded text for the current encoding into the output stream.
   *
   * @param textString the text that should be written.
   * @throws IOException if an error occures.
   */
  protected void writeEncodedText (final String textString) throws IOException
  {
    final StringBuffer buffer = new StringBuffer(" ");
    buffer.append(textString);
    final byte[] text = buffer.toString().getBytes(getCodepage());
    out.write(text, encodingHeader.length, text.length - encodingHeader.length);
  }

  /**
   * Prints an empty chunk. This is called for all undefined chunk-cells.
   * @throws IOException if an IOError occured.
   */
  public void printEmptyChunk() throws IOException
  {
    out.write(space);
  }

  /**
   * Flushes the output stream.
   * @throws IOException if an IOError occured.
   */
  public void flush() throws IOException
  {
    getOut().flush();
  }

  /**
   * Tests whether the given encoding is supported.
   *
   * @param encoding the encoding that should be tested.
   * @return true, if the encoding is supported, false otherwise.
   */
  public boolean isEncodingSupported(final String encoding)
  {
    if (EncodingSupport.isSupportedEncoding(encoding))
    {
      // if already checked there, then use it ...
      return true;
    }
    return false;
  }

  /**
   * Returns true, if the current or next page will be the first page of
   * this printer command set.
   *
   * @return true, if this is the first page, false otherwise.
   */
  public boolean isFirstPage()
  {
    return firstPage;
  }

  /**
   * Defines whether this is the first page that is printed, false otherwise.
   *
   * @param firstPage the new first page flag.
   */
  protected void setFirstPage(final boolean firstPage)
  {
    this.firstPage = firstPage;
  }
}
