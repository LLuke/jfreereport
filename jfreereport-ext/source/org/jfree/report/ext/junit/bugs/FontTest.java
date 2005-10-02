package org.jfree.report.ext.junit.bugs;

import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.awt.font.FontRenderContext;

/**
 * Creation-Date: 01.10.2005, 19:28:55
 *
 * @author Thomas Morgner
 */
public class FontTest
{
  protected static final String TEST_STRING = "asdfasdfasdfisdfifuasdffif";

  public FontTest()
  {
  }

  private static double computeWidth (final Font font,
                               FontRenderContext frc, char[] string)
  {
    Rectangle2D rect = font.getStringBounds(string, 0, string.length, frc);
    return rect.getWidth();
  }

  private static double computeWidth (double[] widths, char[] string)
  {
    double width = 0;
    for (int i = 0; i < string.length; i++)
    {
      char c = string[i];
      width += widths[c - 'a'];
    }
    return width;
  }

  public static void main (String[] args)
  {
    Font font = new Font ("Coronet", Font.ITALIC, 80);
    FontRenderContext frc = new FontRenderContext(null, true, true);
    char[] chars = new char[('z' - 'a')];
    double[] charW = new double[chars.length];

    for (int i = 0; i < chars.length; i++)
    {
      chars[i] = (char) ('a' + i);
      Rectangle2D rect = font.getStringBounds("" + chars[i], frc);
      charW[i] = rect.getWidth();
    }

    System.out.println(TEST_STRING);
    final char[] string = TEST_STRING.toCharArray();
    System.out.println(computeWidth(font, frc, string));
    System.out.println(computeWidth(charW, string));

  }
}
