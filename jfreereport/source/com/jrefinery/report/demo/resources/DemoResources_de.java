/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com);
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
 * ------------------
 * DemoResources.java
 * ------------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: DemoResources.java,v 1.1 2002/05/07 14:19:15 mungady Exp $
 *
 * Changes
 * -------
 * 27-Mar-2002 : Version 1 (DG);
 *
 */

package com.jrefinery.report.demo.resources;

import java.awt.event.KeyEvent;
import java.util.ListResourceBundle;
import javax.swing.KeyStroke;

/**
 * User interface items for the JFreeReport demonstration application.  These have been put into
 * a ResourceBundle to ease localisation of the application.
 */
public class DemoResources_de extends ListResourceBundle {

    /**
     * Returns the contents of the resource bundle.
     */
    public Object[][] getContents() {
        return contents;
    }

    /** The resources to be localised. */
    static final Object[][] contents = {

        { "project.name",      "JFreeReport" },
        { "project.version",   "0.7.2"},
        { "project.info",      "http://www.object-refinery.com/jfreereport/index.html" },
        { "project.copyright", "(C)opyright 2000-2002, by Simba Management Limited and Contributors" },

        // in the title pattern, leave in the '{0}' as it gets replaced with the version number
        { "main-frame.title.pattern", "JFreeReport {0} Demo" },

        {"action.print-preview.name",        "Seitenansicht ..."},
        {"action.print-preview.description", "Den Bericht in der Seitenansicht betrachten."},
        {"action.print-preview.mnemonic",    new Integer(KeyEvent.VK_P)},
        {"action.print-preview.accelerator", KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK)},

        {"action.about.name",        "�ber..."},
        {"action.about.description", "Informationen �ber JFreeReport"},
        {"action.about.mnemonic",    new Integer(KeyEvent.VK_A)},
//      {"action.print-preview.accelerator", KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK)},

        { "menu.file.name",     "Demo"},
        { "menu.file.mnemonic", new Character('D') },

        { "menu.help.name",     "Hilfe"},
        { "menu.help.mnemonic", new Character('H') },

        {"action.close.name",        "Schliessen"},
        {"action.close.description", "Beendet JFreeReportDemo"},
        {"action.close.mnemonic",    new Integer(KeyEvent.VK_B)},
        {"action.close.accelerator", KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK)},

    };


}