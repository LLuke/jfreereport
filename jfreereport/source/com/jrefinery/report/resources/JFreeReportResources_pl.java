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
 * JFreeReportResources_pl.java
 * ----------------------------
 *
 *
 */
package com.jrefinery.report.resources;

import java.awt.event.KeyEvent;

/**
 * Polish Language Resources.
 *
 * @author PB
 */
public class JFreeReportResources_pl extends JFreeReportResources
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
        {"action.save-as.name", "Zapisz do pliku PDF..."},
        {"action.save-as.description", "Zapisz do pliku PDF"},
        {"action.save-as.mnemonic", new Integer(KeyEvent.VK_Z)},

        {"action.page-setup.name", "Ustawienia wydruku"},
        {"action.page-setup.description", "Ustawienia wydruku"},
        {"action.page-setup.mnemonic", new Integer(KeyEvent.VK_W)},

        {"action.print.name", "Drukuj..."},
        {"action.print.description", "Drukuj raport"},
        {"action.print.mnemonic", new Integer(KeyEvent.VK_D)},

        {"action.close.name", "Zamknij"},
        {"action.close.description", "Zamknij okno podgl\u0105du"},
        {"action.close.mnemonic", new Integer(KeyEvent.VK_K)},

        {"action.gotopage.name", "Przejd\u017a do strony..."},
        {"action.gotopage.description",
         "Przejd\u017a bezpo\u015brednio do okre\u015blonej strony"},

        {"dialog.gotopage.message", "Podaj numer strony"},
        {"dialog.gotopage.title", "Przejd\u017a do strony..."},

        {"action.about.name", "O programie..."},
        {"action.about.description", "Informacja o programie"},
        {"action.about.mnemonic", new Integer(KeyEvent.VK_O)},

        {"action.firstpage.name", "Przejd\u017a do pierwszej strony"},
        {"action.firstpage.description", "Przejd\u017a do pierwszej strony"},

        {"action.lastpage.name", "Przejd\u017a do ostatniej strony"},
        {"action.lastpage.description", "Przejd\u017a do ostatniej strony"},

        {"action.back.name", "Przejd\u017a do poprzedniej strony"},
        {"action.back.description", "Przejd\u017a do poprzedniej strony"},

        {"action.forward.name", "Przejd\u017a do nast\u0119pnej strony"},
        {"action.forward.description", "Przejd\u017a do nast\u0119pnej strony"},

        {"action.zoomIn.name", "Powi\u0119kszenie"},
        {"action.zoomIn.description", "Powi\u0119kszenie"},

        {"action.zoomOut.name", "Pomniejszenie"},
        {"action.zoomOut.description", "Pomniejszenie"},

        {"preview-frame.title", "Podgl\u0105d wydruku"},

        {"menu.file.name", "Plik"},
        {"menu.file.mnemonic", new Character('P')},

        {"menu.help.name", "Pomoc"},
        {"menu.help.mnemonic", new Character('c')},

        {"file.save.pdfdescription", "Pliki PDF"},
        {"statusline.pages", "Strona {0} z {1}"},
        {"statusline.error",
         "Wyst\u0105pi\u0142 b\u0142\u0105d podczas generowania raportu: {0}"},
        {"error.processingfailed.title", "Przetwarzanie raportu nie powiod\u0142o si\u0119"},
        {"error.processingfailed.message", "Podczas przetwarzania raportu wyst\u0105pi\u0142 "
      + "nast\u0119puj\u0105cy b\u0142\u0105d: {0}"},
        {"error.savefailed.message", "Podczas zapisywania do pliku PDF wyst\u0105pi\u0142 "
      + "nast\u0119puj\u0105cy b\u0142\u0105d: {0}"},
        {"error.savefailed.title", "B\u0142\u0105d podczas zapisywania"},
        {"error.printfailed.message",
         "Podczas drukowania wyst\u0105pi\u0142 nast\u0119puj\u0105cy b\u0142\u0105d: {0}"},
        {"error.printfailed.title", "B\u0142\u0105d podczas drukowania"},

        {"tabletarget.page", "Strona {0}"},

        {"pdfsavedialog.warningTitle", "Ostrze\u017cenie"},
        {"pdfsavedialog.dialogtitle", "Zapisywanie raportu do pliku PDF..."},
        {"pdfsavedialog.filename", "Nazwa pliku"},
        {"pdfsavedialog.author", "Autor"},
        {"pdfsavedialog.title", "Tytu\u0142"},
        {"pdfsavedialog.selectFile", "Wybierz plik"},
        {"pdfsavedialog.security", "Zabezpieczenia i szyfrowanie"},

        {"pdfsavedialog.securityNone", "Brak zabezpiecze\u0144"},
        {"pdfsavedialog.security40bit", "Szyfrowanie 40-bitowym kluczem"},
        {"pdfsavedialog.security128bit", "Szyfrowanie 128-bitowym kluczem"},
        {"pdfsavedialog.userpassword", "Has\u0142o u\u017cytkownika"},
        {"pdfsavedialog.userpasswordconfirm", "Powt\u00f3rzenie has\u0142a)"},
        {"pdfsavedialog.userpasswordNoMatch",
         "Has\u0142o i powt\u00f3rzenie has\u0142a s\u0105 r\u00f3\u017cne."},
        {"pdfsavedialog.ownerpassword", "Has\u0142o w\u0142a\u015bciciela"},
        {"pdfsavedialog.ownerpasswordconfirm", "Powt\u00f3rzenie has\u0142a"},
        {"pdfsavedialog.ownerpasswordNoMatch",
         "Has\u0142o i potwierdzenie has\u0142a s\u0105 r\u00f3\u017cne."},

        {"pdfsavedialog.ownerpasswordEmpty", "Nie podano has\u0142a w\u0142a\u015bciciela. "
      + "U\u017cytkownicy b\u0119d\u0105 mogli zmienia\u0107 ustawienia "
      + "zabezpiecze\u0144. Kontynuowa\u0107?"},

        {"pdfsavedialog.errorTitle", "B\u0142\u0105d"},
        {"pdfsavedialog.targetIsEmpty", "Prosz\u0119 poda\u0107 nazw\u0119 pliku PDF."},
        {"pdfsavedialog.targetIsNoFile", "Wybrano nieprawid\u0142owy plik."},
        {"pdfsavedialog.targetIsNotWritable", "Wybrany plik nie mo\u017ce by\u0107 zapisany."},
        {"pdfsavedialog.targetOverwriteConfirmation",
         "Plik ''{0}'' ju\u017c istnieje. Zast\u0105pi\u0107?"},
        {"pdfsavedialog.targetOverwriteTitle", "Nadpisa\u0107 plik?"},

        {"pdfsavedialog.allowCopy", "Zezw\u00f3l na kopiowanie"},
        {"pdfsavedialog.allowPrinting", "Opcje drukowania"},
        {"pdfsavedialog.allowDegradedPrinting", "Zezw\u00f3l na ograniczone drukowanie"},
        {"pdfsavedialog.allowScreenreader",
         "Zezw\u00f3l na u\u017cycie innych urz\u0105dze\u0144 czytaj\u0105cych"},
        {"pdfsavedialog.allowAssembly", "Zezw\u00f3l na edycj\u0119 sk\u0142adu"},
        {"pdfsavedialog.allowModifyContents",
         "Zezw\u00f3l na modyfikacj\u0119 zawarto\u015bci"},
        {"pdfsavedialog.allowModifyAnnotations", "Zezw\u00f3l na modyfikacj\u0119 notatek"},
        {"pdfsavedialog.allowFillIn", "Zezw\u00f3l na wprowadzanie danych do formularzy"},

        {"pdfsavedialog.option.noprinting", "Brak drukowania"},
        {"pdfsavedialog.option.degradedprinting", "Drukowanie niskiej jako\u015bci"},
        {"pdfsavedialog.option.fullprinting", "Drukowanie dozwolone"},

        {"pdfsavedialog.cancel", "Anuluj"},
        {"pdfsavedialog.confirm", "OK"},

      };


  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{"pl"});
  }

}
