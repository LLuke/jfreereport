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
 * JFreeReportResources_hu.java
 * ----------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Demeter F. Tamás;
 * Contributor(s):   -;
 *
 *
 * $Id: JFreeReportResources_hu.java,v 1.1 2003/07/07 22:44:05 taqua Exp $
 *
 *
 */
package org.jfree.report.modules.gui.base.resources;

import java.awt.event.KeyEvent;

/**
 * Hungarian Language Resources.
 *
 * @author Demeter F. Tamás
 */
public class JFreeReportResources_hu extends JFreeReportResources
{
  public JFreeReportResources_hu()
  {
  }

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
        {"action.close.name", "Bezárás"},
        {"action.close.description", "Az elõnézet bezárása"},
        {"action.close.mnemonic", new Integer(KeyEvent.VK_B)},

        {"action.gotopage.name", "Ugorj adott oldalra ..."},
        {"action.gotopage.description", "Egy oldalra közvetlen ugrás"},
        {"action.gotopage.mnemonic", new Integer(KeyEvent.VK_G)},

        {"dialog.gotopage.message", "Írd be az oldalszámot"},
        {"dialog.gotopage.title", "Ugorj az oldalra"},

        {"action.about.name", "Névjegy..."},
        {"action.about.description", "Információ az alkalmazásról"},
        {"action.about.mnemonic", new Integer(KeyEvent.VK_N)},

        {"action.firstpage.name", "Legelsõ"},
        {"action.firstpage.mnemonic", new Integer(KeyEvent.VK_HOME)},
        {"action.firstpage.description", "Ugorj az elsõ oldalra"},

        {"action.back.name", "Vissza"},
        {"action.back.description", "Lapozz az elõzõ oldalra"},
        {"action.back.mnemonic", new Integer(KeyEvent.VK_PAGE_UP)},

        {"action.forward.name", "Elõre"},
        {"action.forward.description", "Lapozz a következõ oldalra"},
        {"action.forward.mnemonic", new Integer(KeyEvent.VK_PAGE_DOWN)},

        {"action.lastpage.name", "Legutolsó"},
        {"action.lastpage.description", "Ugorj az utolsó oldalra"},
        {"action.lastpage.mnemonic", new Integer(KeyEvent.VK_END)},

        {"action.zoomIn.name", "Nagyítás"},
        {"action.zoomIn.description", "Nagyítás"},
        {"action.zoomIn.mnemonic", new Integer(KeyEvent.VK_PLUS)},

        {"action.zoomOut.name", "Kicsinyítés"},
        {"action.zoomOut.description", "Kicsinyítés"},
        {"action.zoomOut.mnemonic", new Integer(KeyEvent.VK_MINUS)},

        // preview frame...
        {"preview-frame.title", "Nyomtatási elõnézet"},

        // menu labels...
        {"menu.file.name", "Fájl"},
        {"menu.file.mnemonic", new Character('F')},

        {"menu.navigation.name", "Navigáció"},
        {"menu.navigation.mnemonic", new Character('N')},

        {"menu.zoom.name", "Méretezés"},
        {"menu.zoom.mnemonic", new Character('M')},

        {"menu.help.name", "Súgó"},
        {"menu.help.mnemonic", new Character('S')},

        {"statusline.pages", "Oldal: {0}/{1}"},
        {"statusline.error", "A lista generálásakor hiba lépett fel: {0}"},
        {"statusline.repaginate", "Számítom az oldaltöréseket, kérlek várj."},
      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{JFreeReportResources.class.getName(), "hu"});
  }

}
