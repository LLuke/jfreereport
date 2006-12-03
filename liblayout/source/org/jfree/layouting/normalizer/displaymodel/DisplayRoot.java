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

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.normalizer.generator.ContentGenerator;

/**
 * The root is the first element in a display-model-tree. Although it is
 * yet another flow, it connects the tree to the outside world, as it
 * holds the references to the current layout process and the renderer.
 *
 * @author Thomas Morgner
 */
public class DisplayRoot extends DisplayFlowElement
{
  private LayoutProcess layoutProcess;
  private ContentGenerator contentGenerator;

  public DisplayRoot(final LayoutContext context)
  {
    super(context);
  }

  public LayoutProcess getLayoutProcess()
  {
    return layoutProcess;
  }

  public void setLayoutProcess(final LayoutProcess layoutProcess)
  {
    this.layoutProcess = layoutProcess;
  }

  public ContentGenerator getContentGenerator()
  {
    return contentGenerator;
  }

  public void setContentGenerator(final ContentGenerator contentGenerator)
  {
    this.contentGenerator = contentGenerator;
  }

  public DisplayRoot getRootFlow()
  {
    return this;
  }

  public Object clone() throws CloneNotSupportedException
  {
    DisplayRoot dfe = (DisplayRoot) super.clone();
    dfe.contentGenerator = null;
    dfe.layoutProcess = null;
    return dfe;
  }



}
