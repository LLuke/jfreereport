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
import org.jfree.layout.style.DefaultStyleKeys;
import org.jfree.layout.style.StyleSheet;
import org.jfree.layout.style.StyleUtilities;

public abstract class AbstractNodeView implements NodeView
{
  private LayoutInformation layoutInformation;
  private DocumentNode node;
  private boolean majorAxisValid;
  private boolean minorAxisValid;
  private NodeView parent;

  public AbstractNodeView (final DocumentNode node)
  {
    this.node = node;
    this.layoutInformation = new LayoutInformation();
  }

  /**
   * Do not break the view.
   *
   * @param axis
   * @param startOffset
   * @param width
   * @param force
   * @return the view unchanged.
   */
  public NodeView breakView (final int axis,
                             final float startOffset,
                             final float width,
                             final boolean force)
  {
    return this;
  }

  /**
   * This view is not breakable. Override this to change the behaviour.
   *
   * @param axis
   * @param len
   * @return
   */
  public int getBreakWeight (final int axis,
                             final float len)
  {
    return BadBreakWeight;
  }

  public DocumentNode getNode ()
  {
    return node;
  }

  public String toString ()
  {
    return "DefaultView={Node=" + node.getName() + "}";
  }

  public float getMaximumSpan (final int axis)
  {
    return 0;
  }

  public float getMinimumSpan (final int axis)
  {
    return 0;
  }

  public float getPreferredSpan (final int axis)
  {
    return 0;
  }

  public float getSpanSize (final int axis)
  {
    if (axis == X_AXIS)
    {
      return layoutInformation.getWidth();
    }
    else
    {
      return layoutInformation.getHeight();
    }
  }

  public float getX ()
  {
    return layoutInformation.getX();
  }

  public float getY ()
  {
    return layoutInformation.getY();
  }

  public void setLocation (final float x, final float y)
  {
    layoutInformation.setPosition(x, y);
  }

  public void setSpanSize (final int axis, final float value)
  {
    if (axis == X_AXIS)
    {
      layoutInformation.setWidth(value);

    }
    if (axis == getMajorAxis())
    {

    }
  }

  protected abstract void performMinorAxisLayout();
  protected abstract void performMajorAxisLayout();

  public LayoutInformation getLayoutInformation()
  {
    return layoutInformation;
  }

  /**
   * This method computes all borders. It assumes, that the border
   * definition will not be changed during the layouting process.
   */
  protected void computeBorders ()
  {
    if (node == null)
    {
      throw new NullPointerException("Given node must never be null.");
    }
    // perform layout on this node ..
    final StyleSheet style = node.getStyle();
    final float marginTop = StyleUtilities.getFloatStyle
            (style, DefaultStyleKeys.MARGIN_TOP);
    final float marginLeft = StyleUtilities.getFloatStyle
            (style, DefaultStyleKeys.MARGIN_LEFT);
    final float marginBottom = StyleUtilities.getFloatStyle
            (style, DefaultStyleKeys.MARGIN_BOTTOM);
    final float marginRight = StyleUtilities.getFloatStyle
            (style, DefaultStyleKeys.MARGIN_RIGHT);

    final float paddingTop = StyleUtilities.getFloatStyle
            (style, DefaultStyleKeys.PADDING_TOP);
    final float paddingLeft = StyleUtilities.getFloatStyle
            (style, DefaultStyleKeys.PADDING_LEFT);
    final float paddingBottom = StyleUtilities.getFloatStyle
            (style, DefaultStyleKeys.PADDING_BOTTOM);
    final float paddingRight = StyleUtilities.getFloatStyle
            (style, DefaultStyleKeys.PADDING_RIGHT);
    layoutInformation.setMargin
            (marginTop, marginLeft, marginBottom, marginRight);
    layoutInformation.setPadding
            (paddingTop, paddingLeft, paddingBottom, paddingRight);
  }

  public float getAlignment (final int axis)
  {
    return 0.5f;
  }

  public boolean isLayoutValid()
  {
    return majorAxisValid || minorAxisValid;
  }

  public boolean isMajorAxisValid ()
  {
    return majorAxisValid;
  }

  public boolean isMinorAxisValid ()
  {
    return minorAxisValid;
  }

  public void invalidateLayout()
  {
    majorAxisValid = false;
    minorAxisValid = false;
  }

  public void invalidateLayout(final int axis)
  {
    if (axis == getMajorAxis())
    {
      majorAxisValid = false;
    }
    else
    {
      minorAxisValid = false;
    }
  }

  protected void validated(final int axis)
  {
    if (axis == getMajorAxis())
    {
      majorAxisValid = true;
    }
    else
    {
      minorAxisValid = true;
    }
  }

  public ViewFactory getViewFactory ()
  {
    if (getParent() == null)
    {
      return null;
    }
    return getParent().getViewFactory();
  }

  public NodeView getParent ()
  {
    return parent;
  }

  protected void setParent(final NodeView parent)
  {
    this.parent = parent;
  }
}
