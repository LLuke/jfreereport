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
 * LineSpecification.java
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

package org.jfree.layouting.model.line;

import org.jfree.layouting.input.style.keys.line.TextHeight;
import org.jfree.layouting.input.style.keys.line.LineStackingStrategy;
import org.jfree.layouting.input.style.keys.line.VerticalAlign;

/**
 * The vertical align should be split up as specified in
 * the linebox module.
 */
public class LineSpecification
{
  private TextHeight textHeight;
  private long lineHeight;
  private LineStackingStrategy lineStackingStrategy;
  private VerticalAlign verticalAlign;

  public LineSpecification ()
  {
  }

  public TextHeight getTextHeight ()
  {
    return textHeight;
  }

  public void setTextHeight (TextHeight textHeight)
  {
    this.textHeight = textHeight;
  }

  public long getLineHeight ()
  {
    return lineHeight;
  }

  public void setLineHeight (long lineHeight)
  {
    this.lineHeight = lineHeight;
  }

  public LineStackingStrategy getLineStackingStrategy ()
  {
    return lineStackingStrategy;
  }

  public void setLineStackingStrategy (LineStackingStrategy lineStackingStrategy)
  {
    this.lineStackingStrategy = lineStackingStrategy;
  }

  public VerticalAlign getVerticalAlign ()
  {
    return verticalAlign;
  }

  public void setVerticalAlign (VerticalAlign verticalAlign)
  {
    this.verticalAlign = verticalAlign;
  }
}
