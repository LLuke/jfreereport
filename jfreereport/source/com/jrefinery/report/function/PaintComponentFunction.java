/**
 * Date: Feb 12, 2003
 * Time: 10:32:57 AM
 *
 * $Id: PaintComponentFunction.java,v 1.3 2003/02/22 18:52:25 taqua Exp $
 */
package com.jrefinery.report.function;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.Group;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.event.LayoutListener;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.targets.base.bandlayout.BandLayoutManagerUtil;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Paints a AWT or Swing Component, fitting the component into the element bounds.
 * The component must be contained in the dataRow.
 */
public class PaintComponentFunction extends AbstractFunction implements LayoutListener
{
  /** Literal text for the 'field' property. */
  public static final String FIELD_PROPERTY = "field";
  /** Literal text for the 'field' property. */
  public static final String ELEMENT_PROPERTY = "element";
  /** Literal text for the 'scale' property. */
  public static final String SCALE_PROPERTY = "scale";

  /** the created image, cached for getValue() */
  private Image image;
  /** the last element found */
  private Element element;

  /**
   * DefaultConstructor
   */
  public PaintComponentFunction()
  {
  }

  /**
   * Try to find the element in the last active root-band.
   *
   * @param band
   * @return
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
   * Receives notification that the report has started.
   *
   * @param event  the event.
   */
  public void reportStarted(ReportEvent event)
  {
    element = findElement(event.getReport().getReportHeader());
  }

  /**
   * Receives notification that the report has finished.
   *
   * @param event  the event.
   */
  public void reportFinished(ReportEvent event)
  {
    element = findElement(event.getReport().getReportFooter());
  }

  /**
   * Receives notification that a page has started.
   *
   * @param event  the event.
   */
  public void pageStarted(ReportEvent event)
  {
    element = findElement(event.getReport().getPageHeader());
  }

  /**
   * Receives notification that a page has ended.
   *
   * @param event  the event.
   */
  public void pageFinished(ReportEvent event)
  {
    element = findElement(event.getReport().getPageFooter());
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event  the event.
   */
  public void groupStarted(ReportEvent event)
  {
    Group g = event.getReport().getGroup(event.getState().getCurrentGroupIndex());
    element = findElement(g.getHeader());
  }

  /**
   * Receives notification that a group has finished.
   *
   * @param event  the event.
   */
  public void groupFinished(ReportEvent event)
  {
    Group g = event.getReport().getGroup(event.getState().getCurrentGroupIndex());
    element = findElement(g.getFooter());
  }

  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event  the event.
   */
  public void itemsAdvanced(ReportEvent event)
  {
    element = findElement(event.getReport().getItemBand());
  }

  /**
   * Receives notification that the band layouting has completed.
   * <P>
   * The event carries the current report state.
   *
   * @param event The event.
   */
  public void layoutComplete(ReportEvent event)
  {
    Object o = getDataRow().get(getField());
    if ((o instanceof Component) == false)
    {
      image = null;
      return;
    }

    if (element == null)
    {
      image = null;
      return;
    }

    float scale = getScale();

    Rectangle2D bounds = BandLayoutManagerUtil.getBounds(element,null);
    Component comp = (Component) o;
    Dimension dim = new Dimension((int) (bounds.getWidth()), (int) (bounds.getHeight()));
    comp.setSize(dim);
    comp.validate();
    BufferedImage bi = new BufferedImage((int)(scale * dim.width),
                                         (int)(scale * dim.height),
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
    if (image == null) return null;
    ImageReference ref = new ImageReference(image);
    ref.setScaleX(1f/getScale());
    ref.setScaleY(1f/getScale());
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
      if (f == 0) return 1;
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
