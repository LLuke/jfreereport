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
 * $Id: PDFExportResources.java,v 1.1 2003/07/07 22:44:06 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 05.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.pdf.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

public class PDFExportResources extends JFreeReportResources
{
  public PDFExportResources()
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
        {"action.save-as.name", "Save As PDF..."},
        {"action.save-as.description", "Save to PDF format"},
        {"action.save-as.mnemonic", new Integer(KeyEvent.VK_A)},
        {"action.save-as.accelerator", createMenuKeystroke(KeyEvent.VK_S)},
        {"action.save-as.small-icon", getIcon("org/jfree/report/modules/gui/base/resources/SaveAs16.gif")},
        {"action.save-as.icon", getIcon("org/jfree/report/modules/gui/base/resources/SaveAs24.gif")},

        {"error.processingfailed.title", "Report processing failed"},
        {"error.processingfailed.message", "Error on processing this report: {0}"},
        {"error.savefailed.message", "Error on saving the PDF file: {0}"},
        {"error.savefailed.title", "Error on save"},

        {"file.save.pdfdescription", "PDF documents"},

        {"pdfsavedialog.dialogtitle", "Saving Report into a PDF-File ..."},
        {"pdfsavedialog.filename", "Filename"},
        {"pdfsavedialog.author", "Author"},
        {"pdfsavedialog.title", "Title"},
        {"pdfsavedialog.selectFile", "Select File"},
        {"pdfsavedialog.security", "Security Settings and Encryption"},
        {"pdfsavedialog.encoding", "Encoding"},

        {"pdfsavedialog.securityNone", "No Security"},
        {"pdfsavedialog.security40bit", "Encrypt with 40 bit keys"},
        {"pdfsavedialog.security128bit", "Encrypt with 128 bit keys"},
        {"pdfsavedialog.userpassword", "User Password"},
        {"pdfsavedialog.userpasswordconfirm", "Confirm"},
        {"pdfsavedialog.userpasswordNoMatch", "The user-passwords do not match."},
        {"pdfsavedialog.ownerpassword", "Owner Password"},
        {"pdfsavedialog.ownerpasswordconfirm", "Confirm"},
        {"pdfsavedialog.ownerpasswordNoMatch", "The owner-passwords do not match."},
        {"pdfsavedialog.ownerpasswordEmpty", "The owner-password is empty. Users may be "
      + "able to change security constraints. Continue anyway?"},

        {"pdfsavedialog.warningTitle", "Warning"},
        {"pdfsavedialog.errorTitle", "Error"},
        {"pdfsavedialog.targetIsEmpty", "Please specify a filename for the pdf file."},
        {"pdfsavedialog.targetIsNoFile", "The selected target is no ordinary file."},
        {"pdfsavedialog.targetIsNotWritable", "The selected file is not writable."},
        {"pdfsavedialog.targetOverwriteConfirmation",
         "The file ''{0}'' exists. Overwrite it?"},
        {"pdfsavedialog.targetOverwriteTitle", "Overwrite file?"},


        {"pdfsavedialog.allowCopy", "Allow Copy"},
        {"pdfsavedialog.allowPrinting", "Allow Printing"},
        {"pdfsavedialog.allowDegradedPrinting", "Allow Degraded Printing"},
        {"pdfsavedialog.allowScreenreader", "Allow Usage Of Screenreaders"},
        {"pdfsavedialog.allowAssembly", "Allow (Re-)assembly"},
        {"pdfsavedialog.allowModifyContents", "Allow Modifications of Contents"},
        {"pdfsavedialog.allowModifyAnnotations", "Allow Modification Of Annotations"},
        {"pdfsavedialog.allowFillIn", "Allow Fill In of Formulardata"},

        {"pdfsavedialog.option.noprinting", "No printing"},
        {"pdfsavedialog.option.degradedprinting", "Low quality printing"},
        {"pdfsavedialog.option.fullprinting", "Printing allowed"},

        {"pdfsavedialog.cancel", "Cancel"},
        {"pdfsavedialog.confirm", "Confirm"},

      };


  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{PDFExportResources.class.getName(), null});
  }

}
