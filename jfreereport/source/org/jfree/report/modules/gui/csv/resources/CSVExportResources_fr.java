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
 * CSVExportResources_es.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CSVExportResources_fr.java,v 1.1 2003/07/07 22:44:05 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 05.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.csv.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

public class CSVExportResources_fr
{
  public CSVExportResources_fr()
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
        {"action.export-to-csv.name", "Exporter en CSV..."},
        {"action.export-to-csv.description", "Enregistrer au format CSV"},
        {"action.export-to-csv.mnemonic", new Integer(KeyEvent.VK_C)},

        {"csvexportdialog.dialogtitle", "Exporter le rapport vers un fichier CSV..."},
        {"csvexportdialog.filename", "Nom de fichier"},
        {"csvexportdialog.encoding", "Encodage"},
        {"csvexportdialog.separatorchar", "Caractère de séparation"},
        {"csvexportdialog.selectFile", "Sélectionner un fichier"},

        {"csvexportdialog.warningTitle", "Attention"},
        {"csvexportdialog.errorTitle", "Erreur"},
        {"csvexportdialog.targetIsEmpty",
         "Veuillez spécifier un nom de fichier pour le fichier CSV."},
        {"csvexportdialog.targetIsNoFile", "Le fichier cible spécifié n'est pas un fichier ordinaire."},
        {"csvexportdialog.targetIsNotWritable",
         "Le fichier sélectionner est en lecture seule."},
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
        {"csvexportdialog.strict-layout",
         "Effectuer une disposition stricte pendant l'export."},
         
        {"error.processingfailed.title", "Echec du traitement du Report"},
        {"error.processingfailed.message", "Erreur lors du traitement de ce rapport: {0}"},
        {"error.savefailed.message", "Erreur durant l'enregistrement en PDF : {0}"},
        {"error.savefailed.title", "Erreur durant l'enregistrement"},
        
        {"csvexportdialog.csv-file-description", "Fichiers séparés par une virgule."},

      };


  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{CSVExportResources.class.getName(), "fr"});
  }

}
