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
 * ---------------------
 * DemoResources_de.java
 * ---------------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DemoResources_de.java,v 1.12 2003/06/16 16:06:56 taqua Exp $
 *
 * Changes
 * -------
 * 27-Mar-2002 : Version 1 (DG);
 * 16-May-2002 : Line delimiters adjusted and load icons from jar (JS)
 *
 */
package com.jrefinery.report.demo.resources;

import java.awt.event.KeyEvent;

import com.jrefinery.report.resources.JFreeReportResources;

/**
 * User interface items for the JFreeReport demonstration application.  These have been put into
 * a ResourceBundle to ease localisation of the application.
 *
 * @author TM
 */
public class DemoResources_de extends JFreeReportResources
{
  /**
   * Returns the contents of the resource bundle.
   *
   * @return an array of localised resources.
   */
  public Object[][] getContents ()
  {
    return CONTENTS;
  }

  /** The resources to be localised. */
  private static final Object[][] CONTENTS = {
    // in the title pattern, leave in the '{0}' as it gets replaced with the version number
    {"main-frame.title.pattern", "JFreeReport {0} Demo"},

    {"action.close.name", "Schliessen"},
    {"action.close.description", "Beendet JFreeReportDemo"},
    {"action.close.mnemonic", new Integer (KeyEvent.VK_B)},

    {"action.print-preview.name", "Seitenansicht ..."},
    {"action.print-preview.description", "Den Bericht in der Seitenansicht betrachten."},
    {"action.print-preview.mnemonic", new Integer (KeyEvent.VK_P)},

    {"action.about.name", "�ber..."},
    {"action.about.description", "Informationen �ber JFreeReport"},
    {"action.about.mnemonic", new Integer (KeyEvent.VK_A)},

    {"menu.file.name", "Demo"},
    {"menu.file.mnemonic", new Character ('D')},
    {"menu.help.name", "Hilfe"},
    {"menu.help.mnemonic", new Character ('H')},

    {"exitdialog.title", "Programmende"},
    {"exitdialog.message", "M�chten Sie das Programm beenden?"},

    {"report.definitionnotfound", "ReportDefinition {0} wurde nicht im ClassPath gefunden."},
    {"report.definitionfailure.message", "Reportdefinition konnte nicht geladen werden. {0} "},
    {"report.definitionfailure.title", "Das Laden der Berichtdefinition schlug fehl."},
    {"report.definitionnull", "Es wurde keine Reportdefinition erzeugt"},
    {"error", "Fehler"},
    {"example", "Beispiel {0}"}

  };
}
