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
 * PlainTextExportResources_de.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PlainTextExportResources_de.java,v 1.4 2003/08/25 14:29:30 taqua Exp $
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
 * German language resource for the plain text export GUI.
 *
 * @author Thomas Morgner
 */
public class PlainTextExportResources_de extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public PlainTextExportResources_de()
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
        {"action.export-to-plaintext.name", "Speichen als Text-Datei ..."},
        {"action.export-to-plaintext.description", "Speichert den Bericht als Text Datei"},
        {"action.export-to-plaintext.mnemonic", new Integer(KeyEvent.VK_T)},

        {"plain-text-exportdialog.cancel", "Abbrechen"},
        {"plain-text-exportdialog.chars-per-inch", "cpi (Zeichen pro Zoll)"},
        {"plain-text-exportdialog.confirm", "OK"},
        {"plain-text-exportdialog.dialogtitle", "Bericht als Text speichern ..."},
        {"plain-text-exportdialog.encoding", "Zeichensatz"},
        {"plain-text-exportdialog.errorTitle", "Fehler"},
        {"plain-text-exportdialog.filename", "Dateiname"},
        {"plain-text-exportdialog.font-settings", "Drucker-Schriftart"},
        {"plain-text-exportdialog.lines-per-inch", "lpi (Zeilen pro Zoll)"},
        {"plain-text-exportdialog.printer", "Drucker"},
        {"plain-text-exportdialog.printer.epson", "Epson ESC/P kompatibler Drucker"},
        {"plain-text-exportdialog.printer.ibm", "IBM kompatibler Drucker"},
        {"plain-text-exportdialog.printer.plain", "Einfache Textausgabe"},
        {"plain-text-exportdialog.selectFile", "Ausw\u00e4hlen"},
        {"plain-text-exportdialog.targetIsEmpty", "Es wurde keine Zieldatei angegeben."},
        {"plain-text-exportdialog.targetIsNoFile",
         "Die angegebene Zieldatei ist keine gew\u00f6hnliche Datei."},
        {"plain-text-exportdialog.targetIsNotWritable",
         "Die Zieldatei kann nicht beschrieben werden."},
        {"plain-text-exportdialog.targetOverwriteConfirmation",
         "Die Zieldatei existiert bereits. Soll diese Datei \u00fcberschrieben werden?"},
        {"plain-text-exportdialog.targetOverwriteTitle", "Datei \u00fcberschreiben?"},
        {"plain-text-exportdialog.warningTitle", "Warnung"},

        {"error.processingfailed.title", "Fehler beim bearbeiten des Berichtes"},
        {"error.processingfailed.message", "Die Berichtsgenerierung ist fehlgeschlagen: {0}"},
        {"error.savefailed.message", "Der Bericht konnte nicht gespeichert werden: {0}"},
        {"error.savefailed.title", "Speichern fehlgeschlagen"},

        {"plaintext-export.progressdialog.title", "Exportiere in eine Text Datei ..."},
        {"plaintext-export.progressdialog.message", 
            "Der Bericht wird nun in eine Text Datei exportiert ..."},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{PlainTextExportResources.class.getName(), "de"});
  }
}
