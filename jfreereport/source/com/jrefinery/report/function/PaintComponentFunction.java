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
 * ---------------------------
 * PaintComponentFunction.java
 * ---------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PaintComponentFunction.java,v 1.15 2003/06/27 14:25:18 taqua Exp $
 *
 * Changes
 * -------
 * 12-Feb-2003 : Initial version
 * 25-Feb-2003 : BugFixes: Images got lost on pagebreaks ...
 * 26-Feb-2003 : Fixed Checkstyle issues (DG);
 *
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
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import com.jrefinery.report.Element;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.event.LayoutEvent;
import com.jrefinery.report.event.LayoutListener;
import com.jrefinery.report.targets.base.bandlayout.BandLayoutManagerUtil;

/**
 * Paints a AWT or Swing Component, fitting the component into the element bounds.
 * The component must be contained in the dataRow.
 *
 * @author Thomas Morgner
 */
public class PaintComponentFunction extends AbstractFunction
    implements LayoutListener, Serializable
{
  /** Literal text for the 'field' property. */
  public static final String FIELD_PROPERTY = "field";

  /** Literal text for the 'field' property. */
  public static final String ELEMENT_PROPERTY = "element";

  /** Literal text for the 'scale' property. */
  public static final String SCALE_PROPERTY = "scale";

  /** the created image, cached for getValue(). */
  private transient Image image;

  /** supplies a valid peer for the draw operation. */
  private transient Frame peerSupply;

  /**
   * DefaultConstructor.
   */
  public PaintComponentFunction()
  {
    peerSupply = new Frame();
    peerSupply.setLayout(new BorderLayout());
  }

  /**
   * Returns the element used by the function.
   * <P>
   * The element name corresponds to a element in the report. The element name must
   * be unique, as the first occurence of the element is used.
   *
   * @return The field name.
   */
  public String getElement()
  {
    return getProperty(ELEMENT_PROPERTY);
  }

  /**
   * Sets the element name for the function.
   * <P>
   * The element name corresponds to a element in the report. The element name must
   * be unique, as the first occurence of the element is used.
   *
   * @param field  the field name (null not permitted).
   */
  public void setElement(final String field)
  {
    if (field == null)
    {
      throw new NullPointerException();
    }
    setProperty(ELEMENT_PROPERTY, field);
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
  public void setField(final String field)
  {
    if (field == null)
    {
      throw new NullPointerException();
    }
    setProperty(FIELD_PROPERTY, field);
  }

  /**
   * Receives notification that the band layouting has completed.
   * <P>
   * The event carries the current report state.
   *
   * @param event The event.
   */
  public void layoutComplete(final LayoutEvent event)
  {
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

    final Rectangle2D bounds = BandLayoutManagerUtil.getBounds(element, null);
    // no valid layout
    if (bounds.getWidth() <= 0 || bounds.getHeight() <= 0)
    {
      return;
    }

    final Component comp = (Component) o;
    final Dimension dim = new Dimension((int) (bounds.getWidth()), (int) (bounds.getHeight()));
    comp.setSize(dim);

    // supplies the peer and allows drawing ...
    synchronized (peerSupply)
    {
      peerSupply.add(comp, BorderLayout.CENTER);
      peerSupply.pack();
      peerSupply.setSize(dim);
      peerSupply.validate();

      final BufferedImage bi = new BufferedImage((int) (scale * dim.width),
          (int) (scale * dim.height),
          BufferedImage.TYPE_INT_ARGB);
      final Graphics2D graph = bi.createGraphics();
      graph.setTransform(AffineTransform.getScaleInstance(scale, scale));
      comp.paint(graph);
      graph.dispose();
      image = bi;

      peerSupply.remove(comp);
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
      return null;
    }
    final ImageReference ref = new ImageReference(image);
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
  public void setScale(final float scale)
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
    final String scale = getProperty(SCALE_PROPERTY, "1");
    try
    {
      final float f = Float.parseFloat(scale);
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
   * Return a completly separated copy of this function. The copy does no
   * longer share any changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance()
  {
    final PaintComponentFunction pc = (PaintComponentFunction) super.getInstance();
    pc.peerSupply = new Frame();
    pc.peerSupply.setLayout(new BorderLayout());
    return pc;
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
    final String fieldProp = getProperty(FIELD_PROPERTY);
    if (fieldProp == null)
    {
      throw new FunctionInitializeException("No Such Property : field");
    }
    final String elementProp = getProperty(ELEMENT_PROPERTY);
    if (elementProp == null)
    {
      throw new FunctionInitializeException("No Such Property : element");
    }
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
