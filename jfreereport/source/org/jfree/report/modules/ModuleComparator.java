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
 * ModuleComparator.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ModuleComparator.java,v 1.1 2003/07/10 20:05:59 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 09.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules;

import java.util.Comparator;

public class ModuleComparator implements Comparator
{
  public ModuleComparator()
  {
  }

  /**
   * Compares its two arguments for order.  Returns a negative integer,
   * zero, or a positive integer as the first argument is less than, equal
   * to, or greater than the second.<p>
   *
   * @param o1 the first object to be compared.
   * @param o2 the second object to be compared.
   * @return a negative integer, zero, or a positive integer as the
   *         first argument is less than, equal to, or greater than the
   *         second.
   * @throws ClassCastException if the arguments' types prevent them from
   *         being compared by this Comparator.
   */
  public int compare(Object o1, Object o2)
  {
    if (o1 == null && o2 == null)
    {
      return 0;
    }
    if (o1 == null)
    {
      return -1;
    }
    if (o2 == null)
    {
      return +1;
    }

    Module mod1 = (Module) o1;
    Module mod2 = (Module) o2;

    if (isBaseModule(mod1, mod2))
    {
      return 1;
    }
    else if (isBaseModule(mod2, mod1))
    {
      return -1;
    }
    else
    {
      return mod1.getModuleClass().compareTo(mod2.getModuleClass());
    }
  }

  private boolean isBaseModule (final Module mod, final ModuleInfo mi)
  {
    ModuleInfo[] info = mod.getRequiredModules();
    for (int i = 0; i < info.length; i++)
    {
      if (info[i].getModuleClass().equals(mi.getModuleClass()))
      {
        return true;
      }
    }
    info = mod.getOptionalModules();
    for (int i = 0; i < info.length; i++)
    {
      if (info[i].getModuleClass().equals(mi.getModuleClass()))
      {
        return true;
      }
    }
    return false;
  }

}
