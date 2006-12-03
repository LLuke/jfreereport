/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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

package org.jfree.layouting.normalizer.displaymodel;

import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.normalizer.content.NormalizationException;

/**
 * A marker element that indicates that the current flow is suspended
 * until this element has been handled. (Use this for all display:none
 * or comment nodes.
 *
 * @author Thomas Morgner
 */
public class DisplayPassThroughElement extends DisplayElement
{
  public DisplayPassThroughElement(final LayoutContext context)
  {
    super(context);
  }

  public void add(DisplayNode node) throws NormalizationException
  {

  }

  protected void signalFinish() throws NormalizationException
  {

  }

  protected void signalStart() throws NormalizationException
  {

  }
}
