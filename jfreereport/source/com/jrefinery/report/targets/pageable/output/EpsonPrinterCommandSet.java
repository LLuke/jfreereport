/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ---------------------------
 * EpsonPrinterCommandSet.java
 * ---------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: EpsonPrinterCommandSet.java,v 1.10 2003/05/02 12:40:34 taqua Exp $
 *
 * Changes
 * -------
 * 30-Jan-2003 : Initial version
 * 10-Feb-2003 : Documentation
 */
package com.jrefinery.report.targets.pageable.output;

import java.awt.print.PageFormat;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.jrefinery.report.util.EncodingSupport;
import com.jrefinery.report.util.StringUtil;

/**
 * Implements the printer command set for Epson ESC/P compatible printers.
 * This implementation assumes that AutoLF is disabled.
 * <p>
 * This implementation is untested. If you have access to an ESC/P compatible
 * printer, you could try this command set to improve printing quality.
 *
 * @see PrinterCommandSet
 * @see PlainTextOutputTarget
 *
 * @author Thomas Morgner
 */
public class EpsonPrinterCommandSet extends PrinterCommandSet
{
  /**
   * Creates a new EpsonPrinterCommandSet.
   *
   * @param out the target output stream
   * @param format the pageformat of the used report
   * @param defaultCPI the characters-per-inch for the output.
   * @param defaultLPI the lines-per-inch for the output.
   */
  public EpsonPrinterCommandSet(OutputStream out, PageFormat format, int defaultCPI, int defaultLPI)
  {
    super(out, format, defaultCPI, defaultLPI);
  }

  /**
   * Defines the new font for the printer. The font must be one of the supported fonts,
   * the available fonts are defined in PrinterCommandSet. You may use additional fonts,
   * if your printer supports them.
   * <p>
   * To use these fonts, specify the font parameter for the escape sequence 0x1b, 0x6b,
   * [font-selection] as defined in your printers reference manual.
   *
   * @param fontSelection the printers font selection token.
   * @throws IOException if there was an IOError while writing the command.
   */
  public void setFont(byte fontSelection) throws IOException
  {
    if (fontSelection == getFont())
    {
      return;
    }
    getOut().write(0x1b);
    getOut().write(0x6b);
    getOut().write(fontSelection);
    super.setFont(fontSelection);
  }

  /**
   * Disables the compressed print mode. This mode is used to create smaller fontwidths
   * on epson printers.
   *
   * @throws IOException if there was an IOError while writing the command.
   */
  private void disableCompressedPrint()
      throws IOException
  {
    if (getCharacterWidth() > 15)
    {
      getOut().write(0x0f);
    }
  }

  /**
   * Enables the compressed print mode. This mode is used to create smaller fontwidths
   * on epson printers.
   *
   * @throws IOException if there was an IOError while writing the command.
   */
  private void enableCompressedPrint()
      throws IOException
  {
    if (getCharacterWidth() <= 15)
    {
      getOut().write(0x12);
    }
  }

  /**
   * Defines the character width for the current font. The width is specified in
   * Characters-per-inch. Valid values are 10, 12, 15, 17 and 20 cpi.
   *
   * @param charWidth the character width in CPI.
   * @throws IOException if there was an IOError while writing the command or if the
   *   character width is not supported by the printer.
   */
  public void setCharacterWidth(byte charWidth) throws IOException
  {
    if (charWidth == getCharacterWidth())
    {
      return;
    }

    switch (charWidth)
    {
      case 10:
        {
          disableCompressedPrint();
          getOut().write(0x1b);
          getOut().write(0x50);
          super.setCharacterWidth(charWidth);
          break;
        }
      case 12:
        {
          disableCompressedPrint();
          getOut().write(0x1b);
          getOut().write(0x4d);
          super.setCharacterWidth(charWidth);
          break;
        }
      case 15:
        {
          disableCompressedPrint();
          getOut().write(0x1b);
          getOut().write(0x67);
          super.setCharacterWidth(charWidth);
          break;
        }
      case 17:
        {
          enableCompressedPrint();
          getOut().write(0x1b);
          getOut().write(0x50);
          super.setCharacterWidth(charWidth);
          break;
        }
      case 20:
        {
          enableCompressedPrint();
          getOut().write(0x1b);
          getOut().write(0x4d);
          super.setCharacterWidth(charWidth);
          break;
        }
      default:
        throw new IOException("This character width is not supported. Use one of 10,12,15,17,20");
    }
  }

