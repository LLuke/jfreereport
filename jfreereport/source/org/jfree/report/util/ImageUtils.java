/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * ImageUtils.java
 * ------------------------------
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ImageUtils.java,v 1.4 2005/02/23 21:06:05 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 31.08.2003 : Initial version
 *  
 */

package org.jfree.report.util;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Provides utility methods for image creation and manipluation.
 *
 * @author Thomas Morgner
 */
public final class ImageUtils
{
  /**
   * DefaultConstructor.
   */
  private ImageUtils ()
  {
  }

  /**
   * Creates a transparent image.  These can be used for aligning menu items.
   *
   * @param width  the width.
   * @param height the height.
   * @return the created transparent image.
   */
  public static BufferedImage createTransparentImage (final int width, final int height)
  {
    final BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    final int[] data = img.getRGB(0, 0, width, height, null, 0, width);
    Arrays.fill(data, 0x00000000);
    img.setRGB(0, 0, width, height, data, 0, width);
    return img;
  }

  /**
   * Creates a transparent icon. The Icon can be used for aligning menu items.
   *
   * @param width  the width of the new icon
   * @param height the height of the new icon
   * @return the created transparent icon.
   */
  public static Icon createTransparentIcon (final int width, final int height)
  {
    return new ImageIcon(createTransparentImage(width, height));
  }
}
