/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * DefaultDemoSelector.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.report.demo.util;

import java.util.ArrayList;

/**
 * A simple demo selector implementation. A demo selector is used by the
 * CompoundFrame to collect and display all available demos.
 *
 * @author Thomas Morgner
 */
public class DefaultDemoSelector implements DemoSelector
{
  private ArrayList demos;
  private ArrayList childs;
  private String name;

  public DefaultDemoSelector(final String name)
  {
    if (name == null)
    {
      throw new NullPointerException();
    }
    this.name = name;
    this.demos = new ArrayList();
    this.childs = new ArrayList();
  }

  public String getName()
  {
    return name;
  }

  public void addChild (DemoSelector selector)
  {
    if (selector == null)
    {
      throw new NullPointerException();
    }
    childs.add(selector);
  }

  public DemoSelector[] getChilds()
  {
    return (DemoSelector[]) childs.toArray(new DemoSelector[childs.size()]);
  }

  public void addDemo (DemoHandler handler)
  {
    if (handler == null)
    {
      throw new NullPointerException();
    }
    demos.add(handler);
  }

  public DemoHandler[] getDemos()
  {
    return (DemoHandler[]) demos.toArray(new DemoHandler[demos.size()]);
  }

  public int getChildCount()
  {
    return childs.size();
  }

  public int getDemoCount()
  {
    return demos.size();
  }
}
