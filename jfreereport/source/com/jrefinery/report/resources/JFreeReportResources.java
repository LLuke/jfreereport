package com.jrefinery.report.resources;

import com.jrefinery.report.util.Log;
import com.jrefinery.report.targets.G2OutputTarget;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ListResourceBundle;
import java.net.URL;

public class JFreeReportResources extends ListResourceBundle
{

  private static JFreeReportResources res = new JFreeReportResources();
  /**
   * Returns the array of strings in the resource bundle.
   */
  public Object[][] getContents ()
  {
    return contents;
  }

  /** The resources to be localised. */
  private static final Object[][] contents = 
    {
      { "project.name", "JFreeReport"},
      { "project.version", "0.7.2"},
      { "project.info", "http://www.object-refinery.com/jfreereport/index.html"},
      { "project.copyright", "(C)opyright 2000-2002, by Simba Management Limited and Contributors"},
  
      { "action.save-as.name", "Save As..."},
      { "action.save-as.description", "Save to PDF format"},
      { "action.save-as.mnemonic", new Integer (KeyEvent.VK_A)},
      { "action.save-as.accelerator", KeyStroke.getKeyStroke (KeyEvent.VK_A, KeyEvent.CTRL_MASK)},
      { "action.save-as.small-icon", getIcon("com/jrefinery/report/resources/SaveAs16.gif")},
      { "action.save-as.icon", getIcon("com/jrefinery/report/resources/SaveAs24.gif")},
  
      { "action.page-setup.name", "Page Setup"},
      { "action.page-setup.description", "Page Setup"},
      { "action.page-setup.mnemonic", new Integer (KeyEvent.VK_G)},
      { "action.page-setup.small-icon", getIcon("com/jrefinery/report/resources/PageSetup16.gif")},
      { "action.page-setup.icon", getIcon("com/jrefinery/report/resources/PageSetup24.gif")},
//      { "action.page-setup.accelerator", KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK)},
  
      { "action.print.name", "Print..."},
      { "action.print.description", "Print report"},
      { "action.print.mnemonic", new Integer (KeyEvent.VK_P)},
      { "action.print.accelerator", KeyStroke.getKeyStroke (KeyEvent.VK_P, KeyEvent.CTRL_MASK)},
      { "action.print.small-icon", getIcon("com/jrefinery/report/resources/Print16.gif")},
      { "action.print.icon", getIcon("com/jrefinery/report/resources/Print24.gif")},
  
      { "action.close.name", "Close"},
      { "action.close.description", "Close print preview window"},
      { "action.close.mnemonic", new Integer(KeyEvent.VK_C) },
      { "action.close.accelerator", KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK) },

      { "action.about.name", "About..."},
      { "action.about.description", "Information about the application"},
      { "action.about.mnemonic", new Integer (KeyEvent.VK_A)},
      { "action.about.small-icon", getIcon("com/jrefinery/report/resources/About16.gif")},
      { "action.about.icon", getIcon("com/jrefinery/report/resources/About24.gif")},

      { "action.back.name", "Back"},
      { "action.back.mnemonic", new Integer (KeyEvent.VK_PAGE_UP)},
      { "action.back.description", "Switch to the previous page"},
      { "action.back.small-icon", getIcon("com/jrefinery/report/resources/Back16.gif") },
      { "action.back.icon", getIcon("com/jrefinery/report/resources/Back24.gif") },

      { "action.forward.name", "Forward"},
      { "action.forward.description", "Switch to the next page"},
      { "action.forward.mnemonic", new Integer (KeyEvent.VK_PAGE_DOWN)},
      { "action.forward.small-icon", getIcon("com/jrefinery/report/resources/Forward16.gif") },
      { "action.forward.icon", getIcon("com/jrefinery/report/resources/Forward24.gif") },

      { "action.zoomIn.name", "Zoom In"},
      { "action.zoomIn.description", "Increase zoom"},
      { "action.zoomIn.mnemonic", new Integer (KeyEvent.VK_PLUS)},
      { "action.zoomIn.small-icon", getIcon("com/jrefinery/report/resources/ZoomIn16.gif") },
      { "action.zoomIn.icon", getIcon("com/jrefinery/report/resources/ZoomIn24.gif") },

      { "action.zoomOut.name", "Zoom Out"},
      { "action.zoomOut.description", "Decrease Zoom"},
      { "action.zoomOut.mnemonic", new Integer (KeyEvent.VK_MINUS)},
      { "action.zoomOut.small-icon", getIcon("com/jrefinery/report/resources/ZoomOut16.gif") },
      { "action.zoomOut.icon", getIcon("com/jrefinery/report/resources/ZoomOut24.gif") },

      { "action.information.name", "Information"},
      { "action.information.mnemonic", new Integer (0)},
      { "action.information.description", "Informations about JFreeReport"},
      { "action.information.small-icon", getIcon("com/jrefinery/report/resources/Information16.gif") },
      { "action.information.icon", getIcon("com/jrefinery/report/resources/Information24.gif") },

    // preview frame...
      { "preview-frame.title", "Print Preview"},
  
    // menu labels...
      { "menu.file.name", "File"},
      { "menu.file.mnemonic", new Character ('F')},
  
      { "menu.help.name", "Help"},
      { "menu.help.mnemonic", new Character ('H')},
    };
  
  /**
   * Attempts to load an image from classpath. If this failes, an empty
   * image icon is returned.
   */
  public static ImageIcon getIcon(String filename)
  {

    URL in = ClassLoader.getSystemResource(filename);
    if (in == null)
    {
      Log.debug ("Unable to load file: " + filename);
      return new ImageIcon(new BufferedImage (BufferedImage.TYPE_INT_RGB, 1, 1));
    }
    Image img = Toolkit.getDefaultToolkit().getImage(in);
    return new ImageIcon(img);
  }
}
