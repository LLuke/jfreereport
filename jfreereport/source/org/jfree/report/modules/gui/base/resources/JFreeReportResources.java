/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Thomas Morgner, Object Refinery Limited and Contributors.
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
 * -------------------------
 * JFreeReportResources.java
 * -------------------------
 * (C)opyright 2000-2003, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: JFreeReportResources.java,v 1.61 2003/06/29 19:01:18 taqua Exp $
 *
 */
package org.jfree.report.modules.gui.base.resources;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.ListResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.jfree.report.util.Log;

/**
 * English language resources.
 *
 * @author Thomas Morgner
 */
public class JFreeReportResources extends ListResourceBundle
{
  /**
   * Default constructor.
   */
  public JFreeReportResources()
  {
  }

  /**
   * Used to test the resourcebundle for null values.
   *
   * @param args  ignored.
   */
  public static void main(final String[] args)
  {
    Object lastKey = null;
    try
    {
      final Hashtable elements = new Hashtable();
      for (int i = 0; i < CONTENTS.length; i++)
      {
        final Object[] row = CONTENTS[i];
        lastKey = row[0];
        elements.put(row[0], row[1]);
      }
      getIcon("org/jfree/report/modules/gui/base/resources/SaveAs16.gif");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      Log.debug("LastKey read: " + lastKey);
    }
    System.exit(0);
  }

  /**
   * Returns an array of localised resources.
   *
   * @return an array of localised resources.
   */
  public Object[][] getContents()
  {
    return CONTENTS;
  }

  /**
   * Prints all defined resource bundle keys and their assigned values.
   */
  public void printAll()
  {
    final Object[][] c = getContents();
    for (int i = 0; i < c.length; i++)
    {
      final Object[] cc = c[i];
      System.out.print(cc[0]);
      System.out.print("=");
      System.out.println(cc[1]);

    }
  }

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
    Arrays.fill(data, 0xff000000);
    img.setRGB(0, 0, width, height, data, 0, width);
    return img;
  }

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
      return new ImageIcon(createTransparentImage(1, 1));
    }
    final Image img = Toolkit.getDefaultToolkit().createImage(in);
    if (img == null)
    {
      Log.warn("Unable to instantiate the image: " + filename);
      return new ImageIcon(createTransparentImage(1, 1));
    }
    return new ImageIcon(img);
  }

  /**
   * Creates a platform independed menu keystroke for the given character.
   *
   * @param character the keystroke character
   * @return the generated keystroke object.
   */
  protected static final KeyStroke createMenuKeystroke(final int character)
  {
    return KeyStroke.getKeyStroke(character, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
  }

  /** The resources to be localised. */
  private static final Object[][] CONTENTS =
      {
        {"action.close.name", "Close"},
        {"action.close.description", "Close print preview window"},
        {"action.close.mnemonic", new Integer(KeyEvent.VK_C)},
        {"action.close.accelerator", createMenuKeystroke(KeyEvent.VK_X)},

        {"action.gotopage.name", "Go to page ..."},
        {"action.gotopage.description", "View a page directly"},
        {"action.gotopage.mnemonic", new Integer(KeyEvent.VK_G)},
        {"action.gotopage.accelerator", createMenuKeystroke(KeyEvent.VK_G)},

        {"dialog.gotopage.message", "Enter a page number"},
        {"dialog.gotopage.title", "Go to page"},

        {"action.about.name", "About..."},
        {"action.about.description", "Information about the application"},
        {"action.about.mnemonic", new Integer(KeyEvent.VK_A)},
        {"action.about.small-icon", getIcon("org/jfree/report/modules/gui/base/resources/About16.gif")},
        {"action.about.icon", getIcon("org/jfree/report/modules/gui/base/resources/About24.gif")},

        {"action.firstpage.name", "Home"},
        {"action.firstpage.mnemonic", new Integer(KeyEvent.VK_HOME)},
        {"action.firstpage.description", "Switch to the first page"},
        {"action.firstpage.small-icon",
         getIcon("org/jfree/report/modules/gui/base/resources/FirstPage16.gif")},
        {"action.firstpage.icon", getIcon("org/jfree/report/modules/gui/base/resources/FirstPage24.gif")},
        {"action.firstpage.accelerator", KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0)},

        {"action.back.name", "Back"},
        {"action.back.description", "Switch to the previous page"},
        {"action.back.mnemonic", new Integer(KeyEvent.VK_PAGE_UP)},
        {"action.back.small-icon", getIcon("org/jfree/report/modules/gui/base/resources/Back16.gif")},
        {"action.back.icon", getIcon("org/jfree/report/modules/gui/base/resources/Back24.gif")},
        {"action.back.accelerator", KeyStroke.getKeyStroke("PAGE_UP")},

        {"action.forward.name", "Forward"},
        {"action.forward.description", "Switch to the next page"},
        {"action.forward.mnemonic", new Integer(KeyEvent.VK_PAGE_DOWN)},
        {"action.forward.small-icon",
         getIcon("org/jfree/report/modules/gui/base/resources/Forward16.gif")},
        {"action.forward.icon", getIcon("org/jfree/report/modules/gui/base/resources/Forward24.gif")},
        {"action.forward.accelerator", KeyStroke.getKeyStroke("PAGE_DOWN")},

        {"action.lastpage.name", "End"},
        {"action.lastpage.description", "Switch to the last page"},
        {"action.lastpage.mnemonic", new Integer(KeyEvent.VK_END)},
        {"action.lastpage.small-icon",
         getIcon("org/jfree/report/modules/gui/base/resources/LastPage16.gif")},
        {"action.lastpage.icon", getIcon("org/jfree/report/modules/gui/base/resources/LastPage24.gif")},
        {"action.lastpage.accelerator", KeyStroke.getKeyStroke(KeyEvent.VK_END, 0)},

        {"action.zoomIn.name", "Zoom In"},
        {"action.zoomIn.description", "Increase zoom"},
        {"action.zoomIn.mnemonic", new Integer(KeyEvent.VK_PLUS)},
        {"action.zoomIn.small-icon", getIcon("org/jfree/report/modules/gui/base/resources/ZoomIn16.gif")},
        {"action.zoomIn.icon", getIcon("org/jfree/report/modules/gui/base/resources/ZoomIn24.gif")},
        {"action.zoomIn.accelerator", KeyStroke.getKeyStroke("PLUS")},

        {"action.zoomOut.name", "Zoom Out"},
        {"action.zoomOut.description", "Decrease Zoom"},
        {"action.zoomOut.mnemonic", new Integer(KeyEvent.VK_MINUS)},
        {"action.zoomOut.small-icon",
         getIcon("org/jfree/report/modules/gui/base/resources/ZoomOut16.gif")},
        {"action.zoomOut.icon", getIcon("org/jfree/report/modules/gui/base/resources/ZoomOut24.gif")},
        {"action.zoomOut.accelerator", KeyStroke.getKeyStroke("MINUS")},

        // preview frame...
        {"preview-frame.title", "Print Preview"},

        // menu labels...
        {"menu.file.name", "File"},
        {"menu.file.mnemonic", new Character('F')},

        {"menu.navigation.name", "Navigation"},
        {"menu.navigation.mnemonic", new Character('N')},

        {"menu.zoom.name", "Zoom"},
        {"menu.zoom.mnemonic", new Character('Z')},

        {"menu.help.name", "Help"},
        {"menu.help.mnemonic", new Character('H')},

        {"statusline.pages", "Page {0} of {1}"},
        {"statusline.error", "Reportgeneration produced an error: {0}"},
        {"statusline.repaginate", "Calculating pagebreaks, please wait."},

      };

}