/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 *
 * $Id: JFreeReportResources.java,v 1.25 2002/11/07 21:45:28 taqua Exp $
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
import java.util.Hashtable;
import java.util.ListResourceBundle;

/**
 * English Language Resources.
 *
 * @author TM
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
      Object o = getIcon ("com/jrefinery/report/resources/SaveAs16.gif");
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
      Log.debug ("Unable to load file: " + filename);
      return new ImageIcon (new BufferedImage (BufferedImage.TYPE_INT_RGB, 1, 1));
    }
    Image img = Toolkit.getDefaultToolkit ().createImage (in);
    if (img == null)
    {
      Log.warn ("Unable to instantiate the image " + filename);
      return new ImageIcon (new BufferedImage (BufferedImage.TYPE_INT_RGB, 1, 1));
    }
    return new ImageIcon (img);
  }

  /** The resources to be localised. */
  private static final Object[][] CONTENTS =
          {
            {"project.name", "JFreeReport"},
            {"project.version", "0.7.6"},
            {"project.info", "http://www.object-refinery.com/jfreereport/index.html"},
            {"project.copyright",
                "(C)opyright 2000-2002, by Simba Management Limited and Contributors"},

            {"action.save-as.name", "Save As..."},
            {"action.save-as.description", "Save to PDF format"},
            {"action.save-as.mnemonic", new Integer (KeyEvent.VK_A)},
            {"action.save-as.accelerator", KeyStroke.getKeyStroke ("control S")},
            {"action.save-as.small-icon", getIcon ("com/jrefinery/report/resources/SaveAs16.gif")},
            {"action.save-as.icon", getIcon ("com/jrefinery/report/resources/SaveAs24.gif")},

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

            {"menu.help.name", "Help"},
            {"menu.help.mnemonic", new Character ('H')},

            {"file.save.pdfdescription", "PDF documents"},
            {"statusline.pages", "Page {0} of {1}"},
            {"statusline.error", "Reportgeneration produced an error: {0}"},
            {"error.processingfailed.title", "Report processing failed"},
            {"error.processingfailed.message", "Error on processing this report: {0}"},
            {"error.savefailed.message", "Error on saving the PDF file: {0}"},
            {"error.savefailed.title", "Error on save"},
            {"error.printfailed.message", "Error on printing the report: {0}"},
            {"error.printfailed.title", "Error on printing"},

            {"pdfsavedialog.dialogtitle", "Saving Report into a PDF-File ..."},
            {"pdfsavedialog.filename", "Filename"},
            {"pdfsavedialog.author", "Author"},
            {"pdfsavedialog.title", "Title"},
            {"pdfsavedialog.selectFile", "Select File"},
            {"pdfsavedialog.security", "Security Settings and Encryption"},

            {"pdfsavedialog.securityNone", "No Security"},
            {"pdfsavedialog.security40bit", "Encrypt with 40 bit keys"},
            {"pdfsavedialog.security128bit", "Encrypt with 128 bit keys"},
            {"pdfsavedialog.userpassword", "User Password"},
            {"pdfsavedialog.userpasswordconfirm", "Confirm"},
            {"pdfsavedialog.userpasswordNoMatch", "The user-passwords do not match."},
            {"pdfsavedialog.ownerpassword", "Owner Password"},
            {"pdfsavedialog.ownerpasswordconfirm", "Confirm"},
            {"pdfsavedialog.ownerpasswordNoMatch", "The owner-passwords do not match."},
            {"pdfsavedialog.ownerpasswordEmpty", "The owner-password is empty. Users may be " +
              "able to change security constraints. Continue anyway?" },

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

}