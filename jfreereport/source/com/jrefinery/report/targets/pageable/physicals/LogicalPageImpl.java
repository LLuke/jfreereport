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
 * $Id: LogicalPageImpl.java,v 1.21 2003/02/08 19:32:06 taqua Exp $
 *
 * Changes
 * -------
 * 02-Dec-2002 : Initial version
 * 04-Dec-2002 : Added Javadocs (DG);
 * 14-Jan-2003 : BugFix: SubBands were not spooled ...
 * 29-Jan-2003 : LogicalPage is closed by default, need explicit open call
 * 01-Feb-2003 : BugFix: SubBand layouting was not translated correctly
 * 07-Feb-2003 : Added explict OperationFactory support
 */

package com.jrefinery.report.targets.pageable.physicals;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.targets.base.ElementLayoutInformation;
import com.jrefinery.report.targets.base.content.Content;
import com.jrefinery.report.targets.base.content.ContentCreationException;
import com.jrefinery.report.targets.base.content.ContentFactory;
import com.jrefinery.report.targets.pageable.LogicalPage;
import com.jrefinery.report.targets.pageable.OutputTarget;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.pageable.Spool;
import com.jrefinery.report.targets.pageable.operations.PhysicalOperation;
import com.jrefinery.report.targets.pageable.operations.OperationFactory;
import com.jrefinery.report.targets.pageable.operations.TextOperationModule;
import com.jrefinery.report.targets.pageable.operations.ImageOperationModule;
import com.jrefinery.report.targets.pageable.operations.ShapeOperationModule;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.ReportConfiguration;

import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.util.List;

