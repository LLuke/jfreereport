/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * -------------------------
 * JFreeReportResources.java
 * -------------------------
 * (C)opyright 2000-2003, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: JFreeReportResources.java,v 1.47 2003/02/25 18:46:10 taqua Exp $
 *
 */
package com.jrefinery.report.resources;

import com.jrefinery.report.util.Log;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.ListResourceBundle;

/**
 * English language resources.
 *
 * @author Thomas Morgner
 */
public class JFreeReportResources extends ListResourceBundle
{
  /**
   * Default constructor.
   */
  public JFreeReportResources ()
  {
  }

  /**
   * Used to test the resourcebundle for null values.
   *
   * @param args  ignored.
   */
  public static void main (String[] args)
  {
    Object lastKey = null;
    try
    {
      Hashtable elements = new Hashtable ();
      for (int i = 0; i < CONTENTS.length; i++)
      {
        Object[] row = CONTENTS[i];
        lastKey = row[0];
        elements.put (row[0], row[1]);
      }
      getIcon ("com/jrefinery/report/resources/SaveAs16.gif");
    }
    catch (Exception e)
    {
      e.printStackTrace ();
      Log.debug ("LastKey read: " + lastKey);
    }
    System.exit (0);
  }

  /** The resources. */
  private static JFreeReportResources res = new JFreeReportResources ();

  /**
   * Returns an array of localised resources.
   *
   * @return an array of localised resources.
   */
  public Object[][] getContents ()
  {
    return CONTENTS;
  }

  /**
   * Prints all defined resource bundle keys and their assigned values.
   */
  public void printAll ()
  {
    Object[][] c = getContents();
    for (int i = 0; i < c.length; i++)
    {
      Object[] cc = c[i];
      System.out.print(cc[0]);
      System.out.print("=");
      System.out.println(cc[1]);

    }
  }
  /**
   * Creates a transparent image.  These can be used for aligning menu items.
   *
   * @param width  the width.
   * @param height  the height.
   *
   * @return the image.
   */
  public static BufferedImage createTransparentImage (int width, int height)
  {
    BufferedImage img = new BufferedImage (width, height, BufferedImage.TYPE_INT_ARGB);
    int[] data = img.getRGB(0, 0, width, height, null, 0, width);
    Arrays.fill(data, 0xff000000);
    img.setRGB(0, 0, width, height, data, 0, width);
    return img;
  }

  /**
   * Attempts to load an image from classpath. If this fails, an empty
   * image icon is returned.
   *
   * @param filename  the file name.
   *
   * @return the image icon.
   */
  public static ImageIcon getIcon (String filename)
  {

    URL in = res.getClass ().getClassLoader ().getResource (filename);
    if (in == null)
    {
      Log.warn ("Unable to find file in the class path: " + filename);
      return new ImageIcon (createTransparentImage(1, 1));
    }
    Image img = Toolkit.getDefaultToolkit ().createImage (in);
    if (img == null)
    {
      Log.warn ("Unable to instantiate the image: " + filename);
      return new ImageIcon (createTransparentImage(1, 1));
    }
    return new ImageIcon (img);
  }

