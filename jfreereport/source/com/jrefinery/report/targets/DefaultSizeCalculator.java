/**
 * Date: Jan 29, 2003
 * Time: 3:47:42 AM
 *
 * $Id$
 */
package com.jrefinery.report.targets;

import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.util.ReportConfiguration;
import com.jrefinery.report.util.Log;

import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.Font;

public class DefaultSizeCalculator implements SizeCalculator
{
  public static class BuggyFontRendererDetector
  {
    private boolean isBuggyVersion;
    private boolean isAliased;

    public BuggyFontRendererDetector ()
    {
      isAliased = ReportConfiguration.getGlobalConfig().isG2TargetUseAliasing();

      // Another funny thing for the docs: On JDK 1.4 the font renderer changed.
      // in previous versions, the font renderer was sensitive to fractional metrics,
      // so that fonts were always rendered without FractionalMetrics enabled.
      // Since 1.4, fonts are always rendered with FractionalMetrics disabled.

      // On a 1.4 version, the aliasing has no influence on non-fractional metrics
      // aliasing has no influence on any version if fractional metrics are enabled.
      FontRenderContext frc_alias = new FontRenderContext(null, true, false);
      FontRenderContext frc_noAlias = new FontRenderContext(null, false, false);
      Font font = new Font ("Serif", Font.PLAIN, 10);
      String myText = "A simple text with some characters to calculate the length.";

      double wAlias =  font.getStringBounds(myText, 0, myText.length(), frc_alias).getWidth();
      double wNoAlias =  font.getStringBounds(myText, 0, myText.length(), frc_noAlias).getWidth();
      isBuggyVersion = (wAlias != wNoAlias);
      boolean buggyOverride = ReportConfiguration.getGlobalConfig().isG2BuggyFRC();
      Log.debug ("This is a buggy version of the font-renderer context: " + isBuggyVersion);
      Log.debug ("The buggy-value is defined in the configuration     : " + buggyOverride);
      if (isAliased())
      {
        Log.debug ("The G2OutputTarget uses Antialiasing. \n" +
                   "The FontRendererBugs should not be visible in TextAntiAliasing-Mode." +
                   "If there are problems with the string-placement, please report your " +
                   "Operating System version and your JDK Version to www.object-refinery.com/jfreereport.");
      }
      else
      {
        if (isBuggyVersion)
        {
          Log.debug ("The G2OutputTarget does not use Antialiasing. \n" +
                     "If your FontRenderer is buggy (text is not displayed correctly by default). \n" +
                     "The system was able to detect this and will try to correct the bug. \n" +
                     "If your strings are not displayed correctly, report your OperationSystem version and your " +
                     "JDK Version to www.object-refinery.com/jfreereport");
        }
        else
        {
          Log.debug ("The G2OutputTarget does not use Antialiasing. \n" +
                     "If your FontRenderer seems to be ok. \n" +
                     "If your strings are not displayed correctly, try to enable the configuration key " +
                     "\"com.jrefinery.report.targets.G2OutputTarget.isBuggyFRC=true\"" +
                     "in the file 'jfreereport.properties' or set this property as System-property. " +
                     "If the bug remains alive, please report your Operating System version and your " +
                     "JDK Version to www.object-refinery.com/jfreereport.");
        }
      }

      if (buggyOverride == true)
      {
        isBuggyVersion = true;
      }
    }

    public FontRenderContext createFontRenderContext ()
    {
      if (isAliased())
      {
        return new FontRenderContext(null, isAliased(), true);
      }
      // buggy is only important on non-aliased environments ...
      // dont use fractional metrics on buggy versions

      // use int_metrics wenn buggy ...
      return new FontRenderContext(null, isAliased(), isBuggyVersion() == false);
    }

    public boolean isAliased ()
    {
      return isAliased;
    }

    public boolean isBuggyVersion ()
    {
      return isBuggyVersion;
    }
  }

  private static BuggyFontRendererDetector frcDetector;

  public static BuggyFontRendererDetector getFrcDetector ()
  {
    if (frcDetector == null)
    {
      frcDetector = new BuggyFontRendererDetector();
    }
    return frcDetector;
  }


  /**
   * The font.
   */
  private FontDefinition font;

  /**
   * Creates a new size calculator.
   *
   * @param font  the font.
   */
  public DefaultSizeCalculator(FontDefinition font)
  {
    this.font = font;
  }

  /**
   * Returns the height of the current font. The font height specifies the distance between
   * 2 base lines.
   *
   * @return the font height.
   */
  public float getLineHeight()
  {
    return font.getFont().getSize2D();
  }

  /**
   * Calculates the width of the specified String in the current Graphics context.
   *
   * @param text the text to be weighted.
   * @param lineStartPos the start position of the substring to be weighted.
   * @param endPos the position of the last characterto be included in the weightening process.
   *
   * @return the width of the given string in 1/72" dpi.
   */
  public float getStringWidth(String text, int lineStartPos, int endPos)
  {
    if (lineStartPos < 0)
    {
      throw new IllegalArgumentException();
    }
    if (lineStartPos > endPos)
    {
      throw new IllegalArgumentException("LineStart on: " + lineStartPos + " End on " + endPos);
    }

    if (lineStartPos == endPos)
      return 0;

    FontRenderContext frc = getFrcDetector().createFontRenderContext();
    Rectangle2D textBounds2 = font.getFont().getStringBounds(text, lineStartPos, endPos, frc);
    float x2 = (float) textBounds2.getWidth();
    return x2;
  }

  /**
   * Converts this object to a string.
   *
   * @return a string.
   */
  public String toString ()
  {
    return "DefaultSizeCalculator={font=" + font + "}";
  }

}
