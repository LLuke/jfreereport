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
 * StaticStringToken.java
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
package org.jfree.layouting.layouter.content.statics;

import org.jfree.layouting.layouter.content.type.TextType;

/**
 * Static text. All CDATA and all constant strings from the 'content'
 * style-definition result in StaticTextTokens.
 *
 * @author Thomas Morgner
 */
public class StaticTextToken extends StaticToken implements TextType
{
  private String text;

  public StaticTextToken(String text)
  {
    if (text == null)
    {
      throw new NullPointerException();
    }
    this.text = text;
  }

  public String getText()
  {
    return text;
  }

  public String toString ()
  {
    return "org.jfree.layouting.layouter.content.statics.StaticTextToken=" +
            "{text='" + text + "'}";
  }
}
