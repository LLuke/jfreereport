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
 * $Id: FontDataInputSource.java,v 1.5 2006/12/03 18:11:59 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.fonts.io;

import java.io.IOException;

/**
 * Creation-Date: 15.12.2005, 15:48:56
 *
 * @author Thomas Morgner
 */
public interface FontDataInputSource
{
  public void readFullyAt (long position, byte[] buffer, int offset, int length)
          throws IOException;

  /**
   * Reads a single byte, returns -1 if the end of the stream as been reached.
   * @param position
   * @return
   * @throws IOException
   */
  public int readAt (long position) throws IOException;

  public int readAt (long position, byte[] buffer, int offset, int length) throws IOException;

  public void dispose();

  public String getFileName();

  public boolean equals (Object o);

  public int hashCode();
}
