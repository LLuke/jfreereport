/**
 * Date: Nov 20, 2002
 * Time: 5:04:04 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable;

public interface SizeCalculator
{
  /**
   * Calculates the width of the specified String in the current Graphics context.
   *
   * @param text the text to be weighted.
   * @param lineStartPos the start position of the substring to be weighted.
   * @param endPos the position of the last characterto be included in the weightening process.
   *
   * @return the width of the given string in 1/72" dpi.
   */
  public float getStringWidth(String text, int lineStartPos, int endPos);

  public float getLineHeight();
}
