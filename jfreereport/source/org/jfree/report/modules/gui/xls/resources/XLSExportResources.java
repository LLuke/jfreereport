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

package org.jfree.report.modules.gui.xls.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;
import org.jfree.report.modules.gui.print.resources.PrintExportResources;

public class XLSExportResources extends JFreeReportResources
{
  public XLSExportResources()
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
        {"action.export-to-excel.name", "Export to Excel..."},
        {"action.export-to-excel.description", "Save to MS-Excel format"},
        {"action.export-to-excel.mnemonic", new Integer(KeyEvent.VK_E)},
        {"action.export-to-excel.accelerator", createMenuKeystroke(KeyEvent.VK_E)},
        // temporarily using the same icon as "Save to PDF", till we have a better one
        {"action.export-to-excel.small-icon",
         getIcon("org/jfree/report/modules/gui/base/resources/SaveAs16.gif")},
        {"action.export-to-excel.icon",
         getIcon("org/jfree/report/modules/gui/base/resources/SaveAs24.gif")},

        {"error.processingfailed.title", "Report processing failed"},
        {"error.processingfailed.message", "Error on processing this report: {0}"},
        {"error.savefailed.message", "Error on saving the PDF file: {0}"},
        {"error.savefailed.title", "Error on save"},

        {"excelexportdialog.dialogtitle", "Export Report into an Excel-File ..."},
        {"excelexportdialog.filename", "Filename"},
        {"excelexportdialog.author", "Author"},
        {"excelexportdialog.title", "Title"},
        {"excelexportdialog.selectFile", "Select File"},

        {"excelexportdialog.warningTitle", "Warning"},
        {"excelexportdialog.errorTitle", "Error"},
        {"excelexportdialog.targetIsEmpty", "Please specify a filename for the Excel file."},
        {"excelexportdialog.targetIsNoFile", "The selected target is no ordinary file."},
        {"excelexportdialog.targetIsNotWritable", "The selected file is not writable."},
        {"excelexportdialog.targetOverwriteConfirmation",
         "The file ''{0}'' exists. Overwrite it?"},
        {"excelexportdialog.targetOverwriteTitle", "Overwrite file?"},

        {"excelexportdialog.cancel", "Cancel"},
        {"excelexportdialog.confirm", "Confirm"},
        {"excelexportdialog.strict-layout", "Perform strict table layouting on export."},

        
      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{XLSExportResources.class.getName(), null});
  }
}
