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
 * TextAlignmentSpecifcation.java
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
package org.jfree.layouting.model.text;

import org.jfree.layouting.input.style.keys.text.TextAlign;
import org.jfree.layouting.input.style.keys.text.TextJustify;
import org.jfree.layouting.input.style.keys.text.TextJustifyTrim;

/**
 * Creation-Date: 21.12.2005, 13:53:29
 *
 * @author Thomas Morgner
 */
public class TextAlignmentSpecifcation
{
  private TextAlign textAlign;
  private String subStringAlignment;
  private TextAlign textAlignLastLine;
  private TextJustify textJustify;
  private TextJustifyTrim textJustifyTrim;
  private long textIndentation;
  private boolean hangingIndentation;
  private double kashidaSpace;

  public TextAlignmentSpecifcation()
  {
  }

  public TextAlign getTextAlign()
  {
    return textAlign;
  }

  public void setTextAlign(final TextAlign textAlign)
  {
    this.textAlign = textAlign;
  }

  public TextAlign getTextAlignLastLine()
  {
    return textAlignLastLine;
  }

  public void setTextAlignLastLine(final TextAlign textAlignLastLine)
  {
    this.textAlignLastLine = textAlignLastLine;
  }

  public TextJustify getTextJustify()
  {
    return textJustify;
  }

  public void setTextJustify(final TextJustify textJustify)
  {
    this.textJustify = textJustify;
  }

  public TextJustifyTrim getTextJustifyTrim()
  {
    return textJustifyTrim;
  }

  public void setTextJustifyTrim(final TextJustifyTrim textJustifyTrim)
  {
    this.textJustifyTrim = textJustifyTrim;
  }

  public long getTextIndentation()
  {
    return textIndentation;
  }

  public void setTextIndentation(final long textIndentation)
  {
    this.textIndentation = textIndentation;
  }

  public boolean isHangingIndentation()
  {
    return hangingIndentation;
  }

  public void setHangingIndentation(final boolean hangingIndentation)
  {
    this.hangingIndentation = hangingIndentation;
  }

  public String getSubStringAlignment()
  {
    return subStringAlignment;
  }

  public void setSubStringAlignment(final String subStringAlignment)
  {
    this.subStringAlignment = subStringAlignment;
  }

  public double getKashidaSpace()
  {
    return kashidaSpace;
  }

  public void setKashidaSpace(final double kashidaSpace)
  {
    this.kashidaSpace = kashidaSpace;
  }
}
