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
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Sergey M Mozgovoi;
 * Contributor(s):   -;
 *
 * $Id: ConverterGUIModule.java,v 1.1 2003/07/07 22:44:05 taqua Exp $
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
 * Slovenian language resource for the report converter GUI.
 * 
 * @author Sergey M Mozgovoi
 */
public class ConverterResources_si extends JFreeReportResources
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
        {"convertdialog.targetIsEmpty", "Ciljna datoteka ni navedena"},
        {"convertdialog.errorTitle", "Napaka"},
        {"convertdialog.targetIsNoFile", "Navedena ciljna datoteka ni navadna datoteka."},
        {"convertdialog.targetIsNotWritable",
         "V navedeno ciljno datoteko ni mogo\u010De pisati."},
        {"convertdialog.targetOverwriteConfirmation",
         "Datoteka ''{0}'' obstaja. Ali jo \u017Eelite prepisati?"},
        {"convertdialog.targetOverwriteTitle", "Ali \u017Eelite prepisati datoteko?"},
        {"convertdialog.targetFile", "Ciljna datoteka"},
        {"convertdialog.sourceIsEmpty", "Izvorna datoteka ni navedena"},
        {"convertdialog.sourceIsNoFile", "Navedena izvorna datoteka ni navadna datoteka."},
        {"convertdialog.sourceIsNotReadable", "Izvorna datoteka ni berljiva."},
        {"convertdialog.sourceFile", "Izvorna datoteka"},
        {"convertdialog.encoding", "Kodiranje"},

        {"convertdialog.action.selectTarget.name", "Izberi"},
        {"convertdialog.action.selectTarget.description", "Izberi ciljno datoteko."},
        {"convertdialog.action.selectSource.name", "Izberi"},
        {"convertdialog.action.selectSource.description", "Izberi izvorno datoteko."},
        {"convertdialog.action.convert.name", "Pretvori"},
        {"convertdialog.action.convert.description", "Pretvori izvorne datoteke."},

        {"convertdialog.title", "Pretvornik poro\u010Dila"},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{ConverterResources.class.getName(), "si"});
  }

}
