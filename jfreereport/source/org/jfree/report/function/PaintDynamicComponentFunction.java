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
 * ----------------------------------
 * PaintDynamicComponentFunction.java
 * ----------------------------------
 * (C)opyright 2002, 2003, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PaintDynamicComponentFunction.java,v 1.8 2005/01/25 00:00:18 taqua Exp $
 *
 * Changes
 * -------
 * 12-Feb-2003 : Initial version
 *
 */
package org.jfree.report.function;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.jfree.report.DefaultImageReference;
import org.jfree.report.event.PageEventListener;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.util.ImageUtils;

/**
 * Paints a AWT or Swing Component. The component must be contained in the
 * dataRow.
 *
 * @author Thomas Morgner
 */
public class PaintDynamicComponentFunction extends AbstractFunction
    implements Serializable, PageEventListener
{
  /** the created image, cached for getValue(). */
  private transient Image image;

  /** supplies a valid peer for the draw operation. */
  private transient Frame peerSupply;

  private String field;
  private float scale;

  /**
   * DefaultConstructor.
   *
   * @throws IllegalStateException (HeadlessException) if no full
   * AWT is available. This function needs a working layout manager.
   */
  public PaintDynamicComponentFunction()
  {
    peerSupply = new Frame();
    peerSupply.setLayout(new BorderLayout());
    scale = 1;
  }

  /**
   * Returns the field used by the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @return The field name.
   */
  public String getField()
  {
    return field;
  }

  /**
   * Sets the field name for the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @param field  the field name (null not permitted).
   */
  public void setField(final String field)
  {
    this.field = field;
  }

  /**
   * Receives notification that the report has started.
   *
   * @param event  the event.
   */
  public void reportStarted(final ReportEvent event)
  {
    image = null;
  }

  /**
   * Receives notification that report generation initializes the current run.
   * <P>
   * The event carries a ReportState.Started state.  Use this to initialize the report.
   *
   * @param event The event.
   */
  public void reportInitialized(final ReportEvent event)
  {
    image = null;
  }

  /**
   * Receives notification that the report has finished.
   *
   * @param event  the event.
   */
  public void reportFinished(final ReportEvent event)
  {
    image = null;
  }

  /**
   * Receives notification that a page has started.
   *
   * @param event  the event.
   */
  public void pageStarted(final ReportEvent event)
  {
    image = null;
  }

  /**
   * Receives notification that a page has ended.
   *
   * @param event  the event.
   */
  public void pageFinished(final ReportEvent event)
  {
    image = null;
  }

  /**
   * Receives notification that a page was canceled by the ReportProcessor.
   * This method is called, when a page was removed from the report after
   * it was generated.
   *
   * @param event The event.
   */
  public void pageCanceled(final ReportEvent event)
  {
    image = null;
  }

  /**
   * This event is fired, whenever an automatic pagebreak has been detected and the report
   * state had been reverted to the previous state.
   *
   * @param event
   */
  public void pageRolledBack (final ReportEvent event)
  {
    image = null;
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event  the event.
   */
  public void groupStarted(final ReportEvent event)
  {
    image = null;
  }

  /**
   * Receives notification that a group has finished.
   *
   * @param event  the event.
   */
  public void groupFinished(final ReportEvent event)
  {
    image = null;
  }

  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event  the event.
   */
  public void itemsAdvanced(final ReportEvent event)
  {
    image = null;
  }

  /**
   * Creates the component.
   *
   * @return the created image or null, if no image could be created.
   */
  private Image createComponentImage()
  {
    final Object o = getDataRow().get(getField());
    if ((o instanceof Component) == false)
    {
      return null;
    }

    final float scale = getScale();

    final Component comp = (Component) o;

    // supplies the peer and allows drawing ...
    synchronized (peerSupply)
    {
      final Dimension dim = comp.getSize();

      peerSupply.add(comp);
      peerSupply.setSize(dim);
      peerSupply.validate();

      final BufferedImage bi =
          ImageUtils.createTransparentImage
          ((int) scale * dim.width, (int) scale * dim.height);
      final Graphics2D graph = bi.createGraphics();
      graph.setBackground(new Color (0,0,0,0));
      graph.setTransform(AffineTransform.getScaleInstance(scale, scale));
      comp.paint(graph);
      graph.dispose();
      return bi;
    }
  }

  /**
   * Return the current expression value.
   * <P>
   * The value depends (obviously) on the expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {
    if (image == null)
    {
      image = createComponentImage();
    }

    final DefaultImageReference ref = new DefaultImageReference(image);
    ref.setScale(1f / getScale(), 1f / getScale());
    return ref;
  }

  /**
   * Define a scale factor for the created image. Using a higher scale factor
   * will produce better results. A scale factor of 2 will double the resolution.
   * A scale factor of 1 will create 72 dpi images.
   *
   * @param scale the scale factor.
   */
  public void setScale(final float scale)
  {
    this.scale = scale;
  }


  /**
   * Gets the scale factor for the created image. Using a higher scale factor
   * will produce better results. A scale factor of 2 will double the resolution.
   * A scale factor of 1 will create 72 dpi images.
   *
   * @return the scale factor.
   */
  public float getScale()
  {
    return scale;
  }

  /**
   * Initializes the function and tests that all required properties are set. If the required
   * field property is not set, a FunctionInitializeException is thrown.
   *
   * @throws FunctionInitializeException when no field is set.
   */
  public void initialize()
      throws FunctionInitializeException
  {
    if (field == null)
    {
      throw new FunctionInitializeException("No Such Property : field");
    }
  }

  /**
   * Return a completly separated copy of this function. The copy does no
   * longer share any changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance()
  {
    final PaintDynamicComponentFunction pc = (PaintDynamicComponentFunction) super.getInstance();
    pc.peerSupply = new Frame();
    pc.peerSupply.setLayout(new BorderLayout());
    return pc;
  }


  /**
   * Helper method for serialization.
   *
   * @param in the input stream from where to read the serialized object.
   * @throws IOException when reading the stream fails.
   * @throws ClassNotFoundException if a class definition for a serialized object
   * could not be found.
   */
  private void readObject(final ObjectInputStream in)
      throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    peerSupply = new Frame();
    peerSupply.setLayout(new BorderLayout());
  }
}
