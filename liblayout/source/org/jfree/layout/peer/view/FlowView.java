/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * ${name}
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Boot.java,v 1.6 2003/11/23 16:50:45 taqua Exp $
 *
 * Changes
 * -------------------------
 * 14-May-2004 : Initial version
 *
 */
package org.jfree.layout.peer.view;

import org.jfree.layout.doc.DocumentNode;

/**
 * todo this implementation is incomplete
 * @see javax.swing.text.FlowView
 */
public abstract class FlowView extends BoxView
{
  private class LogicalView extends AbstractCompositeView
  {
    public LogicalView (final DocumentNode node)
    {
      super(node);
    }

    /**
     * This method is called whenever the size or other layouting preferences changed. When
     * this method is called, a valid size is already set.
     */
    protected void doLayout ()
    {

    }

    public NodeView getParent ()
    {
      return FlowView.super.getParent();
    }

    protected void performMajorAxisLayout ()
    {
      // purely logical container, no layouting
    }

    protected void performMinorAxisLayout ()
    {
      // purely logical container, no layouting
    }

    public int getMajorAxis ()
    {
      return FlowView.this.getMajorAxis();
    }

    public int getMinorAxis ()
    {
      return FlowView.this.getMinorAxis();
    }
  }

  private LogicalView logicalView;

  public FlowView (final DocumentNode node, final int axis)
  {
    super(node, axis);
    logicalView = new LogicalView(node);
    logicalView.setParent(this);
  }

  public float getMaximumSpan (int axis)
  {
    // todo implement me
    return super.getMaximumSpan(axis);
  }

  public float getMinimumSpan (int axis)
  {
    // todo implement me
    return super.getMinimumSpan(axis);
  }

  public float getPreferredSpan (int axis)
  {
    // todo implement me
    return super.getPreferredSpan(axis);
  }

  protected abstract NodeView createRow();

  public CompositeView getLogicalView ()
  {
    return logicalView;
  }
}
