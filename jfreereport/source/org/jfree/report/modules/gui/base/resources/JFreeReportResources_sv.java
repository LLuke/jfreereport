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
 * ----------------------------
 * JFreeReportResources_sv.java
 * ----------------------------
 * (C)opyright 2000-2003, by Object Refinery Limited and Contributors.
 *
 * Original Author:  Thomas Dilts;
 * Contributor(s):   -;
 *
 * $Id: JFreeReportResources_sv.java,v 1.3 2003/08/19 13:37:23 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.gui.base.resources;

import java.awt.event.KeyEvent;

/**
 * Swedish Language Resources.
 *
 * @author Thomas Dilts
 */
public class JFreeReportResources_sv extends JFreeReportResources
{
  /**
   * Default Constructor.
   */
  public JFreeReportResources_sv()
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
        {"action.close.name", "St�nga"},
        {"action.close.description", "St�nga f�rhandsgransknings-f�nster"},
        {"action.close.mnemonic", new Integer(KeyEvent.VK_C)},

        {"action.gotopage.name", "G� till sida ..."},
        {"action.gotopage.description", "Se en sida direkt"},
        {"action.gotopage.mnemonic", new Integer(KeyEvent.VK_G)},

        {"dialog.gotopage.message", "Ange en sida nummer"},
        {"dialog.gotopage.title", "G� till sida"},

        {"action.about.name", "Om..."},
        {"action.about.description", "Information om applikationen"},
        {"action.about.mnemonic", new Integer(KeyEvent.VK_A)},

        {"action.firstpage.name", "Hem"},
        {"action.firstpage.description", "Bl�ddra till den f�rsta sidan"},

        {"action.back.name", "Bl�ddra bak�t"},
        {"action.back.description", "Bl�ddra till den f�reg�ende sidan"},

        {"action.forward.name", "Bl�ddra fram�t"},
        {"action.forward.description", "Bl�ddra till den n�sta sidan"},

        {"action.lastpage.name", "Sista sida"},
        {"action.lastpage.description", "Bl�ddra till den sista sidan"},

        {"action.zoomIn.name", "Zooma in"},
        {"action.zoomIn.description", "F�rst�rka zoomen"},

        {"action.zoomOut.name", "Zooma ut"},
        {"action.zoomOut.description", "Minska zoomen"},

        // preview frame...
        {"preview-frame.title", "F�rhandsgranska"},

        // menu labels...
        {"menu.file.name", "Fil"},
        {"menu.file.mnemonic", new Character('F')},

        {"menu.navigation.name", "Navigation"},
        {"menu.navigation.mnemonic", new Character('N')},

        {"menu.zoom.name", "Zooma"},
        {"menu.zoom.mnemonic", new Character('Z')},

        {"menu.help.name", "Hj�lp"},
        {"menu.help.mnemonic", new Character('H')},

        {"statusline.pages", "Sida {0} av {1}"},
        {"statusline.error", "Reportgeneration skapade ett fel: {0}"},
        {"statusline.repaginate", "Ber�kna sida-brytning, var sn�ll och v�nta."},
      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{JFreeReportResources.class.getName(), "sv"});
  }


}

