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
 * XLSExportResources_fr.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  PR;
 * Contributor(s):   -;
 *
 * $Id: XLSExportResources_fr.java,v 1.4 2003/08/18 18:28:00 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 05.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.xls.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * French language resource for the Excel export GUI.
 * 
 * @author PR
 */
public class XLSExportResources_fr extends JFreeReportResources
{
  /**
   * Default Constructor.
   *
   */
  public XLSExportResources_fr()
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
        {"action.export-to-excel.name", "Exporter en Excel..."},
        {"action.export-to-excel.description", "Enregistrer au format MS-Excel"},
        {"action.export-to-excel.mnemonic", new Integer(KeyEvent.VK_E)},
        
        {"error.processingfailed.title", "Echec du traitement du Report"},
        {"error.processingfailed.message", "Erreur lors du traitement de ce rapport: {0}"},
        {"error.savefailed.message", "Erreur durant l'enregistrement en MS-Excel : {0}"},
        {"error.savefailed.title", "Erreur durant l'enregistrement"},

        {"excelexportdialog.dialogtitle", "Exporter le rapport vers un fichier Excel..."},
        {"excelexportdialog.filename", "Nom de fichier"},
        {"excelexportdialog.author", "Auteur"},
        {"excelexportdialog.title", "Titre"},
        {"excelexportdialog.selectFile", "Sélectionner un fichier"},

        {"excelexportdialog.warningTitle", "Attention"},
        {"excelexportdialog.errorTitle", "Erreur"},
        {"excelexportdialog.targetIsEmpty",
         "Veuillez spécifier un nom de fichier pour le fichier Excel."},
        {"excelexportdialog.targetIsNoFile", 
         "Le fichier cible spécifié n'est pas un fichier ordinaire."},
        {"excelexportdialog.targetIsNotWritable",
         "Le fichier sélectionner est en lecture seule."},
        {"excelexportdialog.targetOverwriteConfirmation",
         "Le fichier ''{0}'' existe. Voulez vous l'écraser?"},
        {"excelexportdialog.targetOverwriteTitle", "Ecraser le fichier?"},

        {"excelexportdialog.cancel", "Annuler"},
        {"excelexportdialog.confirm", "Confirmer"},
        {"excelexportdialog.strict-layout",
         "Effectuer une disposition stricte pendant l'export."},
        
      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{XLSExportResources.class.getName(), "fr"});
  }
}
