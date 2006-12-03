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
 * A flow element defines a new <em>normal-flow</em>. The document root itself
 * and each element that is <em>flowing</em> or <em>absolutely</em> positioned
 * (where the static positioning is considered absolutly positioned).
 *
 * A flow context is a stronger context than a block context, but each flowing
 * element is also a block context.
 *
 * At the moment, this is a tagging class; it may or may not become more
 * relevant in the future.
 *
 * @author Thomas Morgner
 */
public class DisplayFlowElement extends DisplayBlockElement
{
  public DisplayFlowElement(final LayoutContext context)
  {
    super(context);
  }

  protected void signalFinish() throws NormalizationException
  {
    getRootFlow().getContentGenerator().finishedFlow();
  }

}
