/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * LocaleSelectionReportControler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: LocaleSelectionReportControler.java,v 1.1 2005/07/22 16:39:54 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.demo.helper;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.Locale;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.jfree.report.DefaultResourceBundleFactory;
import org.jfree.report.JFreeReport;
import org.jfree.report.modules.gui.base.DefaultReportControler;
import org.jfree.report.modules.gui.base.PreviewProxyBase;
import org.jfree.util.Log;
import org.jfree.ui.KeyedComboBoxModel;
import org.jfree.ui.action.ActionButton;

public class LocaleSelectionReportControler extends DefaultReportControler
{
  private class UpdateAction extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default description string and default
     * icon.
     */
    public UpdateAction ()
    {
      putValue(Action.NAME, "Update");
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      final PreviewProxyBase base = getPreviewBase();
      if (base == null)
      {
        return;
      }
      final JFreeReport report = base.getReport();
      final DefaultResourceBundleFactory rfact =
              new DefaultResourceBundleFactory(getSelectedLocale());
      report.setResourceBundleFactory(rfact);
      try
      {
        base.refresh();
      }
      catch (Exception ex)
      {
        Log.error("Unable to refresh the report.", ex);
      }
    }
  }

  private KeyedComboBoxModel localesModel;
  private UpdateAction updateAction;

  public LocaleSelectionReportControler ()
  {
    localesModel = createLocalesModel();
    updateAction = new UpdateAction();

    final JComboBox cbx = new JComboBox(localesModel);
    setLayout(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    add(new JLabel("Select locale:"), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    add(cbx, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.EAST;
    add(new ActionButton(updateAction));

  }

  private KeyedComboBoxModel createLocalesModel ()
  {
    final KeyedComboBoxModel cn = new KeyedComboBoxModel();
    final Locale[] locales = Locale.getAvailableLocales();
    for (int i = 0; i < locales.length; i++)
    {
      final Locale locale = locales[i];
      cn.add(locale, locale.getDisplayName());
    }
    cn.setSelectedKey(Locale.getDefault());
    return cn;
  }

  public Locale getSelectedLocale ()
  {
    final Locale l = (Locale) localesModel.getSelectedKey();
    if (l == null)
    {
      return Locale.getDefault();
    }
    return l;
  }

  public void setSelectedLocale (final Locale locale)
  {
    if (locale == null)
    {
      throw new NullPointerException();
    }
    localesModel.setSelectedKey(locale);
  }
}
