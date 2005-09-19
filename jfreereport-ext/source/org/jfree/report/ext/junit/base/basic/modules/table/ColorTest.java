package org.jfree.report.ext.junit.base.basic.modules.table;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Creation-Date: 02.09.2005, 13:50:30
 *
 * @author: Thomas Morgner
 */
public class ColorTest
{
  public static void main(String[] args)
  {
    final BufferedImage img = new BufferedImage(1, 1,
            BufferedImage.TYPE_INT_ARGB);
    final Graphics g = img.getGraphics();
    img.setRGB(0,0, 0);

    Color base = new Color (255, 0, 0, 128);
    Color paint = new Color (255, 0, 0, 128);

    g.setColor(base);
    g.drawRect(0, 0, 1, 1);
    g.setColor(paint);
    g.drawRect(0, 0, 1, 1);

    Color result = new Color(img.getRGB(0, 0), true);
    System.out.print("Color: " + result.getRed() + ", " + result.getGreen() +
            ", " + result.getBlue() + ", " + result.getAlpha());
  }
}
