package org.jfree.report.modules.output.pageable.plaintext;

import java.io.IOException;
import java.io.OutputStream;

public class Epson9PinPrinterDriver extends AbstractEpsonPrinterDriver
{
  private static PrinterSpecificationManager printerSpecificationManager;
  private static final String SPECIFICATION_RESOURCE =
          "epson-9pin-printer-specifications.properties";

  public Epson9PinPrinterDriver (final OutputStream out, final int charsPerInch, final int linesPerInch,
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

}
