/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -------------------
 * GeneralElement.java
 * -------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: GeneralElement.java,v 1.5 2002/06/04 21:44:34 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 10-May-2002 : Removed all complex constructors
 * 20-May-2002 : Declared deprecated. This class is no longer used. The ItemFactory produces
 *               TextElements instead which get different filters attached.
 * 04-Jun-2002 : Removed unused imports.
 */

package com.jrefinery.report;

/**
 * This class displays data items that are not Dates, Numbers or Strings.  That is, general
 * java.lang.Object subclasses - converts to strings using the toString() method.
 * <p>
 * This elements functionalty is not achived by using a plain TextElement without any
 * additional filter attached.
 *
 * @author DG
 *
 * @deprecated form this element by stacking it together by using filters
 */
public class GeneralElement extends DataElement
{

  /**
   * Constructs a general element.
   * @deprecated form this element by stacking it together by using filters
   */
  public GeneralElement ()
  {
  }
}
