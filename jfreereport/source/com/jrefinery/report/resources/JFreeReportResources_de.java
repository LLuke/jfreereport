/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -------------------------
 * JFreeReportResources_de.java
 * -------------------------
 *
 */
package com.jrefinery.report.resources;

import java.awt.event.KeyEvent;

public class JFreeReportResources_de extends JFreeReportResources
{

  /**
   * Returns the array of strings in the resource bundle.
   */
  public Object[][] getContents ()
  {
    return contents;
  }

  /** The resources to be localised. */
  private static final Object[][] contents =
          {
            {"action.save-as.name", "Speichern als..."},
            {"action.save-as.description", "Speichert den Bericht als PDF-Datei"},
            {"action.save-as.mnemonic", new Integer (KeyEvent.VK_S)},

            {"action.page-setup.name", "Seite einrichten"},
            {"action.page-setup.description", "Seite einrichten"},
            {"action.page-setup.mnemonic", new Integer (KeyEvent.VK_E)},

            {"action.print.name", "Drucken..."},
            {"action.print.description", "Druckt den Bericht"},
            {"action.print.mnemonic", new Integer (KeyEvent.VK_D)},

            {"action.close.name", "Schliessen"},
            {"action.close.description", "Beendet die Seitenansicht"},
            {"action.close.mnemonic", new Integer (KeyEvent.VK_B)},

            {"action.gotopage.name", "Gehe zu ..."},
            {"action.gotopage.description", "Wechselt zu einer bestimmten Seite im Bericht."},

            {"dialog.gotopage.message", "Seitenzahl eingeben"},
            {"dialog.gotopage.title", "Gehe zu Seite ..."},

            {"action.about.name", "�ber..."},
            {"action.about.description", "Informationen �ber JFreeReport"},
            {"action.about.mnemonic", new Integer (KeyEvent.VK_A)},

            {"action.firstpage.name", "Anfang"},
            {"action.firstpage.description", "Gehe zum Start"},

            {"action.lastpage.name", "Ende"},
            {"action.lastpage.description", "Gehe zum Ende"},

            {"action.back.name", "Zur�ck"},
            {"action.back.description", "Wechselt zur vorherigen Seite"},

            {"action.forward.name", "N�chste"},
            {"action.forward.description", "Wechselt zur n�chsten Seite"},

            {"action.zoomIn.name", "Vergr�ssern"},
            {"action.zoomIn.description", "Zeigt die aktuelle Seite in einem gr�sseren Masstab an"},

            {"action.zoomOut.name", "Verkleinern"},
            {"action.zoomOut.description", "Zeigt die aktuelle Seite in einem kleineren Masstab an"},

            {"preview-frame.title", "Seitenansicht"},

            {"menu.file.name", "Datei"},
            {"menu.file.mnemonic", new Character ('D')},

            {"menu.help.name", "Hilfe"},
            {"menu.help.mnemonic", new Character ('H')},

            {"file.save.pdfdescription", "PDF Dateien"},
            {"statusline.pages", "Seite {0} von {1}"},
            {"statusline.error", "Die ReportGenerierung ist fehlgeschlagen: {0}"},
            {"error.processingfailed.title", "Fehler beim bearbeiten des Berichtes"},
            {"error.processingfailed.message", "Die Berichtsgenerierung ist fehlgeschlagen: {0}"},
            {"error.savefailed.message", "Der Bericht konnte nicht gespeichert werden: {0}"},
            {"error.savefailed.title", "Speichern fehlgeschlagen"},
            {"error.printfailed.message", "Das Drucken ist fehlgeschlagen: {0}"},
            {"error.printfailed.title", "Druck fehlgeschlagen"},

            {"pdfsavedialog.dialogtitle", "Bericht in eine PDF-Datei speichern ..."},
            {"pdfsavedialog.filename", "Dateiname"},
            {"pdfsavedialog.author", "Autor"},
            {"pdfsavedialog.title", "Titel"},
            {"pdfsavedialog.selectFile", "Ausw�hlen"},
            {"pdfsavedialog.security", "Sicherheitseinstellungen und Verschl�sselung"},

            {"pdfsavedialog.securityNone", "Keine Sicherheit"},
            {"pdfsavedialog.security40bit", "Verschl�sselung mit 40Bit Schl�ssel"},
            {"pdfsavedialog.security128bit", "Verschl�sselung mit 128Bit Schl�ssel"},
            {"pdfsavedialog.userpassword", "Benutzerkennwort"},
            {"pdfsavedialog.userpasswordconfirm", "Wiederholen"},
            {"pdfsavedialog.userpasswordNoMatch", "Die Benutzerkennworte stimmen nicht �berein."},
            {"pdfsavedialog.ownerpassword", "Hauptkennwort"},
            {"pdfsavedialog.ownerpasswordconfirm", "Wiederholen"},
            {"pdfsavedialog.ownerpasswordNoMatch", "Die Hauptkennworte stimmen nicht �berein."},

            {"pdfsavedialog.errorTitle", "Fehler"},
            {"pdfsavedialog.targetIsEmpty", "Bitte geben Sie einen Dateinamen f�r die PDF-Datei an."},
            {"pdfsavedialog.targetIsNoFile", "Der Dateiname zeigt auf keine gew�hnliche Datei."},
            {"pdfsavedialog.targetIsNotWritable", "Sie besitzen keine ausreichenden Rechte um die ausgew�hlte Datei zu �berschreiben."},
            {"pdfsavedialog.targetOverwriteConfirmation", "Die Datei ''{0}'' existiert bereits. Soll diese Datei �berschrieben werden?"},
            {"pdfsavedialog.targetOverwriteTitle", "Datei �berschreiben?"},


            {"pdfsavedialog.allowCopy", "Kopieren zulassen"},
            {"pdfsavedialog.allowPrinting", "Drucken zulassen"},
            {"pdfsavedialog.allowDegradedPrinting", "Drucken in verminderter Qualit�t zulassen"},
            {"pdfsavedialog.allowScreenreader", "Inhaltszugriff f�r Sehbehinderte aktiveren"},
            {"pdfsavedialog.allowAssembly", "Zusammensetzen erlauben"},
            {"pdfsavedialog.allowModifyContents", "Inhalt darf ge�ndert werden"},
            {"pdfsavedialog.allowModifyAnnotations", "Anmerkungen d�rfen ge�ndert werden"},
            {"pdfsavedialog.allowFillIn", "Formularfelder d�rfen ge�ndert werden"},

            {"pdfsavedialog.option.noprinting", "Kein Drucken"},
            {"pdfsavedialog.option.degradedprinting", "Drucken mit verminderter Qualit�t"},
            {"pdfsavedialog.option.fullprinting", "Drucken erlaubt"},

            {"pdfsavedialog.cancel", "Abbrechen"},
            {"pdfsavedialog.confirm", "OK"},

          };
}
