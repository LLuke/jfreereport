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
 * $Id: ReportProcessor.java,v 1.1 2002/05/14 21:35:02 taqua Exp $
 * Changes
 * -------------------------
 * 10-May-2002 : Initial version
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

  /**
   * Draws this band to the current OutputTarget. If draw is false, no printing is performed.
   *
   * @param band the band to be printed
   */
  protected void draw (Band band)
  {
    draw (band, cursor.getY ());
  }

  /**
   * checks whether to draw on the output target.
   * @returns true, if the processor should draw, false otherwise.
   */
  protected boolean isDraw ()
  {
    return draw;
  }

  /**
   * Draws this band to the current OutputTarget. If draw is false, no printing is performed.
   * Prints on the given position. The cursor is advanced to the given position plus the
   * height of the band.
   *
   * @param band the band to be printed
   * @param y the position on which to print.
   */
  protected void draw (Band band, float y)
  {
    if (isDraw())
    {
      band.draw (target, cursor.getPageLeft (), y);
    }
    cursor.advance (y - cursor.getY() + band.getHeight ());
  }

  /**
   * Prints the given ReportHeader on the current OutputTarget. If draw is false,
   * no printing is performed, but the cursor is advanced.
   *
   * @param reportHeader the reportheader to print.
   */
  public void printReportHeader (ReportHeader reportHeader)
  {
    if (reportHeader == null)
      throw new NullPointerException ("ReportHeader must not be null");

    draw (reportHeader);
    pageDone = reportHeader.isOwnPage ();
  }


  /**
   * Prints the given PageHeader on the current OutputTarget. If draw is false,
   * no printing is performed, but the cursor is advanced.
   *
   * @param reportHeader the pageheader to print.
   */
  public void printPageHeader (PageHeader pageHeader)
  {
    if (pageHeader == null)
      throw new NullPointerException ("PageHeader must not be null");

    draw (pageHeader);
  }

  /**
   * Prints the given PageFooter on the current OutputTarget. If draw is false,
   * no printing is performed, but the cursor is advanced.
   *
   * @param reportHeader the pagefooter to print.
   */
  public void printPageFooter (PageFooter pageFooter)
  {
    if (pageFooter == null)
      throw new NullPointerException ("PageHeader must not be null");

    draw (pageFooter, cursor.getPageBottom ());
  }

  /**
   * Prints the given GroupHeader on the current OutputTarget. If draw is false,
   * no printing is performed, but the cursor is advanced.
   *
   * @param reportHeader the groupheader to print.
   */
  public void printGroupHeader (GroupHeader groupHeader)
  {
    if (groupHeader == null)
      throw new NullPointerException ("PageHeader must not be null");

    if (isSpaceFor (groupHeader))
    {
      draw (groupHeader);
    }
  }

  /**
   * Prints the given ItemBand on the current OutputTarget. If draw is false,
   * no printing is performed, but the cursor is advanced.
   *
   * @param reportHeader the itemband to print.
   */
  public void printItemBand (ItemBand itemBand)
  {
    if (itemBand == null)
      throw new NullPointerException ("ItemBand must not be null");

    if (isSpaceFor (itemBand))
    {
      draw (itemBand);
    }
  }

  /**
   * Prints the given GroupFooter on the current OutputTarget. If draw is false,
   * no printing is performed, but the cursor is advanced.
   *
   * @param reportHeader the GroupFooter to print.
   */
  public void printGroupFooter (GroupFooter footer)
  {
    if (footer == null)
      throw new NullPointerException ("GroupFooter must not be null");

    if (isSpaceFor (footer))
    {
      draw (footer);
    }
  }

  /**
   * Prints the given ReportFooter on the current OutputTarget. If draw is false,
   * no printing is performed, but the cursor is advanced.
   *
   * @param reportHeader the reportfooter to print.
   */
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

  /**
   * checks whether a pagebreak should be performed before printing any other element.
   */
  public boolean isPageDone ()
  {
    return pageDone;
  }
}
