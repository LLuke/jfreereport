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
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ConverterResources.java,v 1.6 2003/08/26 17:35:51 taqua Exp $
 *
 * Changes
 * -------------------------
 * 06-Jul-2003 : Initial version
 *
 */
package org.jfree.report.modules.gui.converter.resources;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;
import org.jfree.report.modules.gui.config.resources.ConfigResources;

/**
 * English language resource for the report converter GUI.
 *
 * @author Thomas Morgner
 */
public class ConverterResources extends JFreeReportResources
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
        {"convertdialog.targetIsEmpty", "The target file is not specified"},
        {"convertdialog.errorTitle", "Error"},
        {"convertdialog.targetIsNoFile", "The specified target file is no ordinary file."},
        {"convertdialog.targetIsNotWritable", "The specified target file is not writable."},
        {"convertdialog.targetOverwriteConfirmation",
         "The file ''{0}'' exists. Overwrite it?"},
        {"convertdialog.targetOverwriteTitle", "Overwrite file?"},
        {"convertdialog.targetFile", "Target file"},
        {"convertdialog.encoding", "Encoding"},
        {"convertdialog.sourceIsEmpty", "The source file is not specified"},
        {"convertdialog.sourceIsNoFile", "The specified source file is no ordinary file."},
        {"convertdialog.sourceIsNotReadable", "The source file is not readable."},
        {"convertdialog.sourceFile", "Source file"},

        {"convertdialog.action.selectTarget.name", "Select"},
        {"convertdialog.action.selectTarget.description", "Select target file."},
        {"convertdialog.action.selectSource.name", "Select"},
        {"convertdialog.action.selectSource.description", "Select source file."},
        {"convertdialog.action.convert.name", "Convert"},
        {"convertdialog.action.convert.description", "Convert the source files."},

        {"convertdialog.title", "Report-Converter"},

        {"ResultTableModel.Severity", "Severity"},
        {"ResultTableModel.Message", "Message"},
        {"ResultTableModel.Line", "Line"},
        {"ResultTableModel.Column", "Column"},
      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    new ConverterResources().generateResourceProperties("<default>");
    System.exit(0);
  }


}
