package com.jrefinery.report.resources;

import com.jrefinery.report.util.Log;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Hashtable;
import java.util.ListResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

public class JFreeReportResources extends ListResourceBundle
{
  public JFreeReportResources()
  {
  }

  /**
   * Used to test the resourcebundle for null values
   */
  public static void main(String[] args)
  {
    Object lastKey = null;
    try
    {
      Hashtable elements = new Hashtable();
      for (int i = 0; i < contents.length; i++)
      {
        Object[] row = contents[i];
        System.out.println(row[0] + " " + row[1]);
        lastKey = row[0];
        elements.put(row[0], row[1]);
      }
      Object o = getIcon("com/jrefinery/report/resources/SaveAs16.gif");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.out.println(lastKey);
    }
    System.exit(0);
  }

  private static JFreeReportResources res = new JFreeReportResources();

  public Object[][] getContents()
  {
    return contents;
  }

  /**
   * Attempts to load an image from classpath. If this failes, an empty
   * image icon is returned.
   */
  public static ImageIcon getIcon(String filename)
  {

    URL in = res.getClass().getClassLoader().getResource(filename);
    if (in == null)
    {
      Log.debug ("Unable to load file: " + filename);
      return new ImageIcon(new BufferedImage (BufferedImage.TYPE_INT_RGB, 1, 1));
    }
    Image img = Toolkit.getDefaultToolkit().getImage(in);
    return new ImageIcon(img);
  }

  /** The resources to be localised. */
  private static final Object[][] contents =
    {
      { "project.name", "JFreeReport"},
      { "project.version", "0.7.5"},
      { "project.info", "http://www.object-refinery.com/jfreereport/index.html"},
      { "project.copyright", "(C)opyright 2000-2002, by Simba Management Limited and Contributors"},

      { "action.save-as.name", "Save As..."},
      { "action.save-as.description", "Save to PDF format"},
      { "action.save-as.mnemonic", new Integer (KeyEvent.VK_A)},
      { "action.save-as.accelerator", KeyStroke.getKeyStroke ("control S")},
      { "action.save-as.small-icon", getIcon("com/jrefinery/report/resources/SaveAs16.gif")},
      { "action.save-as.icon", getIcon("com/jrefinery/report/resources/SaveAs24.gif")},

      { "action.page-setup.name", "Page Setup"},
      { "action.page-setup.description", "Page Setup"},
      { "action.page-setup.mnemonic", new Integer (KeyEvent.VK_G)},
      { "action.page-setup.small-icon", getIcon("com/jrefinery/report/resources/PageSetup16.gif")},
      { "action.page-setup.icon", getIcon("com/jrefinery/report/resources/PageSetup24.gif")},

      { "action.print.name", "Print..."},
      { "action.print.description", "Print report"},
      { "action.print.mnemonic", new Integer (KeyEvent.VK_P)},
      { "action.print.accelerator", KeyStroke.getKeyStroke ("control P")},
      { "action.print.small-icon", getIcon("com/jrefinery/report/resources/Print16.gif")},
      { "action.print.icon", getIcon("com/jrefinery/report/resources/Print24.gif")},

      { "action.close.name", "Close"},
      { "action.close.description", "Close print preview window"},
      { "action.close.mnemonic", new Integer(KeyEvent.VK_C) },
      { "action.close.accelerator", KeyStroke.getKeyStroke("control X") },

      { "action.gotopage.name", "Go to page ..."},
      { "action.gotopage.description", "View a page directly"},
      { "action.gotopage.mnemonic", new Integer(KeyEvent.VK_G) },
      { "action.gotopage.accelerator", KeyStroke.getKeyStroke("control G") },

      { "dialog.gotopage.message", "Enter a page number"},
      { "dialog.gotopage.title", "Go to page"},

      { "action.about.name", "About..."},
      { "action.about.description", "Information about the application"},
      { "action.about.mnemonic", new Integer (KeyEvent.VK_A)},
      { "action.about.small-icon", getIcon("com/jrefinery/report/resources/About16.gif")},
      { "action.about.icon", getIcon("com/jrefinery/report/resources/About24.gif")},

      { "action.firstpage.name", "Home"},
      { "action.firstpage.mnemonic", new Integer (KeyEvent.VK_HOME)},
      { "action.firstpage.description", "Switch to the first page"},
      { "action.firstpage.small-icon", getIcon("com/jrefinery/report/resources/FirstPage16.gif") },
      { "action.firstpage.icon", getIcon("com/jrefinery/report/resources/FirstPage24.gif") },
      { "action.firstpage.accelerator", KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0) },

