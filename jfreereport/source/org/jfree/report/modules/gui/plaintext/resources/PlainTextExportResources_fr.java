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
 * ------------------------------
 * PDFExportResources.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  PR;
 * Contributor(s):   -;
 *
 * $Id: PlainTextExportResources_fr.java,v 1.8 2003/09/14 19:33:25 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.plaintext.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * French language resource for the plain text export GUI.
 *
 * @author PR
 */
public class PlainTextExportResources_fr extends JFreeReportResources
{
  /**
   * Default Constructor.
   */
  public PlainTextExportResources_fr()
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
        {"action.export-to-plaintext.name", "Enregistrer en text..."},
        {"action.export-to-plaintext.description", "Enregistrer au format PlainText"},
        {"action.export-to-plaintext.mnemonic", new Integer(KeyEvent.VK_T)},

        {"error.processingfailed.title", "Echec du traitement du Report"},
        {"error.processingfailed.message", "Erreur lors du traitement de ce rapport: {0}"},
        {"error.savefailed.message", "Erreur durant l'enregistrement en PDF : {0}"},
        {"error.savefailed.title", "Erreur durant l'enregistrement"},

        {"plain-text-exportdialog.dialogtitle",
         "Exporter le rapport vers un fichier Text (Plain-Text)..."},
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
        {"plain-text-exportdialog.targetIsNoFile",
         "Le fichier cible spécifié n'est pas un fichier ordinaire."},
        {"plain-text-exportdialog.targetIsNotWritable",
         "Le fichier sélectionné est en lecture seule."},
        {"plain-text-exportdialog.targetOverwriteConfirmation",
         "Le fichier ''{0}'' existe. Voulez vous l'écraser?"},
        {"plain-text-exportdialog.targetOverwriteTitle", "Ecraser le fichier?"},

        {"plain-text-exportdialog.cancel", "Annuler"},
        {"plain-text-exportdialog.confirm", "Confirmer"},

        {"plain-text-exportdialog.chars-per-inch", "cpi (Caractères par inch)"},
        {"plain-text-exportdialog.lines-per-inch", "lpi (Lignes par inch)"},
        {"plain-text-exportdialog.font-settings", "Paramètres de la police"},
        
        {"plaintext-export.progressdialog.title", "Exportation vers un fichier text ..."},
        {"plaintext-export.progressdialog.message", 
          "Le rapport est maintenant exporté en fichier text ..."},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{PlainTextExportResources.class.getName(), "fr"});
  }
}
