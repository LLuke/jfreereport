/**
 * Date: Nov 30, 2002
 * Time: 11:21:49 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable;

import com.jrefinery.report.Band;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.pageable.OutputTarget;
import com.jrefinery.report.targets.pageable.Spool;

import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;

public interface LogicalPage
{
  public double getWidth();
  public double getHeight();
  public boolean isOpen();
  public boolean isEmpty();
  public void close();
  public void open();
  public void replaySpool (Spool operations);
  public Spool spoolBand(Rectangle2D bounds, Band band) throws OutputTargetException;
  public void addBand(Rectangle2D bounds, Band band) throws OutputTargetException;
  public PageFormat getPhysicalPageFormat ();
  public OutputTarget getOutputTarget();
  public void setOutputTarget(OutputTarget target);

  public LogicalPage newInstance ();
}
