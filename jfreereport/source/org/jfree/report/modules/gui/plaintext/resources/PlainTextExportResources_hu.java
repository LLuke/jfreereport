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
        {"action.export-to-plaintext.name", "Ment�s mint text f�jl..."},
        {"action.export-to-plaintext.description", "Ment�s egyszer� sz�veges form�tumban"},
        {"action.export-to-plaintext.mnemonic", new Integer(KeyEvent.VK_T)},

        {"error.processingfailed.title", "A lista feldolgoz�sa nem siker�lt"},
        {"error.processingfailed.message", "A lista k�sz�t�se sor�n hiba t�rt�nt: {0}"},
        {"error.savefailed.message", "Hiba a PDF f�jl ment�sekor: {0}"},
        {"error.savefailed.title", "Hiba a ment�skor"},

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
