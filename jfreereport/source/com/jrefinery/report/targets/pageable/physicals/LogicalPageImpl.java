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
 * --------------------
 * LogicalPageImpl.java
 * --------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LogicalPageImpl.java,v 1.2 2002/12/02 20:43:20 taqua Exp $
 *
 * Changes
 * -------
 * 04-Dec-2002 : Added Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.pageable.physicals;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.targets.pageable.OutputTarget;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.pageable.Spool;
import com.jrefinery.report.targets.pageable.LogicalPage;
import com.jrefinery.report.targets.pageable.contents.Content;
import com.jrefinery.report.targets.pageable.operations.OperationFactory;
import com.jrefinery.report.targets.pageable.operations.OperationModul;
import com.jrefinery.report.targets.pageable.operations.PhysicalOperation;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.util.Log;

import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.util.List;

/**
 * A simple logical page implementation ... does work with a single physical page
 * and is not yet able to distribute its contents ...
 *
 * @author Thomas Morgner
 */
public class LogicalPageImpl implements LogicalPage
{
  /** The output target. */
  private OutputTarget outputTarget;
  
  /** An array of physical pages. */
  private PhysicalPage[] physicalPage;
  
  /** The physical page width. */
  private int physicalPageWidth;
  
  /** The logical page format. */
  private PageFormat pageFormat;
  
  /** The physical page format. */
  private PageFormat physicalPageFormat;

  /** A flag that indicates whether or not the logical page is closed. */
  private boolean closed;

  /**
   * Creates a new logical page.
   *
   * @param format  the page format.
   */
  public LogicalPageImpl(PageFormat format)
  {
    this(format, format);
  }

  /**
   * Creates a new logical page.
   *
   * @param log  the logical page format.
   * @param phys  the physical page format.
   */
  public LogicalPageImpl(PageFormat log, PageFormat phys)
  {
    closed = false;
    setPageFormat(log);
    setPhysicalPageFormat(phys);

    int x = (int) (log.getImageableWidth() / phys.getImageableWidth());
    if (x * phys.getImageableWidth() < log.getImageableWidth())
    {
      x += 1;
    }

    int y = (int) (log.getImageableHeight() / phys.getImageableHeight());
    if (y * phys.getImageableHeight() < log.getImageableHeight())
    {
      y += 1;
    }
    physicalPage = new PhysicalPage[x * y];
    physicalPageWidth = y;
    for (int i = 0; i < physicalPage.length; i++)
    {
      physicalPage[i] = new PhysicalPage(phys, 
                                         new Rectangle2D.Double(0, 0, 
                                                                phys.getImageableWidth(), 
                                                                phys.getImageableHeight()));
    }
  }

  /**
   * Returns the physical page at a particular row and column.
   *
   * @param x ??.
   * @param y ??.
   *
   * @return the physical page.
   */
  public PhysicalPage getPhysicalPage(int x, int y)
  {
    return physicalPage[y * physicalPageWidth + x];
  }

  public void setOutputTarget (OutputTarget ot)
  {
    if (ot == null) throw new NullPointerException();
    this.outputTarget = ot;
  }

  public OutputTarget getOutputTarget()
  {
    return outputTarget;
  }

  /**
   * Returns the logical page format.
   *
   * @return the page format.
   */
  public PageFormat getPageFormat()
  {
    return pageFormat;
  }

  /**
   * Sets the logical page format.
   *
   * @param format  the page format (null not permitted).
   */
  public void setPageFormat(PageFormat format)
  {
    if (format == null) 
    {
      throw new NullPointerException();
    }
    this.pageFormat = format;
  }

  /**
   * Returns the physical page format.
   *
   * @return the page format.
   */
  public PageFormat getPhysicalPageFormat()
  {
    return physicalPageFormat;
  }

  /**
   * Sets the physical page format.
   *
   * @param format  the page format.
   */
  public void setPhysicalPageFormat(PageFormat format)
  {
    if (format == null) 
    {
      throw new NullPointerException();
    }
    this.physicalPageFormat = format;
  }

