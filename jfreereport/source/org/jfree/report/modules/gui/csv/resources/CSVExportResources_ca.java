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
 * CSVExportResources.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Marc Casas
 * Contributor(s):   -
 *
 * $Id: CSVExportResources_ca.java,v 1.1 2003/09/08 18:41:45 taqua Exp $
 *
 * Changes
 * -------------------------
 * 03-09-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.csv.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Catalan language resource for the CSV export GUI.
 *
 * @author Marc Casas
 */
public class CSVExportResources_ca extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public CSVExportResources_ca()
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
        {"action.export-to-csv.name", "Exportar a CSV..."},
        {"action.export-to-csv.description", "Desar en format CSV"},
        {"action.export-to-csv.mnemonic", new Integer(KeyEvent.VK_C)},
        {"action.export-to-csv.accelerator", createMenuKeystroke(KeyEvent.VK_C)},

        {"csvexportdialog.dialogtitle", "Exportar l'informe a un fitxer CSV..."},
        {"csvexportdialog.filename", "Nom de fitxer"},
        {"csvexportdialog.encoding", "Codificació"},
        {"csvexportdialog.separatorchar", "Caràcter separador"},
        {"csvexportdialog.selectFile", "Sel·lecciona el fitxer"},

        {"csvexportdialog.warningTitle", "Atenció"},
        {"csvexportdialog.errorTitle", "Error"},
        {"csvexportdialog.targetIsEmpty", "Especifica el nom per al fitxer CSV."},
        {"csvexportdialog.targetIsNoFile", "El fitxer especificat no és un fitxer normal."},
        {"csvexportdialog.targetIsNotWritable", "No es pot escriure al fitxer especificat."},
        {"csvexportdialog.targetOverwriteConfirmation",
         "El fitxer ''{0}'' existeix. El vols sobreescriure?"},
        {"csvexportdialog.targetOverwriteTitle", "Sobreescriure el fitxer?"},

        {"csvexportdialog.cancel", "Cancel·lar"},
        {"csvexportdialog.confirm", "Confirmar"},

        {"csvexportdialog.separator.tab", "Tabulador"},
        {"csvexportdialog.separator.colon", "Coma (,)"},
        {"csvexportdialog.separator.semicolon", "Punt i coma (;)"},
        {"csvexportdialog.separator.other", "Altre"},

        {"csvexportdialog.exporttype", "Elegeix un motor d'exportació"},
        {"csvexportdialog.export.data", "Exportar la fila de dades (Raw Data)"},
        {"csvexportdialog.export.printed_elements", "Elements impresos (Layouted Data)"},
        {"csvexportdialog.strict-layout", "Fer una estricta presentació al exportar."},

        {"error.processingfailed.title", "Error al processament de l'informe"},
        {"error.processingfailed.message", "Error processant aquest informe: {0}"},
        {"error.savefailed.message", "Error guardant el fitxer CSV: {0}"},
        {"error.savefailed.title", "Error al desar"},

        {"csvexportdialog.csv-file-description", "Fitxers 'valors separats per coma'."},

        {"cvs-export.progressdialog.title", "Exportant a fitxer CSV..."},
        {"cvs-export.progressdialog.message", "L'informe és exportat a un fitxer CSV..."},
      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    new CSVExportResources_ca().generateResourceProperties("catalan");
    System.exit(0);
  }

}
