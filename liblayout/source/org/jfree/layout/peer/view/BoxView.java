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
 * ${name}
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Boot.java,v 1.6 2003/11/23 16:50:45 taqua Exp $
 *
 * Changes
 * -------------------------
 * 14-May-2004 : Initial version
 *
 */
package org.jfree.layout.peer.view;

import org.jfree.layout.doc.DocumentNode;
import org.jfree.layout.peer.LayoutInformation;

/**
 * Handles block elements. This implementation assumes, that all
 * elements of the given container are BLOCK elements.
 * <p>
 * Mixing block and inline elements is considered invalid. However,
 * inline elements can always be placed into a container contained in
 * a block element view.  
 */
public class BoxView extends AbstractCompositeView
{
  protected class SizeRequirements
  {
    private float minimum;
    private float preferred;
    private float maximum;

    public SizeRequirements ()
    {
    }

    public SizeRequirements (final float minimum, final float preferred,
                             final float maximum)
    {
      this.minimum = minimum;
      this.preferred = preferred;
      this.maximum = maximum;
    }

    public float getMaximum ()
    {
      return maximum;
    }

    public void setMaximum (final float maximum)
    {
      this.maximum = maximum;
    }

    public float getMinimum ()
    {
      return minimum;
    }

    public void setMinimum (final float minimum)
    {
      this.minimum = minimum;
    }

    public float getPreferred ()
    {
      return preferred;
    }

    public void setPreferred (final float preferred)
    {
      this.preferred = preferred;
    }
  }

  private int majorAxis;
  private int minorAxis;
  private float[] majorAxisOffsets;
  private float[] majorAxisSpans;
  private float[] minorAxisOffsets;
  private float[] minorAxisSpans;
  private SizeRequirements minorAxisRequirements;
  private SizeRequirements majorAxisRequirements;

  public BoxView (final DocumentNode node, final int majorAxis)
  {
    super(node);
    this.majorAxis = majorAxis;
    if (majorAxis == X_AXIS)
    {
      minorAxis = Y_AXIS;
    }
    else
    {
      minorAxis = X_AXIS;
    }
    this.majorAxisOffsets = new float[0];
    this.majorAxisSpans = new float[0];
    this.minorAxisOffsets = new float[0];
    this.minorAxisSpans = new float[0];
  }

  public float getMaximumSpan (final int axis)
  {
    final SizeRequirements requirements;
    if (axis == getMajorAxis())
    {
      majorAxisRequirements =
        calculateMajorAxisRequirements(axis, majorAxisRequirements);
      requirements = majorAxisRequirements;
    }
    else
    {
      minorAxisRequirements =
        calculateMinorAxisRequirements(axis, minorAxisRequirements);
      requirements = minorAxisRequirements;
    }

    final float size = requirements.getMaximum();
    computeBorders();

    if (axis == NodeView.X_AXIS)
    {
      final LayoutInformation li = getLayoutInformation();
      return size + li.getPaddingLeft() + li.getPaddingRight() +
              li.getMarginLeft() + li.getMarginRight();
    }
    else
    {
      final LayoutInformation li = getLayoutInformation();
      return size + li.getPaddingTop() + li.getPaddingBottom() +
              li.getMarginTop() + li.getMarginBottom();
    }
  }

  protected SizeRequirements calculateMinorAxisRequirements
          (final int axis, final SizeRequirements sizeRequirements)
  {
    float minimum = 0;
    float maximum = 0;
    float preferred = 0;
    for (int i = 0; i < getViewCount(); i++)
    {
      final NodeView view = getView(i);
      minimum = Math.max (view.getMinimumSpan(axis), minimum);
      maximum = Math.max (view.getMaximumSpan(axis), maximum);
      preferred = Math.max (view.getPreferredSpan(axis), preferred);
    }
    if (sizeRequirements == null)
    {
      return new SizeRequirements(minimum, preferred, maximum);
    }
    sizeRequirements.setMinimum(minimum);
    sizeRequirements.setMaximum(maximum);
    sizeRequirements.setPreferred(preferred);
    return sizeRequirements;
  }


  protected SizeRequirements calculateMajorAxisRequirements
          (final int axis, final SizeRequirements sizeRequirements)
  {
    float minimum = 0;
    float maximum = 0;
    float preferred = 0;
    for (int i = 0; i < getViewCount(); i++)
    {
      final NodeView view = getView(i);
      minimum += view.getMinimumSpan(axis);
      maximum += view.getMaximumSpan(axis);
      preferred += view.getPreferredSpan(axis);
    }
    if (sizeRequirements == null)
    {
      return new SizeRequirements(minimum, preferred, maximum);
    }
    sizeRequirements.setMinimum(minimum);
    sizeRequirements.setMaximum(maximum);
    sizeRequirements.setPreferred(preferred);
    return sizeRequirements;
  }

  public float getMinimumSpan (final int axis)
  {
    final SizeRequirements requirements;
    if (axis == getMajorAxis())
    {
      majorAxisRequirements =
        calculateMajorAxisRequirements(axis, majorAxisRequirements);
      requirements = majorAxisRequirements;
    }
    else
    {
      minorAxisRequirements =
        calculateMinorAxisRequirements(axis, minorAxisRequirements);
      requirements = minorAxisRequirements;
    }

    final float size = requirements.getMinimum();
    computeBorders();
    if (axis == NodeView.X_AXIS)
    {
      final LayoutInformation li = getLayoutInformation();
      return size + li.getPaddingLeft() + li.getPaddingRight() +
              li.getMarginLeft() + li.getMarginRight();
    }
    else
    {
      final LayoutInformation li = getLayoutInformation();
      return size + li.getPaddingTop() + li.getPaddingBottom() +
              li.getMarginTop() + li.getMarginBottom();
    }
  }

