package org.jfree.report.ext.modules.barcode.base;

/**
 * Encodes a string into JAN specifications
 * <p/>
 * Symbols allowed: 0-9 Start character: yes Stop character: yes Check character:
 * obligatory Code lenght limit: see <code>getNewCode</code> source code
 *
 * @author Mimil
 */
public class BarcodeJAN extends BarcodeEAN13
{

  /**
   * Creates a new instance of BarcodeJAN , can only be used by derivated class
   */
  protected BarcodeJAN ()
  {
  }

  /**
   * Creates a new instance of BarcodeJAN
   */
  public BarcodeJAN (final String code)
  {
    super(getNewCode(code));
  }

  /**
   * Transforms <code>code</code> into EAN13 readable characters
   *
   * @param code The old code
   * @return The new code
   */
  protected static String getNewCode (String code)
  {
    if (code.length() == 12)
    {
      if (code.substring(0, 2) != "49")
      {
        throw new IllegalArgumentException("The number system must be '49' (the two first digits) for an JAN Barcode.");
      }
    }
    else if (code.length() == 10)
    {
      code = "49" + code;
    }

    return code;
  }
}
