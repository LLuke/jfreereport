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
 * Epson24PinPrinterDriver.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.output.pageable.plaintext;

import java.io.IOException;
import java.io.OutputStream;

import org.jfree.report.util.ReportConfiguration;

public class Epson24PinPrinterDriver extends AbstractEpsonPrinterDriver
{
  private static final String SPECIFICATION_RESOURCE =
          "epson-24pin-printer-specifications.properties";
  public static final String EPSON_24PIN_PRINTER_TYPE =
          "org.jfree.report.modules.output.pageable.plaintext.epson.24PinPrinterType";

  private static final String N_360TH_LINE_SPACING = "Epson24pin.n360inch-linespacing";
  private static final String SELECT_LINE_SCORE = "Epson24pin.select-line-score";

  private static PrinterSpecificationManager printerSpecificationManager;

  public Epson24PinPrinterDriver (final OutputStream out,
                                  final float charsPerInch, final float linesPerInch,
                                  final String printerModel)
  {
    super(out, charsPerInch, linesPerInch, printerModel);
  }

  protected void sendDefineLineSpacing (final float lineHeightInPoints)
          throws IOException
  {
    if (getPrinterSpecification().isFeatureAvailable(N_360TH_LINE_SPACING))
    {
      // Printers not supporting this command:
      //   ActionPrinter L-1000, ActionPrinter 3000, LQ-200, LQ-500
      //
      // Set the line spacing with a resolution of 1/360th of an inch
      final int spacePar = (int) (lineHeightInPoints * 5);
      getOut().write(0x1b); // ESC
      getOut().write(0x2b); // +
      getOut().write(spacePar);
    }
    else
    {
      // Set the line spacing with a resolution of 1/180th of an inch
      final int spacePar = (int) (lineHeightInPoints * 2.5); // 1/180
      getOut().write(0x1b); // ESC
      getOut().write(0x33); // 3
      getOut().write(spacePar);
    }
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

    // Printers not supporting this command:
    //   ActionPrinter L-1000, LQ-400, LQ-500, LQ-2550
    //
    if (getPrinterSpecification().isFeatureAvailable(SELECT_LINE_SCORE))
    {
      if (driverState.isStrikethrough())
      {
        if (strikeTrough == false)
        {
          // disable underline
          out.write(0x1b); // ESC
          out.write(0x28); // (
          out.write(0x2D); // -
          out.write(0x03); // const: 3
          out.write(0x00); // const: 0
          out.write(0x01); // const: 1
          out.write(0x02); // select strikethrough
          out.write(0x00); // disable
        }
      }
      else
      {
        if (strikeTrough == true)
        {
          // enable underline
          out.write(0x1b); // ESC
          out.write(0x28); // (
          out.write(0x2D); // -
          out.write(0x03); // const: 3
          out.write(0x00); // const: 0
          out.write(0x01); // const: 1
          out.write(0x02); // select strikethrough
          out.write(0x01); // enable with single continuous line
        }
      }
      driverState.setStrikethrough(strikeTrough);
    }
    else
    {
      driverState.setStrikethrough(false);
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
    return ReportConfiguration.getGlobalConfig().getConfigProperty
            (EPSON_24PIN_PRINTER_TYPE, "Generic 24-Pin printer");
  }
}
