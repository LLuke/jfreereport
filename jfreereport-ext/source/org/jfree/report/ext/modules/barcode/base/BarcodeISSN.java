package org.jfree.report.ext.modules.barcode.base;

/**
 * Encodes a string into ISSN specifications
 * <p/>
 * Symbols allowed: 0-9 Start character: yes Stop character: yes Check character:
 * obligatory Code lenght limit: see <code>getNewCode</code> source code
 *
 * @author Mimil
 */
public class BarcodeISSN extends BarcodeEAN13
{
  //todo: not fully tested

  /**
   * Creates a new instance of BarcodeISSN , can only be used by derivated class
   */
  protected BarcodeISSN ()
  {
  }

  /**
   * Creates a new instance of BarcodeISSN
   */
  public BarcodeISSN (final String code)
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
    //0123-456X00

    if (code != null)
    {
      if (code.length() == 9 && (code.charAt(4) >= 48 && code.charAt(4) <= 57))
      {  //5th character is a digit and price set
        code = "977" + code;
      }
      else if (code.length() == 9)
      { //no price set, hyphen and trailing check digit
        code = code.substring(0, code.length() - 1);  //remove the ISSN check digit
        code = "977" + code.replaceAll(code.charAt(4) + "", "") + "00";
      }
      else if (code.length() == 7)
      { //price set to '00'
        code = "977" + code + "00";
      }
      else if (code.length() == 12 && (code.charAt(4) >= 48 && code.charAt(3) <= 57))
      {//system number with price set
        if (code.substring(0, 3) != "977")
        {
          throw new IllegalArgumentException("The number system must be '977' (the three first digits) for an ISSN Barcode.");
        }
      }
      else if (code.length() == 10)
      {//system number with no price set
        if (code.substring(0, 3) != "977")
        {
          throw new IllegalArgumentException("The number system must be '977' (the three first digits) for an ISSN Barcode.");
        }
        code = code + "00";
      }
      else if (code.length() == 12)
      {    //system number, trailing check digit and 4th character is not a digit
        code = code.substring(0, code.length() - 1);  //remove the ISBN check digit
        code = "977" + code.replaceAll(code.charAt(4) + "", "");
      }
      else if (code.length() == 11)
      {    //check digit, price set and 4th character is not a digit
        code = code.substring(0, code.length() - 3) + code.substring(code.length() - 2, code.length());  //remove the ISSN check digit
        code = "977" + code.replaceAll(code.charAt(4) + "", "");
      }
      else
      {
        throw new IllegalArgumentException("The barcode does not seem to be a valid ISSN Barcode");
      }
    }

    return code;
  }
}
