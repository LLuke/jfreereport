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
 * Original Author:  Thomas Dilts;
 * Contributor(s):   -;
 *
 * $Id: CSVExportResources_sv.java,v 1.4 2003/08/25 14:29:29 taqua Exp $
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
 * Swedish language resource for the CSV export GUI.
 *
 * @author Thomas Dilts
 */
public class CSVExportResources_sv extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public CSVExportResources_sv()
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
        {"action.export-to-csv.name", "Exportera till CSV..."},
        {"action.export-to-csv.description", "Spara till CSV format"},
        {"action.export-to-csv.mnemonic", new Integer(KeyEvent.VK_C)},
        {"action.export-to-csv.accelerator", createMenuKeystroke(KeyEvent.VK_C)},

        {"csvexportdialog.dialogtitle", "Exportera rapporten till en CSV fil ..."},
        {"csvexportdialog.filename", "Filnamn"},
        {"csvexportdialog.encoding", "Kodningen"},
        {"csvexportdialog.separatorchar", "Avskiljaren"},
        {"csvexportdialog.selectFile", "Välja filen"},

        {"csvexportdialog.warningTitle", "Varning"},
        {"csvexportdialog.errorTitle", "Fel"},
        {"csvexportdialog.targetIsEmpty", "Ange filnamnet till en CSV fil."},
        {"csvexportdialog.targetIsNoFile", "Mål filen är inte en vanlig fil."},
        {"csvexportdialog.targetIsNotWritable", "Den valde filen är skrivskydad."},
        {"csvexportdialog.targetOverwriteConfirmation",
         "Filen ''{0}'' Finns. Skriva över den?"},
        {"csvexportdialog.targetOverwriteTitle", "Skriva över filen?"},

        {"csvexportdialog.cancel", "Avbryt"},
        {"csvexportdialog.confirm", "Konfirmera"},

        {"csvexportdialog.separator.tab", "Tabulator"},
        {"csvexportdialog.separator.colon", "Kommatecken (,)"},
        {"csvexportdialog.separator.semicolon", "Semikolon (;)"},
        {"csvexportdialog.separator.other", "Andra"},

        {"csvexportdialog.exporttype", "Välje export motor"},
        {"csvexportdialog.export.data", "Exportera DataRow (Rå data)"},
        {"csvexportdialog.export.printed_elements", "Utskriven element  (Formaterad data)"},
        {"csvexportdialog.strict-layout", "Använda stränga tabell format reglar för exporten."},

        {"error.processingfailed.title", "Report generation misslyckades"},
        {"error.processingfailed.message", "Fel när rapporten skapades: {0}"},
        {"error.savefailed.message", "Fel inträffade under PDF sparning: {0}"},
        {"error.savefailed.title", "Fel under sparningen"},

        {"csvexportdialog.csv-file-description", "Kommatecken-avskilda-värden-filer."},

        {"cvs-export.progressdialog.title", "Export till en CSV på gång ..."},
        {"cvs-export.progressdialog.message", "Rapporten exporterades till en CSV fil ..."},
      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{CSVExportResources.class.getName(), "sv"});
  }

}