  /** The resources to be localised. */
  private static final Object[][] CONTENTS =
          {
            {"project.name", "JFreeReport"},
            {"project.version", "0.8.1"},
            {"project.info", "http://www.object-refinery.com/jfreereport/index.html"},
            {"project.copyright",
                "(C)opyright 2000-2002, by Simba Management Limited and Contributors"},

            {"action.save-as.name", "Save As PDF..."},
            {"action.save-as.description", "Save to PDF format"},
            {"action.save-as.mnemonic", new Integer (KeyEvent.VK_A)},
            {"action.save-as.accelerator", KeyStroke.getKeyStroke ("control S")},
            {"action.save-as.small-icon", getIcon ("com/jrefinery/report/resources/SaveAs16.gif")},
            {"action.save-as.icon", getIcon ("com/jrefinery/report/resources/SaveAs24.gif")},

            {"action.export-to-excel.name", "Export to Excel..."},
            {"action.export-to-excel.description", "Save to MS-Excel format"},
            {"action.export-to-excel.mnemonic", new Integer (KeyEvent.VK_E)},
            {"action.export-to-excel.accelerator", KeyStroke.getKeyStroke ("control E")},
            // temporarily using the same icon as "Save to PDF", till we have a better one
            {"action.export-to-excel.small-icon", 
                getIcon ("com/jrefinery/report/resources/SaveAs16.gif")},
            {"action.export-to-excel.icon", 
                getIcon ("com/jrefinery/report/resources/SaveAs24.gif")},

            {"action.export-to-html.name", "Export to html..."},
            {"action.export-to-html.description", "Save to HTML format"},
            {"action.export-to-html.mnemonic", new Integer (KeyEvent.VK_H)},
            {"action.export-to-html.accelerator", KeyStroke.getKeyStroke ("control H")},
            // temporarily using the same icon as "Save to PDF", till we have a better one
            {"action.export-to-html.small-icon", 
                getIcon ("com/jrefinery/report/resources/SaveAs16.gif")},
            {"action.export-to-html.icon", 
                getIcon ("com/jrefinery/report/resources/SaveAs24.gif")},

            {"action.export-to-csv.name", "Export to CSV..."},
            {"action.export-to-csv.description", "Save to CSV format"},
            {"action.export-to-csv.mnemonic", new Integer (KeyEvent.VK_C)},
            {"action.export-to-csv.accelerator", KeyStroke.getKeyStroke ("control C")},
            // temporarily using the same icon as "Save to PDF", till we have a better one
            {"action.export-to-csv.small-icon", 
                getIcon ("com/jrefinery/report/resources/SaveAs16.gif")},
            {"action.export-to-csv.icon", 
                getIcon ("com/jrefinery/report/resources/SaveAs24.gif")},

            {"action.export-to-plaintext.name", "Save as text file..."},
            {"action.export-to-plaintext.description", "Save to PlainText format"},
            {"action.export-to-plaintext.mnemonic", new Integer (KeyEvent.VK_T)},
            {"action.export-to-plaintext.accelerator", KeyStroke.getKeyStroke ("control T")},
            // temporarily using the same icon as "Save to PDF", till we have a better one
            {"action.export-to-plaintext.small-icon", 
                getIcon ("com/jrefinery/report/resources/SaveAs16.gif")},
            {"action.export-to-plaintext.icon", 
                getIcon ("com/jrefinery/report/resources/SaveAs24.gif")},

            {"action.page-setup.name", "Page Setup"},
            {"action.page-setup.description", "Page Setup"},
            {"action.page-setup.mnemonic", new Integer (KeyEvent.VK_G)},
            {"action.page-setup.small-icon",
                getIcon ("com/jrefinery/report/resources/PageSetup16.gif")},
            {"action.page-setup.icon", getIcon ("com/jrefinery/report/resources/PageSetup24.gif")},

            {"action.print.name", "Print..."},
            {"action.print.description", "Print report"},
            {"action.print.mnemonic", new Integer (KeyEvent.VK_P)},
            {"action.print.accelerator", KeyStroke.getKeyStroke ("control P")},
            {"action.print.small-icon", getIcon ("com/jrefinery/report/resources/Print16.gif")},
            {"action.print.icon", getIcon ("com/jrefinery/report/resources/Print24.gif")},

            {"action.close.name", "Close"},
            {"action.close.description", "Close print preview window"},
            {"action.close.mnemonic", new Integer (KeyEvent.VK_C)},
            {"action.close.accelerator", KeyStroke.getKeyStroke ("control X")},

            {"action.gotopage.name", "Go to page ..."},
            {"action.gotopage.description", "View a page directly"},
            {"action.gotopage.mnemonic", new Integer (KeyEvent.VK_G)},
            {"action.gotopage.accelerator", KeyStroke.getKeyStroke ("control G")},

            {"dialog.gotopage.message", "Enter a page number"},
            {"dialog.gotopage.title", "Go to page"},

            {"action.about.name", "About..."},
            {"action.about.description", "Information about the application"},
            {"action.about.mnemonic", new Integer (KeyEvent.VK_A)},
            {"action.about.small-icon", getIcon ("com/jrefinery/report/resources/About16.gif")},
            {"action.about.icon", getIcon ("com/jrefinery/report/resources/About24.gif")},

            {"action.firstpage.name", "Home"},
            {"action.firstpage.mnemonic", new Integer (KeyEvent.VK_HOME)},
            {"action.firstpage.description", "Switch to the first page"},
            {"action.firstpage.small-icon",
                getIcon ("com/jrefinery/report/resources/FirstPage16.gif")},
            {"action.firstpage.icon", getIcon ("com/jrefinery/report/resources/FirstPage24.gif")},
            {"action.firstpage.accelerator", KeyStroke.getKeyStroke (KeyEvent.VK_HOME, 0)},

            {"action.back.name", "Back"},
            {"action.back.description", "Switch to the previous page"},
            {"action.back.mnemonic", new Integer (KeyEvent.VK_PAGE_UP)},
            {"action.back.small-icon", getIcon ("com/jrefinery/report/resources/Back16.gif")},
            {"action.back.icon", getIcon ("com/jrefinery/report/resources/Back24.gif")},
            {"action.back.accelerator", KeyStroke.getKeyStroke ("PAGE_UP")},

            {"action.forward.name", "Forward"},
            {"action.forward.description", "Switch to the next page"},
            {"action.forward.mnemonic", new Integer (KeyEvent.VK_PAGE_DOWN)},
            {"action.forward.small-icon",
                getIcon ("com/jrefinery/report/resources/Forward16.gif")},
            {"action.forward.icon", getIcon ("com/jrefinery/report/resources/Forward24.gif")},
            {"action.forward.accelerator", KeyStroke.getKeyStroke ("PAGE_DOWN")},

            {"action.lastpage.name", "End"},
            {"action.lastpage.description", "Switch to the last page"},
            {"action.lastpage.mnemonic", new Integer (KeyEvent.VK_END)},
            {"action.lastpage.small-icon",
                getIcon ("com/jrefinery/report/resources/LastPage16.gif")},
            {"action.lastpage.icon", getIcon ("com/jrefinery/report/resources/LastPage24.gif")},
            {"action.lastpage.accelerator", KeyStroke.getKeyStroke (KeyEvent.VK_END, 0)},

            {"action.zoomIn.name", "Zoom In"},
            {"action.zoomIn.description", "Increase zoom"},
            {"action.zoomIn.mnemonic", new Integer (KeyEvent.VK_PLUS)},
            {"action.zoomIn.small-icon", getIcon ("com/jrefinery/report/resources/ZoomIn16.gif")},
            {"action.zoomIn.icon", getIcon ("com/jrefinery/report/resources/ZoomIn24.gif")},
            {"action.zoomIn.accelerator", KeyStroke.getKeyStroke ("PLUS")},

            {"action.zoomOut.name", "Zoom Out"},
            {"action.zoomOut.description", "Decrease Zoom"},
            {"action.zoomOut.mnemonic", new Integer (KeyEvent.VK_MINUS)},
            {"action.zoomOut.small-icon",
                getIcon ("com/jrefinery/report/resources/ZoomOut16.gif")},
            {"action.zoomOut.icon", getIcon ("com/jrefinery/report/resources/ZoomOut24.gif")},
            {"action.zoomOut.accelerator", KeyStroke.getKeyStroke ("MINUS")},

            // preview frame...
            {"preview-frame.title", "Print Preview"},

            // menu labels...
            {"menu.file.name", "File"},
            {"menu.file.mnemonic", new Character ('F')},

            {"menu.navigation.name", "Navigation"},
            {"menu.navigation.mnemonic", new Character ('N')},

            {"menu.zoom.name", "Zoom"},
            {"menu.zoom.mnemonic", new Character ('Z')},

            {"menu.help.name", "Help"},
            {"menu.help.mnemonic", new Character ('H')},

            {"file.save.pdfdescription", "PDF documents"},
            {"statusline.pages", "Page {0} of {1}"},
            {"statusline.error", "Reportgeneration produced an error: {0}"},
            {"statusline.repaginate", "Calculating pagebreaks, please wait."},
            {"error.processingfailed.title", "Report processing failed"},
            {"error.processingfailed.message", "Error on processing this report: {0}"},
            {"error.savefailed.message", "Error on saving the PDF file: {0}"},
            {"error.savefailed.title", "Error on save"},
            {"error.printfailed.message", "Error on printing the report: {0}"},
            {"error.printfailed.title", "Error on printing"},
            {"error.validationfailed.message", "Error while validating the user input."},
            {"error.validationfailed.title", "Error on validation"},

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
             + "able to change security constraints. Continue anyway?" },

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

            {"convertdialog.targetIsEmpty", "The target file is not specified"},
            {"convertdialog.errorTitle", "Error"},
            {"convertdialog.targetIsNoFile", "The specified target file is no ordinary file." },
            {"convertdialog.targetIsNotWritable", "The specified target file is not writable."},
            {"convertdialog.targetOverwriteConfirmation",
                "The file ''{0}'' exists. Overwrite it?"},
            {"convertdialog.targetOverwriteTitle", "Overwrite file?"},
            {"convertdialog.targetFile", "Target file"},
            {"convertdialog.sourceIsEmpty", "The source file is not specified"},
            {"convertdialog.sourceIsNoFile", "The specified source file is no ordinary file."},
            {"convertdialog.sourceIsNotReadable", "The source file is not readable."},
            {"convertdialog.sourceFile", "Source file"},

            {"convertdialog.action.selectTarget.name", "Select"},
            {"convertdialog.action.selectTarget.description", "Select target file."},
            {"convertdialog.action.selectSource.name", "Select"},
            {"convertdialog.action.selectSource.description", "Select source file."},
            {"convertdialog.action.convert.name", "Convert"},
            {"convertdialog.action.convert.description", "Convert the source files."},

            {"convertdialog.title", "Report-Converter"},
          };

}