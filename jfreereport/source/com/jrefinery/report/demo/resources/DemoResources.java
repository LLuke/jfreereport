/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * ------------------
 * DemoResources.java
 * ------------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: DemoResources.java,v 1.12 2002/12/12 12:26:55 mungady Exp $
 *
 * Changes
 * -------
 * 27-Mar-2002 : Version 1 (DG);
 * 16-May-2002 : Load images from jar
 *
 */

package com.jrefinery.report.demo.resources;

import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

import com.jrefinery.report.resources.JFreeReportResources;

/**
 * User interface items for the JFreeReport demonstration application.  These have been put into
 * a ResourceBundle to ease localisation of the application.
 *
 * @author David Gilbert
 */
public class DemoResources extends JFreeReportResources
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
    {"project.name", "JFreeReport"},
    {"project.version", "0.7.6"},
    {"project.info", "http://www.object-refinery.com/jfreereport/index.html"},
    {"project.copyright", "(C)opyright 2000-2002, by Simba Management Limited and Contributors"},

    // in the title pattern, leave in the '{0}' as it gets replaced with the version number
    {"main-frame.title.pattern", "JFreeReport {0} Demo"},

    {"action.close.name", "Exit"},
    {"action.close.description", "exits JFreeReportDemo"},
    {"action.close.mnemonic", new Integer (KeyEvent.VK_E)},
    {"action.close.accelerator", KeyStroke.getKeyStroke (KeyEvent.VK_X, KeyEvent.CTRL_MASK)},

    {"action.print-preview.name", "Print Preview..."},
    {"action.print-preview.description", "Preview the report"},
    {"action.print-preview.mnemonic", new Integer (KeyEvent.VK_P)},
    {"action.print-preview.accelerator",
        KeyStroke.getKeyStroke (KeyEvent.VK_P, KeyEvent.CTRL_MASK)},
    {"action.print-preview.small-icon",
        getIcon ("com/jrefinery/report/resources/PrintPreview16.gif")},
    {"action.print-preview.icon", getIcon ("com/jrefinery/report/resources/PrintPreview24.gif")},

    {"action.about.name", "About..."},
    {"action.about.description", "Information about the application"},
    {"action.about.mnemonic", new Integer (KeyEvent.VK_A)},
    {"action.about.small-icon", getIcon ("com/jrefinery/report/resources/About16.gif")},
    {"action.about.icon", getIcon ("com/jrefinery/report/resources/About24.gif")},

    {"menu.file.name", "File"},
    {"menu.file.mnemonic", new Character ('F')},
    {"menu.help.name", "Help"},
    {"menu.help.mnemonic", new Character ('H')},
    {"exitdialog.title", "Confirmation .."},
    {"exitdialog.message", "Are you sure that you want to exit the program?"},


    {"report.definitionnotfound", "ReportDefinition {0} not found in the classpath"},
    {"report.definitionfailure.message", "Reportdefinition {0} could not be loaded."},
    {"report.definitionfailure.title", "Loader error"},
    {"report.definitionnull", "Reportdefinition was not generated"},
    {"error", "Error"},
    {"example", "Example {0}"}
  };

}
