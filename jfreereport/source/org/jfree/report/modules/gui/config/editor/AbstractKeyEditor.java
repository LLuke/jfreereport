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
 * AbstractKeyEditor.java
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
import java.util.ResourceBundle;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.report.modules.gui.config.model.ConfigDescriptionEntry;
import org.jfree.report.modules.gui.config.resources.ConfigResources;
import org.jfree.report.util.ImageUtils;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;

public abstract class AbstractKeyEditor extends JComponent
{
  private static final String RESOURCE_BUNDLE =
      ConfigResources.class.getName();

  private Icon errorIcon;
  private Icon emptyIcon;

  private ReportConfiguration config;
  private ConfigDescriptionEntry entry;
  private boolean validInput;
  private JLabel stateLabel;
  private ResourceBundle resources;

  public AbstractKeyEditor (ReportConfiguration config, ConfigDescriptionEntry entry)
  {
    this.resources = ResourceBundle.getBundle(RESOURCE_BUNDLE);
    this.setLayout(new BorderLayout());
    this.config = config;
    this.entry = entry;
    stateLabel = new JLabel(getEmptyIcon());
  }

  protected Icon getEmptyIcon ()
  {
    if (emptyIcon == null)
    {
      Icon errorIcon = getErrorIcon();
      int width = errorIcon.getIconWidth();
      int height = errorIcon.getIconHeight();
      emptyIcon = ImageUtils.createTransparentIcon(width, height);
    }
    return emptyIcon;
  }

  protected Icon getErrorIcon ()
  {
    if (errorIcon == null)
    {
      errorIcon = (Icon) resources.getObject("default-editor.error-icon");
    }
    return errorIcon;
  }

  protected void setContentPane(JPanel contentPane)
  {
    removeAll();
    add (contentPane, BorderLayout.CENTER);
    add (stateLabel, BorderLayout.EAST);
  }


  public ReportConfiguration getConfig()
  {
    return config;
  }

  public ConfigDescriptionEntry getEntry()
  {
    return entry;
  }

  protected String loadValue ()
  {
    return config.getConfigProperty(entry.getKeyName());
  }

  protected void storeValue (String o)
  {
    config.setConfigProperty(entry.getKeyName(), o);
  }

  protected void deleteValue ()
  {
    config.setConfigProperty(entry.getKeyName(), null);
  }

  public abstract void reset ();
  public abstract void store ();

  public boolean isValidInput()
  {
    return validInput;
  }

  protected void setValidInput(boolean validInput)
  {
    if (this.validInput != validInput)
    {
      boolean oldValue = this.validInput;
      this.validInput = validInput;
      firePropertyChange("ValidInput", oldValue, validInput);
      if (this.validInput == false)
      {
        stateLabel.setIcon(getErrorIcon());
      }
      else
      {
        stateLabel.setIcon(getEmptyIcon());
      }
    }
    else
    {
      Log.debug ("No change: " + validInput);
    }
  }

  public boolean isDefined ()
  {
    return config.isLocallyDefined(entry.getKeyName());
  }

  public abstract void setLabelWidth (int width);
  public abstract int getLabelWidth ();
}
