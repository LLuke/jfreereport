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
 * HtmlExportResources_fr.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  PR;
 * Contributor(s):   -;
 *
 * $Id: HtmlExportResources_fr.java,v 1.3 2003/07/14 20:16:05 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 05-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.html.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * French language resource for the HTML export GUI.
 * 
 * @author PR
 */
public class HtmlExportResources_fr extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public HtmlExportResources_fr()
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
        {"action.export-to-html.name", "Exporter en html..."},
        {"action.export-to-html.description", "Enregistrer au format HTML"},
        {"action.export-to-html.mnemonic", new Integer(KeyEvent.VK_H)},

        {"htmlexportdialog.dialogtitle", "Exporter le rapport vers un fichier Html ..."},

        {"htmlexportdialog.filename", "Nom de fichier"},
        {"htmlexportdialog.datafilename", "Répertoire de données"},
        {"htmlexportdialog.copy-external-references", "Copier des références externes"},

        {"htmlexportdialog.author", "Auteur"},
        {"htmlexportdialog.title", "Titre"},
        {"htmlexportdialog.encoding", "Encodage"},
        {"htmlexportdialog.selectZipFile", "Sélectionner un fichier"},
        {"htmlexportdialog.selectStreamFile", "Sélectionner un fichier"},
        {"htmlexportdialog.selectDirFile", "Sélectionner un fichier"},

        {"htmlexportdialog.strict-layout",
         "Effectuer une disposition stricte pendant l'export."},
        {"htmlexportdialog.generate-xhtml", "Génération XHTML 1.0"},
        {"htmlexportdialog.generate-html4", "Génération HTML 4.0"},

        {"htmlexportdialog.warningTitle", "Attention"},
        {"htmlexportdialog.errorTitle", "Erreur"},
        {"htmlexportdialog.targetIsEmpty", "Le fichier source n'est pas spécifié."},
        {"htmlexportdialog.targetIsNoFile", "Le chemin spécifié est incorrect."},
        {"htmlexportdialog.targetIsNotWritable",
         "Le fichier sélectionné est en lecture seule."},
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

        {"htmlexportdialog.html-documents", "Documents HTML"},
        {"htmlexportdialog.zip-archives", "Archives ZIP"},

        {"htmlexportdialog.stream-export", "Exportation File stream"},
        {"htmlexportdialog.directory-export", "Exportation répertoire"},
        {"htmlexportdialog.zip-export", "Exportation fichier ZIP"},
        
        {"error.processingfailed.title", "Echec du traitement du Report"},
        {"error.processingfailed.message", "Erreur lors du traitement de ce rapport: {0}"},
        {"error.savefailed.message", "Erreur durant l'enregistrement en PDF : {0}"},
        {"error.savefailed.title", "Erreur durant la sauvegarde"},
        {"error.validationfailed.message",
         "Erreur pendant la validation des entrées utilisateur."},
        {"error.validationfailed.title", "Erreur de validation"},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{HtmlExportResources.class.getName(), "fr"});
  }
}
