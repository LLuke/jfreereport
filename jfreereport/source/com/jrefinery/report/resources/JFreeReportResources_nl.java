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
 * JFreeReportResources_nl.java
 * ----------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Hendri Smit;
 * Contributor(s):   -;
 *
 * $Id: JFreeReportResources_fr.java,v 1.5 2003/04/09 16:16:07 mungady Exp $
 */
package com.jrefinery.report.resources;

import java.awt.event.KeyEvent;

/**
 * Dutch Language Resources.
 *
 * @author Hendri Smit
 */
public class JFreeReportResources_nl extends JFreeReportResources
{

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
        {"action.save-as.name", "Opslaan Als PDF..."},
        {"action.save-as.description", "Opslaan in PDF-formaat"},
        {"action.save-as.mnemonic", new Integer(KeyEvent.VK_L)},

        {"action.page-setup.name", "Pagina-instelling..."},
        {"action.page-setup.description", "Pagina instellen"},
        {"action.page-setup.mnemonic", new Integer(KeyEvent.VK_G)},

        {"action.export-to-plaintext.name", "Opslaan als Tekst Bestand..."},
        {"action.export-to-plaintext.description", "Opslaan als Tekst zonder opmaak"},
        {"action.export-to-plaintext.mnemonic", new Integer(KeyEvent.VK_T)},

        {"action.export-to-excel.name", "Exporteren naar Excel..."},
        {"action.export-to-excel.description", "Opslaan in MS-Excel formaat"},
        {"action.export-to-excel.mnemonic", new Integer(KeyEvent.VK_X)},

        {"action.export-to-html.name", "Exporteren naar HTML..."},
        {"action.export-to-html.description", "Opslaan in HTML formaat"},
        {"action.export-to-html.mnemonic", new Integer(KeyEvent.VK_H)},

        {"action.export-to-csv.name", "Exporteren naar CSV..."},
        {"action.export-to-csv.description", "Opslaan in CSV formaat"},
        {"action.export-to-csv.mnemonic", new Integer(KeyEvent.VK_C)},

        {"action.print.name", "Afdrukken..."},
        {"action.print.description", "Afdrukken"},
        {"action.print.mnemonic", new Integer(KeyEvent.VK_D)},

        {"action.close.name", "Sluiten"},
        {"action.close.description", "Sluit afdrukvoorbeeld"},
        {"action.close.mnemonic", new Integer(KeyEvent.VK_S)},

        {"action.gotopage.name", "Ga naar..."},
        {"action.gotopage.description", "Ga naar opgegeven pagina"},

        {"dialog.gotopage.message", "Geef pagina nummer"},
        {"dialog.gotopage.title", "Ga naar pagina ..."},

        {"action.about.name", "Info..."},
        {"action.about.description", "Informatie over de applicatie"},
        {"action.about.mnemonic", new Integer(KeyEvent.VK_I)},

        {"action.firstpage.name", "Begin"},
        {"action.firstpage.description", "Ga naar de eerste pagina"},

        {"action.lastpage.name", "Einde"},
        {"action.lastpage.description", "Ga naar de laatste pagina"},

        {"action.back.name", "Terug"},
        {"action.back.description", "Ga naar de vorige pagina"},

        {"action.forward.name", "Verder"},
        {"action.forward.description", "Ga naar de volgende pagina"},

        {"action.zoomIn.name", "Vergroten"},
        {"action.zoomIn.description", "Inzoomen"},

        {"action.zoomOut.name", "Verkleinen"},
        {"action.zoomOut.description", "Uitzoomen"},

        {"preview-frame.title", "Afdrukvoorbeeld"},

        {"menu.file.name", "Bestand"},
        {"menu.file.mnemonic", new Character('B')},

        {"menu.help.name", "Help"},
        {"menu.help.mnemonic", new Character('H')},

