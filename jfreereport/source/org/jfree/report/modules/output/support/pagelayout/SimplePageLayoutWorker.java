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
 * SimplePageLayoutWorker.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 26.09.2003 : Initial version
 *  
 */

package org.jfree.report.modules.output.support.pagelayout;

import org.jfree.report.Band;
import org.jfree.report.ReportProcessingException;

public interface SimplePageLayoutWorker
{
  boolean PAGEBREAK_BEFORE_HANDLED = true;
  boolean PAGEBREAK_BEFORE_IGNORED = false;
  boolean BAND_SPOOLED = true;
  boolean BAND_PRINTED = false;

  public boolean print(Band band, boolean spoolBand, boolean handlePagebreakBefore)
      throws ReportProcessingException;

  public boolean printBottom(Band band) throws ReportProcessingException;

  public boolean isPageEnded ();

  /**
   * Reinitialize the cursor of the layout worker. Called when
   * a new page is started.
   */
  public void resetCursor();
  public float getCursorPosition ();
  public void setReservedSpace (float reserved);
  public float getReservedSpace ();

  public void setMaximumBandHeight (float maxBandHeight);
  public float getMaximumBandHeight ();

  public boolean isPageEmpty();

}
