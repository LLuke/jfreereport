package org.jfree.report.ext.modules.barcode.base;

import org.jfree.report.ext.modules.barcode.BarcodeIllegalLenghtException;

/**
 * Encodes a string into UPCA specifications
 *
 * Symbols allowed: 0-9
 * Start character: yes
 * Stop character: yes
 * Check character: obligatory
 * Code lenght limit: see <code>getNewCode</code> source code
 *
 * @author Mimil
 */
public class BarcodeUPCA extends BarcodeEAN13 {

    /**
     * Creates a new instance of BarcodeUPCA
     */
    public BarcodeUPCA(String code) {
        super(formatUPCA(code));
    }

    /**
     * Creates a new instance of BarcodeUPCA, can only be used by derivated class
     */
    protected BarcodeUPCA() {}

    /**
     * Transforms <code>code</code> into EAN13 readable characters
     *
     * @param code The old code
     * @return  The new code
     */
    private static String formatUPCA (String code) {
        if(code.length() != 11) {
            throw new BarcodeIllegalLenghtException("BarcodeUPCA", 11, 11);
        }
        return ("0" + code);
    }
}