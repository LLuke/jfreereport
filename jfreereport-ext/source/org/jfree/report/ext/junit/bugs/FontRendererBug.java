/**
 * Date: Dec 12, 2002
 * Time: 10:09:52 PM
 *
 * $Id: FontRendererBug.java,v 1.1 2003/07/08 14:21:47 taqua Exp $
 */
package org.jfree.report.ext.junit.bugs;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FontRendererBug extends JPanel
{
  public final static String myText = "A simple text with not tricks and traps";
  public static void main(final String[] args)
  {
    //G2OutputTarget ot = new G2OutputTarget(G2OutputTarget.createEmptyGraphics(), new PageFormat());

    printMe (true);
    printMe (false);

    final JFrame frame = new JFrame();
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(final WindowEvent e)
      {
        System.exit (0);
      }
    });
    frame.setContentPane(new FontRendererBug());
    frame.setSize(400, 400);
    frame.setVisible(true);
  }

  /**
   * Creates a new <code>JPanel</code> with a double buffer
   * and a flow layout.
   */
  public FontRendererBug()
  {
  }

  protected void paintComponent(final Graphics g)
  {
    final Graphics2D g2 = (Graphics2D) g;
    g2.setPaint(Color.white);
    g2.fill(getBounds());
    g2.setPaint(Color.black);

    final Font font = new Font("Serif", Font.PLAIN, 10);
    g2.setFont(font);

    drawText(g2, 20, false, false);
    drawText(g2, 40, false, true);
    drawText(g2, 60, true, false);
    drawText(g2, 80, true, true);
  }

  public void drawText (final Graphics2D g2, final int pos, final boolean fract, final boolean alias)
  {
    g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
        fract ? RenderingHints.VALUE_FRACTIONALMETRICS_ON :
        RenderingHints.VALUE_FRACTIONALMETRICS_OFF);

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        alias ? RenderingHints.VALUE_ANTIALIAS_ON:
        RenderingHints.VALUE_ANTIALIAS_OFF);

    final Rectangle2D size = getFont().getStringBounds(myText, 0, myText.length(),
        g2.getFontRenderContext());
    g2.drawString(myText, 0, pos);
    g2.draw(new Line2D.Double(size.getX(), pos + 10, size.getWidth(), pos + 10));
  }

  public static void printMe (final boolean alias)
  {
    System.out.println ("----------------------------------");

    final FontRenderContext frc_fract = new FontRenderContext(null, alias, true);
    final FontRenderContext frc_int = new FontRenderContext(null, alias, false);

    Font font = new Font("Serif", Font.PLAIN, 10);
    System.out.println("Text: 10: Fract: " + font.getStringBounds(myText, 0, myText.length(), frc_fract));
    System.out.println("Text: 10: Int  : " + font.getStringBounds(myText, 0, myText.length(), frc_int));
    System.out.println("Text: 10: GVi  : " + font.createGlyphVector(frc_int, myText).getLogicalBounds());
    System.out.println("Text: 10: GViv : " + font.createGlyphVector(frc_int, myText).getVisualBounds());
    System.out.println("Text: 10: GVf  : " + font.createGlyphVector(frc_fract, myText).getLogicalBounds());
    System.out.println("Text: 10: GVfv : " + font.createGlyphVector(frc_fract, myText).getVisualBounds());

    font = new Font("Serif", Font.PLAIN, 40);
    System.out.println("Text: 40: Fract: " + font.getStringBounds(myText, 0, myText.length(), frc_fract));
    System.out.println("Text: 40: Int  : " + font.getStringBounds(myText, 0, myText.length(), frc_int));
  }
}