  /**
   * Defines the font style for the printed text. This implementation does not
   * support strike-through, due to a missing reference manual for epson printers.
   *
   * @param bold true, if the text should be printed in bold mode.
   * @param italic true, if the text should be italic, false otherwise
   * @param underline true, if the text should be underlined, false otherwise
   * @param strike true, if the text should be strikethrough, false otherwise
   * @throws IOException if there was an IOError while writing the command
   */
  public void setFontStyle(boolean bold, boolean italic, boolean underline, boolean strike)
      throws IOException
  {
    if (isBold())
    {
      if (bold == false)
      {
        // disable bold
        getOut().write(0x1b);
        getOut().write(0x46);
      }
    }
    else
    {
      if (bold == true)
      {
        // enable bold
        getOut().write(0x1b);
        getOut().write(0x45);
      }
    }

    if (isItalic())
    {
      if (italic == false)
      {
        // disable italic
        getOut().write(0x1b);
        getOut().write(0x35);
      }
    }
    else
    {
      if (italic == true)
      {
        // enable italic
        getOut().write(0x1b);
        getOut().write(0x34);
      }
    }

    if (isUnderline())
    {
      if (underline == false)
      {
        // disable underline
        getOut().write(0x1b);
        getOut().write(0x2d);
        getOut().write(0x30);
      }
    }
    else
    {
      if (bold == true)
      {
        // enable underline
        getOut().write(0x1b);
        getOut().write(0x2d);
        getOut().write(0x31);
      }
    }

    if (isStrikethrough())
    {
      if (strike == false)
      {
        // disable underline
        getOut().write(0x1b);
        getOut().write(0x2d);
        getOut().write(0x30);
      }
    }
    else
    {
      if (strike == true)
      {
        // enable underline
        getOut().write(0x1b);
        getOut().write(0x2d);
        getOut().write(0x31);
      }
    }

    // dont know about strike-through, argh, I'll need a ESC/P reference book...
    super.setFontStyle(bold, italic, underline, false);
  }

  /**
   * Defines the papersize in lines.
   *
   * @param lines the number of lines that could be printed on a single page.
   * @throws IOException if there was an IOError while writing the command
   */
  public void setPaperSize(int lines) throws IOException
  {
    getOut().write(0x1b);
    getOut().write(0x43);
    getOut().write(lines);
    super.setPaperSize(lines);
  }

  /**
   * Defines the horizontal borders for the current paper. The borders are given
   * in characters.
   *
   * @param left the number of spaces printed on the start of a line.
   * @param right the number of spaces left free on the right paper border.
   * @throws IOException if an IOException occured while updating the printer state.
   */
  public void setHorizontalBorder(int left, int right) throws IOException
  {
    getOut().write(0x1b);
    getOut().write(0x6c);
    getOut().write(left);
    getOut().write(0x1b);
    getOut().write(0x51);
    getOut().write(right);
    super.setHorizontalBorder(left, right);
  }

  /**
   * Defines the line spacing for the printer, the spacing is given in
   * 1/1440 inches.
   *
   * @param spaceInInch the linespacing in 1/1440 inches.
   * @throws IOException if an IOException occured while updating the printer state.
   */
  public void setLineSpacing(int spaceInInch) throws IOException
  {
    // convert into 1/360 inch
    int spacePar = (spaceInInch / 5);
    getOut().write(0x1b);
    getOut().write(0x2b);
    getOut().write(spacePar);
    super.setLineSpacing(spaceInInch);
  }

