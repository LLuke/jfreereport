/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * FlowBoxDefinition.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: NormalFlowRenderBox.java,v 1.8 2006/07/27 17:56:27 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model;

import java.util.ArrayList;

import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.util.Log;

/**
 * A box that defines its own normal flow. All absolutly positioned or
 * floating elements define their own normal flow.
 *
 * Each flow contains an invisible place-holder element, which marks the
 * position of that element in the parent's normal-flow.
 *
 * A flow may hold a set of sub-flows. Sub-Flows are derived from floating
 * elements. Absolutely positioned elements are placed on the page context.
 *
 * Normal-flows are derived for each absolutly or staticly positioned element.
 *
 * @author Thomas Morgner
 */
public class NormalFlowRenderBox extends BlockRenderBox
{
  private Object placeHolderId;
  private SpacerRenderNode placeHolder;
  private ArrayList subFlows;

  public NormalFlowRenderBox(final BoxDefinition boxDefinition,
                             final CSSValue valign)
  {
    super(boxDefinition, valign);
    placeHolder = new SpacerRenderNode(0, 0, true);
    placeHolderId = getPlaceHolder().getInstanceId();
    subFlows = new ArrayList();

    // hardcoded for now, content forms lines, which flow from top to bottom
    // and each line flows horizontally (later with support for LTR and RTL)
  }

  public SpacerRenderNode getPlaceHolder()
  {
    return placeHolder;
  }

  public void addFlow (NormalFlowRenderBox flow)
  {
    subFlows.add(flow);
  }

  public NormalFlowRenderBox[] getFlows ()
  {
    return (NormalFlowRenderBox[])
            subFlows.toArray(new NormalFlowRenderBox[subFlows.size()]);
  }

  public NormalFlowRenderBox getFlow (int i)
  {
    return (NormalFlowRenderBox) subFlows.get(i);
  }

  public int getFlowCount()
  {
    return subFlows.size();
  }

  /**
   * Derive creates a disconnected node that shares all the properties of the
   * original node. The derived node will no longer have any parent, silbling,
   * child or any other relationships with other nodes.
   *
   * @return
   */
  public RenderNode derive(boolean deepDerive)
  {
    NormalFlowRenderBox renderBox = (NormalFlowRenderBox) super.derive(deepDerive);
    if (deepDerive)
    {
      renderBox.subFlows = new ArrayList(subFlows.size());
      for (int i = 0; i < subFlows.size(); i++)
      {
        NormalFlowRenderBox box = (NormalFlowRenderBox) subFlows.get(i);
        renderBox.subFlows.add(box.derive(true));

        box.placeHolder = (SpacerRenderNode) findNodeById(box.placeHolderId);
      }
      renderBox.placeHolder = null;
    }
    else
    {
      renderBox.placeHolder = (SpacerRenderNode) placeHolder.derive(true);
      renderBox.subFlows = new ArrayList();
    }
    return renderBox;
  }

  /**
   * Derive creates a disconnected node that shares all the properties of the
   * original node. The derived node will no longer have any parent, silbling,
   * child or any other relationships with other nodes.
   *
   * @return
   */
  public RenderNode deriveFrozen(boolean deepDerive)
  {
    NormalFlowRenderBox renderBox = (NormalFlowRenderBox) super.deriveFrozen(deepDerive);
    if (deepDerive)
    {
      renderBox.subFlows = new ArrayList(subFlows.size());
      for (int i = 0; i < subFlows.size(); i++)
      {
        NormalFlowRenderBox box = (NormalFlowRenderBox) subFlows.get(i);
        renderBox.subFlows.add(box.deriveFrozen(true));

        box.placeHolder = (SpacerRenderNode) findNodeById(box.placeHolderId);
      }
      renderBox.placeHolder = null;
    }
    else
    {
      renderBox.placeHolder = (SpacerRenderNode) placeHolder.deriveFrozen(true);
      renderBox.subFlows = new ArrayList();
    }
    return renderBox;
  }

  public RenderBox getInsertationPoint()
  {
    for (int i = 0; i < subFlows.size(); i++)
    {
      NormalFlowRenderBox box = (NormalFlowRenderBox) subFlows.get(i);
      if (box.isOpen())
      {
        return box.getInsertationPoint();
      }
    }
    return super.getInsertationPoint();
  }

  /**
   * Checks, whether a validate run would succeed. Under certain conditions, for
   * instance if there is a auto-width component open, it is not possible to
   * perform a layout run, unless that element has been closed.
   * <p/>
   * Generally speaking: An element cannot be layouted, if <ul> <li>the element
   * contains childs, which cannot be layouted,</li> <li>the element has
   * auto-width or depends on an auto-width element,</li> <li>the element is a
   * floating or positioned element, or is a child of an floating or positioned
   * element.</li> </ul>
   *
   * @return
   */
  public boolean isValidatable()
  {
    if (isOpen() == false)
    {
      return true;
    }

    for (int i = 0; i < subFlows.size(); i++)
    {
      NormalFlowRenderBox box = (NormalFlowRenderBox) subFlows.get(i);
      if (box.isOpen())
      {
        Log.debug ("Not validatable: Open SubFlow");
        return false;
      }
    }

    // the root flow always has the width of 100% ..
    final RenderBox parent = getParent();
    if (parent instanceof LogicalPageBox == false)
    {
      if (RenderLength.AUTO.equals(getBoxDefinition().getPreferredWidth()))
      {
        Log.debug ("Not validatable: NormalFlow is an Auto-Width Box");
        return false;
      }
    }

    RenderNode child = getLastChild();
    while (child != null)
    {
      if (child.isValidatable() == false)
      {
        return false;
      }
      child = child.getPrev();
    }

    return true;
  }

  public NormalFlowRenderBox getNormalFlow()
  {
    return this;
  }

  public void validate(RenderNodeState upTo)
  {
    super.validate(upTo);
    for (int i = 0; i < subFlows.size(); i++)
    {
      NormalFlowRenderBox box = (NormalFlowRenderBox) subFlows.get(i);
      if (box.isOpen() == false)
      {
        box.validate(upTo);
      }
    }

    Log.debug ("VALIDATE ON NOF " + upTo);
    setState(upTo);
  }

  public long getEffectiveLayoutSize (int axis)
  {
    return getPreferredSize(axis);
  }

}
