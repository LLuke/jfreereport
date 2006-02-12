/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * TextSpecification.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: TextSpecification.java,v 1.1 2006/02/12 21:43:10 taqua Exp $
 *
 * Changes
 * -------------------------
 * 21.12.2005 : Initial version
 */
package org.jfree.layouting.model.text;

/**
 * Creation-Date: 21.12.2005, 14:07:31
 *
 * @author Thomas Morgner
 */
public class TextSpecification
{
  private TextAlignmentSpecifcation alignmentSpecifcation;
  private TextDecorationSpecification underlineSpecification;
  private TextDecorationSpecification overlineSpecification;
  private TextDecorationSpecification lineThroughSpecification;
  private TextLayoutSpecification layoutSpecification;
  private TextOverflowSpecification overflowSpecification;
  private TextProcessingSpecification processingSpecification;
  private TextSpacingSpecification spacingSpecification;

  public TextSpecification()
  {
    alignmentSpecifcation = new TextAlignmentSpecifcation();
    underlineSpecification = new TextDecorationSpecification();
    overlineSpecification = new TextDecorationSpecification();
    lineThroughSpecification = new TextDecorationSpecification();
    layoutSpecification = new TextLayoutSpecification();
    overflowSpecification = new TextOverflowSpecification();
    processingSpecification = new TextProcessingSpecification();
    spacingSpecification = new TextSpacingSpecification();
  }

  public TextAlignmentSpecifcation getAlignmentSpecifcation()
  {
    return alignmentSpecifcation;
  }

  public TextDecorationSpecification getUnderlineSpecification()
  {
    return underlineSpecification;
  }

  public TextDecorationSpecification getOverlineSpecification()
  {
    return overlineSpecification;
  }

  public TextDecorationSpecification getLineThroughSpecification()
  {
    return lineThroughSpecification;
  }

  public TextLayoutSpecification getLayoutSpecification()
  {
    return layoutSpecification;
  }

  public TextOverflowSpecification getOverflowSpecification()
  {
    return overflowSpecification;
  }

  public TextProcessingSpecification getProcessingSpecification()
  {
    return processingSpecification;
  }

  public TextSpacingSpecification getSpacingSpecification()
  {
    return spacingSpecification;
  }
}
