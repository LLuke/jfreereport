/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * PrinterDriver.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.output.pageable.plaintext;

import java.awt.print.Paper;
import java.io.IOException;

public interface PrinterDriver
{
  /**
   * the roman font.
   */
  public static final byte SELECT_FONT_ROMAN = 0x00;
  /**
   * the swiss font.
   */
  public static final byte SELECT_FONT_SWISS = 0x01;
  /**
   * the courier font.
   */
  public static final byte SELECT_FONT_COURIER = 0x02;
  /**
   * the prestige font.
   */
  public static final byte SELECT_FONT_PRESTIGE = 0x03;
  /**
   * the OCR-A font.
   */
  public static final byte SELECT_FONT_OCR_A = 0x05;
  /**
   * the OCR-B font.
   */
  public static final byte SELECT_FONT_OCR_B = 0x06;
  /**
   * the orator font.
   */
  public static final byte SELECT_FONT_ORATOR = 0x07;
  /**
   * the swiss-bold font.
   */
  public static final byte SELECT_FONT_SWISS_BOLD = 0x7A;
  /**
   * the gothic font.
   */
  public static final byte SELECT_FONT_GOTHIC = 0x7C;
  /**
   * selects the font, which is selected on the printer menu.
   */
  public static final byte SELECT_FONT_FROM_MENU = 0x7F;
  /**
   * the Carriage Return control character, the printer carriage returns to the start of
   * the line.
   */
  public static final char CARRIAGE_RETURN = 0x0D;
  /**
   * scrolls the paper up a single line.
   */
  public static final char LINE_FEED = 0x0A;
  /**
   * the form feed character, ejects the current page and starts the next page.
   */
  public static final char FORM_FEED = 0x0C;
  /**
   * the space character.
   */
  public static final char SPACE = 0x20;

  public static final float CPI_10 = 10;
  public static final float CPI_12 = 12;
  public static final float CPI_15 = 15;
  public static final float CPI_17 = 17.14f;
  public static final float CPI_20 = 20;

  public static final float LPI_10 = 10;
  public static final float LPI_6 = 6;

  /**
   * Gets the default character width in CPI.
   *
   * @return the default character width in CPI.
   */
  public float getCharactersPerInch ();

  /**
   * Gets the default line height.
   *
   * @return the default line height.
   */
  public float getLinesPerInch ();

  /**
   * Resets the printer and starts a new page. Prints the top border lines (if
   * necessary).
   *
   * @throws IOException if there was an IOError while writing the command
   */
  void startPage (final Paper paper, final String encoding)
          throws IOException;

  /**
   * Ends the current page. Should print empty lines or an FORM_FEED command.
   *
   * @param overflow
   * @throws IOException if there was an IOError while writing the command
   */
  void endPage (boolean overflow)
          throws IOException;

  /**
   * Starts a new line.
   *
   * @throws IOException if an IOError occures.
   */
  void startLine ()
          throws IOException;

  /**
   * Ends a new line.
   *
   * @param overflow
   * @throws IOException if an IOError occures.
   */
  void endLine (boolean overflow)
          throws IOException;

  /**
   * Prints a single text chunk at the given position on the current line. The chunk
   * should not be printed, if an previous chunk overlays this chunk.
   *
   * @param chunk the chunk that should be written
   * @throws IOException if an IO error occured.
   */
  public void printChunk (PlaintextDataChunk chunk)
          throws IOException;

  /**
   * Prints an empty chunk. This is called for all undefined chunk-cells. The last defined
   * font is used to print that empty text.
   *
   * @throws IOException if an IOError occured.
   */
  public void printEmptyChunk (int count)
          throws IOException;

  /**
   * Flushes the output stream.
   *
   * @throws java.io.IOException if an IOError occured.
   */
  public void flush ()
          throws IOException;

  /**
   * Prints some raw content. This content is not processed in any way, so be very
   * carefull.
   *
   * @param out the content that should be printed.
   */
  public void printRaw (byte[] out)
          throws IOException;

}
