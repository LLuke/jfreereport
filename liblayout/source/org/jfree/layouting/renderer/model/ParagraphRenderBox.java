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
 * $Id: ParagraphRenderBox.java,v 1.1 2006/07/11 13:51:02 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model;

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
      if (notify)
      {
        super.notifyStateChange(oldState, newState);
      }
    }
  }

  private InlineRenderBox pool;
  private LineBoxRenderBox lineboxContainer;

  public ParagraphRenderBox(final BoxDefinition boxDefinition)
  {
    super(boxDefinition);
    pool = new InlineRenderBox(new EmptyBoxDefinition());
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
    super.addChild(child);
  }

  public void validate()
  {
    fillLineboxCollection();
    validateBorders();
    validateMargins();
    validatePaddings();

    // ok, now the tricky part. Lets build the real lines.
    // This code splits a logical line into one or more physical lines.
    RenderNode line = lineboxContainer.getFirstChild();
    ValidationStruct lineStruct = new ValidationStruct();
    lineStruct.setCursor(getY() + getTopInsets());

    final long width = getWidth();
    final long x = getX() + getLeftInsets();
    RenderNode[] target = new RenderNode[2];
    Log.debug("Performing validate on Paragraph: width=" + width + ", x=" + x);

    while (line != null)
    {
      lineStruct.setProgress(Long.MAX_VALUE);
      lineStruct.setNode(line);
      Log.debug("Performing validate on Paragraph line: " + line);

      // split the line unless there is nothing more to split ...
      RenderNode lineFragment = line;

      while (lineFragment != null)
      {
        final long preferredSize = lineFragment.getPreferredSize(getMinorAxis());
        if (preferredSize < width)
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
            target = lineFragment.split(getMinorAxis(), pos, target);
          }
          else
          {
            // there is no 'best' split, so split after the first child
            final long firstSplit = lineFragment.getFirstBreak(getMinorAxis());

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
              if (firstSplit == 395000)
              {
                lineFragment.getFirstBreak(getMinorAxis());
              }
              target = lineFragment.split(getMinorAxis(), firstSplit, target);
            }
          }
        }

        Log.debug(".");
        final RenderNode firstSplitNode = target[0];
        if (firstSplitNode.isEmpty())
        {
          // Ups ..
          Log.debug("Empty box after the split. ");
        }
        firstSplitNode.setX(x);
        firstSplitNode.setWidth(width);
        firstSplitNode.setY(lineStruct.getCursor());
        firstSplitNode.validate();
        lineStruct.addCursorPosition(firstSplitNode.getHeight());

        addDirectly(firstSplitNode);

        lineFragment = target[1];
        if (target[1] != null)
        {
          long prefWidth = target[1].getPreferredSize(getMinorAxis());
          if (prefWidth < lineStruct.getProgress())
          {
            lineStruct.setProgress(prefWidth);
          }
          else
          {
            // no progress, add it as full-next line and hope the best ..
            Log.debug("Made no progress at all: " + target[1]);
            addDirectly(target[1]);

            target[1].setX(x);
            target[1].setWidth(width);
            target[1].setY(lineStruct.getCursor());
            target[1].validate();
            lineStruct.addCursorPosition(target[1].getHeight());
            lineFragment = null;
          }
        }
      }

      line = line.getNext();
    }

    setHeight(lineStruct.getCursor() - getY());
    Log.debug("Paragraph: " + getHeight());
    setState(RenderNodeState.FINISHED);
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
      Log.debug("Paragraph rebuilds lineboxes: " + lineCount + " lines created.");

      setState(RenderNodeState.LAYOUTING);
    }

  }

  public long getMinimumChunkSize(int axis)
  {
    fillLineboxCollection();
    return lineboxContainer.getMinimumChunkSize(axis);
  }

  public long getMinimumSize(int axis)
  {
    fillLineboxCollection();
    return lineboxContainer.getMinimumSize(axis);
  }

  public long getPreferredSize(int axis)
  {
    fillLineboxCollection();
    return lineboxContainer.getPreferredSize(axis);
  }

  public long getMaximumSize(int axis)
  {
    fillLineboxCollection();
    return lineboxContainer.getMaximumSize(axis);
  }

  public RenderBox getInsertationPoint()
  {
    return pool.getInsertationPoint();
  }

  public boolean isOpen()
  {
    return pool.isOpen();
  }

  protected void setOpen(final boolean open)
  {
    pool.setOpen(open);
  }

  public void close()
  {
    pool.close();
  }

  public RenderNode findNodeById(Object instanceId)
  {
    validate();
    return super.findNodeById(instanceId);
  }

  public BreakAfterEnum getBreakAfterAllowed()
  {
    return pool.getBreakAfterAllowed();
  }

  public boolean isEmpty()
  {
    return pool.getFirstChild() == null;
  }
}
