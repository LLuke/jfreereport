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

public class ConverterResources_nl extends JFreeReportResources
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
        {"convertdialog.action.convert.description", "Converteer de bronbestanden."},
        {"convertdialog.action.convert.name", "Converteren"},
        {"convertdialog.action.selectSource.description", "Selecteer brondbestand."},
        {"convertdialog.action.selectSource.name", "Selecteer"},
        {"convertdialog.action.selectTarget.description", "Selecteer doelbestand."},
        {"convertdialog.action.selectTarget.name", "Selecteer"},
        {"convertdialog.errorTitle", "Fout"},
        {"convertdialog.sourceFile", "Bronbestand"},
        {"convertdialog.sourceIsEmpty", "Bronbestand is leeg."},
        {"convertdialog.sourceIsNoFile", "Ongeldig bronbestand."},
        {"convertdialog.sourceIsNotReadable", "Het bronbestand kan niet worden gelezen."},
        {"convertdialog.targetFile", "Doelbestand"},
        {"convertdialog.targetIsEmpty", "Doelbestand is leeg."},
        {"convertdialog.targetIsNoFile", "Ongeldig doelbestand."},
        {"convertdialog.targetIsNotWritable", "Het doelbestand kan niet worden beschreven."},
        {"convertdialog.targetOverwriteConfirmation",
         "Het bestand ''{0}'' bestaat reeds. Overschrijven?"},
        {"convertdialog.targetOverwriteTitle", "Bestand overschrijven?"},
        {"convertdialog.title", "Report-Converter"},


      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{ConverterResources.class.getName(), "nl"});
  }

}
