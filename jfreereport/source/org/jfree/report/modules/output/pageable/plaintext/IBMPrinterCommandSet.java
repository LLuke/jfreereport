/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * -------------------------
 * IBMPrinterCommandSet.java
 * -------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: IBMPrinterCommandSet.java,v 1.6 2003/08/25 14:29:31 taqua Exp $
 *
 * Changes
 * -------
 * 30-Jan-2003 : Initial version
 * 10-Feb-2003 : Documentation
 */
package org.jfree.report.modules.output.pageable.plaintext;

import java.awt.print.PageFormat;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.jfree.report.modules.gui.base.components.EncodingSupport;
import org.jfree.report.util.StringUtil;

/**
 * Implements the printer command set for IBM compatible printers.
 * <p>
 * This implementation is untested. If you have access to an IBM compatible
 * printer, you could try this command set to improve printing quality.
 * <p>
 * This implementation does only work for 8-Bit character sets.
 *
 * @see PrinterCommandSet
 * @see PlainTextOutputTarget
 *
 * @author Thomas Morgner
 */
public strictfp class IBMPrinterCommandSet extends PrinterCommandSet
{
  /**
   * Creates a new IBMPrinterCommandSet.
   *
   * @param out the target output stream
   * @param format the pageformat of the used report
   * @param defaultCPI the characters-per-inch for the output.
   * @param defaultLPI the lines-per-inch for the output.
   */
  public IBMPrinterCommandSet(final OutputStream out, final PageFormat format,
                              final int defaultCPI, final int defaultLPI)
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
   * @throws java.io.IOException if there was an IOError while writing the command.
   */
  public void setFont(final byte fontSelection)
      throws IOException
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
   * Defines the character width for the current font. The width is specified in
   * Characters-per-inch. Valid values are 10, 12, 15, 17 and 20 cpi.
   *
   * @param charWidth the character width in CPI.
   * @throws java.io.IOException if there was an IOError while writing the command or if the
   *   character width is not supported by the printer.
   */
  public void setCharacterWidth(final byte charWidth) throws IOException
  {
    if (charWidth == getCharacterWidth())
    {
      return;
    }

    switch (charWidth)
    {
      case 10:
        {
          getOut().write(0x12);
          super.setCharacterWidth(charWidth);
          break;
        }
      case 12:
        {
          getOut().write(0x1b);
          getOut().write(0x3a);
          super.setCharacterWidth(charWidth);
          break;
        }
      case 15:
        {
          getOut().write(0x1b);
          getOut().write(0x67);
          super.setCharacterWidth(charWidth);
          break;
        }
      case 17:
        {
          getOut().write(0x0f);
          super.setCharacterWidth(charWidth);
          break;
        }
      case 20:
        {
          getOut().write(0x1b);
          getOut().write(0x0f);
          super.setCharacterWidth(charWidth);
          break;
        }
      default:
        throw new IOException("This character width is not supported. Use one of 10,12,15,17,20");
    }
  }

  /**
   * Defines the font style for the printed text. The IBM-CommandSet does not
   * support strike-through.
   *
   * @param bold true, if the text should be printed in bold mode.
   * @param italic true, if the text should be italic, false otherwise
   * @param underline true, if the text should be underlined, false otherwise
   * @param strike true, if the text should be strikethrough, false otherwise
   * @throws java.io.IOException if there was an IOError while writing the command
   */
  public void setFontStyle(final boolean bold, final boolean italic,
                           final boolean underline, final boolean strike)
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
        getOut().write(0x25);
        getOut().write(0x48);
      }
    }
    else
    {
      if (italic == true)
      {
        // enable italic
        getOut().write(0x1b);
        getOut().write(0x25);
        getOut().write(0x47);
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
    // strikethrough is not supported ...
    super.setFontStyle(bold, italic, underline, false);
  }

  /**
   * Defines the papersize in lines.
   *
   * @param lines the number of lines that could be printed on a single page.
   * @throws java.io.IOException if there was an IOError while writing the command
   */
  public void setPaperSize(final int lines)
      throws IOException
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
   * @throws java.io.IOException if an IOException occured while updating the printer state.
   */
  public void setHorizontalBorder(final int left, final int right)
      throws IOException
  {
    getOut().write(0x1b);
    getOut().write(0x58);
    getOut().write(left);
    getOut().write(right);
    super.setHorizontalBorder(left, right);
  }

  /**
   * Defines the horizontal borders for the current paper. The borders are given
   * in characters.
   *
   * @param top the number of blank lines printed on the start of a page.
   * @param bottom the number of blank lines printed at the end of a page
   * @throws java.io.IOException if an IOException occured while updating the printer state.
   */
  public void setVerticalBorder(final int top, final int bottom)
      throws IOException
  {
    final int top1 = (top / 256);
    final int top2 = (top % 256);
    final int bottom1 = (bottom / 256);
    final int bottom2 = (bottom % 256);
    getOut().write(0x1b);
    getOut().write(0x5b);
    getOut().write(0x04);
    getOut().write(0x00);
    getOut().write(top1);
    getOut().write(top2);
    getOut().write(bottom1);
    getOut().write(bottom2);
    super.setVerticalBorder(top, bottom);
  }

  /**
   * Defines the line spacing for the printer, the spacing is given in
   * 1/1440 inches.
   *
   * @param spaceInInch the linespacing in 1/1440 inches.
   * @throws java.io.IOException if an IOException occured while updating the printer state.
   */
  public void setLineSpacing(final int spaceInInch)
      throws IOException
  {
    // round it to 1/72 inch, its easier this way...
    final int spacePar = (spaceInInch / 20);
    getOut().write(0x1b);
    getOut().write(0x41);
    getOut().write(spacePar);
    getOut().write(0x1b);
    getOut().write(0x32);
    super.setLineSpacing(spaceInInch);
  }

  /**
   * Defines the code page for the text to be printed. The codepage is an
   * 8-Bit encoding scheme to print non-ascii-characters.
   *
   * @param codepage the new codepage that should be used.
   * @throws java.io.IOException if there was an IOError while writing the command
   */
  public void setCodePage(final String codepage)
      throws IOException
  {
    final int[] cp = translateCodePage(codepage);
    getOut().write(0x1b);
    getOut().write(0x5b);
    getOut().write(0x54);
    getOut().write(0x05);
    getOut().write(0x00);
    getOut().write(0x00);
    getOut().write(0x00);
    getOut().write(cp[0]);
    getOut().write(cp[1]);
    getOut().write(0x00);
    super.setCodePage(codepage);
  }

  /**
   * Defines whether to print in AutoLF mode. If AutoLF is enabled, then a linefeed
   * character is automaticly printed after every &lt;CR&gt; character.
   *
   * @param autoLF the new autoLF state
   * @throws java.io.IOException if there was an IOError while writing the command
   */
  public void setAutoLF(final boolean autoLF)
      throws IOException
  {
    if (autoLF == false)
    {
      if (isAutoLf())
      {
        getOut().write(0x1b);
        getOut().write(0x35);
        getOut().write(0x30);
      }
    }
    else
    {
      if (isAutoLf() == false)
      {
        getOut().write(0x1b);
        getOut().write(0x35);
        getOut().write(0x31);
      }
    }
    super.setAutoLF(autoLF);
  }

  /**
   * Translates the given Codepage String into a Epson Byte Code. The encoding
   * string must be in the format CpXXXX where XXXX is the number of the codepage.
   * <p>
   * You may test whether the printer supports a certain encoding by calling
   * {@link IBMPrinterCommandSet#isEncodingSupported}.
   *
   * @param cp the code page
   * @return the epson byte code.
   * @throws java.io.UnsupportedEncodingException if the encoding is not supported.
   */
  private static int[] translateCodePage(final String cp)
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
        final int i = Integer.parseInt(cp.substring(2));
        final int[] retval = new int[2];
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
   * This implementation is empty, as IBM printers handle the left border automaticly.
   *
   * @throws java.io.IOException if there was an IOError while writing the command
   */
  public void startLine() throws IOException
  {
  }

  /**
   * This implementation is empty, as IBM printers handle the top borders automaticly.
   *
   * @throws java.io.IOException if there was an IOError while writing the command
   */
  public void startPage() throws IOException
  {
  }

  /**
   * This implementation send the form feed command, IBM printers handle the bottom
   * border automaticly.
   *
   * @throws java.io.IOException if there was an IOError while writing the command
   */
  public void endPage() throws IOException
  {
    writeControlChar(FORM_FEED);
  }

  /**
   * Checks, whether the given encoding string is supported by this printer command set.
   *
   * @param encoding the encoding that should be tested.
   * @return true, if the encoding is valid, false otherwise.
   */
  public boolean isEncodingSupported(final String encoding)
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
