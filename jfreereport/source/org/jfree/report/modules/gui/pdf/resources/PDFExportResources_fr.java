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
 * PDFExportResources.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PDFExportResources_fr.java,v 1.2 2003/07/11 09:20:55 mimil Exp $
 *
 * Changes 
 * -------------------------
 * 05.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.pdf.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

public class PDFExportResources_fr extends JFreeReportResources
{
  /**
   * Default Constructor.
   *
   */
  public PDFExportResources_fr()
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
        {"action.save-as.name", "Enregistrer en PDF..."},
        {"action.save-as.description", "Enregistrer au format PDF"},
        {"action.save-as.mnemonic", new Integer(KeyEvent.VK_P)},

        {"file.save.pdfdescription", "Documents PDF"},

        {"error.processingfailed.title", "Echec du traitement du Report"},
        {"error.processingfailed.message", "Erreur lors du traitement de ce rapport: {0}"},
        {"error.savefailed.message", "Erreur durant l'enregistrement en PDF : {0}"},
        {"error.savefailed.title", "Erreur durant la sauvegarde"},
        
        {"file.save.pdfdescription", "Documents PDF"},

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
        {"pdfsavedialog.ownerpasswordNoMatch",
         "Le mot de passe du propriétaire est incorrect."},
        {"pdfsavedialog.ownerpasswordEmpty",
         "Le mot de passe du propriétaire est vide. Les utilisateurs "
      + "pourront modifier la sécurité. Continuer ?"},

        {"pdfsavedialog.warningTitle", "Attention"},
        {"pdfsavedialog.errorTitle", "Erreur"},
        {"pdfsavedialog.targetIsEmpty", "Veuillez spécifier un chemin pour le PDF."},
        {"pdfsavedialog.targetIsNoFile", 
           "Le fichier cible spécifié n'est pas un fichier ordinaire."},
        {"pdfsavedialog.targetIsNotWritable", "Le fichier sélectionné est en lecture seule."},
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

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{PDFExportResources.class.getName(), "fr"});
  }
}
