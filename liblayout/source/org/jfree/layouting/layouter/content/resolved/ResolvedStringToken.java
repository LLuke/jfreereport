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
 * ResolvedStringToken.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ResolvedStringToken.java,v 1.1 2006/07/11 13:39:58 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.content.resolved;

import org.jfree.layouting.layouter.content.type.TextType;
import org.jfree.layouting.layouter.content.computed.ComputedToken;

/**
 * Creation-Date: 12.06.2006, 14:38:29
 *
 * @author Thomas Morgner
 */
public class ResolvedStringToken implements ResolvedToken, TextType
{
  private ComputedToken parent;

  private String text;

  public ResolvedStringToken(final ComputedToken parent, final String text)
  {
    if (parent == null)
    {
      throw new NullPointerException();
    }
    this.parent = parent;
    this.text = text;
  }

  public ComputedToken getParent()
  {
    return parent;
  }

  public String getText()
  {
    return text;
  }
}
