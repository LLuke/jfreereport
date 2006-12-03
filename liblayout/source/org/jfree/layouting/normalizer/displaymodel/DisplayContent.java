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

import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.normalizer.content.NormalizationException;

/**
 * A tagging class that separates Elements from content. All renderable content
 * is specified as instances of this class.
 *
 * @author Thomas Morgner
 */
public class DisplayContent extends DisplayNode
{
  private ContentToken content;

  public DisplayContent(final LayoutContext layoutContext,
                        final ContentToken content)
  {
    super(layoutContext);
    if (content == null)
    {
      throw new NullPointerException();
    }
    this.content = content;
  }

  public ContentToken getContent()
  {
    return content;
  }

  protected void signalStart() throws NormalizationException
  {
    getRootFlow().getContentGenerator().addContent(this);
  }
}
