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
 * XLSExportResources_sv.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Dilts;
 * Contributor(s):   -;
 *
 * $Id: XLSExportResources_sv.java,v 1.5 2003/08/31 19:27:58 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.xls.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Swedish language resource for the Html export GUI.
 *
 * @author Thomas Dilts
 */
public class XLSExportResources_sv extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public XLSExportResources_sv()
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
        {"action.export-to-excel.name", "Exportera till Excel..."},
        {"action.export-to-excel.description", "Spara till MS-Excel format"},
        {"action.export-to-excel.mnemonic", new Integer(KeyEvent.VK_E)},
        {"action.export-to-excel.accelerator", createMenuKeystroke(KeyEvent.VK_E)},

        {"error.processingfailed.title", "Report generation misslyckades"},
        {"error.processingfailed.message", "Fel när rapporten skapades: {0}"},
        {"error.savefailed.message", "Fel inträffade under PDF sparning: {0}"},
        {"error.savefailed.title", "Fel under sparningen"},

        {"excelexportdialog.dialogtitle", "Exportera rapporten till en Excel-fil ..."},
        {"excelexportdialog.filename", "Filnamn"},
        {"excelexportdialog.author", "Författaren"},
        {"excelexportdialog.title", "Titeln"},
        {"excelexportdialog.selectFile", "Välja filen"},

        {"excelexportdialog.warningTitle", "Varning"},
        {"excelexportdialog.errorTitle", "Fel"},
        {"excelexportdialog.targetIsEmpty", "Ange filnamnet till Excel filen."},
        {"excelexportdialog.targetIsNoFile", "Mål filen är inte en vanlig fil."},
        {"excelexportdialog.targetIsNotWritable", "Den valde filen är skrivskydad."},
        {"excelexportdialog.targetOverwriteConfirmation",
         "Filen ''{0}'' Finns. Skriva över den?"},
        {"excelexportdialog.targetOverwriteTitle", "Skriva över filen?"},

        {"excelexportdialog.cancel", "Avbryt"},
        {"excelexportdialog.confirm", "Konfirmera"},
        {"excelexportdialog.strict-layout", "Använda stränga tabell format reglar för exporten."},

        {"excel-export.progressdialog.title", "Export på gång till en Excel fil ..."},
        {"excel-export.progressdialog.message", 
          "Rapporten exporterades till en Excel ark ..."},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    new XLSExportResources_sv().generateResourceProperties("swedish");
    System.exit(0);
  }
}
