/**
 * Date: Jan 30, 2003
 * Time: 5:58:45 PM
 *
 * $Id: EpsonPrinterCommandSet.java,v 1.1 2003/01/30 22:58:44 taqua Exp $
 */
package com.jrefinery.report.targets.pageable.output;

import com.jrefinery.report.util.StringUtil;

import java.awt.print.PageFormat;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class EpsonPrinterCommandSet extends PrinterCommandSet
{
  public EpsonPrinterCommandSet(OutputStream out, PageFormat format, int defaultCPI, int defaultLPI)
  {
    super(out, format, defaultCPI, defaultLPI);
  }

  public void setFont(byte fontSelection) throws IOException
  {
    if (fontSelection == getFont())
      return;

    getOut().write (0x1b);
    getOut().write (0x6b);
    getOut().write (fontSelection);
    super.setFont(fontSelection);
  }

  private void disableCompressedPrint ()
    throws IOException
  {
    if (getCharacterWidth() > 15)
    {
      getOut().write(0x0f);
    }
  }

  private void enableCompressedPrint ()
    throws IOException
  {
    if (getCharacterWidth() > 15)
    {
      getOut().write(0x12);
    }
  }

  public void setCharacterWidth(byte charWidth) throws IOException
  {
    if (charWidth == getCharacterWidth()) return;

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

  public void setPaperSize(int lines) throws IOException
  {
    getOut().write(0x1b);
    getOut().write(0x43);
    getOut().write(lines);
    super.setPaperSize(lines);
  }

  // seitenränder horizontal in zeichen ... (== leerzeichen)
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

  // vertical in 1/1440 zoll ..
  public void setVerticalBorder(int top, int bottom) throws IOException
  {
    // this is not supported here ...
    super.setVerticalBorder(top, bottom);
  }

  // vertical in 1/1440 inch ..
  public void setLineSpacing(int spaceInInch) throws IOException
  {
    // convert into 1/360 inch
    int spacePar = (spaceInInch / 5);
    getOut().write(0x1b);
    getOut().write(0x2b);
    getOut().write(spacePar);
    super.setLineSpacing(spaceInInch);
  }

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

  public void setAutoLF(boolean autoLF) throws IOException
  {
    super.setAutoLF(autoLF);
  }

  public void setPrintQuality(boolean letterQuality) throws IOException
  {
    super.setPrintQuality(letterQuality);
  }

  public static int[] translateCodePage (String cp)
    throws UnsupportedEncodingException
  {
    if (StringUtil.startsWithIgnoreCase(cp, "cp"))
    {
      // check the supplied encoding ...
      // only Cp- encodings are supported ...
      new String(" ").getBytes(cp);
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

  public void startLine() throws IOException
  {
  }

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
