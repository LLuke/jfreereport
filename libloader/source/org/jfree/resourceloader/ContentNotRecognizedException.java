/**
 * ================================================
 * LibLoader : a free Java resource loading library
 * ================================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libloader/
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
 *
 * ------------
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.resourceloader;

/**
 * The ContentNotRecognizedException signals that none of the selected resource
 * factories was able to handle the request.
 *
 * @author Thomas Morgner
 */
public class ContentNotRecognizedException extends ResourceCreationException
{
  /**
   * Creates a ContentNotRecognizedException  with no message and no parent.
   */
  public ContentNotRecognizedException()
  {
  }

  /**
   * Creates an ContentNotRecognizedException.
   *
   * @param message the exception message.
   * @param ex      the parent exception.
   */
  public ContentNotRecognizedException(final String message, final Exception ex)
  {
    super(message, ex);
  }

  /**
   * Creates an ContentNotRecognizedException.
   *
   * @param message the exception message.
   */
  public ContentNotRecognizedException(final String message)
  {
    super(message);
  }
}