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
 * AlignedLogicalPageWrapper.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AlignedLogicalPageWrapper.java,v 1.8 2003/06/27 14:25:23 taqua Exp $
 *
 * Changes
 * -------
 * 29-Jan-2003 : Initial version
 *
 */
package com.jrefinery.report.targets.pageable;

import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;

import com.jrefinery.report.Band;
import com.jrefinery.report.targets.base.layout.LayoutSupport;

/**
 * The AlignedLogicalPageWrapper corrects all values of the wrapped LogicalPage
 * so that the bounds are aligned for a given LayoutSupport.
 *
 * @author Thomas Morgner
 */
public class AlignedLogicalPageWrapper implements LogicalPage
{
  /** the base logical page which is wrapped by this implementation. */
  private LogicalPage logicalPage;

  /** the layout support implementation which defines the alignments. */
  private LayoutSupport layoutSupport;

  /**
   * Creates a AlignedLogicalPageWrapper, which wraps the logicalpage to obey
   * to the alignment defined by the LayoutSupport.
   *
   * @param logicalPage the logical page that is aligned
   * @param support the layout support which defines the alignment.
   * @throws NullPointerException if one of the parameters is null.
   */
  public AlignedLogicalPageWrapper(final LogicalPage logicalPage, final LayoutSupport support)
  {
    if (logicalPage == null)
    {
      throw new NullPointerException();
    }
    if (support == null)
    {
      throw new NullPointerException();
    }
    this.logicalPage = logicalPage;
    this.layoutSupport = support;
  }

  /**
   * A helper function which alignes the given value along the boundry.
   *
   * @param value the value that should be aligned
   * @param boundary the alignment boundary
   * @return the aligned value
   */
  private float alignDown(final float value, final float boundary)
  {
    if (boundary == 0)
    {
      return value;
    }
    return (float) Math.floor(value / boundary) * boundary;
  }

  /**
   * Returns the page width, aligned by the horizontal alignment of the layout support.
   *
   * @return the page width.
   */
  public float getWidth()
  {
    return alignDown(logicalPage.getWidth(), layoutSupport.getHorizontalAlignmentBorder());
  }

  /**
   * Returns the page height, aligned by the vertical alignment of the layout support.
   *
   * @return the page height.
   */
  public float getHeight()
  {
    return alignDown(logicalPage.getHeight(), layoutSupport.getVerticalAlignmentBorder());
  }

  /**
   * Returns true if the page is open, and false otherwise.
   *
   * @return true or false.
   */
  public boolean isOpen()
  {
    return logicalPage.isOpen();
  }

  /**
   * Returns true if the page is empty, and false otherwise.
   *
   * @return true or false.
   */
  public boolean isEmpty()
  {
    return logicalPage.isEmpty();
  }

  /**
   * Closes the page.
   */
  public void close()
  {
    logicalPage.close();
  }

  /**
   * Opens the page.
   */
  public void open()
  {
    logicalPage.open();
  }

  /**
   * Replays a spool.A spool is a collection of previously prepared content which
   * should be printed later.
   *
   * @param operations the spool that should be replayed.
   */
  public void replaySpool(final Spool operations)
  {
    logicalPage.replaySpool(operations);
  }

  /**
   * Generate a spool. A spool is a collection (or macro) of lowlevel processing instruction
   * on how to distribute a band and its contents. When a spool is recorded, all operations
   * needed to replay the spool later are created and stored within the spool object.
   * <p>
   * The spool can be saved, cloned and replayed at a later time.
   *
   * @param bounds  the bounds.
   * @param band  the band.
   *
   * @throws OutputTargetException if there is a problem with the output target.
   *
   * @return a spool.
   */
  public Spool spoolBand(final Rectangle2D bounds, final Band band) throws OutputTargetException
  {
    return logicalPage.spoolBand(bounds, band);
  }

  /**
   * Adds a band to the logical page.
   *
   * @param bounds  the bounds.
   * @param band  the band.
   *
   * @throws OutputTargetException if there is a problem with the output target.
   */
  public void addBand(final Rectangle2D bounds, final Band band) throws OutputTargetException
  {
    logicalPage.addBand(bounds, band);
  }

  /**
   * Returns the aligned physical page format.
   *
   * @return the physical page format.
   */
  public PageFormat getPhysicalPageFormat()
  {
    final PageFormat pf = (PageFormat) logicalPage.getPhysicalPageFormat().clone();
    final Paper p = pf.getPaper();
    p.setSize(alignDown((float) p.getWidth(), layoutSupport.getHorizontalAlignmentBorder()),
        alignDown((float) p.getHeight(), layoutSupport.getVerticalAlignmentBorder()));
    pf.setPaper(p);
    return pf;
  }

  /**
   * Gets the output target.
   *
   * @return the output target.
   */
  public OutputTarget getOutputTarget()
  {
    return logicalPage.getOutputTarget();
  }

  /**
   * Sets the output target for the page.
   *
   * @param target  the output target.
   */
  public void setOutputTarget(final OutputTarget target)
  {
    logicalPage.setOutputTarget(target);
  }

  /**
   * Creates a new instance of a logical page.
   *
   * @return a logical page.
   */
  public LogicalPage newInstance()
  {
    return new AlignedLogicalPageWrapper(logicalPage.newInstance(), layoutSupport);
  }
}
