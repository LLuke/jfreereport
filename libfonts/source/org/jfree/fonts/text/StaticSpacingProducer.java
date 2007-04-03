/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/liblayout/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 * $Id: StaticSpacingProducer.java,v 1.5 2007/04/02 11:41:20 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.fonts.text;

import org.jfree.fonts.text.SpacingProducer;
import org.jfree.fonts.text.Spacing;

/**
 * Creation-Date: 11.06.2006, 18:37:39
 *
 * @author Thomas Morgner
 */
public class StaticSpacingProducer implements SpacingProducer
{
  private Spacing spacing;

  public StaticSpacingProducer(final Spacing spacing)
  {
    if (spacing == null)
    {
      this.spacing = Spacing.EMPTY_SPACING;
    }
    else
    {
      this.spacing = spacing;
    }
  }

  public Spacing createSpacing(int codePoint)
  {
    return spacing;
  }


  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }
}
