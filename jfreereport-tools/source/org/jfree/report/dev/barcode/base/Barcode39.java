/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ------------------------------
 * Barcode39.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 * Contributor(s):   Cedric Pronzato;
 *
 * $Id: $
 *
 * Changes (from YYYY-MM-DD)
 * -------------------------
 *
 */

package org.jfree.report.dev.barcode.base;

import java.awt.geom.Rectangle2D;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.jfree.report.dev.barcode.Barcode1D;

public class Barcode39 extends Barcode1D
{

  /**
   * Table holding symbols to be drawn.
   */
  private List codeTable;
  /**
   * Characters allowed.
   */
  protected static final String CHARTABLE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%*";
  /**
   * Checksum character.
   */
  private char checksum;
  /**
   * If the start and stop charcters have to be shown.
   */
  private boolean showStartStop = false;
  /**
   * If the checksum have to be computed.
   */
  private boolean appendChecksum = false;
  /**
   * If the checksum character have to be shown.
   */
  private boolean showChecksum = false;

  /**
   * Symbols allowed
   */
  protected static final byte TABLE[][] = {
    {0, 0, 0, 1, 1, 0, 1, 0, 0},
    {1, 0, 0, 1, 0, 0, 0, 0, 1},
    {0, 0, 1, 1, 0, 0, 0, 0, 1},
    {1, 0, 1, 1, 0, 0, 0, 0, 0},
    {0, 0, 0, 1, 1, 0, 0, 0, 1},
    {1, 0, 0, 1, 1, 0, 0, 0, 0},
    {0, 0, 1, 1, 1, 0, 0, 0, 0},
    {0, 0, 0, 1, 0, 0, 1, 0, 1},
    {1, 0, 0, 1, 0, 0, 1, 0, 0},
    {0, 0, 1, 1, 0, 0, 1, 0, 0},
    {1, 0, 0, 0, 0, 1, 0, 0, 1},
    {0, 0, 1, 0, 0, 1, 0, 0, 1},
    {1, 0, 1, 0, 0, 1, 0, 0, 0},
    {0, 0, 0, 0, 1, 1, 0, 0, 1},
    {1, 0, 0, 0, 1, 1, 0, 0, 0},
    {0, 0, 1, 0, 1, 1, 0, 0, 0},
    {0, 0, 0, 0, 0, 1, 1, 0, 1},
    {1, 0, 0, 0, 0, 1, 1, 0, 0},
    {0, 0, 1, 0, 0, 1, 1, 0, 0},
    {0, 0, 0, 0, 1, 1, 1, 0, 0},
    {1, 0, 0, 0, 0, 0, 0, 1, 1},
    {0, 0, 1, 0, 0, 0, 0, 1, 1},
    {1, 0, 1, 0, 0, 0, 0, 1, 0},
    {0, 0, 0, 0, 1, 0, 0, 1, 1},
    {1, 0, 0, 0, 1, 0, 0, 1, 0},
    {0, 0, 1, 0, 1, 0, 0, 1, 0},
    {0, 0, 0, 0, 0, 0, 1, 1, 1},
    {1, 0, 0, 0, 0, 0, 1, 1, 0},
    {0, 0, 1, 0, 0, 0, 1, 1, 0},
    {0, 0, 0, 0, 1, 0, 1, 1, 0},
    {1, 1, 0, 0, 0, 0, 0, 0, 1},
    {0, 1, 1, 0, 0, 0, 0, 0, 1},
    {1, 1, 1, 0, 0, 0, 0, 0, 0},
    {0, 1, 0, 0, 1, 0, 0, 0, 1},
    {1, 1, 0, 0, 1, 0, 0, 0, 0},
    {0, 1, 1, 0, 1, 0, 0, 0, 0},
    {0, 1, 0, 0, 0, 0, 1, 0, 1},
    {1, 1, 0, 0, 0, 0, 1, 0, 0},
    {0, 1, 1, 0, 0, 0, 1, 0, 0},
    {0, 1, 0, 1, 0, 1, 0, 0, 0},
    {0, 1, 0, 1, 0, 0, 0, 1, 0},
    {0, 1, 0, 0, 0, 1, 0, 1, 0},
    {0, 0, 0, 1, 0, 1, 0, 1, 0},
    {0, 1, 0, 0, 1, 0, 1, 0, 0}
  };


  public Barcode39 (final String code)
  {
    super(code);
    codeTable = new ArrayList();
  }

  /**
   * Tells if the input code passed as argument is a valid code for the code 39
   * specification.
   *
   * @param code The input code to check.
   * @return Boolean
   */
  public static boolean isValidInput (final String code)
  {
    //NB: there is no length checks in code 39.

    for (int i = 0; i < code.length(); i++)
    {
      int character = CHARTABLE.indexOf(code.charAt(i));
      if (character == -1)
      {
        return false;
      }
    }
    return true;
  }

  /**
   * Tells if the input code passed as argument starts and stops with the <code>*</code>
   * character.
   *
   * @param code The input code to check.
   * @return Boolean.
   */
  public static boolean isStartStopPresent (final String code)
  {
    if (code.charAt(0) == '*' || code.charAt(code.length()) == '*')
    {
      return true;
    }
    else
    {
      return false;
    }
  }


  /**
   * Returns the code that sould be displayed, it can differ from the base code passed as
   * argument to the constructor.
   *
   * @return The code to display.
   */
  public String getDisplayedCode ()
  {
    final String oldCode = super.getDisplayedCode();

    if (isShowCode())
    {

    }
    return null;

  }

  /**
   * Computes the bars bounds.
   *
   * @return The bars bounds initialized on <code>x=0</code> and <code>y=0</code>.
   */
  //todo: use autoCompute
  public Rectangle2D getBarBounds ()
  {
    return null;
  }

  /**
   * Draws the barcode.
   *
   * @param g2   the graphics device.
   * @param area the area inside which the object should be drawn.
   * @see org.jfree.ui.Drawable
   */
  //todo: use autoCompute
  public void draw (Graphics2D g2, Rectangle2D area)
  {
  }

  /**
   * Returns the preferred size of the drawable. If the drawable is aspect ratio aware,
   * these bounds should be used to compute the preferred aspect ratio for this drawable.
   *
   * @return the preferred size.
   */
  public Rectangle2D getPreferredSize ()
  {
    return null;
  }

  /**
   * Returns true, if this drawable will preserve an aspect ratio during the drawing.
   *
   * @return true, if an aspect ratio is preserved, false otherwise.
   */
  public boolean isPreserveAspectRatio ()
  {
    return false;
  }


  public List getCodeTable ()
  {
    return codeTable;
  }

  public void setCodeTable (List codeTable)
  {
    this.codeTable = codeTable;
  }

  public char getChecksum ()
  {
    return checksum;
  }

  public void setChecksum (char checksum)
  {
    this.checksum = checksum;
  }

  /**
   * Tells if the start and stop character have to be displayed.
   *
   * @return Boolean.
   */
  public boolean isShowStartStop ()
  {
    return showStartStop;
  }

  public void setShowStartStop (boolean showStartStop)
  {
    this.showStartStop = showStartStop;
  }

  public boolean isAppendChecksum ()
  {
    return appendChecksum;
  }

  public void setAppendChecksum (boolean appendChecksum)
  {
    this.appendChecksum = appendChecksum;
  }

  public boolean isShowChecksum ()
  {
    return showChecksum;
  }

  public void setShowChecksum (boolean showChecksum)
  {
    this.showChecksum = showChecksum;
  }
}
