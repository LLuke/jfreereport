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
 * Barcode93.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 * Contributor(s):   Cedric Pronzato;
 *
 * $Id: Barcode93.java,v 1.1 2005/05/21 21:32:12 mimil Exp $
 *
 * Changes (from 2005-05-19)
 * -------------------------
 *
 */

package org.jfree.report.dev.barcode.base;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.jfree.report.dev.barcode.Barcode1D;

/**
 * Encodes a string into code93 specifications
 * <p/>
 * Symbols allowed: 0-9 A-Z $ % + - . / space <br> Start character: yes, in the symbols
 * table (*) <br> Stop character: yes, in the symbols table (*) <br> Check characters:
 * availables (obligatory?)
 * <p/>
 * Also know as: USS-93
 *
 * @author Mimil
 */
public class Barcode93 extends Barcode1D
{


  /**
   * First checksum character
   */
  private char checksumC;
  /**
   * If the first checksum must be computed and displayed.
   */
  private boolean appendedChecksumC = false;
  /**
   * Second checksum character
   */
  private char checksumK;
  /**
   * If the second checksum must be computed and displayed.
   */
  private boolean appendedChecksumK = false;
  /**
   * If the start and stop charcters have to be shown.
   */
  private boolean showStartStop = false;

  private boolean showChecksums = false;


  public Barcode93 ()
  {
    super();
    setEncoder(new Barcode93Encoder());
  }

  /**
   * Returns the first checksum character.
   *
   * @return The checksum character.
   */
  public char getChecksumC ()
  {
    return checksumC;
  }


  /**
   * Returns the second checksum character.
   *
   * @return The checksom character.
   */
  public char getChecksumK ()
  {
    return checksumK;
  }

  /**
   * Tells if the first checksum have to be computed and added to the displayed code.
   *
   * @return Boolean.
   */
  public boolean isAppendedChecksumC ()
  {
    return appendedChecksumC;
  }

  /**
   * Sets if the checksum have to be computed and added to the symbol.<br> If it is set to
   * <code>true</code>, the checksum is computed.
   *
   * @param appendedChecksumC Boolean.
   */
  public void setAppendedChecksumC (boolean appendedChecksumC)
  {
    this.appendedChecksumC = appendedChecksumC;

    if (appendedChecksumC == true)
    {

      final String code = getCode();

      int check = 0;
      final int length = code.length();
      for (int i = 0; i < length; i++)
      {
        int index = getEncoder().weight(code.charAt(length - i)); //from the right to the left
        check += index * ((i + 1) % 20);  //the weight starts at 1.
      }

      checksumC = (char) (check % 47);
    }
  }

  /**
   * Tells if the second checksim have to be computed and added to the displayed code.
   *
   * @return Boolean.
   */
  public boolean isAppendedChecksumK ()
  {
    return appendedChecksumK;
  }

  /**
   * Sets if the checksum have to be computed and added to the symbol.<br> If it is set to
   * <code>true</code>, the checksum is computed.
   *
   * @param appendedChecksumK Boolean.
   * @throws IllegalStateException If the first checksum has not be appended before this
   *                               one.
   */
  public void setAppendedChecksumK (boolean appendedChecksumK)
  {
    if (isAppendedChecksumC() == false)
    {
      throw new IllegalStateException("You must have appended the checksum C before appending the checksum K");
    }

    this.appendedChecksumK = appendedChecksumK;

    if (appendedChecksumK == true)
    {
      final String code = getCode();

      int check = 0;
      final int length = code.length();
      for (int i = 0; i < length; i++)
      {
        int index = getEncoder().weight(code.charAt(length - i));  //from thr right to the left

        //the weight starts at 1 and don't forget that the checksumC is the right most character now
        check += index * ((i + 1 + 1) % 15);
      }

      check += getEncoder().weight(code.charAt(getChecksumC()));

      checksumK = (char) (check % 47);
    }
  }

  /**
   * Tells if the cheksums have to be displayed.
   *
   * @return Boolean
   */
  public boolean isShowChecksums ()
  {
    return showChecksums;
  }

  /**
   * Sets if the checksums have to be displayed.
   *
   * @param showChecksums Boolean.
   */
  public void setShowChecksums (boolean showChecksums)
  {
    this.showChecksums = showChecksums;
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
   * Tells if the input code passed as argument is a valid code for the code 93
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
   * @return The code to display.
   *
   * @throws IllegalStateException If there is a conflict for displaying the code.
   */
  public String getDisplayedCode ()
  {
    if (isShowCode())
    {
      final StringBuffer buffer = new StringBuffer();
      final String oldCode = super.getDisplayedCode();

      if (isShowStartStop())
      {
        buffer.append("*");
      }
      buffer.append(oldCode);

      if (isShowChecksums())
      {
        if (isAppendedChecksumC())
        {
          buffer.append(getChecksumC());
        }

        if (isAppendedChecksumK())
        {
          buffer.append(getChecksumK());
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
   * Computes the bars bounds.<p> A symbol is composed of 9 narrow bars.<br> The barcode
   * is ended by a narrow bar.
   *
   * @return The bars bounds initialized on <code>x=0</code> and <code>y=0</code>.
   */
  public Rectangle2D getBarBounds ()
  {
    if (!isEncoded())
    {
      encode();
    }

    final int nbChars = getCodeTable().size();
    final float barcodeWidth = getBarWidth() * (nbChars * 9) + 1;

    return new Rectangle2D.Double(0, 0, barcodeWidth, getBarHeight());
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

    double currentX = 0;

    for (int i = 0; i < getCodeTable().size(); i++)
    {
      final byte[] bytes = (byte[]) getCodeTable().get(i);

      for (int j = 0; j < bytes.length; j++)
      {
        if (bytes[i] == 0) //spaces
        {
          currentX += getBarWidth();
        }
        else
        {
          g2.fill(new Rectangle2D.Double(currentX, 0, currentX += getBarWidth(), getBarHeight()));
        }
      }
    }
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

    if (isAppendedChecksumC())
    {
      final byte[][] bytes = getEncoder().encode(code.charAt(getChecksumC()));
      for (int j = 0; j < bytes.length; j++)
      {
        codeTable.add(bytes[j]);
      }
    }

    if (isAppendedChecksumK())
    {
      final byte[][] bytes = getEncoder().encode(code.charAt(getChecksumK()));
      for (int j = 0; j < bytes.length; j++)
      {
        codeTable.add(bytes[j]);
      }
    }

    //always have start and stop characters
    codeTable.add(getEncoder().encode('*')[0]);

    setEncoded(true);
  }
}
