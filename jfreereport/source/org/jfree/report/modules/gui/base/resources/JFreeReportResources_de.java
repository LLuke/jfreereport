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
 * JFreeReportResources_de.java
 * ----------------------------
 * (C)opyright 2000-2003, by Object Refinery Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   Helmut Leininger;
 *
 * $Id: JFreeReportResources_de.java,v 1.6 2003/08/24 15:08:18 taqua Exp $
 *
 * Changes
 * -------
 * 16-Feb-2003 : Corrected version submitted by Helmut Leininger
 *
 */
package org.jfree.report.modules.gui.base.resources;

import java.awt.event.KeyEvent;

/**
 * German Language Resources.
 *
 * @author Thomas Morgner
 */
public class JFreeReportResources_de extends JFreeReportResources
{
  /**
   * Default Constructor.
   */
  public JFreeReportResources_de()
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
        {"action.close.name", "Schliessen"},
        {"action.close.description", "Beendet die Seitenansicht"},
        {"action.close.mnemonic", new Integer(KeyEvent.VK_B)},

        {"action.gotopage.name", "Gehe zu ..."},
        {"action.gotopage.description", "Wechselt zu einer bestimmten Seite im Bericht."},

        {"dialog.gotopage.message", "Seitenzahl eingeben"},
        {"dialog.gotopage.title", "Gehe zu Seite ..."},

        {"action.about.name", "\u00dcber..."},
        {"action.about.description", "Informationen \u00fcber JFreeReport"},
        {"action.about.mnemonic", new Integer(KeyEvent.VK_A)},

        {"action.firstpage.name", "Anfang"},
        {"action.firstpage.description", "Gehe zum StartState"},

        {"action.lastpage.name", "Ende"},
        {"action.lastpage.description", "Gehe zum Ende"},

        {"action.back.name", "Zur\u00fcck"},
        {"action.back.description", "Wechselt zur vorherigen Seite"},

        {"action.forward.name", "N\u00e4chste"},
        {"action.forward.description", "Wechselt zur n\u00e4chsten Seite"},

        {"action.zoomIn.name", "Vergr\u00f6ssern"},
        {"action.zoomIn.description",
         "Zeigt die aktuelle Seite in einem gr\u00f6sseren Masstab an"},

        {"action.zoomOut.name", "Verkleinern"},
        {"action.zoomOut.description",
         "Zeigt die aktuelle Seite in einem kleineren Masstab an"},

        {"preview-frame.title", "Seitenansicht"},

        {"menu.file.name", "Datei"},
        {"menu.file.mnemonic", new Character('D')},

        {"menu.help.name", "Hilfe"},
        {"menu.help.mnemonic", new Character('H')},

        {"menu.zoom.name", "Zoom"},
        {"menu.zoom.mnemonic", new Character('Z')},

        {"menu.navigation.name", "Navigation"},
        {"menu.navigation.mnemonic", new Character('N')},

        {"statusline.pages", "Seite {0} von {1}"},
        {"statusline.error", "Die ReportGenerierung ist fehlgeschlagen: {0}"},
        {"statusline.repaginate", "Erzeuge Seitenumbr\u00fcche ..."},

        {"FileChooser.acceptAllFileFilterText", "Alle Dateien (*.*)"},
        {"FileChooser.cancelButtonMnemonic", new Integer(KeyEvent.VK_A)},
        {"FileChooser.cancelButtonText", "Abbrechen"},
        {"FileChooser.cancelButtonToolTipText", "Bricht die Dateiauswahl ab."},
        {"FileChooser.detailsViewButtonAccessibleName", "Details"},
        {"FileChooser.detailsViewButtonToolTipText", "Details"},
        {"FileChooser.directoryDescriptionText", "Ordner"},
        {"FileChooser.fileDescriptionText", "Datei"},
        {"FileChooser.fileNameLabelMnemonic", new Integer(KeyEvent.VK_N)},
        {"FileChooser.fileNameLabelText", "Dateiname:"},
        {"FileChooser.filesOfTypeLabelMnemonic", new Integer(KeyEvent.VK_T)},
        {"FileChooser.filesOfTypeLabelText", "Dateityp:"},
        {"FileChooser.helpButtonMnemonic", new Integer(KeyEvent.VK_H)},
        {"FileChooser.helpButtonText", "Hilfe"},
        {"FileChooser.helpButtonToolTipText", "Dateiauswahl-Dialog Hilfe"},
        {"FileChooser.homeFolderAccessibleName", "Heimatverzeichnis"},
        {"FileChooser.homeFolderToolTipText", "Heimatverzeichnis"},
        {"FileChooser.listViewButtonAccessibleName", "Liste"},
        {"FileChooser.listViewButtonToolTipText", "Listenansicht"},
        {"FileChooser.lookInLabelMnemonic", new Integer(KeyEvent.VK_I)},
        {"FileChooser.lookInLabelText", "Suche in:"},
        {"FileChooser.newFolderAccessibleNam", "Neuer Ordner"},
        {"FileChooser.newFolderErrorSeparator", ":"},
        {"FileChooser.newFolderErrorText", "Fehler beim erstellen eines neuen Ordners"},
        {"FileChooser.newFolderToolTipText", "Neuen Ordner anlegen"},
        {"FileChooser.openButtonMnemonic", new Integer(KeyEvent.VK_O)},
        {"FileChooser.openButtonText", "Öffnen"},
        {"FileChooser.openButtonToolTipText", "Öffnet die ausgew\u00e4hlte Datei"},
        {"FileChooser.saveButtonMnemonic", new Integer(KeyEvent.VK_S)},
        {"FileChooser.saveButtonText", "Speichern"},
        {"FileChooser.saveButtonToolTipText", "Speichert die ausgew\u00e4hlte Datei"},
        {"FileChooser.updateButtonMnemonic", new Integer(KeyEvent.VK_U)},
        {"FileChooser.updateButtonText", "Aktualisieren"},
        {"FileChooser.updateButtonToolTipText", "Aktualisiert die Ordneransicht"},
        {"FileChooser.upFolderAccessibleName", "Hoch"},
        {"FileChooser.upFolderToolTipText", "Eine Verzeichnisebene h\u00f6her"},

