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

package org.jfree.layout.peer;



/**
 * Stores the computed layout information. Once the layout is complete,
 * the engine assigned one of these layout information objects to every
 * element.
 * <p>
 */
public final class LayoutInformation
{
  private float width;
  private float height;

  private float contentWidth;
  private float contentHeight;

  private float x;
  private float y;

  private float marginTop;
  private float marginLeft;
  private float marginBottom;
  private float marginRight;

  private float paddingTop;
  private float paddingLeft;
  private float paddingBottom;
  private float paddingRight;

  public LayoutInformation ()
  {
  }

  public void setMargin (final float top, final float left,
                         final float bottom, final float right)
  {
    marginRight = right;
    marginLeft = left;
    marginBottom = bottom;
    marginTop = top;
    validate();
  }

  public void setPadding (final float top, final float left,
                          final float bottom, final float right)
  {
    paddingRight = right;
    paddingLeft = left;
    paddingBottom = bottom;
    paddingTop = top;
    validate();
  }

  public void setPosition (final float x, final float y)
  {
    this.x = x;
    this.y = y;
  }

  public void setSize (final float width, final float height)
  {
    this.width = width;
    this.height = height;
    validate();
  }

  public void setContentSize (final float width, final float height)
  {
    this.contentWidth = width;
    this.contentHeight = height;
    validate();
  }

  private void validate()
  {
    this.width = contentWidth + marginLeft + marginRight + paddingLeft + paddingRight;
    this.height = contentHeight + marginTop+ marginBottom+ paddingBottom+ paddingTop;
  }

  public float getContentHeight ()
  {
    return contentHeight;
  }

  public void setContentHeight (float contentHeight)
  {
    this.contentHeight = contentHeight;
  }

  public float getContentWidth ()
  {
    return contentWidth;
  }

  public void setContentWidth (float contentWidth)
  {
    this.contentWidth = contentWidth;
  }

  public float getHeight ()
  {
    return height;
  }

  public void setHeight (float height)
  {
    this.height = height;
  }

  public float getMarginBottom ()
  {
    return marginBottom;
  }

  public void setMarginBottom (float marginBottom)
  {
    this.marginBottom = marginBottom;
  }

  public float getMarginLeft ()
  {
    return marginLeft;
  }

  public void setMarginLeft (float marginLeft)
  {
    this.marginLeft = marginLeft;
  }

  public float getMarginRight ()
  {
    return marginRight;
  }

  public void setMarginRight (float marginRight)
  {
    this.marginRight = marginRight;
  }

  public float getMarginTop ()
  {
    return marginTop;
  }

  public void setMarginTop (float marginTop)
  {
    this.marginTop = marginTop;
  }

  public float getPaddingBottom ()
  {
    return paddingBottom;
  }

  public void setPaddingBottom (float paddingBottom)
  {
    this.paddingBottom = paddingBottom;
  }

  public float getPaddingLeft ()
  {
    return paddingLeft;
  }

  public void setPaddingLeft (float paddingLeft)
  {
    this.paddingLeft = paddingLeft;
  }

  public float getPaddingRight ()
  {
    return paddingRight;
  }

  public void setPaddingRight (float paddingRight)
  {
    this.paddingRight = paddingRight;
  }

  public float getPaddingTop ()
  {
    return paddingTop;
  }

  public void setPaddingTop (float paddingTop)
  {
    this.paddingTop = paddingTop;
  }

  public float getWidth ()
  {
    return width;
  }

  public void setWidth (float width)
  {
    this.width = width;
  }

  public float getX ()
  {
    return x;
  }

  public void setX (float x)
  {
    this.x = x;
  }

  public float getY ()
  {
    return y;
  }

  public void setY (float y)
  {
    this.y = y;
  }

  public float getContentX ()
  {
    return x + marginLeft + paddingLeft;
  }

  public float getContentY ()
  {
    return y + marginTop + paddingTop;
  }
}
