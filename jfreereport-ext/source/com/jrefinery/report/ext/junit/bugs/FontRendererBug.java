/**
 * Date: Dec 12, 2002
 * Time: 10:09:52 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.junit.bugs;

import com.jrefinery.report.targets.pageable.output.G2OutputTarget;
import com.jrefinery.report.util.Log;

import java.awt.print.PageFormat;
import java.awt.font.FontRenderContext;
import java.awt.Font;

public class FontRendererBug
{
  public static void main (String [] args)
  {
    //G2OutputTarget ot = new G2OutputTarget(G2OutputTarget.createEmptyGraphics(), new PageFormat());

    String myText = "A simple text with not tricks and traps";

    boolean alias = true;
    FontRenderContext frc_fract = new FontRenderContext(null, alias, true);
    FontRenderContext frc_int = new FontRenderContext(null, alias, false);

    Font font = new Font ("Serif", Font.PLAIN, 10);
    Log.debug ("Text: 10: Fract: " + font.getStringBounds(myText, 0, myText.length(), frc_fract));
    Log.debug ("Text: 10: Int  : " + font.getStringBounds(myText, 0, myText.length(), frc_int));
    Log.debug ("Text: 10: GVi  : " + font.createGlyphVector(frc_int, myText).getLogicalBounds());
    Log.debug ("Text: 10: GViv : " + font.createGlyphVector(frc_int, myText).getVisualBounds());
    Log.debug ("Text: 10: GVf  : " + font.createGlyphVector(frc_fract, myText).getLogicalBounds());
    Log.debug ("Text: 10: GVfv : " + font.createGlyphVector(frc_fract, myText).getVisualBounds());

    font = new Font ("Serif", Font.PLAIN, 90);
    Log.debug ("Text: 40: Fract: " + font.getStringBounds(myText, 0, myText.length(), frc_fract));
    Log.debug ("Text: 40: Int  : " + font.getStringBounds(myText, 0, myText.length(), frc_int));

  }
}
