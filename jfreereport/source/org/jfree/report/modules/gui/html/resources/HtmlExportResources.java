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
 * HtmlExportResources.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HtmlExportResources.java,v 1.5 2003/09/08 18:39:33 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.html.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;
import org.jfree.report.modules.gui.csv.resources.CSVExportResources;

/**
 * English language resource for the Html export GUI.
 *
 * @author Thomas Morgner
 */
public class HtmlExportResources extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public HtmlExportResources()
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
        {"action.export-to-html.name", "Export to html..."},
        {"action.export-to-html.description", "Save to HTML format"},
        {"action.export-to-html.mnemonic", new Integer(KeyEvent.VK_H)},
        {"action.export-to-html.accelerator", createMenuKeystroke(KeyEvent.VK_H)},
        // temporarily using the same icon as "Save to PDF", till we have a better one
        {"action.export-to-html.small-icon",
         getIcon("org/jfree/report/modules/gui/base/resources/SaveAs16.gif")},
        {"action.export-to-html.icon",
         getIcon("org/jfree/report/modules/gui/base/resources/SaveAs24.gif")},

        {"htmlexportdialog.dialogtitle", "Export Report into an Html-File ..."},

        {"htmlexportdialog.filename", "Filename"},
        {"htmlexportdialog.datafilename", "Data Directory"},
        {"htmlexportdialog.copy-external-references", "Copy external references"},

        {"htmlexportdialog.author", "Author"},
        {"htmlexportdialog.title", "Title"},
        {"htmlexportdialog.encoding", "Encoding"},
        {"htmlexportdialog.selectZipFile", "Select File"},
        {"htmlexportdialog.selectStreamFile", "Select File"},
        {"htmlexportdialog.selectDirFile", "Select File"},

        {"htmlexportdialog.strict-layout", "Perform strict table layouting on export."},
        {"htmlexportdialog.generate-xhtml", "Generate XHTML 1.0 output"},
        {"htmlexportdialog.generate-html4", "Generate HTML 4.0 output"},

        {"htmlexportdialog.warningTitle", "Warning"},
        {"htmlexportdialog.errorTitle", "Error"},
        {"htmlexportdialog.targetIsEmpty", "Please specify a filename for the Html file."},
        {"htmlexportdialog.targetIsNoFile", "The selected target is no ordinary file."},
        {"htmlexportdialog.targetIsNotWritable", "The selected file is not writable."},
        {"htmlexportdialog.targetOverwriteConfirmation",
         "The file ''{0}'' exists. Overwrite it?"},
        {"htmlexportdialog.targetOverwriteTitle", "Overwrite file?"},

        {"htmlexportdialog.cancel", "Cancel"},
        {"htmlexportdialog.confirm", "Confirm"},
        {"htmlexportdialog.targetPathIsAbsolute",
         "The specified target path denotes an absolute directory.\n"
      + "Please enter a data directory within the ZIP file."},
        {"htmlexportdialog.targetDataDirIsNoDirectory",
         "The specified data directory is not valid."},
        {"htmlexportdialog.targetCreateDataDirConfirmation",
         "The specified data directory does not exist.\n"
      + "Shall the missing subdirectories be created?"},
        {"htmlexportdialog.targetCreateDataDirTitle", "Create data directory?"},

        {"htmlexportdialog.html-documents", "HTML documents"},
        {"htmlexportdialog.zip-archives", "ZIP archives"},

        {"htmlexportdialog.stream-export", "File stream export"},
        {"htmlexportdialog.directory-export", "Directory export"},
        {"htmlexportdialog.zip-export", "ZIP file export"},

        {"error.processingfailed.title", "Report processing failed"},
        {"error.processingfailed.message", "Error on processing this report: {0}"},
        {"error.savefailed.message", "Error on saving the file: {0}"},
        {"error.savefailed.title", "Error on save"},
        {"error.validationfailed.message", "Error while validating the user input."},
        {"error.validationfailed.title", "Error on validation"},

        {"html-export.progressdialog.title", "Exporting to an HTML format ..."},
        {"html-export.progressdialog.message", "The report is now exported into a Html file ..."},

      };


  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    new HtmlExportResources().generateResourceProperties("<default>");
    System.exit(0);
  }

}
