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
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CSVExportResources_de.java,v 1.3 2003/08/24 15:08:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.csv.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * German language resource for the CSV export GUI.
 *
 * @author Thomas Morgner
 */
public class CSVExportResources_de extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public CSVExportResources_de()
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
        {"action.export-to-csv.name", "Export nach CSV..."},
        {"action.export-to-csv.description", "Speichert den Bericht im CSV format"},
        {"action.export-to-csv.mnemonic", new Integer(KeyEvent.VK_C)},

        {"csvexportdialog.dialogtitle", "Bericht als CSV-Datei speichern ..."},
        {"csvexportdialog.filename", "Dateiname"},
        {"csvexportdialog.encoding", "Zeichensatz"},
        {"csvexportdialog.separatorchar", "Trennzeichen"},
        {"csvexportdialog.selectFile", "Ausw\u00e4hlen"},

        {"csvexportdialog.warningTitle", "Warnung"},
        {"csvexportdialog.errorTitle", "Fehler"},
        {"csvexportdialog.targetIsEmpty",
         "Bitte geben Sie einen Dateinamen f\u00fcr die CSV-Datei an."},
        {"csvexportdialog.targetIsNoFile", "Der Dateiname zeigt auf keine gew\u00f6hnliche Datei."},
        {"csvexportdialog.targetIsNotWritable",
         "Sie besitzen keine ausreichenden Rechte um die ausgew\u00e4hlte Datei zu "
      + "\u00fcberschreiben."},

        {"csvexportdialog.targetOverwriteConfirmation",
         "Die Datei ''{0}'' existiert bereits. Soll diese Datei \u00fcberschrieben werden?"},
        {"csvexportdialog.targetOverwriteTitle", "Datei \u00fcberschreiben?"},

        {"csvexportdialog.cancel", "Abbrechen"},
        {"csvexportdialog.confirm", "OK"},
        {"csvexportdialog.strict-layout", "Genaue Layout-Umwandlung benutzen."},

        {"csvexportdialog.separator.tab", "Tabulator"},
        {"csvexportdialog.separator.colon", "Komma (,)"},
        {"csvexportdialog.separator.semicolon", "Semikolon (;)"},
        {"csvexportdialog.separator.other", "Anderes"},

        {"csvexportdialog.exporttype", "Bitte Export-Methode ausw\u00e4hlen"},
        {"csvexportdialog.export.data", "Exportiere die Datenquelle (Rohdaten)"},
        {"csvexportdialog.export.printed_elements",
         "Exportiere die gedruckten Daten (Bearbeitete Daten)"},

        {"error.processingfailed.title", "Fehler beim bearbeiten des Berichtes"},
        {"error.processingfailed.message", "Die Berichtsgenerierung ist fehlgeschlagen: {0}"},
        {"error.savefailed.message", "Der Bericht konnte nicht gespeichert werden: {0}"},
        {"error.savefailed.title", "Speichern fehlgeschlagen"},

        {"csvexportdialog.csv-file-description", "Text-Dateien mit komma getrennten Werten."},

        {"cvs-export.progressdialog.title", "Exportiere in eine CSV-Datei ..."},
        {"cvs-export.progressdialog.message", "Der Bericht wird nun in eine CSV Datei exportiert ..."},
      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{CSVExportResources.class.getName(), "de"});
  }

}
