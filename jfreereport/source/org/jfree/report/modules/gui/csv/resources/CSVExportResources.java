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
 * CSVExportResources.java
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

package org.jfree.report.modules.gui.csv.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

public class CSVExportResources extends JFreeReportResources
{
  public CSVExportResources()
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
        {"action.export-to-csv.name", "Export to CSV..."},
        {"action.export-to-csv.description", "Save to CSV format"},
        {"action.export-to-csv.mnemonic", new Integer(KeyEvent.VK_C)},
        {"action.export-to-csv.accelerator", createMenuKeystroke(KeyEvent.VK_C)},
        // temporarily using the same icon as "Save to PDF", till we have a better one
        {"action.export-to-csv.small-icon",
         getIcon("org/jfree/report/modules/gui/base/resources/SaveAs16.gif")},
        {"action.export-to-csv.icon",
         getIcon("org/jfree/report/modules/gui/base/resources/SaveAs24.gif")},

        {"csvexportdialog.dialogtitle", "Export Report into a CSV File ..."},
        {"csvexportdialog.filename", "Filename"},
        {"csvexportdialog.encoding", "Encoding"},
        {"csvexportdialog.separatorchar", "Separator Character"},
        {"csvexportdialog.selectFile", "Select File"},

        {"csvexportdialog.warningTitle", "Warning"},
        {"csvexportdialog.errorTitle", "Error"},
        {"csvexportdialog.targetIsEmpty", "Please specify a filename for the CSV file."},
        {"csvexportdialog.targetIsNoFile", "The selected target is no ordinary file."},
        {"csvexportdialog.targetIsNotWritable", "The selected file is not writable."},
        {"csvexportdialog.targetOverwriteConfirmation",
         "The file ''{0}'' exists. Overwrite it?"},
        {"csvexportdialog.targetOverwriteTitle", "Overwrite file?"},

        {"csvexportdialog.cancel", "Cancel"},
        {"csvexportdialog.confirm", "Confirm"},

        {"csvexportdialog.separator.tab", "Tabulator"},
        {"csvexportdialog.separator.colon", "Comma (,)"},
        {"csvexportdialog.separator.semicolon", "Semicolon (;)"},
        {"csvexportdialog.separator.other", "Other"},

        {"csvexportdialog.exporttype", "Select Export engine"},
        {"csvexportdialog.export.data", "Export the DataRow (Raw Data)"},
        {"csvexportdialog.export.printed_elements", "Printed Elements  (Layouted Data)"},
        {"csvexportdialog.strict-layout", "Perform strict table layouting on export."},

        {"error.processingfailed.title", "Report processing failed"},
        {"error.processingfailed.message", "Error on processing this report: {0}"},
        {"error.savefailed.message", "Error on saving the PDF file: {0}"},
        {"error.savefailed.title", "Error on save"},

        {"csvexportdialog.csv-file-description", "Comma Separated Value files."},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{CSVExportResources.class.getName(), null});
  }

}
