/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * SimplePageLayoutWorker.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: SimplePageLayoutWorker.java,v 1.5 2005/02/19 13:30:00 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 26.09.2003 : Initial version
 *  
 */

package org.jfree.report.modules.output.support.pagelayout;

import org.jfree.report.Band;
import org.jfree.report.ReportProcessingException;

/**
 * The layout worker is responsible to perform the to perform the content creation and
 * output.
 *
 * @author Thomas Morgner
 */
public interface SimplePageLayoutWorker
{
  /**
   * A constant defining, that the 'pagebreak before' should be handled.
   */
  public static final boolean PAGEBREAK_BEFORE_HANDLED = true;
  /**
   * A constant defining, that the 'pagebreak before' should be ignored.
   */
  public static final boolean PAGEBREAK_BEFORE_IGNORED = false;
  /**
   * A constant defining, that the output should be spooled and not yet printed.
   */
  public static final boolean BAND_SPOOLED = true;
  /**
   * A constant defining, that the output can be safely printed.
   */
  public static final boolean BAND_PRINTED = false;

  /**
   * Prints the given band at the current cursor position.
   *
   * @param band                  the band to be printed.
   * @param spoolBand             a flag defining whether the content should be spooled.
   * @param handlePagebreakBefore a flag defining whether the worker should check for the
   *                              'pagebreak-before' flag.
   * @return true, if the band was printed, false otherwise.
   *
   * @throws ReportProcessingException if an exception occured while processing the band.
   */
  public boolean print (Band band, boolean spoolBand, boolean handlePagebreakBefore)
          throws ReportProcessingException;

  /**
   * Prints the given band at the bottom of the page.
   *
   * @param band the band.
   * @return true, if the band was printed successfully, false otherwise.
   *
   * @throws ReportProcessingException if an exception occured while processing the band.
   */
  public boolean printBottom (Band band)
          throws ReportProcessingException;

  /**
   * Checks, whether the page has ended. Once a page that is completly filled, only the
   * page footer will be printed and a page break will be done after that.
   *
   * @return true, if the page is finished, false otherwise.
   */
  public boolean isPageEnded ();

  /**
   * Reinitialize the cursor of the layout worker. Called when a new page is started.
   */
  public void resetCursor ();

  /**
   * Returns the current cursor position. It is assumed, that the cursor goes from top to
   * down, columns are not used.
   *
   * @return the cursor position.
   */
  public long getCursorPosition ();

  /**
   * Defines the reserved size for the current page. This size is not used when performing
   * a layout. This is usually used to preserve the pagefooters space.
   *
   * @param reserved the reserved page height.
   */
  public void setReservedSpace (long reserved);

  /**
   * Returns the reserved size for the current page. This size is not used when performing
   * a layout. This is usually used to preserve the pagefooters space.
   *
   * @return the reserved page height.
   */
  public long getReservedSpace ();

  /**
   * Marks the position of the first content after the page header. This can be used to
   * limit the maximum size of bands so that they do not exceed the available page space.
   * <p/>
   * This feature will be obsolete when bands can span multiple pages.
   *
   * @param topPosition the first content position.
   */
  public void setTopPageContentPosition (long topPosition);

  /**
   * Returns the position of the first content.
   *
   * @return the first content position.
   */
  public long getTopContentPosition ();

  /**
   * Checks, whether the current page is empty. An page is empty if it does not contain
   * printed content. An empty page may have spooled content.
   *
   * @return true, if the page is empty, false otherwise.
   */
  public boolean isPageEmpty ();

  public boolean isWatermarkSupported ();

  public boolean printWatermark (Band watermark)
          throws ReportProcessingException;
}