  /**
   * Add all elements from the band to this logical page. The content is also distributed
   * over the assigned physical pages.
   *
   * @param bounds where to add the band. The bands were calculated by the PageLayouter
   * @param band the band which will be added to the page
   * @throws OutputTargetException if the band addition failed
   */
  public void addBand(Rectangle2D bounds, Band band) throws OutputTargetException
  {
    Spool operations = spoolBand(bounds, band);
    if (operations == null)
    {
      return;
    }
    replaySpool(operations);
  }

  public void replaySpool (Spool operations)
  {
    PhysicalOperation[] ops = operations.getOperations();
    for (int i = 0; i < ops.length; i++)
      getPhysicalPage(0,0).addOperation(ops[i]);
  }

  public Spool spoolBand(Rectangle2D bounds, Band band) throws OutputTargetException
  {
    if (isOpen() == false) throw new IllegalStateException("Band already closed");
    if (band.isVisible() == false)
      return null;

    if (bounds.getHeight() == 0)
      return null;
    
//    Log.debug ("LogicalPage: Band added :  BandType: " + band.getClass() + " bounds: " + bounds);

    PageFormat pf = getPageFormat();
    Rectangle2D logicalPageBounds = new Rectangle2D.Double(0,0, pf.getImageableWidth(), pf.getImageableHeight());
    Rectangle2D ibounds = logicalPageBounds.createIntersection(bounds);

    Spool operations = new Spool();
    operations.addOperation(new PhysicalOperation.AddComment ("Begin Band: " + band.getClass() + " -> " + band.getName()));

    List l = band.getElements();
    for (int i = 0; i < l.size(); i++)
    {
      Element e = (Element) l.get(i);
      if (e instanceof Band)
      {
        Rectangle2D bbounds = (Rectangle2D) e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
        addBand(unionSubRect(bbounds, bounds), (Band) e);
      }
      else
      {
        addElement(ibounds, e, operations);
      }
    }
    return operations;
  }

  private Rectangle2D unionSubRect(Rectangle2D outer, Rectangle2D inner)
  {
    Rectangle2D rt = outer.getBounds2D();

    double w = Math.min (rt.getWidth() - inner.getX(), inner.getWidth());
    double h = Math.min (rt.getHeight() - inner.getY(), inner.getHeight());
    rt.setRect(
        rt.getX() + inner.getX(),
        rt.getY() + inner.getY(),
        Math.max(0, w),
        Math.max(0, h));
    return rt;
  }

  private void addElement(Rectangle2D bounds, Element e, Spool operations) throws OutputTargetException
  {
    if (e.isVisible() == false)
      return;

    OperationModul mod = OperationFactory.getInstance().getModul(e.getContentType());
    if (mod == null)
      throw new OutputTargetException("No handler for content: " + e.getContentType());

    Rectangle2D elementBounds = (Rectangle2D) e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
    if (elementBounds == null)
      throw new NullPointerException("No layout for element");

    Rectangle2D drawBounds = unionSubRect(bounds, elementBounds);
    operations.addOperation(new PhysicalOperation.AddComment ("Begin Element: " + e.getClass() + " -> " + e.getName()));

    Content content = mod.createContentForElement(e, drawBounds, getOutputTarget());
    // split the elements contents, then write ..
    List opsList = mod.createOperations(e, content, drawBounds);
    PhysicalOperation[] ops = new PhysicalOperation[opsList.size()];
    ops = (PhysicalOperation[]) opsList.toArray(ops);

    for (int i = 0; i < ops.length; i++)
    {
      operations.addOperation(ops[i]);
    }
  }

  public void close ()
  {
    for (int i = 0; i < physicalPage.length; i++)
    {
      try
      {
        physicalPage[i].write(getOutputTarget());
      }
      catch (Exception e)
      {
        Log.error ("On CloseLogicalPage" , e);
      }
      physicalPage[i].flush();
    }
    closed = true;
  }

  public boolean isOpen()
  {
    return closed == false;
  }

  public void open ()
  {
    closed = false;
  }

  public boolean isEmpty ()
  {
    for (int i = 0; i < physicalPage.length; i++)
    {
      if (physicalPage[i].isEmpty() == false) return false;
    }
    return true;
  }

  public double getWidth ()
  {
    return getPageFormat().getImageableWidth();
  }

  public double getHeight ()
  {
    return getPageFormat().getImageableHeight();
  }

  public LogicalPage newInstance()
  {
    return new LogicalPageImpl(getPageFormat(), getPhysicalPageFormat());
  }
}
