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
 * ------------------------------
 * HtmlExportResources.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 05.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.html.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;
import org.jfree.report.modules.gui.csv.resources.CSVExportResources;

public class HtmlExportResources_hu extends JFreeReportResources
{
  public HtmlExportResources_hu()
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
        {"action.export-to-html.name", "Exportáld html-ként..."},
        {"action.export-to-html.description", "Mentés HTML formátumban"},
        {"action.export-to-html.mnemonic", new Integer(KeyEvent.VK_H)},

        {"error.processingfailed.title", "A lista feldolgozása nem sikerült"},
        {"error.processingfailed.message", "A lista készítése során hiba történt: {0}"},
        {"error.savefailed.message", "Hiba a PDF fájl mentésekor: {0}"},
        {"error.savefailed.title", "Hiba a mentéskor"},
        {"error.validationfailed.message", "Hiba a az adatbevitel ellenõrzésekor."},
        {"error.validationfailed.title", "Hiba az ellenõrzéskor"},

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

      };


  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{HtmlExportResources.class.getName(), "hu"});
  }

}
