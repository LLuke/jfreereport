/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * AbstractActionDowngrade.java
 * ----------------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: AbstractActionDowngrade.java,v 1.4 2003/08/31 19:27:57 taqua Exp $
 *
 * Changes
 * -------
 * 30-Aug-2002 : Initial version
 * 10-Dec-2002 : Updated Javadocs (DG);
 *
 */

package org.jfree.report.modules.gui.base.components;

import javax.swing.AbstractAction;

/**
 * A class that allows Action features introduced in JDK 1.3 to be used with JDK 1.2.2, by
 * defining the two new constants introduced by Sun in JDK 1.3.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractActionDowngrade extends AbstractAction implements ActionDowngrade
{
  // kills a compile error for JDK's >= 1.3
  // ambiguous reference error ...
  /**
   * The key used for storing a <code>KeyStroke</code> to be used as the
   * accelerator for the action.
   */
  public static final String ACCELERATOR_KEY = ActionDowngrade.ACCELERATOR_KEY;

  /**
   * The key used for storing an int key code to be used as the mnemonic
   * for the action.
   */
  public static final String MNEMONIC_KEY = ActionDowngrade.MNEMONIC_KEY;

  /**
   * Creates a new action with a default (transparent) icon.
   */
  protected AbstractActionDowngrade()
  {
  }
}
