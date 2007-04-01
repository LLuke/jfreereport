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
 * $Id$
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input;

import java.io.Serializable;

import org.jfree.resourceloader.Resource;
import org.jfree.ui.Drawable;

/**
 * The image data is used whenever libLayout wants to display graphics content.
 * Graphics content includes drawables and image data - as both are static
 * graphical content.
 *
 * @author Thomas Morgner
 */
public interface ImageData extends Drawable, Serializable
{
  /**
   * Returns the resource definition that was used to load the image.
   * Return null, if there was no resource loader involved. (This covers both
   * invalid/empty content and generated content.)
   * @return
   */
  public Resource getSource();

  /**
   * Returns the width of the image in micro-dots.
   *
   * @return
   */
  public long getWidth();

  /**
   * Returns the height of the image in micro-dots.
   *
   * @return
   */
  public long getHeight();
}
