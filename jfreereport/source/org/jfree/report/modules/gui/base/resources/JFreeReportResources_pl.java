/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ----------------------------
 * JFreeReportResources_pl.java
 * ----------------------------
 *
 *
 */
package org.jfree.report.modules.gui.base.resources;

import java.awt.event.KeyEvent;

/**
 * Polish Language Resources.
 *
 * @author PB
 */
public class JFreeReportResources_pl extends JFreeReportResources
{

  /**
   * Returns the array of strings in the resource bundle.
   *
   * @return an array of localised resources.
   */
  public Object[][] getContents()
  {
    return CONTENTS;
  }

  /** The resources to be localised. */
  private static final Object[][] CONTENTS =
      {
        {"action.close.name", "Zamknij"},
        {"action.close.description", "Zamknij okno podgl\u0105du"},
        {"action.close.mnemonic", new Integer(KeyEvent.VK_K)},

        {"action.gotopage.name", "Przejd\u017a do strony..."},
        {"action.gotopage.description",
         "Przejd\u017a bezpo\u015brednio do okre\u015blonej strony"},

        {"dialog.gotopage.message", "Podaj numer strony"},
        {"dialog.gotopage.title", "Przejd\u017a do strony..."},

        {"action.about.name", "O programie..."},
        {"action.about.description", "Informacja o programie"},
        {"action.about.mnemonic", new Integer(KeyEvent.VK_O)},

        {"action.firstpage.name", "Przejd\u017a do pierwszej strony"},
        {"action.firstpage.description", "Przejd\u017a do pierwszej strony"},

        {"action.lastpage.name", "Przejd\u017a do ostatniej strony"},
        {"action.lastpage.description", "Przejd\u017a do ostatniej strony"},

        {"action.back.name", "Przejd\u017a do poprzedniej strony"},
        {"action.back.description", "Przejd\u017a do poprzedniej strony"},

        {"action.forward.name", "Przejd\u017a do nast\u0119pnej strony"},
        {"action.forward.description", "Przejd\u017a do nast\u0119pnej strony"},

        {"action.zoomIn.name", "Powi\u0119kszenie"},
        {"action.zoomIn.description", "Powi\u0119kszenie"},

        {"action.zoomOut.name", "Pomniejszenie"},
        {"action.zoomOut.description", "Pomniejszenie"},

        {"preview-frame.title", "Podgl\u0105d wydruku"},

        {"menu.file.name", "Plik"},
        {"menu.file.mnemonic", new Character('P')},

        {"menu.help.name", "Pomoc"},
        {"menu.help.mnemonic", new Character('c')},

        {"statusline.pages", "Strona {0} z {1}"},
        {"statusline.error",
         "Wyst\u0105pi\u0142 b\u0142\u0105d podczas generowania raportu: {0}"},
      };


  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{JFreeReportResources.class.getName(), "pl"});
  }


}
