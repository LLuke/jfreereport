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
 * ------------------------------
 * EnumKeyEditor.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: EnumKeyEditor.java,v 1.3 2003/09/12 18:46:18 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 31-Aug-2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.report.modules.gui.config.model.EnumConfigDescriptionEntry;
import org.jfree.report.util.ReportConfiguration;

/**
 * The enumeration key editor is used to edit configuration keys, which
 * accept a closed set of values. The possible values are defined in the
 * config-description.
 * 
 * @author Thomas Morgner
 */
public class EnumKeyEditor extends AbstractKeyEditor
{
  /**
   * Handles the selection event from the combobox and validates
   * the input.
   */
  private class ComboBoxSelectionHandler implements ItemListener
  {
    /**
     * Invoked when an item has been selected or deselected.
     * The code written for this method performs the operations
     * that need to occur when an item is selected (or deselected).
     * 
     * @param e not used
     */
    public void itemStateChanged(ItemEvent e)
    {
      validateInput();
    }
  }

  /** The editor component. */
  private JComboBox content;
  /** The label to name the editor component. */
  private JLabel entryLabel;
  /** A list of selectable options. */
  private List options;
  /** the content pane. */
  private JPanel entryLabelCarrier;

  /**
   * Creates a new enumeration key editor for the given configuration
   * and key definition. The given displayname will be used as label.
   * 
   * @param config the report configuration used to read the values. 
   * @param entry the metadata for the edited key.
   * @param displayName the text for the label.
   */
  public EnumKeyEditor(ReportConfiguration config, 
    EnumConfigDescriptionEntry entry, String displayName)
  {
    super(config, entry);

    JPanel contentPane = new JPanel();
    contentPane.setLayout(new BorderLayout(5,0));
    entryLabel = new JLabel (displayName);
    entryLabel.setToolTipText(entry.getDescription());

    entryLabelCarrier = new JPanel();
    entryLabelCarrier.setLayout(new BorderLayout());
    entryLabelCarrier.add(entryLabel);
    contentPane.add (entryLabelCarrier, BorderLayout.WEST);


    this.options = Arrays.asList(entry.getOptions());

    content = new JComboBox(entry.getOptions());
    content.addItemListener(new ComboBoxSelectionHandler());
    contentPane.add (content, BorderLayout.CENTER);
    setContentPane(contentPane);
    reset();
  }

  /**
   * Restores the original value as read from the report configuration. 
   * @see org.jfree.report.modules.gui.config.editor.KeyEditor#reset()
   */
  public void reset()
  {
    content.setSelectedItem(loadValue());
  }

  /**
   * Checks, whether the input from the combobox is a valid option. 
   *
   */
  protected void validateInput ()
  {
    setValidInput(options.contains(content.getSelectedItem()));
  }

  /**
   * Saves the currently selected option as new value in the report
   * configuration. 
   * @see org.jfree.report.modules.gui.config.editor.KeyEditor#store()
   */
  public void store()
  {
    if (isValidInput())
    {
      if (isEnabled())
      {
        storeValue((String) content.getSelectedItem());
      }
      else
      {
        deleteValue();
      }
    }
  }

  /**
   * Sets whether or not this component is enabled.
   * A component which is enabled may respond to user input,
   * while a component which is not enabled cannot respond to
   * user input.  Some components may alter their visual
   * representation when they are disabled in order to
   * provide feedback to the user that they cannot take input.
   *
   * @see java.awt.Component#isEnabled
   */
  public void setEnabled(boolean enabled)
  {
    super.setEnabled(enabled);
    content.setEnabled(enabled);
  }

  /**
   * Defines the preferred width of the label. 
   * @see org.jfree.report.modules.gui.config.editor.KeyEditor#setLabelWidth(int)
   * 
   * @param width the new preferred width.
   */
  public void setLabelWidth(int width)
  {
    Dimension prefSize = entryLabel.getPreferredSize();
    entryLabelCarrier.setPreferredSize(new Dimension(width, prefSize.height));
  }

  /**
   * Returns the preferred width of the label. 
   * @see org.jfree.report.modules.gui.config.editor.KeyEditor#getLabelWidth()
   * 
   * @return the preferred width.
   */
  public int getLabelWidth()
  {
    Dimension prefSize = entryLabel.getPreferredSize();
    if (prefSize != null)
    {
      return prefSize.width;
    }
    return 0;
  }

}
