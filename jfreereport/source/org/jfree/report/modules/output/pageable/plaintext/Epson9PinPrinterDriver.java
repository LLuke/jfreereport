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
 * $Id: Epson9PinPrinterDriver.java,v 1.6 2005/03/03 23:00:01 taqua Exp $
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

    if (driverState.isBold())
    {
      if (bold == false)
      {
        // disable bold
        out.write(0x1b); // ESC
        out.write(0x46); // F
      }
    }
    else
    {
      if (bold == true)
      {
        // enable bold
        out.write(0x1b); // ESC
        out.write(0x45); // E
      }
    }

    if (driverState.isItalic())
    {
      if (italic == false)
      {
        // disable italic
        out.write(0x1b); // ESC
        out.write(0x35); // 5
      }
    }
    else
    {
      if (italic == true)
      {
        // enable italic
        out.write(0x1b); // ESC
        out.write(0x34); // 4
      }
    }


    if (driverState.isUnderline())
    {
      if (underline == false)
      {
        // disable underline
        out.write(0x1b); // ESC
        out.write(0x2d); // -
        out.write(0x00); // 0
      }
    }
    else
    {
      if (underline == true)
      {
        // enable underline
        out.write(0x1b); // ESC
        out.write(0x2d); // -
        out.write(0x01); // 1
      }
    }
    driverState.setBold(bold);
    driverState.setItalic(italic);
    driverState.setUnderline(underline);
    driverState.setStrikethrough(false);
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
