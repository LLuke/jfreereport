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
 * -------------------------
 * JFreeReportResources_si.java
 * -------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Sergey M Mozgovoi;
 * Contributor(s):   -;
 *
 * $Id: JFreeReportResources_fr.java,v 1.6 2003/05/16 13:24:41 taqua Exp $
 *
 */
package com.jrefinery.report.resources;


/**
 * Slovenian Language Resources.
 *
 * @author  Sergey M Mozgovoi
 */

public class JFreeReportResources_si extends JFreeReportResources
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

        {"action.save-as.name", "Shrani kot PDF..."},
        {"action.save-as.description", "Shrani v PDF obliki"},

        {"action.export-to-excel.name", "Izvozi v Excel..."},
        {"action.export-to-excel.description", "Shrani v MS-Excel obliki"},

        {"action.export-to-html.name", "Izvozi v HTML..."},
        {"action.export-to-html.description", "Shrani v HTML obliki"},

        {"action.export-to-csv.name", "Izvozi v CSV..."},
        {"action.export-to-csv.description", "Shrani v CSV obliki"},

        {"action.export-to-plaintext.name", "Shrani kot besedilno datoteko..."},
        {"action.export-to-plaintext.description", "Shrani v Plain-Text obliki"},

        {"action.page-setup.name", "Priprava strani"},
        {"action.page-setup.description", "Priprava strani"},

        {"action.print.name", "Natisni ..."},
        {"action.print.description", "Natisni poro\u010Dilo"},

        {"action.close.name", "Zapri"},
        {"action.close.description", "Zapri okno predogleda tiskanja"},

        {"action.gotopage.name", "Pojdi na stran ..."},
        {"action.gotopage.description", "Direkten pogled na stran"},

        {"dialog.gotopage.message", "Vnesite \u0161tevilko strani"},
        {"dialog.gotopage.title", "Pojdi na stran"},

        {"action.about.name", "Vizitka"},
        {"action.about.description", "Informacije o aplikaciji"},

        {"action.firstpage.name", "Domov"},
        {"action.firstpage.description", "Preklopi na prvo stran"},

        {"action.back.name", "Nazaj"},
        {"action.back.description", "Preklopi na prvo stran"},

        {"action.forward.name", "Naprej"},
        {"action.forward.description", "Preklopi na naslednjo stran"},

        {"action.lastpage.name", "Konec"},
        {"action.lastpage.description", "Preklopi na zadnjo stran"},

        {"action.zoomIn.name", "Pribli\u017Eaj"},
        {"action.zoomIn.description", "Pove\u010Daj zoom"},

        {"action.zoomOut.name", "Oddalji"},
        {"action.zoomOut.description", "Zmanj\u0161aj zoom"},

        {"preview-frame.title", "Predogled tiskanja"},

        {"menu.file.name", "Datoteka"},

        {"menu.navigation.name", "Krmarjenje"},

        {"menu.zoom.name", "Zoom"},

        {"menu.help.name", "Pomo\u010D"},

        {"file.save.pdfdescription", "PDF dokumenti"},
        {"statusline.pages", "Stran {0} od {1}"},
        {"statusline.error", "Napaka v \u010Dasu generiranja poro\u010Dila: {0}"},
        {"statusline.repaginate", "\u0160tetje prelomov strani, prosim po\u010Dakajte."},
        {"error.processingfailed.title", "Obdelava poro\u010Dila ni uspela"},
        {"error.processingfailed.message", "Napaka pri obdelavi poro\u010Dila: {0}"},
        {"error.savefailed.message", "Napaka pri shranjevanju PDF datoteke: {0}"},
        {"error.savefailed.title", "Napaka pri shranjevanju"},
        {"error.printfailed.message", "Napaka pri tiskanju poro\u010Dila: {0}"},
        {"error.printfailed.title", "Napaka pri tiskanju"},
        {"error.validationfailed.message", "Napaka med preverjanjem vnosa uporabnikovih podatkov."},
        {"error.validationfailed.title", "Napaka pri preverjanju"},

        {"tabletarget.page", "Stran {0}"},

        {"pdfsavedialog.dialogtitle", "Shranjevanje poro\u010Dila v PDF datoteko ..."},
        {"pdfsavedialog.filename", "Ime datoteke"},
        {"pdfsavedialog.author", "Avtor"},
        {"pdfsavedialog.title", "Naslov"},
        {"pdfsavedialog.selectFile", "Izberite datoteko"},
        {"pdfsavedialog.security", "Varnostne nastavitve in kodiranje"},
        {"pdfsavedialog.encoding", "Kodiranje"},

        {"pdfsavedialog.securityNone", "Ni Varnosti"},
        {"pdfsavedialog.security40bit", "Kodiraj z 40 bit-nim klju\u010Dem"},
        {"pdfsavedialog.security128bit", "Kodiraj z 128 bit-nim klju\u010Dem"},
        {"pdfsavedialog.userpassword", "Uporabni\u0161ko geslo"},
        {"pdfsavedialog.userpasswordconfirm", "Potrdi"},
        {"pdfsavedialog.userpasswordNoMatch", "Uporabni\u0161ki gesli se ne ujemata."},
        {"pdfsavedialog.ownerpassword", "Lastni\u0161ko geslo"},
        {"pdfsavedialog.ownerpasswordconfirm", "Potrdi"},
        {"pdfsavedialog.ownerpasswordNoMatch", "Lastni\u0161ki gesli se ne ujemata."},
        {"pdfsavedialog.ownerpasswordEmpty", "Lastni\u0161ko geslo je prazno. Uporabniki lahko spremenijo varnostne omejitve."
      + " \u017Delite vseeno nadaljevati?"},

        {"pdfsavedialog.warningTitle", "Opozorilo"},
        {"pdfsavedialog.errorTitle", "Napaka"},
        {"pdfsavedialog.targetIsEmpty", "Navedite ime PDF datoteke."},
        {"pdfsavedialog.targetIsNoFile", "Izbrani cilj ni navadna datoteka."},
        {"pdfsavedialog.targetIsNotWritable", "V izbrano datoteko ni mogo\u010De pisati."},
        {"pdfsavedialog.targetOverwriteConfirmation",
         "Datoteka ''{0}'' obstaja. Ali jo \u017Eelite prepisati?"},
        {"pdfsavedialog.targetOverwriteTitle", "Ali \u017Eelite prepisati datoteko?"},


        {"pdfsavedialog.allowCopy", "Dovoli kopiranje"},
        {"pdfsavedialog.allowPrinting", "Dovoli tiskanje"},
        {"pdfsavedialog.allowDegradedPrinting", "Dovoli tiskanje slab\u0161e kakovosti"},
        {"pdfsavedialog.allowScreenreader", "Dovoli uporabo programov za zajem slike"},
        {"pdfsavedialog.allowAssembly", "Dovoli (ponovno) sestavo"},
        {"pdfsavedialog.allowModifyContents", "Dovoli spreminjanje vsebin"},
        {"pdfsavedialog.allowModifyAnnotations", "Dovoli spreminjanje pripomb"},
        {"pdfsavedialog.allowFillIn", "Dovoli vnos v obrazec"},

        {"pdfsavedialog.option.noprinting", "Ni mo\u017Enosti tiskanja"},
        {"pdfsavedialog.option.degradedprinting", "Slab\u0161a kakovost tiskanja"},
        {"pdfsavedialog.option.fullprinting", "Tiskanje dovoljeno"},

        {"pdfsavedialog.cancel", "Prekli\u010Di"},
        {"pdfsavedialog.confirm", "Potrdi"},

        {"excelexportdialog.dialogtitle", "Izvozi poro\u010Dilo v Excel datoteko ..."},
        {"excelexportdialog.filename", "Ime datoteke"},
        {"excelexportdialog.author", "Avtor"},
        {"excelexportdialog.title", "Naslov"},
        {"excelexportdialog.selectFile", "Izberite datoteko"},

        {"excelexportdialog.warningTitle", "Opozorilo"},
        {"excelexportdialog.errorTitle", "Napaka"},
        {"excelexportdialog.targetIsEmpty", "Prosim navedite ime Excel datoteke."},
        {"excelexportdialog.targetIsNoFile", "Izbrani cilj ni navadna datoteka."},
        {"excelexportdialog.targetIsNotWritable", "V izbrano datoteko ni mogo\u010De pisati."},
        {"excelexportdialog.targetOverwriteConfirmation",
         "Datoteka ''{0}'' obstaja. Ali jo \u017Eelite prepisati?"},
        {"excelexportdialog.targetOverwriteTitle", "Ali \u017Eelite prepisati datoteko?"},

        {"excelexportdialog.cancel", "Prekli\u010Di"},
        {"excelexportdialog.confirm", "Potrdi"},
        {"excelexportdialog.strict-layout", "Izvedi dosledno urejanje tabele pri izvozu."},

        {"htmlexportdialog.dialogtitle", "Izvozi poro\u010Dilo v HTML datoteko ..."},

        {"htmlexportdialog.filename", "Ime datoteke"},
        {"htmlexportdialog.datafilename", "Podatkovni imenik"},
        {"htmlexportdialog.copy-external-references", "Kopiraj zunanje sklice"},


        {"htmlexportdialog.author", "Avtor"},
        {"htmlexportdialog.title", "Naslov"},
        {"htmlexportdialog.encoding", "Encoding"},
        {"htmlexportdialog.selectZipFile", "Izberite datoteko"},
        {"htmlexportdialog.selectStreamFile", "Izberite datoteko"},
        {"htmlexportdialog.selectDirFile", "Izberite datoteko"},

        {"htmlexportdialog.strict-layout", "Izvedi dosledno urejanje tabele pri izvozu."},
        {"htmlexportdialog.generate-xhtml", "Generiraj izhod v XHTML 1.0 obliki"},
        {"htmlexportdialog.generate-html4", "Generiraj izhod v HTML 4.0 obliki"},

        {"htmlexportdialog.warningTitle", "Opozorilo"},
        {"htmlexportdialog.errorTitle", "Napaka"},
        {"htmlexportdialog.targetIsEmpty", "Prosim navedite ime HTML datoteke."},
        {"htmlexportdialog.targetIsNoFile", "Izbrani cilj ni navadna datoteka."},
        {"htmlexportdialog.targetIsNotWritable", "V izbrano datoteko ni mogo\u010De pisati."},
        {"htmlexportdialog.targetOverwriteConfirmation",
         "Datoteka ''{0}'' obstaja. Ali jo \u017Eelite prepisati?"},
        {"htmlexportdialog.targetOverwriteTitle", "Ali \u017Eelite prepisati datoteko?"},

        {"htmlexportdialog.cancel", "Prekli\u010Di"},
        {"htmlexportdialog.confirm", "Potrdi"},
        {"htmlexportdialog.targetPathIsAbsolute",
         "Navedena ciljna pot je absolutni imenik.\n"
      + "Prosim vnesite podatkovni imenik v ZIP datoteki."},
        {"htmlexportdialog.targetDataDirIsNoDirectory",
         "Navedeni podatkovni imenik ni veljaven."},

        {"htmlexportdialog.targetCreateDataDirConfirmation",
         "Navedeni podatkovni imenik ne obstaja.\n"
      + "Ali naj ustvarim manjkajo\u010De podimenike?"},
        {"htmlexportdialog.targetCreateDataDirTitle", "Ustvarim podatkovni imenik?"},

        {"csvexportdialog.dialogtitle", "Izvozi poro\u010Dilo v CSV datoteko ..."},
        {"csvexportdialog.filename", "Ime datoteke"},
        {"csvexportdialog.encoding", "Kodiranje"},
        {"csvexportdialog.separatorchar", "Lo\u010Dilo"},
        {"csvexportdialog.selectFile", "Izberite datoteko"},

        {"csvexportdialog.warningTitle", "Opozorilo"},
        {"csvexportdialog.errorTitle", "Napaka"},
        {"csvexportdialog.targetIsEmpty", "Prosim navedite ime za CSV datoteko."},
        {"csvexportdialog.targetIsNoFile", "Izbrani cilj ni navadna datoteka."},
        {"csvexportdialog.targetIsNotWritable", "V izbrano datoteko ni mogo\u010De pisati."},
        {"csvexportdialog.targetOverwriteConfirmation",
         "Datoteka ''{0}'' obstaja. Ali jo \u017Eelite prepisati?"},
        {"csvexportdialog.targetOverwriteTitle", "Ali \u017Eelite prepisati datoteko?"},

        {"csvexportdialog.cancel", "Prekli\u010Di"},
        {"csvexportdialog.confirm", "Potrdi"},

        {"csvexportdialog.separator.tab", "Tabulator"},
        {"csvexportdialog.separator.colon", "Vejica (,)"},
        {"csvexportdialog.separator.semicolon", "Podpi\u010Dje (;)"},
        {"csvexportdialog.separator.other", "Ostalo"},

        {"csvexportdialog.exporttype", "Izberi izvozni mehanizem"},
        {"csvexportdialog.export.data", "Izvozi podatkovno vrstico (neobdelani podatki)"},
        {"csvexportdialog.export.printed_elements", "Natisnjeni elementi (urejeni podatki)"},
        {"csvexportdialog.strict-layout", "Izvedi dosledno urejanje tabele pri izvozu."},


        {"plain-text-exportdialog.dialogtitle", "Izvozi poro\u010Dilo v Plain-Text datoteko."},
        {"plain-text-exportdialog.filename", "Ime datoteke"},
        {"plain-text-exportdialog.encoding", "Kodiranje"},
        {"plain-text-exportdialog.printer", "Tip tiskalnika"},
        {"plain-text-exportdialog.printer.plain", "Izhod v Plain-Text obliki"},
        {"plain-text-exportdialog.printer.epson", "Zdru\u017Eljivo s tiskalnikom Epson ESC/P"},
        {"plain-text-exportdialog.printer.ibm", "Zdru\u017Eljivo s tiskalnikom IBM"},
        {"plain-text-exportdialog.selectFile", "Izberite datoteko"},

        {"plain-text-exportdialog.warningTitle", "Opozorilo"},
        {"plain-text-exportdialog.errorTitle", "Napaka"},
        {"plain-text-exportdialog.targetIsEmpty",
         "Prosim navedite ime za CSV datoteko."},
        {"plain-text-exportdialog.targetIsNoFile", "Izbrani cilj ni navadna datoteka."},
        {"plain-text-exportdialog.targetIsNotWritable", "V izbrano datoteko ni mogo\u010De pisati."},
        {"plain-text-exportdialog.targetOverwriteConfirmation",
         "Datoteka ''{0}'' obstaja. Ali jo \u017Eelite prepisati?"},
        {"plain-text-exportdialog.targetOverwriteTitle", "Ali \u017Eelite prepisati datoteko?"},

        {"plain-text-exportdialog.cancel", "Prekli\u010Di"},
        {"plain-text-exportdialog.confirm", "Potrdi"},

        {"plain-text-exportdialog.chars-per-inch", "cpi (znakov na palec)"},
        {"plain-text-exportdialog.lines-per-inch", "lpi (vrstic na palec)"},
        {"plain-text-exportdialog.font-settings", "Nastavitev pisave"},

        {"convertdialog.targetIsEmpty", "Ciljna datoteka ni navedena"},
        {"convertdialog.errorTitle", "Napaka"},
        {"convertdialog.targetIsNoFile", "Navedena ciljna datoteka ni navadna datoteka."},
        {"convertdialog.targetIsNotWritable", "V navedeno ciljno datoteko ni mogo\u010De pisati."},
        {"convertdialog.targetOverwriteConfirmation",
         "Datoteka ''{0}'' obstaja. Ali jo \u017Eelite prepisati?"},
        {"convertdialog.targetOverwriteTitle", "Ali \u017Eelite prepisati datoteko?"},
        {"convertdialog.targetFile", "Ciljna datoteka"},
        {"convertdialog.sourceIsEmpty", "Izvorna datoteka ni navedena"},
        {"convertdialog.sourceIsNoFile", "Navedena izvorna datoteka ni navadna datoteka."},
        {"convertdialog.sourceIsNotReadable", "Izvorna datoteka ni berljiva."},
        {"convertdialog.sourceFile", "Izvorna datoteka"},

        {"convertdialog.action.selectTarget.name", "Izberi"},
        {"convertdialog.action.selectTarget.description", "Izberi ciljno datoteko."},
        {"convertdialog.action.selectSource.name", "Izberi"},
        {"convertdialog.action.selectSource.description", "Izberi izvorno datoteko."},
        {"convertdialog.action.convert.name", "Pretvori"},
        {"convertdialog.action.convert.description", "Pretvori izvorne datoteke."},

        {"convertdialog.title", "Pretvornik poro\u010Dila"},
      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(String[] args)
  {
    ResourceCompareTool.main(new String[]{"si"});
  }

}
