/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/libfonts/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * HorizontalHeaderTable.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.fonts.truetype;

import org.jfree.fonts.ByteAccessUtilities;

/**
 * Creation-Date: 06.11.2005, 20:24:42
 *
 * @author Thomas Morgner
 */
public class HorizontalHeaderTable implements FontTable
{
  public static final long TABLE_ID =
          ('h' << 24 | 'h' << 16 | 'e' << 8 | 'a');

  private long version;
  private short ascender;
  private short descender;
  private short lineGap;

  private int maxAdvanceWidth;
  private short minLeftSidebearing;
  private short minRightSidebearing;
  private short xMaxExtent;
  private short caretSlopeRise;
  private short caretSlopeRun;
  private short caretOffset;
//  private short reserved0;
//  private short reserved1;
//  private short reserved2;
//  private short reserved3;
  private short metricDataFormat;
  private int numberOfHMetrics;

  public HorizontalHeaderTable(final byte[] data)
  {
    version = ByteAccessUtilities.readULong(data, 0);
    ascender = ByteAccessUtilities.readShort(data, 4);
    descender = ByteAccessUtilities.readShort(data, 6);
    lineGap = ByteAccessUtilities.readShort(data, 8);

    maxAdvanceWidth = ByteAccessUtilities.readUShort(data, 10);
    minLeftSidebearing = ByteAccessUtilities.readShort(data, 12);
    minRightSidebearing = ByteAccessUtilities.readShort(data, 14);
    xMaxExtent = ByteAccessUtilities.readShort(data, 16);
    caretSlopeRise = ByteAccessUtilities.readShort(data, 18);
    caretSlopeRun = ByteAccessUtilities.readShort(data, 20);
    caretOffset = ByteAccessUtilities.readShort(data, 22);
    metricDataFormat = ByteAccessUtilities.readShort(data, 32);
    numberOfHMetrics = ByteAccessUtilities.readUShort(data, 34);
  }

  public long getVersion()
  {
    return version;
  }

  public void setVersion(final int version)
  {
    this.version = version;
  }

  public short getAscender()
  {
    return ascender;
  }

  public void setAscender(final short ascender)
  {
    this.ascender = ascender;
  }

  public short getDescender()
  {
    return descender;
  }

  public void setDescender(final short descender)
  {
    this.descender = descender;
  }

  public short getLineGap()
  {
    return lineGap;
  }

  public void setLineGap(final short lineGap)
  {
    this.lineGap = lineGap;
  }

  public int getMaxAdvanceWidth()
  {
    return maxAdvanceWidth;
  }

  public void setMaxAdvanceWidth(final int maxAdvanceWidth)
  {
    this.maxAdvanceWidth = maxAdvanceWidth;
  }

  public short getMinLeftSidebearing()
  {
    return minLeftSidebearing;
  }

  public void setMinLeftSidebearing(final short minLeftSidebearing)
  {
    this.minLeftSidebearing = minLeftSidebearing;
  }

  public short getMinRightSidebearing()
  {
    return minRightSidebearing;
  }

  public void setMinRightSidebearing(final short minRightSidebearing)
  {
    this.minRightSidebearing = minRightSidebearing;
  }

  public short getxMaxExtent()
  {
    return xMaxExtent;
  }

  public void setxMaxExtent(final short xMaxExtent)
  {
    this.xMaxExtent = xMaxExtent;
  }

  public short getCaretSlopeRise()
  {
    return caretSlopeRise;
  }

  public void setCaretSlopeRise(final short caretSlopeRise)
  {
    this.caretSlopeRise = caretSlopeRise;
  }

  public short getCaretSlopeRun()
  {
    return caretSlopeRun;
  }

  public void setCaretSlopeRun(final short caretSlopeRun)
  {
    this.caretSlopeRun = caretSlopeRun;
  }

  public short getCaretOffset()
  {
    return caretOffset;
  }

  public void setCaretOffset(final short caretOffset)
  {
    this.caretOffset = caretOffset;
  }

  public short getMetricDataFormat()
  {
    return metricDataFormat;
  }

  public void setMetricDataFormat(final short metricDataFormat)
  {
    this.metricDataFormat = metricDataFormat;
  }

  public int getNumberOfHMetrics()
  {
    return numberOfHMetrics;
  }

  public void setNumberOfHMetrics(final int numberOfHMetrics)
  {
    this.numberOfHMetrics = numberOfHMetrics;
  }

  public long getName()
  {
    return TABLE_ID;
  }
}
