package org.jfree.report.ext.modules.barcode.base;

/**
 * Encodes a string into EAN13 specifications
 * <p/>
 * Symbols allowed: 0-9 Start character: yes Stop character: yes Check character:
 * obligatory Code lenght limit: see <code>getNewCode</code> source code
 *
 * @author Mimil
 */
//todo: set the code according to its size
public class BarcodeISMN extends BarcodeEAN13
{

  /**
   * Creates a new instance of BarcodeISMN , can only be used by derivated class
   */
  protected BarcodeISMN ()
  {
  }

  /**
   * Creates a new instance of BarcodeISMN
   */
  public BarcodeISMN (final String code)
  {
    super(getNewCode(code));
    //this.setCode(code);
  }

  /**
   * Transforms <code>code</code> into EAN13 readable characters
   *
   * @param code The old code
   * @return The new code
   */
  protected static String getNewCode (String code)
  {
    //M-345-12345-8
    //todo not finished and not fully tested
    if (code.length() == 8)
    {
      code = "9790" + code;
    }
    else if (code.length() == 9 && code.charAt(0) == '0')
    {
      code = "979" + code;
    }
    else if (code.length() == 13 && code.charAt(0) == 'M')
    {  //starts with a M, hyphens and original check digit
      code = code.substring(1, code.length() - 1);  //remove 'M' and the check digit
      code = "9790" + code.replaceAll(code.charAt(0) + "", "");
    }
    else
    {
      throw new IllegalArgumentException("The barcode does not seem to be a valid ISMN Barcode");
    }

    return code;
  }
}
