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

        {"action.page-setup.name", "Sida inst�llningar"},
        {"action.page-setup.description", "Sida inst�llningar"},
        {"action.page-setup.mnemonic", new Integer(KeyEvent.VK_G)},

        {"action.print.name", "Skriva ut..."},
        {"action.print.description", "Skriva ut rapporten"},
        {"action.print.mnemonic", new Integer(KeyEvent.VK_P)},

        {"action.close.name", "St�nga"},
        {"action.close.description", "St�nga f�rhandsgransknings-f�nster"},
        {"action.close.mnemonic", new Integer(KeyEvent.VK_C)},

        {"action.gotopage.name", "G� till sida ..."},
        {"action.gotopage.description", "Se en sida direkt"},
        {"action.gotopage.mnemonic", new Integer(KeyEvent.VK_G)},

        {"dialog.gotopage.message", "Ange en sida nummer"},
        {"dialog.gotopage.title", "G� till sida"},

        {"action.about.name", "Om..."},
        {"action.about.description", "Information om applikationen"},
        {"action.about.mnemonic", new Integer(KeyEvent.VK_A)},

        {"action.firstpage.name", "Hem"},
        {"action.firstpage.description", "Bl�ddra till den f�rsta sidan"},

        {"action.back.name", "Bl�ddra bak�t"},
        {"action.back.description", "Bl�ddra till den f�reg�ende sidan"},

        {"action.forward.name", "Bl�ddra fram�t"},
        {"action.forward.description", "Bl�ddra till den n�sta sidan"},

        {"action.lastpage.name", "Sista sida"},
        {"action.lastpage.description", "Bl�ddra till den sista sidan"},

        {"action.zoomIn.name", "Zooma in"},
        {"action.zoomIn.description", "F�rst�rka zoomen"},

        {"action.zoomOut.name", "Zooma ut"},
        {"action.zoomOut.description", "Minska zoomen"},

        // preview frame...
        {"preview-frame.title", "F�rhandsgranska"},

        // menu labels...
        {"menu.file.name", "Fil"},
        {"menu.file.mnemonic", new Character('F')},

        {"menu.navigation.name", "Navigation"},
        {"menu.navigation.mnemonic", new Character('N')},

        {"menu.zoom.name", "Zooma"},
        {"menu.zoom.mnemonic", new Character('Z')},

        {"menu.help.name", "Hj�lp"},
        {"menu.help.mnemonic", new Character('H')},

        {"file.save.pdfdescription", "PDF dokument"},
        {"statusline.pages", "Sida {0} av {1}"},
        {"statusline.error", "Reportgeneration skapade ett fel: {0}"},
        {"statusline.repaginate", "Ber�kna sida-brytning, var sn�ll och v�nta."},
        {"error.processingfailed.title", "Report generation misslyckades"},
        {"error.processingfailed.message", "Fel n�r rapporten skapades: {0}"},
        {"error.savefailed.message", "Fel intr�ffade under PDF sparning: {0}"},
        {"error.savefailed.title", "Fel under sparningen"},
        {"error.printfailed.message", "Fel n�r rapporten skapades: {0}"},
        {"error.printfailed.title", "Fel under utskrivningen"},
        {"error.validationfailed.message", "Fel under valideringen av angiven information."},
        {"error.validationfailed.title", "Fel under valideringen"},

        {"tabletarget.page", "Sida {0}"},

        {"pdfsavedialog.dialogtitle", "Sparning av rapporten till en PDF-fil ..."},
        {"pdfsavedialog.filename", "Filnamn"},
        {"pdfsavedialog.author", "F�rfattaren"},
        {"pdfsavedialog.title", "Titeln"},
        {"pdfsavedialog.selectFile", "V�lja filen"},
        {"pdfsavedialog.security", "S�kerhets inst�llningar och kodningen"},
        {"pdfsavedialog.encoding", "Kodningen"},

        {"pdfsavedialog.securityNone", "Inga s�kerhet"},
        {"pdfsavedialog.security40bit", "Kryptera med 40 bit nycklar"},
        {"pdfsavedialog.security128bit", "Kryptera med 128 bit nycklar"},
        {"pdfsavedialog.userpassword", "Anv�nd l�senord"},
        {"pdfsavedialog.userpasswordconfirm", "Konfirmera"},
        {"pdfsavedialog.userpasswordNoMatch", "L�senorden �r inte likadan."},
        {"pdfsavedialog.ownerpassword", "�garens l�senord"},
        {"pdfsavedialog.ownerpasswordconfirm", "Konfirmera"},
        {"pdfsavedialog.ownerpasswordNoMatch", "�garens l�senord �r inte likadan."},
        {"pdfsavedialog.ownerpasswordEmpty", "�garens l�senord �r tom. Anv�ndare kan "
      + "�ndra p� s�kerheten. Forts�tta?"},

        {"pdfsavedialog.warningTitle", "Varning"},
        {"pdfsavedialog.errorTitle", "Fel"},
        {"pdfsavedialog.targetIsEmpty", "Ange filnamnet till pdf filen."},
        {"pdfsavedialog.targetIsNoFile", "M�l filen �r inte en vanlig fil."},
        {"pdfsavedialog.targetIsNotWritable", "Den valde filen �r skrivskydad."},
        {"pdfsavedialog.targetOverwriteConfirmation",
         "Filen ''{0}'' Finns. Skriva �ver den?"},
        {"pdfsavedialog.targetOverwriteTitle", "Skriva �ver filen?"},


        {"pdfsavedialog.allowCopy", "Till�ta kopiering"},
        {"pdfsavedialog.allowPrinting", "Till�ta skrivning"},
        {"pdfsavedialog.allowDegradedPrinting", "Till�ta f�rs�mrade skrivning"},
        {"pdfsavedialog.allowScreenreader", "Till�ta sk�rml�sare"},
        {"pdfsavedialog.allowAssembly", "Till�ta �terskapelse"},
        {"pdfsavedialog.allowModifyContents", "Till�ta modifiering av inneh�llet"},
        {"pdfsavedialog.allowModifyAnnotations", "Till�ta modifiering av anteckningar"},
        {"pdfsavedialog.allowFillIn", "Till�ta �ndring av formul�r data"},

        {"pdfsavedialog.option.noprinting", "F�r ej skrivas ut"},
        {"pdfsavedialog.option.degradedprinting", "L�g kvalitet utskrifter"},
        {"pdfsavedialog.option.fullprinting", "Skrivning till�ten"},

        {"pdfsavedialog.cancel", "Avbryt"},
        {"pdfsavedialog.confirm", "Konfirmera"},

        {"excelexportdialog.dialogtitle", "Exportera rapporten till en Excel-fil ..."},
        {"excelexportdialog.filename", "Filnamn"},
        {"excelexportdialog.author", "F�rfattaren"},
        {"excelexportdialog.title", "Titeln"},
        {"excelexportdialog.selectFile", "V�lja filen"},

        {"excelexportdialog.warningTitle", "Varning"},
        {"excelexportdialog.errorTitle", "Fel"},
        {"excelexportdialog.targetIsEmpty", "Ange filnamnet till Excel filen."},
        {"excelexportdialog.targetIsNoFile", "M�l filen �r inte en vanlig fil."},
        {"excelexportdialog.targetIsNotWritable", "Den valde filen �r skrivskydad."},
        {"excelexportdialog.targetOverwriteConfirmation",
         "Filen ''{0}'' Finns. Skriva �ver den?"},
        {"excelexportdialog.targetOverwriteTitle", "Skriva �ver filen?"},

        {"excelexportdialog.cancel", "Avbryt"},
        {"excelexportdialog.confirm", "Konfirmera"},
        {"excelexportdialog.strict-layout", "Anv�nda str�nga tabell format reglar f�r exporten."},

        {"htmlexportdialog.dialogtitle", "Exportera rapporten till en Html-Fil ..."},

        {"htmlexportdialog.filename", "Filnamn"},
        {"htmlexportdialog.datafilename", "Data katalog"},
        {"htmlexportdialog.copy-external-references", "Kopiera externa referenser"},

        {"htmlexportdialog.author", "F�rfattaren"},
        {"htmlexportdialog.title", "Titeln"},
        {"htmlexportdialog.encoding", "Kodningen"},
        {"htmlexportdialog.selectZipFile", "V�lja filen"},
        {"htmlexportdialog.selectStreamFile", "V�lja filen"},
        {"htmlexportdialog.selectDirFile", "V�lja filen"},

        {"htmlexportdialog.strict-layout", "Anv�nda str�nga tabell format reglar f�r exporten."},
        {"htmlexportdialog.generate-xhtml", "Generera XHTML 1.0 output"},
        {"htmlexportdialog.generate-html4", "Generera HTML 4.0 output"},

        {"htmlexportdialog.warningTitle", "Varning"},
        {"htmlexportdialog.errorTitle", "Fel"},
        {"htmlexportdialog.targetIsEmpty", "Ange filnamnet till Html filen."},
        {"htmlexportdialog.targetIsNoFile", "M�l filen �r inte en vanlig fil."},
        {"htmlexportdialog.targetIsNotWritable", "Den valde filen �r skrivskydad."},
        {"htmlexportdialog.targetOverwriteConfirmation",
         "Filen ''{0}'' Finns. Skriva �ver den?"},
        {"htmlexportdialog.targetOverwriteTitle", "Skriva �ver filen?"},

        {"htmlexportdialog.cancel", "Avbryt"},
        {"htmlexportdialog.confirm", "Konfirmera"},
        {"htmlexportdialog.targetPathIsAbsolute",
         "M�l path �r en absolut katalog.\nAnge katalogen i en ZIP fil."},
        {"htmlexportdialog.targetDataDirIsNoDirectory",
         "Angiven katalog �r ogiltig."},
        {"htmlexportdialog.targetCreateDataDirConfirmation",
         "Angiven katalog finns inte.\n"
      + "Ska alla underkatalog skapas?"},
        {"htmlexportdialog.targetCreateDataDirTitle", "Skapa katalogen?"},

        {"csvexportdialog.dialogtitle", "Exportera rapporten till en CSV fil ..."},
        {"csvexportdialog.filename", "Filnamn"},
        {"csvexportdialog.encoding", "Kodningen"},
        {"csvexportdialog.separatorchar", "Avskiljaren"},
        {"csvexportdialog.selectFile", "V�lja filen"},

        {"csvexportdialog.warningTitle", "Varning"},
        {"csvexportdialog.errorTitle", "Fel"},
        {"csvexportdialog.targetIsEmpty", "Ange filnamnet till en CSV fil."},
        {"csvexportdialog.targetIsNoFile", "M�l filen �r inte en vanlig fil."},
        {"csvexportdialog.targetIsNotWritable", "Den valde filen �r skrivskydad."},
        {"csvexportdialog.targetOverwriteConfirmation",
         "Filen ''{0}'' Finns. Skriva �ver den?"},
        {"csvexportdialog.targetOverwriteTitle", "Skriva �ver filen?"},

        {"csvexportdialog.cancel", "Avbryt"},
        {"csvexportdialog.confirm", "Konfirmera"},

        {"csvexportdialog.separator.tab", "Tabulator"},
        {"csvexportdialog.separator.colon", "Kommatecken (,)"},
        {"csvexportdialog.separator.semicolon", "Semikolon (;)"},
        {"csvexportdialog.separator.other", "Andra"},

        {"csvexportdialog.exporttype", "V�lje export motor"},
        {"csvexportdialog.export.data", "Exportera DataRow (R� data)"},
        {"csvexportdialog.export.printed_elements", "Utskriven element  (Formaterad data)"},
        {"csvexportdialog.strict-layout", "Anv�nda str�nga tabell format reglar f�r exporten."},


        {"plain-text-exportdialog.dialogtitle", "Exportera rapporten till en vanlig text fil ..."},
        {"plain-text-exportdialog.filename", "Filnamn"},
        {"plain-text-exportdialog.encoding", "Kodningen"},
        {"plain-text-exportdialog.printer", "Skrivare typ"},
        {"plain-text-exportdialog.printer.plain", "Vanlig text utskrift"},
        {"plain-text-exportdialog.printer.epson", "Epson ESC/P kompatibel"},
        {"plain-text-exportdialog.printer.ibm", "IBM kompatibel"},
        {"plain-text-exportdialog.selectFile", "V�lja filen"},

        {"plain-text-exportdialog.warningTitle", "Varning"},
        {"plain-text-exportdialog.errorTitle", "Fel"},
        {"plain-text-exportdialog.targetIsEmpty",
         "Ange filnamnet till CSV filen."},
        {"plain-text-exportdialog.targetIsNoFile", "M�l filen �r inte en vanlig fil."},
        {"plain-text-exportdialog.targetIsNotWritable", "Den valde filen �r skrivskydad."},
        {"plain-text-exportdialog.targetOverwriteConfirmation",
         "Filen ''{0}'' Finns. Skriva �ver den?"},
        {"plain-text-exportdialog.targetOverwriteTitle", "Skriva �ver filen?"},

        {"plain-text-exportdialog.cancel", "Avbryt"},
        {"plain-text-exportdialog.confirm", "Konfirmera"},

        {"plain-text-exportdialog.chars-per-inch", "cpi (Antal tecken i en tum)"},
        {"plain-text-exportdialog.lines-per-inch", "lpi (Rader i en tum)"},
        {"plain-text-exportdialog.font-settings", "Teckensnitt inst�llningar"},

        {"convertdialog.targetIsEmpty", "M�l filen angavs inte."},
        {"convertdialog.errorTitle", "Fel"},
        {"convertdialog.targetIsNoFile", "M�l filen �r inte en vanlig fil."},
        {"convertdialog.targetIsNotWritable", "Den valde filen �r skrivskydad."},
        {"convertdialog.targetOverwriteConfirmation",
         "Filen ''{0}'' Finns. Skriva �ver den?"},
        {"convertdialog.targetOverwriteTitle", "Skriva �ver filen?"},
        {"convertdialog.targetFile", "M�l fil"},
        {"convertdialog.sourceIsEmpty", "K�lla filen angavs inte"},
        {"convertdialog.sourceIsNoFile", "K�lla filen �r inte en vanlig fil."},
        {"convertdialog.sourceIsNotReadable", "K�lla filen �r skrivskydad."},
        {"convertdialog.sourceFile", "K�lla fil"},

        {"convertdialog.action.selectTarget.name", "V�lja"},
        {"convertdialog.action.selectTarget.description", "V�lja m�l filen."},
        {"convertdialog.action.selectSource.name", "Select"},
        {"convertdialog.action.selectSource.description", "V�lja k�lla filen."},
        {"convertdialog.action.convert.name", "Konvertera"},
        {"convertdialog.action.convert.description", "Konvertera k�lla filerna."},

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

