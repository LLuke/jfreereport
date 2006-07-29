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
 * ParagraphRenderBox.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ParagraphRenderBox.java,v 1.10 2006/07/27 17:56:27 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
import org.jfree.layouting.input.style.keys.text.TextAlign;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.renderer.Loggers;
import org.jfree.layouting.renderer.model.alignment.Alignment;
import org.jfree.layouting.renderer.model.alignment.CenterAlignment;
import org.jfree.layouting.renderer.model.alignment.JustifyAlignment;
import org.jfree.layouting.renderer.model.alignment.LeadingEdgeAlignment;
import org.jfree.layouting.renderer.model.alignment.TrailingEdgeAlignment;
import org.jfree.util.Log;

/**
 * This articifial renderbox is the connection between block-contexts and the
 * sequences of consectual inline-boxes of that block.
 * <p/>
 * This renderbox generates lineboxes whenever needed.
 * <p/>
 * When asked for layout-sizes or when doing the layouting, it first checks its
 * validity and performs splits on all forced pagebreaks. At the end, there is
 * no inner element's edge with an activated clear-property.
 * <p/>
 * After that, it generates temporary lineboxes for all of its childs. When
 * *re*-computing the layout, these lineboxes get invalidated and merged back
 * into the paragraph.
 *
 * @author Thomas Morgner
 */
public class ParagraphRenderBox extends BlockRenderBox
{
  private static class LineBoxRenderBox extends BlockRenderBox
  {
    private boolean notify;

    public LineBoxRenderBox(final BoxDefinition boxDefinition,
                            final CSSValue valign)
    {
      super(boxDefinition, valign);
    }

    public boolean isNotify()
    {
      return notify;
    }

    public void setNotify(final boolean notify)
    {
      this.notify = notify;
    }

    /**
     * Propage all changes to all silbling nodes which come after this node and
     * to all childs.
     * <p/>
     * If this node is the last child, make the parent pending again.
     *
     * @param state
     */
    protected void notifyStateChange(final RenderNodeState oldState,
                                     final RenderNodeState newState)
    {
     // if (notify)
      {
        super.notifyStateChange(oldState, newState);
      }
    }

    protected void validateMargins()
    {
      super.validateMargins();
    }
  }

  private static class PoolBox extends InlineRenderBox
  {
    private boolean alwaysPropagate;

    public PoolBox(final BoxDefinition boxDefinition,
                   final CSSValue valign)
    {
      super(boxDefinition, valign);
      alwaysPropagate = true;
    }

    public void close()
    {
      if (isOpen() == false)
      {
        return;
      }

      RenderBox parent = getParent();
      super.close();
      if (parent != null)
      {
        parent.close();
      }
    }

    public void trim ()
    {
      // remove leading and trailing spacer ...
      RenderNode node = getFirstChild();
      while (node != null)
      {
        if (node.isIgnorableForRendering() == false)
        {
          node.setMarginsValidated(false);
          node.validateMargins();
          break;
        }
        remove(node);
        node = getFirstChild();
      }

      node = getLastChild();
      while (node != null)
      {
        if (node.isIgnorableForRendering() == false)
        {
          node.setMarginsValidated(false);
          node.validateMargins();
          break;
        }
        remove(node);
        node = getLastChild();
      }
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
      final PoolBox renderNode = (PoolBox) super.derive(deepDerive);
      renderNode.alwaysPropagate = false;
      return renderNode;
    }

    public boolean isAlwaysPropagateEvents()
    {
      return alwaysPropagate || super.isAlwaysPropagateEvents();
    }
  }

  private PoolBox pool;
  private LineBoxRenderBox lineboxContainer;
  private Alignment textAlignment;
  private Alignment lastLineAlignment;

  public ParagraphRenderBox(final BoxDefinition boxDefinition,
                            final LayoutContext context)
  {
    super(boxDefinition,
            context.getStyle().getValue(LineStyleKeys.VERTICAL_ALIGN));
    final CSSValue valign = getVerticalAlignment();
    CSSValue alignVal = context.getStyle().getValue(TextStyleKeys.TEXT_ALIGN);
    CSSValue alignLastVal = context.getStyle().getValue(TextStyleKeys.TEXT_ALIGN_LAST);
    this.textAlignment = createAlignment(alignVal);
    if (textAlignment instanceof JustifyAlignment)
    {
      this.lastLineAlignment = createAlignment(alignLastVal);
    }
    else
    {
      this.lastLineAlignment = textAlignment;
    }

    pool = new PoolBox(new EmptyBoxDefinition(), valign);
    pool.setParent(this);
    // yet another helper box. Level 2
    lineboxContainer = new LineBoxRenderBox(new EmptyBoxDefinition(), valign);
    lineboxContainer.setParent(this);
    // level 3 means: Add all lineboxes to the paragraph
  }

