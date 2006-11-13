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
 * JStatusBar.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JStatusBar.java,v 1.6 2005/12/09 20:05:32 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.gui.swing.common;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.jfree.report.modules.gui.common.IconTheme;

public class JStatusBar extends JComponent
{
  public static final int TYPE_ERROR = 3;
  public static final int TYPE_WARNING = 2;
  public static final int TYPE_INFORMATION = 1;
  public static final int TYPE_NONE = 0;

  private JComponent otherComponents;
  private JLabel statusHolder;
  private IconTheme theme;
  private Locale locale;

  public JStatusBar (final IconTheme theme)
  {
    this(theme, Locale.getDefault());
  }

  public JStatusBar (final IconTheme theme,
                     final Locale locale)
  {
    this.locale = locale;
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createMatteBorder
            (1, 0, 0, 0, UIManager.getDefaults().getColor("controlShadow")));
    statusHolder = new JLabel(" ");
    statusHolder.setMinimumSize(new Dimension(0, 20));
    add(statusHolder, BorderLayout.CENTER);

    otherComponents = new JPanel();
    add(otherComponents, BorderLayout.EAST);
    this.theme = theme;
  }

  protected IconTheme getTheme()
  {
    return theme;
  }

  public JComponent getExtensionArea ()
  {
    return otherComponents;
  }

  public void setStatus (final int type, final String text)
  {
//    Log.debug ("Setting status: " + type + ", " + text);
    if (type == TYPE_ERROR)
    {
      final Icon res = getTheme().getSmallIcon(locale, "statusbar.errorIcon");
      statusHolder.setIcon(res);
    }
    else if (type == TYPE_WARNING)
    {
      final Icon res = getTheme().getSmallIcon(locale, "statusbar.warningIcon");
      statusHolder.setIcon(res);
    }
    else if (type == TYPE_INFORMATION)
    {
      final Icon res = getTheme().getSmallIcon(locale, "statusbar.informationIcon");
      statusHolder.setIcon(res);
    }
    else
    {
      final Icon res = getTheme().getSmallIcon(locale, "statusbar.otherIcon");
      statusHolder.setIcon(res);
    }
    statusHolder.setText(text);
  }

  public void clear ()
  {
    setStatus(TYPE_NONE, " ");
  }
}
