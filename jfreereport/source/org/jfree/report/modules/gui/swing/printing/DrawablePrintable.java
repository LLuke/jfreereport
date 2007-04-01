/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2000-2007, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * $Id$
 * ------------
 * (C) Copyright 2000-2005, by Object Refinery Limited.
 * (C) Copyright 2005-2007, by Pentaho Corporation.
 */

package org.jfree.report.modules.gui.swing.printing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import org.jfree.ui.Drawable;

/**
 * Creation-Date: 15.11.2006, 22:14:09
 *
 * @author Thomas Morgner
 */
public class DrawablePrintable implements Printable
{
  private Drawable drawable;

  public DrawablePrintable(final Drawable drawable)
  {
    this.drawable = drawable;
  }

  /**
   * Prints the page at the specified index into the specified {@link
   * java.awt.Graphics} context in the specified format.  A
   * <code>PrinterJob</code> calls the <code>Printable</code> interface to
   * request that a page be rendered into the context specified by
   * <code>graphics</code>.  The format of the page to be drawn is specified by
   * <code>pageFormat</code>.  The zero based index of the requested page is
   * specified by <code>pageIndex</code>. If the requested page does not exist
   * then this method returns NO_SUCH_PAGE; otherwise PAGE_EXISTS is returned.
   * The <code>Graphics</code> class or subclass implements the {@link
   * java.awt.print.PrinterGraphics} interface to provide additional
   * information.  If the <code>Printable</code> object aborts the print job
   * then it throws a {@link java.awt.print.PrinterException}.
   *
   * @param graphics   the context into which the page is drawn
   * @param pageFormat the size and orientation of the page being drawn
   * @param pageIndex  the zero based index of the page to be drawn
   * @return PAGE_EXISTS if the page is rendered successfully or NO_SUCH_PAGE if
   *         <code>pageIndex</code> specifies a non-existent page.
   * @throws java.awt.print.PrinterException
   *          thrown when the print job is terminated.
   */
  public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
      throws PrinterException
  {
    if (drawable == null)
    {
      return NO_SUCH_PAGE;
    }

    final Graphics2D g2 = (Graphics2D) graphics;
    final Rectangle2D bounds = new Rectangle2D.Double
        (0,0, pageFormat.getImageableWidth(), pageFormat.getImageableHeight());
    drawable.draw(g2, bounds);
    return PAGE_EXISTS;
  }
}