  private Alignment createAlignment (CSSValue value)
  {
    if (TextAlign.LEFT.equals(value) ||
        TextAlign.START.equals(value))
    {
      return new LeadingEdgeAlignment();
    }
    if (TextAlign.RIGHT.equals(value) ||
        TextAlign.END.equals(value))
    {
      return new TrailingEdgeAlignment();
    }
    if (TextAlign.CENTER.equals(value))
    {
      return new CenterAlignment();
    }
    if (TextAlign.JUSTIFY.equals(value))
    {
      return new JustifyAlignment();
    }
    return new LeadingEdgeAlignment();
  }


  public final void addChild(final RenderNode child)
  {
    pool.addChild(child);
  }

  protected void addDirectly(final RenderNode child)
  {
    if (child instanceof PoolBox)
    {
      PoolBox poolBox = (PoolBox) child;
      poolBox.trim();
    }
    super.addGeneratedChild(child);
  }

  public void validate(RenderNodeState upTo)
  {
    if (getState() == RenderNodeState.FINISHED)
    {
      return;
    }
    if (getState() == RenderNodeState.UNCLEAN)
    {
      validateBorders();
      validatePaddings();
      setState(RenderNodeState.PENDING);
    }

    if (reachedState(upTo))
    {
      return;
    }

    if (getState() == RenderNodeState.PENDING)
    {
      createLineboxCollection();
      validateMargins();
      setState(RenderNodeState.LAYOUTING);
    }

    if (reachedState(upTo))
    {
      return;
    }

    if (isIgnorableForRendering())
    {
      setHeight(0);
      setWidth(0);
      setState(RenderNodeState.FINISHED);
      return;
    }

    if (getWidth() == 0)
    {
      //throw new Error();
      Loggers.VALIDATION.debug("Paragraph width is set to zero: Not processing childs");
      return;
    }

    // ok, now the tricky part. Lets build the real lines.
    // This code splits a logical line into one or more physical lines.
    RenderNode line = lineboxContainer.getFirstChild();
    long nodePos = getPosition(getMajorAxis()) + getLeadingInsets(getMajorAxis());


    final long width = getWidth();
    final long x = getPosition(getMinorAxis()) + getLeadingInsets(getMinorAxis());
    RenderNode[] target = new RenderNode[2];
    Loggers.VALIDATION.debug("Performing validate on Paragraph: width=" + width + ", x=" + x);

    while (line != null)
    {
      Loggers.VALIDATION.debug("Performing validate on Paragraph line: " + line);

      // split the line unless there is nothing more to split ...
      RenderNode lineFragment = line;
      boolean deadLockDanger = false;

      long progress = Long.MAX_VALUE;
      while (lineFragment != null)
      {

        target = performLinebreak(lineFragment, width, target);

        final RenderNode firstSplitNode = target[0];
        if (firstSplitNode.isEmpty())
        {
          // Ups .. this could easily lead to an dead-lock.
          Loggers.VALIDATION.debug("Empty box after the split. ");
          if (deadLockDanger == true)
          {
            throw new IllegalStateException("Infinite loop detected.");
          }
          deadLockDanger = true;
        }
        else
        {
          addDirectly(firstSplitNode);

          firstSplitNode.setPosition(getMinorAxis(), x);
          firstSplitNode.setPosition(getMajorAxis(), nodePos);
          firstSplitNode.setDimension(getMinorAxis(), width);
          firstSplitNode.validate(RenderNodeState.FINISHED);
          nodePos += (firstSplitNode.getDimension(getMajorAxis()));
          final boolean overflow;
          if (target[1] == null)
          {
            // this will be the last line.
            overflow = lastLineAlignment.align(getMinorAxis(), firstSplitNode, width);
          }
          else
          {
            overflow = textAlignment.align(getMinorAxis(), firstSplitNode, width);
          }
        }

        if (target[1] != null)
        {
          long prefWidth = target[1].getPreferredSize(getMinorAxis());
          if (prefWidth < progress)
          {
            progress = prefWidth;
            lineFragment = target[1];
          }
          else
          {
            // no progress, add it as full-next line and hope the best ..
            Loggers.VALIDATION.debug("Made no progress at all: " + target[1]);
            addDirectly(target[1]);

            target[1].setPosition(getMinorAxis(), x);
            target[1].setPosition(getMajorAxis(), nodePos);
            target[1].setDimension(getMinorAxis(), width);
            target[1].validate(RenderNodeState.FINISHED);
            nodePos += target[1].getDimension(getMajorAxis());
            // this will be the last line.
            final boolean overflow =
                    lastLineAlignment.align(getMinorAxis(), target[1], width);
            lineFragment = null;
            deadLockDanger = false;
          }
        }
        else
        {
          deadLockDanger = false;
          lineFragment = null;
        }
      }

      line = line.getNext();
    }

    setHeight(nodePos - getY());
    Loggers.VALIDATION.debug("Paragraph: " + getHeight());
    pool.setState(RenderNodeState.FINISHED);
    setState(RenderNodeState.FINISHED);
  }

