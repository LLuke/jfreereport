/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * JFreeReportResources_sv.java
 * ----------------------------
 * (C)opyright 2000-2003, by Object Refinery Limited and Contributors.
 *
 * Original Author:  Thomas Dilts;
 * Contributor(s):   -;
 *
 * $Id: JFreeReportResources_sv.java,v 1.4 2003/08/24 15:08:18 taqua Exp $
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
 * Swedish Language Resources.
 *
 * @author Thomas Dilts
 */
public class JFreeReportResources_sv extends JFreeReportResources
{
  /**
   * Default Constructor.
   */
  public JFreeReportResources_sv()
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
   * Returns the array of strings in the resource bundle.
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
  protected static BufferedImage createTransparentImage(final int width, final int height)
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
  protected static ImageIcon getIcon(final String filename)
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
    try
    {
      return KeyStroke.getKeyStroke
          (character, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    }
    catch (UnsupportedOperationException he)
    {
      // headless exception extends UnsupportedOperation exception,
      // but the HeadlessException is not defined in older JDKs...
      return KeyStroke.getKeyStroke(character, KeyEvent.CTRL_MASK);
    }
  }

  /** The resources to be localised. */
  private static final Object[][] CONTENTS =
      {
        {"action.close.name", "Stänga"},
        {"action.close.description", "Stänga förhandsgransknings-fönster"},
        {"action.close.mnemonic", new Integer(KeyEvent.VK_C)},
        {"action.close.accelerator", createMenuKeystroke(KeyEvent.VK_X)},

        {"action.gotopage.name", "Gå till sida ..."},
        {"action.gotopage.description", "Se en sida direkt"},
        {"action.gotopage.mnemonic", new Integer(KeyEvent.VK_G)},
        {"action.gotopage.accelerator", createMenuKeystroke(KeyEvent.VK_G)},

        {"dialog.gotopage.message", "Ange en sida nummer"},
        {"dialog.gotopage.title", "Gå till sida"},

        {"action.about.name", "Om..."},
        {"action.about.description", "Information om applikationen"},
        {"action.about.mnemonic", new Integer(KeyEvent.VK_A)},
        {"action.about.small-icon",
         getIcon("org/jfree/report/modules/gui/base/resources/About16.gif")},
        {"action.about.icon", getIcon("org/jfree/report/modules/gui/base/resources/About24.gif")},

        {"action.firstpage.name", "Hem"},
        {"action.firstpage.description", "Bläddra till den första sidan"},
        {"action.firstpage.description", "Switch to the first page"},
        {"action.firstpage.small-icon",
         getIcon("org/jfree/report/modules/gui/base/resources/FirstPage16.gif")},
        {"action.firstpage.icon",
         getIcon("org/jfree/report/modules/gui/base/resources/FirstPage24.gif")},
        {"action.firstpage.accelerator", KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0)},

        {"action.back.name", "Bläddra bakåt"},
        {"action.back.description", "Bläddra till den föregående sidan"},
        {"action.back.mnemonic", new Integer(KeyEvent.VK_PAGE_UP)},
        {"action.back.small-icon",
         getIcon("org/jfree/report/modules/gui/base/resources/Back16.gif")},
        {"action.back.icon", getIcon("org/jfree/report/modules/gui/base/resources/Back24.gif")},
        {"action.back.accelerator", KeyStroke.getKeyStroke("PAGE_UP")},

        {"action.forward.name", "Bläddra framåt"},
        {"action.forward.description", "Bläddra till den nästa sidan"},
        {"action.forward.mnemonic", new Integer(KeyEvent.VK_PAGE_DOWN)},
        {"action.forward.small-icon",
         getIcon("org/jfree/report/modules/gui/base/resources/Forward16.gif")},
        {"action.forward.icon",
         getIcon("org/jfree/report/modules/gui/base/resources/Forward24.gif")},
        {"action.forward.accelerator", KeyStroke.getKeyStroke("PAGE_DOWN")},

