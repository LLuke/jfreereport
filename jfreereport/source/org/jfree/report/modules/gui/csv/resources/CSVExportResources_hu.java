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
 * CSVExportResources_es.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Demeter F. Tamás;
 * Contributor(s):   -;
 *
 * $Id: CSVExportResources_hu.java,v 1.4 2003/08/24 15:08:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05.07.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.csv.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Hungarian language resource for the CSV export GUI.
 *
 * @author Demeter F. Tamás
 */
public class CSVExportResources_hu extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public CSVExportResources_hu()
  {
  }

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
        {"action.export-to-csv.name", "Exportáld CSV-ként..."},
        {"action.export-to-csv.description", "Mentés CSV formátumban"},
        {"action.export-to-csv.mnemonic", new Integer(KeyEvent.VK_C)},

        {"error.processingfailed.title", "A lista feldolgozása nem sikerült"},
        {"error.processingfailed.message", "A lista készítése során hiba történt: {0}"},
        {"error.savefailed.message", "Hiba a PDF fájl mentésekor: {0}"},
        {"error.savefailed.title", "Hiba a mentéskor"},


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

      };


  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{CSVExportResources.class.getName(), "hu"});
  }

}
