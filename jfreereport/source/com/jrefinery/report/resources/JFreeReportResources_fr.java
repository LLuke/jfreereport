/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * JFreeReportResources_fr.java
 * ----------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  PR;
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: JFreeReportResources_fr.java,v 1.6 2003/05/16 13:24:41 taqua Exp $
 *
 */
package com.jrefinery.report.resources;

import java.awt.event.KeyEvent;

/**
 * French Language Resources.
 *
 * @author PR
 */
public class JFreeReportResources_fr extends JFreeReportResources
{
  /**
   * Returns an array of localised resources.
   *
   * @return an array of localised resources.
   */
  public Object[][] getContents ()
  {
    return CONTENTS;
  }

  /** The resources to be localised. */
  private static final Object[][] CONTENTS =
          {
            {"action.save-as.name", "Enregistrer en PDF..."},
            {"action.save-as.description", "Enregistrer au format PDF"},
            {"action.save-as.mnemonic", new Integer (KeyEvent.VK_P)},

            {"action.export-to-excel.name", "Exporter en Excel..."},
            {"action.export-to-excel.description", "Enregistrer au format MS-Excel"},
            {"action.export-to-excel.mnemonic", new Integer (KeyEvent.VK_E)},

            {"action.export-to-html.name", "Exporter en html..."},
            {"action.export-to-html.description", "Enregistrer au format HTML"},
            {"action.export-to-html.mnemonic", new Integer (KeyEvent.VK_H)},

            {"action.export-to-csv.name", "Exporter en CSV..."},
            {"action.export-to-csv.description", "Enregistrer au format CSV"},
            {"action.export-to-csv.mnemonic", new Integer (KeyEvent.VK_C)},

            {"action.export-to-plaintext.name", "Enregistrer en text..."},
            {"action.export-to-plaintext.description", "Enregistrer au format PlainText"},
            {"action.export-to-plaintext.mnemonic", new Integer (KeyEvent.VK_T)},

            {"action.page-setup.name", "Mise en page"},
            {"action.page-setup.description", "Mise en page"},
            {"action.page-setup.mnemonic", new Integer (KeyEvent.VK_M)},

            {"action.print.name", "Impression..."},
            {"action.print.description", "Impression du rapport"},
            {"action.print.mnemonic", new Integer (KeyEvent.VK_I)},

            {"action.close.name", "Fermer"},
            {"action.close.description", "Fermer l'aper�u avant impression"},
            {"action.close.mnemonic", new Integer (KeyEvent.VK_F)},

            {"action.gotopage.name", "Aller � la page ..."},
            {"action.gotopage.description", "Voir une page directement"},
            {"action.gotopage.mnemonic", new Integer (KeyEvent.VK_A)},

            {"dialog.gotopage.message", "Entrer un num�ro de page"},
            {"dialog.gotopage.title", "Aller � la page"},

            {"action.about.name", "A propos..."},
            {"action.about.description", "Information � propos de l'application"},
            {"action.about.mnemonic", new Integer (KeyEvent.VK_A)},

            {"action.firstpage.name", "D�but"},
//            {"action.firstpage.mnemonic", new Integer (KeyEvent.VK_HOME)},
            {"action.firstpage.description", "Aller � la premi�re page"},

            {"action.back.name", "Pr�c�dent"},
            {"action.back.description", "Aller � la page pr�c�dente"},
//            {"action.back.mnemonic", new Integer (KeyEvent.VK_PAGE_UP)},

            {"action.forward.name", "Suivant"},
            {"action.forward.description", "Aller � la page suivante"},
//            {"action.forward.mnemonic", new Integer (KeyEvent.VK_PAGE_DOWN)},

            {"action.lastpage.name", "Fin"},
            {"action.lastpage.description", "Aller � la derni�re page"},
//            {"action.lastpage.mnemonic", new Integer (KeyEvent.VK_END)},

            {"action.zoomIn.name", "Agrandir"},
            {"action.zoomIn.description", "Agrandir"},
//            {"action.zoomIn.mnemonic", new Integer (KeyEvent.VK_PLUS)},

            {"action.zoomOut.name", "R�tr�cir"},
            {"action.zoomOut.description", "R�tr�cir"},
//            {"action.zoomOut.mnemonic", new Integer (KeyEvent.VK_MINUS)},

            // preview frame...
            {"preview-frame.title", "Aper�u avant impression"},

            // menu labels...
            {"menu.file.name", "Fichier"},
            {"menu.file.mnemonic", new Character ('F')},

            {"menu.navigation.name", "Navigation"},
            {"menu.navigation.mnemonic", new Character ('N')},

            {"menu.zoom.name", "Zoom"},
            {"menu.zoom.mnemonic", new Character ('Z')},

            {"menu.help.name", "Aide"},
            {"menu.help.mnemonic", new Character ('A')},

            {"file.save.pdfdescription", "Documents PDF"},
            {"statusline.pages", "Page {0} de {1}"},
            {"statusline.error", "Reportgeneration � produit une erreur: {0}"},
            {"statusline.repaginate", "Calcule de la coupure des pages, veuillez patienter."},
            {"error.processingfailed.title", "Echec du traitement du Report"},
            {"error.processingfailed.message", "Erreur lors du traitement de ce rapport: {0}"},
            {"error.savefailed.message", "Erreur durant l'enregistrement en PDF : {0}"},
            {"error.savefailed.title", "Erreur durant la sauvegarde"},
            {"error.printfailed.message", "Erreur � l'impression du rapport: {0}"},
            {"error.printfailed.title", "Erreur � l'impression"},
            {"error.validationfailed.message", 
                "Erreur pendant la validation des entr�es utilisateur."},
            {"error.validationfailed.title", "Erreur de validation"},
            
            {"tabletarget.page", "Page {0}"},

            {"pdfsavedialog.dialogtitle", "Enregistrement du rapport en PDF ..."},
            {"pdfsavedialog.filename", "Nom du fichier"},
            {"pdfsavedialog.author", "Auteur"},
            {"pdfsavedialog.title", "Titre"},
            {"pdfsavedialog.selectFile", "S�lectionner un fichier"},
            {"pdfsavedialog.security", "Param�tres de s�curit� et de chiffrage"},
            {"pdfsavedialog.encoding", "Encodage"},

            {"pdfsavedialog.securityNone", "Pas de s�curit�"},
            {"pdfsavedialog.security40bit", "Chiffrage en 40 bits"},
            {"pdfsavedialog.security128bit", "Chiffrage en 128 bits"},
            {"pdfsavedialog.userpassword", "Mot de passe utilisateur"},
            {"pdfsavedialog.userpasswordconfirm", "Confirmer"},
            {"pdfsavedialog.userpasswordNoMatch", "Le mot de passe ne correspond pas."},
            {"pdfsavedialog.ownerpassword", "Mot de passe du propri�taire"},
            {"pdfsavedialog.ownerpasswordconfirm", "Confirmer"},
            {"pdfsavedialog.ownerpasswordNoMatch", 
                "Le mot de passe du propri�taire est incorrect."},
            {"pdfsavedialog.ownerpasswordEmpty", 
                "Le mot de passe du propri�taire est vide. Les utilisateurs "
             + "pourront modifier la s�curit�. Continuer ?" },

            {"pdfsavedialog.warningTitle", "Attention"},
            {"pdfsavedialog.errorTitle", "Erreur"},
            {"pdfsavedialog.targetIsEmpty", "Veuillez sp�cifier un chemin pour le PDF."},
            {"pdfsavedialog.targetIsNoFile", "Le chemin sp�cifi� est incorrect."},
            {"pdfsavedialog.targetIsNotWritable", "Le fichier s�lectionner est en lecture seule."},
            {"pdfsavedialog.targetOverwriteConfirmation",
                "Le fichier ''{0}'' existe. Voulez vous l'�craser?"},
            {"pdfsavedialog.targetOverwriteTitle", "Ecraser le fichier?"},


            {"pdfsavedialog.allowCopy", "Autoriser la copie"},
            {"pdfsavedialog.allowPrinting", "Autoriser l'impression"},
            {"pdfsavedialog.allowDegradedPrinting", "Autoriser les impressions de d�grad�s"},
            {"pdfsavedialog.allowScreenreader", "Autoriser la visualisation"},
            {"pdfsavedialog.allowAssembly", "Autoriser le (R�-)assemblage"},
            {"pdfsavedialog.allowModifyContents", "Autoriser les modifications du contenu"},
            {"pdfsavedialog.allowModifyAnnotations", "Autoriser les modifications des annotations"},
            {"pdfsavedialog.allowFillIn", "Autoriser le remplissage des formules"},

            {"pdfsavedialog.option.noprinting", "Aucune impression"},
            {"pdfsavedialog.option.degradedprinting", "Qualit� basse d'impression"},
            {"pdfsavedialog.option.fullprinting", "Impression autoris�e"},

            {"pdfsavedialog.cancel", "Annuler"},
            {"pdfsavedialog.confirm", "Confirmer"},
            
            {"excelexportdialog.dialogtitle", "Exporter le rapport vers un fichier Excel..."},
            {"excelexportdialog.filename", "Nom de fichier"},
            {"excelexportdialog.author", "Auteur"},
            {"excelexportdialog.title", "Titre"},
            {"excelexportdialog.selectFile", "S�lectionner un fichier"},

            {"excelexportdialog.warningTitle", "Attention"},
            {"excelexportdialog.errorTitle", "Erreur"},
            {"excelexportdialog.targetIsEmpty", 
                "Veuillez sp�cifier un nom de fichier pour le fichier Excel."},
            {"excelexportdialog.targetIsNoFile", "Le chemin sp�cifi� est incorrect."},
            {"excelexportdialog.targetIsNotWritable", 
                "Le fichier s�lectionner est en lecture seule."},
            {"excelexportdialog.targetOverwriteConfirmation",
                "Le fichier ''{0}'' existe. Voulez vous l'�craser?"},
            {"excelexportdialog.targetOverwriteTitle", "Ecraser le fichier?"},

            {"excelexportdialog.cancel", "Annuler"},
            {"excelexportdialog.confirm", "Confirmer"},
            {"excelexportdialog.strict-layout", 
                "Effectuer une disposition stricte pendant l'export."},

            {"htmlexportdialog.dialogtitle", "Exporter le rapport vers un fichier Html ..."},

            {"htmlexportdialog.filename", "Nom de fichier"},
            {"htmlexportdialog.datafilename", "R�pertoire des donn�es"},
            {"htmlexportdialog.copy-external-references", "Copier des r�f�rences externes"},

            {"htmlexportdialog.author", "Auteur"},
            {"htmlexportdialog.title", "Titre"},
            {"htmlexportdialog.encoding", "Encodage"},
            {"htmlexportdialog.selectZipFile", "S�lectionner un fichier"},
            {"htmlexportdialog.selectStreamFile", "S�lectionner un fichier"},
            {"htmlexportdialog.selectDirFile", "S�lectionner un fichier"},

            {"htmlexportdialog.strict-layout", 
                "Effectuer une disposition stricte pendant l'export."},
            {"htmlexportdialog.generate-xhtml", "G�n�ration XHTML 1.0"},
            {"htmlexportdialog.generate-html4", "G�n�ration HTML 4.0"},

            {"htmlexportdialog.warningTitle", "Attention"},
            {"htmlexportdialog.errorTitle", "Erreur"},
            {"htmlexportdialog.targetIsEmpty", "Caract�re de s�parationHtml."},
            {"htmlexportdialog.targetIsNoFile", "Le chemin sp�cifi� est incorrect."},
            {"htmlexportdialog.targetIsNotWritable", 
                "Le fichier s�lectionner est en lecture seule."},
            {"htmlexportdialog.targetOverwriteConfirmation",
                "Le fichier ''{0}'' existe. Voulez vous l'�craser?"},
            {"htmlexportdialog.targetOverwriteTitle", "Ecraser le fichier?"},

            {"htmlexportdialog.cancel", "Annuler"},
            {"htmlexportdialog.confirm", "Confirmer"},
            {"htmlexportdialog.targetPathIsAbsolute",
                "Le chemin de la cible indique un r�pertoire absolu.\n"
                + "Veuillez saisir le r�pertoire de donn�es dans le fichier ZIP."},
            {"htmlexportdialog.targetDataDirIsNoDirectory", 
                "Le chemin sp�cifi� n'est pas un r�pertoire."},
            {"htmlexportdialog.targetCreateDataDirConfirmation",
                "Le chemin sp�cifi� n'existe pas.\n" 
                + "Les sous-r�pertoires absents doivent-ils �tre cr��s?"},
            {"htmlexportdialog.targetCreateDataDirTitle", "Cr�er le r�pertoire?"},

            {"csvexportdialog.dialogtitle", "Exporter le rapport vers un fichier CSV..."},
            {"csvexportdialog.filename", "Nom de fichier"},
            {"csvexportdialog.encoding", "Encodage"},
            {"csvexportdialog.separatorchar", "Caract�re de s�paration"},
            {"csvexportdialog.selectFile", "S�lectionner un fichier"},

            {"csvexportdialog.warningTitle", "Attention"},
            {"csvexportdialog.errorTitle", "Erreur"},
            {"csvexportdialog.targetIsEmpty", 
                "Veuillez sp�cifier un nom de fichier pour le fichier CSV."},
            {"csvexportdialog.targetIsNoFile", "Le chemin sp�cifi� est incorrect."},
            {"csvexportdialog.targetIsNotWritable", 
                "Le fichier s�lectionner est en lecture seule."},
            {"csvexportdialog.targetOverwriteConfirmation",
                "Le fichier ''{0}'' existe. Voulez vous l'�craser?"},
            {"csvexportdialog.targetOverwriteTitle", "Ecraser le fichier?"},

            {"csvexportdialog.cancel", "*Annuler"},
            {"csvexportdialog.confirm", "Confirmer"},

            {"csvexportdialog.separator.tab", "Tabulation"},
            {"csvexportdialog.separator.colon", "Virgule (,)"},
            {"csvexportdialog.separator.semicolon", "Point-virgule (;)"},
            {"csvexportdialog.separator.other", "Autre"},

            {"csvexportdialog.exporttype", "S�lectionner un moteur d'exportation"},
            {"csvexportdialog.export.data", "Exporter par ligne (Raw Data)"},
            {"csvexportdialog.export.printed_elements", "El�ments imprim�s  (Layouted Data)"},
            {"csvexportdialog.strict-layout", 
                "Effectuer une disposition stricte pendant l'export."},


            {"plain-text-exportdialog.dialogtitle", 
                "Exporter le rapport vers un fichier Text (Plain-Text)..."},
            {"plain-text-exportdialog.filename", "Nom de fichier"},
            {"plain-text-exportdialog.encoding", "Encodage"},
            {"plain-text-exportdialog.printer", "Type d'imprimante"},
            {"plain-text-exportdialog.printer.plain", "Plain text"},
            {"plain-text-exportdialog.printer.epson", "Compatible Epson ESC/P"},
            {"plain-text-exportdialog.printer.ibm", "Compatible IBM"},
            {"plain-text-exportdialog.selectFile", "S�lectionner un fichier"},

            {"plain-text-exportdialog.warningTitle", "Attention"},
            {"plain-text-exportdialog.errorTitle", "Erreur"},
            {"plain-text-exportdialog.targetIsEmpty", 
                "Veuillez sp�cifier un nom de fichier pour le fichier CSV."},
            {"plain-text-exportdialog.targetIsNoFile", "Le chemin sp�cifi� est incorrect."},
            {"plain-text-exportdialog.targetIsNotWritable", 
                "Le fichier s�lectionner est en lecture seule."},
            {"plain-text-exportdialog.targetOverwriteConfirmation",
                "Le fichier ''{0}'' existe. Voulez vous l'�craser?"},
            {"plain-text-exportdialog.targetOverwriteTitle", "Ecraser le fichier?"},

            {"plain-text-exportdialog.cancel", "Annuler"},
            {"plain-text-exportdialog.confirm", "Confirmer"},

            {"plain-text-exportdialog.chars-per-inch", "cpi (Characters per inch)"},
            {"plain-text-exportdialog.lines-per-inch", "lpi (Lines per inch)"},
            {"plain-text-exportdialog.font-settings", "Param�tres de la police"},

            {"convertdialog.targetIsEmpty", "Le fichier cible n'est pas sp�cifi�"},
            {"convertdialog.errorTitle", "Erreur"},
            {"convertdialog.targetIsNoFile", "Le chemin sp�cifi� est incorrect." },
            {"convertdialog.targetIsNotWritable", "Le fichier s�lectionner est en lecture seule."},
            {"convertdialog.targetOverwriteConfirmation",
                "Le fichier ''{0}'' existe. Voulez vous l'�craser?"},
            {"convertdialog.targetOverwriteTitle", "Ecraser le fichier?"},
            {"convertdialog.targetFile", "Fichier Cible"},
            {"convertdialog.sourceIsEmpty", "Le fichier source n'est pas sp�cifi�"},
            {"convertdialog.sourceIsNoFile", "Le fichier source cible n'est pas sp�cifi�."},
            {"convertdialog.sourceIsNotReadable", "Le fichier source n'est pas lisible."},
            {"convertdialog.sourceFile", "Fichier source"},

            {"convertdialog.action.selectTarget.name", "S�lectionner"},
            {"convertdialog.action.selectTarget.description", "S�lectionner un fichier cible."},
            {"convertdialog.action.selectSource.name", "S�lectionner"},
            {"convertdialog.action.selectSource.description", "S�lectionner un fichier source."},
            {"convertdialog.action.convert.name", "Convertir"},
            {"convertdialog.action.convert.description", "Convertir les fichiers source."},

            {"convertdialog.title", "Convertisseur de rapport"},

          };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main (String [] args)
  {
    ResourceCompareTool.main(new String[]{"fr"});
  }

}
