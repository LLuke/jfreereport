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
 * $Id: DemoResources_de.java,v 1.5 2002/06/05 21:32:49 taqua Exp $
 *
 * Changes
 * -------
 * 27-Mar-2002 : Version 1 (DG);
 * 16-May-2002 : Line delimiters adjusted and load icons from jar (JS)
 *
 */
package com.jrefinery.report.demo.resources;

import com.jrefinery.report.resources.JFreeReportResources;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

/**
 * User interface items for the JFreeReport demonstration application.  These have been put into
 * a ResourceBundle to ease localisation of the application.
 */
public class DemoResources_de extends JFreeReportResources
{
  /**
   * Returns the contents of the resource bundle.
   */
  public Object[][] getContents ()
  {
    return contents;
  }

  /** The resources to be localised. */
  private static final Object[][] contents = {
    // in the title pattern, leave in the '{0}' as it gets replaced with the version number
    {"main-frame.title.pattern", "JFreeReport {0} Demo"},

    {"action.close.name", "Schliessen"},
    {"action.close.description", "Beendet JFreeReportDemo"},
    {"action.close.mnemonic", new Integer (KeyEvent.VK_B)},
    {"action.close.accelerator", KeyStroke.getKeyStroke (KeyEvent.VK_X, KeyEvent.CTRL_MASK)},

    {"action.print-preview.name", "Seitenansicht ..."},
    {"action.print-preview.description", "Den Bericht in der Seitenansicht betrachten."},
    {"action.print-preview.mnemonic", new Integer (KeyEvent.VK_P)},
    {"action.print-preview.accelerator", KeyStroke.getKeyStroke (KeyEvent.VK_P, KeyEvent.CTRL_MASK)},

    {"action.about.name", "Über..."},
    {"action.about.description", "Informationen über JFreeReport"},
    {"action.about.mnemonic", new Integer (KeyEvent.VK_A)},

    {"menu.file.name", "Demo"},
    {"menu.file.mnemonic", new Character ('D')},
    {"menu.help.name", "Hilfe"},
    {"menu.help.mnemonic", new Character ('H')},

    {"exitdialog.title", "Programmende"},
    {"exitdialog.message", "Möchten Sie das Programm beenden?"},

    {"report.definitionnotfound", "ReportDefinition {0} wurde nicht im ClassPath gefunden."},
    {"report.definitionfailure.message", "Reportdefinition {0} konnte nicht geladen werden."},
    {"report.definitionfailure.title", "Das Laden der Berichtdefinition schlug fehl."},
    {"report.definitionnull", "Es wurde keine Reportdefinition erzeugt"},
    {"error", "Fehler" },
    {"example", "Beispiel {0}"}

  };
}
