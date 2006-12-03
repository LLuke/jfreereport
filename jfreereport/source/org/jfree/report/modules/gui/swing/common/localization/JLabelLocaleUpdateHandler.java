/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.modules.gui.swing.common.localization;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.awt.IllegalComponentStateException;
import javax.swing.JLabel;

/**
 * Creation-Date: 30.11.2006, 13:04:07
 *
 * @author Thomas Morgner
 */
public class JLabelLocaleUpdateHandler implements PropertyChangeListener
{
  private String resourceBundleName;
  private String resourceKey;
  private JLabel target;

  public JLabelLocaleUpdateHandler(final JLabel target,
                                   final String resourceBundleName,
                                   final String resourceKey)
  {
    this.target = target;
    this.resourceBundleName = resourceBundleName;
    this.resourceKey = resourceKey;
  }

  /**
   * This method gets called when a bound property is changed.
   *
   * @param evt A PropertyChangeEvent object describing the event source and the
   *            property that has changed.
   */

  public void propertyChange(PropertyChangeEvent evt)
  {
    try
    {
      final Locale locale = target.getLocale();
      final ResourceBundle bundle =
          ResourceBundle.getBundle(resourceBundleName, locale);
      final String string = bundle.getString(resourceKey);
      target.setText(string);
    }
    catch(IllegalComponentStateException ice)
    {
      target.setText("!ERROR: No Parent: " + resourceKey);
    }
    catch(MissingResourceException mre)
    {
      target.setText("!ERROR: MissingResource: " + resourceKey);
    }

  }
}
