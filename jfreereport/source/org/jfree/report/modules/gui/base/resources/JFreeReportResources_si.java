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
 * -------------------------
 * JFreeReportResources_si.java
 * -------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Sergey M Mozgovoi;
 * Contributor(s):   -;
 *
 * $Id: JFreeReportResources_si.java,v 1.2 2003/07/18 17:56:38 taqua Exp $
 *
 */
package org.jfree.report.modules.gui.base.resources;

/**
 * Slovenian Language Resources.
 *
 * @author  Sergey M Mozgovoi
 */
public class JFreeReportResources_si extends JFreeReportResources
{
  /**
   * Default Constructor.
   */
  public JFreeReportResources_si()
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

        {"action.close.name", "Zapri"},
        {"action.close.description", "Zapri okno predogleda tiskanja"},

        {"action.gotopage.name", "Pojdi na stran ..."},
        {"action.gotopage.description", "Direkten pogled na stran"},

        {"dialog.gotopage.message", "Vnesite \u0161tevilko strani"},
        {"dialog.gotopage.title", "Pojdi na stran"},

        {"action.about.name", "Vizitka"},
        {"action.about.description", "Informacije o aplikaciji"},

        {"action.firstpage.name", "Domov"},
        {"action.firstpage.description", "Preklopi na prvo stran"},

        {"action.back.name", "Nazaj"},
        {"action.back.description", "Preklopi na prvo stran"},

        {"action.forward.name", "Naprej"},
        {"action.forward.description", "Preklopi na naslednjo stran"},

        {"action.lastpage.name", "Konec"},
        {"action.lastpage.description", "Preklopi na zadnjo stran"},

        {"action.zoomIn.name", "Pribli\u017Eaj"},
        {"action.zoomIn.description", "Pove\u010Daj zoom"},

        {"action.zoomOut.name", "Oddalji"},
        {"action.zoomOut.description", "Zmanj\u0161aj zoom"},

        {"preview-frame.title", "Predogled tiskanja"},

        {"menu.file.name", "Datoteka"},

        {"menu.navigation.name", "Krmarjenje"},

        {"menu.zoom.name", "Zoom"},

        {"menu.help.name", "Pomo\u010D"},

        {"statusline.pages", "Stran {0} od {1}"},
        {"statusline.error", "Napaka v \u010Dasu generiranja poro\u010Dila: {0}"},
        {"statusline.repaginate", "\u0160tetje prelomov strani, prosim po\u010Dakajte."},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{JFreeReportResources.class.getName(), "si"});
  }

}
