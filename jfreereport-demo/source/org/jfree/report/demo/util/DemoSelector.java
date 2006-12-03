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
 * DemoSelector.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.report.demo.util;

/**
 * A demo selector holds information about a set of demos using the DemoHandler
 * interface and child demo selectors holding other releated demos.
 *
 * @author Thomas Morgner
 */
public interface DemoSelector
{
  public String getName();
  public DemoSelector[] getChilds();
  public int getChildCount();
  public DemoHandler[] getDemos();
  public int getDemoCount();
}
