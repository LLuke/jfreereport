/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * $Id: PaintComponentFunction.java,v 1.7 2003/02/26 13:57:57 mungady Exp $
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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.event.LayoutEvent;
import com.jrefinery.report.event.LayoutListener;
import com.jrefinery.report.targets.base.bandlayout.BandLayoutManagerUtil;

/**
 * Paints a AWT or Swing Component, fitting the component into the element bounds.
 * The component must be contained in the dataRow.
 * 
 * @author Thomas Morgner
 */
public class PaintComponentFunction extends AbstractFunction implements LayoutListener
{
  /** Literal text for the 'field' property. */
  public static final String FIELD_PROPERTY = "field";
  
  /** Literal text for the 'field' property. */
  public static final String ELEMENT_PROPERTY = "element";
  
  /** Literal text for the 'scale' property. */
  public static final String SCALE_PROPERTY = "scale";

  /** the created image, cached for getValue(). */
  private Image image;

  /**
   * DefaultConstructor.
   */
  public PaintComponentFunction()
  {
  }

  /**
   * Try to find the defined element in the last active root-band.
   *
   * @param band the band that is suspected to contain the element.
   * @return the found element or null, if no element could be found.
   */
  private Element findElement (Band band)
  {
    Element[] elements = band.getElementArray();
    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];
      if (e instanceof Band)
      {
        return findElement((Band) e);
      }
      else if (e.getName().equals(getElement()))
      {
        return e;
      }
    }
    Log.debug ("Element not found in " + band);
    return null;
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
  public void setElement(String field)
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
  public void setField(String field)
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
  public void layoutComplete(LayoutEvent event)
  {
    // the current value in the dataRow is no AWT-Component ...
    Object o = getDataRow().get(getField());
    if ((o instanceof Component) == false)
    {
      image = null;
      Log.debug ("Is no Component");
      return;
    }

    // this is not the band with the element in it ...
    Element element = findElement(event.getLayoutedBand());
    if (element == null)
    {
      // don't change/delete the image if already created ...
      Log.debug ("Element not found");
      return;
    }
    Log.debug ("Element found");

    float scale = getScale();

    Rectangle2D bounds = BandLayoutManagerUtil.getBounds(element, null);
    // no valid layout
    if (bounds.getWidth() <= 0 || bounds.getHeight() <= 0)
    {
      return;
    }

    Component comp = (Component) o;
    Dimension dim = new Dimension((int) (bounds.getWidth()), (int) (bounds.getHeight()));
    comp.setSize(dim);
    comp.validate();
    if (comp.getParent() != null)
    {
      // if the parent is not visible, the component will not be painted...
      Log.info ("This component has a parent, this may influence the paint process.");
    }
    // this may be deprecated, but it is the only way to check whether the
    // component has a peer ...
    if (comp.getPeer() == null)
    {
      Log.warn ("This component has no peer, and may be invisible. This could prevent painting.");
    }
    if (comp.isShowing() == false)
    {
      // isShowing == false means,that nothing is painted ...
      Log.warn ("This component is not visible and may deny painting.");
    }

    BufferedImage bi = new BufferedImage((int) (scale * dim.width),
                                         (int) (scale * dim.height),
                                         BufferedImage.TYPE_INT_ARGB);
    Graphics2D graph = bi.createGraphics();
    graph.setTransform(AffineTransform.getScaleInstance(scale, scale));
    comp.paint(graph);
    graph.dispose();
    image = bi;
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
      Log.debug ("No Image created ..");
      return null;
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
  public void setScale (float scale)
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
  public float getScale ()
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
    String elementProp = getProperty(ELEMENT_PROPERTY);
    if (elementProp == null)
    {
      throw new FunctionInitializeException("No Such Property : element");
    }
  }
}
