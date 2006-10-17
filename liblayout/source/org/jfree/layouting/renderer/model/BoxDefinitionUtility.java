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
 * BoxDefinitionUtility.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: BoxDefinitionUtility.java,v 1.1 2006/07/11 13:51:02 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model;

/**
 * Creation-Date: 25.06.2006, 15:50:14
 *
 * @author Thomas Morgner
 */
public class BoxDefinitionUtility
{
  private BoxDefinitionUtility()
  {
  }

  public int countHorizontalVariables (BoxDefinition boxDefinition)
  {
    int retval = 0;
//    if (boxDefinition.getMarginLeft() == BoxDefinition.AUTO_VALUE)
//    {
//      retval += 1;
//    }
////    if (boxDefinition.getPaddingLeft() == BoxDefinition.AUTO_VALUE)
////    {
////      retval += 1;
////    }
////    if (boxDefinition.getPaddingRight() == BoxDefinition.AUTO_VALUE)
////    {
////      retval += 1;
////    }
//    if (boxDefinition.getMarginRight() == BoxDefinition.AUTO_VALUE)
//    {
//      retval += 1;
//    }

    return retval;
  }

  public int countVerticalVariables (BoxDefinition boxDefinition)
  {
    int retval = 0;
//    if (boxDefinition.getMarginTop() == BoxDefinition.AUTO_VALUE)
//    {
//      retval += 1;
//    }
////    if (boxDefinition.getPaddingLeft() == BoxDefinition.AUTO_VALUE)
////    {
////      retval += 1;
////    }
////    if (boxDefinition.getPaddingRight() == BoxDefinition.AUTO_VALUE)
////    {
////      retval += 1;
////    }
//    if (boxDefinition.getMarginBottom() == BoxDefinition.AUTO_VALUE)
//    {
//      retval += 1;
//    }

    return retval;
  }
}
