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
 * PackageState.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PackageState.java,v 1.4 2003/08/24 15:08:18 taqua Exp $
 *
 * Changes
 * -------------------------
 * 10-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules;

import java.util.Comparator;

import org.jfree.report.util.Log;

/**
 * The package state class is used by the package manager to keep track of
 * the activation level of the installed packages.
 *
 * @author Thomas Morgner
 */
public class PackageState implements Comparable
{
  /** A constant defining that the package is new. */
  public static final int STATE_NEW = 0;
  /** A constant defining that the package has been loaded and configured. */
  public static final int STATE_CONFIGURED = 1;
  /** A constant defining that the package was initialized and is ready to use. */
  public static final int STATE_INITIALIZED = 2;
  /** A constant defining that the package produced an error and is not available. */
  public static final int STATE_ERROR = -2;

  /** The module class that contains the package information. */
  private final Module module;
  /** The state of the module. */
  private int state;
  /** A reference to the module comparator to sort modules by dependency. */
  private static ModuleComparator comparator;

  /**
   * Provides a singleton interface to the comparator.
   *
   * @return the module comparator.
   */
  protected static Comparator getComparator()
  {
    if (comparator == null)
    {
      comparator = new ModuleComparator();
    }
    return comparator;
  }

  /**
   * Creates a new package state for the given module. The module state will
   * be initialized to STATE_NEW.
   *
   * @param module the module.
   */
  public PackageState(final Module module)
  {
    this.module = module;
    this.state = STATE_NEW;
  }

  /**
   * Configures the module and raises the state to STATE_CONFIGURED if the
   * module is not yet configured.
   *
   * @return true, if the module was configured, false otherwise.
   */
  public boolean configure()
  {
    if (state == STATE_NEW)
    {
      module.configure();
      state = STATE_CONFIGURED;
      return true;
    }
    return false;
  }

  /**
   * Returns the module managed by this state implementation.
   *
   * @return the module.
   */
  public Module getModule()
  {
    return module;
  }

  /**
   * Returns the current state of the module. This method returns either
   * STATE_NEW, STATE_CONFIGURED, STATE_INITIALIZED or STATE_ERROR.
   *
   * @return the module state.
   */
  public int getState()
  {
    return state;
  }

  /**
   * Initializes the contained module and raises the set of the module to
   * STATE_INITIALIZED, if the module was not yet initialized. In case of an
   * error, the module state will be set to STATE_ERROR and the module will
   * not be available.
   *
   * @return true, if the module was successfully initialized, false otherwise.
   */
  public boolean initialize()
  {
    if (state == STATE_CONFIGURED)
    {
      try
      {
        module.initialize();
        state = STATE_INITIALIZED;
        return true;
      }
      catch (ModuleInitializeException me)
      {
        Log.warn("Unable to initialize the module " + module.getName(), me);
        state = STATE_ERROR;
      }
    }
    return false;
  }

  /**
   * Compares the modules of the package state by using the module comparator.
   *
   * @param   o the Object to be compared.
   * @return  a negative integer, zero, or a positive integer as this object
   *          is less than, equal to, or greater than the specified object.
   *
   * @throws ClassCastException if the specified object's type prevents it
   *         from being compared to this Object.
   */
  public int compareTo(final Object o)
  {
    if (o == null)
    {
      return 1;
    }
    final PackageState state = (PackageState) o;
    return getComparator().compare(this.getModule(), state.getModule());
  }
}