        {"action.lastpage.name", "Sista sida"},
        {"action.lastpage.description", "Bläddra till den sista sidan"},
        {"action.lastpage.mnemonic", new Integer(KeyEvent.VK_END)},
        {"action.lastpage.small-icon",
         getIcon("org/jfree/report/modules/gui/base/resources/LastPage16.gif")},
        {"action.lastpage.icon",
         getIcon("org/jfree/report/modules/gui/base/resources/LastPage24.gif")},
        {"action.lastpage.accelerator", KeyStroke.getKeyStroke(KeyEvent.VK_END, 0)},

        {"action.zoomIn.name", "Zooma in"},
        {"action.zoomIn.description", "Förstärka zoomen"},
        {"action.zoomIn.mnemonic", new Integer(KeyEvent.VK_PLUS)},
        {"action.zoomIn.small-icon",
         getIcon("org/jfree/report/modules/gui/base/resources/ZoomIn16.gif")},
        {"action.zoomIn.icon", getIcon("org/jfree/report/modules/gui/base/resources/ZoomIn24.gif")},
        {"action.zoomIn.accelerator", KeyStroke.getKeyStroke("PLUS")},

        {"action.zoomOut.name", "Zooma ut"},
        {"action.zoomOut.description", "Minska zoomen"},
        {"action.zoomOut.mnemonic", new Integer(KeyEvent.VK_MINUS)},
        {"action.zoomOut.small-icon",
         getIcon("org/jfree/report/modules/gui/base/resources/ZoomOut16.gif")},
        {"action.zoomOut.icon",
         getIcon("org/jfree/report/modules/gui/base/resources/ZoomOut24.gif")},
        {"action.zoomOut.accelerator", KeyStroke.getKeyStroke("MINUS")},

        // preview frame...
        {"preview-frame.title", "Förhandsgranska"},

        // menu labels...
        {"menu.file.name", "Fil"},
        {"menu.file.mnemonic", new Character('F')},

        {"menu.navigation.name", "Navigation"},
        {"menu.navigation.mnemonic", new Character('N')},

        {"menu.zoom.name", "Zooma"},
        {"menu.zoom.mnemonic", new Character('Z')},

        {"menu.help.name", "Hjälp"},
        {"menu.help.mnemonic", new Character('H')},

        {"statusline.pages", "Sida {0} av {1}"},
        {"statusline.error", "Reportgeneration skapade ett fel: {0}"},
        {"statusline.repaginate", "Beräkna sida-brytning, var snäll och vänta."},

        // these are the swing default values ...
        {"FileChooser.acceptAllFileFilterText", "Alla filer (*.*)"},
        {"FileChooser.cancelButtonMnemonic", new Integer(KeyEvent.VK_C)},
        {"FileChooser.cancelButtonText", "Avbryt"},
        {"FileChooser.cancelButtonToolTipText", "Avbryt fil fil väljare"},
        {"FileChooser.detailsViewButtonAccessibleName", "Detaljerna"},
        {"FileChooser.detailsViewButtonToolTipText", "Detaljerna"},
        {"FileChooser.directoryDescriptionText", "Katalog"},
        {"FileChooser.fileDescriptionText", "Generisk fil"},
        {"FileChooser.fileNameLabelMnemonic", new Integer(KeyEvent.VK_N)},
        {"FileChooser.fileNameLabelText", "Fil namn:"},
        {"FileChooser.filesOfTypeLabelMnemonic", new Integer(KeyEvent.VK_T)},
        {"FileChooser.filesOfTypeLabelText", "Filer av typ:"},
        {"FileChooser.helpButtonMnemonic", new Integer(KeyEvent.VK_H)},
        {"FileChooser.helpButtonText", "Hjälp"},
        {"FileChooser.helpButtonToolTipText", "Filväljare hjälp"},
        {"FileChooser.homeFolderAccessibleName", "Hem"},
        {"FileChooser.homeFolderToolTipText", "Hem"},
        {"FileChooser.listViewButtonAccessibleName", "Lista"},
        {"FileChooser.listViewButtonToolTipText", "Lista"},
        {"FileChooser.lookInLabelMnemonic", new Integer(KeyEvent.VK_I)},
        {"FileChooser.lookInLabelText", "Titta i:"},
        {"FileChooser.newFolderAccessibleNam", "Ny katalog"},
        {"FileChooser.newFolderErrorSeparator", ":"},
        {"FileChooser.newFolderErrorText", "Fel under katalog skapandet"},
        {"FileChooser.newFolderToolTipText", "Skapa en ny katalog"},
        {"FileChooser.openButtonMnemonic", new Integer(KeyEvent.VK_O)},
        {"FileChooser.openButtonText", "Öppna"},
        {"FileChooser.openButtonToolTipText", "Öppna den valda filen"},
        {"FileChooser.saveButtonMnemonic", new Integer(KeyEvent.VK_S)},
        {"FileChooser.saveButtonText", "Spara"},
        {"FileChooser.saveButtonToolTipText", "Spara den valda filen"},
        {"FileChooser.updateButtonMnemonic", new Integer(KeyEvent.VK_U)},
        {"FileChooser.updateButtonText", "Updatera"},
        {"FileChooser.updateButtonToolTipText", "Updatera katalog lista"},
        {"FileChooser.upFolderAccessibleName", "Upp"},
        {"FileChooser.upFolderToolTipText", "Upp en nivå"},

