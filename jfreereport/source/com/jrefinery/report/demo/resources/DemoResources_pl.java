/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * $Id: DemoResources_pl.java,v 1.4 2003/05/02 12:39:43 taqua Exp $
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
 * @author Piotr Bzdyl
 */
public class DemoResources_pl extends JFreeReportResources
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

    {"action.close.name", "Wyj\u015bcie"},
    {"action.close.description", "Wyj\u015b z JFreeReportDemo"},
    {"action.close.mnemonic", new Integer (KeyEvent.VK_E)},
    {"action.close.accelerator", KeyStroke.getKeyStroke (KeyEvent.VK_X, KeyEvent.CTRL_MASK)},

    {"action.print-preview.name", "Podgl\u0105d wydruku..."},
    {"action.print-preview.description", "Podgl\u0105d raportu"},
    {"action.print-preview.mnemonic", new Integer (KeyEvent.VK_P)},
    {"action.print-preview.accelerator",
        KeyStroke.getKeyStroke (KeyEvent.VK_P, KeyEvent.CTRL_MASK)},
    {"action.print-preview.small-icon",
        getIcon ("com/jrefinery/report/resources/PrintPreview16.gif")},
    {"action.print-preview.icon", getIcon ("com/jrefinery/report/resources/PrintPreview24.gif")},

    {"action.about.name", "O programie..."},
    {"action.about.description", "Informacja o programie"},
    {"action.about.mnemonic", new Integer (KeyEvent.VK_A)},
    {"action.about.small-icon", getIcon ("com/jrefinery/report/resources/About16.gif")},
    {"action.about.icon", getIcon ("com/jrefinery/report/resources/About24.gif")},

    {"menu.file.name", "Plik"},
    {"menu.file.mnemonic", new Character ('P')},
    {"menu.help.name", "Pomoc"},
    {"menu.help.mnemonic", new Character ('c')},
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

}
