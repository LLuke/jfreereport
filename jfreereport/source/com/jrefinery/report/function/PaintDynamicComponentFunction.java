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
 * ----------------------------------
 * PaintDynamicComponentFunction.java
 * ----------------------------------
 * (C)opyright 2002, 2003, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PaintDynamicComponentFunction.java,v 1.12 2003/06/19 18:44:09 taqua Exp $
 *
 * Changes
 * -------
 * 12-Feb-2003 : Initial version
 *
 */
package com.jrefinery.report.function;

import java.awt.BorderLayout;
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

import com.jrefinery.report.ImageReference;
import com.jrefinery.report.event.ReportEvent;

/**
 * Paints a AWT or Swing Component. The component must be contained in the
 * dataRow.
 *
 * @author Thomas Morgner
 */
public class PaintDynamicComponentFunction extends AbstractFunction implements Serializable
{
  /** Literal text for the 'field' property. */
  public static final String FIELD_PROPERTY = "field";

  /** Literal text for the 'scale' property. */
  public static final String SCALE_PROPERTY = "scale";

  /** the created image, cached for getValue(). */
  private transient Image image;

  /** supplies a valid peer for the draw operation. */
  private transient Frame peerSupply;

  /**
   * DefaultConstructor.
   */
  public PaintDynamicComponentFunction()
  {
    peerSupply = new Frame();
    peerSupply.setLayout(new BorderLayout());
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
    return getProperty(FIELD_PROPERTY);
  }

  /**
   * Sets the field name for the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @param field  the field name (null not permitted).
   */
  public void setField(String field)
  {
    if (field == null)
    {
      throw new NullPointerException();
    }
    setProperty(FIELD_PROPERTY, field);
  }

  /**
   * Receives notification that the report has started.
   *
   * @param event  the event.
   */
  public void reportStarted(ReportEvent event)
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
  public void reportInitialized(ReportEvent event)
  {
    image = null;
  }

  /**
   * Receives notification that the report has finished.
   *
   * @param event  the event.
   */
  public void reportFinished(ReportEvent event)
  {
    image = null;
  }

  /**
   * Receives notification that a page has started.
   *
   * @param event  the event.
   */
  public void pageStarted(ReportEvent event)
  {
    image = null;
  }

  /**
   * Receives notification that a page has ended.
   *
   * @param event  the event.
   */
  public void pageFinished(ReportEvent event)
  {
    image = null;
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event  the event.
   */
  public void groupStarted(ReportEvent event)
  {
    image = null;
  }

  /**
   * Receives notification that a group has finished.
   *
   * @param event  the event.
   */
  public void groupFinished(ReportEvent event)
  {
    image = null;
  }

  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event  the event.
   */
  public void itemsAdvanced(ReportEvent event)
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
    Object o = getDataRow().get(getField());
    if ((o instanceof Component) == false)
    {
      return null;
    }

    float scale = getScale();

    Component comp = (Component) o;

    // supplies the peer and allows drawing ...
    synchronized (peerSupply)
    {
      Dimension dim = comp.getSize();

      peerSupply.add(comp);
      peerSupply.pack();
      peerSupply.setSize(dim);
      peerSupply.validate();

      BufferedImage bi = new BufferedImage((int) (scale * dim.width),
          (int) (scale * dim.height),
          BufferedImage.TYPE_INT_ARGB);
      Graphics2D graph = bi.createGraphics();
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

    ImageReference ref = new ImageReference(image);
    ref.setScaleX(1f / getScale());
    ref.setScaleY(1f / getScale());
    return ref;
  }

  /**
   * Define a scale factor for the created image. Using a higher scale factor
   * will produce better results. A scale factor of 2 will double the resolution.
   * A scale factor of 1 will create 72 dpi images.
   *
   * @param scale the scale factor.
   */
  public void setScale(float scale)
  {
    setProperty(SCALE_PROPERTY, String.valueOf(scale));
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
    String scale = getProperty(SCALE_PROPERTY, "1");
    try
    {
      float f = Float.parseFloat(scale);
      if (f == 0)
      {
        return 1;
      }
      return f;
    }
    catch (Exception e)
    {
      return 1;
    }
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
    String fieldProp = getProperty(FIELD_PROPERTY);
    if (fieldProp == null)
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
    PaintDynamicComponentFunction pc = (PaintDynamicComponentFunction) super.getInstance();
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
  private void readObject(ObjectInputStream in)
      throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    peerSupply = new Frame();
    peerSupply.setLayout(new BorderLayout());
  }
}
