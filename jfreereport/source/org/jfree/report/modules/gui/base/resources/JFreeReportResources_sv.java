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
 * $Id: JFreeReportResources_sv.java,v 1.6 2003/08/31 19:27:57 taqua Exp $
 *
 */
package org.jfree.report.modules.gui.base.resources;

import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

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
   * Returns the array of strings in the resource bundle.
   *
   * @return an array of localised resources.
   */
  public Object[][] getContents()
  {
    return CONTENTS;
  }

  /** The resources to be localised. */
  private static final Object[][] CONTENTS =
      {
        {"action.close.name", "St�nga"},
        {"action.close.description", "St�nga f�rhandsgransknings-f�nster"},
        {"action.close.mnemonic", new Integer(KeyEvent.VK_C)},
        {"action.close.accelerator", createMenuKeystroke(KeyEvent.VK_X)},

        {"action.gotopage.name", "G� till sida ..."},
        {"action.gotopage.description", "Se en sida direkt"},
        {"action.gotopage.mnemonic", new Integer(KeyEvent.VK_G)},
        {"action.gotopage.accelerator", createMenuKeystroke(KeyEvent.VK_G)},

        {"dialog.gotopage.message", "Ange en sida nummer"},
        {"dialog.gotopage.title", "G� till sida"},

        {"action.about.name", "Om..."},
        {"action.about.description", "Information om applikationen"},
        {"action.about.mnemonic", new Integer(KeyEvent.VK_A)},

        {"action.firstpage.name", "Hem"},
        {"action.firstpage.description", "Bl�ddra till den f�rsta sidan"},
        {"action.firstpage.description", "Switch to the first page"},
        {"action.firstpage.accelerator", KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0)},

        {"action.back.name", "Bl�ddra bak�t"},
        {"action.back.description", "Bl�ddra till den f�reg�ende sidan"},
        {"action.back.mnemonic", new Integer(KeyEvent.VK_PAGE_UP)},
        {"action.back.accelerator", KeyStroke.getKeyStroke("PAGE_UP")},

        {"action.forward.name", "Bl�ddra fram�t"},
        {"action.forward.description", "Bl�ddra till den n�sta sidan"},
        {"action.forward.mnemonic", new Integer(KeyEvent.VK_PAGE_DOWN)},
        {"action.forward.accelerator", KeyStroke.getKeyStroke("PAGE_DOWN")},

        {"action.lastpage.name", "Sista sida"},
        {"action.lastpage.description", "Bl�ddra till den sista sidan"},
        {"action.lastpage.mnemonic", new Integer(KeyEvent.VK_END)},
        {"action.lastpage.accelerator", KeyStroke.getKeyStroke(KeyEvent.VK_END, 0)},

        {"action.zoomIn.name", "Zooma in"},
        {"action.zoomIn.description", "F�rst�rka zoomen"},
        {"action.zoomIn.mnemonic", new Integer(KeyEvent.VK_PLUS)},
        {"action.zoomIn.accelerator", KeyStroke.getKeyStroke("PLUS")},

        {"action.zoomOut.name", "Zooma ut"},
        {"action.zoomOut.description", "Minska zoomen"},
        {"action.zoomOut.mnemonic", new Integer(KeyEvent.VK_MINUS)},
        {"action.zoomOut.accelerator", KeyStroke.getKeyStroke("MINUS")},

        // preview frame...
        {"preview-frame.title", "F�rhandsgranska"},

        // menu labels...
        {"menu.file.name", "Fil"},
        {"menu.file.mnemonic", new Character('F')},

        {"menu.navigation.name", "Navigation"},
        {"menu.navigation.mnemonic", new Character('N')},

        {"menu.zoom.name", "Zooma"},
        {"menu.zoom.mnemonic", new Character('Z')},

        {"menu.help.name", "Hj�lp"},
        {"menu.help.mnemonic", new Character('H')},

        {"statusline.pages", "Sida {0} av {1}"},
        {"statusline.error", "Reportgeneration skapade ett fel: {0}"},
        {"statusline.repaginate", "Ber�kna sida-brytning, var sn�ll och v�nta."},

        // these are the swing default values ...
        {"FileChooser.acceptAllFileFilterText", "Alla filer (*.*)"},
        {"FileChooser.cancelButtonMnemonic", new Integer(KeyEvent.VK_C)},
        {"FileChooser.cancelButtonText", "Avbryt"},
        {"FileChooser.cancelButtonToolTipText", "Avbryt fil fil v�ljare"},
        {"FileChooser.detailsViewButtonAccessibleName", "Detaljerna"},
        {"FileChooser.detailsViewButtonToolTipText", "Detaljerna"},
        {"FileChooser.directoryDescriptionText", "Katalog"},
        {"FileChooser.fileDescriptionText", "Generisk fil"},
        {"FileChooser.fileNameLabelMnemonic", new Integer(KeyEvent.VK_N)},
        {"FileChooser.fileNameLabelText", "Fil namn:"},
        {"FileChooser.filesOfTypeLabelMnemonic", new Integer(KeyEvent.VK_T)},
        {"FileChooser.filesOfTypeLabelText", "Filer av typ:"},
        {"FileChooser.helpButtonMnemonic", new Integer(KeyEvent.VK_H)},
        {"FileChooser.helpButtonText", "Hj�lp"},
        {"FileChooser.helpButtonToolTipText", "Filv�ljare hj�lp"},
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
        {"FileChooser.openButtonText", "�ppna"},
        {"FileChooser.openButtonToolTipText", "�ppna den valda filen"},
        {"FileChooser.saveButtonMnemonic", new Integer(KeyEvent.VK_S)},
        {"FileChooser.saveButtonText", "Spara"},
        {"FileChooser.saveButtonToolTipText", "Spara den valda filen"},
        {"FileChooser.updateButtonMnemonic", new Integer(KeyEvent.VK_U)},
        {"FileChooser.updateButtonText", "Updatera"},
        {"FileChooser.updateButtonToolTipText", "Updatera katalog lista"},
        {"FileChooser.upFolderAccessibleName", "Upp"},
        {"FileChooser.upFolderToolTipText", "Upp en niv�"},

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
        {"ColorChooser.previewText", "F�rhandsgranska"},
        {"ColorChooser.resetText", "Omst�lla"},
        {"ColorChooser.rgbNameText", "RGB"},
        {"ColorChooser.rgbRedMnemonic", new Integer(KeyEvent.VK_R)},
        {"ColorChooser.rgbRedText", "R�d"},
        {"ColorChooser.rgbGreenMnemonic", new Integer(KeyEvent.VK_G)},
        {"ColorChooser.rgbGreenText", "Gr�n"},
        {"ColorChooser.rgbBlueMnemonic", new Integer(KeyEvent.VK_B)},
        {"ColorChooser.rgbBlueText", "Bl�"},
        {"ColorChooser.sampleText", "Exempel Text  Exempel Text"},
        {"ColorChooser.swatchesNameText", "Swatches"},
        {"ColorChooser.swatchesRecentText", "Senaste:"},

        {"InternalFrameTitlePane.closeButtonAccessibleName", "St�ng"},
        {"InternalFrameTitlePane.iconifyButtonAccessibleName", "Minimalt"},
        {"InternalFrameTitlePane.maximizeButtonAccessibleName", "Maximalt"},

        {"FormView.resetButtonText", "Omst�lla"},
        {"FormView.submitButtonText", "Ange fr�gast�llningen"},

        {"AbstractButton.clickText", "klicka"},

        {"AbstractDocument.additionText", "l�gga till"},
        {"AbstractDocument.deletionText", "ta bort"},
        {"AbstractDocument.redoText", "G�r om"},
        {"AbstractDocument.styleChangeText", "styl f�r�ndring"},
        {"AbstractDocument.undoText", "�ngra"},

        {"AbstractUndoableEdit.redoText", "G�r om"},
        {"AbstractUndoableEdit.undoText", "�ngra"},

        {"ProgressMonitor.progressText", "F�rlopp..."},

        {"SplitPane.leftButtonText", "v�nster knapp"},
        {"SplitPane.rightButtonText", "h�ger knapp"},

        // progress dialog defaults ...
        {"progress-dialog.prepare-layout", "F�rbereda formaten."},
        {"progress-dialog.perform-output", "Rapportarbetet p�g�ng ..."},
        {"progress-dialog.page-label", "Sida: {0}"},
        {"progress-dialog.rows-label", "Rad: {0} / {1}"},
        {"progress-dialog.pass-label", "Pass: {0} - Ber�kna v�rden ..."},
      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    new JFreeReportResources_sv().generateResourceProperties("swedish");
    System.exit(0);
//    ResourceCompareTool.main(new String[]{JFreeReportResources.class.getName(), "sv"});
  }
}
