/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * ----------------
 * LogicalPage.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Content.java,v 1.1 2002/12/02 17:56:54 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 *
 */
package com.jrefinery.report.targets.pageable;

import com.jrefinery.report.Band;

import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;

/**
 * An interface that defines a logical page.
 *
 * @author Thomas Morgner.
 */
public interface LogicalPage
{
  /**
   * Returns the page width.
   *
   * @return the page width.
   */
  public double getWidth();
  
  /**
   * Returns the page height.
   *
   * @return the page height.
   */
  public double getHeight();
  
  /**
   * Returns true if the page is open, and false otherwise.
   *
   * @return true or false.
   */
  public boolean isOpen();
  
  /**
   * Returns true if the page is empty, and false otherwise.
   *
   * @return true or false.
   */
  public boolean isEmpty();
  
  /**
   * Closes the page. 
   */
  public void close();
  
  /**
   * Opens the page.
   */
  public void open();
  
  /**
   * Replays a spool.
   *
   * @param operations ??.
   */
  public void replaySpool (Spool operations);
  
  /**
   * ??.
   *
   * @param bounds  the bounds.
   * @param band  the band.
   *
   * @throws OutputTargetException if there is a problem with the output target.
   *
   * @return a spool.
   */
  public Spool spoolBand(Rectangle2D bounds, Band band) throws OutputTargetException;
  
  /**
   * Adds a band to the logical page.
   *
   * @param bounds  the bounds.
   * @param band  the band.
   *
   * @throws OutputTargetException if there is a problem with the output target.
   */
  public void addBand(Rectangle2D bounds, Band band) throws OutputTargetException;
  
  /**
   * Returns the physical page format.
   *
   * @return the physical page format.
   */
  public PageFormat getPhysicalPageFormat ();
  
  /**
   * Gets the output target.
   *
   * @return the output target.
   */
  public OutputTarget getOutputTarget();
  
  /**
   * Sets the output target for the page.
   *
   * @param target  the output target.
   */
  public void setOutputTarget(OutputTarget target);

  /**
   * Creates a new instance of a logical page.
   *
   * @return a logical page.
   */
  public LogicalPage newInstance ();
  
}