        {"file.save.pdfdescription", "PDF Documenten"},
        {"statusline.pages", "Pagina {0} van {1}"},
        {"statusline.error", "Er is een fout ontstaan in de report generatie: {0}"},
        {"statusline.repaginate", "Paginanummering berekenen..."},
        {"error.processingfailed.title", "Fout tijdens report generatie"},
        {"error.processingfailed.message", "Report generatie mislukt: {0}"},
        {"error.savefailed.message", "Opslaan als PDF mislukt: {0}"},
        {"error.savefailed.title", "Fout tijdens opslaan"},
        {"error.printfailed.message", "Afdrukken mislukt: {0}"},
        {"error.printfailed.title", "Fout tijdens afdrukken"},
        {"error.validationfailed.message", "Validatie van gebruikersinvoer mislukt."},
        {"error.validationfailed.title", "Fout tijdens validatie"},

        {"tabletarget.page", "Pagina {0}"},

        {"pdfsavedialog.warningTitle", "Waarschuwing"},
        {"pdfsavedialog.dialogtitle", "Opslaan in PDF formaat..."},
        {"pdfsavedialog.filename", "Bestandsnaam"},
        {"pdfsavedialog.author", "Auteur"},
        {"pdfsavedialog.title", "Titel"},
        {"pdfsavedialog.selectFile", "Selecteer Bestand"},
        {"pdfsavedialog.security", "Beveiligingsinstellingen en Encryptie"},
        {"pdfsavedialog.encoding", "Encoderen"},

        {"pdfsavedialog.securityNone", "Geen Beveiliging"},
        {"pdfsavedialog.security40bit", "Encryptie met 40 bit sleutels"},
        {"pdfsavedialog.security128bit", "Encryptie met 128 bit sleutels"},
        {"pdfsavedialog.userpassword", "Gebruikerswachtwoord"},
        {"pdfsavedialog.userpasswordconfirm", "Herhalen"},
        {"pdfsavedialog.userpasswordNoMatch", "De gebruikerswachtwoorden komen niet overeen."},
        {"pdfsavedialog.ownerpassword", "Eigenaarswachtwoord"},
        {"pdfsavedialog.ownerpasswordconfirm", "Herhalen"},
        {"pdfsavedialog.ownerpasswordNoMatch", "De eigenaarswachtwoorden komen niet overeen."},

        {"pdfsavedialog.ownerpasswordEmpty", "Het eigenaarswachtwoord is leeg. Gebruikers kunnen " 
            + "beveiligingsinstellingen wijzigen. Doorgaan?"},

        {"pdfsavedialog.errorTitle", "Fout"},
        {"pdfsavedialog.targetIsEmpty", "Geef een bestandsnaam voor het PDF bestand."},
        {"pdfsavedialog.targetIsNoFile", "Doelbestand is ongeldig."},
        {"pdfsavedialog.targetIsNotWritable", "Doelbestand kan niet worden beschreven."},
        {"pdfsavedialog.targetOverwriteConfirmation",
         "Het bestand ''{0}'' bestaat reeds. Overschrijven?"},
        {"pdfsavedialog.targetOverwriteTitle", "Bestand overschrijven?"},


        {"pdfsavedialog.allowCopy", "Kopieren toestaan"},
        {"pdfsavedialog.allowPrinting", "Afdrukken toestaan"},
        {"pdfsavedialog.allowDegradedPrinting", "Afdrukken in verminderde kwaliteit toestaan"},
        {"pdfsavedialog.allowScreenreader", "Gebruik van Screenreaders toestaan"},
        {"pdfsavedialog.allowAssembly", "Assemblage toestaan"},
        {"pdfsavedialog.allowModifyContents", "Inhoud wijzigen toestaan"},
        {"pdfsavedialog.allowModifyAnnotations", "Opmerkingen wijzigen toestaan"},
        {"pdfsavedialog.allowFillIn", "Formulierdata invullen toestaan"},

        {"pdfsavedialog.option.noprinting", "Afdrukken niet toegestaan"},
        {"pdfsavedialog.option.degradedprinting", "Afdrukken in verminderde kwaliteit"},
        {"pdfsavedialog.option.fullprinting", "Afdrukken geoorloofd"},

        {"pdfsavedialog.cancel", "Annuleren"},
        {"pdfsavedialog.confirm", "OK"},


        {"excelexportdialog.strict-layout", "Exacte layout gebruiken."},
        {"excelexportdialog.dialogtitle", "Opslaan als Excel bestand..."},
        {"excelexportdialog.filename", "Bestandsnaam"},
        {"excelexportdialog.author", "Auteur"},
        {"excelexportdialog.title", "Titel"},
        {"excelexportdialog.selectFile", "Selecteer Bestand"},

