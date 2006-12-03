/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.values;

/**
 * Creation-Date: 23.11.2005, 11:41:24
 *
 * @author Thomas Morgner
 */
public class CSSNumericType extends CSSType
{
  public static final CSSNumericType NUMBER = new CSSNumericType("");
  public static final CSSNumericType PERCENTAGE = new CSSNumericType("%");
  public static final CSSNumericType EM = new CSSNumericType("em");
  public static final CSSNumericType EX = new CSSNumericType("ex");
  public static final CSSNumericType PX = new CSSNumericType("px");

  public static final CSSNumericType CM = new CSSNumericType("cm");
  public static final CSSNumericType MM = new CSSNumericType("mm");
  public static final CSSNumericType INCH = new CSSNumericType("inch");

  public static final CSSNumericType PT = new CSSNumericType("pt");
  public static final CSSNumericType PC = new CSSNumericType("pc");

  public static final CSSNumericType DEG = new CSSNumericType("deg");

  protected CSSNumericType(String name)
  {
    super(name);
  }

}