  /**
   * Defines the code page for the text to be printed. The codepage is an
   * 8-Bit encoding scheme to print non-ascii-characters.
   *
   * @param codepage the new codepage that should be used.
   * @throws IOException if there was an IOError while writing the command
   */
  public void setCodePage(String codepage) throws IOException
  {
    // http://ecc400.com/intermec/pdf/77019001_6820tr.pdf
    // page 48
    int[] cp = translateCodePage(codepage);
    getOut().write(0x1b);
    getOut().write(0x52);
    getOut().write(0xff);
    getOut().write(cp[0]);
    getOut().write(cp[1]);
    super.setCodePage(codepage);
  }

  /**
   * This implementation is empty, the Epson Printer Language defines no command
   * to alter the AutoLF feature from within a programm, we assume no AutoLF.
   *
   * @param autoLF this parameter is ignored.
   * @throws IOException if there was an IOError while writing the command
   */
  public void setAutoLF(boolean autoLF) throws IOException
  {
    // epson has no notion of auto lf, however it is configurable from
    // the printers menu, not with control characters ...
  }

  /**
   * Always returns false, as ESC/P has we assume AutoLF to be disabled for all
   * Epson-Printers.
   *
   * @return always false, as we have to assume that AutoLF is disabled.
   */
  public boolean isAutoLf()
  {
    return false;
  }

  /**
   * Defines the printing quality for the printed text. Set to true, to
   * enable LetterQuality printing, false enables Draft-Quality.
   *
   * @param letterQuality true, if letter quality should be used, false for draft-quality
   * @throws IOException if there was an IOError while writing the command
   */
  public void setPrintQuality(boolean letterQuality) throws IOException
  {
    if (letterQuality)
    {
      getOut().write(0x1b);
      getOut().write(0x78);
      getOut().write(0x01);
    }
    else
    {
      getOut().write(0x1b);
      getOut().write(0x78);
      getOut().write(0x00);
    }
    super.setPrintQuality(letterQuality);
  }

  /**
   * Translates the given Codepage String into a Epson Byte Code. The encoding
   * string must be in the format CpXXXX where XXXX is the number of the codepage.
   * <p>
   * You may test whether the printer supports a certain encoding by calling
   * {@link EpsonPrinterCommandSet#isEncodingSupported}.
   *
   * @param cp the code page
   * @return the epson byte code.
   * @throws UnsupportedEncodingException if the encoding is not supported.
   */
  private static int[] translateCodePage(String cp)
      throws UnsupportedEncodingException
  {
    if (StringUtil.startsWithIgnoreCase(cp, "cp"))
    {
      // check the supplied encoding ...
      // only Cp- encodings are supported ...
      if (EncodingSupport.isSupportedEncoding(cp) == false)
      {
        throw new UnsupportedEncodingException("The encoding " + cp + "is not valid");
      }

      try
      {
        int i = Integer.parseInt(cp.substring(2));
        int[] retval = new int[2];
        retval[0] = i / 256;
        retval[1] = i % 256;
        return retval;
      }
      catch (Exception e)
      {
        throw new UnsupportedEncodingException("The encoding " + cp + "is not valid");
      }
    }
    throw new UnsupportedEncodingException("The encoding " + cp + " is no codepage encoding");
  }

  /**
   * This implementation is empty, as epson printers handle the left border automaticly.
   *
   * @throws IOException if there was an IOError while writing the command
   */
  public void startLine() throws IOException
  {
  }

  /**
   * Resets the printer to the default values. This performs a reset and then sets the
   * values defined for this CommandSet.
   *
   * @throws IOException if there was an IOError while writing the command
   */
  public void resetPrinter() throws IOException
  {
    getOut().write(0x1b);
    getOut().write(0x40);
    super.resetPrinter();
  }

  /**
   * Checks, whether the given encoding string is supported by this printer command set.
   *
   * @param encoding the encoding that should be tested.
   * @return true, if the encoding is valid, false otherwise.
   */
  public boolean isEncodingSupported(String encoding)
  {
    try
    {
      translateCodePage(encoding);
      return true;
    }
    catch (UnsupportedEncodingException use)
    {
      return false;
    }
  }
}
