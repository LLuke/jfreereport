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
 * Original Author:  Demeter F. Tamás;
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
 * @author Demeter F. Tamás
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
        {"action.save-as.name", "Mentés mint PDF..."},
        {"action.save-as.description", "Mentés PDF formátumban"},
        {"action.save-as.mnemonic", new Integer(KeyEvent.VK_M)},

        {"action.export-to-excel.name", "Exportáld Excel-ként..."},
        {"action.export-to-excel.description", "Mentés MS-Excel formátumban"},
        {"action.export-to-excel.mnemonic", new Integer(KeyEvent.VK_E)},

        {"action.export-to-html.name", "Exportáld html-ként..."},
        {"action.export-to-html.description", "Mentés HTML formátumban"},
        {"action.export-to-html.mnemonic", new Integer(KeyEvent.VK_H)},

        {"action.export-to-csv.name", "Exportáld CSV-ként..."},
        {"action.export-to-csv.description", "Mentés CSV formátumban"},
        {"action.export-to-csv.mnemonic", new Integer(KeyEvent.VK_C)},

        {"action.export-to-plaintext.name", "Mentés mint text fájl..."},
        {"action.export-to-plaintext.description", "Mentés egyszerû szöveges formátumban"},
        {"action.export-to-plaintext.mnemonic", new Integer(KeyEvent.VK_T)},

        {"action.page-setup.name", "Oldal beállítása"},
        {"action.page-setup.description", "Oldal beállítása"},
        {"action.page-setup.mnemonic", new Integer(KeyEvent.VK_O)},

        {"action.print.name", "Nyomtatás..."},
        {"action.print.description", "Lista nyomtatása"},
        {"action.print.mnemonic", new Integer(KeyEvent.VK_N)},

        {"action.close.name", "Bezárás"},
        {"action.close.description", "Az elõnézet bezárása"},
        {"action.close.mnemonic", new Integer(KeyEvent.VK_B)},

        {"action.gotopage.name", "Ugorj adott oldalra ..."},
        {"action.gotopage.description", "Egy oldalra közvetlen ugrás"},
        {"action.gotopage.mnemonic", new Integer(KeyEvent.VK_G)},

        {"dialog.gotopage.message", "Írd be az oldalszámot"},
        {"dialog.gotopage.title", "Ugorj az oldalra"},

        {"action.about.name", "Névjegy..."},
        {"action.about.description", "Információ az alkalmazásról"},
        {"action.about.mnemonic", new Integer(KeyEvent.VK_N)},

        {"action.firstpage.name", "Legelsõ"},
        {"action.firstpage.mnemonic", new Integer(KeyEvent.VK_HOME)},
        {"action.firstpage.description", "Ugorj az elsõ oldalra"},

        {"action.back.name", "Vissza"},
        {"action.back.description", "Lapozz az elõzõ oldalra"},
        {"action.back.mnemonic", new Integer(KeyEvent.VK_PAGE_UP)},

        {"action.forward.name", "Elõre"},
        {"action.forward.description", "Lapozz a következõ oldalra"},
        {"action.forward.mnemonic", new Integer(KeyEvent.VK_PAGE_DOWN)},

        {"action.lastpage.name", "Legutolsó"},
        {"action.lastpage.description", "Ugorj az utolsó oldalra"},
        {"action.lastpage.mnemonic", new Integer(KeyEvent.VK_END)},

        {"action.zoomIn.name", "Nagyítás"},
        {"action.zoomIn.description", "Nagyítás"},
        {"action.zoomIn.mnemonic", new Integer(KeyEvent.VK_PLUS)},

        {"action.zoomOut.name", "Kicsinyítés"},
        {"action.zoomOut.description", "Kicsinyítés"},
        {"action.zoomOut.mnemonic", new Integer(KeyEvent.VK_MINUS)},

        // preview frame...
        {"preview-frame.title", "Nyomtatási elõnézet"},

        // menu labels...
        {"menu.file.name", "Fájl"},
        {"menu.file.mnemonic", new Character('F')},

        {"menu.navigation.name", "Navigáció"},
        {"menu.navigation.mnemonic", new Character('N')},

        {"menu.zoom.name", "Méretezés"},
        {"menu.zoom.mnemonic", new Character('M')},

        {"menu.help.name", "Súgó"},
        {"menu.help.mnemonic", new Character('S')},

        {"file.save.pdfdescription", "PDF dokumentumok"},
        {"statusline.pages", "Oldal: {0}/{1}"},
        {"statusline.error", "A lista generálásakor hiba lépett fel: {0}"},
        {"statusline.repaginate", "Számítom az oldaltöréseket, kérlek várj."},
        {"error.processingfailed.title", "A lista feldolgozása nem sikerült"},
        {"error.processingfailed.message", "A lista készítése során hiba történt: {0}"},
        {"error.savefailed.message", "Hiba a PDF fájl mentésekor: {0}"},
        {"error.savefailed.title", "Hiba a mentéskor"},
        {"error.printfailed.message", "Hiba a nyomtatás során: {0}"},
        {"error.printfailed.title", "Hiba a nyomtatáskor"},
        {"error.validationfailed.message", "Hiba a az adatbevitel ellenõrzésekor."},
        {"error.validationfailed.title", "Hiba az ellenõrzéskor"},

        {"tabletarget.page", "Oldal: {0}"},

        {"pdfsavedialog.dialogtitle", "Lista mentése PDF-fájlba ..."},
        {"pdfsavedialog.filename", "Fájlnév"},
        {"pdfsavedialog.author", "Szerzõ"},
        {"pdfsavedialog.title", "Cím"},
        {"pdfsavedialog.selectFile", "Válassz egy fájlt"},
        {"pdfsavedialog.security", "Biztonsági beállítások és titkosítás"},
        {"pdfsavedialog.encoding", "Karakterkódolás"},

        {"pdfsavedialog.securityNone", "Nincs titkosítás"},
        {"pdfsavedialog.security40bit", "Titkosítsd 40 bites kulccsal"},
        {"pdfsavedialog.security128bit", "Titkosítsd 128 bites kulccsal"},
        {"pdfsavedialog.userpassword", "Felhasználói jelszó"},
        {"pdfsavedialog.userpasswordconfirm", "Megerõsítés"},
        {"pdfsavedialog.userpasswordNoMatch", "A jelszó nem egyezik"},
        {"pdfsavedialog.ownerpassword", "Tulajdonos jelszó"},
        {"pdfsavedialog.ownerpasswordconfirm", "Megerõsítés"},
        {"pdfsavedialog.ownerpasswordNoMatch", "A tulajdonosi jelszó nem egyezik."},
        {"pdfsavedialog.ownerpasswordEmpty", "A tulajdonosi jelszó üres. A felhasználók "
      + "megváltoztathatják a biztonsági megszorításokat. Folytathatom?"},

        {"pdfsavedialog.warningTitle", "Figyelmeztetés"},
        {"pdfsavedialog.errorTitle", "Hiba"},
        {"pdfsavedialog.targetIsEmpty", "Kérlek nevezd el a pdf fájlt."},
        {"pdfsavedialog.targetIsNoFile", "A kiválasztott cél nem fájl."},
        {"pdfsavedialog.targetIsNotWritable", "A kiválasztott fájl nem írható."},
        {"pdfsavedialog.targetOverwriteConfirmation",
         "A(z) ''{0}'' fájl létezik. Felülírhatom?"},
        {"pdfsavedialog.targetOverwriteTitle", "Felülírhatom a fájlt?"},


        {"pdfsavedialog.allowCopy", "Másolás engedélyezése"},
        {"pdfsavedialog.allowPrinting", "Nyomtatás engedélyezése"},
        {"pdfsavedialog.allowDegradedPrinting", "Gyenge minõségû nyomtatás engedélyezése"},
        {"pdfsavedialog.allowScreenreader", "Képolvasók használatának engedélyezése"},
        {"pdfsavedialog.allowAssembly", "(Újra-)szerkeszés engedélyezése"},
        {"pdfsavedialog.allowModifyContents", "Tartalom módosításának engedélyezése"},
        {"pdfsavedialog.allowModifyAnnotations", "Megjegyzések módosításának engedélyezése"},
        {"pdfsavedialog.allowFillIn", "Kitöltések engedélyezése formanyomtatványban"},

        {"pdfsavedialog.option.noprinting", "Nem nyomtatható"},
        {"pdfsavedialog.option.degradedprinting", "Alacsony minõségû nyomtatás"},
        {"pdfsavedialog.option.fullprinting", "Nyomtatás engedélyezve"},

        {"pdfsavedialog.cancel", "Megszakít"},
        {"pdfsavedialog.confirm", "Jóváhagy"},

        {"excelexportdialog.dialogtitle", "A lista exportálása Excel fájlba ..."},
        {"excelexportdialog.filename", "Fájlnév"},
        {"excelexportdialog.author", "Szerzõ"},
        {"excelexportdialog.title", "Cím"},
        {"excelexportdialog.selectFile", "Válassz egy fájlt"},

        {"excelexportdialog.warningTitle", "Figyelmeztetés"},
        {"excelexportdialog.errorTitle", "Hiba"},
        {"excelexportdialog.targetIsEmpty", "Kérlek nevezd el az Excel fájlt."},
        {"excelexportdialog.targetIsNoFile", "A kiválasztott cél nem fájl."},
        {"excelexportdialog.targetIsNotWritable", "A kiválasztott fájl nem írható."},
        {"excelexportdialog.targetOverwriteConfirmation",
         "A(z) ''{0}'' fájl létezik. Felülírhatom?"},
        {"excelexportdialog.targetOverwriteTitle", "Felülírhatom a fájlt?"},

        {"excelexportdialog.cancel", "Megszakít"},
        {"excelexportdialog.confirm", "Jóváhagy"},
        {"excelexportdialog.strict-layout", "Pontos táblamegjelenítés az exportáláskor."},

        {"htmlexportdialog.dialogtitle", "A lista exportálása Html fájlba ..."},

        {"htmlexportdialog.filename", "Fájlnév"},
        {"htmlexportdialog.datafilename", "Adatkönyvtár"},
        {"htmlexportdialog.copy-external-references", "Külsõ hivatkozások másolása"},

        {"htmlexportdialog.author", "Szerzõ"},
        {"htmlexportdialog.title", "Cím"},
        {"htmlexportdialog.encoding", "Karakterkódolás"},
        {"htmlexportdialog.selectZipFile", "Válassz egy fájlt"},
        {"htmlexportdialog.selectStreamFile", "Válassz egy fájlt"},
        {"htmlexportdialog.selectDirFile", "Válassz egy fájlt"},

        {"htmlexportdialog.strict-layout", "Pontos táblamegjelenítés az exportáláskor."},
        {"htmlexportdialog.generate-xhtml", "XHTML 1.0 kimenet generálása"},
        {"htmlexportdialog.generate-html4", "HTML 4.0 kimenet generálása"},

        {"htmlexportdialog.warningTitle", "Figyelmeztetés"},
        {"htmlexportdialog.errorTitle", "Hiba"},
        {"htmlexportdialog.targetIsEmpty", "Kérlek nevezd el a Html fájlt."},
        {"htmlexportdialog.targetIsNoFile", "A kiválasztott cél nem fájl."},
        {"htmlexportdialog.targetIsNotWritable", "A kiválasztott fájl nem írható."},
        {"htmlexportdialog.targetOverwriteConfirmation",
         "A(z) ''{0}'' fájl létezik. Felülírhatom?"},
        {"htmlexportdialog.targetOverwriteTitle", "Felülírhatom a fájlt?"},

        {"htmlexportdialog.cancel", "Megszakít"},
        {"htmlexportdialog.confirm", "Jóváhagy"},
        {"htmlexportdialog.targetPathIsAbsolute",
         "A megadott elérési út egy abszolút könyvtárhivatkozás.\n"
      + "Kérlek adj meg egy adatkönyvtárat a ZIP fájlon belül."},
        {"htmlexportdialog.targetDataDirIsNoDirectory",
         "A megadott adatkönyvtár nem érvényes."},
        {"htmlexportdialog.targetCreateDataDirConfirmation",
         "A megadott adatkönyvtár nem létezik.\n"
      + "Hozzam létre a hiányzó alkönyvtárakat?"},
        {"htmlexportdialog.targetCreateDataDirTitle", "Hozzam létre az adatkönytárat?"},

        {"csvexportdialog.dialogtitle", "A lista exportálása CSV fájlba ..."},
        {"csvexportdialog.filename", "Fájlnév"},
        {"csvexportdialog.encoding", "Karakterkódolás"},
        {"csvexportdialog.separatorchar", "Elválasztó karakter"},
        {"csvexportdialog.selectFile", "Válassz egy fájlt"},

        {"csvexportdialog.warningTitle", "Figyelmeztetés"},
        {"csvexportdialog.errorTitle", "Hiba"},
        {"csvexportdialog.targetIsEmpty", "Kérlek nevezd el a CSV fájlt."},
        {"csvexportdialog.targetIsNoFile", "A kiválasztott cél nem fájl."},
        {"csvexportdialog.targetIsNotWritable", "A kiválasztott fájl nem írható."},
        {"csvexportdialog.targetOverwriteConfirmation",
         "A(z) ''{0}'' fájl létezik. Felülírhatom?"},
        {"csvexportdialog.targetOverwriteTitle", "Felülírhatom a fájlt?"},

        {"csvexportdialog.cancel", "Megszakít"},
        {"csvexportdialog.confirm", "Jóváhagy"},

        {"csvexportdialog.separator.tab", "Tabulátor"},
        {"csvexportdialog.separator.colon", "Vesszõ (,)"},
        {"csvexportdialog.separator.semicolon", "Pontosvesszõ (;)"},
        {"csvexportdialog.separator.other", "Más"},

        {"csvexportdialog.exporttype", "Exportálás módja"},
        {"csvexportdialog.export.data", "Adatsor exportálása (Nyers adat)"},
        {"csvexportdialog.export.printed_elements",
         "Nyomtatott elemek exportálása (Megjelenített adatok)"},
        {"csvexportdialog.strict-layout", "Pontos táblamegjelenítés az exportáláskor."},


        {"plain-text-exportdialog.dialogtitle",
         "A lista exportálása egyszerû szöveges fájlba..."},
        {"plain-text-exportdialog.filename", "Fájlnév"},
        {"plain-text-exportdialog.encoding", "Karakterkódolás"},
        {"plain-text-exportdialog.printer", "Nyomtató típusa"},
        {"plain-text-exportdialog.printer.plain", "Egyszerû szöveges kimenet"},
        {"plain-text-exportdialog.printer.epson", "Epson ESC/P kompatibilis"},
        {"plain-text-exportdialog.printer.ibm", "IBM kompatibilis"},
        {"plain-text-exportdialog.selectFile", "Válassz egy fájlt"},

        {"plain-text-exportdialog.warningTitle", "Figyelmeztetés"},
        {"plain-text-exportdialog.errorTitle", "Hiba"},
        {"plain-text-exportdialog.targetIsEmpty",
         "Kérlek nevezd el a CSV fájlt."},
        {"plain-text-exportdialog.targetIsNoFile", "A kiválasztott cél nem fájl."},
        {"plain-text-exportdialog.targetIsNotWritable", "A kiválasztott fájl nem írható."},
        {"plain-text-exportdialog.targetOverwriteConfirmation",
         "A(z) ''{0}'' fájl létezik. Felülírhatom?"},
        {"plain-text-exportdialog.targetOverwriteTitle", "Felülírhatom a fájlt?"},

        {"plain-text-exportdialog.cancel", "Megszakít"},
        {"plain-text-exportdialog.confirm", "Jóváhagy"},

        {"plain-text-exportdialog.chars-per-inch", "cpi (Karakter per inch)"},
        {"plain-text-exportdialog.lines-per-inch", "lpi (Sor per inch)"},
        {"plain-text-exportdialog.font-settings", "Betûtítpus beállítások"},

        {"convertdialog.targetIsEmpty", "A célállomány nincs megadva"},
        {"convertdialog.errorTitle", "Hiba"},
        {"convertdialog.targetIsNoFile", "A megadott cél nem fájl."},
        {"convertdialog.targetIsNotWritable", "A megadott fájl nem írható."},
        {"convertdialog.targetOverwriteConfirmation",
         "A(z) ''{0}'' fájl létezik. Felülírhatom?"},
        {"convertdialog.targetOverwriteTitle", "Felülírhatom a fájlt?"},
        {"convertdialog.targetFile", "Cél fájl"},
        {"convertdialog.sourceIsEmpty", "A forrás fájl nincs megadva"},
        {"convertdialog.sourceIsNoFile", "A megadott forrás nem közönséges fájl."},
        {"convertdialog.sourceIsNotReadable", "A forrásfájl nem olvasható."},
        {"convertdialog.sourceFile", "Forrás fájl"},

        {"convertdialog.action.selectTarget.name", "Válassz"},
        {"convertdialog.action.selectTarget.description", "Válassz cél fájlt."},
        {"convertdialog.action.selectSource.name", "Válassz"},
        {"convertdialog.action.selectSource.description", "Válassz forrás fájlt."},
        {"convertdialog.action.convert.name", "Konvertálás"},
        {"convertdialog.action.convert.description", "Konvertáld a forrás fájlokat."},

        {"convertdialog.title", "Lista konvertáló"},

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
