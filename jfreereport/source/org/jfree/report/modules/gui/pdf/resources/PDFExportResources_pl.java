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
 * Original Author:  PB;
 * Contributor(s):   -;
 *
 * $Id: PDFExportResources_pl.java,v 1.3 2003/08/24 15:08:19 taqua Exp $
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
 * Polish language resource for the PDF export GUI.
 *
 * @author PB
 */
public class PDFExportResources_pl extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public PDFExportResources_pl()
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
        {"action.save-as.name", "Zapisz do pliku PDF..."},
        {"action.save-as.description", "Zapisz do pliku PDF"},
        {"action.save-as.mnemonic", new Integer(KeyEvent.VK_Z)},

        {"file.save.pdfdescription", "Pliki PDF"},

        {"error.processingfailed.title", "Przetwarzanie raportu nie powiod\u0142o si\u0119"},
        {"error.processingfailed.message", "Podczas przetwarzania raportu wyst\u0105pi\u0142 "
      + "nast\u0119puj\u0105cy b\u0142\u0105d: {0}"},
        {"error.savefailed.message", "Podczas zapisywania do pliku PDF wyst\u0105pi\u0142 "
      + "nast\u0119puj\u0105cy b\u0142\u0105d: {0}"},
        {"error.savefailed.title", "B\u0142\u0105d podczas zapisywania"},

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
    ResourceCompareTool.main(new String[]{PDFExportResources.class.getName(), "pl"});
  }
}