  private RenderNode[] performLinebreak(final RenderNode lineFragment,
                                        final long width,
                                        RenderNode[] target)
  {
    final long preferredSize = lineFragment.getPreferredSize(getMinorAxis());
    if (preferredSize <= width)
    {
      Loggers.SPLITSTRATEGY.debug("No Split needed.");
      target[0] = lineFragment.derive(true);
      target[1] = null;
    }
    else
    {
      final long pos = lineFragment.getBestBreak(getMinorAxis(), width);
      if (pos > 0)
      {
        Loggers.SPLITSTRATEGY.debug("Got a best break at " + pos);
        target = lineFragment.split(getMinorAxis(), pos, target);
      }
      else
      {
        // there is no 'best' split, so split after the first child
        final long firstSplit = lineFragment.getFirstBreak(getMinorAxis());
        Loggers.SPLITSTRATEGY.debug("Seeking the first break at " + firstSplit);

        int axis = getMinorAxis();
        Loggers.SPLITSTRATEGY.debug("       Extra Info: PS: " + lineFragment.getPreferredSize(axis));
        Loggers.SPLITSTRATEGY.debug("       Extra Info: FB: " + lineFragment.getFirstBreak(axis));
        Loggers.SPLITSTRATEGY.debug("       Extra Info: BP: " + lineFragment.getBestBreak(axis, width));
        Loggers.SPLITSTRATEGY.debug("       Extra Info:*BP: " + getBestBreak(axis, width));
        Loggers.SPLITSTRATEGY.debug("       Extra Info:*FP: " + getFirstBreak(axis));

        if (firstSplit == 0)
        {
          Loggers.SPLITSTRATEGY.debug("No Split possible.");
          target[0] = lineFragment.derive(true);
          target[1] = null;
        }
        else
        {
          target = lineFragment.split(getMinorAxis(), firstSplit, target);
        }
      }
    }
    return target;
  }


  /**
   * Removes all children.
   */
  public final void clear()
  {
    pool.clear();
    lineboxContainer.clear();
    super.clear();
  }

  protected final void clearDirectly()
  {
    super.clear();
  }


  private void createLineboxCollection()
  {
    final RenderNodeState state = getState();
    if (state == RenderNodeState.UNCLEAN || state == RenderNodeState.PENDING)
    {
      //

      // todo: Make this more efficient. There is no need to rebuild anything,
      // if there were no structural changes at all.
      clearDirectly();
      lineboxContainer.clear();
      lineboxContainer.setNotify(false);
//      int lineCount = 0;
      if (pool.isForcedSplitNeeded(true))
      {
        // lets get our hands dirty ...
        RenderNode[] target = null;
        RenderNode node = pool;
        do
        {
          // split until there is nothing more to split ..
          target = node.splitForLinebreak(true, target);
          lineboxContainer.addChild(target[0]);
//          lineCount += 1;
          node = target[1];
        }
        while (node != null && node.isForcedSplitNeeded(node.getNext() == null));

        // add the last chunk, if that one does not need splitting anymore ..
        if (node != null)
        {
//          lineCount += 1;
          lineboxContainer.addChild(node.derive(true));
        }
      }
      else
      {
        // oh cool, nothing to split. That makes it easy...
        if (pool.getFirstChild() != null)
        {
//          lineCount += 1;
          lineboxContainer.addChild(pool.derive(true));
        }
      }
      lineboxContainer.setNotify(true);


      // now we have the lineboxes. These boxes are not the *physical*
      // lineboxes, nor to they deal with the clear property for floated
      // elements.

      // todo: Never get called directly ..
      setState(RenderNodeState.LAYOUTING);
    }

  }

  public long getMinimumChunkSize(int axis)
  {
    validate(RenderNodeState.LAYOUTING);
    return lineboxContainer.getMinimumChunkSize(axis);
  }

  protected long getPreferredSize(int axis)
  {
    validate(RenderNodeState.LAYOUTING);
    return lineboxContainer.getPreferredSize(axis);
  }

  /**
   * This needs to be adjusted to support vertical flows as well.
   *
   * @param axis
   * @return
   */
  public long getEffectiveLayoutSize(int axis)
  {
    validate(RenderNodeState.LAYOUTING);
    return lineboxContainer.getEffectiveLayoutSize(axis);
  }

  public RenderBox getInsertationPoint()
  {
    return pool.getInsertationPoint();
  }

  public boolean isAppendable()
  {
    return pool.isAppendable();
  }

  public RenderNode findNodeById(Object instanceId)
  {
    validate(RenderNodeState.FINISHED);
    return super.findNodeById(instanceId);
  }

  public BreakAfterEnum getBreakAfterAllowed(final int axis)
  {
    return pool.getBreakAfterAllowed(axis);
  }

  public boolean isEmpty()
  {
    return pool.isEmpty();
  }

  public boolean isDiscardable()
  {
    return pool.isDiscardable();
  }

  public Alignment getLastLineAlignment()
  {
    return lastLineAlignment;
  }

  public Alignment getTextAlignment()
  {
    return textAlignment;
  }
}
