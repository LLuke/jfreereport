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
 * XLSExportResources_hu.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Demeter F. Tamás;
 * Contributor(s):   -;
 *
 * $Id: XLSExportResources_hu.java,v 1.4 2003/08/25 14:29:30 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05.07.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.xls.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Hungarian language resource for the excel export GUI.
 *
 * @author Demeter F. Tamás
 */
public class XLSExportResources_hu extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public XLSExportResources_hu()
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
        {"action.export-to-excel.name", "Exportáld Excel-ként..."},
        {"action.export-to-excel.description", "Mentés MS-Excel formátumban"},
        {"action.export-to-excel.mnemonic", new Integer(KeyEvent.VK_E)},

        {"error.processingfailed.title", "A lista feldolgozása nem sikerült"},
        {"error.processingfailed.message", "A lista készítése során hiba történt: {0}"},
        {"error.savefailed.message", "Hiba a PDF fájl mentésekor: {0}"},
        {"error.savefailed.title", "Hiba a mentéskor"},

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

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    new XLSExportResources_hu().generateResourceProperties("hungarian");
    System.exit(0);
  }
}
