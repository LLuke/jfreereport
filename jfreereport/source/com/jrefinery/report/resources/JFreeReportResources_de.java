package com.jrefinery.report.resources;

import java.awt.event.KeyEvent;
import java.util.ListResourceBundle;

import javax.swing.KeyStroke;

public class JFreeReportResources_de extends JFreeReportResources
{

  /**
   * Returns the array of strings in the resource bundle.
   */

  public Object[][] getContents()
  {
    return contents;
  }

  /** The resources to be localised. */
  private static final Object[][] contents =
    { 
      { "action.save-as.name", "Speichern unter..." },
      { "action.save-as.description", "Speichert den Bericht als PDF-Datei" }, 
      { "action.save-as.mnemonic", new Integer(KeyEvent.VK_S) }, 

      { "action.page-setup.name", "Seite einrichten" }, 
      { "action.page-setup.description", "Seite einrichten" }, 
      { "action.page-setup.mnemonic", new Integer(KeyEvent.VK_E) },

      { "action.print.name", "Drucken..." }, 
      { "action.print.description", "Druckt den Bericht" }, 
      { "action.print.mnemonic", new Integer(KeyEvent.VK_D) }, 

      { "action.close.name", "Schliessen" }, 
      { "action.close.description", "Beendet die Seitenansicht" },
      { "action.close.mnemonic", new Integer(KeyEvent.VK_B) },

      { "action.about.name", "�ber..." }, 
      { "action.about.description", "Informationen �ber JFreeReport" }, 
      { "action.about.mnemonic", new Integer(KeyEvent.VK_A) },

      { "preview-frame.title", "Seitenansicht" },

      { "menu.file.name", "Datei" },
      { "menu.file.mnemonic", new Character('D') }, 

      { "menu.help.name", "Hilfe" },
      { "menu.help.mnemonic", new Character('H') },
  };
}
