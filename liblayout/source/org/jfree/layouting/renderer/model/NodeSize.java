/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * NodeSize.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
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
package org.jfree.layouting.renderer.model;

/**
 * A class, that encapsulates size information.
 *
 * @author Thomas Morgner
 */
public class NodeSize
{
  private long preferredMajor;
  private long preferredMinor;
  private long minimumMajor;
  private long minimumMinor;
  private long maximumMajor;
  private long maximumMinor;

  public NodeSize()
  {
  }

  public long getPreferredMajor()
  {
    return preferredMajor;
  }

  public void setPreferredMajor(final long preferredMajor)
  {
    this.preferredMajor = preferredMajor;
  }

  public long getPreferredMinor()
  {
    return preferredMinor;
  }

  public void setPreferredMinor(final long preferredMinor)
  {
    this.preferredMinor = preferredMinor;
  }

  public long getMinimumMajor()
  {
    return minimumMajor;
  }

  public void setMinimumMajor(final long minimumMajor)
  {
    this.minimumMajor = minimumMajor;
  }

  public long getMinimumMinor()
  {
    return minimumMinor;
  }

  public void setMinimumMinor(final long minimumMinor)
  {
    this.minimumMinor = minimumMinor;
  }

  public long getMaximumMajor()
  {
    return maximumMajor;
  }

  public void setMaximumMajor(final long maximumMajor)
  {
    this.maximumMajor = maximumMajor;
  }

  public long getMaximumMinor()
  {
    return maximumMinor;
  }

  public void setMaximumMinor(final long maximumMinor)
  {
    this.maximumMinor = maximumMinor;
  }
}
