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
 * --------------------
 * LogicalPageImpl.java
 * --------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LogicalPageImpl.java,v 1.3 2003/07/23 16:02:21 taqua Exp $
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

package org.jfree.report.modules.output.pageable.base.physicals;

import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.content.Content;
import org.jfree.report.content.ContentCreationException;
import org.jfree.report.content.ContentFactory;
import org.jfree.report.modules.output.pageable.base.LogicalPage;
import org.jfree.report.modules.output.pageable.base.OutputTarget;
import org.jfree.report.modules.output.pageable.base.OutputTargetException;
import org.jfree.report.modules.output.pageable.base.Spool;
import org.jfree.report.modules.output.pageable.base.operations.DrawableOperationModule;
import org.jfree.report.modules.output.pageable.base.operations.ImageOperationModule;
import org.jfree.report.modules.output.pageable.base.operations.OperationFactory;
import org.jfree.report.modules.output.pageable.base.operations.PhysicalOperation;
import org.jfree.report.modules.output.pageable.base.operations.ShapeOperationModule;
import org.jfree.report.modules.output.pageable.base.operations.TextOperationModule;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.ElementLayoutInformation;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;

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

  /** The operation factory. */
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
  public LogicalPageImpl(final PageFormat format)
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
  public LogicalPageImpl(final PageFormat log, final PageFormat phys)
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
          new Rectangle2D.Float(0, 0,
              (float) phys.getImageableWidth(),
              (float) phys.getImageableHeight()));
    }
  }

  /**
   * Gets the local instance of the operations factory. The operation factory is
   * responsible for converting content into basic operations, which can be executed
   * on the OutputTarget.
   *
   * @return the local operation factory.
   */
  public OperationFactory getOperationFactory()
  {
    return operationFactory;
  }

  /**
   * Initializes the operations factory. The operation factory is responsible for
   * converting content into basic operations, which can be executed on the OutputTarget.
   *
   * @return the local operation factory.
   */
  protected OperationFactory createOperationFactory()
  {
    final OperationFactory operationFactory = new OperationFactory();
    operationFactory.registerModule(new TextOperationModule());
    operationFactory.registerModule(new ImageOperationModule());
    operationFactory.registerModule(new ShapeOperationModule());
    operationFactory.registerModule(new DrawableOperationModule());
    return operationFactory;
  }

  /**
   * Returns the physical page at a particular row and column of the PageGrid.
   * The logical page can be split into multiple physical pages to display content
   * that would not fit on a single page.
   *
   * @param x the X-Coordinate in the page grid.
   * @param y the Y-Coordinate in the page grid.
   *
   * @return the physical page.
   */
  public PhysicalPage getPhysicalPage(final int x, final int y)
  {
    return physicalPage[y * physicalPageWidth + x];
  }

  /**
   * Defines the OutputTarget that is used to finally print the content.
   *
   * @param ot the outputTarget, must not be null
   * @throws NullPointerException if the given OutputTarget is null
   */
  public void setOutputTarget(final OutputTarget ot)
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
  public void setPageFormat(final PageFormat format)
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
  public void setPhysicalPageFormat(final PageFormat format)
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
  public void addBand(final Rectangle2D bounds, final Band band) throws OutputTargetException
  {
    final Spool operations = spoolBand(bounds, band);
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
  public void replaySpool(final Spool operations)
  {
    final PhysicalOperation[] ops = operations.getOperations();
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
  public Spool spoolBand(final Rectangle2D bounds, final Band band) throws OutputTargetException
  {
    if (isOpen() == false)
    {
      throw new IllegalStateException("Band already closed");
    }

    final Spool spool = new Spool();
    spoolBand(bounds, band, spool);
    return spool;
  }

  /**
   * Creates a spool made up of the contents generated from the given Band. The band
   * is printed at the location and with the dimensions specified in <code>bounds</code>.
   * <p>
   * Spooling is the process of creating operations suitable for the physical pages.
   * The generated operations are added to the given spool.
   * <p>
   * ToDo: Support distribution over multiple pages ...
   *
   * @param bounds the bounds that define where to print the given band on this logical page
   * @param band the band that should be spooled/printed
   * @param spool the spool which collects the generated operations.
   *
   * @throws OutputTargetException if there is a problem with the output target.
   */
  protected void spoolBand(final Rectangle2D bounds, final Band band, final Spool spool)
      throws OutputTargetException
  {
    // do nothing if the band is invisble
    if (band.isVisible() == false)
    {
      //Log.debug ("The Band is not visible!");
      return;
    }
    // do nothing if the band has a height of 0 (also invisible)
    if (bounds.getHeight() == 0)
    {
      //Log.debug ("The Band has a height of 0!");
      return;
    }

    final PageFormat pf = getPageFormat();
    final Rectangle2D logicalPageBounds = new Rectangle2D.Float(0, 0,
        (float) pf.getImageableWidth(),
        (float) pf.getImageableHeight());
    final Rectangle2D ibounds = logicalPageBounds.createIntersection(bounds);

    if (addOperationComments)
    {
      spool.addOperation(new PhysicalOperation.AddComment(
          new Log.SimpleMessage("Begin Band: ", band.getClass(), " -> ", band.getName())));
    }

    // process all elements
    final Element[] elements = band.getElementArray();
    for (int i = 0; i < elements.length; i++)
    {
      final Element e = elements[i];
      if (e instanceof Band)
      {
        final Rectangle2D bbounds = (Rectangle2D) 
          e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
        spoolBand(translateSubRect(bounds, bbounds), (Band) e, spool);
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
  private Rectangle2D translateSubRect(final Rectangle2D outer, final Rectangle2D inner)
  {
    final float w = 
      (float) Math.min(outer.getX() + outer.getWidth() - inner.getX(), inner.getWidth());
    final float h = 
      (float) Math.min(outer.getY() + outer.getHeight() - inner.getY(), inner.getHeight());
    final Rectangle2D rc = new Rectangle2D.Float(
        (float) (outer.getX() + inner.getX()),
        (float) (outer.getY() + inner.getY()),
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
  private void addElement(final Rectangle2D bounds, final Element e, final Spool operations)
      throws OutputTargetException
  {
    if (e.isVisible() == false)
    {
      //Log.debug ("The Element " + e + " is not visible");
      return;
    }
    final ContentFactory factory = outputTarget.getContentFactory();
    if (factory.canHandleContent(e.getContentType()) == false)
    {
      /*
      Log.debug (new Log.SimpleMessage("The OutputTarget does not support the content type: ",
                                       e.getContentType()));
                                       */
      return;
    }
    final Rectangle2D elementBounds = (Rectangle2D)
        e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
    if (elementBounds == null)
    {
      throw new NullPointerException("No layout for element");
    }
    final Rectangle2D drawBounds = translateSubRect(bounds, elementBounds);
    if (addOperationComments)
    {
      operations.addOperation(new PhysicalOperation.AddComment("Begin Element: " + e.getClass()
          + " -> " + e.getName()));
      operations.addOperation(new PhysicalOperation.AddComment(" ...  Element: " + drawBounds));
    }

    final ElementLayoutInformation eli = new ElementLayoutInformation(drawBounds);

    try
    {
      final Content content = factory.createContentForElement(e, eli, getOutputTarget());
      if (content == null)
      {
        // Log.debug ("Content is null, no content created?");
        return;
      }
      // split the elements contents, then write ..
      getOperationFactory().createOperations(operations, e, content, drawBounds);
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
  public void close()
  {
    for (int i = 0; i < physicalPage.length; i++)
    {
      try
      {
        physicalPage[i].write(getOutputTarget());
      }
      catch (Exception e)
      {
        Log.error("On CloseLogicalPage", e);
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
  public void open()
  {
    closed = false;
  }

  /**
   * Test whether this page is empty. A logical page is empty, when all
   * physical pages are empty (no operations were executed there).
   *
   * @return true, if the page is empty and nothing was printed, false otherwise.
   */
  public boolean isEmpty()
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
  public float getWidth()
  {
    return (float) getPageFormat().getImageableWidth();
  }


  /**
   * Get the height of this logical page. Logical pages start at coordinate (0,0)
   * and have no borders. Borders should be handled by the Physical Pages.
   *
   * @return the defined height of this logical page.
   */
  public float getHeight()
  {
    return (float) getPageFormat().getImageableHeight();
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
