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
 * ----------------------------
 * JFreeReportResources_ru.java
 * ----------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Sergey M Mozgovoi;
 * Contributor(s):   -;
 *
 * $Id: JFreeReportResources_ru.java,v 1.4 2003/08/24 15:08:18 taqua Exp $
 *
 *
 */
package org.jfree.report.modules.gui.base.resources;


/**
 * Russian Language Resources.
 *
 * @author Sergey M Mozgovoi
 */
public class JFreeReportResources_ru extends JFreeReportResources
{
  /**
   * Default Constructor.
   */
  public JFreeReportResources_ru()
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
        {"action.close.name", "\u0417\u0430\u043a\u0440\u044b\u0442\u044c"},
        {"action.close.description",
         "\u0412\u044b\u0439\u0442\u0438 \u0438\u0437 \u043e\u043a\u043d\u0430 " +
      "\u043f\u0440\u0435\u0434\u0432\u0430\u0440\u0438\u0442\u0435\u043b\u044c\u043d" +
      "\u043e\u0433\u043e \u043f\u0440\u043e\u0441\u043c\u043e\u0442\u0440\u0430"},

        {"action.gotopage.name",
         "\u041f\u0435\u0440\u0435\u0439\u0442\u0438 \u043d\u0430 \u0441\u0442\u0440" +
      "\u0430\u043d\u0438\u0446\u0443..."},
        {"action.gotopage.description",
         "\u041f\u0440\u043e\u0441\u043c\u043e\u0442\u0440 \u0441\u0442\u0440\u0430\u043d" +
      "\u0438\u0446\u044b \u043d\u0435\u043f\u043e\u0441\u0440\u0435\u0434\u0441\u0442\u0432" +
      "\u0435\u043d\u043d\u043e"},

        {"dialog.gotopage.message",
         "\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u043d\u043e\u043c\u0435\u0440 " +
      "\u0441\u0442\u0440\u0430\u043d\u0438\u0446\u044b"},
        {"dialog.gotopage.title",
         "\u041f\u0435\u0440\u0435\u0439\u0442\u0438 \u043d\u0430 \u0441\u0442\u0440\u0430" +
      "\u043d\u0438\u0446\u0443"},

        {"action.about.name",
         "\u041e \u043f\u0440\u043e\u0433\u0440\u0430\u043c\u043c\u0435..."},
        {"action.about.description",
         "\u0418\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0438\u044f \u043e " +
      "\u043f\u0440\u0438\u043b\u043e\u0436\u0435\u043d\u0438\u0438"},

        {"action.firstpage.name",
         "\u041d\u0430\u0447\u0430\u043b\u043e"},
        {"action.firstpage.description",
         "\u041f\u0435\u0440\u0435\u043a\u043b\u044e\u0447\u0438\u0442\u044c\u0441\u044f " +
      "\u043d\u0430 \u043f\u0435\u0440\u0432\u0443\u044e " +
      "\u0441\u0442\u0440\u0430\u043d\u0438\u0446\u0443"},

        {"action.back.name", "\u041d\u0430\u0437\u0430\u0434"},
        {"action.back.description",
         "\u041f\u0435\u0440\u0435\u043a\u043b\u044e\u0447\u0438\u0442\u044c\u0441\u044f " +
      "\u043d\u0430 \u043f\u0440\u0435\u0434\u044b\u0434\u0443\u0449\u0435\u044e " +
      "\u0441\u0442\u0440\u0430\u043d\u0438\u0446\u0443"},

        {"action.forward.name", "\u0412\u043f\u0435\u0440\u0435\u0434"},
        {"action.forward.description", "\u041f\u0435\u0440\u0435\u043a\u043b\u044e\u0447\u0438" +
      "\u0442\u044c\u0441\u044f \u043d\u0430 \u0441\u043b\u0435\u0434\u0443\u044e\u0449" +
      "\u0435\u044e \u0441\u0442\u0440\u0430\u043d\u0438\u0446\u0443"},

        {"action.lastpage.name", "\u041a\u043e\u043d\u0435\u0446"},
        {"action.lastpage.description",
         "\u041f\u0435\u0440\u0435\u043a\u043b\u044e\u0447\u0438\u0442\u044c\u0441\u044f " +
      "\u043d\u0430 \u043f\u043e\u0441\u043b\u0435\u0434\u043d\u044e\u044e \u0441\u0442\u0440" +
      "\u0430\u043d\u0438\u0446\u0443"},

        {"action.zoomIn.name",
         "\u0423\u0432\u0435\u043b\u0438\u0447\u0438\u0442\u044c " +
      "\u043c\u0430\u0441\u0448\u0442\u0430\u0431"},
        {"action.zoomIn.description",
         "\u0423\u0432\u0435\u043b\u0438\u0447\u0435\u043d\u0438\u0435 " +
      "\u043c\u0430\u0441\u0448\u0442\u0430\u0431\u0430"},

        {"action.zoomOut.name",
         "\u0423\u043c\u0435\u043d\u044c\u0448\u0438\u0442\u044c " +
      "\u043c\u0430\u0441\u0448\u0442\u0430\u0431"},
        {"action.zoomOut.description",
         "\u0423\u043c\u0435\u043d\u044c\u0448\u0435\u043d\u0438\u0435 " +
      "\u043c\u0430\u0441\u0448\u0442\u0430\u0431\u0430"},

        {"preview-frame.title",
         "\u041f\u0440\u043e\u0441\u043c\u043e\u0442\u0440 " +
      "\u043f\u0435\u0440\u0435\u0434 \u043f\u0435\u0447\u0430\u0442\u044c\u044e"},

        {"menu.file.name", "\u0424\u0430\u0439\u043b"},
        {"menu.navigation.name", "\u041d\u0430\u0432\u0438\u0433\u0430\u0446\u0438\u044f"},
        {"menu.zoom.name", "\u041c\u0430\u0441\u0448\u0442\u0430\u0431"},

        {"menu.help.name", "\u0421\u043f\u0440\u0430\u0432\u043a\u0430"},

        {"statusline.pages",
         "\u0421\u0442\u0440\u0430\u043d\u0438\u0446\u0430 {0} \u0438\u0437 {1}"},
        {"statusline.error",
         "\u0413\u0435\u043d\u0435\u0440\u0430\u0442\u043e\u0440 \u043e\u0442\u0447\u0435\u0442" +
      "\u043e\u0432 \u0432\u044b\u0434\u0430\u043b \u043e\u0448\u0438\u0431\u043a\u0443: {0}"},
        {"statusline.repaginate",
         "\u0420\u0430\u0441\u0447\u0435\u0442 \u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442" +
      "\u0432\u0430 \u0441\u0442\u0440\u0430\u043d\u0438\u0446, \u043f\u043e\u0436\u0430\u043b" +
      "\u0443\u0439\u0441\u0442\u0430 \u043f\u043e\u0434\u043e\u0436\u0434\u0438\u0442\u0435."},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{JFreeReportResources.class.getName(), "ru"});
  }


}

