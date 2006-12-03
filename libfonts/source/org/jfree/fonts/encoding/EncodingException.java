/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libfonts/
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.fonts.encoding;

import java.io.IOException;

/**
 * Creation-Date: 19.04.2006, 22:03:51
 *
 * @author Thomas Morgner
 */
public class EncodingException extends IOException
{
  /**
   * Constructs an <code>IOException</code> with <code>null</code> as its error
   * detail message.
   */
  public EncodingException()
  {
  }

  /**
   * Constructs an <code>IOException</code> with the specified detail message.
   * The error message string <code>s</code> can later be retrieved by the
   * <code>{@link Throwable#getMessage}</code> method of class
   * <code>java.lang.Throwable</code>.
   *
   * @param s the detail message.
   */
  public EncodingException(String s)
  {
    super(s);
  }
}
