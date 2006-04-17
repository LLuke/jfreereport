/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * BoxSpecification.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.layouting.model.box;

import org.jfree.layouting.input.style.keys.box.DisplayModel;
import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.input.style.keys.box.FloatDisplace;
import org.jfree.layouting.input.style.keys.box.Floating;
import org.jfree.layouting.input.style.keys.box.IndentEdgeReset;
import org.jfree.layouting.input.style.keys.box.Overflow;
import org.jfree.layouting.input.style.keys.box.Visibility;
import org.jfree.layouting.util.geom.StrictBounds;

/**
 * Margins, paddings, Width and height are not filled in until
 * the actual layout computation starts. Precomputed and converted
 * values are kept in the stylesheet. 
 */
public class BoxSpecification
{
  private DisplayRole displayRole;
  private DisplayModel displayModel;
  private Floating floating;
  private FloatDisplace floatDisplace;
  private IndentEdgeReset indentEdgeReset;

  // or top /bottom, its all the same ...
  private boolean clearLeft;
  private boolean clearRight;
  private boolean clearAfterLeft;
  private boolean clearAfterRight;

  private Overflow overflowX;
  private Overflow overflowY;
  private StrictBounds overflowClip;
  private Visibility visibility;

  /** The width and height as specified by the stylesheet. */
  private long maxWidth;
  private long maxHeight;
  private long minWidth;
  private long minHeight;

  private boolean contentBoxSizing;

  private long paddingTop;
  private long paddingLeft;
  private long paddingBottom;
  private long paddingRight;

  private long marginTop;
  private long marginLeft;
  private long marginBottom;
  private long marginRight;

  /** The element's bounds */
  private long width;
  private long height;
  private long x;
  private long y;

  public BoxSpecification ()
  {
  }

  public DisplayModel getDisplayModel ()
  {
    return displayModel;
  }

  public void setDisplayModel (DisplayModel displayModel)
  {
    this.displayModel = displayModel;
  }

  public DisplayRole getDisplayRole ()
  {
    return displayRole;
  }

  public void setDisplayRole (DisplayRole displayRole)
  {
    this.displayRole = displayRole;
  }

  public Floating getFloating ()
  {
    return floating;
  }

  public void setFloating (Floating floating)
  {
    this.floating = floating;
  }

  public long getHeight ()
  {
    return height;
  }

  public void setHeight (long height)
  {
    this.height = height;
  }

  public long getWidth ()
  {
    return width;
  }

  public void setWidth (long width)
  {
    this.width = width;
  }

  public long getX ()
  {
    return x;
  }

  public void setX (long x)
  {
    this.x = x;
  }

  public long getY ()
  {
    return y;
  }

  public void setY (long y)
  {
    this.y = y;
  }

  public boolean isClearAfterLeft ()
  {
    return clearAfterLeft;
  }

  public void setClearAfterLeft (boolean clearAfterLeft)
  {
    this.clearAfterLeft = clearAfterLeft;
  }

  public boolean isClearAfterRight ()
  {
    return clearAfterRight;
  }

  public void setClearAfterRight (boolean clearAfterRight)
  {
    this.clearAfterRight = clearAfterRight;
  }

  public boolean isClearLeft ()
  {
    return clearLeft;
  }

  public void setClearLeft (boolean clearLeft)
  {
    this.clearLeft = clearLeft;
  }

  public boolean isClearRight ()
  {
    return clearRight;
  }

  public void setClearRight (boolean clearRight)
  {
    this.clearRight = clearRight;
  }

  public boolean isContentBoxSizing ()
  {
    return contentBoxSizing;
  }

  public void setContentBoxSizing (boolean contentBoxSizing)
  {
    this.contentBoxSizing = contentBoxSizing;
  }

  public FloatDisplace getFloatDisplace ()
  {
    return floatDisplace;
  }

  public void setFloatDisplace (FloatDisplace floatDisplace)
  {
    this.floatDisplace = floatDisplace;
  }

  public IndentEdgeReset getIndentEdgeReset ()
  {
    return indentEdgeReset;
  }

  public void setIndentEdgeReset (IndentEdgeReset indentEdgeReset)
  {
    this.indentEdgeReset = indentEdgeReset;
  }

  public long getMarginBottom ()
  {
    return marginBottom;
  }

  public void setMarginBottom (long marginBottom)
  {
    this.marginBottom = marginBottom;
  }

  public long getMarginLeft ()
  {
    return marginLeft;
  }

  public void setMarginLeft (long marginLeft)
  {
    this.marginLeft = marginLeft;
  }

  public long getMarginRight ()
  {
    return marginRight;
  }

  public void setMarginRight (long marginRight)
  {
    this.marginRight = marginRight;
  }

  public long getMarginTop ()
  {
    return marginTop;
  }

  public void setMarginTop (long marginTop)
  {
    this.marginTop = marginTop;
  }

  public long getMaxHeight ()
  {
    return maxHeight;
  }

  public void setMaxHeight (long maxHeight)
  {
    this.maxHeight = maxHeight;
  }

  public long getMaxWidth ()
  {
    return maxWidth;
  }

  public void setMaxWidth (long maxWidth)
  {
    this.maxWidth = maxWidth;
  }

  public long getMinHeight ()
  {
    return minHeight;
  }

  public void setMinHeight (long minHeight)
  {
    this.minHeight = minHeight;
  }

  public long getMinWidth ()
  {
    return minWidth;
  }

  public void setMinWidth (long minWidth)
  {
    this.minWidth = minWidth;
  }

  public StrictBounds getOverflowClip ()
  {
    return overflowClip;
  }

  public void setOverflowClip (StrictBounds overflowClip)
  {
    this.overflowClip = overflowClip;
  }

  public Overflow getOverflowX ()
  {
    return overflowX;
  }

  public void setOverflowX (Overflow overflowX)
  {
    this.overflowX = overflowX;
  }

  public Overflow getOverflowY ()
  {
    return overflowY;
  }

  public void setOverflowY (Overflow overflowY)
  {
    this.overflowY = overflowY;
  }

  public long getPaddingBottom ()
  {
    return paddingBottom;
  }

  public void setPaddingBottom (long paddingBottom)
  {
    this.paddingBottom = paddingBottom;
  }

  public long getPaddingLeft ()
  {
    return paddingLeft;
  }

  public void setPaddingLeft (long paddingLeft)
  {
    this.paddingLeft = paddingLeft;
  }

  public long getPaddingRight ()
  {
    return paddingRight;
  }

  public void setPaddingRight (long paddingRight)
  {
    this.paddingRight = paddingRight;
  }

  public long getPaddingTop ()
  {
    return paddingTop;
  }

  public void setPaddingTop (long paddingTop)
  {
    this.paddingTop = paddingTop;
  }

  public Visibility getVisibility ()
  {
    return visibility;
  }

  public void setVisibility (Visibility visibility)
  {
    this.visibility = visibility;
  }

}
