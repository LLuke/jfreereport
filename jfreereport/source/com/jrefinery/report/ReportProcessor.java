/**
 * =============================================================
 * JFreeReport - a Java report printing API;
 * =========================================
 *
 * (C) Copyright 2000, Simba Management Limited;
 * Contact: David Gilbert (david.gilbert@bigfoot.com);
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307, USA.
 *
 * ReportProcessingException.java
 * -----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: ReportProcessingException.java,v 1.1.1.1 2002/04/25 17:02:14 taqua Exp $
 * Changes
 * -------------------------
 * 10-May-2002 : Initial version
 *
 */
package com.jrefinery.report;

/**
 * Processes a page of an report. This is an abstraction to protect the reportState from
 * detailed knowledge of the printing process. All printing centric state information is
 * collected in this class.
 */
public class ReportProcessor implements JFreeReportConstants
{
  private Cursor cursor;
  private OutputTarget target;
  private boolean draw;
  private boolean pageDone;
  private JFreeReport report;

  /**
   * Creates a new ReportProcessor. The ReportProcessor will use the given output target
   * to print. The pageFooter is used to reserve the needed space in the cursor.
   */
  public ReportProcessor (OutputTarget out, boolean draw, PageFooter pageFooter)
  {
    cursor = new Cursor (out);
    target = out;
    this.draw = draw;
    pageDone = false;

    cursor.reserveSpace (pageFooter.getHeight ());
  }

  protected void draw (Band band)
  {
    draw (band, cursor.getY ());
  }

  protected void draw (Band band, float y)
  {
    if (draw)
    {
      band.draw (target, cursor.getPageLeft (), y);
    }
    cursor.advance (band.getHeight ());
  }

  public void printReportHeader (ReportHeader reportHeader)
  {
    if (reportHeader == null)
      throw new NullPointerException ("ReportHeader must not be null");

    draw (reportHeader);
    pageDone = reportHeader.isOwnPage ();
  }

  public void printPageHeader (PageHeader pageHeader)
  {
    if (pageHeader == null)
      throw new NullPointerException ("PageHeader must not be null");

    draw (pageHeader);
  }

  public void printPageFooter (PageFooter pageFooter)
  {
    if (pageFooter == null)
      throw new NullPointerException ("PageHeader must not be null");

    draw (pageFooter, cursor.getPageBottom ());
  }

  public void printGroupHeader (GroupHeader groupHeader)
  {
    if (groupHeader == null)
      throw new NullPointerException ("PageHeader must not be null");

    if (isSpaceFor (groupHeader))
    {
      draw (groupHeader);
    }
  }

  public void printItemBand (ItemBand itemBand)
  {
    if (itemBand == null)
      throw new NullPointerException ("ItemBand must not be null");

    if (isSpaceFor (itemBand))
    {
      draw (itemBand);
    }
  }

  public void printGroupFooter (GroupFooter footer)
  {
    if (footer == null)
      throw new NullPointerException ("GroupFooter must not be null");

    if (isSpaceFor (footer))
    {
      draw (footer);
    }
  }

  public void printReportFooter (ReportFooter footer)
  {
    if (footer == null)
      throw new NullPointerException ("ReportFooter must not be null");

    if (isSpaceFor (footer))
    {
      draw (footer);
    }
  }

  /**
   * Tests whether there is enough space to print the given band. If there is not enough space,
   * set the pagedone flag, to signal that a pageBreak should be done.
   */
  public boolean isSpaceFor (Band band)
  {
    if (band == null)
      throw new NullPointerException ("Band must not be null");

    if (isPageDone())
      return false;

    if (cursor.isSpaceFor (band.getHeight ()) == false)
    {
      setPageDone ();
      return false;
    }
    return true;
  }

  /**
   * declares that this page is fully printed.
   */
  public void setPageDone ()
  {
    pageDone = true;
  }

  public boolean isPageDone ()
  {
    return pageDone;
  }
}
