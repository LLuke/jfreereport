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
 * XLSExportResources_de.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: XLSExportResources_de.java,v 1.4 2003/08/25 14:29:30 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.xls.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * German language resource for the excel export GUI.
 *
 * @author Thomas Morgner
 */
public class XLSExportResources_de extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public XLSExportResources_de()
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
        {"action.export-to-excel.name", "Export nach Excel..."},
        {"action.export-to-excel.description", "Speichert den Bericht im MS-Excel format"},
        {"action.export-to-excel.mnemonic", new Integer(KeyEvent.VK_X)},

        {"error.processingfailed.title", "Fehler beim bearbeiten des Berichtes"},
        {"error.processingfailed.message", "Die Berichtsgenerierung ist fehlgeschlagen: {0}"},
        {"error.savefailed.message", "Der Bericht konnte nicht gespeichert werden: {0}"},
        {"error.savefailed.title", "Speichern fehlgeschlagen"},


        {"excelexportdialog.strict-layout", "Genaue Layout-Umwandlung benutzen."},
        {"excelexportdialog.dialogtitle", "Bericht als eine Excel-Datei speichern ..."},
        {"excelexportdialog.filename", "Dateiname"},
        {"excelexportdialog.author", "Autor"},
        {"excelexportdialog.title", "Titel"},
        {"excelexportdialog.selectFile", "Ausw\u00e4hlen"},

        {"excelexportdialog.warningTitle", "Warnung"},
        {"excelexportdialog.errorTitle", "Fehler"},
        {"excelexportdialog.targetIsEmpty",
         "Bitte geben Sie einen Dateinamen f\u00fcr die Excel-Datei an."},
        {"excelexportdialog.targetIsNoFile",
         "Der Dateiname zeigt auf keine gew\u00f6hnliche Datei."},
        {"excelexportdialog.targetIsNotWritable",
         "Sie besitzen keine ausreichenden Rechte um die ausgew\u00e4hlte Datei zu "
      + "\u00fcberschreiben."},
        {"excelexportdialog.targetOverwriteConfirmation",
         "Die Datei ''{0}'' existiert bereits. Soll diese Datei \u00fcberschrieben werden?"},
        {"excelexportdialog.targetOverwriteTitle", "Datei \u00fcberschreiben?"},

        {"excelexportdialog.cancel", "Abbrechen"},
        {"excelexportdialog.confirm", "OK"},

        {"excel-export.progressdialog.title", "Exportiere in eine Excel-Datei ..."},
        {"excel-export.progressdialog.message", 
          "Der Bericht wird nun in eine Excel-Arbeitsmappe gespeichert..."},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{XLSExportResources.class.getName(), "de"});
  }
}
