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
 * $Id: Barcode39.java,v 1.2 2005/05/18 20:00:25 mimil Exp $
 *
 * Changes (from YYYY-MM-DD)
 * -------------------------
 *
 */

package org.jfree.report.dev.barcode.base;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
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
  private boolean appendedChecksum = false;
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
    isValidInput(code);
    codeTable = new ArrayList();
  }

  /**
   * Encodes the characters code in the symbols code.
   */
  public void encode ()
  {
    String code = getCode();

    //always have start and stop characters
    if (!isStartStopPresent(code))
    {
      codeTable.add(TABLE[CHARTABLE.indexOf('*')]);
    }

    for (int i = 0; i < code.length(); i++)
    {
      final byte[] bytes = TABLE[CHARTABLE.indexOf(code.charAt(i))];
      codeTable.add(bytes);
    }

    //always have start and stop characters
    if (!isStartStopPresent(code))
    {
      codeTable.add(TABLE[CHARTABLE.indexOf('*')]);
    }
  }

  /**
   * Tells if the input code passed as argument is a valid code for the code 39
   * specification.
   *
   * @param code The input code to check.
   * @return Boolean
   *
   * @throws NullPointerException If <code>code</code> is null.
   */
  public static boolean isValidInput (final String code)
  {
    if (code == null)
    {
      throw new NullPointerException("Unable to verify a null code");
    }

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
   *
   * @throws NullPointerException If <code>code</code> is null.
   */
  public static boolean isStartStopPresent (final String code)
  {
    if (code == null)
    {
      throw new NullPointerException("Unable to verify a null code");
    }

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
   * Returns the code that should be displayed, it can differ from the base code passed as
   * argument to the constructor.
   *
   * @return The code to display or null if the code should not be displayed.
   *
   * @throws IllegalStateException If there is a conflict for displaying the code.
   */
  public String getDisplayedCode ()
  {
    final String oldCode = super.getDisplayedCode();
    final StringBuffer buffer = new StringBuffer();

    if (isShowCode())
    {

      if (isShowStartStop() && !isStartStopPresent(oldCode))
      {
        buffer.append("*");
        buffer.append(oldCode);
        if (isShowChecksum())
        {
          if (isAppendedChecksum())
          {
            buffer.append(getChecksum());
          }
          else
          {
            throw new IllegalStateException("Showing the checksum is requested but it has not been ask to be computed.");
          }
        }
        buffer.append("*");
      }
      else if (!isShowStartStop() && isStartStopPresent(oldCode))
      {
        buffer.append(oldCode.substring(1, oldCode.length() - 1));
        if (isShowChecksum())
        {
          buffer.append(getChecksum());
        }
      }

      return buffer.toString();
    }

    return null;
  }

  /**
   * Computes the bars bounds.<p>
   * <p/>
   * A symbol is composed of 3 wide bars and 6 narrow bars.<br> Each symbol is separated
   * by a narrow bar.
   *
   * @return The bars bounds initialized on <code>x=0</code> and <code>y=0</code>.
   */
  public Rectangle2D getBarBounds ()
  {
    int nbChars = codeTable.size();
    float width = nbChars * (3 * getNarrowToWide() + 6) * getBarWidth() + (nbChars - 1) * getBarWidth();

    return new Rectangle2D.Double(0, 0, width, getBarHeight());
  }

  /**
   * Draws the bars symbols. The graphics target is already clipped.
   *
   * @param g2 The graphics target.
   */
  public void drawBars (Graphics2D g2)
  {
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


  /**
   * Returns the symbols table.
   *
   * @return The symbols table.
   */
  public List getCodeTable ()
  {
    return codeTable;
  }

  /**
   * Sets the symbols table.
   *
   * @param codeTable The symbols table.
   */
  public void setCodeTable (List codeTable)
  {
    this.codeTable = codeTable;
  }

  public char getChecksum ()
  {
    return checksum;
  }


  /**
   * Tells if the start and stop characters have to be displayed.
   *
   * @return Boolean.
   */
  public boolean isShowStartStop ()
  {
    return showStartStop;
  }

  /**
   * Sets if the start and stop characters have to be displayed.
   *
   * @param showStartStop Boolean.
   */
  public void setShowStartStop (boolean showStartStop)
  {
    this.showStartStop = showStartStop;
  }

  public boolean isAppendedChecksum ()
  {
    return appendedChecksum;
  }

  public void setAppendedChecksum (boolean appendedChecksum)
  {
    this.appendedChecksum = appendedChecksum;
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
