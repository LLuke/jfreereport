package org.jfree.report.ext.modules.barcode;

/**
 * Created by IntelliJ IDEA. User: Administrateur Date: 10 mai 2004 Time: 00:23:59 To
 * change this template use File | Settings | File Templates.
 */
public class BarcodeIllegalLengthException extends IllegalArgumentException
{
  public BarcodeIllegalLengthException (final String message)
  {
    super(message);
  }

  public BarcodeIllegalLengthException (final Barcode barcode, final int mimimunSize,
                                        final int maximumSize)
  {
    super(barcode.getClass().getName() + " lenght must be between " + mimimunSize + " and " + maximumSize + ".");
  }

  public BarcodeIllegalLengthException (final String barcodeType, final int mimimunSize,
                                        final int maximumSize)
  {
    super(barcodeType + " lenght must be between " + mimimunSize + " and " + maximumSize + ".");
  }
}
