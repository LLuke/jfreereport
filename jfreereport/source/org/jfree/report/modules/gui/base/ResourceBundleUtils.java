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
 * ResourceBundleUtils.java
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
 * 19.01.2004 : Initial version
 *
 */

package org.jfree.report.modules.gui.base;

import java.net.URL;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.jfree.report.util.Log;
import org.jfree.report.util.ImageUtils;

public class ResourceBundleUtils
{

  /**
   * Attempts to load an image from classpath. If this fails, an empty
   * image icon is returned.
   *
   * @param filename  the file name.
   *
   * @return the image icon.
   */
  public static ImageIcon getIcon(final String filename)
  {

    final URL in = Thread.currentThread().getContextClassLoader().getResource(filename);
    if (in == null)
    {
      Log.warn("Unable to find file in the class path: " + filename);
      return new ImageIcon(ImageUtils.createTransparentImage(1, 1));
    }
    final Image img = Toolkit.getDefaultToolkit().createImage(in);
    if (img == null)
    {
      Log.warn("Unable to instantiate the image: " + filename);
      return new ImageIcon(ImageUtils.createTransparentImage(1, 1));
    }
    return new ImageIcon(img);
  }

  /**
   * Creates a platform independed menu keystroke for the given character.
   *
   * @param key the keystroke string, either a "VK_*" sequence (as defined
   * in the KeyEvent class) or a single character.
   * @return the generated keystroke object.
   */
  public static final KeyStroke createMenuKeystroke(final String key)
  {
    return KeyStroke.getKeyStroke(createMnemonic(key).intValue(), getMenuKeyMask());
  }

  public static Integer createMnemonic (final String key)
  {
    if (key == null)
    {
      throw new NullPointerException("Key is null.");
    }
    if (key.length() == 0)
    {
      throw new IllegalArgumentException("Key is empty.");
    }
    int character = key.charAt(0);
    if (key.startsWith("VK_"))
    {
      try
      {
        Field f = KeyEvent.class.getField(key);
        Integer keyCode = (Integer) f.get(null);
        character = keyCode.intValue();
      }
      catch (Exception nsfe)
      {
        // ignore the exception ...
      }
    }
    return new Integer(character);
  }

  private static int getMenuKeyMask()
  {
    try
    {
      return Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    }
    catch (UnsupportedOperationException he)
    {
      // headless exception extends UnsupportedOperation exception,
      // but the HeadlessException is not defined in older JDKs...
      return KeyEvent.CTRL_MASK;
    }
  }
}
