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
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PDFExportResources_de.java,v 1.2 2003/08/19 13:37:24 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 05-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.pdf.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * German language resource for the PDf export GUI.
 * 
 * @author Thomas Morgner
 */
public class PDFExportResources_de extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public PDFExportResources_de()
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
        {"action.save-as.name", "Speichern als PDF-Datei ..."},
        {"action.save-as.description", "Speichert den Bericht als PDF-Datei"},
        {"action.save-as.mnemonic", new Integer(KeyEvent.VK_S)},

        {"file.save.pdfdescription", "PDF Dateien"},

        {"error.processingfailed.title", "Fehler beim bearbeiten des Berichtes"},
        {"error.processingfailed.message", "Die Berichtsgenerierung ist fehlgeschlagen: {0}"},
        {"error.savefailed.message", "Der Bericht konnte nicht gespeichert werden: {0}"},
        {"error.savefailed.title", "Speichern fehlgeschlagen"},

        {"pdfsavedialog.warningTitle", "Warnung"},
        {"pdfsavedialog.dialogtitle", "Bericht in eine PDF-Datei speichern ..."},
        {"pdfsavedialog.filename", "Dateiname"},
        {"pdfsavedialog.author", "Autor"},
        {"pdfsavedialog.title", "Titel"},
        {"pdfsavedialog.selectFile", "Ausw\u00e4hlen"},
        {"pdfsavedialog.security", "Sicherheitseinstellungen und Verschl\u00fcsselung"},
        {"pdfsavedialog.encoding", "Zeichensatz"},

        {"pdfsavedialog.securityNone", "Keine Sicherheit"},
        {"pdfsavedialog.security40bit", "Verschl\u00fcsselung mit 40Bit Schl\u00fcssel"},
        {"pdfsavedialog.security128bit", "Verschl\u00fcsselung mit 128Bit Schl\u00fcssel"},
        {"pdfsavedialog.userpassword", "Benutzerkennwort"},
        {"pdfsavedialog.userpasswordconfirm", "Wiederholen"},
        {"pdfsavedialog.userpasswordNoMatch", "Die Benutzerkennworte stimmen nicht \u00fcberein."},
        {"pdfsavedialog.ownerpassword", "Hauptkennwort"},
        {"pdfsavedialog.ownerpasswordconfirm", "Wiederholen"},
        {"pdfsavedialog.ownerpasswordNoMatch", "Die Hauptkennworte stimmen nicht \u00fcberein."},

        {"pdfsavedialog.ownerpasswordEmpty", "Das Benutzerpasswort ist leer. Benutzer sind "
      + "m\u00f6glicherweise in der Lage die Sicherheitsbeschr\u00e4nkungen zu umgehen. "
      + "Trotzdem fortfahren?"},

        {"pdfsavedialog.errorTitle", "Fehler"},
        {"pdfsavedialog.targetIsEmpty",
         "Bitte geben Sie einen Dateinamen f\u00fcr die PDF-Datei an."},
        {"pdfsavedialog.targetIsNoFile", "Der Dateiname zeigt auf keine gew\u00f6hnliche Datei."},
        {"pdfsavedialog.targetIsNotWritable",
         "Sie besitzen keine ausreichenden Rechte um die ausgew\u00e4hlte Datei zu "
      + "\u00fcberschreiben."},
        {"pdfsavedialog.targetOverwriteConfirmation",
         "Die Datei ''{0}'' existiert bereits. Soll diese Datei \u00fcberschrieben werden?"},
        {"pdfsavedialog.targetOverwriteTitle", "Datei \u00fcberschreiben?"},


        {"pdfsavedialog.allowCopy", "Kopieren zulassen"},
        {"pdfsavedialog.allowPrinting", "Drucken zulassen"},
        {"pdfsavedialog.allowDegradedPrinting", "Drucken in verminderter Qualit\u00e4t zulassen"},
        {"pdfsavedialog.allowScreenreader", "Inhaltszugriff f\u00fcr Sehbehinderte aktivieren"},
        {"pdfsavedialog.allowAssembly", "Zusammensetzen erlauben"},
        {"pdfsavedialog.allowModifyContents", "Inhalt darf ge\u00e4ndert werden"},
        {"pdfsavedialog.allowModifyAnnotations", "Anmerkungen d\u00fcrfen ge\u00e4ndert werden"},
        {"pdfsavedialog.allowFillIn", "Formularfelder d\u00fcrfen ge\u00e4ndert werden"},

        {"pdfsavedialog.option.noprinting", "Kein Drucken"},
        {"pdfsavedialog.option.degradedprinting", "Drucken mit verminderter Qualit\u00e4t"},
        {"pdfsavedialog.option.fullprinting", "Drucken erlaubt"},

        {"pdfsavedialog.cancel", "Abbrechen"},
        {"pdfsavedialog.confirm", "OK"},

        {"pdf-export.progressdialog.title", "Exportiere in eine PDF-Datei ..."},
        {"pdf-export.progressdialog.message", "Der Bericht wird nun in ein PDF-Dokument gespeichert ..."},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{PDFExportResources.class.getName(), "de"});
  }
}
