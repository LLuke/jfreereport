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
 * JFreeReportResources_nl.java
 * ----------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Hendri Smit;
 * Contributor(s):   -;
 *
 * $Id: JFreeReportResources_nl.java,v 1.3 2003/07/18 17:56:38 taqua Exp $
 */
package org.jfree.report.modules.gui.base.resources;

import java.awt.event.KeyEvent;

/**
 * Dutch Language Resources.
 *
 * @author Hendri Smit
 */
public class JFreeReportResources_nl extends JFreeReportResources
{
  public JFreeReportResources_nl()
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
        {"action.close.name", "Sluiten"},
        {"action.close.description", "Sluit afdrukvoorbeeld"},
        {"action.close.mnemonic", new Integer(KeyEvent.VK_S)},

        {"action.gotopage.name", "Ga naar..."},
        {"action.gotopage.description", "Ga naar opgegeven pagina"},

        {"dialog.gotopage.message", "Geef pagina nummer"},
        {"dialog.gotopage.title", "Ga naar pagina ..."},

        {"action.about.name", "Info..."},
        {"action.about.description", "Informatie over de applicatie"},
        {"action.about.mnemonic", new Integer(KeyEvent.VK_I)},

        {"action.firstpage.name", "Begin"},
        {"action.firstpage.description", "Ga naar de eerste pagina"},

        {"action.lastpage.name", "Einde"},
        {"action.lastpage.description", "Ga naar de laatste pagina"},

        {"action.back.name", "Terug"},
        {"action.back.description", "Ga naar de vorige pagina"},

        {"action.forward.name", "Verder"},
        {"action.forward.description", "Ga naar de volgende pagina"},

        {"action.zoomIn.name", "Vergroten"},
        {"action.zoomIn.description", "Inzoomen"},

        {"action.zoomOut.name", "Verkleinen"},
        {"action.zoomOut.description", "Uitzoomen"},

        {"preview-frame.title", "Afdrukvoorbeeld"},

        {"menu.file.name", "Bestand"},
        {"menu.file.mnemonic", new Character('B')},

        {"menu.help.name", "Help"},
        {"menu.help.mnemonic", new Character('H')},

        {"menu.navigation.name", "Navigatie"},
        {"menu.navigation.mnemonic", new Character ('N')},

        {"menu.zoom.name", "In-/Uitzoomen"},
        {"menu.zoom.mnemonic", new Character ('Z')},

        {"statusline.pages", "Pagina {0} van {1}"},
        {"statusline.error", "Er is een fout ontstaan in de report generatie: {0}"},
        {"statusline.repaginate", "Paginanummering berekenen..."},

        {"FileChooser.acceptAllFileFilterText", "Alle Bestanden (*.*)"},
        {"FileChooser.cancelButtonMnemonic", new Integer(KeyEvent.VK_C)},
        {"FileChooser.cancelButtonText", "Annuleren"},
        {"FileChooser.cancelButtonToolTipText", "Sluiten"},
        {"FileChooser.detailsViewButtonAccessibleName", "Details"},
        {"FileChooser.detailsViewButtonToolTipText", "Details"},
        {"FileChooser.directoryDescriptionText", "Map"},
        {"FileChooser.fileDescriptionText", "Generiek Bestand"},
        {"FileChooser.fileNameLabelMnemonic", new Integer(KeyEvent.VK_N)},
        {"FileChooser.fileNameLabelText", "Bestandsnaam:"},
        {"FileChooser.filesOfTypeLabelMnemonic", new Integer(KeyEvent.VK_T)},
        {"FileChooser.filesOfTypeLabelText", "Bestandstypen:"},
        {"FileChooser.helpButtonMnemonic", new Integer(KeyEvent.VK_H)},
        {"FileChooser.helpButtonText", "Help"},
        {"FileChooser.helpButtonToolTipText", "Help"},
        {"FileChooser.homeFolderAccessibleName", "Begin"},
        {"FileChooser.homeFolderToolTipText", "Beginmap"},
        {"FileChooser.listViewButtonAccessibleName", "Lijst"},
        {"FileChooser.listViewButtonToolTipText", "Lijst"},
        {"FileChooser.lookInLabelMnemonic", new Integer(KeyEvent.VK_I)},
        {"FileChooser.lookInLabelText", "Zoek in:"},
        {"FileChooser.newFolderAccessibleName", "Nieuwe Map"},
        {"FileChooser.newFolderErrorSeparator", ": "},
        {"FileChooser.newFolderErrorText", "Fout tijdens creëren nieuwe map"},
        {"FileChooser.newFolderToolTipText", "Maak Nieuwe Map"},
        {"FileChooser.openButtonMnemonic", new Integer(KeyEvent.VK_O)},
        {"FileChooser.openButtonToolTipText", "Open geselecteerd bestand"},
        {"FileChooser.openButtonText", "Openen"},
        {"FileChooser.openDialogTitleText", "Openen"},
        {"FileChooser.saveButtonMnemonic", new Integer(KeyEvent.VK_S)},
        {"FileChooser.saveButtonText", "Opslaan"},
        {"FileChooser.saveButtonToolTipText", "Sla geselecteerd bestand op"},
        {"FileChooser.saveDialogTitleText", "Opslaan"},
        {"FileChooser.updateButtonText", "Vernieuwen"},
        {"FileChooser.updateButtonMnemonic", new Integer(KeyEvent.VK_U)},
        {"FileChooser.updateButtonToolTipText", "Vernieuw map inhoud"},
        {"FileChooser.upFolderAccessibleName", "Omhoog"},
        {"FileChooser.upFolderToolTipText", "Eén niveau omhoog"},
        {"FileChooser.other.newFolder", "NieuweMap"},
        {"FileChooser.other.newFolder.subsequent", "NieuweMap.{0}"},
        {"FileChooser.win32.newFolder", "Nieuwe Map"},
        {"FileChooser.win32.newFolder.subsequent", "Nieuwe Map ({0})"},