      { "action.back.name", "Back"},
      { "action.back.description", "Switch to the previous page"},
      { "action.back.mnemonic", new Integer (KeyEvent.VK_PAGE_UP)},
      { "action.back.small-icon", getIcon("com/jrefinery/report/resources/Back16.gif") },
      { "action.back.icon", getIcon("com/jrefinery/report/resources/Back24.gif") },
      { "action.back.accelerator", KeyStroke.getKeyStroke("PAGE_UP") },

      { "action.forward.name", "Forward"},
      { "action.forward.description", "Switch to the next page"},
      { "action.forward.mnemonic", new Integer (KeyEvent.VK_PAGE_DOWN)},
      { "action.forward.small-icon", getIcon("com/jrefinery/report/resources/Forward16.gif") },
      { "action.forward.icon", getIcon("com/jrefinery/report/resources/Forward24.gif") },
      { "action.forward.accelerator", KeyStroke.getKeyStroke("PAGE_DOWN") },

      { "action.lastpage.name", "End"},
      { "action.lastpage.description", "Switch to the last page"},
      { "action.lastpage.mnemonic", new Integer (KeyEvent.VK_END)},
      { "action.lastpage.small-icon", getIcon("com/jrefinery/report/resources/LastPage16.gif") },
      { "action.lastpage.icon", getIcon("com/jrefinery/report/resources/LastPage24.gif") },
      { "action.lastpage.accelerator", KeyStroke.getKeyStroke(KeyEvent.VK_END, 0) },

      { "action.zoomIn.name", "Zoom In"},
      { "action.zoomIn.description", "Increase zoom"},
      { "action.zoomIn.mnemonic", new Integer (KeyEvent.VK_PLUS)},
      { "action.zoomIn.small-icon", getIcon("com/jrefinery/report/resources/ZoomIn16.gif") },
      { "action.zoomIn.icon", getIcon("com/jrefinery/report/resources/ZoomIn24.gif") },
      { "action.zoomIn.accelerator", KeyStroke.getKeyStroke("PLUS") },

      { "action.zoomOut.name", "Zoom Out"},
      { "action.zoomOut.description", "Decrease Zoom"},
      { "action.zoomOut.mnemonic", new Integer (KeyEvent.VK_MINUS)},
      { "action.zoomOut.small-icon", getIcon("com/jrefinery/report/resources/ZoomOut16.gif") },
      { "action.zoomOut.icon", getIcon("com/jrefinery/report/resources/ZoomOut24.gif") },
      { "action.zoomOut.accelerator", KeyStroke.getKeyStroke("MINUS") },

    // preview frame...
      { "preview-frame.title", "Print Preview"},

    // menu labels...
      { "menu.file.name", "File"},
      { "menu.file.mnemonic", new Character ('F')},

      { "menu.help.name", "Help"},
      { "menu.help.mnemonic", new Character ('H')},

      { "file.save.pdfdescription", "PDF documents" },
      { "statusline.pages", "Page {0} of {1}" },
      { "statusline.error", "Reportgeneration produced an error: {0}" },
      { "error.processingfailed.title", "Report processing failed" },
      { "error.processingfailed.message", "Error on processing this report: {0}" },
      { "error.savefailed.message", "Error on saving the PDF file: {0}" },
      { "error.savefailed.title", "Error on save"},
      { "error.printfailed.message", "Error on printing the report: {0}" },
      { "error.printfailed.title", "Error on printing"},


    };

}