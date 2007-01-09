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
 * $Id: PageAreaRenderBox.java,v 1.2 2006/12/03 18:58:09 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.namespace.Namespaces;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.output.OutputProcessorMetaData;

/**
 * A renderbox, that represents a page-area. As our CSS-parser does not yet
 * handle @page declarations, we can ignore all advanced features and
 * concentrate on the plain content holding ability of this element.
 *
 * @author Thomas Morgner
 */
public class PageAreaRenderBox extends BlockRenderBox
{
  public PageAreaRenderBox(final BoxDefinition boxDefinition)
  {
    super(boxDefinition);
    final NodeLayoutProperties nodeLayoutProperties = getNodeLayoutProperties();
    nodeLayoutProperties.setNamespace(Namespaces.LIBLAYOUT_NAMESPACE);
    nodeLayoutProperties.setTagName("page-area");
  }

  public void appyStyle(LayoutContext context, OutputProcessorMetaData metaData)
  {
    super.appyStyle(context, metaData);
    final NodeLayoutProperties nodeLayoutProperties = getNodeLayoutProperties();
    nodeLayoutProperties.setNamespace(Namespaces.LIBLAYOUT_NAMESPACE);
    nodeLayoutProperties.setTagName("page-area");
  }

  public void setParent(RenderBox parent)
  {
    super.setParent(parent);
  }
}
