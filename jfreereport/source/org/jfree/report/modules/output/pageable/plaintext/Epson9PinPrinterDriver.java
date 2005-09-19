/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * Epson9PinPrinterDriver.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: Epson9PinPrinterDriver.java,v 1.8 2005/09/09 14:23:49 mtennes Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.output.pageable.plaintext;

import java.io.IOException;
import java.io.OutputStream;

import org.jfree.report.JFreeReportBoot;

public class Epson9PinPrinterDriver extends AbstractEpsonPrinterDriver
{
    private static final byte TWELVECPI = 0x01;
    private static final byte CONDENSED = 0x04;
    private static final byte BOLD = 0x08;
    private static final byte ITALICS = 0x40;
    private static final byte UNDERLINE = (byte) 0x80;
    private int masterselect = 0;
  private static PrinterSpecificationManager printerSpecificationManager;
  private static final String SPECIFICATION_RESOURCE =
          "epson-9pin-printer-specifications.properties";
  public static final String EPSON_9PIN_PRINTER_TYPE =
          "org.jfree.report.modules.output.pageable.plaintext.epson.9PinPrinterType";

  public Epson9PinPrinterDriver (final OutputStream out,
                                 final float charsPerInch,
                                 final float linesPerInch,
                                 final String printerModel)
  {
    super(out, charsPerInch, linesPerInch, printerModel);
  }

  protected void sendDefineLineSpacing (final float lineHeightInPoints)
          throws IOException
  {
    // All printers support that command.
    final int spacePar = (int) (lineHeightInPoints * 3); // 1/216
    getOut().write(0x1b); // ESC
    getOut().write(0x33); // 3
    getOut().write(spacePar);
  }


  protected void sendFontStyle (final boolean bold, final boolean italic,
                                final boolean underline, final boolean strikeTrough)
          throws IOException
  {
    final OutputStream out = getOut();
    final DriverState driverState = getDriverState();
      byte[] bytes = new byte[8];
      int byteindex = 0;

    if (driverState.isBold())
    {
      if (bold == false)
      {
        // disable bold
          masterselect &= ~BOLD;
          bytes[byteindex++] = 0x1b;
          bytes[byteindex++] = 0x46;
      }
    }
    else
    {
      if (bold == true)
      {
        // enable bold
          masterselect |= BOLD;
          bytes[byteindex++] = 0x1b;
          bytes[byteindex++] = 0x45;
      }
    }

    if (driverState.isItalic())
    {
      if (italic == false)
      {
        // disable italic
          masterselect &= ~ITALICS;
          bytes[byteindex++] = 0x1b;
          bytes[byteindex++] = 0x35;
      }
    }
    else
    {
      if (italic == true)
      {
        // enable italic
          masterselect |= ITALICS;
          bytes[byteindex++] = 0x1b;
          bytes[byteindex++] = 0x34;
      }
    }


    if (driverState.isUnderline())
    {
      if (underline == false)
      {
        // disable underline
          masterselect &= ~UNDERLINE;
          bytes[byteindex++] = 0x1b;
          bytes[byteindex++] = 0x2d;
          bytes[byteindex++] = 0x00;
      }
    }
    else
    {
      if (underline == true)
      {
        // enable underline
          masterselect |= UNDERLINE;
          bytes[byteindex++] = 0x1b;
          bytes[byteindex++] = 0x2d;
          bytes[byteindex++] = 0x01;
      }
    }
      final boolean useMasterSelect =
      JFreeReportBoot.getInstance().getExtendedConfig().getBoolProperty
      ("org.jfree.report.modules.output.pageable.plaintext.UseEpsonMasterSelect");

      if(useMasterSelect) {
          out.write(0x1b); // disable condensed printing
          out.write(0x21);
          out.write((byte) masterselect);
      } else {
          for (int i = 0; i < byteindex; i++) {
              out.write(bytes[i]);
          }
      }
    driverState.setBold(bold);
    driverState.setItalic(italic);
    driverState.setUnderline(underline);
    driverState.setStrikethrough(false);
  }

    protected void sendDefineCharacterWidth(final float charactersPerInch)
            throws IOException {
        byte[] bytes = new byte[4];
        int byteindex = 0;
        boolean useMasterSelect =
        JFreeReportBoot.getInstance().getExtendedConfig().getBoolProperty
        ("org.jfree.report.modules.output.pageable.plaintext.UseEpsonMasterSelect");
        if (charactersPerInch == CPI_10) {
            masterselect &= ~TWELVECPI;
            bytes[byteindex++] = 0x12; // disable condensed printing
            bytes[byteindex++] = 0x1b;
            bytes[byteindex++] = 0x50; // select 10 CPI
        } else if (charactersPerInch == CPI_12) {
            masterselect |= TWELVECPI;
            bytes[byteindex++] = 0x12; // disable condensed printing
            bytes[byteindex++] = 0x1b;
            bytes[byteindex++] = 0x4d; // select 12 CPI
         } else if (charactersPerInch == CPI_15) {
            // All ESC/P2 and 24Pin ESC/P printers support that mode
            // Additionally, the 9Pin printer models FX-2170 and DFX-5000+
            // support that character width.
            bytes[byteindex++] = 0x12; // disable condensed printing
            bytes[byteindex++] = 0x1b;
            bytes[byteindex++] = 0x67; // select 15 CPI
            useMasterSelect = false;
        } else if (charactersPerInch == CPI_17) {
            masterselect |= CONDENSED;
            masterselect &= ~TWELVECPI;
            bytes[byteindex++] = 0x0f; // enable condensed printing
            bytes[byteindex++] = 0x1b;
            bytes[byteindex++] = 0x50; // select 10 CPI (-> 17.14 cpi because of condensed printing)
        } else if (charactersPerInch == CPI_20) {
            masterselect |= CONDENSED;
            masterselect |= TWELVECPI;
            bytes[byteindex++] = 0x0f; // enable condensed printing
            bytes[byteindex++] = 0x1b;
            bytes[byteindex++] = 0x4d; // select 12 CPI (-> 20 cpi because of condensed printing)
        } else {
            throw new IllegalArgumentException("The given character width is invalid");
        }

        if(useMasterSelect) {
            getOut().write(0x1b);
            getOut().write(0x21);
            getOut().write((byte) masterselect);
        } else {
            for (int i = 0; i < byteindex; i++) {
                getOut().write(bytes[i]);
            }
        }
    }

  protected PrinterSpecificationManager getPrinterSpecificationManager ()
  {
    return loadSpecificationManager();
  }

  public static synchronized PrinterSpecificationManager loadSpecificationManager ()
  {
    if (printerSpecificationManager == null)
    {
      printerSpecificationManager = new PrinterSpecificationManager();
      printerSpecificationManager.load(SPECIFICATION_RESOURCE);
    }
    return printerSpecificationManager;
  }

  public static String getDefaultPrinter ()
  {
    return JFreeReportBoot.getInstance().getGlobalConfig().getConfigProperty
            (EPSON_9PIN_PRINTER_TYPE, "Generic 9-Pin printer");
  }
}