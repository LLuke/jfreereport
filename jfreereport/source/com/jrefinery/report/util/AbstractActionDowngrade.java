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
 * ----------------------------
 * AbstractActionDowngrade.java
 * ----------------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractActionDowngrade.java,v 1.12 2003/05/14 22:26:40 taqua Exp $
 *
 * Changes
 * -------
 * 30-Aug-2002 : Initial version
 * 10-Dec-2002 : Updated Javadocs (DG);
 *
 */

package com.jrefinery.report.util;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

/**
 * A class that allows Action features introduced in JDK 1.3 to be used with JDK 1.2.2, by
 * defining the two new constants introduced by Sun in JDK 1.3.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractActionDowngrade extends AbstractAction implements ActionDowngrade
{
  // kills a compile error for JDK's >= 1.3
  // ambiguous reference error ...
  /**
   * The key used for storing a <code>KeyStroke</code> to be used as the
   * accelerator for the action.
   */
  public static final String ACCELERATOR_KEY = ActionDowngrade.ACCELERATOR_KEY;

  /**
   * The key used for storing an int key code to be used as the mnemonic
   * for the action.
   */
  public static final String MNEMONIC_KEY = ActionDowngrade.MNEMONIC_KEY;

  /**
   * A transparent 16x16 icon.
   */
  private static final ImageIcon TRANSPARENT_EMPTY_ICON_16 =
      new ImageIcon(createTransparentImage(16, 16));

  /**
   * A transparent 24x24 icon.
   */
  private static final ImageIcon TRANSPARENT_EMPTY_ICON_24 =
      new ImageIcon(createTransparentImage(24, 24));

  /**
   * Creates a transparent image, which can be used for aligning menu items.
   *
   * @param width  the image width.
   * @param height  the image height.
   *
   * @return a transparent image.
   */
  public static BufferedImage createTransparentImage(int width, int height)
  {
    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    int[] data = img.getRGB(0, 0, width, height, null, 0, width);
    Arrays.fill(data, 0x00000000);
    img.setRGB(0, 0, width, height, data, 0, width);
    return img;
  }

  /**
   * Creates a new action with a default (transparent) icon.
   */
  protected AbstractActionDowngrade()
  {
    putValue(SMALL_ICON, TRANSPARENT_EMPTY_ICON_16);
    putValue("ICON24", TRANSPARENT_EMPTY_ICON_24);
  }
}
