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
 * $Id: Barcode39.java,v 1.10 2005/06/01 21:32:47 mimil Exp $
 *
 * Changes (from 2005-05-17) (CP)
 * -------------------------
 *
 */

package org.jfree.report.dev.barcode.base;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.jfree.report.dev.barcode.Barcode1D;
import org.jfree.report.dev.barcode.BarcodeException;

/**
 * Encodes a string into code39 specifications
 * <p/>
 * Symbols allowed: 0-9 A-Z $ % + - . / space <br> Start character: yes, in the symbols
 * table (*) <br> Stop character: yes, in the symbols table (*) <br> Check character:
 * available
 * <p/>
 * Also know as: - USD3 - 3of9 - HIBC - LOGMARS
 *
 * @author Mimil
 */
public class Barcode39 extends Barcode1D
{


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


  public Barcode39 ()
  {
    super();
    setEncoder(new Barcode39Encoder());
  }

  /**
   * Encodes the characters code in the symbols code.
   */
  public void encode ()
  {
    final String code = getCode();
    final List codeTable = getCodeTable();

    //always have start and stop characters
    codeTable.add(getEncoder().encode('*')[0]);

    for (int i = 0; i < code.length(); i++)
    {
      final byte[][] bytes = getEncoder().encode(code.charAt(i));
      for (int j = 0; j < bytes.length; j++)
      {
        codeTable.add(bytes[j]);
      }
    }

    if (isAppendedChecksum())
    {
      if (getEncoder().isValid(getChecksum()))
      {
        final byte[][] bytes = getEncoder().encode(getChecksum());
        for (int j = 0; j < bytes.length; j++)
        {
          codeTable.add(bytes[j]);
        }
      }
      else
      {
        throw new BarcodeException("The checksum is invalid because it does not belong to" +
                "the symbology of this barcode.");
      }
    }

    //always have start and stop characters
    codeTable.add(getEncoder().encode('*')[0]);

    setEncoded(true);
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
  public boolean isValidInput (final String code)
  {
    if (code == null)
    {
      throw new NullPointerException("Unable to verify a null code");
    }

    //NB: there is no length check in code 39.
    for (int i = 0; i < code.length(); i++)
    {
      boolean b = getEncoder().isValid(code.charAt(i));
      if (!b)
      {
        return false;
      }
    }
    return true;
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

      if (isShowStartStop())
      {
        buffer.append("*");
      }
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
      if (isShowStartStop())
      {
        buffer.append("*");
      }

      return buffer.toString();
    }

    return null;
  }

  /**
   * Computes the bars bounds.<p> A symbol is composed of 3 wide bars and 6 narrow
   * bars.<br> Each symbol is separated by a narrow bar.
   *
   * @return The bars bounds initialized on <code>x=0</code> and <code>y=0</code>.
   */
  public Rectangle2D getBarBounds ()
  {
    if (!isEncoded())
    {
      encode();
    }

    int nbChars = getCodeTable().size();
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
    if (!isEncoded())
    {
      encode();
    }

    for (int i = 0; i < getCodeTable().size(); i++)
    {
      final byte[] bytes = (byte[]) getCodeTable().get(i);

      for (int j = 0; j < bytes.length; j++)
      {
        if (bytes[j] == 0) //narrow
        {
          if (j % 2 == 0)  // bar
          {
            g2.fill(new Rectangle2D.Double(0, 0, getBarWidth(), getBarHeight()));
          }
          g2.translate(getBarWidth(), 0);
        }
        else  //wide
        {
          if (j % 2 == 0)  // bar
          {
            g2.fill(new Rectangle2D.Double(0, 0, getBarWidth() * getNarrowToWide(), getBarHeight()));
          }
          g2.translate(getBarWidth() * getNarrowToWide(), 0);
        }
      }

      if (i < getCodeTable().size() - 1)
      {
        g2.translate(getBarWidth(), 0);        //the inter character gap
      }
    }
  }


  /**
   * Returns the checksum.
   *
   * @return The checksum character.
   */
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

  /**
   * Tells if the checksum have to be computed and added to the symbol.
   *
   * @return Boolean.
   */
  public boolean isAppendedChecksum ()
  {
    return appendedChecksum;
  }

  /**
   * Sets if the checksum have to be computed and added to the symbol.<br> If it is set to
   * <code>true</code>, the checksum is computed.
   *
   * @param appendedChecksum Boolean.
   */
  public void setAppendedChecksum (boolean appendedChecksum)
  {
    this.appendedChecksum = appendedChecksum;
    if (appendedChecksum == true)
    {
      final String code = getCode();

      int index = 0;
      for (int i = 0; i < code.length(); i++)
      {
        index += getEncoder().weight(code.charAt(i));
      }

      checksum = getEncoder().charAt(index % 43);
    }
  }

  /**
   * Tells if the checksum have to be displayed in the code.
   *
   * @return Boolean
   */
  public boolean isShowChecksum ()
  {
    return showChecksum;
  }

  /**
   * Sets if the checksum have to be displayed in the code.
   *
   * @param showChecksum
   */
  public void setShowChecksum (boolean showChecksum)
  {
    this.showChecksum = showChecksum;
  }
}
