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
 * ProcessAttributeName.java
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
package org.jfree.layouting.model;

/**
 * Creation-Date: 10.12.2005, 23:37:28
 *
 * @author Thomas Morgner
 */
public class ProcessAttributeName
{
  public static final ProcessAttributeName LANGUAGE =
          new ProcessAttributeName("language");
  public static final ProcessAttributeName PSEUDO_CLASS =
          new ProcessAttributeName("pseudo-class");
  public static final ProcessAttributeName TYPE =
          new ProcessAttributeName("type");
  public static final ProcessAttributeName LAYOUT_CONTEXT =
          new ProcessAttributeName("layout-context");
  public static final ProcessAttributeName ELEMENT_CONTEXT =
          new ProcessAttributeName("element-context");
  public static final ProcessAttributeName INPUT_SAVE_POINT =
          new ProcessAttributeName("input-savepoint");

  private final String myName; // for debug only

  private ProcessAttributeName(String name)
  {
    myName = name;
  }

  public String toString()
  {
    return myName;
  }
}
