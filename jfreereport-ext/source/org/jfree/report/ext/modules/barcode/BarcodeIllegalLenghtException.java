package org.jfree.report.ext.modules.barcode;

/**
 * Created by IntelliJ IDEA.
 * User: Administrateur
 * Date: 10 mai 2004
 * Time: 00:23:59
 * To change this template use File | Settings | File Templates.
 */
public class BarcodeIllegalLenghtException extends RuntimeException {
    public BarcodeIllegalLenghtException(String message) {
        super(message);
    }

    public BarcodeIllegalLenghtException(Barcode barcode, int mimimunSize, int maximumSize) {
        super(barcode.getClass().getName()+" lenght must be between "+mimimunSize+" and "+maximumSize+".");
    }

    public BarcodeIllegalLenghtException(String barcodeType, int mimimunSize, int maximumSize) {
        super(barcodeType+" lenght must be between "+mimimunSize+" and "+maximumSize+".");
    }
}
