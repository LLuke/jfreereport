/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ConfigStoreException.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ConfigStoreException.java,v 1.2 2003/08/19 21:01:17 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 14-Jun-2003 : Initial version
 *  
 */

package org.jfree.report.modules.misc.configstore.base;

import org.jfree.util.StackableException;

/**
 * The config store exception is throwns if an error prevents an operation
 * on the current configuration storage provider.
 * 
 * @author Thomas Morgner
 */
public class ConfigStoreException extends StackableException
{
  /**
   * DefaultConstructor.
   */
  public ConfigStoreException()
  {
  }

  /**
   * Creates a config store exception with the given message and root
   * exception.
   *  
   * @param s the exception message.
   * @param e the exception that caused all the trouble.
   */
  public ConfigStoreException(String s, Exception e)
  {
    super(s, e);
  }

  /**
   * Creates a config store exception with the given message.
   * 
   * @param s the message.
   */
  public ConfigStoreException(String s)
  {
    super(s);
  }
}
