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
 * -------------------------
 * JFreeReportResources.java
 * -------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: JFreeReportResources_fr.java,v 1.3 2003/03/23 17:45:57 mimil Exp $
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
            {"action.save-as.mnemonic", new Integer (KeyEvent.VK_A)},
            {"action.save-as.small-icon", getIcon ("com/jrefinery/report/resources/SaveAs16.gif")},
            {"action.save-as.icon", getIcon ("com/jrefinery/report/resources/SaveAs24.gif")},

            {"action.export-to-excel.name", "Exporter en Excel..."},
            {"action.export-to-excel.description", "Enregistrer au format MS-Excel"},
            {"action.export-to-excel.mnemonic", new Integer (KeyEvent.VK_E)},
            // temporarily using the same icon as "Save to PDF", till we have a better one
            {"action.export-to-excel.small-icon", 
                getIcon ("com/jrefinery/report/resources/SaveAs16.gif")},
            {"action.export-to-excel.icon", 
                getIcon ("com/jrefinery/report/resources/SaveAs24.gif")},
                
            {"action.export-to-html.name", "Exporter en html..."},
            {"action.export-to-html.description", "Enregistrer au format HTML"},
            {"action.export-to-html.mnemonic", new Integer (KeyEvent.VK_H)},
            // temporarily using the same icon as "Save to PDF", till we have a better one
            {"action.export-to-html.small-icon", 
                getIcon ("com/jrefinery/report/resources/SaveAs16.gif")},
            {"action.export-to-html.icon", 
                getIcon ("com/jrefinery/report/resources/SaveAs24.gif")},
            
            {"action.export-to-csv.name", "Exporter en CSV..."},
            {"action.export-to-csv.description", "Enregistrer au format CSV"},
            {"action.export-to-csv.mnemonic", new Integer (KeyEvent.VK_C)},
            // temporarily using the same icon as "Save to PDF", till we have a better one
            {"action.export-to-csv.small-icon", 
                getIcon ("com/jrefinery/report/resources/SaveAs16.gif")},
            {"action.export-to-csv.icon", 
                getIcon ("com/jrefinery/report/resources/SaveAs24.gif")},
                
            {"action.export-to-plaintext.name", "Enregistrer en text..."},
            {"action.export-to-plaintext.description", "Enregistrer au format PlainText"},
            {"action.export-to-plaintext.mnemonic", new Integer (KeyEvent.VK_T)},
            // temporarily using the same icon as "Save to PDF", till we have a better one
            {"action.export-to-plaintext.small-icon", 
                getIcon ("com/jrefinery/report/resources/SaveAs16.gif")},
            {"action.export-to-plaintext.icon", 
                getIcon ("com/jrefinery/report/resources/SaveAs24.gif")},    
            
            {"action.page-setup.name", "Mise en page"},
            {"action.page-setup.description", "Mise en page"},
            {"action.page-setup.small-icon",
                getIcon ("com/jrefinery/report/resources/PageSetup16.gif")},
            {"action.page-setup.icon", getIcon ("com/jrefinery/report/resources/PageSetup24.gif")},

            {"action.print.name", "Impression..."},
            {"action.print.description", "Impression du rapport"},
            {"action.print.mnemonic", new Integer (KeyEvent.VK_P)},
            {"action.print.small-icon", getIcon ("com/jrefinery/report/resources/Print16.gif")},
            {"action.print.icon", getIcon ("com/jrefinery/report/resources/Print24.gif")},

            {"action.close.name", "Fermer"},
            {"action.close.description", "Fermer l'aperçu avant impression"},
            {"action.close.mnemonic", new Integer (KeyEvent.VK_C)},

            {"action.gotopage.name", "Aller à la page ..."},
            {"action.gotopage.description", "Voir une page directement"},
            {"action.gotopage.mnemonic", new Integer (KeyEvent.VK_G)},

            {"dialog.gotopage.message", "Entrer un numéro de page"},
            {"dialog.gotopage.title", "Aller à la page"},

            {"action.about.name", "A propos..."},
            {"action.about.description", "Information à propos de l'application"},
            {"action.about.mnemonic", new Integer (KeyEvent.VK_A)},
            {"action.about.small-icon", getIcon ("com/jrefinery/report/resources/About16.gif")},
            {"action.about.icon", getIcon ("com/jrefinery/report/resources/About24.gif")},

            {"action.firstpage.name", "Début"},
            {"action.firstpage.mnemonic", new Integer (KeyEvent.VK_HOME)},
            {"action.firstpage.description", "Aller à la première page"},
            {"action.firstpage.small-icon",
                getIcon ("com/jrefinery/report/resources/FirstPage16.gif")},
            {"action.firstpage.icon", getIcon ("com/jrefinery/report/resources/FirstPage24.gif")},

            {"action.back.name", "Précédent"},
            {"action.back.description", "Aller à la page précédente"},
            {"action.back.mnemonic", new Integer (KeyEvent.VK_PAGE_UP)},
            {"action.back.small-icon", getIcon ("com/jrefinery/report/resources/Back16.gif")},
            {"action.back.icon", getIcon ("com/jrefinery/report/resources/Back24.gif")},

            {"action.forward.name", "Suivant"},
            {"action.forward.description", "Aller à la page suivante"},
            {"action.forward.mnemonic", new Integer (KeyEvent.VK_PAGE_DOWN)},
            {"action.forward.small-icon",
                getIcon ("com/jrefinery/report/resources/Forward16.gif")},
            {"action.forward.icon", getIcon ("com/jrefinery/report/resources/Forward24.gif")},

            {"action.lastpage.name", "Fin"},
            {"action.lastpage.description", "Aller à la dernière page"},
            {"action.lastpage.mnemonic", new Integer (KeyEvent.VK_END)},
            {"action.lastpage.small-icon",
                getIcon ("com/jrefinery/report/resources/LastPage16.gif")},
            {"action.lastpage.icon", getIcon ("com/jrefinery/report/resources/LastPage24.gif")},

            {"action.zoomIn.name", "Agrandir"},
            {"action.zoomIn.description", "Agrandir"},
            {"action.zoomIn.mnemonic", new Integer (KeyEvent.VK_PLUS)},
            {"action.zoomIn.small-icon", getIcon ("com/jrefinery/report/resources/ZoomIn16.gif")},
            {"action.zoomIn.icon", getIcon ("com/jrefinery/report/resources/ZoomIn24.gif")},

            {"action.zoomOut.name", "Rétrécir"},
            {"action.zoomOut.description", "Rétrécir"},
            {"action.zoomOut.mnemonic", new Integer (KeyEvent.VK_MINUS)},
            {"action.zoomOut.small-icon",
                getIcon ("com/jrefinery/report/resources/ZoomOut16.gif")},
            {"action.zoomOut.icon", getIcon ("com/jrefinery/report/resources/ZoomOut24.gif")},

            // preview frame...
            {"preview-frame.title", "Aperçu avant impression"},

            // menu labels...
            {"menu.file.name", "Fichier"},
            {"menu.file.mnemonic", new Character ('F')},

            {"menu.navigation.name", "Navigation"},
            {"menu.navigation.mnemonic", new Character ('N')},

            {"menu.zoom.name", "Zoom"},
            {"menu.zoom.mnemonic", new Character ('Z')},

            {"menu.help.name", "Aide"},
            {"menu.help.mnemonic", new Character ('H')},

            {"file.save.pdfdescription", "Documents PDF"},
            {"statusline.pages", "Page {0} de {1}"},
            {"statusline.error", "Reportgeneration à produit une erreur: {0}"},
            {"statusline.repaginate", "Calcule de la coupure des pages, veuillez patienter."},
            {"error.processingfailed.title", "Echec du traitement du Report"},
            {"error.processingfailed.message", "Erreur lors du traitement de ce rapport: {0}"},
            {"error.savefailed.message", "Erreur durant l'enregistrement en PDF : {0}"},
            {"error.savefailed.title", "Erreur durant la sauvegarde"},
            {"error.printfailed.message", "Erreur à l'impression du rapport: {0}"},
            {"error.printfailed.title", "Erreur à l'impression"},
            {"error.validationfailed.message", "Erreur pendant la validation des entrées utilisateur."},
            {"error.validationfailed.title", "Erreur de validation"},
            
            {"tabletarget.page", "Page {0}"},

            {"pdfsavedialog.dialogtitle", "Enregistrement du rapport en PDF ..."},
            {"pdfsavedialog.filename", "Nom du fichier"},
            {"pdfsavedialog.author", "Auteur"},
            {"pdfsavedialog.title", "Titre"},
            {"pdfsavedialog.selectFile", "Sélectionner un fichier"},
            {"pdfsavedialog.security", "Paramètres de sécurité et de chiffrage"},
            {"pdfsavedialog.encoding", "Encodage"},

            {"pdfsavedialog.securityNone", "Pas de sécurité"},
            {"pdfsavedialog.security40bit", "Chiffrage en 40 bits"},
            {"pdfsavedialog.security128bit", "Chiffrage en 128 bits"},
            {"pdfsavedialog.userpassword", "Mot de passe utilisateur"},
            {"pdfsavedialog.userpasswordconfirm", "Confirmer"},
            {"pdfsavedialog.userpasswordNoMatch", "Le mot de passe ne correspond pas."},
            {"pdfsavedialog.ownerpassword", "Mot de passe du propriétaire"},
            {"pdfsavedialog.ownerpasswordconfirm", "Confirmer"},
            {"pdfsavedialog.ownerpasswordNoMatch", "Le mot de passe du propriétaire est incorrect."},
            {"pdfsavedialog.ownerpasswordEmpty", "Le mot de passe du propriétaire est vide. Les utilisateurs "
             + "pourront modifier la sécurité. Continuer ?" },

            {"pdfsavedialog.warningTitle", "Attention"},
            {"pdfsavedialog.errorTitle", "Erreur"},
            {"pdfsavedialog.targetIsEmpty", "Veuillez spécifier un chemin pour le PDF."},
            {"pdfsavedialog.targetIsNoFile", "Le chemin spécifié est incorrect."},
            {"pdfsavedialog.targetIsNotWritable", "Le fichier sélectionner est en lecture seule."},
            {"pdfsavedialog.targetOverwriteConfirmation",
                "Le fichier ''{0}'' existe. Voulez vous l'écraser?"},
            {"pdfsavedialog.targetOverwriteTitle", "Ecraser le fichier?"},


            {"pdfsavedialog.allowCopy", "Autoriser la copie"},
            {"pdfsavedialog.allowPrinting", "Autoriser l'impression"},
            {"pdfsavedialog.allowDegradedPrinting", "Autoriser les impressions de dégradés"},
            {"pdfsavedialog.allowScreenreader", "Autoriser la visualisation"},
            {"pdfsavedialog.allowAssembly", "Autoriser le (Ré-)assemblage"},
            {"pdfsavedialog.allowModifyContents", "Autoriser les modifications du contenu"},
            {"pdfsavedialog.allowModifyAnnotations", "Autoriser les modifications des annotations"},
            {"pdfsavedialog.allowFillIn", "Autoriser le remplissage des formules"},

            {"pdfsavedialog.option.noprinting", "Aucune impression"},
            {"pdfsavedialog.option.degradedprinting", "Qualité basse d'impression"},
            {"pdfsavedialog.option.fullprinting", "Impression autorisée"},

            {"pdfsavedialog.cancel", "Annuler"},
            {"pdfsavedialog.confirm", "Confirmer"},
            
            {"excelexportdialog.dialogtitle", "Exporter le rapport vers un fichier Excel..."},
            {"excelexportdialog.filename", "Nom de fichier"},
            {"excelexportdialog.author", "Auteur"},
            {"excelexportdialog.title", "Titre"},
            {"excelexportdialog.selectFile", "Sélectionner un fichier"},

            {"excelexportdialog.warningTitle", "Attention"},
            {"excelexportdialog.errorTitle", "Erreur"},
            {"excelexportdialog.targetIsEmpty", "Veuillez spécifier un nom de fichier pour le fichier Excel."},
            {"excelexportdialog.targetIsNoFile", "Le chemin spécifié est incorrect."},
            {"excelexportdialog.targetIsNotWritable", "Le fichier sélectionner est en lecture seule."},
            {"excelexportdialog.targetOverwriteConfirmation",
                "Le fichier ''{0}'' existe. Voulez vous l'écraser?"},
            {"excelexportdialog.targetOverwriteTitle", "Ecraser le fichier?"},

            {"excelexportdialog.cancel", "Annuler"},
            {"excelexportdialog.confirm", "Confirmer"},
            {"excelexportdialog.strict-layout", "Effectuer une disposition stricte pendant l'export."},

            {"htmlexportdialog.dialogtitle", "Exporter le rapport vers un fichier Html ..."},

            {"htmlexportdialog.filename", "Nom de fichier"},
            {"htmlexportdialog.datafilename", "Répertoire des données"},
            {"htmlexportdialog.copy-external-references", "Copier des références externes"},

            {"htmlexportdialog.author", "Auteur"},
            {"htmlexportdialog.title", "Titre"},
            {"htmlexportdialog.encoding", "Encodage"},
            {"htmlexportdialog.selectZipFile", "Sélectionner un fichier"},
            {"htmlexportdialog.selectStreamFile", "Sélectionner un fichier"},
            {"htmlexportdialog.selectDirFile", "Sélectionner un fichier"},

            {"htmlexportdialog.strict-layout", "Effectuer une disposition stricte pendant l'export."},
            {"htmlexportdialog.generate-xhtml", "Génération XHTML 1.0"},
            {"htmlexportdialog.generate-html4", "Génération HTML 4.0"},

            {"htmlexportdialog.warningTitle", "Attention"},
            {"htmlexportdialog.errorTitle", "Erreur"},
            {"htmlexportdialog.targetIsEmpty", "Caractére de séparationHtml."},
            {"htmlexportdialog.targetIsNoFile", "Le chemin spécifié est incorrect."},
            {"htmlexportdialog.targetIsNotWritable", "Le fichier sélectionner est en lecture seule."},
            {"htmlexportdialog.targetOverwriteConfirmation",
                "Le fichier ''{0}'' existe. Voulez vous l'écraser?"},
            {"htmlexportdialog.targetOverwriteTitle", "Ecraser le fichier?"},

            {"htmlexportdialog.cancel", "Annuler"},
            {"htmlexportdialog.confirm", "Confirmer"},
            {"htmlexportdialog.targetPathIsAbsolute",
                "Le chemin de la cible indique un répertoire absolu.\n"
                + "Veuillez saisir le répertoire de données dans le fichier ZIP."},
            {"htmlexportdialog.targetDataDirIsNoDirectory", 
                "Le chemin spécifié n'est pas un répertoire."},
            {"htmlexportdialog.targetCreateDataDirConfirmation",
                "Le chemin spécifié n'existe pas.\n" 
                + "Les sous-répertoires absents doivent-ils être créés?"},
            {"htmlexportdialog.targetCreateDataDirTitle", "Créer le répertoire?"},

            {"csvexportdialog.dialogtitle", "Exporter le rapport vers un fichier CSV..."},
            {"csvexportdialog.filename", "Nom de fichier"},
            {"csvexportdialog.encoding", "Encodage"},
            {"csvexportdialog.separatorchar", "Caractère de séparation"},
            {"csvexportdialog.selectFile", "Sélectionner un fichier"},

            {"csvexportdialog.warningTitle", "Attention"},
            {"csvexportdialog.errorTitle", "Erreur"},
            {"csvexportdialog.targetIsEmpty", "Veuillez spécifier un nom de fichier pour le fichier CSV."},
            {"csvexportdialog.targetIsNoFile", "Le chemin spécifié est incorrect."},
            {"csvexportdialog.targetIsNotWritable", "Le fichier sélectionner est en lecture seule."},
            {"csvexportdialog.targetOverwriteConfirmation",
                "Le fichier ''{0}'' existe. Voulez vous l'écraser?"},
            {"csvexportdialog.targetOverwriteTitle", "Ecraser le fichier?"},

            {"csvexportdialog.cancel", "*Annuler"},
            {"csvexportdialog.confirm", "Confirmer"},

            {"csvexportdialog.separator.tab", "Tabulation"},
            {"csvexportdialog.separator.colon", "Virgule (,)"},
            {"csvexportdialog.separator.semicolon", "Point-virgule (;)"},
            {"csvexportdialog.separator.other", "Autre"},

            {"csvexportdialog.exporttype", "Sélectionner un moteur d'exportation"},
            {"csvexportdialog.export.data", "Exporter par ligne (Raw Data)"},
            {"csvexportdialog.export.printed_elements", "Eléments imprimés  (Layouted Data)"},
            {"csvexportdialog.strict-layout", "Effectuer une disposition stricte pendant l'export."},


            {"plain-text-exportdialog.dialogtitle", "Exporter le rapport vers un fichier Text (Plain-Text)..."},
            {"plain-text-exportdialog.filename", "Nom de fichier"},
            {"plain-text-exportdialog.encoding", "Encodage"},
            {"plain-text-exportdialog.printer", "Type d'imprimante"},
            {"plain-text-exportdialog.printer.plain", "Plain text"},
            {"plain-text-exportdialog.printer.epson", "Compatible Epson ESC/P"},
            {"plain-text-exportdialog.printer.ibm", "Compatible IBM"},
            {"plain-text-exportdialog.selectFile", "Sélectionner un fichier"},

            {"plain-text-exportdialog.warningTitle", "Attention"},
            {"plain-text-exportdialog.errorTitle", "Erreur"},
            {"plain-text-exportdialog.targetIsEmpty", 
                "Veuillez spécifier un nom de fichier pour le fichier CSV."},
            {"plain-text-exportdialog.targetIsNoFile", "Le chemin spécifié est incorrect."},
            {"plain-text-exportdialog.targetIsNotWritable", "Le fichier sélectionner est en lecture seule."},
            {"plain-text-exportdialog.targetOverwriteConfirmation",
                "Le fichier ''{0}'' existe. Voulez vous l'écraser?"},
            {"plain-text-exportdialog.targetOverwriteTitle", "Ecraser le fichier?"},

            {"plain-text-exportdialog.cancel", "Annuler"},
            {"plain-text-exportdialog.confirm", "Confirmer"},

            {"plain-text-exportdialog.chars-per-inch", "cpi (Characters per inch)"},
            {"plain-text-exportdialog.lines-per-inch", "lpi (Lines per inch)"},
            {"plain-text-exportdialog.font-settings", "Paramètres de la police"},

            {"convertdialog.targetIsEmpty", "Le fichier cible n'est pas spécifié"},
            {"convertdialog.errorTitle", "Erreur"},
            {"convertdialog.targetIsNoFile", "Le chemin spécifié est incorrect." },
            {"convertdialog.targetIsNotWritable", "Le fichier sélectionner est en lecture seule."},
            {"convertdialog.targetOverwriteConfirmation",
                "Le fichier ''{0}'' existe. Voulez vous l'écraser?"},
            {"convertdialog.targetOverwriteTitle", "Ecraser le fichier?"},
            {"convertdialog.targetFile", "Fichier Cible"},
            {"convertdialog.sourceIsEmpty", "Le fichier source n'est pas spécifié"},
            {"convertdialog.sourceIsNoFile", "Le fichier source cible n'est pas spécifié."},
            {"convertdialog.sourceIsNotReadable", "Le fichier source n'est pas lisible."},
            {"convertdialog.sourceFile", "Fichier source"},

            {"convertdialog.action.selectTarget.name", "Sélectionner"},
            {"convertdialog.action.selectTarget.description", "Sélectionner un fichier cible."},
            {"convertdialog.action.selectSource.name", "Sélectionner"},
            {"convertdialog.action.selectSource.description", "Sélectionner un fichier source."},
            {"convertdialog.action.convert.name", "Convertir"},
            {"convertdialog.action.convert.description", "Convertir les fichiers source."},

            {"convertdialog.title", "Convertisseur de rapport"},

          };

}
