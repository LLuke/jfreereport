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
 * ImageUtils.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
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

public final class ImageUtils
{
  /**
   * Creates a transparent image.  These can be used for aligning menu items.
   *
   * @param width  the width.
   * @param height  the height.
   *
   * @return the image.
   */
  public static BufferedImage createTransparentImage(final int width, final int height)
  {
    final BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    final int[] data = img.getRGB(0, 0, width, height, null, 0, width);
    Arrays.fill(data, 0x00000000);
    img.setRGB(0, 0, width, height, data, 0, width);
    return img;
  }

  public static Icon createTransparentIcon (final int width, final int height)
  {
    return new ImageIcon (createTransparentImage(width, height));
  }
}
