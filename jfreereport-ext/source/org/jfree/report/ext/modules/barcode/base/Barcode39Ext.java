package org.jfree.report.ext.modules.barcode.base;


/**
 * Encodes a string into code39 extended specifications
 * <p/>
 * Symbols allowed: 0-9 A-Z a-z $ % * + - . / space non-printables      (ASCII128) Start
 * character: yes, in the symbols table (*) Stop character: yes, in the symbols table (*)
 * Check character: available
 *
 * @author Mimil
 */
public class Barcode39Ext extends Barcode39
{

  /**
   * Characters allowed
   */
  protected static final String[] EXTENDEDTABLE = {
    "%U",
    "$A", "$B", "$C", "$D", "$E", "$F", "$G", "$H", "$I", "$J", "$K", "$L", "$M", "$N", "$O", "$P", "$Q", "$R", "$S", "$T", "$U", "$V", "$W", "$X", "$Y", "$Z",
    "%A", "%B", "%C", "%D", "%E",
    " ",
    "/A", "/B", "/C", "/D", "/E", "/F", "/G", "/H", "/I", "/J", "/K", "/L", "-", ".", "/O",
    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
    "/Z",
    "%F", "%G", "%H", "%I", "%J", "%V",
    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
    "%K", "%L", "%M", "%N", "%O", "%W",
    "+A", "+B", "+C", "+D", "+E", "+F", "+G", "+H", "+I", "+J", "+K", "+L", "+M", "+N", "+O", "+P", "+Q", "+R", "+S", "+T", "+U", "+V", "+W", "+X", "+Y", "+Z",
    "%P", "%Q", "%R", "%S", "%T"

  };

  /**
   * Creates a new instance of Barcode39 Extended
   */
  public Barcode39Ext (final String code)
  {
    super(getNewCode(code));

    this.setCode(code); //set back to the readable code
  }

  /**
   * Creates a new instance of Barcode39 Extended, can only be used by derivated class
   */
  protected Barcode39Ext ()
  {
  };

  /**
   * Transforms <code>code</code> into code39 readable characters
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
      throw new IllegalArgumentException("The character '" + code.charAt(i) + "' is illegal in code 39 extended.");
    }

    return buf.toString();
  }

}
