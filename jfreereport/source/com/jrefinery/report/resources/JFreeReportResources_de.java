package com.jrefinery.report.resources;

import java.awt.event.KeyEvent;

public class JFreeReportResources_de extends JFreeReportResources
{

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
            {"action.save-as.name", "Speichern als..."},
            {"action.save-as.description", "Speichert den Bericht als PDF-Datei"},
            {"action.save-as.mnemonic", new Integer (KeyEvent.VK_S)},

            {"action.page-setup.name", "Seite einrichten"},
            {"action.page-setup.description", "Seite einrichten"},
            {"action.page-setup.mnemonic", new Integer (KeyEvent.VK_E)},

            {"action.print.name", "Drucken..."},
            {"action.print.description", "Druckt den Bericht"},
            {"action.print.mnemonic", new Integer (KeyEvent.VK_D)},

            {"action.close.name", "Schliessen"},
            {"action.close.description", "Beendet die Seitenansicht"},
            {"action.close.mnemonic", new Integer (KeyEvent.VK_B)},

            {"action.gotopage.name", "Gehe zu ..."},
            {"action.gotopage.description", "Wechselt zu einer bestimmten Seite im Bericht."},

            {"dialog.gotopage.message", "Seitenzahl eingeben"},
            {"dialog.gotopage.title", "Gehe zu Seite ..."},

            {"action.about.name", "Über..."},
            {"action.about.description", "Informationen über JFreeReport"},
            {"action.about.mnemonic", new Integer (KeyEvent.VK_A)},

            {"action.firstpage.name", "Anfang"},
            {"action.firstpage.description", "Gehe zum Start"},

            {"action.lastpage.name", "Ende"},
            {"action.lastpage.description", "Gehe zum Ende"},

            {"action.back.name", "Zurück"},
            {"action.back.description", "Wechselt zur vorherigen Seite"},

            {"action.forward.name", "Nächste"},
            {"action.forward.description", "Wechselt zur nächsten Seite"},

            {"action.zoomIn.name", "Vergrössern"},
            {"action.zoomIn.description", "Zeigt die aktuelle Seite in einem grösseren Masstab an"},

            {"action.zoomOut.name", "Verkleinern"},
            {"action.zoomOut.description", "Zeigt die aktuelle Seite in einem kleineren Masstab an"},

            {"preview-frame.title", "Seitenansicht"},

            {"menu.file.name", "Datei"},
            {"menu.file.mnemonic", new Character ('D')},

            {"menu.help.name", "Hilfe"},
            {"menu.help.mnemonic", new Character ('H')},

            {"file.save.pdfdescription", "PDF Dateien"},
            {"statusline.pages", "Seite {0} von {1}"},
            {"statusline.error", "Die ReportGenerierung ist fehlgeschlagen: {0}"},
            {"error.processingfailed.title", "Fehler beim bearbeiten des Berichtes"},
            {"error.processingfailed.message", "Die Berichtsgenerirung ist fehlgeschlagen: {0}"},
            {"error.savefailed.message", "Der Bericht konnte nicht gespeichert werden: {0}"},
            {"error.savefailed.title", "Speichern fehlgeschlagen"},
            {"error.printfailed.message", "Das Drucken ist fehlgeschlagen: {0}"},
            {"error.printfailed.title", "Druck fehlgeschlagen"},

          };
}
