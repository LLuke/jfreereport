package com.jrefinery.report.resources;

import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import java.util.ListResourceBundle;

public class JFreeReportResources_de extends ListResourceBundle {

    /**
     * Returns the array of strings in the resource bundle.
     */
    public Object[][] getContents() {
        return contents;
    }

    /** The resources to be localised. */
    private static final Object[][] contents = {

        {"project.name",      "JFreeReport"},
        {"project.version",   "0.7.2"},
        {"project.info",      "http://www.object-refinery.com/jfreereport/index.html"},
        {"project.copyright", "(C)opyright 2000-2002, by Simba Management Limited and Contributors"},

        {"action.save-as.name",        "Speichern unter..."},
        {"action.save-as.description", "Speichert den Bericht als PDF-Datei"},
        {"action.save-as.mnemonic",    new Integer(KeyEvent.VK_S)},
        {"action.save-as.accelerator", KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK)},

        {"action.page-setup.name",        "Seite einrichten"},
        {"action.page-setup.description", "Seite einrichten"},
        {"action.page-setup.mnemonic",    new Integer(KeyEvent.VK_E)},
//        {"action.page-setup.accelerator", KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK)},

        {"action.print.name",        "Drucken..."},
        {"action.print.description", "Druckt den Bericht"},
        {"action.print.mnemonic",    new Integer(KeyEvent.VK_D)},
        {"action.print.accelerator", KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK)},

        {"action.close.name",        "Schliessen"},
        {"action.close.description", "Beendet die Druckvorschau"},
        {"action.close.mnemonic",    new Integer(KeyEvent.VK_B)},
        {"action.close.accelerator", KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK)},

        {"action.about.name",        "Über..."},
        {"action.about.description", "Informationen über JFreeReport"},
        {"action.about.mnemonic",    new Integer(KeyEvent.VK_A)},

        // preview frame...
        {"preview-frame.title", "Druckvorschau"},

        // menu labels...
        {"menu.file.name", "Datei"},
        {"menu.file.mnemonic", new Character('D')},

        {"menu.file.exit", "Schliessen"},
        {"menu.file.exit.mnemonic", new Character('x')},

        {"menu.help.name", "Hilfe"},
        {"menu.help.mnemonic", new Character('H')},

        // dialog messages...
        {"dialog.exit.title", "Druckvorschau schliessen..."},
        {"dialog.exit.message", "Soll die Seitenansicht beendet werden?"},

    };

}