/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractActionDowngrade.java,v 1.6 2002/12/04 19:58:16 taqua Exp $
 *
 * Changes
 * -------
 * 30-Aug-2002 : Initial version
 */
package com.jrefinery.report.util;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Defines the 2 new constants introduced by Sun in version 1.3 of the J2SDK.
 *
 * @author TM
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

  public static BufferedImage createTransparentImage (int width, int height)
  {
    BufferedImage img = new BufferedImage (width, height, BufferedImage.TYPE_INT_ARGB);
    int[] data = img.getRGB(0,0, width, height, null, 0, width);
    Arrays.fill(data, 0x00000000);
    img.setRGB(0,0,width,height,data,0,width);
    return img;
  }

  /**
   * Defines an <code>Action</code> object with a default
   * description string and default icon.
   */
  public AbstractActionDowngrade()
  {
    putValue (SMALL_ICON, new ImageIcon (createTransparentImage(16,16)));
    putValue ("ICON24", new ImageIcon (createTransparentImage(16,16)));
  }
}
