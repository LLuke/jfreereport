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
 * JFreeReportResources_hu.java
 * ----------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Demeter F. Tam�s;
 * Contributor(s):   -;
 *
 *
 * $Id: JFreeReportResources_hu.java,v 1.1 2003/05/16 13:24:42 taqua Exp $
 *
 *
 */
package com.jrefinery.report.resources;

import java.awt.event.KeyEvent;

/**
 * Hungarian Language Resources.
 *
 * @author Demeter F. Tam�s
 */
public class JFreeReportResources_hu extends JFreeReportResources
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
        {"action.save-as.name", "Ment�s mint PDF..."},
        {"action.save-as.description", "Ment�s PDF form�tumban"},
        {"action.save-as.mnemonic", new Integer(KeyEvent.VK_M)},

        {"action.export-to-excel.name", "Export�ld Excel-k�nt..."},
        {"action.export-to-excel.description", "Ment�s MS-Excel form�tumban"},
        {"action.export-to-excel.mnemonic", new Integer(KeyEvent.VK_E)},

        {"action.export-to-html.name", "Export�ld html-k�nt..."},
        {"action.export-to-html.description", "Ment�s HTML form�tumban"},
        {"action.export-to-html.mnemonic", new Integer(KeyEvent.VK_H)},

        {"action.export-to-csv.name", "Export�ld CSV-k�nt..."},
        {"action.export-to-csv.description", "Ment�s CSV form�tumban"},
        {"action.export-to-csv.mnemonic", new Integer(KeyEvent.VK_C)},

        {"action.export-to-plaintext.name", "Ment�s mint text f�jl..."},
        {"action.export-to-plaintext.description", "Ment�s egyszer� sz�veges form�tumban"},
        {"action.export-to-plaintext.mnemonic", new Integer(KeyEvent.VK_T)},

        {"action.page-setup.name", "Oldal be�ll�t�sa"},
        {"action.page-setup.description", "Oldal be�ll�t�sa"},
        {"action.page-setup.mnemonic", new Integer(KeyEvent.VK_O)},

        {"action.print.name", "Nyomtat�s..."},
        {"action.print.description", "Lista nyomtat�sa"},
        {"action.print.mnemonic", new Integer(KeyEvent.VK_N)},

        {"action.close.name", "Bez�r�s"},
        {"action.close.description", "Az el�n�zet bez�r�sa"},
        {"action.close.mnemonic", new Integer(KeyEvent.VK_B)},

        {"action.gotopage.name", "Ugorj adott oldalra ..."},
        {"action.gotopage.description", "Egy oldalra k�zvetlen ugr�s"},
        {"action.gotopage.mnemonic", new Integer(KeyEvent.VK_G)},

        {"dialog.gotopage.message", "�rd be az oldalsz�mot"},
        {"dialog.gotopage.title", "Ugorj az oldalra"},

        {"action.about.name", "N�vjegy..."},
        {"action.about.description", "Inform�ci� az alkalmaz�sr�l"},
        {"action.about.mnemonic", new Integer(KeyEvent.VK_N)},

        {"action.firstpage.name", "Legels�"},
        {"action.firstpage.mnemonic", new Integer(KeyEvent.VK_HOME)},
        {"action.firstpage.description", "Ugorj az els� oldalra"},

        {"action.back.name", "Vissza"},
        {"action.back.description", "Lapozz az el�z� oldalra"},
        {"action.back.mnemonic", new Integer(KeyEvent.VK_PAGE_UP)},

        {"action.forward.name", "El�re"},
        {"action.forward.description", "Lapozz a k�vetkez� oldalra"},
        {"action.forward.mnemonic", new Integer(KeyEvent.VK_PAGE_DOWN)},

        {"action.lastpage.name", "Legutols�"},
        {"action.lastpage.description", "Ugorj az utols� oldalra"},
        {"action.lastpage.mnemonic", new Integer(KeyEvent.VK_END)},

        {"action.zoomIn.name", "Nagy�t�s"},
        {"action.zoomIn.description", "Nagy�t�s"},
        {"action.zoomIn.mnemonic", new Integer(KeyEvent.VK_PLUS)},

        {"action.zoomOut.name", "Kicsiny�t�s"},
        {"action.zoomOut.description", "Kicsiny�t�s"},
        {"action.zoomOut.mnemonic", new Integer(KeyEvent.VK_MINUS)},

        // preview frame...
        {"preview-frame.title", "Nyomtat�si el�n�zet"},

        // menu labels...
        {"menu.file.name", "F�jl"},
        {"menu.file.mnemonic", new Character('F')},

        {"menu.navigation.name", "Navig�ci�"},
        {"menu.navigation.mnemonic", new Character('N')},

        {"menu.zoom.name", "M�retez�s"},
        {"menu.zoom.mnemonic", new Character('M')},

        {"menu.help.name", "S�g�"},
        {"menu.help.mnemonic", new Character('S')},

        {"file.save.pdfdescription", "PDF dokumentumok"},
        {"statusline.pages", "Oldal: {0}/{1}"},
        {"statusline.error", "A lista gener�l�sakor hiba l�pett fel: {0}"},
        {"statusline.repaginate", "Sz�m�tom az oldalt�r�seket, k�rlek v�rj."},
        {"error.processingfailed.title", "A lista feldolgoz�sa nem siker�lt"},
        {"error.processingfailed.message", "A lista k�sz�t�se sor�n hiba t�rt�nt: {0}"},
        {"error.savefailed.message", "Hiba a PDF f�jl ment�sekor: {0}"},
        {"error.savefailed.title", "Hiba a ment�skor"},
        {"error.printfailed.message", "Hiba a nyomtat�s sor�n: {0}"},
        {"error.printfailed.title", "Hiba a nyomtat�skor"},
        {"error.validationfailed.message", "Hiba a az adatbevitel ellen�rz�sekor."},
        {"error.validationfailed.title", "Hiba az ellen�rz�skor"},

        {"tabletarget.page", "Oldal: {0}"},

        {"pdfsavedialog.dialogtitle", "Lista ment�se PDF-f�jlba ..."},
        {"pdfsavedialog.filename", "F�jln�v"},
        {"pdfsavedialog.author", "Szerz�"},
        {"pdfsavedialog.title", "C�m"},
        {"pdfsavedialog.selectFile", "V�lassz egy f�jlt"},
        {"pdfsavedialog.security", "Biztons�gi be�ll�t�sok �s titkos�t�s"},
        {"pdfsavedialog.encoding", "Karakterk�dol�s"},

        {"pdfsavedialog.securityNone", "Nincs titkos�t�s"},
        {"pdfsavedialog.security40bit", "Titkos�tsd 40 bites kulccsal"},
        {"pdfsavedialog.security128bit", "Titkos�tsd 128 bites kulccsal"},
        {"pdfsavedialog.userpassword", "Felhaszn�l�i jelsz�"},
        {"pdfsavedialog.userpasswordconfirm", "Meger�s�t�s"},
        {"pdfsavedialog.userpasswordNoMatch", "A jelsz� nem egyezik"},
        {"pdfsavedialog.ownerpassword", "Tulajdonos jelsz�"},
        {"pdfsavedialog.ownerpasswordconfirm", "Meger�s�t�s"},
        {"pdfsavedialog.ownerpasswordNoMatch", "A tulajdonosi jelsz� nem egyezik."},
        {"pdfsavedialog.ownerpasswordEmpty", "A tulajdonosi jelsz� �res. A felhaszn�l�k "
      + "megv�ltoztathatj�k a biztons�gi megszor�t�sokat. Folytathatom?"},

        {"pdfsavedialog.warningTitle", "Figyelmeztet�s"},
        {"pdfsavedialog.errorTitle", "Hiba"},
        {"pdfsavedialog.targetIsEmpty", "K�rlek nevezd el a pdf f�jlt."},
        {"pdfsavedialog.targetIsNoFile", "A kiv�lasztott c�l nem f�jl."},
        {"pdfsavedialog.targetIsNotWritable", "A kiv�lasztott f�jl nem �rhat�."},
        {"pdfsavedialog.targetOverwriteConfirmation",
         "A(z) ''{0}'' f�jl l�tezik. Fel�l�rhatom?"},
        {"pdfsavedialog.targetOverwriteTitle", "Fel�l�rhatom a f�jlt?"},


        {"pdfsavedialog.allowCopy", "M�sol�s enged�lyez�se"},
        {"pdfsavedialog.allowPrinting", "Nyomtat�s enged�lyez�se"},
        {"pdfsavedialog.allowDegradedPrinting", "Gyenge min�s�g� nyomtat�s enged�lyez�se"},
        {"pdfsavedialog.allowScreenreader", "K�polvas�k haszn�lat�nak enged�lyez�se"},
        {"pdfsavedialog.allowAssembly", "(�jra-)szerkesz�s enged�lyez�se"},
        {"pdfsavedialog.allowModifyContents", "Tartalom m�dos�t�s�nak enged�lyez�se"},
        {"pdfsavedialog.allowModifyAnnotations", "Megjegyz�sek m�dos�t�s�nak enged�lyez�se"},
        {"pdfsavedialog.allowFillIn", "Kit�lt�sek enged�lyez�se formanyomtatv�nyban"},

        {"pdfsavedialog.option.noprinting", "Nem nyomtathat�"},
        {"pdfsavedialog.option.degradedprinting", "Alacsony min�s�g� nyomtat�s"},
        {"pdfsavedialog.option.fullprinting", "Nyomtat�s enged�lyezve"},

        {"pdfsavedialog.cancel", "Megszak�t"},
        {"pdfsavedialog.confirm", "J�v�hagy"},

        {"excelexportdialog.dialogtitle", "A lista export�l�sa Excel f�jlba ..."},
        {"excelexportdialog.filename", "F�jln�v"},
        {"excelexportdialog.author", "Szerz�"},
        {"excelexportdialog.title", "C�m"},
        {"excelexportdialog.selectFile", "V�lassz egy f�jlt"},

        {"excelexportdialog.warningTitle", "Figyelmeztet�s"},
        {"excelexportdialog.errorTitle", "Hiba"},
        {"excelexportdialog.targetIsEmpty", "K�rlek nevezd el az Excel f�jlt."},
        {"excelexportdialog.targetIsNoFile", "A kiv�lasztott c�l nem f�jl."},
        {"excelexportdialog.targetIsNotWritable", "A kiv�lasztott f�jl nem �rhat�."},
        {"excelexportdialog.targetOverwriteConfirmation",
         "A(z) ''{0}'' f�jl l�tezik. Fel�l�rhatom?"},
        {"excelexportdialog.targetOverwriteTitle", "Fel�l�rhatom a f�jlt?"},

        {"excelexportdialog.cancel", "Megszak�t"},
        {"excelexportdialog.confirm", "J�v�hagy"},
        {"excelexportdialog.strict-layout", "Pontos t�blamegjelen�t�s az export�l�skor."},

        {"htmlexportdialog.dialogtitle", "A lista export�l�sa Html f�jlba ..."},

        {"htmlexportdialog.filename", "F�jln�v"},
        {"htmlexportdialog.datafilename", "Adatk�nyvt�r"},
        {"htmlexportdialog.copy-external-references", "K�ls� hivatkoz�sok m�sol�sa"},

        {"htmlexportdialog.author", "Szerz�"},
        {"htmlexportdialog.title", "C�m"},
        {"htmlexportdialog.encoding", "Karakterk�dol�s"},
        {"htmlexportdialog.selectZipFile", "V�lassz egy f�jlt"},
        {"htmlexportdialog.selectStreamFile", "V�lassz egy f�jlt"},
        {"htmlexportdialog.selectDirFile", "V�lassz egy f�jlt"},

        {"htmlexportdialog.strict-layout", "Pontos t�blamegjelen�t�s az export�l�skor."},
        {"htmlexportdialog.generate-xhtml", "XHTML 1.0 kimenet gener�l�sa"},
        {"htmlexportdialog.generate-html4", "HTML 4.0 kimenet gener�l�sa"},

        {"htmlexportdialog.warningTitle", "Figyelmeztet�s"},
        {"htmlexportdialog.errorTitle", "Hiba"},
        {"htmlexportdialog.targetIsEmpty", "K�rlek nevezd el a Html f�jlt."},
        {"htmlexportdialog.targetIsNoFile", "A kiv�lasztott c�l nem f�jl."},
        {"htmlexportdialog.targetIsNotWritable", "A kiv�lasztott f�jl nem �rhat�."},
        {"htmlexportdialog.targetOverwriteConfirmation",
         "A(z) ''{0}'' f�jl l�tezik. Fel�l�rhatom?"},
        {"htmlexportdialog.targetOverwriteTitle", "Fel�l�rhatom a f�jlt?"},

        {"htmlexportdialog.cancel", "Megszak�t"},
        {"htmlexportdialog.confirm", "J�v�hagy"},
        {"htmlexportdialog.targetPathIsAbsolute",
         "A megadott el�r�si �t egy abszol�t k�nyvt�rhivatkoz�s.\n"
      + "K�rlek adj meg egy adatk�nyvt�rat a ZIP f�jlon bel�l."},
        {"htmlexportdialog.targetDataDirIsNoDirectory",
         "A megadott adatk�nyvt�r nem �rv�nyes."},
        {"htmlexportdialog.targetCreateDataDirConfirmation",
         "A megadott adatk�nyvt�r nem l�tezik.\n"
      + "Hozzam l�tre a hi�nyz� alk�nyvt�rakat?"},
        {"htmlexportdialog.targetCreateDataDirTitle", "Hozzam l�tre az adatk�nyt�rat?"},

        {"csvexportdialog.dialogtitle", "A lista export�l�sa CSV f�jlba ..."},
        {"csvexportdialog.filename", "F�jln�v"},
        {"csvexportdialog.encoding", "Karakterk�dol�s"},
        {"csvexportdialog.separatorchar", "Elv�laszt� karakter"},
        {"csvexportdialog.selectFile", "V�lassz egy f�jlt"},

        {"csvexportdialog.warningTitle", "Figyelmeztet�s"},
        {"csvexportdialog.errorTitle", "Hiba"},
        {"csvexportdialog.targetIsEmpty", "K�rlek nevezd el a CSV f�jlt."},
        {"csvexportdialog.targetIsNoFile", "A kiv�lasztott c�l nem f�jl."},
        {"csvexportdialog.targetIsNotWritable", "A kiv�lasztott f�jl nem �rhat�."},
        {"csvexportdialog.targetOverwriteConfirmation",
         "A(z) ''{0}'' f�jl l�tezik. Fel�l�rhatom?"},
        {"csvexportdialog.targetOverwriteTitle", "Fel�l�rhatom a f�jlt?"},

        {"csvexportdialog.cancel", "Megszak�t"},
        {"csvexportdialog.confirm", "J�v�hagy"},

        {"csvexportdialog.separator.tab", "Tabul�tor"},
        {"csvexportdialog.separator.colon", "Vessz� (,)"},
        {"csvexportdialog.separator.semicolon", "Pontosvessz� (;)"},
        {"csvexportdialog.separator.other", "M�s"},

        {"csvexportdialog.exporttype", "Export�l�s m�dja"},
        {"csvexportdialog.export.data", "Adatsor export�l�sa (Nyers adat)"},
        {"csvexportdialog.export.printed_elements",
         "Nyomtatott elemek export�l�sa (Megjelen�tett adatok)"},
        {"csvexportdialog.strict-layout", "Pontos t�blamegjelen�t�s az export�l�skor."},


        {"plain-text-exportdialog.dialogtitle",
         "A lista export�l�sa egyszer� sz�veges f�jlba..."},
        {"plain-text-exportdialog.filename", "F�jln�v"},
        {"plain-text-exportdialog.encoding", "Karakterk�dol�s"},
        {"plain-text-exportdialog.printer", "Nyomtat� t�pusa"},
        {"plain-text-exportdialog.printer.plain", "Egyszer� sz�veges kimenet"},
        {"plain-text-exportdialog.printer.epson", "Epson ESC/P kompatibilis"},
        {"plain-text-exportdialog.printer.ibm", "IBM kompatibilis"},
        {"plain-text-exportdialog.selectFile", "V�lassz egy f�jlt"},

        {"plain-text-exportdialog.warningTitle", "Figyelmeztet�s"},
        {"plain-text-exportdialog.errorTitle", "Hiba"},
        {"plain-text-exportdialog.targetIsEmpty",
         "K�rlek nevezd el a CSV f�jlt."},
        {"plain-text-exportdialog.targetIsNoFile", "A kiv�lasztott c�l nem f�jl."},
        {"plain-text-exportdialog.targetIsNotWritable", "A kiv�lasztott f�jl nem �rhat�."},
        {"plain-text-exportdialog.targetOverwriteConfirmation",
         "A(z) ''{0}'' f�jl l�tezik. Fel�l�rhatom?"},
        {"plain-text-exportdialog.targetOverwriteTitle", "Fel�l�rhatom a f�jlt?"},

        {"plain-text-exportdialog.cancel", "Megszak�t"},
        {"plain-text-exportdialog.confirm", "J�v�hagy"},

        {"plain-text-exportdialog.chars-per-inch", "cpi (Karakter per inch)"},
        {"plain-text-exportdialog.lines-per-inch", "lpi (Sor per inch)"},
        {"plain-text-exportdialog.font-settings", "Bet�t�tpus be�ll�t�sok"},

        {"convertdialog.targetIsEmpty", "A c�l�llom�ny nincs megadva"},
        {"convertdialog.errorTitle", "Hiba"},
        {"convertdialog.targetIsNoFile", "A megadott c�l nem f�jl."},
        {"convertdialog.targetIsNotWritable", "A megadott f�jl nem �rhat�."},
        {"convertdialog.targetOverwriteConfirmation",
         "A(z) ''{0}'' f�jl l�tezik. Fel�l�rhatom?"},
        {"convertdialog.targetOverwriteTitle", "Fel�l�rhatom a f�jlt?"},
        {"convertdialog.targetFile", "C�l f�jl"},
        {"convertdialog.sourceIsEmpty", "A forr�s f�jl nincs megadva"},
        {"convertdialog.sourceIsNoFile", "A megadott forr�s nem k�z�ns�ges f�jl."},
        {"convertdialog.sourceIsNotReadable", "A forr�sf�jl nem olvashat�."},
        {"convertdialog.sourceFile", "Forr�s f�jl"},

        {"convertdialog.action.selectTarget.name", "V�lassz"},
        {"convertdialog.action.selectTarget.description", "V�lassz c�l f�jlt."},
        {"convertdialog.action.selectSource.name", "V�lassz"},
        {"convertdialog.action.selectSource.description", "V�lassz forr�s f�jlt."},
        {"convertdialog.action.convert.name", "Konvert�l�s"},
        {"convertdialog.action.convert.description", "Konvert�ld a forr�s f�jlokat."},

        {"convertdialog.title", "Lista konvert�l�"},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(String[] args)
  {
    ResourceCompareTool.main(new String[]{"hu"});
  }

}