        {"OptionPane.cancelButtonText", "Abbruch"},
        {"OptionPane.noButtonText", "Nein"},
        {"OptionPane.okButtonText", "OK"},
        {"OptionPane.titleText", "Eine Option ausw\u00e4hlen"},
        {"OptionPane.yesButtonText", "Ja"},

        {"ColorChooser.cancelText", "Abbruch"},
        {"ColorChooser.hsbNameText", "HSB"},
        {"ColorChooser.hsbHueText", "H"},
        {"ColorChooser.hsbSaturationText", "S"},
        {"ColorChooser.hsbBrightnessText", "B"},
        {"ColorChooser.hsbRedText", "R"},
        {"ColorChooser.hsbGreenText", "G"},
        {"ColorChooser.hsbBlueText", "B"},
        {"ColorChooser.okText", "OK"},
        {"ColorChooser.previewText", "Vorschau"},
        {"ColorChooser.resetText", "Reset"},
        {"ColorChooser.rgbNameText", "RGB"},
        {"ColorChooser.rgbRedMnemonic", new Integer(KeyEvent.VK_R)},
        {"ColorChooser.rgbRedText", "Rot"},
        {"ColorChooser.rgbGreenMnemonic", new Integer(KeyEvent.VK_G)},
        {"ColorChooser.rgbGreenText", "Gr\00fcn"},
        {"ColorChooser.rgbBlueMnemonic", new Integer(KeyEvent.VK_B)},
        {"ColorChooser.rgbBlueText", "Blau"},
        {"ColorChooser.sampleText", "Beispieltext Beispieltext"},
        {"ColorChooser.swatchesNameText", "Swatches"},
        {"ColorChooser.swatchesRecentText", "Recent:"},

        {"InternalFrameTitlePane.closeButtonAccessibleName", "Schliessen"},
        {"InternalFrameTitlePane.iconifyButtonAccessibleName", "Minimieren"},
        {"InternalFrameTitlePane.maximizeButtonAccessibleName", "Maximieren"},

        {"FormView.resetButtonText", "Reset"},
        {"FormView.submitButtonText", "Abfrage absenden"},

        {"AbstractButton.clickText", "click"},

        {"AbstractDocument.additionText", "hinzuf\u00fcgen"},
        {"AbstractDocument.deletionText", "entfernen"},
        {"AbstractDocument.redoText", "Wiederholen"},
        {"AbstractDocument.styleChangeText", "Formatierung \u00e4ndern"},
        {"AbstractDocument.undoText", "R\u00fcckg\u00e4ngig"},

        {"AbstractUndoableEdit.redoText", "Wiederholen"},
        {"AbstractUndoableEdit.undoText", "R\u00fcckg\u00e4ngig"},

        {"ProgressMonitor.progressText", "Fortschritt..."},

        {"SplitPane.leftButtonText", "Linker Knopf"},
        {"SplitPane.rightButtonText", "Rechter Knopf"},

        // progress dialog defaults
        {"progress-dialog.prepare-layout", "Bereite das Layout des Berichts vor."},
        {"progress-dialog.perform-output", "Der Export des Berichts wird nun ausgeführt ..."},
        {"progress-dialog.page-label", "Seite: {0}"},
        {"progress-dialog.rows-label", "Zeile: {0} / {1}"},
        {"progress-dialog.pass-label", "Durchlauf: {0} - Berechne die Funktionswerte ..."},
      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{JFreeReportResources.class.getName(), "de"});
  }

}
