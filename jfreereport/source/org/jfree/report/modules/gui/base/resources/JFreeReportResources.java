/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * $Id: JFreeReportResources.java,v 1.10 2003/08/31 19:27:57 taqua Exp $
 *
 */
package org.jfree.report.modules.gui.base.resources;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ListResourceBundle;
import java.util.Properties;
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
    new JFreeReportResources().generateResourceProperties("<default>");
    System.exit(0);
  }

  public void generateResourceProperties (String lang)
  {
    Object lastKey = null;
    try
    {
      Object[][] CONTENTS = getContents();
      final Properties elements = new Properties();
      for (int i = 0; i < CONTENTS.length; i++)
      {
        final Object[] row = CONTENTS[i];
        lastKey = row[0];
        if (row[1] instanceof Integer)
        {
          Integer in = (Integer) row[1];
          elements.setProperty((String) row[0], createMenuKeystroke(in.intValue()));
        }
        else if (row[1] instanceof Character)
        {
          Character in = (Character) row[1];
          elements.setProperty((String) row[0], createMenuKeystroke(in.charValue()));
        }
        else if (row[1] instanceof KeyStroke)
        {
          KeyStroke in = (KeyStroke) row[1];
          elements.setProperty((String) row[0], createMenuKeystroke(in.getKeyCode()));
        }
        else
        {
          elements.setProperty((String) row[0], (String) row[1]);
        }
      }


      elements.store(System.out, "JFreeReport resource bundle. Language " + lang);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      Log.debug("LastKey read: " + lastKey);
    }
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
   * Attempts to load an image from classpath. If this fails, an empty
   * image icon is returned.
   *
   * @param filename  the file name.
   *
   * @return the image icon.
   */
  protected static Object getIcon(final String filename)
  {
/*
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
    */
    return filename;
  }

  /**
   * Creates a platform independed menu keystroke for the given character.
   *
   * @param character the keystroke character
   * @return the generated keystroke object.
   */
  protected static final String createMenuKeystroke(final int character)
  {
    try
    {
      Field[] fs = KeyEvent.class.getFields();
      for (int i = 0; i < fs.length; i++)
      {
        Field f = fs[i];
        if (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers()))
        {
          if (f.getType().equals(Integer.TYPE))
          {
            if (f.getInt(null) == character)
            {
              return f.getName();
            }
          }
        }
      }
    }
    catch (Exception nsfe)
    {
      // ignore the exception ...

    }
    return "" + (char) character;
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
        {"action.about.small-icon",
         getIcon("org/jfree/report/modules/gui/base/resources/About16.gif")},
        {"action.about.icon", getIcon("org/jfree/report/modules/gui/base/resources/About24.gif")},

        {"action.firstpage.name", "Home"},
        {"action.firstpage.mnemonic", new Integer(KeyEvent.VK_HOME)},
        {"action.firstpage.description", "Switch to the first page"},
        {"action.firstpage.small-icon",
         getIcon("org/jfree/report/modules/gui/base/resources/FirstPage16.gif")},
        {"action.firstpage.icon",
         getIcon("org/jfree/report/modules/gui/base/resources/FirstPage24.gif")},
        {"action.firstpage.accelerator", KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0)},

        {"action.back.name", "Back"},
        {"action.back.description", "Switch to the previous page"},
        {"action.back.mnemonic", new Integer(KeyEvent.VK_PAGE_UP)},
        {"action.back.small-icon",
         getIcon("org/jfree/report/modules/gui/base/resources/Back16.gif")},
        {"action.back.icon", getIcon("org/jfree/report/modules/gui/base/resources/Back24.gif")},
        {"action.back.accelerator", KeyStroke.getKeyStroke("PAGE_UP")},

        {"action.forward.name", "Forward"},
        {"action.forward.description", "Switch to the next page"},
        {"action.forward.mnemonic", new Integer(KeyEvent.VK_PAGE_DOWN)},
        {"action.forward.small-icon",
         getIcon("org/jfree/report/modules/gui/base/resources/Forward16.gif")},
        {"action.forward.icon",
         getIcon("org/jfree/report/modules/gui/base/resources/Forward24.gif")},
        {"action.forward.accelerator", KeyStroke.getKeyStroke("PAGE_DOWN")},

        {"action.lastpage.name", "End"},
        {"action.lastpage.description", "Switch to the last page"},
        {"action.lastpage.mnemonic", new Integer(KeyEvent.VK_END)},
        {"action.lastpage.small-icon",
         getIcon("org/jfree/report/modules/gui/base/resources/LastPage16.gif")},
        {"action.lastpage.icon",
         getIcon("org/jfree/report/modules/gui/base/resources/LastPage24.gif")},
        {"action.lastpage.accelerator", KeyStroke.getKeyStroke(KeyEvent.VK_END, 0)},

        {"action.zoomIn.name", "Zoom In"},
        {"action.zoomIn.description", "Increase zoom"},
        {"action.zoomIn.mnemonic", new Integer(KeyEvent.VK_PLUS)},
        {"action.zoomIn.small-icon",
         getIcon("org/jfree/report/modules/gui/base/resources/ZoomIn16.gif")},
        {"action.zoomIn.icon", getIcon("org/jfree/report/modules/gui/base/resources/ZoomIn24.gif")},
        {"action.zoomIn.accelerator", KeyStroke.getKeyStroke("PLUS")},

        {"action.zoomOut.name", "Zoom Out"},
        {"action.zoomOut.description", "Decrease Zoom"},
        {"action.zoomOut.mnemonic", new Integer(KeyEvent.VK_MINUS)},
        {"action.zoomOut.small-icon",
         getIcon("org/jfree/report/modules/gui/base/resources/ZoomOut16.gif")},
        {"action.zoomOut.icon",
         getIcon("org/jfree/report/modules/gui/base/resources/ZoomOut24.gif")},
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

        // these are the swing default values ...
        {"FileChooser.acceptAllFileFilterText", "All Files (*.*)"},
        {"FileChooser.cancelButtonMnemonic", new Integer(KeyEvent.VK_C)},
        {"FileChooser.cancelButtonText", "Cancel"},
        {"FileChooser.cancelButtonToolTipText", "Abort file chooser dialog"},
        {"FileChooser.detailsViewButtonAccessibleName", "Details"},
        {"FileChooser.detailsViewButtonToolTipText", "Details"},
        {"FileChooser.directoryDescriptionText", "Directory"},
        {"FileChooser.fileDescriptionText", "Generic File"},
        {"FileChooser.fileNameLabelMnemonic", new Integer(KeyEvent.VK_N)},
        {"FileChooser.fileNameLabelText", "File name:"},
        {"FileChooser.filesOfTypeLabelMnemonic", new Integer(KeyEvent.VK_T)},
        {"FileChooser.filesOfTypeLabelText", "Files of type:"},
        {"FileChooser.helpButtonMnemonic", new Integer(KeyEvent.VK_H)},
        {"FileChooser.helpButtonText", "Help"},
        {"FileChooser.helpButtonToolTipText", "FileChooser help"},
        {"FileChooser.homeFolderAccessibleName", "Home"},
        {"FileChooser.homeFolderToolTipText", "Home"},
        {"FileChooser.listViewButtonAccessibleName", "List"},
        {"FileChooser.listViewButtonToolTipText", "List"},
        {"FileChooser.lookInLabelMnemonic", new Integer(KeyEvent.VK_I)},
        {"FileChooser.lookInLabelText", "Look in:"},
        {"FileChooser.newFolderAccessibleName", "New Folder"},
        {"FileChooser.newFolderErrorSeparator", ":"},
        {"FileChooser.newFolderErrorText", "Error creating new folder"},
        {"FileChooser.newFolderToolTipText", "Create New Folder"},
        {"FileChooser.openButtonMnemonic", new Integer(KeyEvent.VK_O)},
        {"FileChooser.openButtonText", "Open"},
        {"FileChooser.openButtonToolTipText", "Open selected file"},
        {"FileChooser.saveButtonMnemonic", new Integer(KeyEvent.VK_S)},
        {"FileChooser.saveButtonText", "Save"},
        {"FileChooser.saveButtonToolTipText", "Save selected file"},
        {"FileChooser.updateButtonMnemonic", new Integer(KeyEvent.VK_U)},
        {"FileChooser.updateButtonText", "Update"},
        {"FileChooser.updateButtonToolTipText", "Update directory listing"},
        {"FileChooser.upFolderAccessibleName", "Up"},
        {"FileChooser.upFolderToolTipText", "Up One Level"},
        {"FileChooser.openDialogTitleText", "Open"},
        {"FileChooser.other.newFolder", "New Folder"},
        {"FileChooser.other.newFolder.subsequent", "New folder. {0}"},
        {"FileChooser.saveDialogTitleText", "Save"},
        {"FileChooser.win32.newFolder", "New Folder"},
        {"FileChooser.win32.newFolder.subsequent", "New folder. ({0})"},

        {"OptionPane.yesButtonText", "Yes"},
        {"OptionPane.noButtonText", "No"},
        {"OptionPane.okButtonText", "OK"},
        {"OptionPane.cancelButtonText", "Cancel"},
        {"OptionPane.titleText", "Choose an option"},

        {"ColorChooser.cancelText", "Cancel"},
        {"ColorChooser.hsbNameText", "HSB"},
        {"ColorChooser.hsbHueText", "H"},
        {"ColorChooser.hsbSaturationText", "S"},
        {"ColorChooser.hsbBrightnessText", "B"},
        {"ColorChooser.hsbRedText", "R"},
        {"ColorChooser.hsbGreenText", "G"},
        {"ColorChooser.hsbBlueText", "B"},
        {"ColorChooser.okText", "OK"},
        {"ColorChooser.previewText", "Preview"},
        {"ColorChooser.resetText", "Reset"},
        {"ColorChooser.rgbNameText", "RGB"},
        {"ColorChooser.rgbRedMnemonic", new Integer(KeyEvent.VK_R)},
        {"ColorChooser.rgbRedText", "Red"},
        {"ColorChooser.rgbGreenMnemonic", new Integer(KeyEvent.VK_G)},
        {"ColorChooser.rgbGreenText", "Green"},
        {"ColorChooser.rgbBlueMnemonic", new Integer(KeyEvent.VK_B)},
        {"ColorChooser.rgbBlueText", "Blue"},
        {"ColorChooser.sampleText", "Sample Text  Sample Text"},
        {"ColorChooser.swatchesNameText", "Swatches"},
        {"ColorChooser.swatchesRecentText", "Recent:"},

        {"InternalFrameTitlePane.closeButtonAccessibleName", "Close"},
        {"InternalFrameTitlePane.iconifyButtonAccessibleName", "Iconify"},
        {"InternalFrameTitlePane.maximizeButtonAccessibleName", "Maximize"},

        {"FormView.resetButtonText", "Reset"},
        {"FormView.submitButtonText", "Submit Query"},

        {"AbstractButton.clickText", "click"},

        {"AbstractDocument.additionText", "addition"},
        {"AbstractDocument.deletionText", "deletion"},
        {"AbstractDocument.redoText", "Redo"},
        {"AbstractDocument.styleChangeText", "style change"},
        {"AbstractDocument.undoText", "Undo"},

        {"AbstractUndoableEdit.redoText", "Redo"},
        {"AbstractUndoableEdit.undoText", "Undo"},

        {"ProgressMonitor.progressText", "Progress..."},

        {"SplitPane.leftButtonText", "left button"},
        {"SplitPane.rightButtonText", "right button"},

        // progress dialog defaults ...
        {"progress-dialog.prepare-layout", "Preparing the layout for the output."},
        {"progress-dialog.perform-output", "Performing the requested report output ..."},
        {"progress-dialog.page-label", "Page: {0}"},
        {"progress-dialog.rows-label", "Row: {0} / {1}"},
        {"progress-dialog.pass-label", "Pass: {0} - Computing function values ..."},
      };
}