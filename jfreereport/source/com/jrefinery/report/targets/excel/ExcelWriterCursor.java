/**
 * Date: Jan 14, 2003
 * Time: 5:16:03 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.excel;

public class ExcelWriterCursor
{
  /** The y-coordinate. */
  private float y;

  public ExcelWriterCursor()
  {
  }

  /**
   * Adds the specified amount to the y-coordinate.
   * @param amount The amount that the cursor should advance down the page.
   */
  public void advance (float amount)
  {
    if (amount < 0)
    {
      throw new IllegalArgumentException ("Cannot advance negative");
    }
    y += amount;
  }

  /**
   * Moves the cursor to the given y-coordinate. All space beween the current position
   * before the move and the new position is considered filled and won't get filled by
   * the generator.
   *
   * @param amount The amount that the cursor should advance down the page.
   */
  public void advanceTo (float amount)
  {
    if (amount < y)
    {
      throw new IllegalArgumentException ("Cannot advance negative");
    }
    y = amount;
  }

  /**
   * @return the current y-position of this cursor.
   */
  public float getY ()
  {
    return y;
  }

}
