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
 * $Id: ParagraphRenderBox.java,v 1.5 2006/07/18 14:40:28 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.renderer.model.alignment.Alignment;
import org.jfree.layouting.renderer.model.alignment.JustifyAlignment;
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

    public LineBoxRenderBox(final BoxDefinition boxDefinition)
    {
      super(boxDefinition);
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
    public PoolBox(final BoxDefinition boxDefinition)
    {
      super(boxDefinition);
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
  }

  public static final int ALIGN_LEFT = 0;
  public static final int ALIGN_RIGHT = 0;
  public static final int ALIGN_CENTER = 0;
  public static final int ALIGN_JUSTIFY = 0;

  private PoolBox pool;
  private LineBoxRenderBox lineboxContainer;
  private Alignment textAlignment;
  private Alignment lastLineAlignment;

  public ParagraphRenderBox(final BoxDefinition boxDefinition,
                            final Alignment textAlignment,
                            final Alignment lastLineAlignment)
  {
    super(boxDefinition);

    this.textAlignment = textAlignment;
    if (textAlignment instanceof JustifyAlignment)
    {
      this.lastLineAlignment = lastLineAlignment;
    }
    else
    {
      this.lastLineAlignment = textAlignment;
    }

    pool = new PoolBox(new EmptyBoxDefinition());
    pool.setParent(this);
    // yet another helper box. Level 2
    lineboxContainer = new LineBoxRenderBox(new EmptyBoxDefinition());
    lineboxContainer.setParent(this);
    // level 3 means: Add all lineboxes to the paragraph
  }

  public final void addChild(final RenderNode child)
  {
    pool.addChild(child);
  }

  protected void addDirectly(final RenderNode child)
  {
    setOpen(true);
    super.addChild(child);
    setOpen(false);
  }

  public void validate()
  {
    fillLineboxCollection();
    validateBorders();
    validateMargins();
    validatePaddings();

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
      Log.debug("Paragraph width is set to zero: Not processing childs");
      return;
    }

    // ok, now the tricky part. Lets build the real lines.
    // This code splits a logical line into one or more physical lines.
    RenderNode line = lineboxContainer.getFirstChild();
    long nodePos = getPosition(getMajorAxis()) + getLeadingInsets(getMajorAxis());


    final long width = getWidth();
    final long x = getPosition(getMinorAxis()) + getLeadingInsets(getMinorAxis());
    RenderNode[] target = new RenderNode[2];
    Log.debug("Performing validate on Paragraph: width=" + width + ", x=" + x);

    while (line != null)
    {
      Log.debug("Performing validate on Paragraph line: " + line);

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
          Log.debug("Empty box after the split. ");
          if (deadLockDanger == true)
          {
            throw new IllegalStateException("Infinite loop detected.");
          }
          deadLockDanger = true;
        }
        else
        {
          firstSplitNode.setPosition(getMinorAxis(), x);
          firstSplitNode.setPosition(getMajorAxis(), nodePos);
          firstSplitNode.setDimension(getMinorAxis(), width);
          firstSplitNode.validate();
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
          addDirectly(firstSplitNode);
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
            Log.debug("Made no progress at all: " + target[1]);
            addDirectly(target[1]);

            target[1].setPosition(getMinorAxis(), x);
            target[1].setPosition(getMajorAxis(), nodePos);
            target[1].setDimension(getMinorAxis(), width);
            target[1].validate();
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
    Log.debug("Paragraph: " + getHeight());
    setState(RenderNodeState.FINISHED);
  }

  private RenderNode[] performLinebreak(final RenderNode lineFragment,
                                        final long width,
                                        RenderNode[] target)
  {
    final long preferredSize = lineFragment.getPreferredSize(getMinorAxis());
    if (preferredSize <= width)
    {
      Log.debug("No Split needed.");
      target[0] = lineFragment.derive(true);
      target[1] = null;
    }
    else
    {
      final long pos = lineFragment.getBestBreak(getMinorAxis(), width);
      if (pos > 0)
      {
        Log.debug("Got a best break at " + pos);
        target = lineFragment.split(getMinorAxis(), pos, target);
      }
      else
      {
        // there is no 'best' split, so split after the first child
        final long firstSplit = lineFragment.getFirstBreak(getMinorAxis());
        Log.debug("Seeking the first break at " + firstSplit);

        int axis = getMinorAxis();
        Log.debug("       Extra Info: PS: " + lineFragment.getPreferredSize(axis));
        Log.debug("       Extra Info: FB: " + lineFragment.getFirstBreak(axis));
        Log.debug("       Extra Info: BP: " + lineFragment.getBestBreak(axis, width));
        Log.debug("       Extra Info:*BP: " + getBestBreak(axis, width));
        Log.debug("       Extra Info:*FP: " + getFirstBreak(axis));

        if (firstSplit == 0)
        {
          Log.debug("No Split possible.");
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


  private void fillLineboxCollection()
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
      int lineCount = 0;
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
          lineCount += 1;
          node = target[1];
        }
        while (node != null && node.isForcedSplitNeeded(node.getNext() == null));

        // add the last chunk, if that one does not need splitting anymore ..
        if (node != null)
        {
          lineCount += 1;
          lineboxContainer.addChild(node.derive(true));
        }
      }
      else
      {
        // oh cool, nothing to split. That makes it easy...
        if (pool.getFirstChild() != null)
        {
          lineCount += 1;
          lineboxContainer.addChild(pool.derive(true));
        }
      }
      lineboxContainer.setNotify(true);


      // now we have the lineboxes. These boxes are not the *physical*
      // lineboxes, nor to they deal with the clear property for floated
      // elements.
      //  Log.debug("Paragraph rebuilds lineboxes: " + lineCount + " lines created.");

      setState(RenderNodeState.LAYOUTING);
    }

  }

  public long getMinimumChunkSize(int axis)
  {
    fillLineboxCollection();
    return lineboxContainer.getMinimumChunkSize(axis);
  }

  public long getPreferredSize(int axis)
  {
    fillLineboxCollection();
    final long preferredSize = lineboxContainer.getPreferredSize(axis);
    return preferredSize;
  }

  /**
   * This needs to be adjusted to support vertical flows as well.
   *
   * @param axis
   * @param node
   * @return
   */
  protected long getEffectiveLayoutSize(int axis, RenderNode node)
  {
    fillLineboxCollection();
    final long preferredSize = lineboxContainer.getEffectiveLayoutSize(axis);
    Log.debug("Paragraph signals on EffLayoutSize: " + preferredSize);
    return preferredSize;
  }

  public RenderBox getInsertationPoint()
  {
    return pool.getInsertationPoint();
  }

  public RenderNode findNodeById(Object instanceId)
  {
    validate();
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

  /**
   * The reference point corresponds to the baseline of an box. For now, we
   * define only one reference point per box. The reference point of boxes
   * corresponds to the reference point of the first linebox.
   *
   * @param axis
   * @return
   */
  public long getReferencePoint(int axis)
  {
    fillLineboxCollection();
    return lineboxContainer.getReferencePoint(axis);
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
