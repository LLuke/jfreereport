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
 * Original Author:  Demeter F. Tam�s;
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
 * @author Demeter F. Tam�s
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
        {"action.export-to-excel.name", "Export�ld Excel-k�nt..."},
        {"action.export-to-excel.description", "Ment�s MS-Excel form�tumban"},
        {"action.export-to-excel.mnemonic", new Integer(KeyEvent.VK_E)},

        {"error.processingfailed.title", "A lista feldolgoz�sa nem siker�lt"},
        {"error.processingfailed.message", "A lista k�sz�t�se sor�n hiba t�rt�nt: {0}"},
        {"error.savefailed.message", "Hiba a PDF f�jl ment�sekor: {0}"},
        {"error.savefailed.title", "Hiba a ment�skor"},

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
