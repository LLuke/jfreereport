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
 * PDFExportResources.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PlainTextExportResources.java,v 1.3 2003/08/19 13:37:24 taqua Exp $
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

/**
 * English language resource for the PDF export GUI.
 * 
 * @author Thomas Morgner
 */
public class PlainTextExportResources extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public PlainTextExportResources()
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
        {"action.export-to-plaintext.name", "Save as text file..."},
        {"action.export-to-plaintext.description", "Save to PlainText format"},
        {"action.export-to-plaintext.mnemonic", new Integer(KeyEvent.VK_T)},
        {"action.export-to-plaintext.accelerator", createMenuKeystroke(KeyEvent.VK_T)},
        // temporarily using the same icon as "Save to PDF", till we have a better one
        {"action.export-to-plaintext.small-icon",
         getIcon("org/jfree/report/modules/gui/base/resources/SaveAs16.gif")},
        {"action.export-to-plaintext.icon",
         getIcon("org/jfree/report/modules/gui/base/resources/SaveAs24.gif")},

        {"error.processingfailed.title", "Report processing failed"},
        {"error.processingfailed.message", "Error on processing this report: {0}"},
        {"error.savefailed.message", "Error on saving the PDF file: {0}"},
        {"error.savefailed.title", "Error on save"},

        {"plain-text-exportdialog.dialogtitle", "Export Report into an Plain-Text-File ..."},
        {"plain-text-exportdialog.filename", "Filename"},
        {"plain-text-exportdialog.encoding", "Encoding"},
        {"plain-text-exportdialog.printer", "Printer type"},
        {"plain-text-exportdialog.printer.plain", "Plain text output"},
        {"plain-text-exportdialog.printer.epson", "Epson ESC/P compatible"},
        {"plain-text-exportdialog.printer.ibm", "IBM compatible"},
        {"plain-text-exportdialog.selectFile", "Select File"},

        {"plain-text-exportdialog.warningTitle", "Warning"},
        {"plain-text-exportdialog.errorTitle", "Error"},
        {"plain-text-exportdialog.targetIsEmpty",
         "Please specify a filename for the CSV file."},
        {"plain-text-exportdialog.targetIsNoFile", "The selected target is no ordinary file."},
        {"plain-text-exportdialog.targetIsNotWritable", "The selected file is not writable."},
        {"plain-text-exportdialog.targetOverwriteConfirmation",
         "The file ''{0}'' exists. Overwrite it?"},
        {"plain-text-exportdialog.targetOverwriteTitle", "Overwrite file?"},

        {"plain-text-exportdialog.cancel", "Cancel"},
        {"plain-text-exportdialog.confirm", "Confirm"},

        {"plain-text-exportdialog.chars-per-inch", "cpi (Characters per inch)"},
        {"plain-text-exportdialog.lines-per-inch", "lpi (Lines per inch)"},
        {"plain-text-exportdialog.font-settings", "Font settings"},

        {"plaintext-export.progressdialog.title", "Exporting to a text file ..."},
        {"plaintext-export.progressdialog.message", "The report is now exported into a text file ..."},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{PlainTextExportResources.class.getName(), null});
  }
}
