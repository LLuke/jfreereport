/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ----------------------------
 * JFreeReportResources_de.java
 * ----------------------------
 *
 * $Id: JFreeReportResources_de.java,v 1.30 2003/02/26 16:42:21 mungady Exp $
 *
 * Changes
 * -------
 * 16-Feb-2003 : Corrected version submitted by Helmut Leininger
 *
 */
package com.jrefinery.report.resources;

import java.awt.event.KeyEvent;

/**
 * German Language Resources.
 *
 * @author Thomas Morgner
 */
public class JFreeReportResources_de extends JFreeReportResources
{

  /**
   * Unicode translation:
   *
   * &auml; - \u00E4
   * &Auml; - \u00C4
   * &ouml; - \u00f6
   * &Ouml; - \u00d6
   * &Uuml; - \u00dc
   * &uuml; - \u00fc
   * &szlig; - \u00df
   *
   */
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
        {"action.save-as.name", "Speichern als PDF-Datei ..."},
        {"action.save-as.description", "Speichert den Bericht als PDF-Datei"},
        {"action.save-as.mnemonic", new Integer(KeyEvent.VK_S)},

        {"action.page-setup.name", "Seite einrichten"},
        {"action.page-setup.description", "Seite einrichten"},
        {"action.page-setup.mnemonic", new Integer(KeyEvent.VK_E)},

        {"action.export-to-plaintext.name", "Speichen als Text-Datei ..."},
        {"action.export-to-plaintext.description", "Speichert den Bericht als Text Datei"},
        {"action.export-to-plaintext.mnemonic", new Integer(KeyEvent.VK_T)},

        {"action.export-to-excel.name", "Export nach Excel..."},
        {"action.export-to-excel.description", "Speichert den Bericht im MS-Excel format"},
        {"action.export-to-excel.mnemonic", new Integer(KeyEvent.VK_X)},

        {"action.export-to-html.name", "Export nach HTML..."},
        {"action.export-to-html.description", "Speichert den Bericht im HTML format"},
        {"action.export-to-html.mnemonic", new Integer(KeyEvent.VK_H)},

        {"action.export-to-csv.name", "Export nach CSV..."},
        {"action.export-to-csv.description", "Speichert den Bericht im CSV format"},
        {"action.export-to-csv.mnemonic", new Integer(KeyEvent.VK_C)},

        {"action.print.name", "Drucken..."},
        {"action.print.description", "Druckt den Bericht"},
        {"action.print.mnemonic", new Integer(KeyEvent.VK_D)},

        {"action.close.name", "Schliessen"},
        {"action.close.description", "Beendet die Seitenansicht"},
        {"action.close.mnemonic", new Integer(KeyEvent.VK_B)},

        {"action.gotopage.name", "Gehe zu ..."},
        {"action.gotopage.description", "Wechselt zu einer bestimmten Seite im Bericht."},

        {"dialog.gotopage.message", "Seitenzahl eingeben"},
        {"dialog.gotopage.title", "Gehe zu Seite ..."},

        {"action.about.name", "\u00dcber..."},
        {"action.about.description", "Informationen \u00fcber JFreeReport"},
        {"action.about.mnemonic", new Integer(KeyEvent.VK_A)},

        {"action.firstpage.name", "Anfang"},
        {"action.firstpage.description", "Gehe zum StartState"},

        {"action.lastpage.name", "Ende"},
        {"action.lastpage.description", "Gehe zum Ende"},

        {"action.back.name", "Zur\u00fcck"},
        {"action.back.description", "Wechselt zur vorherigen Seite"},

        {"action.forward.name", "N\u00e4chste"},
        {"action.forward.description", "Wechselt zur n\u00e4chsten Seite"},

        {"action.zoomIn.name", "Vergr\u00f6ssern"},
        {"action.zoomIn.description",
         "Zeigt die aktuelle Seite in einem gr\u00f6sseren Masstab an"},

        {"action.zoomOut.name", "Verkleinern"},
        {"action.zoomOut.description",
         "Zeigt die aktuelle Seite in einem kleineren Masstab an"},

        {"preview-frame.title", "Seitenansicht"},

        {"menu.file.name", "Datei"},
        {"menu.file.mnemonic", new Character('D')},

        {"menu.help.name", "Hilfe"},
        {"menu.help.mnemonic", new Character('H')},

        {"file.save.pdfdescription", "PDF Dateien"},
        {"statusline.pages", "Seite {0} von {1}"},
        {"statusline.error", "Die ReportGenerierung ist fehlgeschlagen: {0}"},
        {"statusline.repaginate", "Erzeuge Seitenumbr\u00fcche ..."},
        {"error.processingfailed.title", "Fehler beim bearbeiten des Berichtes"},
        {"error.processingfailed.message", "Die Berichtsgenerierung ist fehlgeschlagen: {0}"},
        {"error.savefailed.message", "Der Bericht konnte nicht gespeichert werden: {0}"},
        {"error.savefailed.title", "Speichern fehlgeschlagen"},
        {"error.printfailed.message", "Das Drucken ist fehlgeschlagen: {0}"},
        {"error.printfailed.title", "Druck fehlgeschlagen"},
        {"error.validationfailed.message", 
            "Die \u00fcberpr\u00fcfung der Benutzereingaben schlug fehl."},
        {"error.validationfailed.title", "Eingabe\u00fcberpr\u00fcfung fehlgeschlagen"},

        {"tabletarget.page", "Seite {0}"},

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

        {"convertdialog.action.convert.description", "Konvertiert die Quelldatei"},
        {"convertdialog.action.convert.name", "Konvertieren"},
        {"convertdialog.action.selectSource.description", "W\u00e4hlt die Quelldatei aus."},
        {"convertdialog.action.selectSource.name", "Ausw\u00e4hlen"},
        {"convertdialog.action.selectTarget.description", "W\u00e4hlt die Zieldatei aus"},
        {"convertdialog.action.selectTarget.name", "Ausw\u00e4hlen"},
        {"convertdialog.errorTitle", "Fehler"},
        {"convertdialog.sourceFile", "Quelldatei"},
        {"convertdialog.sourceIsEmpty", "Es wurde keine Quelldatei angegeben."},
        {"convertdialog.sourceIsNoFile", "Die Quelldatei ist keine normale Datei."},
        {"convertdialog.sourceIsNotReadable", "Die Quelldatei kann nicht gelesen werden."},
        {"convertdialog.targetFile", "Zieldatei"},
        {"convertdialog.targetIsEmpty", "Es wurde keine Zieldatei angegeben."},
        {"convertdialog.targetIsNoFile", "Die Zieldatei ist keine normale Datei."},
        {"convertdialog.targetIsNotWritable", "Die Zieldatei ist nicht beschreibbar."},
        {"convertdialog.targetOverwriteConfirmation",
         "Die Zieldatei existiert bereits.\nSoll die Datei \u00fcberschrieben werden?"},
        {"convertdialog.targetOverwriteTitle", "Datei \u00fcberschreiben?"},
        {"convertdialog.title", "Report-Konverter"},

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

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main (String [] args)
  {
    new JFreeReportResources_de().printAll();
  }
  
}
