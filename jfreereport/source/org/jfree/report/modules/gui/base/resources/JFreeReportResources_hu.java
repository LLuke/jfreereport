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
 * Original Author:  Demeter F. Tam�s;
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
 * @author Demeter F. Tam�s
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
        {"action.close.name", "Bez�r�s"},
        {"action.close.description", "Az el�n�zet bez�r�sa"},
        {"action.close.mnemonic", new Integer(KeyEvent.VK_B)},

        {"action.gotopage.name", "Ugorj adott oldalra ..."},
        {"action.gotopage.description", "Egy oldalra k�zvetlen ugr�s"},
        {"action.gotopage.mnemonic", new Integer(KeyEvent.VK_G)},

        {"dialog.gotopage.message", "�rd be az oldalsz�mot"},
        {"dialog.gotopage.title", "Ugorj az oldalra"},

        {"action.about.name", "N�vjegy..."},
        {"action.about.description", "Inform�ci� az alkalmaz�sr�l"},
        {"action.about.mnemonic", new Integer(KeyEvent.VK_N)},

        {"action.firstpage.name", "Legels�"},
        {"action.firstpage.mnemonic", new Integer(KeyEvent.VK_HOME)},
        {"action.firstpage.description", "Ugorj az els� oldalra"},

        {"action.back.name", "Vissza"},
        {"action.back.description", "Lapozz az el�z� oldalra"},
        {"action.back.mnemonic", new Integer(KeyEvent.VK_PAGE_UP)},

        {"action.forward.name", "El�re"},
        {"action.forward.description", "Lapozz a k�vetkez� oldalra"},
        {"action.forward.mnemonic", new Integer(KeyEvent.VK_PAGE_DOWN)},

        {"action.lastpage.name", "Legutols�"},
        {"action.lastpage.description", "Ugorj az utols� oldalra"},
        {"action.lastpage.mnemonic", new Integer(KeyEvent.VK_END)},

        {"action.zoomIn.name", "Nagy�t�s"},
        {"action.zoomIn.description", "Nagy�t�s"},
        {"action.zoomIn.mnemonic", new Integer(KeyEvent.VK_PLUS)},

        {"action.zoomOut.name", "Kicsiny�t�s"},
        {"action.zoomOut.description", "Kicsiny�t�s"},
        {"action.zoomOut.mnemonic", new Integer(KeyEvent.VK_MINUS)},

        // preview frame...
        {"preview-frame.title", "Nyomtat�si el�n�zet"},

        // menu labels...
        {"menu.file.name", "F�jl"},
        {"menu.file.mnemonic", new Character('F')},

        {"menu.navigation.name", "Navig�ci�"},
        {"menu.navigation.mnemonic", new Character('N')},

        {"menu.zoom.name", "M�retez�s"},
        {"menu.zoom.mnemonic", new Character('M')},

        {"menu.help.name", "S�g�"},
        {"menu.help.mnemonic", new Character('S')},

        {"statusline.pages", "Oldal: {0}/{1}"},
        {"statusline.error", "A lista gener�l�sakor hiba l�pett fel: {0}"},
        {"statusline.repaginate", "Sz�m�tom az oldalt�r�seket, k�rlek v�rj."},
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