        {"excelexportdialog.warningTitle", "Waarschuwing"},
        {"excelexportdialog.errorTitle", "Fout"},
        {"excelexportdialog.targetIsEmpty",
         "Geef een bestandsnaam voor het Excel bestand."},
        {"excelexportdialog.targetIsNoFile", 
            "Doelbestand is ongeldig."},
        {"excelexportdialog.targetIsNotWritable",
            "Doelbestand kan niet worden beschreven."},
        {"excelexportdialog.targetOverwriteConfirmation",
         "Het bestand ''{0}'' bestaat reeds. Overschrijven?"},
        {"excelexportdialog.targetOverwriteTitle", "Bestand overschrijven?"},

        {"excelexportdialog.cancel", "Annuleren"},
        {"excelexportdialog.confirm", "OK"},

        {"htmlexportdialog.dialogtitle", "Opslaan als HTML bestand..."},
        {"htmlexportdialog.filename", "Bestandsnaam"},
        {"htmlexportdialog.author", "Auteur"},
        {"htmlexportdialog.title", "Titel"},

        {"htmlexportdialog.warningTitle", "Waarschuwing"},
        {"htmlexportdialog.errorTitle", "Fout"},
        {"htmlexportdialog.targetIsEmpty",
         "Geef een bestandsnaam voor het HTML bestand."},
        {"htmlexportdialog.targetIsNoFile", 
            "Doelbestand is ongeldig."},
        {"htmlexportdialog.targetIsNotWritable",
            "Doelbestand kan niet worden beschreven."},
        {"htmlexportdialog.targetOverwriteConfirmation",
         "Het bestand ''{0}'' bestaat reeds. Overschrijven?"},
        {"htmlexportdialog.targetOverwriteTitle", "Bestand overschrijven?"},

        {"htmlexportdialog.cancel", "Annuleren"},
        {"htmlexportdialog.confirm", "OK"},

        {"htmlexportdialog.strict-layout", "Exacte layout gebruiken."},
        {"htmlexportdialog.copy-external-references", "Externe referenties kopiëren."},
        {"htmlexportdialog.datafilename", "Data map"},
        {"htmlexportdialog.encoding", "Encoderen"},
        {"htmlexportdialog.generate-html4", "Genereer HTML4 uitvoer"},
        {"htmlexportdialog.generate-xhtml", "Genereer XHTML1.0 uitvoer"},

        {"htmlexportdialog.selectDirFile", "Selecteer Bestand"},
        {"htmlexportdialog.selectStreamFile", "Selecteer Bestand"},
        {"htmlexportdialog.selectZipFile", "Selecteer Bestand"},

        {"htmlexportdialog.targetCreateDataDirConfirmation",
            "De opgegeven data map bestaat niet, " 
            + "moeten de ontbrekende submappen worden gecreëerd?"},
        {"htmlexportdialog.targetCreateDataDirTitle", "Data map aanmaken?"},
        {"htmlexportdialog.targetDataDirIsNoDirectory",
         "Data map is ongeldig."},
        {"htmlexportdialog.targetPathIsAbsolute",
         "Het opgegeven doel is geen map in het ZIP-bestand."},

        {"csvexportdialog.dialogtitle", "Opslaan als CSV bestand..."},
        {"csvexportdialog.filename", "Bestandsnaam"},
        {"csvexportdialog.encoding", "Encoderen"},
        {"csvexportdialog.separatorchar", "Scheidingsteken"},
        {"csvexportdialog.selectFile", "Selecteer Bestand"},

        {"csvexportdialog.warningTitle", "Waarschuwing"},
        {"csvexportdialog.errorTitle", "Fout"},
        {"csvexportdialog.targetIsEmpty", 
            "Geef een bestandsnaam voor het CSV bestand."},
        {"csvexportdialog.targetIsNoFile", "Doelbestand is ongeldig."},
        {"csvexportdialog.targetIsNotWritable",
         "Doelbestand kan niet worden beschreven."},

