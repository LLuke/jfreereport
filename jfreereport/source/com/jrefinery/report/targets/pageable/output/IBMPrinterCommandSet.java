/**
 * Date: Jan 30, 2003
 * Time: 4:44:46 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.output;

import com.jrefinery.report.util.StringUtil;

import java.awt.print.PageFormat;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class IBMPrinterCommandSet extends PrinterCommandSet
{
  public IBMPrinterCommandSet(OutputStream out, PageFormat format, int defaultCPI, int defaultLPI)
  {
    super(out, format, defaultCPI, defaultLPI);
  }

  public void setFont(byte fontSelection)
    throws IOException
  {
    if (fontSelection == getFont())
      return;

    getOut().write (0x1b);
    getOut().write (0x6b);
    getOut().write (fontSelection);
    super.setFont(fontSelection);
  }

  // in CPI
  public void setCharacterWidth(byte charWidth)throws IOException
  {
    if (charWidth == getCharacterWidth()) return;

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

  public void setPaperSize(int lines)
    throws IOException
  {
    getOut().write(0x1b);
    getOut().write(0x43);
    getOut().write(lines);
    super.setPaperSize(lines);
  }

  // seitenränder horizontal in zeichen ... (== leerzeichen)
  public void setHorizontalBorder(int left, int right)
    throws IOException
  {
    getOut().write(0x1b);
    getOut().write(0x58);
    getOut().write(left);
    getOut().write(right);
    super.setHorizontalBorder(left, right);
  }

  // vertical in 1/1440 zoll ..
  public void setVerticalBorder(int top, int bottom)
    throws IOException
  {
    int top1 = (top / 256);
    int top2 = (top % 256);
    int bottom1 = (bottom / 256);
    int bottom2 = (bottom % 256);
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

  // vertical in 1/1440 inch ..
  public void setLineSpacing(int spaceInInch)
    throws IOException
  {
    // round it to 1/72 inch, its easier this way...
    int spacePar = (spaceInInch / 20);
    getOut().write(0x1b);
    getOut().write(0x41);
    getOut().write(spacePar);
    getOut().write(0x1b);
    getOut().write(0x32);
    super.setLineSpacing(spaceInInch);
  }

  public void setCodePage(String codepage)
    throws IOException
  {
    int[] cp = translateCodePage(codepage);
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

  public void setAutoLF(boolean autoLF)
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

  // dont know how to set printquality for IBM protocol ...
  public void setPrintQuality(boolean letterQuality)
    throws IOException
  {
    super.setPrintQuality(false);
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

  public void startPage() throws IOException
  {
  }

  public void endPage() throws IOException
  {
    getOut().write(FORM_FEED);
  }
}
