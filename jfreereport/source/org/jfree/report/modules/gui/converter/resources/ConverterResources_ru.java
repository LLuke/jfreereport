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
 * ConverterResources.java
 * ----------------------------
 *
 *
 */
package org.jfree.report.modules.gui.converter.resources;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

public class ConverterResources_ru extends JFreeReportResources
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

        {"convertdialog.targetIsEmpty",
         "\u0423\u043a\u0430\u0437\u0430\u043d\u043d\u044b\u0439 \u0444\u0430\u0439\u043b " +
      "\u043d\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442"},
        {"convertdialog.errorTitle",
         "\u041e\u0448\u0438\u0431\u043a\u0430"},
        {"convertdialog.targetIsNoFile",
         "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u043e\u0431\u044a" +
      "\u0435\u043a\u0442 \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f " +
      "\u043e\u0431\u044b\u0447\u043d\u044b\u043c \u0444\u0430\u0439\u043b\u043e\u043c."},
        {"convertdialog.targetIsNotWritable",
         "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u0444\u0430\u0439\u043b " +
      "\u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043f\u0435\u0440\u0435" +
      "\u0437\u0430\u043f\u0438\u0441\u044b\u0432\u0430\u0435\u043c\u044b\u043c."},
        {"convertdialog.targetOverwriteConfirmation",
         "\u0424\u0430\u0439\u043b ''{0}'' \u0443\u0436\u0435 \u0441\u0443\u0449\u0435" +
      "\u0441\u0442\u0432\u0443\u0435\u0442. \u041f\u0435\u0440\u0435\u0437\u0430\u043f" +
      "\u0438\u0441\u0430\u0442\u044c \u0435\u0433\u043e?"},
        {"convertdialog.targetOverwriteTitle",
         "\u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u0430\u0442\u044c " +
      "\u0444\u0430\u0439\u043b?"},
        {"convertdialog.targetFile",
         "\u0423\u043a\u0430\u0437\u0430\u043d\u043d\u044b\u0439 \u0444\u0430\u0439\u043b"},
        {"convertdialog.sourceIsEmpty",
         "\u0418\u0441\u0445\u043e\u0434\u043d\u044b\u0439 \u0444\u0430\u0439\u043b " +
      "\u043d\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442"},
        {"convertdialog.sourceIsNoFile",
         "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u0438\u0441\u0445" +
      "\u043e\u0434\u043d\u044b\u0439 \u0444\u0430\u0439\u043b \u043d\u0435 \u044f\u0432" +
      "\u043b\u044f\u0435\u0442\u0441\u044f \u043e\u0431\u044b\u0447\u043d\u044b\u043c " +
      "\u0444\u0430\u0439\u043b\u043e\u043c."},
        {"convertdialog.sourceIsNotReadable",
         "\u0418\u0441\u0445\u043e\u0434\u043d\u044b\u0439 \u0444\u0430\u0439\u043b " +
      "\u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u0447\u0438\u0442" +
      "\u0430\u0435\u043c\u044b\u043c."},
        {"convertdialog.sourceFile",
         "\u0418\u0441\u0445\u043e\u0434\u043d\u044b\u0439 \u0444\u0430\u0439\u043b"},

        {"convertdialog.action.selectTarget.name", "\u0412\u044b\u0431\u043e\u0440"},
        {"convertdialog.action.selectTarget.description",
         "\u0412\u044b\u0431\u0440\u0430\u0442\u044c \u0432\u044b\u0445\u043e\u0434\u043d" +
      "\u043e\u0439 \u0444\u0430\u0439\u043b."},
        {"convertdialog.action.selectSource.name", "\u0412\u044b\u0431\u043e\u0440"},
        {"convertdialog.action.selectSource.description",
         "\u0412\u044b\u0431\u0440\u0430\u0442\u044c \u0438\u0441\u0445\u043e\u0434\u043d" +
      "\u044b\u0439 \u0444\u0430\u0439\u043b."},
        {"convertdialog.action.convert.name",
         "\u041a\u043e\u043d\u0432\u0435\u0440\u0442\u0438\u0440\u043e\u0432" +
      "\u0430\u043d\u0438\u0435"},
        {"convertdialog.action.convert.description",
         "\u041a\u043e\u043d\u0432\u0435\u0440\u0442\u0438\u0440\u043e\u0432\u0430" +
      "\u0442\u044c \u0438\u0441\u0445\u043e\u0434\u043d\u044b\u0439 \u0444\u0430\u0439\u043b."},

        {"convertdialog.title",
         "\u041a\u043e\u043d\u0432\u0435\u0440\u0442\u043e\u0440 \u043e" +
      "\u0442\u0447\u0435\u0442\u043e\u0432"},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{ConverterResources.class.getName(), "ru"});
  }

}
