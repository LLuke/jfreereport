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
 * ConverterResources_hu.java
 * ----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Demeter F. Tam�s;
 * Contributor(s):   -;
 *
 * $Id: ConverterResources_hu.java,v 1.3 2003/08/19 13:37:23 taqua Exp $
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
 * Hungarian language resource for the report converter GUI.
 * 
 * @author Demeter F. Tam�s
 */
public class ConverterResources_hu extends JFreeReportResources
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
        {"convertdialog.targetIsEmpty", "A c�l�llom�ny nincs megadva"},
        {"convertdialog.errorTitle", "Hiba"},
        {"convertdialog.targetIsNoFile", "A megadott c�l nem f�jl."},
        {"convertdialog.targetIsNotWritable", "A megadott f�jl nem �rhat�."},
        {"convertdialog.targetOverwriteConfirmation",
         "A(z) ''{0}'' f�jl l�tezik. Fel�l�rhatom?"},
        {"convertdialog.targetOverwriteTitle", "Fel�l�rhatom a f�jlt?"},
        {"convertdialog.targetFile", "C�l f�jl"},
        {"convertdialog.sourceIsEmpty", "A forr�s f�jl nincs megadva"},
        {"convertdialog.sourceIsNoFile", "A megadott forr�s nem k�z�ns�ges f�jl."},
        {"convertdialog.sourceIsNotReadable", "A forr�sf�jl nem olvashat�."},
        {"convertdialog.sourceFile", "Forr�s f�jl"},
        {"convertdialog.encoding", "Karakterk�dol�s"},

        {"convertdialog.action.selectTarget.name", "V�lassz"},
        {"convertdialog.action.selectTarget.description", "V�lassz c�l f�jlt."},
        {"convertdialog.action.selectSource.name", "V�lassz"},
        {"convertdialog.action.selectSource.description", "V�lassz forr�s f�jlt."},
        {"convertdialog.action.convert.name", "Konvert�l�s"},
        {"convertdialog.action.convert.description", "Konvert�ld a forr�s f�jlokat."},

        {"convertdialog.title", "Lista konvert�l�"},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{ConverterResources.class.getName(), "hu"});
  }

}
