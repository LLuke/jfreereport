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
 * BarcodePanel.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 * Contributor(s):   Cedric Pronzato;
 *
 * $Id: BarcodePanel.java,v 1.2 2005/06/29 20:52:13 mimil Exp $
 *
 * Changes (from 2005-05-23)
 * -------------------------
 *
 */

package org.jfree.report.dev.barcode.tester;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

import org.jfree.report.dev.barcode.Barcode1D;

public class BarcodePanel extends JPanel
{
  private Barcode1D barcode1D;

  /**
   * Creates a new <code>JPanel</code> with a double buffer and a flow layout.
   */
  public BarcodePanel ()
  {
    super(null);
  }

  /**
   * Create a new buffered JPanel with the specified layout manager
   */
  public BarcodePanel (Barcode1D barcode1D)
  {
    super(null);
    this.barcode1D = barcode1D;
  }

  /**
   * If the <code>preferredSize</code> has been set to a non-<code>null</code> value just
   * returns it. If the UI delegate's <code>getPreferredSize</code> method returns a non
   * <code>null</code> value then return that; otherwise defer to the component's layout
   * manager.
   *
   * @return the value of the <code>preferredSize</code> property
   *
   * @see #setPreferredSize
   * @see javax.swing.plaf.ComponentUI
   */
  public Dimension getPreferredSize ()
  {
    if (barcode1D == null)
    {
      return super.getPreferredSize();
    }
    return barcode1D.getPreferredSize();
  }

  /**
   * Calls the UI delegate's paint method, if the UI delegate is non-<code>null</code>. We
   * pass the delegate a copy of the <code>Graphics</code> object to protect the rest of
   * the paint code from irrevocable changes (for example, <code>Graphics.translate</code>).
   * <p/>
   * If you override this in a subclass you should not make permanent changes to the
   * passed in <code>Graphics</code>. For example, you should not alter the clip
   * <code>Rectangle</code> or modify the transform. If you need to do these operations
   * you may find it easier to create a new <code>Graphics</code> from the passed in
   * <code>Graphics</code> and manipulate it. Further, if you do not invoker super's
   * implementation you must honor the opaque property, that is if this component is
   * opaque, you must completely fill in the background in a non-opaque color. If you do
   * not honor the opaque property you will likely see visual artifacts.
   * <p/>
   * The passed in <code>Graphics</code> object might have a transform other than the
   * identify transform installed on it.  In this case, you might get unexpected results
   * if you cumulatively apply another transform.
   *
   * @param g the <code>Graphics</code> object to protect
   * @see #paint
   * @see javax.swing.plaf.ComponentUI
   */
  protected void paintComponent (Graphics g)
  {
    super.paintComponent(g);
    if (barcode1D != null)
    {
      final Graphics2D graphics = (Graphics2D) g.create();

      barcode1D.draw(graphics, new Rectangle2D.Double(0, 0, getPreferredSize().width, getPreferredSize()
              .height));

      graphics.dispose();
    }
  }

  public Barcode1D getBarcode1D ()
  {
    return barcode1D;
  }

  public void setBarcode1D (Barcode1D barcode1D)
  {
    this.barcode1D = barcode1D;
  }
}
