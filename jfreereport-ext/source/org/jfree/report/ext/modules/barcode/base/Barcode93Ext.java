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
  protected static final String[] EXTENDEDTABLE = {
    SPECIAL_CHAR2 + "U",
    SPECIAL_CHAR1 + "A", SPECIAL_CHAR1 + "B", SPECIAL_CHAR1 + "C", SPECIAL_CHAR1 + "D",
    SPECIAL_CHAR1 + "E", SPECIAL_CHAR1 + "F", SPECIAL_CHAR1 + "G", SPECIAL_CHAR1 + "H",
    SPECIAL_CHAR1 + "I", SPECIAL_CHAR1 + "J", SPECIAL_CHAR1 + "K", SPECIAL_CHAR1 + "L",
    SPECIAL_CHAR1 + "M", SPECIAL_CHAR1 + "N", SPECIAL_CHAR1 + "O", SPECIAL_CHAR1 + "P",
    SPECIAL_CHAR1 + "Q", SPECIAL_CHAR1 + "R", SPECIAL_CHAR1 + "S", SPECIAL_CHAR1 + "T",
    SPECIAL_CHAR1 + "U", SPECIAL_CHAR1 + "V", SPECIAL_CHAR1 + "W", SPECIAL_CHAR1 + "X",
    SPECIAL_CHAR1 + "Y", SPECIAL_CHAR1 + "Z",
    SPECIAL_CHAR2 + "A", SPECIAL_CHAR2 + "B", SPECIAL_CHAR2 + "C", SPECIAL_CHAR2 + "D",
    SPECIAL_CHAR2 + "E",
    " ",
    SPECIAL_CHAR3 + "A", SPECIAL_CHAR3 + "B", SPECIAL_CHAR3 + "C", SPECIAL_CHAR3 + "D",
    SPECIAL_CHAR3 + "E", SPECIAL_CHAR3 + "F", SPECIAL_CHAR3 + "G", SPECIAL_CHAR3 + "H",
    SPECIAL_CHAR3 + "I", SPECIAL_CHAR3 + "J", SPECIAL_CHAR3 + "K", SPECIAL_CHAR3 + "L",
    "-", ".", SPECIAL_CHAR3 + "O",
    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
    SPECIAL_CHAR3 + "Z",
    SPECIAL_CHAR2 + "F", SPECIAL_CHAR2 + "G", SPECIAL_CHAR2 + "H", SPECIAL_CHAR2 + "I",
    SPECIAL_CHAR2 + "J", SPECIAL_CHAR2 + "V",
    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
    "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
    SPECIAL_CHAR2 + "K", SPECIAL_CHAR2 + "L", SPECIAL_CHAR2 + "M", SPECIAL_CHAR2 + "N",
    SPECIAL_CHAR2 + "O", SPECIAL_CHAR2 + "W",
    SPECIAL_CHAR4 + "A", SPECIAL_CHAR4 + "B", SPECIAL_CHAR4 + "C", SPECIAL_CHAR4 + "D",
    SPECIAL_CHAR4 + "E", SPECIAL_CHAR4 + "F", SPECIAL_CHAR4 + "G", SPECIAL_CHAR4 + "H",
    SPECIAL_CHAR4 + "I", SPECIAL_CHAR4 + "J", SPECIAL_CHAR4 + "K", SPECIAL_CHAR4 + "L",
    SPECIAL_CHAR4 + "M", SPECIAL_CHAR4 + "N", SPECIAL_CHAR4 + "O", SPECIAL_CHAR4 + "P",
    SPECIAL_CHAR4 + "Q", SPECIAL_CHAR4 + "R", SPECIAL_CHAR4 + "S", SPECIAL_CHAR4 + "T",
    SPECIAL_CHAR4 + "U", SPECIAL_CHAR4 + "V", SPECIAL_CHAR4 + "W", SPECIAL_CHAR4 + "X",
    SPECIAL_CHAR4 + "Y", SPECIAL_CHAR4 + "Z",
    SPECIAL_CHAR2 + "P", SPECIAL_CHAR2 + "Q", SPECIAL_CHAR2 + "R", SPECIAL_CHAR2 + "S",
    SPECIAL_CHAR2 + "T"
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
