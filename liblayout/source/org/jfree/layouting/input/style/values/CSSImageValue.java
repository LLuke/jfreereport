/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * CSSImageValue.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: CSSImageValue.java,v 1.1 2006/02/12 21:54:28 taqua Exp $
 *
 * Changes
 * -------------------------
 * 14.12.2005 : Initial version
 */
package org.jfree.layouting.input.style.values;

import org.jfree.layouting.input.LayoutImageData;
import org.jfree.layouting.input.ExternalLayoutImageData;

/**
 * Creation-Date: 14.12.2005, 22:08:30
 *
 * @author Thomas Morgner
 */
public class CSSImageValue implements CSSValue
{
  private LayoutImageData imageData;

  public CSSImageValue(LayoutImageData imageData)
  {
    this.imageData = imageData;
  }

  public LayoutImageData getImageData()
  {
    return imageData;
  }

  public String getCSSText()
  {
    // this one is not displayable.
    if (imageData instanceof ExternalLayoutImageData)
    {
      ExternalLayoutImageData eli = (ExternalLayoutImageData) imageData;
      String uri = eli.getUri();
      if (uri != null)
      {
        return "url(" + uri + ")";
      }
      else
      {
        return "url(" + eli.getSource() + ")";
      }
    }
    else
    {
      return "none /* This value is not displayable as CSS. */";
    }
  }
}
