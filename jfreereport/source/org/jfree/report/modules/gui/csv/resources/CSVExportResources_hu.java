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
 * Original Author:  Demeter F. Tam�s;
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
 * @author Demeter F. Tam�s
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
        {"action.export-to-csv.name", "Export�ld CSV-k�nt..."},
        {"action.export-to-csv.description", "Ment�s CSV form�tumban"},
        {"action.export-to-csv.mnemonic", new Integer(KeyEvent.VK_C)},

        {"error.processingfailed.title", "A lista feldolgoz�sa nem siker�lt"},
        {"error.processingfailed.message", "A lista k�sz�t�se sor�n hiba t�rt�nt: {0}"},
        {"error.savefailed.message", "Hiba a PDF f�jl ment�sekor: {0}"},
        {"error.savefailed.title", "Hiba a ment�skor"},


        {"csvexportdialog.dialogtitle", "A lista export�l�sa CSV f�jlba ..."},
        {"csvexportdialog.filename", "F�jln�v"},
        {"csvexportdialog.encoding", "Karakterk�dol�s"},
        {"csvexportdialog.separatorchar", "Elv�laszt� karakter"},
        {"csvexportdialog.selectFile", "V�lassz egy f�jlt"},

        {"csvexportdialog.warningTitle", "Figyelmeztet�s"},
        {"csvexportdialog.errorTitle", "Hiba"},
        {"csvexportdialog.targetIsEmpty", "K�rlek nevezd el a CSV f�jlt."},
        {"csvexportdialog.targetIsNoFile", "A kiv�lasztott c�l nem f�jl."},
        {"csvexportdialog.targetIsNotWritable", "A kiv�lasztott f�jl nem �rhat�."},
        {"csvexportdialog.targetOverwriteConfirmation",
         "A(z) ''{0}'' f�jl l�tezik. Fel�l�rhatom?"},
        {"csvexportdialog.targetOverwriteTitle", "Fel�l�rhatom a f�jlt?"},

        {"csvexportdialog.cancel", "Megszak�t"},
        {"csvexportdialog.confirm", "J�v�hagy"},

        {"csvexportdialog.separator.tab", "Tabul�tor"},
        {"csvexportdialog.separator.colon", "Vessz� (,)"},
        {"csvexportdialog.separator.semicolon", "Pontosvessz� (;)"},
        {"csvexportdialog.separator.other", "M�s"},

        {"csvexportdialog.exporttype", "Export�l�s m�dja"},
        {"csvexportdialog.export.data", "Adatsor export�l�sa (Nyers adat)"},
        {"csvexportdialog.export.printed_elements",
         "Nyomtatott elemek export�l�sa (Megjelen�tett adatok)"},
        {"csvexportdialog.strict-layout", "Pontos t�blamegjelen�t�s az export�l�skor."},

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
