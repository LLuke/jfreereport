package org.jfree.report.ext.modules.barcode.base;

/**
 * Encodes a string into ISBN specifications
 *
 * Symbols allowed: 0-9
 * Start character: yes
 * Stop character: yes
 * Check character: obligatory
 * Code lenght limit: see <code>getNewCode</code> source code
 *
 * Also know as:
 * - bookland
 *
 * @author Mimil
 */
public class BarcodeISBN extends BarcodeEAN13 {

    /**
     * Creates a new instance of BarcodeISBN , can only be used by derivated class
     */
    protected BarcodeISBN() {}

    /**
     * Creates a new instance of BarcodeISBN
     */
    public BarcodeISBN(String code) {
        super(getNewCode(code));
    }

    /**
     * Transforms <code>code</code> into EAN13 readable characters
     *
     * @param code The old code
     * @return  The new code
     */
    protected static String getNewCode(String code) {
        if(code != null) {
            if(code.length() == 9) {
                code = "978" + code;
            } else if (code.length() == 12 ) {
                if(code.substring(0,3) != "978") {
                    throw new IllegalArgumentException("The number system must be '978' (the three first digits) for an ISBN Barcode.");
                }
            } else if(code.length() == 13 && !(code.charAt(3) >= 48 && code.charAt(3) <= 57)) {    //4th character is not a digit
                code = code.substring(0, code.length()-1);  //remove the ISBN check digit
                code = "978" + code.replaceAll(code.charAt(3) + "", "");
            } else {
            throw new IllegalArgumentException("The barcode does not seem to be a valid ISBN Barcode");
            }
        }

        return code;
    }
}
