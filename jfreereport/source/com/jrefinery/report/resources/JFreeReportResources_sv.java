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
 * ----------------------------
 * JFreeReportResources_de.java
 * ----------------------------
 *
 * $Id: JFreeReportResources_sv.java,v 1.5 2003/06/27 14:25:22 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package com.jrefinery.report.resources;

import java.awt.event.KeyEvent;

/**
 * Swedish Language Resources.
 *
 * @author Thomas Dilts
 */
public class JFreeReportResources_sv extends JFreeReportResources
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
        {"action.save-as.name", "Spara som PDF..."},
        {"action.save-as.description", "Spara till PDF format"},
        {"action.save-as.mnemonic", new Integer(KeyEvent.VK_A)},

        {"action.export-to-excel.name", "Exportera till Excel..."},
        {"action.export-to-excel.description", "Spara till MS-Excel format"},
        {"action.export-to-excel.mnemonic", new Integer(KeyEvent.VK_E)},

        {"action.export-to-html.name", "Exportera till html..."},
        {"action.export-to-html.description", "Spara till HTML format"},
        {"action.export-to-html.mnemonic", new Integer(KeyEvent.VK_H)},

        {"action.export-to-csv.name", "Exportera till CSV..."},
        {"action.export-to-csv.description", "Spara till CSV format"},
        {"action.export-to-csv.mnemonic", new Integer(KeyEvent.VK_C)},

        {"action.export-to-plaintext.name", "Spara som text file..."},
        {"action.export-to-plaintext.description", "Spara till vanlig text fil"},
        {"action.export-to-plaintext.mnemonic", new Integer(KeyEvent.VK_T)},

        {"action.page-setup.name", "Sida inställningar"},
        {"action.page-setup.description", "Sida inställningar"},
        {"action.page-setup.mnemonic", new Integer(KeyEvent.VK_G)},

        {"action.print.name", "Skriva ut..."},
        {"action.print.description", "Skriva ut rapporten"},
        {"action.print.mnemonic", new Integer(KeyEvent.VK_P)},

        {"action.close.name", "Stänga"},
        {"action.close.description", "Stänga förhandsgransknings-fönster"},
        {"action.close.mnemonic", new Integer(KeyEvent.VK_C)},

        {"action.gotopage.name", "Gå till sida ..."},
        {"action.gotopage.description", "Se en sida direkt"},
        {"action.gotopage.mnemonic", new Integer(KeyEvent.VK_G)},

        {"dialog.gotopage.message", "Ange en sida nummer"},
        {"dialog.gotopage.title", "Gå till sida"},

        {"action.about.name", "Om..."},
        {"action.about.description", "Information om applikationen"},
        {"action.about.mnemonic", new Integer(KeyEvent.VK_A)},

        {"action.firstpage.name", "Hem"},
        {"action.firstpage.description", "Bläddra till den första sidan"},

        {"action.back.name", "Bläddra bakåt"},
        {"action.back.description", "Bläddra till den föregående sidan"},

        {"action.forward.name", "Bläddra framåt"},
        {"action.forward.description", "Bläddra till den nästa sidan"},

        {"action.lastpage.name", "Sista sida"},
        {"action.lastpage.description", "Bläddra till den sista sidan"},

        {"action.zoomIn.name", "Zooma in"},
        {"action.zoomIn.description", "Förstärka zoomen"},

        {"action.zoomOut.name", "Zooma ut"},
        {"action.zoomOut.description", "Minska zoomen"},

        // preview frame...
        {"preview-frame.title", "Förhandsgranska"},

        // menu labels...
        {"menu.file.name", "Fil"},
        {"menu.file.mnemonic", new Character('F')},

        {"menu.navigation.name", "Navigation"},
        {"menu.navigation.mnemonic", new Character('N')},

        {"menu.zoom.name", "Zooma"},
        {"menu.zoom.mnemonic", new Character('Z')},

        {"menu.help.name", "Hjälp"},
        {"menu.help.mnemonic", new Character('H')},

        {"file.save.pdfdescription", "PDF dokument"},
        {"statusline.pages", "Sida {0} av {1}"},
        {"statusline.error", "Reportgeneration skapade ett fel: {0}"},
        {"statusline.repaginate", "Beräkna sida-brytning, var snäll och vänta."},
        {"error.processingfailed.title", "Report generation misslyckades"},
        {"error.processingfailed.message", "Fel när rapporten skapades: {0}"},
        {"error.savefailed.message", "Fel inträffade under PDF sparning: {0}"},
        {"error.savefailed.title", "Fel under sparningen"},
        {"error.printfailed.message", "Fel när rapporten skapades: {0}"},
        {"error.printfailed.title", "Fel under utskrivningen"},
        {"error.validationfailed.message", "Fel under valideringen av angiven information."},
        {"error.validationfailed.title", "Fel under valideringen"},

        {"tabletarget.page", "Sida {0}"},

        {"pdfsavedialog.dialogtitle", "Sparning av rapporten till en PDF-fil ..."},
        {"pdfsavedialog.filename", "Filnamn"},
        {"pdfsavedialog.author", "Författaren"},
        {"pdfsavedialog.title", "Titeln"},
        {"pdfsavedialog.selectFile", "Välja filen"},
        {"pdfsavedialog.security", "Säkerhets inställningar och kodningen"},
        {"pdfsavedialog.encoding", "Kodningen"},

        {"pdfsavedialog.securityNone", "Inga säkerhet"},
        {"pdfsavedialog.security40bit", "Kryptera med 40 bit nycklar"},
        {"pdfsavedialog.security128bit", "Kryptera med 128 bit nycklar"},
        {"pdfsavedialog.userpassword", "Använd lösenord"},
        {"pdfsavedialog.userpasswordconfirm", "Konfirmera"},
        {"pdfsavedialog.userpasswordNoMatch", "Lösenorden är inte likadan."},
        {"pdfsavedialog.ownerpassword", "Ägarens lösenord"},
        {"pdfsavedialog.ownerpasswordconfirm", "Konfirmera"},
        {"pdfsavedialog.ownerpasswordNoMatch", "Ägarens lösenord är inte likadan."},
        {"pdfsavedialog.ownerpasswordEmpty", "Ägarens lösenord är tom. Användare kan "
      + "ändra på säkerheten. Fortsätta?"},

        {"pdfsavedialog.warningTitle", "Varning"},
        {"pdfsavedialog.errorTitle", "Fel"},
        {"pdfsavedialog.targetIsEmpty", "Ange filnamnet till pdf filen."},
        {"pdfsavedialog.targetIsNoFile", "Mål filen är inte en vanlig fil."},
        {"pdfsavedialog.targetIsNotWritable", "Den valde filen är skrivskydad."},
        {"pdfsavedialog.targetOverwriteConfirmation",
         "Filen ''{0}'' Finns. Skriva över den?"},
        {"pdfsavedialog.targetOverwriteTitle", "Skriva över filen?"},


        {"pdfsavedialog.allowCopy", "Tillåta kopiering"},
        {"pdfsavedialog.allowPrinting", "Tillåta skrivning"},
        {"pdfsavedialog.allowDegradedPrinting", "Tillåta försämrade skrivning"},
        {"pdfsavedialog.allowScreenreader", "Tillåta skärmläsare"},
        {"pdfsavedialog.allowAssembly", "Tillåta återskapelse"},
        {"pdfsavedialog.allowModifyContents", "Tillåta modifiering av innehållet"},
        {"pdfsavedialog.allowModifyAnnotations", "Tillåta modifiering av anteckningar"},
        {"pdfsavedialog.allowFillIn", "Tillåta ändring av formulär data"},

        {"pdfsavedialog.option.noprinting", "Får ej skrivas ut"},
        {"pdfsavedialog.option.degradedprinting", "Låg kvalitet utskrifter"},
        {"pdfsavedialog.option.fullprinting", "Skrivning tillåten"},

        {"pdfsavedialog.cancel", "Avbryt"},
        {"pdfsavedialog.confirm", "Konfirmera"},

        {"excelexportdialog.dialogtitle", "Exportera rapporten till en Excel-fil ..."},
        {"excelexportdialog.filename", "Filnamn"},
        {"excelexportdialog.author", "Författaren"},
        {"excelexportdialog.title", "Titeln"},
        {"excelexportdialog.selectFile", "Välja filen"},

        {"excelexportdialog.warningTitle", "Varning"},
        {"excelexportdialog.errorTitle", "Fel"},
        {"excelexportdialog.targetIsEmpty", "Ange filnamnet till Excel filen."},
        {"excelexportdialog.targetIsNoFile", "Mål filen är inte en vanlig fil."},
        {"excelexportdialog.targetIsNotWritable", "Den valde filen är skrivskydad."},
        {"excelexportdialog.targetOverwriteConfirmation",
         "Filen ''{0}'' Finns. Skriva över den?"},
        {"excelexportdialog.targetOverwriteTitle", "Skriva över filen?"},

        {"excelexportdialog.cancel", "Avbryt"},
        {"excelexportdialog.confirm", "Konfirmera"},
        {"excelexportdialog.strict-layout", "Använda stränga tabell format reglar för exporten."},

        {"htmlexportdialog.dialogtitle", "Exportera rapporten till en Html-Fil ..."},

        {"htmlexportdialog.filename", "Filnamn"},
        {"htmlexportdialog.datafilename", "Data katalog"},
        {"htmlexportdialog.copy-external-references", "Kopiera externa referenser"},

        {"htmlexportdialog.author", "Författaren"},
        {"htmlexportdialog.title", "Titeln"},
        {"htmlexportdialog.encoding", "Kodningen"},
        {"htmlexportdialog.selectZipFile", "Välja filen"},
        {"htmlexportdialog.selectStreamFile", "Välja filen"},
        {"htmlexportdialog.selectDirFile", "Välja filen"},

        {"htmlexportdialog.strict-layout", "Använda stränga tabell format reglar för exporten."},
        {"htmlexportdialog.generate-xhtml", "Generera XHTML 1.0 output"},
        {"htmlexportdialog.generate-html4", "Generera HTML 4.0 output"},

        {"htmlexportdialog.warningTitle", "Varning"},
        {"htmlexportdialog.errorTitle", "Fel"},
        {"htmlexportdialog.targetIsEmpty", "Ange filnamnet till Html filen."},
        {"htmlexportdialog.targetIsNoFile", "Mål filen är inte en vanlig fil."},
        {"htmlexportdialog.targetIsNotWritable", "Den valde filen är skrivskydad."},
        {"htmlexportdialog.targetOverwriteConfirmation",
         "Filen ''{0}'' Finns. Skriva över den?"},
        {"htmlexportdialog.targetOverwriteTitle", "Skriva över filen?"},

        {"htmlexportdialog.cancel", "Avbryt"},
        {"htmlexportdialog.confirm", "Konfirmera"},
        {"htmlexportdialog.targetPathIsAbsolute",
         "Mål path är en absolut katalog.\nAnge katalogen i en ZIP fil."},
        {"htmlexportdialog.targetDataDirIsNoDirectory",
         "Angiven katalog är ogiltig."},
        {"htmlexportdialog.targetCreateDataDirConfirmation",
         "Angiven katalog finns inte.\n"
      + "Ska alla underkatalog skapas?"},
        {"htmlexportdialog.targetCreateDataDirTitle", "Skapa katalogen?"},

        {"csvexportdialog.dialogtitle", "Exportera rapporten till en CSV fil ..."},
        {"csvexportdialog.filename", "Filnamn"},
        {"csvexportdialog.encoding", "Kodningen"},
        {"csvexportdialog.separatorchar", "Avskiljaren"},
        {"csvexportdialog.selectFile", "Välja filen"},

        {"csvexportdialog.warningTitle", "Varning"},
        {"csvexportdialog.errorTitle", "Fel"},
        {"csvexportdialog.targetIsEmpty", "Ange filnamnet till en CSV fil."},
        {"csvexportdialog.targetIsNoFile", "Mål filen är inte en vanlig fil."},
        {"csvexportdialog.targetIsNotWritable", "Den valde filen är skrivskydad."},
        {"csvexportdialog.targetOverwriteConfirmation",
         "Filen ''{0}'' Finns. Skriva över den?"},
        {"csvexportdialog.targetOverwriteTitle", "Skriva över filen?"},

        {"csvexportdialog.cancel", "Avbryt"},
        {"csvexportdialog.confirm", "Konfirmera"},

        {"csvexportdialog.separator.tab", "Tabulator"},
        {"csvexportdialog.separator.colon", "Kommatecken (,)"},
        {"csvexportdialog.separator.semicolon", "Semikolon (;)"},
        {"csvexportdialog.separator.other", "Andra"},

        {"csvexportdialog.exporttype", "Välje export motor"},
        {"csvexportdialog.export.data", "Exportera DataRow (Rå data)"},
        {"csvexportdialog.export.printed_elements", "Utskriven element  (Formaterad data)"},
        {"csvexportdialog.strict-layout", "Använda stränga tabell format reglar för exporten."},


        {"plain-text-exportdialog.dialogtitle", "Exportera rapporten till en vanlig text fil ..."},
        {"plain-text-exportdialog.filename", "Filnamn"},
        {"plain-text-exportdialog.encoding", "Kodningen"},
        {"plain-text-exportdialog.printer", "Skrivare typ"},
        {"plain-text-exportdialog.printer.plain", "Vanlig text utskrift"},
        {"plain-text-exportdialog.printer.epson", "Epson ESC/P kompatibel"},
        {"plain-text-exportdialog.printer.ibm", "IBM kompatibel"},
        {"plain-text-exportdialog.selectFile", "Välja filen"},

        {"plain-text-exportdialog.warningTitle", "Varning"},
        {"plain-text-exportdialog.errorTitle", "Fel"},
        {"plain-text-exportdialog.targetIsEmpty",
         "Ange filnamnet till CSV filen."},
        {"plain-text-exportdialog.targetIsNoFile", "Mål filen är inte en vanlig fil."},
        {"plain-text-exportdialog.targetIsNotWritable", "Den valde filen är skrivskydad."},
        {"plain-text-exportdialog.targetOverwriteConfirmation",
         "Filen ''{0}'' Finns. Skriva över den?"},
        {"plain-text-exportdialog.targetOverwriteTitle", "Skriva över filen?"},

        {"plain-text-exportdialog.cancel", "Avbryt"},
        {"plain-text-exportdialog.confirm", "Konfirmera"},

        {"plain-text-exportdialog.chars-per-inch", "cpi (Antal tecken i en tum)"},
        {"plain-text-exportdialog.lines-per-inch", "lpi (Rader i en tum)"},
        {"plain-text-exportdialog.font-settings", "Teckensnitt inställningar"},

        {"convertdialog.targetIsEmpty", "Mål filen angavs inte."},
        {"convertdialog.errorTitle", "Fel"},
        {"convertdialog.targetIsNoFile", "Mål filen är inte en vanlig fil."},
        {"convertdialog.targetIsNotWritable", "Den valde filen är skrivskydad."},
        {"convertdialog.targetOverwriteConfirmation",
         "Filen ''{0}'' Finns. Skriva över den?"},
        {"convertdialog.targetOverwriteTitle", "Skriva över filen?"},
        {"convertdialog.targetFile", "Mål fil"},
        {"convertdialog.sourceIsEmpty", "Källa filen angavs inte"},
        {"convertdialog.sourceIsNoFile", "Källa filen är inte en vanlig fil."},
        {"convertdialog.sourceIsNotReadable", "Källa filen är skrivskydad."},
        {"convertdialog.sourceFile", "Källa fil"},

        {"convertdialog.action.selectTarget.name", "Välja"},
        {"convertdialog.action.selectTarget.description", "Välja mål filen."},
        {"convertdialog.action.selectSource.name", "Select"},
        {"convertdialog.action.selectSource.description", "Välja källa filen."},
        {"convertdialog.action.convert.name", "Konvertera"},
        {"convertdialog.action.convert.description", "Konvertera källa filerna."},

        {"convertdialog.title", "Rapport konverterare"},
      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{"sv"});
  }

}

