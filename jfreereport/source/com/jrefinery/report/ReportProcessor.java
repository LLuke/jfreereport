/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ReportProcessingException.java
 * ------------------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportProcessor.java,v 1.14 2002/10/15 20:37:13 taqua Exp $
 *
 * Changes
 * -------
 * 10-May-2002 : Initial version
 * 04-Jun-2002 : Documentation
 * 09-Jul-2002 : Docs
 * 08-Aug-2002 : Band visibility support
 */
package com.jrefinery.report;

import com.jrefinery.report.targets.OutputTarget;
import com.jrefinery.report.targets.OutputTargetException;
import com.jrefinery.report.util.Log;

/**
 * Processes a page of a report. This is an abstraction to protect the reportState from
 * detailed knowledge of the printing process. All printing centric state information is
 * concentrated in this class.
 * <p>
 * Warning: This class will completly change in the next days, dont take the stuff here too serious for now!
 *
 * @author TM
 */
public class ReportProcessor implements JFreeReportConstants
{
  /** The cursor. */
  private Cursor cursor;

  /** The output target. */
  private OutputTarget target;

  /** Drawing to the output target?  Or just repaginating? */
  private boolean draw;

  /** A flag that indicates the current page is done. */
  private boolean pageDone;

  /**
   * Creates a new ReportProcessor. The ReportProcessor will use the given output target
   * for printing. The pageFooter is used to reserve the needed space in the cursor.
   *
   * @param out  the output target.
   * @param draw  the 'draw' flag.
   * @param pageFooter  the page footer.
   */
  public ReportProcessor (OutputTarget out, boolean draw, PageFooter pageFooter)
  {
    this.cursor = new Cursor (out);
    this.target = out;
    this.draw = draw;
    this.pageDone = false;

    this.cursor.reserveSpace (pageFooter.getHeight ());
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
   * Checks whether to draw on the output target.
   *
   * @return true, if the processor should draw, false otherwise.
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
    float dheight = 0;

    if (band.isVisible () == false)
    {
      return;
    }

    if (isDraw ())
    {
      try
      {
        dheight = band.draw (target, cursor.getPageLeft (), y);
      }
      catch (OutputTargetException e)
      {
        Log.error ("Unable to draw band", e);
      }
    }
    if (band.getHeight() == -100)
    {
      setPageDone();
      return;
    }

    if (dheight < band.getHeight ())
    {
      dheight = band.getHeight ();
    }

    cursor.advance (y - cursor.getY () + dheight);
  }

  /**
   * Prints the given ReportHeader on the current OutputTarget. If draw is false,
   * no printing is performed, but the cursor is advanced.
   *
   * @param reportHeader the reportheader to print.
   * @throws NullPointerException if the given Band is null
   */
  public void printReportHeader (ReportHeader reportHeader)
  {
    if (reportHeader == null)
    {
      throw new NullPointerException ("ReportHeader must not be null");
    }

    draw (reportHeader);
    pageDone = reportHeader.isOwnPage ();
  }


  /**
   * Prints the given PageHeader on the current OutputTarget. If draw is false,
   * no printing is performed, but the cursor is advanced.
   *
   * @param pageHeader the pageheader to print.
   * @throws NullPointerException if the given Band is null
   */
  public void printPageHeader (PageHeader pageHeader)
  {
    if (pageHeader == null)
    {
      throw new NullPointerException ("PageHeader must not be null");
    }

    draw (pageHeader);
  }

  /**
   * Prints the given PageFooter on the current OutputTarget. If draw is false,
   * no printing is performed, but the cursor is advanced.
   *
   * @param pageFooter the pagefooter to print.
   * @throws NullPointerException if the given Band is null
   */
  public void printPageFooter (PageFooter pageFooter)
  {
    if (pageFooter == null)
    {
      throw new NullPointerException ("PageHeader must not be null");
    }

    draw (pageFooter, cursor.getPageBottom ());
  }

  /**
   * Prints the given GroupHeader on the current OutputTarget. If draw is false,
   * no printing is performed, but the cursor is advanced.
   *
   * @param groupHeader the groupheader to print.
   * @throws NullPointerException if the given Band is null
   */
  public void printGroupHeader (GroupHeader groupHeader)
  {
    if (groupHeader == null)
    {
      throw new NullPointerException ("PageHeader must not be null");
    }

    if (isSpaceFor (groupHeader))
    {
      draw (groupHeader);
    }
  }

  /**
   * Prints the given ItemBand on the current OutputTarget. If draw is false,
   * no printing is performed, but the cursor is advanced.
   *
   * @param itemBand the itemband to print.
   * @throws NullPointerException if the given Band is null
   */
  public void printItemBand (ItemBand itemBand)
  {
    if (itemBand == null)
    {
      throw new NullPointerException ("ItemBand must not be null");
    }

    if (isSpaceFor (itemBand))
    {
      draw (itemBand);
    }
  }

  /**
   * Prints the given GroupFooter on the current OutputTarget. If draw is false,
   * no printing is performed, but the cursor is advanced.
   *
   * @param footer the GroupFooter to print.
   * @throws NullPointerException if the given Band is null
   */
  public void printGroupFooter (GroupFooter footer)
  {
    if (footer == null)
    {
      throw new NullPointerException ("GroupFooter must not be null");
    }

    if (isSpaceFor (footer))
    {
      draw (footer);
    }
  }

  /**
   * Prints the given ReportFooter on the current OutputTarget. If draw is false,
   * no printing is performed, but the cursor is advanced.
   *
   * @param footer the reportfooter to print.
   * @throws NullPointerException if the given Band is null
   */
  public void printReportFooter (ReportFooter footer)
  {
    if (footer == null)
    {
      throw new NullPointerException ("ReportFooter must not be null");
    }

    if (isSpaceFor (footer))
    {
      draw (footer);
    }
  }

  /**
   * Tests whether there is enough space to print the given band. If there is not enough space,
   * set the pagedone flag, to signal that a pageBreak should be done. After a isSpaceFor()
   * returned false, isDone() will also return always false and no printing can be done anymore,
   * with the exception of the pagefooter, whose space was reserved previously.
   *
   * @param band the band which is tested
   *
   * @return true, if there is space for the band.
   *
   * @throws NullPointerException if the given Band is null
   */
  public boolean isSpaceFor (Band band)
  {
    if (band == null)
    {
      throw new NullPointerException ("Band must not be null");
    }

    if (isPageDone ())
    {
      return false;
    }

    if (band.isVisible () == false)
    {
      return true;
    }

    if (band.getHeight() == -100)
    {
      return true;
    }

    if (cursor.isSpaceFor (band.getHeight ()) == false)
    {
      setPageDone ();
      return false;
    }
    return true;
  }

  /**
   * Declares that this page is fully printed. Every subsequent call to isSpaceFor will
   * return false, as this page is completly printed.
   */
  public void setPageDone ()
  {
    pageDone = true;
  }

  /**
   * Checks whether a pagebreak should be performed before printing any other element.
   *
   * @return the 'page-done' flag.
   */
  public boolean isPageDone ()
  {
    return pageDone;
  }
}
