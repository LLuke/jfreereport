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
 * ConfigTreeModelException.java
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
 * 30.08.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config.model;

import org.jfree.util.StackableException;

public class ConfigTreeModelException extends StackableException
{
  /**
   * Creates a StackableRuntimeException with no message and no parent.
   */
  public ConfigTreeModelException()
  {
  }

  /**
   * Creates an exception.
   *
   * @param message  the exception message.
   * @param ex  the parent exception.
   */
  public ConfigTreeModelException(String message, Exception ex)
  {
    super(message, ex);
  }

  /**
   * Creates an exception.
   *
   * @param message  the exception message.
   */
  public ConfigTreeModelException(String message)
  {
    super(message);
  }
}
