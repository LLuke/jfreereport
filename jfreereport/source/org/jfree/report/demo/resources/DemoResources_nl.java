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
 * ---------------------
 * DemoResources_de.java
 * ---------------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DemoResources_nl.java,v 1.1 2003/09/08 18:41:40 taqua Exp $
 *
 * Changes
 * -------
 * 27-Mar-2002 : Version 1 (DG);
 * 16-May-2002 : Line delimiters adjusted and load icons from jar (JS)
 *
 */
package org.jfree.report.demo.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * User interface items for the JFreeReport demonstration application.  These have been put into
 * a ResourceBundle to ease localisation of the application.
 *
 * @author TM
 */
public class DemoResources_nl extends JFreeReportResources
{
  /**
   * Returns the contents of the resource bundle.
   *
   * @return an array of localised resources.
   */
  public Object[][] getContents()
  {
    return CONTENTS;
  }

  /** The resources to be localised. */
  private static final Object[][] CONTENTS = {
   // in the title pattern, leave in the '{0}' as it gets replaced with the version number
   {"main-frame.title.pattern", "JFreeReport {0} Demo"},

   {"action.close.name", "Afsluiten"},
   {"action.close.description", "Sluit JFreeReport Demo af"},
   {"action.close.mnemonic", new Integer(KeyEvent.VK_E)},
   {"action.close.accelerator", createMenuKeystroke(KeyEvent.VK_X)},

   {"action.print-preview.name", "Afdrukvoorbeeld..."},
   {"action.print-preview.description", "Geef een voorbeeld van het rapport"},
   {"action.print-preview.mnemonic", new Integer(KeyEvent.VK_F)},
   {"action.print-preview.accelerator", createMenuKeystroke(KeyEvent.VK_P)},

   {"action.about.name", "Info..."},
   {"action.about.description", "Informatie over de applicatie"},
   {"action.about.mnemonic", new Integer(KeyEvent.VK_I)},

   {"menu.file.name", "Bestand"},
   {"menu.file.mnemonic", new Character('B')},
   {"menu.help.name", "Help"},
   {"menu.help.mnemonic", new Character('H')},
   {"exitdialog.title", "Bevestiging .."},
   {"exitdialog.message", "Weet u zeker dat u het programma wilt afsluiten?"},

   {"report.definitionnotfound", "Rapportdefinitie {0} niet gevonden in klassepad"},
   {"report.definitionfailure.message", "Rapportdefinitie {0} kon niet worden geladen."},
   {"report.definitionfailure.title", "Fout tijdens laden"},
   {"report.definitionnull", "Rapportdefinitie is niet gegenereerd"},
   {"error", "Fout"},
   {"example", "Voorbeeld {0}"},

   {"report.previewfailure.message", 
    "Er is een fout opgetreden tijdens de initialisatie van het voorbeeld venster."},
   {"report.previewfailure.title", "Rapport voorbeeld fout"},

  };


  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    new DemoResources_nl().generateResourceProperties("dutch");
    System.exit(0);
  }

}
