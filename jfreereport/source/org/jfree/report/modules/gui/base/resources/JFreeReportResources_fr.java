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
 * JFreeReportResources_fr.java
 * ----------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  PR;
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: JFreeReportResources_fr.java,v 1.7 2003/11/01 19:52:27 taqua Exp $
 *
 */
package org.jfree.report.modules.gui.base.resources;

import java.awt.event.KeyEvent;

/**
 * French Language Resources.
 *
 * @author PR
 */
public class JFreeReportResources_fr extends JFreeReportResources
{
  /**
   * Default Constructor.
   */
  public JFreeReportResources_fr()
  {
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

  /** The resources to be localised. */
  private static final Object[][] CONTENTS =
      {
        {"action.close.name", "Fermer"},
        {"action.close.description", "Fermer l'aper�u avant impression"},
        {"action.close.mnemonic", new Integer(KeyEvent.VK_F)},

        {"action.gotopage.name", "Aller � la page ..."},
        {"action.gotopage.description", "Voir une page directement"},
        {"action.gotopage.mnemonic", new Integer(KeyEvent.VK_A)},

        {"dialog.gotopage.message", "Entrer un num�ro de page"},
        {"dialog.gotopage.title", "Aller � la page"},

        {"action.about.name", "A propos..."},
        {"action.about.description", "Information � propos de l'application"},
        {"action.about.mnemonic", new Integer(KeyEvent.VK_A)},

        {"action.firstpage.name", "D�but"},
        {"action.firstpage.description", "Aller � la premi�re page"},

        {"action.back.name", "Pr�c�dent"},
        {"action.back.description", "Aller � la page pr�c�dente"},

        {"action.forward.name", "Suivant"},
        {"action.forward.description", "Aller � la page suivante"},

        {"action.lastpage.name", "Fin"},
        {"action.lastpage.description", "Aller � la derni�re page"},

        {"action.zoomIn.name", "Agrandir"},
        {"action.zoomIn.description", "Agrandir"},

        {"action.zoomOut.name", "R�tr�cir"},
        {"action.zoomOut.description", "R�tr�cir"},

        // preview frame...
        {"preview-frame.title", "Aper�u avant impression"},

        // menu labels...
        {"menu.file.name", "Fichier"},
        {"menu.file.mnemonic", new Character('F')},

        {"menu.navigation.name", "Navigation"},
        {"menu.navigation.mnemonic", new Character('N')},

        {"menu.zoom.name", "Zoom"},
        {"menu.zoom.mnemonic", new Character('Z')},

        {"menu.help.name", "Aide"},
        {"menu.help.mnemonic", new Character('i')},

        {"statusline.pages", "Page {0} de {1}"},
        {"statusline.error", "Reportgeneration � produit une erreur: {0}"},
        {"statusline.repaginate", "Calcul des sauts de page, veuillez patienter."},


        // these are the swing default values ...
        {"FileChooser.acceptAllFileFilterText", "Tous les fichiers (*.*)"},
        {"FileChooser.cancelButtonMnemonic", new Integer(KeyEvent.VK_A)},
        {"FileChooser.cancelButtonText", "Annuler"},
        {"FileChooser.cancelButtonToolTipText", 
          "Quitte la boite de dialogue de s�lection d�un fichier"},
        {"FileChooser.detailsViewButtonAccessibleName", "D�tails"},
        {"FileChooser.detailsViewButtonToolTipText", "D�tails"},
        {"FileChooser.directoryDescriptionText", "Chemin"},
        {"FileChooser.fileDescriptionText", "Fichier g�n�rique"},
        {"FileChooser.fileNameLabelMnemonic", new Integer(KeyEvent.VK_N)},
        {"FileChooser.fileNameLabelText", "Nom de fichier:"},
        {"FileChooser.filesOfTypeLabelMnemonic", new Integer(KeyEvent.VK_T)},
        {"FileChooser.filesOfTypeLabelText", "Type de fichier:"},
        {"FileChooser.helpButtonMnemonic", new Integer(KeyEvent.VK_A)},
        {"FileChooser.helpButtonText", "Aide"},
        {"FileChooser.helpButtonToolTipText", 
          "Aide sur la boite de dialogue de s�lection d�un fichier"},
        {"FileChooser.homeFolderAccessibleName", "Home"},
        {"FileChooser.homeFolderToolTipText", "Home"},
        {"FileChooser.listViewButtonAccessibleName", "Liste"},
        {"FileChooser.listViewButtonToolTipText", "Liste"},
        {"FileChooser.lookInLabelMnemonic", new Integer(KeyEvent.VK_R)},
        {"FileChooser.lookInLabelText", "Regarder dans:"},
        {"FileChooser.newFolderAccessibleName", "Nouveau dossier"},
        {"FileChooser.newFolderErrorSeparator", ":"},
        {"FileChooser.newFolderErrorText", "Erreur lors de la cr�ation du nouveau dossier"},
        {"FileChooser.newFolderToolTipText", "Cr�er un nouveau dossier"},
        {"FileChooser.openButtonMnemonic", new Integer(KeyEvent.VK_O)},
        {"FileChooser.openButtonText", "Ouvrir"},
        {"FileChooser.openButtonToolTipText", "Ouvrir le fichier s�lectionn�"},
        {"FileChooser.saveButtonMnemonic", new Integer(KeyEvent.VK_S)},
        {"FileChooser.saveButtonText", "Sauvegarder"},
        {"FileChooser.saveButtonToolTipText", "Sauvegarder le fichier s�lectionn�"},
        {"FileChooser.updateButtonMnemonic", new Integer(KeyEvent.VK_M)},
        {"FileChooser.updateButtonText", "Actualiser"},
        {"FileChooser.updateButtonToolTipText", "Actualiser le dossier"},
        {"FileChooser.upFolderAccessibleName", "Monter"},
        {"FileChooser.upFolderToolTipText", "Monter d'un niveau"},
        {"FileChooser.openDialogTitleText", "Ouvrir"},
        {"FileChooser.other.newFolder", "Nouveau dossier"},
        {"FileChooser.other.newFolder.subsequent", "Nouveau dossier. {0}"},
        {"FileChooser.saveDialogTitleText", "Sauvegarder"},
        {"FileChooser.win32.newFolder", "Nouveau dossier"},
        {"FileChooser.win32.newFolder.subsequent", "Nouveau dossier. ({0})"},

        {"OptionPane.yesButtonText", "Oui"},
        {"OptionPane.noButtonText", "Non"},
        {"OptionPane.okButtonText", "OK"},
        {"OptionPane.cancelButtonText", "Annuler"},
        {"OptionPane.titleText", "Choisir une option"},

        {"ColorChooser.cancelText", "Annuler"},
        {"ColorChooser.hsbNameText", "HSB"},
        {"ColorChooser.hsbHueText", "H"},
        {"ColorChooser.hsbSaturationText", "S"},
        {"ColorChooser.hsbBrightnessText", "B"},
        {"ColorChooser.hsbRedText", "R"},
        {"ColorChooser.hsbGreenText", "V"},
        {"ColorChooser.hsbBlueText", "B"},
        {"ColorChooser.okText", "OK"},
        {"ColorChooser.previewText", "Aper�u"},
        {"ColorChooser.resetText", "Reset"},
        {"ColorChooser.rgbNameText", "RVB"},
        {"ColorChooser.rgbRedMnemonic", new Integer(KeyEvent.VK_R)},
        {"ColorChooser.rgbRedText", "Rouge"},
        {"ColorChooser.rgbGreenMnemonic", new Integer(KeyEvent.VK_V)},
        {"ColorChooser.rgbGreenText", "Vert"},
        {"ColorChooser.rgbBlueMnemonic", new Integer(KeyEvent.VK_B)},
        {"ColorChooser.rgbBlueText", "Bleu"},
        {"ColorChooser.sampleText", "Exemple Exemple"},
        //{"ColorChooser.swatchesNameText", "Swatches"}, i don t see what it means
        {"ColorChooser.swatchesRecentText", "R�cent:"},

        {"InternalFrameTitlePane.closeButtonAccessibleName", "Fermer"},
        {"InternalFrameTitlePane.iconifyButtonAccessibleName", "Iconifier"},
        {"InternalFrameTitlePane.maximizeButtonAccessibleName", "Maximiser"},

        {"FormView.resetButtonText", "Reset"},
        {"FormView.submitButtonText", "Envoyer une requ�te"},

        {"AbstractButton.clickText", "click"},

        //{"AbstractDocument.additionText", "addition"}, i don t know where it is used
        //{"AbstractDocument.deletionText", "deletion"}, i don t know where it is used
        {"AbstractDocument.redoText", "R�p�ter"},
        //{"AbstractDocument.styleChangeText", "style change"}, i don t know where it is used
        {"AbstractDocument.undoText", "Annuler"},

        {"AbstractUndoableEdit.redoText", "R�p�ter"},
        {"AbstractUndoableEdit.undoText", "Annuler"},

        {"ProgressMonitor.progressText", "Progression..."},

        {"SplitPane.leftButtonText", "Bouton gauche"},
        {"SplitPane.rightButtonText", "Bouton droit"},

        // progress dialog defaults ...
        {"progress-dialog.prepare-layout", "Pr�paration de la disposition pour le rendu."},
        {"progress-dialog.perform-output", "Ex�cution du rendu pour le rapport demand� ..."},
        {"progress-dialog.page-label", "Page: {0}"},
        {"progress-dialog.rows-label", "Ligne: {0} / {1}"},
        {"progress-dialog.pass-label", "Pass: {0} - Computing function values ..."},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    new JFreeReportResources_fr().generateResourceProperties("french");
    System.exit(0);
//    ResourceCompareTool.main(new String[]{JFreeReportResources.class.getName(), "fr"});
  }


}
