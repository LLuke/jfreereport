/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * --------------------
 * ActionDowngrade.java
 * --------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ActionDowngrade.java,v 1.5 2002/12/11 00:41:42 mungady Exp $
 *
 * ChangeLog
 * ---------
 */
package com.jrefinery.report.util;

import javax.swing.Action;

/**
 * Defines the 2 new constants introduced by Sun in version 1.3 of the J2SDK.
 *
 * @author Thomas Morgner
 */
public interface ActionDowngrade extends Action
{
  /**
   * The key used for storing a <code>KeyStroke</code> to be used as the
   * accelerator for the action.
   */
  public static final String ACCELERATOR_KEY = "AcceleratorKey";

  /**
   * The key used for storing an int key code to be used as the mnemonic
   * for the action.
   */
  public static final String MNEMONIC_KEY = "MnemonicKey";
}