        {"OptionPane.cancelButtonText", "Annuleren"},
        {"OptionPane.noButtonText", "Nee"},
        {"OptionPane.okButtonText", "OK"},
        {"OptionPane.titleText", "Selecteer een optie"},
        {"OptionPane.yesButtonText", "Ja"},

        {"ColorChooser.cancelText", "Annuleren"},
        {"ColorChooser.hsbNameText", "HSB"},
        {"ColorChooser.hsbHueText", "H"},
        {"ColorChooser.hsbSaturationText", "S"},
        {"ColorChooser.hsbBrightnessText", "B"},
        {"ColorChooser.hsbRedText", "R"},
        {"ColorChooser.hsbGreenText", "G"},
        {"ColorChooser.hsbBlueText", "B"},
        {"ColorChooser.okText", "OK"},
        {"ColorChooser.previewText", "Voorbeeld"},
        {"ColorChooser.resetText", "Reset"},
        {"ColorChooser.rgbNameText", "RGB"},
        {"ColorChooser.rgbRedMnemonic", new Integer(KeyEvent.VK_R)},
        {"ColorChooser.rgbRedText", "Rood"},
        {"ColorChooser.rgbGreenMnemonic", new Integer(KeyEvent.VK_G)},
        {"ColorChooser.rgbGreenText", "Groen"},
        {"ColorChooser.rgbBlueMnemonic", new Integer(KeyEvent.VK_B)},
        {"ColorChooser.rgbBlueText", "Blauw"},
        {"ColorChooser.sampleText", "Voorbeeld tekst  Voorbeeld tekst"},
        {"ColorChooser.swatchesNameText", "Kleurenpalet"},
        {"ColorChooser.swatchesRecentText", "Laatst gebruikt:"},

        {"InternalFrameTitlePane.closeButtonAccessibleName", "Sluiten"},
        {"InternalFrameTitlePane.iconifyButtonAccessibleName", "Iconifiseren"},
        {"InternalFrameTitlePane.maximizeButtonAccessibleName", "Maximaliseren"},

        {"FormView.resetButtonText", "Wissen"},
        {"FormView.submitButtonText", "Versturen"},

        {"AbstractButton.clickText", "klik"},

        {"AbstractDocument.additionText", "toevoeging"},
        {"AbstractDocument.deletionText", "verwijdering"},
        {"AbstractDocument.redoText", "Opnieuw doen"},
        {"AbstractDocument.styleChangeText", "stijl verandering"},
        {"AbstractDocument.undoText", "Ongedaan maken"},

        {"AbstractUndoableEdit.redoText", "Opnieuw doen"},
        {"AbstractUndoableEdit.undoText", "Ongedaan maken"},

        {"ProgressMonitor.progressText", "Voortgang..."},

        {"SplitPane.leftButtonText", "linker knop"},
        {"SplitPane.rightButtonText", "rechter knop"}
      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{JFreeReportResources.class.getName(), "nl"});
  }


}
