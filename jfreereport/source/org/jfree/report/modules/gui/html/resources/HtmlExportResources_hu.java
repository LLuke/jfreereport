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
        {"action.export-to-html.name", "Export�ld html-k�nt..."},
        {"action.export-to-html.description", "Ment�s HTML form�tumban"},
        {"action.export-to-html.mnemonic", new Integer(KeyEvent.VK_H)},

        {"error.processingfailed.title", "A lista feldolgoz�sa nem siker�lt"},
        {"error.processingfailed.message", "A lista k�sz�t�se sor�n hiba t�rt�nt: {0}"},
        {"error.savefailed.message", "Hiba a PDF f�jl ment�sekor: {0}"},
        {"error.savefailed.title", "Hiba a ment�skor"},
        {"error.validationfailed.message", "Hiba a az adatbevitel ellen�rz�sekor."},
        {"error.validationfailed.title", "Hiba az ellen�rz�skor"},

        {"htmlexportdialog.dialogtitle", "A lista export�l�sa Html f�jlba ..."},

        {"htmlexportdialog.filename", "F�jln�v"},
        {"htmlexportdialog.datafilename", "Adatk�nyvt�r"},
        {"htmlexportdialog.copy-external-references", "K�ls� hivatkoz�sok m�sol�sa"},

        {"htmlexportdialog.author", "Szerz�"},
        {"htmlexportdialog.title", "C�m"},
        {"htmlexportdialog.encoding", "Karakterk�dol�s"},
        {"htmlexportdialog.selectZipFile", "V�lassz egy f�jlt"},
        {"htmlexportdialog.selectStreamFile", "V�lassz egy f�jlt"},
        {"htmlexportdialog.selectDirFile", "V�lassz egy f�jlt"},

        {"htmlexportdialog.strict-layout", "Pontos t�blamegjelen�t�s az export�l�skor."},
        {"htmlexportdialog.generate-xhtml", "XHTML 1.0 kimenet gener�l�sa"},
        {"htmlexportdialog.generate-html4", "HTML 4.0 kimenet gener�l�sa"},

        {"htmlexportdialog.warningTitle", "Figyelmeztet�s"},
        {"htmlexportdialog.errorTitle", "Hiba"},
        {"htmlexportdialog.targetIsEmpty", "K�rlek nevezd el a Html f�jlt."},
        {"htmlexportdialog.targetIsNoFile", "A kiv�lasztott c�l nem f�jl."},
        {"htmlexportdialog.targetIsNotWritable", "A kiv�lasztott f�jl nem �rhat�."},
        {"htmlexportdialog.targetOverwriteConfirmation",
         "A(z) ''{0}'' f�jl l�tezik. Fel�l�rhatom?"},
        {"htmlexportdialog.targetOverwriteTitle", "Fel�l�rhatom a f�jlt?"},

        {"htmlexportdialog.cancel", "Megszak�t"},
        {"htmlexportdialog.confirm", "J�v�hagy"},
        {"htmlexportdialog.targetPathIsAbsolute",
         "A megadott el�r�si �t egy abszol�t k�nyvt�rhivatkoz�s.\n"
      + "K�rlek adj meg egy adatk�nyvt�rat a ZIP f�jlon bel�l."},
        {"htmlexportdialog.targetDataDirIsNoDirectory",
         "A megadott adatk�nyvt�r nem �rv�nyes."},
        {"htmlexportdialog.targetCreateDataDirConfirmation",
         "A megadott adatk�nyvt�r nem l�tezik.\n"
      + "Hozzam l�tre a hi�nyz� alk�nyvt�rakat?"},
        {"htmlexportdialog.targetCreateDataDirTitle", "Hozzam l�tre az adatk�nyt�rat?"},

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
