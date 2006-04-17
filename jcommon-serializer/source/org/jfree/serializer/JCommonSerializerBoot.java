/**
 * ===================================================
 * JCommon-Serializer : a free serialization framework
 * ===================================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/jcommon-serializer/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2006, by Object Refinery Limited and Pentaho Corporation.
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
 * JCommonSerializerBoot.java
 * ------------
 * (C) Copyright 2006, by Object Refinery Limited and Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.serializer;

import org.jfree.base.AbstractBoot;
import org.jfree.base.BootableProjectInfo;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 17.02.2006, 21:54:24
 *
 * @author Thomas Morgner
 */
public class JCommonSerializerBoot extends AbstractBoot
{
  private static JCommonSerializerBoot instance;

  public static synchronized JCommonSerializerBoot getInstance()
  {
    if (instance == null)
    {
      instance = new JCommonSerializerBoot();
    }
    return instance;
  }

  private JCommonSerializerBoot()
  {
  }

  /**
   * Loads the configuration. This will be called exactly once.
   *
   * @return The configuration.
   */
  protected Configuration loadConfiguration()
  {
    return createDefaultHierarchicalConfiguration
            ("/org/jfree/serializer/jcommon-serializer.properties",
             "/jcommon-serializer.properties", true);
  }

  /** Performs the boot. */
  protected void performBoot()
  {

  }

  /**
   * Returns the project info.
   *
   * @return The project info.
   */
  protected BootableProjectInfo getProjectInfo()
  {
    return JCommonSerializerInfo.getInstance();
  }
}
