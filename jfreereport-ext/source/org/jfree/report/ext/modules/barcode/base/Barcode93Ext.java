package org.jfree.report.ext.modules.barcode.base;

/**
 * Encodes a string into code93 extended specifications
 * <p/>
 * Symbols allowed: 0-9 A-Z a-z $ % * + - . / space non-printables      (ASCII128) Start
 * character: yes, in the symbols table (*) Stop character: yes, in the symbols table (*)
 * Check character: obligatory
 *
 * @author Mimil
 */
public class Barcode93Ext extends Barcode93
{

  /**
   * Allowed characters
   */
  protected static String[] EXTENDEDTABLE = {
    ch2 + "U",
    ch1 + "A", ch1 + "B", ch1 + "C", ch1 + "D", ch1 + "E", ch1 + "F", ch1 + "G", ch1 + "H", ch1 + "I", ch1 + "J", ch1 + "K", ch1 + "L", ch1 + "M", ch1 + "N", ch1 + "O", ch1 + "P", ch1 + "Q", ch1 + "R", ch1 + "S", ch1 + "T", ch1 + "U", ch1 + "V", ch1 + "W", ch1 + "X", ch1 + "Y", ch1 + "Z",
    ch2 + "A", ch2 + "B", ch2 + "C", ch2 + "D", ch2 + "E",
    " ",
    ch3 + "A", ch3 + "B", ch3 + "C", ch3 + "D", ch3 + "E", ch3 + "F", ch3 + "G", ch3 + "H", ch3 + "I", ch3 + "J", ch3 + "K", ch3 + "L", "-", ".", ch3 + "O",
    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
    ch3 + "Z",
    ch2 + "F", ch2 + "G", ch2 + "H", ch2 + "I", ch2 + "J", ch2 + "V",
    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
    ch2 + "K", ch2 + "L", ch2 + "M", ch2 + "N", ch2 + "O", ch2 + "W",
    ch4 + "A", ch4 + "B", ch4 + "C", ch4 + "D", ch4 + "E", ch4 + "F", ch4 + "G", ch4 + "H", ch4 + "I", ch4 + "J", ch4 + "K", ch4 + "L", ch4 + "M", ch4 + "N", ch4 + "O", ch4 + "P", ch4 + "Q", ch4 + "R", ch4 + "S", ch4 + "T", ch4 + "U", ch4 + "V", ch4 + "W", ch4 + "X", ch4 + "Y", ch4 + "Z",
    ch2 + "P", ch2 + "Q", ch2 + "R", ch2 + "S", ch2 + "T"
  };

  /**
   * Creates a new instance of Barcode93 Extended, can only be used by derivated class
   */
  public Barcode93Ext ()
  {
  }

  /**
   * Creates a new instance of Barcode93 Extended
   */
  public Barcode93Ext (final String code)
  {
    super(getNewCode(code));

    this.setCode(code + getCheckSumC() + getCheckSumK()); //set back to the readable code
  }

  /**
   * Transforms <code>code</code> into code93 readable characters
   *
   * @param code The old code
   * @return The new code
   */
  protected static String getNewCode (final String code)
  {
    final StringBuffer buf = new StringBuffer();
    int i = 0;

    try
    {
      for (i = 0; i < code.length(); i++)
      {
        buf.append(EXTENDEDTABLE[code.charAt(i)]);
      }
    }
    catch (Exception exp)
    {
      throw new IllegalArgumentException("The character '" + code.charAt(i) + "' is illegal in code 93 extended.");
    }

    return buf.toString();
  }
}
