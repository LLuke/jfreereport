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
 * TextKeyEditor.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 31.08.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import org.jfree.report.modules.gui.config.model.ConfigDescriptionEntry;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.Log;

public class TextKeyEditor extends AbstractKeyEditor
{
  private class DocumentChangeHandler implements DocumentListener
  {
    public DocumentChangeHandler()
    {
    }

    /**
     * Gives notification that an attribute or set of attributes changed.
     *
     * @param e the document event
     */
    public void changedUpdate(DocumentEvent e)
    {
      validateContent();
    }

    /**
     * Gives notification that a portion of the document has been
     * removed.  The range is given in terms of what the view last
     * saw (that is, before updating sticky positions).
     *
     * @param e the document event
     */
    public void removeUpdate(DocumentEvent e)
    {
      validateContent();
    }

    /**
     * Gives notification that there was an insert into the document.  The
     * range given by the DocumentEvent bounds the freshly inserted region.
     *
     * @param e the document event
     */
    public void insertUpdate(DocumentEvent e)
    {
      validateContent();
    }
  }

  private JTextField content;
  private JLabel entryLabel;
  private JPanel entryLabelCarrier;

  public TextKeyEditor(ReportConfiguration config, ConfigDescriptionEntry entry, String displayName)
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

    content = new JTextField();
    content.getDocument().addDocumentListener(new DocumentChangeHandler());

    contentPane.add (content, BorderLayout.CENTER);
    setContentPane(contentPane);
    reset();
  }

  public void validateContent ()
  {
    Log.debug ("Do something else.");
    setValidInput(true);
  }

  public void reset()
  {
    content.setText(loadValue());
  }

  public void store()
  {
    if (isValidInput())
    {
      if (isEnabled())
      {
        storeValue(content.getText());
      }
      else
      {
        deleteValue();
      }
    }
  }

  public String getContent()
  {
    return content.getText();
  }

  /**
   * Sets whether or not this component is enabled.
   * A component which is enabled may respond to user input,
   * while a component which is not enabled cannot respond to
   * user input.  Some components may alter their visual
   * representation when they are disabled in order to
   * provide feedback to the user that they cannot take input.
   *
   * @see Component#isEnabled
   */
  public void setEnabled(boolean enabled)
  {
    super.setEnabled(enabled);
    content.setEnabled(enabled);
  }

  public void setLabelWidth(int width)
  {
    Dimension prefSize = entryLabel.getPreferredSize();
    entryLabelCarrier.setPreferredSize(new Dimension(width, prefSize.height));
  }

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