        {"OptionPane.yesButtonText", "Ja"},
        {"OptionPane.noButtonText", "Nej"},
        {"OptionPane.okButtonText", "OK"},
        {"OptionPane.cancelButtonText", "Avbryt"},

        {"ColorChooser.cancelText", "Avbryt"},
        {"ColorChooser.hsbNameText", "HSB"},
        {"ColorChooser.hsbHueText", "H"},
        {"ColorChooser.hsbSaturationText", "S"},
        {"ColorChooser.hsbBrightnessText", "B"},
        {"ColorChooser.hsbRedText", "R"},
        {"ColorChooser.hsbGreenText", "G"},
        {"ColorChooser.hsbBlueText", "B"},
        {"ColorChooser.okText", "OK"},
        {"ColorChooser.previewText", "Förhandsgranska"},
        {"ColorChooser.resetText", "Omställa"},
        {"ColorChooser.rgbNameText", "RGB"},
        {"ColorChooser.rgbRedMnemonic", new Integer(KeyEvent.VK_R)},
        {"ColorChooser.rgbRedText", "Röd"},
        {"ColorChooser.rgbGreenMnemonic", new Integer(KeyEvent.VK_G)},
        {"ColorChooser.rgbGreenText", "Grön"},
        {"ColorChooser.rgbBlueMnemonic", new Integer(KeyEvent.VK_B)},
        {"ColorChooser.rgbBlueText", "Blå"},
        {"ColorChooser.sampleText", "Exempel Text  Exempel Text"},
        {"ColorChooser.swatchesNameText", "Swatches"},
        {"ColorChooser.swatchesRecentText", "Senaste:"},

        {"InternalFrameTitlePane.closeButtonAccessibleName", "Stäng"},
        {"InternalFrameTitlePane.iconifyButtonAccessibleName", "Minimalt"},
        {"InternalFrameTitlePane.maximizeButtonAccessibleName", "Maximalt"},

        {"FormView.resetButtonText", "Omställa"},
        {"FormView.submitButtonText", "Ange frågaställningen"},

        {"AbstractButton.clickText", "klicka"},

        {"AbstractDocument.additionText", "lägga till"},
        {"AbstractDocument.deletionText", "ta bort"},
        {"AbstractDocument.redoText", "Gör om"},
        {"AbstractDocument.styleChangeText", "styl förändring"},
        {"AbstractDocument.undoText", "Ångra"},

        {"AbstractUndoableEdit.redoText", "Gör om"},
        {"AbstractUndoableEdit.undoText", "Ångra"},

        {"ProgressMonitor.progressText", "Förlopp..."},

        {"SplitPane.leftButtonText", "vänster knapp"},
        {"SplitPane.rightButtonText", "höger knapp"},

        // progress dialog defaults ...
        {"progress-dialog.prepare-layout", "Förbereda formaten."},
        {"progress-dialog.perform-output", "Rapportarbetet pågång ..."},
        {"progress-dialog.page-label", "Sida: {0}"},
        {"progress-dialog.rows-label", "Rad: {0} / {1}"},
        {"progress-dialog.pass-label", "Pass: {0} - Beräkna värden ..."},
      };

}
