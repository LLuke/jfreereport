/**
 * Date: Jan 29, 2003
 * Time: 11:11:17 PM
 *
 * $Id: AlignedLogicalPageWrapper.java,v 1.2 2003/02/02 23:43:52 taqua Exp $
 */
package com.jrefinery.report.targets.pageable;

import com.jrefinery.report.Band;
import com.jrefinery.report.targets.base.layout.LayoutSupport;

import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;

public class AlignedLogicalPageWrapper implements LogicalPage
{
  private LogicalPage logicalPage;
  private LayoutSupport layoutSupport;

  public AlignedLogicalPageWrapper(LogicalPage logicalPage, LayoutSupport support)
  {
    if (logicalPage == null)
      throw new NullPointerException();

    if (support == null)
      throw new NullPointerException();

    this.logicalPage = logicalPage;
    this.layoutSupport = support;
  }

  private double alignDown (double value, double boundry)
  {
    if (boundry == 0)
      return value;

    return Math.floor(value / boundry) * boundry;
  }

  /**
   * Returns the page width.
   *
   * @return the page width.
   */
  public double getWidth()
  {
    return alignDown(logicalPage.getWidth(), layoutSupport.getHorizontalAlignmentBorder());
  }

  /**
   * Returns the page height.
   *
   * @return the page height.
   */
  public double getHeight()
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
  public void replaySpool(Spool operations)
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
   * @throws com.jrefinery.report.targets.pageable.OutputTargetException if there is a problem with the output target.
   *
   * @return a spool.
   */
  public Spool spoolBand(Rectangle2D bounds, Band band) throws OutputTargetException
  {
    return logicalPage.spoolBand(bounds, band);
  }

  /**
   * Adds a band to the logical page.
   *
   * @param bounds  the bounds.
   * @param band  the band.
   *
   * @throws com.jrefinery.report.targets.pageable.OutputTargetException if there is a problem with the output target.
   */
  public void addBand(Rectangle2D bounds, Band band) throws OutputTargetException
  {
    logicalPage.addBand(bounds, band);
  }

  /**
   * Returns the physical page format.
   * todo: Find a better way of handling this. Support different pageformats in one
   * logical Page (BookStyle).
   *
   * @return the physical page format.
   */
  public PageFormat getPhysicalPageFormat()
  {
    return logicalPage.getPhysicalPageFormat();
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
  public void setOutputTarget(OutputTarget target)
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