        {"csvexportdialog.targetOverwriteConfirmation",
         "Het bestand ''{0}'' bestaat reeds. Overschrijven?"},
        {"csvexportdialog.targetOverwriteTitle", "Bestand overschrijven?"},

        {"csvexportdialog.cancel", "Annuleren"},
        {"csvexportdialog.confirm", "OK"},
        {"csvexportdialog.strict-layout", "Exacte layout gebruiken."},

        {"csvexportdialog.separator.tab", "Tabulator"},
        {"csvexportdialog.separator.colon", "Komma (,)"},
        {"csvexportdialog.separator.semicolon", "Punt-komma (;)"},
        {"csvexportdialog.separator.other", "Anders"},

        {"csvexportdialog.exporttype", "Selecteer export methode"},
        {"csvexportdialog.export.data", "Exporteer de brondata (Ruwe data)"},
        {"csvexportdialog.export.printed_elements", 
            "Afgedrukte elementen (Layout data)"},

        {"convertdialog.action.convert.description", "Converteer de bronbestanden."},
        {"convertdialog.action.convert.name", "Converteren"},
        {"convertdialog.action.selectSource.description", "Selecteer brondbestand."},
        {"convertdialog.action.selectSource.name", "Selecteer"},
        {"convertdialog.action.selectTarget.description", "Selecteer doelbestand."},
        {"convertdialog.action.selectTarget.name", "Selecteer"},
        {"convertdialog.errorTitle", "Fout"},
        {"convertdialog.sourceFile", "Bronbestand"},
        {"convertdialog.sourceIsEmpty", "Bronbestand is leeg."},
        {"convertdialog.sourceIsNoFile", "Ongeldig bronbestand."},
        {"convertdialog.sourceIsNotReadable", "Het bronbestand kan niet worden gelezen."},
        {"convertdialog.targetFile", "Doelbestand"},
        {"convertdialog.targetIsEmpty", "Doelbestand is leeg."},
        {"convertdialog.targetIsNoFile", "Ongeldig doelbestand."},
        {"convertdialog.targetIsNotWritable", "Het doelbestand kan niet worden beschreven."},
        {"convertdialog.targetOverwriteConfirmation",
         "Het bestand ''{0}'' bestaat reeds. Overschrijven?"},
        {"convertdialog.targetOverwriteTitle", "Bestand overschrijven?"},
        {"convertdialog.title", "Report-Converter"},

        {"plain-text-exportdialog.cancel", "Annuleren"},
        {"plain-text-exportdialog.chars-per-inch", "kpi (Karakters per inch)"},
        {"plain-text-exportdialog.confirm", "OK"},
        {"plain-text-exportdialog.dialogtitle", "Opslaan als Text Bestand..."},
        {"plain-text-exportdialog.encoding", "Encoderen"},
        {"plain-text-exportdialog.errorTitle", "Fout"},
        {"plain-text-exportdialog.filename", "Bestandsnaam"},
        {"plain-text-exportdialog.font-settings", "Lettertype-instellingen"},
        {"plain-text-exportdialog.lines-per-inch", "rpi (Regels per inch)"},
        {"plain-text-exportdialog.printer", "Printer"},
        {"plain-text-exportdialog.printer.epson", "Epson ESC/P compatible printer"},
        {"plain-text-exportdialog.printer.ibm", "IBM compatible printer"},
        {"plain-text-exportdialog.printer.plain", "Simpele Tekstuitvoer"},
        {"plain-text-exportdialog.selectFile", "Selecteer bestand"},
        {"plain-text-exportdialog.targetIsEmpty", "Geef een bestandsnaam"},
        {"plain-text-exportdialog.targetIsNoFile", "Doelbestand is ongeldig"},
        {"plain-text-exportdialog.targetIsNotWritable", "Doelbestand kan niet worden beschreven."},
        {"plain-text-exportdialog.targetOverwriteConfirmation", 
	    "Het bestand ''{0}'' bestaat reeds. Overschrijven?"},
        {"plain-text-exportdialog.targetOverwriteTitle", "Bestand overschrijven?"},
        {"plain-text-exportdialog.warningTitle", "Waarschuwing"},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main (String [] args)
  {
    ResourceCompareTool.main(new String[]{"nl"});
  }
  
}
