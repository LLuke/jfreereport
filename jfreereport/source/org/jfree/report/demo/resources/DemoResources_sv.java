/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * Original Author:  Thomas Dilts;
 * Contributor(s):   -;
 *
 * $Id: DemoResources_sv.java,v 1.2 2003/08/31 19:27:56 taqua Exp $
 *
 * Changes
 * -------
 * 27-Mar-2002 : Version 1 (DG);
 * 16-May-2002 : Load images from jar
 *
 */

package org.jfree.report.demo.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;

/**
 * User interface items for the JFreeReport demonstration application.  These have been put into
 * a ResourceBundle to ease localisation of the application.
 *
 * @author David Gilbert
 */
public class DemoResources_sv extends JFreeReportResources
{
  /**
   * Returns the contents of the resource bundle.
   *
   * @return an array of localised resources.
   */
  public Object[][] getContents()
  {
    return CONTENTS;
  }

  /** The resources to be localised. */
  private static final Object[][] CONTENTS = {
    // in the title pattern, leave in the '{0}' as it gets replaced with the version number
    {"main-frame.title.pattern", "JFreeReport {0} Demo"},

    {"action.close.name", "Avsluta"},
    {"action.close.description", "Avsluter JFreeReportDemo"},
    {"action.close.mnemonic", new Integer(KeyEvent.VK_E)},
    {"action.close.accelerator", createMenuKeystroke(KeyEvent.VK_X)},

    {"action.print-preview.name", "Förhandsgranska..."},
    {"action.print-preview.description", "Förhandsgranska rapporten"},
    {"action.print-preview.mnemonic", new Integer(KeyEvent.VK_P)},
    {"action.print-preview.accelerator", createMenuKeystroke(KeyEvent.VK_P)},

    {"action.about.name", "Om..."},
    {"action.about.description", "Information om applikationen"},
    {"action.about.mnemonic", new Integer(KeyEvent.VK_A)},

    {"menu.file.name", "Fil"},
    {"menu.file.mnemonic", new Character('F')},
    {"menu.help.name", "Hjälp"},
    {"menu.help.mnemonic", new Character('H')},
    {"exitdialog.title", "Konfirmation .."},
    {"exitdialog.message", "Är du säkert att du vill avlsluta programet?"},


    {"report.definitionnotfound", "Rapportdefinition {0} hittades inte i classpath"},
    {"report.definitionfailure.message", "Rapportdefinition {0} kunda inte laddas."},
    {"report.definitionfailure.title", "Laddningsfel"},
    {"report.definitionnull", "Rapportdefinition skapades inte"},
    {"error", "Fel"},
    {"example", "Exempel {0}"},

    {"report.previewfailure.message", 
      "Ett fel uppstod under initializeringen av förhandgranskningsfönstret."},
    {"report.previewfailure.title", "Förhandgranskningsfel"},

  };

}
