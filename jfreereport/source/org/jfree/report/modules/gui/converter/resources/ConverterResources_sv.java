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
 * ConverterResources.java
 * ----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Dilts;
 * Contributor(s):   -;
 *
 * $Id: ConverterResources_sv.java,v 1.4 2003/08/24 15:08:18 taqua Exp $
 *
 * Changes
 * -------------------------
 * 06-Jul-2003 : Initial version
 *
 */
package org.jfree.report.modules.gui.converter.resources;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Swedish language resource for the report converter GUI.
 *
 * @author Thomas Dilts
 */
public class ConverterResources_sv extends JFreeReportResources
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
        {"convertdialog.targetIsEmpty", "M�l filen angavs inte."},
        {"convertdialog.errorTitle", "Fel"},
        {"convertdialog.targetIsNoFile", "M�l filen �r inte en vanlig fil."},
        {"convertdialog.targetIsNotWritable", "Den valde filen �r skrivskydad."},
        {"convertdialog.targetOverwriteConfirmation",
         "Filen ''{0}'' Finns. Skriva �ver den?"},
        {"convertdialog.targetOverwriteTitle", "Skriva �ver filen?"},
        {"convertdialog.targetFile", "M�l fil"},
        {"convertdialog.sourceIsEmpty", "K�lla filen angavs inte"},
        {"convertdialog.sourceIsNoFile", "K�lla filen �r inte en vanlig fil."},
        {"convertdialog.sourceIsNotReadable", "K�lla filen �r skrivskydad."},
        {"convertdialog.sourceFile", "K�lla fil"},
        {"convertdialog.encoding", "Kodningen"},

        {"convertdialog.action.selectTarget.name", "V�lja"},
        {"convertdialog.action.selectTarget.description", "V�lja m�l filen."},
        {"convertdialog.action.selectSource.name", "Select"},
        {"convertdialog.action.selectSource.description", "V�lja k�lla filen."},
        {"convertdialog.action.convert.name", "Konvertera"},
        {"convertdialog.action.convert.description", "Konvertera k�lla filerna."},

        {"convertdialog.title", "Rapport konverterare"},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{ConverterResources.class.getName(), "sv"});
  }

}
