/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * JFreeReportResources_ca.java
 * ----------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Marc Casas;
 * Contributor(s):   -;
 *
 * $Id: $
 *
 */
package org.jfree.report.modules.gui.base.resources;

import java.awt.event.KeyEvent;

/**
 * Catalan Language Resources.
 *
 * @author Marc Casas
 */
public class JFreeReportResources_ca extends JFreeReportResources
{
  /**
   * Default Constructor.
   */
  public JFreeReportResources_ca()
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
        {"action.close.name", "Tancar"},
        {"action.close.description", "Tancar finestra de vista prèvia"},
        {"action.close.mnemonic", new Integer(KeyEvent.VK_C)},

        {"action.gotopage.name", "Anar a ..."},
        {"action.gotopage.description", "Visualitzar una pàgina determinada."},

        {"dialog.gotopage.message", "Introdueix un número de pàgina"},
        {"dialog.gotopage.title", "Anar a la pàgina ..."},

        {"action.about.name", "Quan a..."},
        {"action.about.description", "Quan a JFreeReport"},
        {"action.about.mnemonic", new Integer(KeyEvent.VK_A)},

        {"action.firstpage.name", "Inici"},
        {"action.firstpage.description", "Anar a la primera pàgina"},

        {"action.lastpage.name", "Final"},
        {"action.lastpage.description", "Anar a la última pàgina"},

        {"action.back.name", "Anterior"},
        {"action.back.description", "Anar a la pàgina anterior"},

        {"action.forward.name", "Següent"},
        {"action.forward.description", "Anar a la pàgina següent"},

        {"action.zoomIn.name", "Augmentar Zoom"},
        {"action.zoomIn.description", "Augmentar el zoom"},

        {"action.zoomOut.name", "Disminuir Zoom"},
        {"action.zoomOut.description", "Disminuir el zoom"},

        {"preview-frame.title", "Vista preliminar"},

        {"menu.file.name", "Arxiu"},
        {"menu.file.mnemonic", new Character('A')},

        {"menu.help.name", "Ajuda"},
        {"menu.help.mnemonic", new Character('J')},

        {"statusline.pages", "Pàgina {0} de {1}"},
        {"statusline.error", "Error al generar el document: {0}"},
        {"statusline.repaginate", "Calculant els salts de pàgina, un moment."},

        // these are the swing default values ...
        {"FileChooser.acceptAllFileFilterText", "Tots els fitxers (*.*)"},
        {"FileChooser.cancelButtonMnemonic", new Integer(KeyEvent.VK_C)},
        {"FileChooser.cancelButtonText", "Cancel·lar"},
        {"FileChooser.cancelButtonToolTipText", "Cancel·lar el diàleg d'elecció de fitxer"},
        {"FileChooser.detailsViewButtonAccessibleName", "Detalls"},
        {"FileChooser.detailsViewButtonToolTipText", "Detalls"},
        {"FileChooser.directoryDescriptionText", "Directori"},
        {"FileChooser.fileDescriptionText", "Fitxer genèric"},
        {"FileChooser.fileNameLabelMnemonic", new Integer(KeyEvent.VK_N)},
        {"FileChooser.fileNameLabelText", "Nom:"},
        {"FileChooser.filesOfTypeLabelMnemonic", new Integer(KeyEvent.VK_T)},
        {"FileChooser.filesOfTypeLabelText", "Fitxers de tipus:"},
        {"FileChooser.helpButtonMnemonic", new Integer(KeyEvent.VK_J)},
        {"FileChooser.helpButtonText", "Ajuda"},
        {"FileChooser.helpButtonToolTipText", "Ajuda del diàleg d'elecció de fitxer"},
        {"FileChooser.homeFolderAccessibleName", "Inici"},
        {"FileChooser.homeFolderToolTipText", "Inici"},
        {"FileChooser.listViewButtonAccessibleName", "Llista"},
        {"FileChooser.listViewButtonToolTipText", "Llista"},
        {"FileChooser.lookInLabelMnemonic", new Integer(KeyEvent.VK_I)},
        {"FileChooser.lookInLabelText", "Mirar a:"},
        {"FileChooser.newFolderAccessibleName", "Nova carpeta"},
        {"FileChooser.newFolderErrorSeparator", ":"},
        {"FileChooser.newFolderErrorText", "Error creant la nova carpeta"},
        {"FileChooser.newFolderToolTipText", "Crear una nova carpeta"},
        {"FileChooser.openButtonMnemonic", new Integer(KeyEvent.VK_O)},
        {"FileChooser.openButtonText", "Obrir"},
        {"FileChooser.openButtonToolTipText", "Obre el fitxer sel·leccionat"},
        {"FileChooser.saveButtonMnemonic", new Integer(KeyEvent.VK_D)},
        {"FileChooser.saveButtonText", "Desar"},
        {"FileChooser.saveButtonToolTipText", "Desa el fitxer sel·leccionat"},
        {"FileChooser.updateButtonMnemonic", new Integer(KeyEvent.VK_U)},
        {"FileChooser.updateButtonText", "Actualitzar"},
        {"FileChooser.updateButtonToolTipText", "Actualitza la llista de fitxers"},
        {"FileChooser.upFolderAccessibleName", "Amunt"},
        {"FileChooser.upFolderToolTipText", "Puja un nivell"},
        {"FileChooser.openDialogTitleText", "Obrir"},
        {"FileChooser.other.newFolder", "Nova Carpeta"},
        {"FileChooser.other.newFolder.subsequent", "Nova carpeta. {0}"},
        {"FileChooser.saveDialogTitleText", "Desar"},
        {"FileChooser.win32.newFolder", "Nova Carpeta"},
        {"FileChooser.win32.newFolder.subsequent", "Nova carpeta. ({0})"},

