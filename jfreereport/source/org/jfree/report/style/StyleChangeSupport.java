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
 * -----------------------
 * StyleChangeSupport.java
 * -----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: StyleChangeSupport.java,v 1.3 2003/08/25 14:29:33 taqua Exp $
 *
 * Changes
 * -------
 * 18.03.2003 : Initial version
 */
package org.jfree.report.style;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * A utility class for managing a collection of {@link StyleChangeListener} objects.
 *
 * @author Thomas Morgner.
 */
public class StyleChangeSupport
{
  /** Storage for the listeners. */
  private ArrayList listeners;

  /** The source. */
  private final ElementStyleSheet source;

  /**
   * Creates a new support object.
   *
   * @param source  the source of change events.
   */
  public StyleChangeSupport(final ElementStyleSheet source)
  {
    this.source = source;
  }

  /**
   * Adds a listener.
   *
   * @param l  the listener.
   */
  public void addListener(final StyleChangeListener l)
  {
    if (l == null)
    {
      throw new NullPointerException("Listener == null");
    }
    if (listeners == null)
    {
      listeners = new ArrayList(5);
    }
    listeners.add(new WeakReference(l));
  }

  /**
   * Removes a listener.
   *
   * @param l  the listener.
   */
  public void removeListener(final StyleChangeListener l)
  {
    if (l == null)
    {
      throw new NullPointerException("Listener == null");
    }
    if (listeners == null)
    {
      return;
    }
    listeners.remove(l);
  }

  /**
   * Notifies all listeners that a style has changed.
   *
   * @param key  the style key.
   * @param value  the new style value.
   */
  public void fireStyleChanged(final StyleKey key, final Object value)
  {
    if (listeners == null)
    {
      return;
    }
    ArrayList removeList = null;

    for (int i = 0; i < listeners.size(); i++)
    {
      final WeakReference ref = (WeakReference) listeners.get(i);
      final StyleChangeListener l = (StyleChangeListener) ref.get();
      if (l != null)
      {
        l.styleChanged(source, key, value);
      }
      else
      {
        if (removeList == null)
        {
          removeList = new ArrayList(5);
        }
        removeList.add(ref);
      }
    }
    if (removeList != null)
    {
      listeners.removeAll(removeList);
    }
  }

  /**
   * Notifies all listeners that a style has been removed.
   *
   * @param key  the style key.
   */
  public void fireStyleRemoved(final StyleKey key)
  {
    if (listeners == null)
    {
      return;
    }
    ArrayList removeList = null;

    for (int i = 0; i < listeners.size(); i++)
    {
      final WeakReference ref = (WeakReference) listeners.get(i);
      final StyleChangeListener l = (StyleChangeListener) ref.get();
      if (l != null)
      {
        l.styleRemoved(source, key);
      }
      else
      {
        if (removeList == null)
        {
          removeList = new ArrayList(5);
        }
        removeList.add(ref);
      }
    }
    if (removeList != null)
    {
      listeners.removeAll(removeList);
    }
  }
}
