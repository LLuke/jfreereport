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
 * PDFExportResources.java
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

package org.jfree.report.modules.gui.plaintext.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

public class PlainTextExportResources_hu extends JFreeReportResources
{
  public PlainTextExportResources_hu()
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
        {"action.export-to-plaintext.name", "Mentés mint text fájl..."},
        {"action.export-to-plaintext.description", "Mentés egyszerû szöveges formátumban"},
        {"action.export-to-plaintext.mnemonic", new Integer(KeyEvent.VK_T)},

        {"error.processingfailed.title", "A lista feldolgozása nem sikerült"},
        {"error.processingfailed.message", "A lista készítése során hiba történt: {0}"},
        {"error.savefailed.message", "Hiba a PDF fájl mentésekor: {0}"},
        {"error.savefailed.title", "Hiba a mentéskor"},

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

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{PlainTextExportResources.class.getName(), "hu"});
  }
}
