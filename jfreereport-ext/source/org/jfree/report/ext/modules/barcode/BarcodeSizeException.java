package org.jfree.report.ext.modules.barcode;


import java.awt.Insets;
import java.awt.geom.Rectangle2D;

import org.jfree.report.style.FontDefinition;

/**
 * Created by IntelliJ IDEA. User: Administrateur Date: 30 mars 2004 Time: 21:48:09 To
 * change this template use File | Settings | File Templates.
 */
public class BarcodeSizeException extends IllegalStateException
        {
  public BarcodeSizeException (final String message, final Rectangle2D bounds,
                               final Insets margins, final Insets quietzones,
                               final FontDefinition font)
  {
    super(message
            + "\n\tBounds were: " + bounds.toString()
            + "\n\tMargins were: " + margins.toString()
            + "\n\tQuietZones were: " + quietzones.toString()
            + "\n\tFont was: " + font.toString());
  }

  public BarcodeSizeException (final String message)
  {
    super(message);
  }
}
