/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * --------------------------
 * DefaultSizeCalculator.java
 * --------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DefaultSizeCalculator.java,v 1.6 2003/02/27 10:35:39 mungady Exp $
 *
 * Changes
 * -------
 * 29-Jan-2003 : Initial version
 * 05-Feb-2003 : Moved from package com.jrefinery.report.targets.pageable
 *
 */
package com.jrefinery.report.targets.base.layout;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.ReportConfiguration;

/**
 * An AWT-Based default implementation of an SizeCalculator. This implementation
 * tries to detect the currently used FontRendererContext; some JDKs are unable
 * to return reasonable sizes for the given text.
 *
 * @see com.jrefinery.report.targets.base.layout.DefaultSizeCalculator
 * 
 * @author Thomas Morgner
 */
public class DefaultSizeCalculator implements SizeCalculator
{
  /**
   * A helper class that is able to detect whether the implementation is considered
   * buggy. A non-buggy implementation should show no differences between aliased-versions
   * of Graphics2D and non-aliased versions.
   * <p>
   * On JDK 1.4 the font renderer changed.
   * In previous versions, the font renderer was sensitive to fractional metrics,
   * so that fonts were always rendered without FractionalMetrics enabled.
   * Since 1.4, fonts are always rendered with FractionalMetrics disabled.
   * <p>
   * Obviously this is annoying if you try to write a layouter for all JDKs :(
   */
  public static class BuggyFontRendererDetector
  {
    /** a flag that indicates whether the FontRenderContext implementation is buggy. */
    private boolean isBuggyVersion;

    /** a flag that checks whether aliasing is used to draw the contents on Graphics objects. */
    private boolean isAliased;

    /** Cache the created FontRenderContext. FRC is read only. */
    private FontRenderContext fontRenderContext;

    /**
     * creates a new BuggyFontRendererDetector.
     */
    public BuggyFontRendererDetector ()
    {
      isAliased = ReportConfiguration.getGlobalConfig().isG2TargetUseAliasing();

      // Another funny thing for the docs: On JDK 1.4 the font renderer changed.
      // in previous versions, the font renderer was sensitive to fractional metrics,
      // so that fonts were always rendered without FractionalMetrics enabled.
      // Since 1.4, fonts are always rendered with FractionalMetrics disabled.

      // On a 1.4 version, the aliasing has no influence on non-fractional metrics
      // aliasing has no influence on any version if fractional metrics are enabled.
      FontRenderContext frcAlias = new FontRenderContext(null, true, false);
      FontRenderContext frcNoAlias = new FontRenderContext(null, false, false);
      Font font = new Font ("Serif", Font.PLAIN, 10);
      String myText = "A simple text with some characters to calculate the length.";

      double wAlias =  font.getStringBounds(myText, 0, myText.length(), frcAlias).getWidth();
      double wNoAlias =  font.getStringBounds(myText, 0, myText.length(), frcNoAlias).getWidth();
      isBuggyVersion = (wAlias != wNoAlias);
      boolean buggyOverride = ReportConfiguration.getGlobalConfig().isG2BuggyFRC();
      Log.debug ("This is a buggy version of the font-renderer context: " + isBuggyVersion);
      Log.debug ("The buggy-value is defined in the configuration     : " + buggyOverride);
      if (isAliased())
      {
        Log.debug ("The G2OutputTarget uses Antialiasing. \n" 
                   + "The FontRendererBugs should not be visible in TextAntiAliasing-Mode.\n"
                   + "If there are problems with the string-placement, please report your \n"
                   + "Operating System version and your JDK Version to "
                   + "www.object-refinery.com/jfreereport.\n");
      }
      else
      {
        if (isBuggyVersion)
        {
          Log.debug ("The G2OutputTarget does not use Antialiasing. \n" 
                     + "If your FontRenderer is buggy (text is not displayed correctly by "
                     + "default).\n" 
                     + "The system was able to detect this and will try to correct the bug. \n" 
                     + "If your strings are not displayed correctly, report your Operating System "
                     + "version and your \n" 
                     + "JDK Version to www.object-refinery.com/jfreereport\n");
        }
        else
        {
          Log.debug ("The G2OutputTarget does not use Antialiasing. \n" 
                     + "If your FontRenderer seems to be ok. \n" 
                     + "If your strings are not displayed correctly, try to enable the "
                     + "configuration key \n" 
                     + "\"com.jrefinery.report.targets.G2OutputTarget.isBuggyFRC=true\"\n" 
                     + "in the file 'jfreereport.properties' or set this property as "
                     + "System-property. \n" 
                     + "If the bug remains alive, please report your Operating System version "
                     + "and your \n JDK Version to www.object-refinery.com/jfreereport.\n");
        }
      }

      if (buggyOverride == true)
      {
        isBuggyVersion = true;
      }
    }

    /**
     * creates a new FontRenderContext suitable to calculate a string size, independend
     * from the AWT-bug.
     *
     * @return a font render context that is valid and not affected by the bugs.
     */
    public FontRenderContext createFontRenderContext ()
    {
      if (fontRenderContext == null)
      {
        if (isAliased())
        {
          fontRenderContext = new FontRenderContext(null, isAliased(), true);
        }
        else
        {
          // buggy is only important on non-aliased environments ...
          // dont use fractional metrics on buggy versions

          // use int_metrics wenn buggy ...
          fontRenderContext = new FontRenderContext(null, isAliased(), isBuggyVersion() == false);
        }
      }
      return fontRenderContext;
    }

    /**
     * Gets the defined aliasing state for the FontRenderContext and the target Graphics2D.
     *
     * @return the aliasing state.
     */
    public boolean isAliased ()
    {
      return isAliased;
    }

    /**
     * Gets the buggy state of the AWT implementation.
     *
     * @return true, if the AWT implementation is buggy and not able to perform accurate
     * font rendering.
     */
    public boolean isBuggyVersion ()
    {
      return isBuggyVersion;
    }
  }

  /** the FontRenderContext bug detector instance. */
  private static BuggyFontRendererDetector frcDetector;

  /**
   * Returns a singleon instance of the FontRenderContext bug detector.
   *
   * @return the FontRenderContext-detector
   */
  public static BuggyFontRendererDetector getFrcDetector ()
  {
    if (frcDetector == null)
    {
      frcDetector = new BuggyFontRendererDetector();
    }
    return frcDetector;
  }

  /** The font. */
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
    {
      return 0;
    }
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
