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
 * ModuleInitializeException.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ModuleInitializeException.java,v 1.1 2003/07/07 22:44:05 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 05-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.modules;

import org.jfree.util.StackableException;

/**
 * This exception is thrown when the module initialization encountered an
 * unrecoverable error which prevents the module from being used.
 * 
 * @author Thomas Morgner
 */
public class ModuleInitializeException extends StackableException
{
  /**
   * Creates a ModuleInitializeException with no message and no base
   * exception.
   */
  public ModuleInitializeException()
  {
  }

  /**
   * Creates a ModuleInitializeException with the given message and base
   * exception.
   * 
   * @param s the message
   * @param e the root exception 
   */
  public ModuleInitializeException(String s, Exception e)
  {
    super(s, e);
  }

  /**
   * Creates a ModuleInitializeException with the given message and no base
   * exception.
   * 
   * @param s the exception message
   */
  public ModuleInitializeException(String s)
  {
    super(s);
  }
}
