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
 * HtmlExportResources.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 05.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.html.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;
import org.jfree.report.modules.gui.csv.resources.CSVExportResources;

public class HtmlExportResources_de extends JFreeReportResources
{
  public HtmlExportResources_de()
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

        {"action.export-to-html.name", "Export nach HTML..."},
        {"action.export-to-html.description", "Speichert den Bericht im HTML format"},
        {"action.export-to-html.mnemonic", new Integer(KeyEvent.VK_H)},

        {"htmlexportdialog.dialogtitle", "Bericht als eine Html-Datei speichern ..."},
        {"htmlexportdialog.filename", "Dateiname"},
        {"htmlexportdialog.author", "Autor"},
        {"htmlexportdialog.title", "Titel"},

        {"htmlexportdialog.warningTitle", "Warnung"},
        {"htmlexportdialog.errorTitle", "Fehler"},
        {"htmlexportdialog.targetIsEmpty",
         "Bitte geben Sie einen Dateinamen f\u00dcr die Html-Datei an."},
        {"htmlexportdialog.targetIsNoFile",
         "Der Dateiname zeigt auf keine gew\u00f6hnliche Datei."},
        {"htmlexportdialog.targetIsNotWritable",
         "Sie besitzen keine ausreichenden Rechte um die ausgew\u00e4hlte Datei zu "
      + "\u00fcberschreiben."},
        {"htmlexportdialog.targetOverwriteConfirmation",
         "Die Datei ''{0}'' existiert bereits. Soll diese Datei \u00fcberschrieben werden?"},
        {"htmlexportdialog.targetOverwriteTitle", "Datei \u00fcberschreiben?"},

        {"htmlexportdialog.cancel", "Abbrechen"},
        {"htmlexportdialog.confirm", "OK"},

        {"htmlexportdialog.strict-layout", "Genaue Layout-Umwandlung benutzen."},
        {"htmlexportdialog.copy-external-references", "Externe Resourcen kopieren."},
        {"htmlexportdialog.datafilename", "Datenverzeichnis"},
        {"htmlexportdialog.encoding", "Zeichensatz"},
        {"htmlexportdialog.generate-html4", "Datei im HTML4 Format erzeugen"},
        {"htmlexportdialog.generate-xhtml", "Datei im XHTML1.0 Format erzeugen"},

        {"htmlexportdialog.selectDirFile", "Ausw\u00e4hlen"},
        {"htmlexportdialog.selectStreamFile", "Ausw\u00e4hlen"},
        {"htmlexportdialog.selectZipFile", "Ausw\u00e4hlen"},

        {"htmlexportdialog.targetCreateDataDirConfirmation",
         "Das eingegebene Datenverzeichnis existiert nicht, "
      + "soll ein neues Verzeichnis erzeugt werden?"},
        {"htmlexportdialog.targetCreateDataDirTitle", "Datenverzeichnis erzeugen?"},
        {"htmlexportdialog.targetDataDirIsNoDirectory",
         "Das angegebene Datenverzeichnis verweist nicht auf ein Verzeichnis."},
        {"htmlexportdialog.targetPathIsAbsolute",
         "Das eingebene Datenverzeichnis ist kein Pfad innerhalb der ZIP-Datei."},

        {"htmlexportdialog.html-documents", "HTML Dateien"},
        {"htmlexportdialog.zip-archives", "ZIP Archive"},

        {"htmlexportdialog.stream-export", "Datei Export"},
        {"htmlexportdialog.directory-export", "Directory Export"},
        {"htmlexportdialog.zip-export", "ZIP-Archiv Export"},

        {"error.processingfailed.title", "Fehler beim bearbeiten des Berichtes"},
        {"error.processingfailed.message", "Die Berichtsgenerierung ist fehlgeschlagen: {0}"},
        {"error.savefailed.message", "Der Bericht konnte nicht gespeichert werden: {0}"},
        {"error.savefailed.title", "Speichern fehlgeschlagen"},
        {"error.validationfailed.message",
         "Die \u00fcberpr\u00fcfung der Benutzereingaben schlug fehl."},
        {"error.validationfailed.title", "Eingabe\u00fcberpr\u00fcfung fehlgeschlagen"},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{HtmlExportResources.class.getName(), "de"});
  }

}
