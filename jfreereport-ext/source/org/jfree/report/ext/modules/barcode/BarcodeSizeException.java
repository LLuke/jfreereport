package org.jfree.report.ext.modules.barcode;


import org.jfree.report.style.FontDefinition;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Administrateur
 * Date: 30 mars 2004
 * Time: 21:48:09
 * To change this template use File | Settings | File Templates.
 */
public class BarcodeSizeException extends Exception{
    public BarcodeSizeException(String message, Rectangle2D bounds, Insets margins, Insets quietzones, FontDefinition font) {
        super(message
                + "\n\tBounds were: " + bounds.toString()
                + "\n\tMargins were: " + margins.toString()
                + "\n\tQuietZones were: " + quietzones.toString()
                + "\n\tFont was: " + font.toString());
    }

    public BarcodeSizeException(String message) {
        super(message);
    }
}
