/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * DemoResources_pl.java
 * ---------------------
 * (C)opyright 2002, 2003, by Piotr Bzdyl.
 *
 * Original Author:  Piotr Bzdyl;
 * Contributor(s):   -;
 *
 * $Id: DemoResources_pl.java,v 1.3 2003/08/28 17:45:42 taqua Exp $
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
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * User interface items for the JFreeReport demonstration application.  These have been put into
 * a ResourceBundle to ease localisation of the application.
 *
 * @author Piotr Bzdyl
 */
public class DemoResources_pl extends JFreeReportResources
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

    {"action.close.name", "Wyj\u015bcie"},
    {"action.close.description", "Wyj\u015b z JFreeReportDemo"},
    {"action.close.mnemonic", new Integer(KeyEvent.VK_E)},

    {"action.print-preview.name", "Podgl\u0105d wydruku..."},
    {"action.print-preview.description", "Podgl\u0105d raportu"},
    {"action.print-preview.mnemonic", new Integer(KeyEvent.VK_P)},

    {"action.about.name", "O programie..."},
    {"action.about.description", "Informacja o programie"},
    {"action.about.mnemonic", new Integer(KeyEvent.VK_A)},

    {"menu.file.name", "Plik"},
    {"menu.file.mnemonic", new Character('P')},
    {"menu.help.name", "Pomoc"},
    {"menu.help.mnemonic", new Character('c')},
    {"exitdialog.title", "Potwierdzenie..."},
    {"exitdialog.message", "Czy na pewno wyj\u015b\u0107 z programu?"},


    {"report.definitionnotfound",
     "Definicja raportu {0} nie znaleziona w \u015bcie\u017ce (classpath)"},
    {"report.definitionfailure.message", "Nie mo\u017cna za\u0142adowa� definicji raportu {0}."},
    {"report.definitionfailure.title", "B\u0142\u0105d podczas \u0142adowania (Loader error)"},
    {"report.definitionnull", "Definicja raportu nie zosta\u0142a wygenerowana"},
    {"error", "B\u0142\u0105d"},
    {"example", "Przyk\u0142ad {0}"}
  };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    new DemoResources_pl().generateResourceProperties("polish");
    System.exit(0);
  }

}