  public float getPreferredSpan (final int axis)
  {
    final SizeRequirements requirements;
    if (axis == getMajorAxis())
    {
      majorAxisRequirements =
        calculateMajorAxisRequirements(axis, majorAxisRequirements);
      requirements = majorAxisRequirements;
    }
    else
    {
      minorAxisRequirements =
        calculateMinorAxisRequirements(axis, minorAxisRequirements);
      requirements = minorAxisRequirements;
    }

    final float size = requirements.getPreferred();

    computeBorders();
    if (axis == NodeView.X_AXIS)
    {
      final LayoutInformation li = getLayoutInformation();
      return size + li.getPaddingLeft() + li.getPaddingRight() +
              li.getMarginLeft() + li.getMarginRight();
    }
    else
    {
      final LayoutInformation li = getLayoutInformation();
      return size + li.getPaddingTop() + li.getPaddingBottom() +
              li.getMarginTop() + li.getMarginBottom();
    }
  }

  protected void doLayout ()
  {
    // update cache size...
    final int viewSize = getViewCount();
    if (isLayoutValid() == false)
    {
      majorAxisSpans = new float[viewSize];
      majorAxisOffsets = new float[viewSize];

      minorAxisSpans = new float[viewSize];
      minorAxisOffsets = new float[viewSize];
    }

    computeBorders();
    final LayoutInformation li = getLayoutInformation();

    if (majorAxis == NodeView.X_AXIS)
    {
      doMajorAxisLayout(li.getContentX(), li.getContentWidth(),
              NodeView.X_AXIS, majorAxisOffsets, majorAxisSpans);
      doMinorAxisLayout(li.getContentY(), li.getContentHeight(),
              NodeView.Y_AXIS, minorAxisOffsets, minorAxisSpans);
      updateLayout(minorAxisOffsets, majorAxisOffsets, minorAxisSpans, majorAxisSpans);
    }
    else
    {
      doMajorAxisLayout(li.getContentY(), li.getContentHeight(),
              NodeView.Y_AXIS, majorAxisOffsets, majorAxisSpans);
      doMinorAxisLayout(li.getContentX(), li.getContentWidth(),
              NodeView.X_AXIS, minorAxisOffsets, minorAxisSpans);
      updateLayout(majorAxisOffsets, minorAxisOffsets, majorAxisSpans, minorAxisSpans);
    }

    validated(majorAxis);
    validated(minorAxis);
  }

  protected void performMajorAxisLayout ()
  {
    doLayout();
  }

  protected void performMinorAxisLayout ()
  {
    invalidateLayout();
  }

  private void updateLayout (final float[] x, final float[] y,
                             final float[] width, final float[] height)
  {
    for (int i = 0; i < getViewCount(); i++)
    {
      final NodeView view = getView(i);
      view.setLocation(x[i], y[i]);
      view.setSpanSize(X_AXIS, width[i]);
      view.setSpanSize(Y_AXIS, height[i]);
    }
  }

  private void doMajorAxisLayout
          (final float fixedOffset, final float size, final int axis,
           final float[] offsets, final float[] spans )
  {
    float min = 0;
    float max = 0;
    float preferred = 0;

    for (int i = 0; i < getViewCount(); i++)
    {
      final NodeView view = getView(i);
      spans[i] = view.getPreferredSpan(axis);
      preferred += spans[i];
      min += view.getMinimumSpan(axis);
      max += view.getMaximumSpan(axis);
    }

    final float padding = size - preferred;
    float adjustmentFactor = 0;
    if (padding != 0)
    {
      if (padding < 0)
      {
        // not enough space to print all elements at their preferred size
        // so we have to shink them
        adjustmentFactor = (preferred - min) / preferred;
      }
      else
      {
        // print all elements can be printed at their preferred size
        // so we can grow them to better fill the space ..
        adjustmentFactor = (max - preferred) / preferred;
      }
    }

    float offset = fixedOffset;
    for (int i = 0; i < getViewCount(); i++)
    {
      final NodeView view = getView(i);
      offsets[i] = offset;

      if (adjustmentFactor == 0)
      {
      }
      else if (adjustmentFactor < 1)
      {
        // shrink
        final float minSize = view.getMinimumSpan(axis);
        spans[i] = Math.max (adjustmentFactor * spans[i], minSize);
      }
      else
      {
        // enlarge
        final float maxSize = view.getMaximumSpan(axis);
        spans[i] = Math.min (adjustmentFactor * spans[i], maxSize);
      }
      offset += spans[i];
    }
  }

  private void doMinorAxisLayout
          (final float fixedOffset, final float size, final int axis,
           final float[] offsets, final float[] spans)
  {
    final int n = getViewCount();
	  for (int i = 0; i < n; i++)
    {
	    final NodeView v = getView(i);
	    final float min = v.getMinimumSpan(axis);
	    final float max = v.getMaximumSpan(axis);

	    if (max < size)
      {
        // enlarge the element as much as possible
        final float align = v.getAlignment(axis);
        offsets[i] = fixedOffset + (size - max) * align;
        spans[i] = max;
	    }
      else
      {
        // make it the target width, or as small as it can get.
        offsets[i] = fixedOffset;
        spans[i] = Math.max(min, size);
	    }
	  }
  }

  public int getMajorAxis ()
  {
    return majorAxis;
  }


  public int getMinorAxis()
  {
    return minorAxis;
  }

}
