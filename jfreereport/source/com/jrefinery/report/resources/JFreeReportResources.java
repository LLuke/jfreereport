package com.jrefinery.report.resources;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.util.ListResourceBundle;

public class JFreeReportResources extends ListResourceBundle
{

  /**
   * Returns the array of strings in the resource bundle.
   */
  public Object[][] getContents ()
  {
    return contents;
  }

  /** The resources to be localised. */
  private static final Object[][] contents = {

    {"project.name", "JFreeReport"},
    {"project.version", "0.7.1"},
    {"project.info", "http://www.object-refinery.com/jfreereport/index.html"},
    {"project.copyright", "(C)opyright 2000-2002, by Simba Management Limited and Contributors"},

    {"action.save-as.name", "Save As..."},
    {"action.save-as.description", "Save to PDF format"},
    {"action.save-as.mnemonic", new Integer (KeyEvent.VK_A)},
    {"action.save-as.accelerator", KeyStroke.getKeyStroke (KeyEvent.VK_A, KeyEvent.CTRL_MASK)},

    {"action.page-setup.name", "Page Setup"},
    {"action.page-setup.description", "Page Setup"},
    {"action.page-setup.mnemonic", new Integer (KeyEvent.VK_G)},
//        {"action.page-setup.accelerator", KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK)},

    {"action.print.name", "Print..."},
    {"action.print.description", "Print report"},
    {"action.print.mnemonic", new Integer (KeyEvent.VK_P)},
    {"action.print.accelerator", KeyStroke.getKeyStroke (KeyEvent.VK_P, KeyEvent.CTRL_MASK)},

    {"action.close.name", "Close"},
    {"action.close.description", "Close print preview window"},
    {"action.close.mnemonic", new Integer (KeyEvent.VK_C)},

    {"action.about.name", "About..."},
    {"action.about.description", "Information about the application"},
    {"action.about.mnemonic", new Integer (KeyEvent.VK_A)},

    // preview frame...
    {"preview-frame.title", "Print Preview"},

    // menu labels...
    {"menu.file.name", "File"},
    {"menu.file.mnemonic", new Character ('F')},

    {"menu.file.exit", "Exit"},
    {"menu.file.exit.mnemonic", new Character ('x')},

    {"menu.help.name", "Help"},
    {"menu.help.mnemonic", new Character ('H')},

    {"menu.help.about.name", "About..."},
    {"menu.help.about.mnemonic", new Character ('A')},

    // dialog messages...
    {"dialog.exit.title", "Confirm exit..."},
    {"dialog.exit.message", "Are you sure you want to exit?"},

  };

}