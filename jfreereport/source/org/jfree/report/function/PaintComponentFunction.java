/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ---------------------------
 * PaintComponentFunction.java
 * ---------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PaintComponentFunction.java,v 1.20 2005/10/11 14:53:21 taqua Exp $
 *
 * Changes
 * -------
 * 12-Feb-2003 : Initial version
 * 25-Feb-2003 : BugFixes: Images got lost on pagebreaks ...
 * 26-Feb-2003 : Fixed Checkstyle issues (DG);
 *
 *
 */
package org.jfree.report.function;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Window;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.jfree.report.DefaultImageReference;
import org.jfree.report.Element;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.event.LayoutEvent;
import org.jfree.report.event.LayoutListener;
import org.jfree.report.layout.BandLayoutManagerUtil;
import org.jfree.report.util.ImageUtils;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.util.Log;

/**
 * Paints a AWT or Swing Component, fitting the component into the element bounds. The
 * component must be contained in the dataRow.
 * <p/>
 * In an headless environment this function wont work and will always return null.
 *
 * @author Thomas Morgner
 * @deprecated Use the new Component-Element instead. It uses drawables for this
 * job, and therefore the result looks much better.
 */
public class PaintComponentFunction extends AbstractFunction
        implements LayoutListener, Serializable
{
  /**
   * the created image, cached for getValue().
   */
  private transient Image image;

  /**
   * supplies a valid peer for the draw operation.
   */
  private transient Frame peerSupply;
  private String element;
  private String field;
  private float scale;

  /**
   * DefaultConstructor.
   */
  public PaintComponentFunction ()
  {
    if (isHeadless() == false)
    {
      peerSupply = new Frame();
      peerSupply.setLayout(new BorderLayout());
    }
    this.scale = 1;
  }

  /**
   * Returns the element used by the function. <P> The element name corresponds to a
   * element in the report. The element name must be unique, as the first occurence of the
   * element is used.
   *
   * @return The field name.
   */
  public String getElement ()
  {
    return element;
  }

  /**
   * Sets the element name for the function. <P> The element name corresponds to a element
   * in the report. The element name must be unique, as the first occurence of the element
   * is used.
   *
   * @param field the field name (null not permitted).
   */
  public void setElement (final String field)
  {
    if (field == null)
    {
      throw new NullPointerException();
    }
    this.element = field;
  }

  /**
   * Returns the field used by the function. <P> The field name corresponds to a column
   * name in the report's TableModel.
   *
   * @return The field name.
   */
  public String getField ()
  {
    return field;
  }

  /**
   * Sets the field name for the function. <P> The field name corresponds to a column name
   * in the report's TableModel.
   *
   * @param field the field name (null not permitted).
   */
  public void setField (final String field)
  {
    if (field == null)
    {
      throw new NullPointerException();
    }
    this.field = field;
  }

  /**
   * Tests, whether the report generation is executed in an headless environment.
   *
   * @return true, if this is an headless environment, false otherwise.
   */
  protected static boolean isHeadless ()
  {
    return JFreeReportBoot.getInstance().getGlobalConfig().getConfigProperty
            ("java.awt.headless", "false").equals("true");
  }

  /**
   * Receives notification that the band layouting has completed. <P> The event carries
   * the current report state.
   *
   * @param event The event.
   */
  public void layoutComplete (final LayoutEvent event)
  {
    if (isHeadless())
    {
      return;
    }

    // the current value in the dataRow is no AWT-Component ...
    final Object o = getDataRow().get(getField());
    if ((o instanceof Component) == false)
    {
      image = null;
      return;
    }

    // this is not the band with the element in it ...
    final Element element = FunctionUtilities.findElement(event.getLayoutedBand(), getElement());
    if (element == null)
    {
      // don't change/delete the image if already created ...
      return;
    }

    final float scale = getScale();

    final StrictBounds strictBounds = BandLayoutManagerUtil.getBounds(element, null);

    // no valid layout
    if (strictBounds.getWidth() <= 0 || strictBounds.getHeight() <= 0)
    {
      return;
    }

    final Component comp = (Component) o;
    final Dimension2D dim = StrictGeomUtility.createAWTDimension
            (strictBounds.getWidth(), strictBounds.getHeight());
    comp.setSize((int) dim.getWidth(), (int) dim.getHeight());

    if (comp instanceof Window)
    {
      Window window = (Window) comp;
      window.addNotify(); // pack or validate do not work well here

      final BufferedImage bi =
              ImageUtils.createTransparentImage
              ((int) (scale * dim.getWidth()), (int) (scale * dim.getHeight()));
      final Graphics2D graph = bi.createGraphics();
      graph.setBackground(new Color(0, 0, 0, 0));
      graph.setTransform(AffineTransform.getScaleInstance(scale, scale));
      comp.paint(graph);
      graph.dispose();
      image = bi;
    }
    else
    {
      // supplies the peer and allows drawing ...
      peerSupply.pack();
      peerSupply.add(comp, BorderLayout.CENTER);
      peerSupply.setSize((int) dim.getWidth(), (int) dim.getHeight());

      final BufferedImage bi =
              ImageUtils.createTransparentImage
              ((int) (scale * dim.getWidth()), (int) (scale * dim.getHeight()));
      final Graphics2D graph = bi.createGraphics();
      graph.setBackground(new Color(0, 0, 0, 0));
      graph.setTransform(AffineTransform.getScaleInstance(scale, scale));
      comp.paint(graph);
      graph.dispose();
      image = bi;
      peerSupply.remove(comp);
      peerSupply.dispose();
    }
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on the
   * expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue ()
  {
    if (image == null)
    {
      return null;
    }
    try
    {
      final DefaultImageReference ref = new DefaultImageReference(image);
      ref.setScale(1f/ getScale(), 1f/ getScale());
      return ref;
    }
    catch (IOException e)
    {
      Log.warn ("Unable to fully load a given image. (It should not happen here.)");
      return null;
    }
  }

  /**
   * Define a scale factor for the created image. Using a higher scale factor will produce
   * better results. A scale factor of 2 will double the resolution. A scale factor of 1
   * will create 72 dpi images.
   *
   * @param scale the scale factor.
   */
  public void setScale (final float scale)
  {
    this.scale = scale;
  }

  /**
   * Gets the scale factor for the created image. Using a higher scale factor will produce
   * better results. A scale factor of 2 will double the resolution. A scale factor of 1
   * will create 72 dpi images.
   *
   * @return the scale factor.
   */
  public float getScale ()
  {
    return scale;
  }

  /**
   * Return a completly separated copy of this function. The copy does no longer share any
   * changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance ()
  {
    final PaintComponentFunction pc = (PaintComponentFunction) super.getInstance();
    if (isHeadless() == false)
    {
      pc.peerSupply = new Frame();
      pc.peerSupply.setLayout(new BorderLayout());
    }
    return pc;
  }

  /**
   * Helper method for serialization.
   *
   * @param in the input stream from where to read the serialized object.
   * @throws IOException            when reading the stream fails.
   * @throws ClassNotFoundException if a class definition for a serialized object could
   *                                not be found.
   */
  private void readObject (final ObjectInputStream in)
          throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    if (isHeadless() == false)
    {
      peerSupply = new Frame();
      peerSupply.setLayout(new BorderLayout());
    }
  }

  public void outputComplete (final LayoutEvent event)
  {
  }
}
