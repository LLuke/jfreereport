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
 * PackageState.java
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
 * 10.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules;

import java.util.Comparator;

import org.jfree.report.util.Log;

public class PackageState implements Comparable
{
  private static final int STATE_NEW = 0;
  private static final int STATE_CONFIGURED = 1;
  private static final int STATE_INITIALIZED = 2;
  private static final int STATE_ERROR = -2;

  private Module module;
  private int state;
  private static ModuleComparator comparator;

  protected Comparator getComparator ()
  {
    if (comparator == null)
    {
      comparator = new ModuleComparator();
    }
    return comparator;
  }

  public PackageState(Module module)
  {
    this.module = module;
    this.state = STATE_NEW;
  }

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

  public Module getModule()
  {
    return module;
  }

  public int getState()
  {
    return state;
  }

  public boolean initialize ()
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
        Log.warn ("Unable to initialize the module " + module.getName(), me);
        state = STATE_ERROR;
      }
    }
    return false;
  }

  /**
   * Compares this object with the specified object for order.  Returns a
   * negative integer, zero, or a positive integer as this object is less
   * than, equal to, or greater than the specified object.<p>
   *
   * In the foregoing description, the notation
   * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
   * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
   * <tt>0</tt>, or <tt>1</tt> according to whether the value of <i>expression</i>
   * is negative, zero or positive.
   *
   * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
   * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
   * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
   * <tt>y.compareTo(x)</tt> throws an exception.)<p>
   *
   * The implementor must also ensure that the relation is transitive:
   * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
   * <tt>x.compareTo(z)&gt;0</tt>.<p>
   *
   * Finally, the implementer must ensure that <tt>x.compareTo(y)==0</tt>
   * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
   * all <tt>z</tt>.<p>
   *
   * It is strongly recommended, but <i>not</i> strictly required that
   * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
   * class that implements the <tt>Comparable</tt> interface and violates
   * this condition should clearly indicate this fact.  The recommended
   * language is "Note: this class has a natural ordering that is
   * inconsistent with equals."
   *
   * @param   o the Object to be compared.
   * @return  a negative integer, zero, or a positive integer as this object
   *		is less than, equal to, or greater than the specified object.
   *
   * @throws ClassCastException if the specified object's type prevents it
   *         from being compared to this Object.
   */
  public int compareTo(Object o)
  {
    if (o == null)
    {
      return 1;
    }
    PackageState state = (PackageState) o;
    return getComparator().compare(this.getModule(), state.getModule());
  }
}