/**
 * A simple logical page implementation. Does work with a single physical page
 * and is not yet able to distribute its contents.
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

  private OperationFactory operationFactory;

  /**
   * A flag that indicates whether or not to add comments to the generated
   * physical operations. This is a usefull debugging option, but you won't have
   * fun with it elsewhere.
   */
  private boolean addOperationComments;
  /**
   * Creates a new logical page, where the physical page format is equal to the
   * logical page format.
   *
   * @param format  the page format.
   */
  public LogicalPageImpl(PageFormat format)
  {
    this(format, format);
  }

  /**
   * Creates a new logical page.  This creates a set of physical pages to receive
   * the content generated by the logical page.
   *
   * @param log  the logical page format.
   * @param phys  the physical page format.
   */
  public LogicalPageImpl(PageFormat log, PageFormat phys)
  {
    addOperationComments = ReportConfiguration.getGlobalConfig().isPrintOperationComment();
    closed = true; // logical page is closed by default ..
    operationFactory = createOperationFactory();

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

  public OperationFactory getOperationFactory()
  {
    return operationFactory;
  }

  protected OperationFactory createOperationFactory()
  {
    OperationFactory operationFactory = new OperationFactory();
    operationFactory.registerModule(new TextOperationModule());
    operationFactory.registerModule(new ImageOperationModule());
    operationFactory.registerModule(new ShapeOperationModule());
    return operationFactory;
  }
  /**
   * Returns the physical page at a particular row and column. The logical page
   * can be split into multiple physical pages to display content that would not
   * fit on a single page.
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

  /**
   * Defines the OutputTarget that is used to finally print the content.
   *
   * @param ot the outputTarget, must not be null
   * @throws NullPointerException if the given OutputTarget is null
   */
  public void setOutputTarget (OutputTarget ot)
  {
    if (ot == null)
    {
      throw new NullPointerException();
    }
    this.outputTarget = ot;
  }

  /**
   * Returns the OutputTarget used to display the contents of this logical page.
   *
   * @return the defined OutputTarget
   */
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
    if (operations.isEmpty())
    {
      return;
    }
    replaySpool(operations);
  }

  /**
   * Replays a previously recorded spool. The spool is a collection of PhysicalOperations.
   *
   * @param operations  the operations.
   */
  public void replaySpool (Spool operations)
  {
    PhysicalOperation[] ops = operations.getOperations();
    for (int i = 0; i < ops.length; i++)
    {
      getPhysicalPage(0, 0).addOperation(ops[i]);
    }
  }

  /**
   * Creates a spool made up of the contents generated from the given Band. The band
   * is printed at the location and with the dimensions specified in <code>bounds</code>.
   * <p>
   * Spooling is the process of creating operations suitable for the physical pages.
   * <p>
   * ToDo: Support distribution over multiple pages ...
   *
   * @param bounds the bounds that define where to print the given band on this logical page
   * @param band the band that should be spooled/printed
   * @return the generated spool for the given band
   *
   * @throws OutputTargetException if there is a problem with the output target.
   */
  public Spool spoolBand(Rectangle2D bounds, Band band) throws OutputTargetException
  {
    if (isOpen() == false)
    {
      throw new IllegalStateException("Band already closed");
    }

    Spool spool = new Spool();
    spoolBand(bounds, band, spool);
    return spool;
  }

  protected void spoolBand (Rectangle2D bounds, Band band, Spool spool)
    throws OutputTargetException
  {
    // do nothing if the band is invisble
    if (band.isVisible() == false)
    {
      return;
    }
    // do nothing if the band has a height of 0 (also invisible)
    if (bounds.getHeight() == 0)
    {
      return;
    }

    PageFormat pf = getPageFormat();
    Rectangle2D logicalPageBounds = new Rectangle2D.Double(0, 0, pf.getImageableWidth(),
                                                           pf.getImageableHeight());
    Rectangle2D ibounds = logicalPageBounds.createIntersection(bounds);

    if (addOperationComments)
    {
      spool.addOperation(new PhysicalOperation.AddComment (
          new Log.SimpleMessage("Begin Band: ", band.getClass(), " -> ", band.getName())));
    }

    // process all elements
    List l = band.getElements();
    for (int i = 0; i < l.size(); i++)
    {
      Element e = (Element) l.get(i);
      if (e instanceof Band)
      {
        Rectangle2D bbounds = (Rectangle2D) e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
        spoolBand(translateSubRect(bbounds, bounds), (Band) e, spool);
      }
      else
      {
        addElement(ibounds, e, spool);
      }
    }
  }

  /**
   * Converts an inner rectangle to the coordinate space of the outer rectangle.
   * The inner rectangle's origin (0,0) is mapped to the outer rectangles upper
   * left corner.
   *
   * @param outer the outer rectangle in the global coordinate space
   * @param inner the inner rectangle in the local coordinate space
   * @return the translated sub rectangle.
   */
  private Rectangle2D translateSubRect(Rectangle2D outer, Rectangle2D inner)
  {
    double w = Math.min (outer.getX() + outer.getWidth() - inner.getX(), inner.getWidth());
    double h = Math.min (outer.getY() + outer.getHeight() - inner.getY(), inner.getHeight());
    Rectangle2D rc = new Rectangle2D.Double(
        outer.getX() + inner.getX(),
        outer.getY() + inner.getY(),
        Math.max(0, w),
        Math.max(0, h));

    return rc;
  }

  /**
   * Add the specified element to the logical page. Create content from the values
   * contained in the element and format the content by using the element's attributes.
   * <p>
   * @param bounds  the element bounds.
   * @param e  the element.
   * @param operations  the operations.
   *
   * @throws OutputTargetException if there was content that could not be handled
   * @throws NullPointerException if the element has no valid layout (no BOUNDS defined).
   * Bounds are usually defined by the BandLayoutManager.
   */
  private void addElement(Rectangle2D bounds, Element e, Spool operations)
      throws OutputTargetException
  {
    if (e.isVisible() == false)
    {
      return;
    }
    ContentFactory factory = outputTarget.getContentFactory();
    if (factory.canHandleContent(e.getContentType()) == false)
    {
      Log.debug (new Log.SimpleMessage("The OutputTarget does not support the content type: ", e.getContentType()));
      return;
    }
    Rectangle2D elementBounds = (Rectangle2D)
        e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
    if (elementBounds == null)
    {
      throw new NullPointerException("No layout for element");
    }
    Rectangle2D drawBounds = translateSubRect(bounds, elementBounds);
    if (addOperationComments)
    {
      operations.addOperation(new PhysicalOperation.AddComment ("Begin Element: " + e.getClass()
                              + " -> " + e.getName()));
      operations.addOperation(new PhysicalOperation.AddComment (" ...  Element: " + drawBounds));
    }


    ElementLayoutInformation eli = new ElementLayoutInformation(drawBounds);

    try
    {
      Content content = factory.createContentForElement(e, eli, getOutputTarget());
      // split the elements contents, then write ..

      List opsList = getOperationFactory().createOperations(e, content, drawBounds);
      PhysicalOperation[] ops = new PhysicalOperation[opsList.size()];
      ops = (PhysicalOperation[]) opsList.toArray(ops);
      for (int i = 0; i < ops.length; i++)
      {
        operations.addOperation(ops[i]);
      }
    }
    catch (ContentCreationException ce)
    {
      throw new OutputTargetException("Unable to create content", ce);
    }
  }

  /**
   * Close this logical page and all physical pages. Write the content to the
   * OutputTarget.
   *
   * todo how to handle multiple physical pages? How to specify which page should be printed ...
   */
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

  /**
   * Test whether is logical page is opened and bands can be added to the page.
   *
   * @return true if the page is open, false otherwise
   */
  public boolean isOpen()
  {
    return closed == false;
  }

  /**
   * Opens the logical page. Prepare everything to get bands added.
   */
  public void open ()
  {
    closed = false;
  }

  /**
   * Test whether this page is empty. A logical page is empty, when all
   * physical pages are empty (no operations were executed there).
   *
   * @return true, if the page is empty and nothing was printed, false otherwise.
   */
  public boolean isEmpty ()
  {
    for (int i = 0; i < physicalPage.length; i++)
    {
      if (physicalPage[i].isEmpty() == false)
      {
        return false;
      }
    }
    return true;
  }

  /**
   * Get the width of this logical page. Logical pages start at coordinate (0,0)
   * and have no borders. Borders should be handled by the Physical Pages.
   *
   * @return the defined width of this logical page.
   */
  public double getWidth ()
  {
    return getPageFormat().getImageableWidth();
  }


  /**
   * Get the height of this logical page. Logical pages start at coordinate (0,0)
   * and have no borders. Borders should be handled by the Physical Pages.
   *
   * @return the defined height of this logical page.
   */
  public double getHeight ()
  {
    return getPageFormat().getImageableHeight();
  }

  /**
   * Returns a new instance of this logical page, fully initialized as this page,
   * but without this pages state cloned.
   *
   * @return a new instance of this LogicalPage
   */
  public LogicalPage newInstance()
  {
    return new LogicalPageImpl(getPageFormat(), getPhysicalPageFormat());
  }
}