        {"OptionPane.yesButtonText", "Si"},
        {"OptionPane.noButtonText", "No"},
        {"OptionPane.okButtonText", "D'acord"},
        {"OptionPane.cancelButtonText", "Cancel·lar"},
        {"OptionPane.titleText", "Elegeix una opció"},

        {"ColorChooser.cancelText", "Cancel·lar"},
        {"ColorChooser.hsbNameText", "HSB"},
        {"ColorChooser.hsbHueText", "H"},
        {"ColorChooser.hsbSaturationText", "S"},
        {"ColorChooser.hsbBrightnessText", "B"},
        {"ColorChooser.hsbRedText", "R"},
        {"ColorChooser.hsbGreenText", "G"},
        {"ColorChooser.hsbBlueText", "B"},
        {"ColorChooser.okText", "D'acord"},
        {"ColorChooser.previewText", "Vista prèvia"},
        {"ColorChooser.resetText", "Valors per defecte"},
        {"ColorChooser.rgbNameText", "RGB"},
        {"ColorChooser.rgbRedMnemonic", new Integer(KeyEvent.VK_R)},
        {"ColorChooser.rgbRedText", "Red"},
        {"ColorChooser.rgbGreenMnemonic", new Integer(KeyEvent.VK_G)},
        {"ColorChooser.rgbGreenText", "Green"},
        {"ColorChooser.rgbBlueMnemonic", new Integer(KeyEvent.VK_B)},
        {"ColorChooser.rgbBlueText", "Blue"},
        {"ColorChooser.sampleText", "Text de proves Text de proves"},
        {"ColorChooser.swatchesNameText", "Swatches"},
        {"ColorChooser.swatchesRecentText", "Recent:"},

        {"InternalFrameTitlePane.closeButtonAccessibleName", "Tanca"},
        {"InternalFrameTitlePane.iconifyButtonAccessibleName", "Iconitza"},
        {"InternalFrameTitlePane.maximizeButtonAccessibleName", "Maximitza"},

        {"FormView.resetButtonText", "Valors per defecte"},
        {"FormView.submitButtonText", "Envia consulta"},

        {"AbstractButton.clickText", "click"},

        {"AbstractDocument.additionText", "addition"},
        {"AbstractDocument.deletionText", "deletion"},
        {"AbstractDocument.redoText", "Refés"},
        {"AbstractDocument.styleChangeText", "style change"},
        {"AbstractDocument.undoText", "Desfés"},

        {"AbstractUndoableEdit.redoText", "Refés"},
        {"AbstractUndoableEdit.undoText", "Desfés"},

        {"ProgressMonitor.progressText", "Progrés..."},

        {"SplitPane.leftButtonText", "botó esquerra"},
        {"SplitPane.rightButtonText", "botó dret"},

        // progress dialog defaults ...
        {"progress-dialog.prepare-layout", "Preparant el disseny de la sortida."},
        {"progress-dialog.perform-output", "Fent la sortida de l'informe..."},
        {"progress-dialog.page-label", "Pàgina: {0}"},
        {"progress-dialog.rows-label", "Fila: {0} / {1}"},
        {"progress-dialog.pass-label", "Passada: {0} - Càlculant els valors de les funcions..."},
     };


  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{JFreeReportResources.class.getName(), "ca"});
  }


}